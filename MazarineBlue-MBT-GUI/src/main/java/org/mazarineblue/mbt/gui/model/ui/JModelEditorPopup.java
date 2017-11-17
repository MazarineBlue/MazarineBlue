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

import java.util.ArrayList;
import java.util.Collection;
import javax.swing.JFrame;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.event.PopupMenuListener;

public final class JModelEditorPopup
        extends JPopupMenu
        implements ActiveCellListener {

    private static final long serialVersionUID = 1L;

    private final Collection<JModelMenuItem> collection = new ArrayList<>(3);

    @SuppressWarnings("LeakingThisInConstructor")
    public JModelEditorPopup(JFrame owner, JTable table, TableModelConvertor convertor) {
        TablePopupMenuListener position = new TablePopupMenuListener(this, table);
        addMenuItem(new AddAction(owner, position, convertor).createMenuItem());
        addMenuItem(new EditAction(owner, position, convertor).createMenuItem());
        addMenuItem(new RemoveAction(owner, position, convertor).createMenuItem());
        addPopupMenuListener(position);
    }

    private void addMenuItem(JModelMenuItem menuItem) {
        collection.add(menuItem);
        add(menuItem);
    }

    @Override
    public final void addPopupMenuListener(PopupMenuListener l) {
        super.addPopupMenuListener(l);
    }

    @Override
    public void updateActiveCell(int row, int column) {
        setVisible(shouldBeVisible(row, column));
    }

    private boolean shouldBeVisible(int row, int column) {
        return collection.stream().anyMatch(menuItem -> menuItem.shouldBeVisible(row, column));
    }
}
