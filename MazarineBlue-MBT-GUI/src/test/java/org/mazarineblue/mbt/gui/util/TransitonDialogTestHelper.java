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

import java.util.ArrayList;
import java.util.Collection;
import javax.swing.JFrame;
import org.junit.After;
import org.junit.Before;
import org.mazarineblue.mbt.gui.TransitionDialog;
import org.mazarineblue.mbt.gui.model.State;
import org.mazarineblue.mbt.gui.model.Transition;
import org.mazarineblue.mbt.gui.pages.TransitionPage;

public abstract class TransitonDialogTestHelper {

    protected static final String INVALID_CHARACTERS_REGULAR = "~!@#$%^&";
    protected static final String INVALID_CHARACTERS_VARIABLE = "~!@#%^";
    protected static final String VALID_CHARACTERS_VARIABLE = "$a == $b || 0 <= ${c} && ${c} <= 100";

    protected static final String STATE_A = "State A";
    protected static final String STATE_B = "State B";
    protected static final String STATE_C = "State C";
    protected static final String STATE_D = "State D";
    protected static final String STATE_E = "State E";

    protected static final int STATE_A_INDEX = 0;
    protected static final int STATE_B_INDEX = 1;
    protected static final int STATE_C_INDEX = 2;
    protected static final int STATE_D_INDEX = 3;
    protected static final int STATE_E_INDEX = 4;

    @SuppressWarnings("ProtectedField")
    protected FormActionSpy<Transition> okSpy;
    @SuppressWarnings("ProtectedField")
    protected TransitionDialog dialog;
    @SuppressWarnings("ProtectedField")
    protected TransitionPage transitionPage;

    @Before
    @SuppressWarnings("unchecked")
    public void setup() {
        okSpy = new FormActionSpy<>();
        Collection<State> states = new ArrayList<>(4);
        states.add(State.createDefault(STATE_A).addViews("View 1", "View 2"));
        states.add(State.createDefault(STATE_B).addViews("View 1"));
        states.add(State.createDefault(STATE_C).addViews("View 2"));
        states.add(State.createDefault(STATE_D).addViews("View 3"));
        states.add(State.createDefault(STATE_E));

        dialog = new TransitionDialog(new JFrame(), "Test Transition Dialog");
        dialog.setAcceptAction(okSpy);
        dialog.setOptions(states);
        transitionPage = new TransitionPage(dialog);
    }

    @After
    public void teardown() {
        dialog.dispose();
        dialog = null;
        okSpy = null;
    }
}
