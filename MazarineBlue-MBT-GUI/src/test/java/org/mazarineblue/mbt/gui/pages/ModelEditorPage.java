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
package org.mazarineblue.mbt.gui.pages;

import static java.awt.EventQueue.invokeLater;
import java.util.concurrent.TimeoutException;
import javax.swing.JComboBox;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import org.mazarineblue.mbt.gui.ModelEditorFrame;
import org.mazarineblue.mbt.gui.StateDialog;
import org.mazarineblue.mbt.gui.TransitionDialog;
import static org.mazarineblue.swing.SwingUtil.fetchChildNamed;
import static org.mazarineblue.swing.SwingUtil.fetchWindowTitled;
import static org.mazarineblue.swing.SwingUtil.waitFor;

public class ModelEditorPage
        implements AutoCloseable {

    private static final int TIMEOUT = 500;

    public final ModelEditorFrame frame;
    private final JMenuItem newStateMenuItem;
    private final JMenuItem newTransitionMenuItem;
    public final JComboBox<?> viewComboBox;
    public final JTable table;
    public final JPopupMenu popupMenu;
    public final JMenuItem addMenuItem;
    public final JMenuItem editMenuItem;
    public final JMenuItem removeMenuItem;

    public ModelEditorPage(ModelEditorFrame frame) {
        this.frame = frame;
        newStateMenuItem = fetchChildNamed(frame, "newStateMenuItem", JMenuItem.class);
        newTransitionMenuItem = fetchChildNamed(frame, "newTransitionMenuItem", JMenuItem.class);
        viewComboBox = fetchChildNamed(frame, "viewComboBox", JComboBox.class);
        table = fetchChildNamed(frame, "stateTransactionTable", JTable.class);
        popupMenu = table.getComponentPopupMenu();
        popupMenu.setInvoker(table);
        addMenuItem = (JMenuItem) popupMenu.getComponent(0);
        editMenuItem = (JMenuItem) popupMenu.getComponent(1);
        removeMenuItem = (JMenuItem) popupMenu.getComponent(2);
    }

    public StatePage addState()
            throws TimeoutException {
        invokeLater(() -> newStateMenuItem.doClick());
        return waitFor(() -> new StatePage(fetchWindowTitled(frame, "New State", StateDialog.class)), TIMEOUT);

    }

    public StatePage editStage()
            throws TimeoutException {
        invokeLater(() -> editMenuItem.doClick());
        return waitFor(() -> new StatePage(fetchWindowTitled(frame, "Edit State", StateDialog.class)), TIMEOUT);
    }

    public StatePage removeStage()
            throws TimeoutException {
        invokeLater(() -> removeMenuItem.doClick());
        return waitFor(() -> new StatePage(fetchWindowTitled(frame, "Remove State", StateDialog.class)), TIMEOUT);
    }

    public TransitionPage addTransition()
            throws TimeoutException {
        invokeLater(() -> newTransitionMenuItem.doClick());
        return waitFor(() -> new TransitionPage(fetchWindowTitled(frame, "New Transition", TransitionDialog.class)), TIMEOUT);
    }

    public TransitionPage addStateSource()
            throws TimeoutException {
        invokeLater(() -> addMenuItem.doClick());
        return waitFor(() -> new TransitionPage(fetchWindowTitled(frame, "Edit Transition", TransitionDialog.class)), TIMEOUT);
    }

    public TransitionPage editTransition()
            throws TimeoutException {
        invokeLater(() -> editMenuItem.doClick());
        return waitFor(() -> new TransitionPage(fetchWindowTitled(frame, "Edit Transition", TransitionDialog.class)), TIMEOUT);
    }

    public TransitionPage removeTransition()
            throws TimeoutException {
        invokeLater(() -> removeMenuItem.doClick());
        return waitFor(() -> new TransitionPage(fetchWindowTitled(frame, "Remove Transition", TransitionDialog.class)), TIMEOUT);
    }

    @Override
    public void close()
            throws Exception {
        frame.dispose();
    }
}
