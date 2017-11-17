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
import java.util.concurrent.TimeoutException;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import static org.mazarineblue.mbt.gui.StringConstants.INVALID_CHARACTERS_USED;
import org.mazarineblue.mbt.gui.util.TransitonDialogTestHelper;
import static org.mazarineblue.swing.SwingUtil.waitUntilFalse;
import static org.mazarineblue.swing.SwingUtil.waitUntilTrue;

public class TransitonDialogIT
        extends TransitonDialogTestHelper {

    @Test
    public void name_InvalidInput()
            throws TimeoutException, InterruptedException, InvocationTargetException {
        CharSequence invalid = INVALID_CHARACTERS_REGULAR;
        for (int i = 0; i < invalid.length(); ++i) {
            transitionPage.setNameErrorVisible(false);
            waitUntilFalse(transitionPage::isNameErrorVisible, 500);

            transitionPage.focusOnName();
            transitionPage.setName(Character.toString(invalid.charAt(i)));
            transitionPage.focusOnAction();

            waitUntilTrue(transitionPage::isNameErrorVisible, 500);
            assertEquals(INVALID_CHARACTERS_USED, transitionPage.getNameErrorMessage());
        }
    }

    @Test
    public void guard_Invalid()
            throws TimeoutException, InterruptedException, InvocationTargetException {
        transitionPage.setName("Test Name");
        CharSequence invalid = INVALID_CHARACTERS_VARIABLE;
        for (int i = 0; i < invalid.length(); ++i) {
            transitionPage.setGuardErrorVisible(false);
            waitUntilFalse(transitionPage::isGuardErrorVisible, 500);

            transitionPage.focusOnGuard();
            transitionPage.setGuard(Character.toString(invalid.charAt(i)));
            transitionPage.focusOnAction();

            waitUntilTrue(transitionPage::isGuardErrorVisible, 500);
            assertEquals(INVALID_CHARACTERS_USED, transitionPage.getGuardErrorMessage());
        }
    }
}
