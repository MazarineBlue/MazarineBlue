/*
 * Copyright (c) 2016 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.mazarineblue.mbt.gui;

import static java.awt.EventQueue.invokeLater;
import java.lang.reflect.InvocationTargetException;
import static java.util.Arrays.asList;
import java.util.List;
import java.util.concurrent.TimeoutException;
import javax.swing.JLabel;
import javax.swing.JPanel;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import static org.mazarineblue.mbt.gui.StringConstants.AFTER_STATE_DOESNT_SHARE_VIEW;
import static org.mazarineblue.mbt.gui.StringConstants.BEFORE_STATE_DOESNT_SHARE_VIEW;
import static org.mazarineblue.mbt.gui.StringConstants.BUSINESS_VALUE_MAX;
import static org.mazarineblue.mbt.gui.StringConstants.CANT_BE_BLANK;
import static org.mazarineblue.mbt.gui.StringConstants.INVALID_CHARACTERS_USED;
import static org.mazarineblue.mbt.gui.StringConstants.IS_ALREADY_ADDED;
import org.mazarineblue.mbt.gui.model.State;
import org.mazarineblue.mbt.gui.model.Transition;
import org.mazarineblue.mbt.gui.util.TransitonDialogTestHelper;
import static org.mazarineblue.swing.SwingUtil.fetchChildNamed;
import static org.mazarineblue.swing.SwingUtil.waitFor;
import static org.mazarineblue.swing.SwingUtil.waitUntilFalse;
import static org.mazarineblue.swing.SwingUtil.waitUntilTrue;

public class TransitonDialogTest
        extends TransitonDialogTestHelper {

    private void assertItem(String expected, int index) {
        @SuppressWarnings("unchecked")
        JPanel panel = transitionPage.getBeforeStateComponent(index, JPanel.class);
        JLabel label = fetchChildNamed(panel, "itemLabel", JLabel.class);
        assertEquals(expected, label.getText());
    }

    @Test
    public void initialDialog()
            throws TimeoutException {
        assertTrue(transitionPage.getName().isEmpty());
        assertFalse(transitionPage.isNameErrorVisible());
        assertFalse(transitionPage.isBeforeStateOptionEditable());
        assertEquals(-1, transitionPage.getBeforeStateSelectedIndex());
        assertFalse(transitionPage.isBeforeStateErrorVisible());
        assertEquals(0, transitionPage.getBeforeStateCount());
        assertFalse(transitionPage.isAfterStateOptionEditable());
        assertEquals(-1, transitionPage.getAfterStateSelectedIndex());
        assertFalse(transitionPage.isAfterStateErrorVisible());
        assertTrue(transitionPage.getAction().isEmpty());
    }

    @Test
    public void name_EmptyInput()
            throws TimeoutException, InterruptedException, InvocationTargetException {
        transitionPage.focusOnAction();
        waitUntilTrue(transitionPage::isNameErrorVisible, 500);
        assertTrue(transitionPage.getName().isEmpty());
        assertEquals(CANT_BE_BLANK, transitionPage.getNameErrorMessage());
    }

    @Test
    public void name_InvalidInput()
            throws TimeoutException, InterruptedException, InvocationTargetException {
        transitionPage.setNameErrorVisible(false);
        waitUntilFalse(transitionPage::isNameErrorVisible, 500);

        transitionPage.focusOnName();
        transitionPage.setName(INVALID_CHARACTERS_REGULAR);
        transitionPage.focusOnAction();

        waitUntilTrue(transitionPage::isNameErrorVisible, 500);
        assertEquals(INVALID_CHARACTERS_USED, transitionPage.getNameErrorMessage());
    }

    @Test
    public void name_ValidInput()
            throws InterruptedException, InvocationTargetException {
        transitionPage.focusOnName();
        transitionPage.setName("_State 1-2");
        transitionPage.focusOnAction();
        assertFalse(transitionPage.isNameErrorVisible());
    }

    @Test
    public void guard_Invalid()
            throws TimeoutException, InterruptedException, InvocationTargetException {
        transitionPage.setName("Test Name");
        transitionPage.setGuardErrorVisible(false);
        waitUntilFalse(transitionPage::isGuardErrorVisible, 500);

        transitionPage.isNameErrorVisible();
        transitionPage.setGuard(INVALID_CHARACTERS_VARIABLE);
        transitionPage.focusOnAction();

        waitUntilTrue(transitionPage::isGuardErrorVisible, 500);
        assertEquals(INVALID_CHARACTERS_USED, transitionPage.getGuardErrorMessage());
    }

    @Test
    public void guard_Valid()
            throws InterruptedException, InvocationTargetException {
        transitionPage.isNameErrorVisible();
        transitionPage.setGuard(VALID_CHARACTERS_VARIABLE);
        transitionPage.focusOnAction();
        assertFalse(transitionPage.isGuardErrorVisible());
    }

    @Test
    public void beforeState_AddOnce()
            throws TimeoutException, InterruptedException, InvocationTargetException {
        transitionPage.addBeforeState(STATE_D_INDEX);
        assertEquals(1, transitionPage.getBeforeStateCount());
        assertItem(STATE_D, 0);
        assertFalse(transitionPage.isBeforeStateErrorVisible());
    }

    @Test
    public void beforeState_AddTwice()
            throws TimeoutException, InterruptedException, InvocationTargetException {
        transitionPage.addBeforeState(STATE_D_INDEX);
        transitionPage.addBeforeState(STATE_D_INDEX);

        waitUntilTrue(transitionPage::isBeforeStateErrorVisible, 500);
        assertEquals(1, transitionPage.getBeforeStateCount());
        assertItem(STATE_D, 0);
        assertEquals(IS_ALREADY_ADDED, transitionPage.getBeforeStateErrorMessage());
    }

    @Test
    public void beforeState_StateB_StateA_Happy()
            throws TimeoutException, InterruptedException, InvocationTargetException {
        transitionPage.setAfterStateSelectedIndex(STATE_A_INDEX);
        transitionPage.addBeforeState(STATE_B_INDEX);

        assertEquals(1, transitionPage.getBeforeStateCount());
        assertItem(STATE_B, 0);
        assertFalse(transitionPage.isBeforeStateErrorVisible());
    }

    @Test
    public void beforeState_StateB_StateB_Happy()
            throws TimeoutException, InterruptedException, InvocationTargetException {
        transitionPage.setAfterStateSelectedIndex(STATE_B_INDEX);
        transitionPage.addBeforeState(STATE_B_INDEX);

        assertEquals(1, transitionPage.getBeforeStateCount());
        assertItem(STATE_B, 0);
        assertFalse(transitionPage.isBeforeStateErrorVisible());
    }

    @Test
    public void beforeState_StateC_StateB_Rainy()
            throws TimeoutException, InterruptedException, InvocationTargetException {
        transitionPage.setAfterStateSelectedIndex(STATE_B_INDEX);
        transitionPage.addBeforeState(STATE_E_INDEX);

        waitUntilTrue(transitionPage::isBeforeStateErrorVisible, 500);
        assertEquals(0, transitionPage.getBeforeStateCount());
        assertEquals(BEFORE_STATE_DOESNT_SHARE_VIEW, transitionPage.getBeforeStateErrorMessage());
    }

    @Test
    public void beforeState_StateE_StateB_Rainy()
            throws TimeoutException, InterruptedException, InvocationTargetException {
        transitionPage.setAfterStateSelectedIndex(STATE_B_INDEX);
        transitionPage.addBeforeState(STATE_E_INDEX);

        waitUntilTrue(transitionPage::isBeforeStateErrorVisible, 500);
        assertEquals(0, transitionPage.getBeforeStateCount());
        assertEquals(BEFORE_STATE_DOESNT_SHARE_VIEW, transitionPage.getBeforeStateErrorMessage());
    }

    @Test
    public void beforeState_StateE_StateE_Happy()
            throws TimeoutException, InterruptedException, InvocationTargetException {
        transitionPage.setAfterStateSelectedIndex(STATE_E_INDEX);
        transitionPage.addBeforeState(STATE_E_INDEX);

        assertEquals(1, transitionPage.getBeforeStateCount());
        assertItem(STATE_E, 0);
        assertFalse(transitionPage.isBeforeStateErrorVisible());
    }

    @Test
    public void afterState_StateA_StateA_Happy()
            throws TimeoutException, InterruptedException, InvocationTargetException {
        transitionPage.addBeforeState(STATE_A_INDEX);
        transitionPage.setAfterStateSelectedIndex(STATE_A_INDEX);

        assertEquals(1, transitionPage.getBeforeStateCount());
        assertItem(STATE_A, 0);
        assertFalse(transitionPage.isAfterStateErrorVisible());
    }

    @Test
    public void afterState_StateABC_StateA_Happy()
            throws TimeoutException, InterruptedException, InvocationTargetException {
        transitionPage.addBeforeState(STATE_A_INDEX);
        transitionPage.addBeforeState(STATE_B_INDEX);
        transitionPage.addBeforeState(STATE_C_INDEX);
        transitionPage.setAfterStateSelectedIndex(STATE_A_INDEX);

        assertEquals(3, transitionPage.getBeforeStateCount());
        assertItem(STATE_A, 0);
        assertItem(STATE_B, 1);
        assertItem(STATE_C, 2);
        assertFalse(transitionPage.isAfterStateErrorVisible());
    }

    @Test
    public void afterState_StateBC_StateA_Happy()
            throws TimeoutException, InterruptedException, InvocationTargetException {
        transitionPage.addBeforeState(STATE_B_INDEX);
        transitionPage.addBeforeState(STATE_C_INDEX);
        transitionPage.setAfterStateSelectedIndex(STATE_A_INDEX);

        assertEquals(2, transitionPage.getBeforeStateCount());
        assertItem(STATE_B, 0);
        assertItem(STATE_C, 1);
        assertFalse(transitionPage.isAfterStateErrorVisible());
    }

    @Test
    public void afterState_StateABC_StateB_Rainy()
            throws TimeoutException, InterruptedException, InvocationTargetException {
        invokeLater(() -> dialog.setVisible(true));
        transitionPage.addBeforeState(STATE_A_INDEX);
        transitionPage.addBeforeState(STATE_B_INDEX);
        transitionPage.addBeforeState(STATE_C_INDEX);
        transitionPage.setAfterStateSelectedIndex(STATE_B_INDEX);

        waitUntilTrue(transitionPage::isAfterStateErrorVisible, 500);
        assertEquals(3, transitionPage.getBeforeStateCount());
        assertItem(STATE_A, 0);
        assertItem(STATE_B, 1);
        assertItem(STATE_C, 2);
        assertEquals(AFTER_STATE_DOESNT_SHARE_VIEW, transitionPage.getAfterStateErrorMessage());
    }

    @Test
    public void afterState_StateBC_StateB_Rainy()
            throws TimeoutException, InterruptedException, InvocationTargetException {
        transitionPage.addBeforeState(STATE_B_INDEX);
        transitionPage.addBeforeState(STATE_C_INDEX);
        transitionPage.setAfterStateSelectedIndex(STATE_B_INDEX);

        waitUntilTrue(transitionPage::isAfterStateErrorVisible, 500);
        assertEquals(2, transitionPage.getBeforeStateCount());
        assertItem(STATE_B, 0);
        assertItem(STATE_C, 1);
        assertEquals(AFTER_STATE_DOESNT_SHARE_VIEW, transitionPage.getAfterStateErrorMessage());
    }

    @Test
    public void afterState_StateC_StateB_Rainy()
            throws TimeoutException, InterruptedException, InvocationTargetException {
        transitionPage.addBeforeState(STATE_C_INDEX);
        transitionPage.setAfterStateSelectedIndex(STATE_B_INDEX);

        waitUntilTrue(transitionPage::isAfterStateErrorVisible, 500);
        assertEquals(1, transitionPage.getBeforeStateCount());
        assertItem(STATE_C, 0);
        assertEquals(AFTER_STATE_DOESNT_SHARE_VIEW, transitionPage.getAfterStateErrorMessage());
    }

    @Test
    public void afterState_StateE_StateE_Happy()
            throws TimeoutException, InterruptedException, InvocationTargetException {
        transitionPage.addBeforeState(STATE_E_INDEX);
        transitionPage.setAfterStateSelectedIndex(STATE_E_INDEX);

        assertEquals(1, transitionPage.getBeforeStateCount());
        assertItem(STATE_E, 0);
        assertFalse(transitionPage.isAfterStateErrorVisible());
    }

    @Test
    public void afterState_StateE_StateB_Rainy()
            throws TimeoutException, InterruptedException, InvocationTargetException {
        transitionPage.addBeforeState(STATE_E_INDEX);
        transitionPage.setAfterStateSelectedIndex(STATE_B_INDEX);

        waitUntilTrue(transitionPage::isAfterStateErrorVisible, 500);
        assertEquals(1, transitionPage.getBeforeStateCount());
        assertItem(STATE_E, 0);
        assertEquals(AFTER_STATE_DOESNT_SHARE_VIEW, transitionPage.getAfterStateErrorMessage());
    }

    @Test
    public void accept_WrongInput1()
            throws TimeoutException, InterruptedException, InvocationTargetException {
        transitionPage.setName(INVALID_CHARACTERS_REGULAR);
        transitionPage.setGuard(INVALID_CHARACTERS_VARIABLE);
        transitionPage.setAfterStateSelectedIndex(STATE_D_INDEX);
        transitionPage.addBeforeState(STATE_A_INDEX);
        transitionPage.setAction("Test Actions");
        transitionPage.accept();

        waitUntilTrue(transitionPage::isNameErrorVisible, 500);
        waitUntilTrue(transitionPage::isGuardErrorVisible, 500);
        waitUntilTrue(transitionPage::isBeforeStateErrorVisible, 500);
        assertFalse(transitionPage.isAfterStateErrorVisible());
        assertEquals(INVALID_CHARACTERS_USED, transitionPage.getNameErrorMessage());
        assertEquals(INVALID_CHARACTERS_USED, transitionPage.getGuardErrorMessage());
        assertEquals(BEFORE_STATE_DOESNT_SHARE_VIEW, transitionPage.getBeforeStateErrorMessage());
    }

    @Test
    public void accept_WrongInput2()
            throws TimeoutException, InterruptedException, InvocationTargetException {
        transitionPage.setName(INVALID_CHARACTERS_REGULAR);
        transitionPage.setGuard(INVALID_CHARACTERS_VARIABLE);
        transitionPage.addBeforeState(STATE_A_INDEX);
        transitionPage.setAfterStateSelectedIndex(STATE_D_INDEX);
        transitionPage.setAction("Test Actions");
        waitFor(() -> fetchChildNamed(dialog, "itemLabel", JLabel.class), 500);
        transitionPage.accept();

        waitUntilTrue(transitionPage::isNameErrorVisible, 500);
        waitUntilTrue(transitionPage::isGuardErrorVisible, 500);
        waitUntilTrue(transitionPage::isAfterStateErrorVisible, 500);
        assertFalse(transitionPage.isBeforeStateErrorVisible());
        assertEquals(INVALID_CHARACTERS_USED, transitionPage.getNameErrorMessage());
        assertEquals(INVALID_CHARACTERS_USED, transitionPage.getGuardErrorMessage());
        assertEquals(AFTER_STATE_DOESNT_SHARE_VIEW, transitionPage.getAfterStateErrorMessage());
    }

    @Test
    public void accept()
            throws TimeoutException, InterruptedException, InvocationTargetException {
        transitionPage.setName("Test Transition");
        transitionPage.setGuard(VALID_CHARACTERS_VARIABLE);
        transitionPage.setBusinessValue(BUSINESS_VALUE_MAX);
        transitionPage.setAfterStateSelectedIndex(STATE_B_INDEX);
        transitionPage.addBeforeState(STATE_A_INDEX);
        transitionPage.setAction("Test Action");
        waitFor(() -> fetchChildNamed(dialog, "itemLabel", JLabel.class), 500);
        transitionPage.accept();

        Transition newTransition = okSpy.getNewState();
        List<State> sources = newTransition.getSources();
        assertNull(okSpy.getOldState());
        assertEquals("Test Transition", newTransition.getName());
        assertEquals(VALID_CHARACTERS_VARIABLE, newTransition.getGuard());
        assertEquals(BUSINESS_VALUE_MAX, newTransition.getBusinessValue());
        assertEquals(1, sources.size());
        assertEquals(STATE_A, sources.iterator().next().getName());
        assertEquals(STATE_B, newTransition.getDestination().getName());
        assertEquals("Test Action", newTransition.getAction());
    }

    @Test
    public void setOld()
            throws TimeoutException, InterruptedException, InvocationTargetException {
        transitionPage.setNameErrorVisible(true);
        transitionPage.setGuardErrorVisible(true);
        transitionPage.setBeforeStateErrorVisible(true);
        transitionPage.setAfterStateErrorVisible(true);

        State stateA = State.createDefault("State A").addViews("View 1", "View 2").setAction("State Action");
        State stateB = State.createDefault("State B").addViews("View 1", "View 2").setAction("State Action");
        Transition transition = Transition.createDefault("Name").setSources(stateA, stateB).setDestination(stateA);
        transition.setGuard("Guard").setBusinessValue(BUSINESS_VALUE_MAX).setAction("Transition Action");
        dialog.setOptions(asList(stateA, stateB));
        dialog.setOld(transition);

        assertFalse(transitionPage.isNameErrorVisible());
        assertFalse(transitionPage.isGuardErrorVisible());
        assertFalse(transitionPage.isBeforeStateErrorVisible());
        assertFalse(transitionPage.isAfterStateErrorVisible());
        assertEquals("Name", transitionPage.getName());
        assertEquals("Guard", transitionPage.getGuard());
        assertEquals(BUSINESS_VALUE_MAX, transitionPage.getBusinessValue());
        assertEquals(2, transitionPage.getBeforeStateOptionCount());
        assertEquals(stateA, transitionPage.getBeforeStateOption(0));
        assertEquals(stateB, transitionPage.getBeforeStateOption(1));
        assertEquals(2, transitionPage.getBeforeStateCount());
        assertEquals(stateA, transitionPage.getBeforeState(0));
        assertEquals(stateB, transitionPage.getBeforeState(1));
        assertEquals(stateA, transitionPage.getAfterStateSelectedOption());
        assertEquals("Transition Action", transitionPage.getAction());
    }

    @Test
    public void setOptions()
            throws TimeoutException {
        State stateA = State.createDefault("State A").addViews("View 1", "View 2").setAction("State Action");
        State stateB = State.createDefault("State B").addViews("View 1", "View 2").setAction("State Action");
        dialog.setOptions(asList(stateA, stateB));

        assertEquals(2, transitionPage.getBeforeStateOptionCount());
        assertEquals(stateA, transitionPage.getBeforeStateOption(0));
        assertEquals(stateB, transitionPage.getBeforeStateOption(1));
        assertEquals(0, transitionPage.getBeforeStateCount());
    }
}
