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
package org.mazarineblue.mbt.gui.model.ui;

import javax.swing.JFrame;
import org.mazarineblue.mbt.gui.StateDialog;
import org.mazarineblue.mbt.gui.TransitionDialog;
import org.mazarineblue.mbt.gui.model.State;
import org.mazarineblue.mbt.gui.model.Transition;

class EditAction
        extends AbstractAction {

    private final JFrame owner;

    EditAction(JFrame owner, TablePopupMenuListener position, TableModelConvertor convertor) {
        super("Edit", position, convertor);
        this.owner = owner;
    }

    @Override
    public boolean isVisible(int row, int column) {
        Object value = getValueAt(row, column);
        return value instanceof State || value instanceof Transition;
    }

    @Override
    public void doAction(int row, int column) {
        Object value = getValueAt(row, column);
        if (value instanceof State)
            editState(value);
        else if (value instanceof Transition)
            editTransition(value);
    }

    private void editState(Object value) {
        StateDialog dialog = new StateDialog(owner, "Edit State");
        dialog.setOptions(getViews());
        dialog.setOld((State) value);
        dialog.setAcceptAction(this::replace);
        dialog.setVisible(true);
    }

    private void editTransition(Object value) {
        TransitionDialog dialog = new TransitionDialog(owner, "Edit Transition");
        dialog.setOptions(getStates());
        dialog.setOld((Transition) value);
        dialog.setAcceptAction(this::replace);
        dialog.setVisible(true);
    }
}
