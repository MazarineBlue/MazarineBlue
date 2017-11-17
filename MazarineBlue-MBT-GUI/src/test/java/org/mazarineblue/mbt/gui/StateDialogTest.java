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

import java.lang.reflect.InvocationTargetException;
import static java.util.Arrays.asList;
import java.util.Collection;
import java.util.concurrent.TimeoutException;
import javax.swing.JLabel;
import javax.swing.JPanel;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import static org.mazarineblue.mbt.gui.StringConstants.CANT_BE_BLANK;
import static org.mazarineblue.mbt.gui.StringConstants.INVALID_CHARACTERS_USED;
import static org.mazarineblue.mbt.gui.StringConstants.IS_ALREADY_ADDED;
import org.mazarineblue.mbt.gui.model.State;
import org.mazarineblue.mbt.gui.util.StateDialogTestHelper;
import static org.mazarineblue.swing.SwingUtil.fetchChildNamed;
import static org.mazarineblue.swing.SwingUtil.waitFor;
import static org.mazarineblue.swing.SwingUtil.waitUntilFalse;
import static org.mazarineblue.swing.SwingUtil.waitUntilTrue;

public class StateDialogTest
        extends StateDialogTestHelper {

    @Test
    public void initialDialog()
            throws TimeoutException {
        assertTrue(statePage.getName().isEmpty());
        assertFalse(statePage.isNameErrorVisible());
        assertTrue(statePage.isOptionEditable());
        assertEquals(-1, statePage.getSelectedIndex());
        assertFalse(statePage.isViewErrorVisible());
        assertEquals(0, statePage.getViewCount());
        assertTrue(statePage.getAction().isEmpty());
    }

    @Test
    public void name_EmptyInput()
            throws TimeoutException, InterruptedException, InvocationTargetException {
        statePage.focusOnAction();
        waitUntilTrue(statePage::isNameErrorVisible, 500);
        assertTrue(statePage.getName().isEmpty());
        assertEquals(CANT_BE_BLANK, statePage.getNameErrorMessage());
    }

    @Test
    public void name_InvalidInput()
            throws TimeoutException, InterruptedException, InvocationTargetException {
        statePage.setNameErrorVisible(false);
        waitUntilFalse(statePage::isNameErrorVisible, 500);

        statePage.focusOnName();
        statePage.setName(INVALID_CHARACTERS);
        statePage.focusOnAction();

        waitUntilTrue(statePage::isNameErrorVisible, 500);
        assertEquals(INVALID_CHARACTERS_USED, statePage.getNameErrorMessage());
    }

    @Test
    public void name_ValidInput()
            throws TimeoutException, InterruptedException, InvocationTargetException {
        statePage.focusOnName();
        statePage.setName("_State 1-2");
        statePage.focusOnAction();
        assertFalse(statePage.isNameErrorVisible());
    }

    @Test
    public void views_EmptyInput()
            throws TimeoutException, InterruptedException, InvocationTargetException {
        statePage.addView("");

        assertEquals(0, statePage.getOptionCount());
        assertEquals(0, statePage.getViewCount());
        assertTrue(statePage.isViewErrorVisible());
        assertEquals(CANT_BE_BLANK, statePage.getViewErrorMessage());
    }

    @Test
    public void views_InvalidInput()
            throws TimeoutException, InterruptedException, InvocationTargetException {
        statePage.setViewErrorVisible(false);
        waitUntilFalse(statePage::isViewErrorVisible, 500);

        statePage.focusOnOptions();
        statePage.setSelectedOption(INVALID_CHARACTERS);
        statePage.addView();
        waitUntilTrue(statePage::isViewErrorVisible, 500);

        assertEquals(0, statePage.getOptionCount());
        assertEquals(0, statePage.getViewCount());
        assertEquals(INVALID_CHARACTERS_USED, statePage.getViewErrorMessage());
    }

    @Test
    public void views_AddOnce()
            throws TimeoutException, InterruptedException, InvocationTargetException {
        statePage.setSelectedOption("Test view");
        statePage.addView();

        @SuppressWarnings("unchecked")
        JPanel panel = statePage.getViewComponent(0, JPanel.class);
        JLabel label = fetchChildNamed(panel, "itemLabel", JLabel.class);
        assertEquals(1, statePage.getOptionCount());
        assertEquals(1, statePage.getViewCount());
        assertEquals("Test view", statePage.getOption(0));
        assertEquals("Test view", label.getText());
        assertFalse(statePage.isViewErrorVisible());
    }

    @Test
    public void views_AddTwice()
            throws TimeoutException, InterruptedException, InvocationTargetException {
        statePage.setSelectedOption("Test view");
        statePage.addView();
        statePage.addView();

        waitUntilTrue(statePage::isViewErrorVisible, 500);
        @SuppressWarnings("unchecked")
        JPanel panel = statePage.getViewComponent(0, JPanel.class);
        JLabel label = fetchChildNamed(panel, "itemLabel", JLabel.class);
        assertEquals(1, statePage.getOptionCount());
        assertEquals(1, statePage.getViewCount());
        assertEquals("Test view", statePage.getOption(0));
        assertEquals("Test view", label.getText());
        assertEquals(IS_ALREADY_ADDED, statePage.getViewErrorMessage());
    }

    @Test
    public void accept_WrongInput()
            throws TimeoutException, InterruptedException, InvocationTargetException {
        statePage.setName(INVALID_CHARACTERS);
        statePage.setSelectedOption("Test View");
        statePage.addView();
        statePage.setAction("Test Actions");
        waitFor(() -> fetchChildNamed(statePage.dialog, "itemLabel", JLabel.class), 500);
        statePage.accept();

        waitUntilTrue(statePage::isNameErrorVisible, 500);
        assertEquals(INVALID_CHARACTERS_USED, statePage.getNameErrorMessage());
    }

    @Test
    public void accept()
            throws TimeoutException, InterruptedException, InvocationTargetException {
        statePage.setName("Test State");
        statePage.setSelectedOption("Test View");
        statePage.addView();
        statePage.setAction("Test Action");
        waitFor(() -> fetchChildNamed(statePage.dialog, "itemLabel", JLabel.class), 500);
        statePage.accept();

        State newState = okSpy.getNewState();
        Collection<String> views = newState.getViews();
        assertNull(okSpy.getOldState());
        assertEquals(1, views.size());
        assertEquals("Test State", newState.getName());
        assertEquals("Test View", views.iterator().next());
        assertEquals("Test Action", newState.getAction());
    }

    @Test
    public void setOld()
            throws TimeoutException {
        statePage.dialog.setOld(State.createDefault("Name").addViews("View 1", "View 2").setAction("Action"));
        assertEquals("Name", statePage.getName());
        assertFalse(statePage.isNameErrorVisible());
        assertFalse(statePage.isViewErrorVisible());
        assertEquals(-1, statePage.getSelectedIndex());
        assertEquals(null, statePage.getSelectedOption());
        assertEquals(2, statePage.getOptionCount());
        assertEquals("View 1", statePage.getOption(0));
        assertEquals("View 2", statePage.getOption(1));
        assertEquals(2, statePage.getViewCount());
        assertEquals("View 1", statePage.getView(0));
        assertEquals("View 2", statePage.getView(1));
        assertEquals("Action", statePage.getAction());
    }

    @Test
    public void setOptions()
            throws TimeoutException {
        statePage.dialog.setOptions(asList("View 1", "View 2"));
        assertEquals(0, statePage.getViewCount());
        assertEquals(-1, statePage.getSelectedIndex());
        assertEquals(null, statePage.getSelectedOption());
        assertEquals(2, statePage.getOptionCount());
        assertEquals("View 1", statePage.getOption(0));
        assertEquals("View 2", statePage.getOption(1));
        assertEquals(0, statePage.getViewCount());
    }
}
