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
package org.mazarineblue.mbt.gui.util;

import javax.swing.JFrame;
import org.junit.After;
import org.junit.Before;
import org.mazarineblue.mbt.gui.StateDialog;
import org.mazarineblue.mbt.gui.model.State;
import org.mazarineblue.mbt.gui.pages.StatePage;

public abstract class StateDialogTestHelper {

    protected static final String INVALID_CHARACTERS = "~!@#$%^&";

    @SuppressWarnings("ProtectedField")
    protected FormActionSpy<State> okSpy;
    @SuppressWarnings("ProtectedField")
    private StateDialog dialog;
    @SuppressWarnings("ProtectedField")
    protected StatePage statePage;

    @Before
    @SuppressWarnings("unchecked")
    public void setup() {
        okSpy = new FormActionSpy<>();
        dialog = new StateDialog(new JFrame(), "Test State Dialog");
        dialog.setAcceptAction(okSpy);
        statePage = new StatePage(dialog);
    }

    @After
    public void teardown() {
        dialog.dispose();
        dialog = null;
        statePage = null;
    }
}
