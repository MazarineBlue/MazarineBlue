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

import java.awt.event.ActionEvent;
import java.util.function.BiConsumer;
import javax.swing.AbstractAction;

class TableManipulationAction
        extends AbstractAction
        implements ActiveCellListener {

    private static final long serialVersionUID = 1L;

    private final BiConsumer<Integer, Integer> action;
    private int row;
    private int column;

    TableManipulationAction(String name, BiConsumer<Integer, Integer> action) {
        super(name);
        this.action = action;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        action.accept(row, column);
    }

    @Override
    public void updateActiveCell(int row, int column) {
        this.row = row;
        this.column = column;
    }
}
