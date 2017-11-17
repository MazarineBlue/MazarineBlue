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

import static java.awt.EventQueue.invokeLater;
import java.awt.Point;
import javax.swing.JTable;
import static javax.swing.SwingUtilities.convertPoint;
import javax.swing.event.EventListenerList;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

class TablePopupMenuListener
        implements PopupMenuListener {

    private final EventListenerList list = new EventListenerList();
    private final JModelEditorPopup popup;
    private final JTable table;

    TablePopupMenuListener(JModelEditorPopup popup, JTable table) {
        this.popup = popup;
        this.table = table;
        addListener(popup);
    }

    @Override
    public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
        invokeLater(() -> {
            Point point = convertPoint(popup, new Point(0, 0), table);
            int row = table.rowAtPoint(point);
            int column = table.columnAtPoint(point);
            for (ActiveCellListener listener : list.getListeners(ActiveCellListener.class))
                listener.updateActiveCell(row, column);
            popup.pack();
            popup.validate();
        });
    }

    @Override
    public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
        // Nothing to do
    }

    @Override
    public void popupMenuCanceled(PopupMenuEvent e) {
        // Nothing to do
    }

    public final void addListener(ActiveCellListener listener) {
        list.add(ActiveCellListener.class, listener);
    }

    public void removeListener(ActiveCellListener listener) {
        list.remove(ActiveCellListener.class, listener);
    }
}
