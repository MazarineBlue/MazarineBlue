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
import org.mazarineblue.mbt.gui.TransitionDialog;
import org.mazarineblue.mbt.gui.model.State;
import org.mazarineblue.mbt.gui.model.Transition;

class AddAction
        extends AbstractAction {

    private final JFrame owner;

    AddAction(JFrame owner, TablePopupMenuListener position, TableModelConvertor convertor) {
        super("Add", position, convertor);
        this.owner = owner;
    }

    @Override
    public boolean isVisible(int row, int column) {
        return !isHeader(row) && isModelElement(row, column);
    }

    private boolean isModelElement(int row, int column) {
        Object value = getValueAt(row, column);
        return !(value instanceof State) && !(value instanceof Transition);
    }

    private boolean isHeader(int row) {
        return row < 0;
    }

    @Override
    public void doAction(int row, int column) {
        TransitionDialog dialog = new TransitionDialog(owner, "Edit Transition");
        dialog.setOptions(getStates());
        dialog.setOld((Transition) getValueAt(row, 0));
        dialog.addSource((State) getValueAt(-1, column));
        dialog.setAcceptAction(this::replace);
        dialog.setVisible(true);
    }
}
