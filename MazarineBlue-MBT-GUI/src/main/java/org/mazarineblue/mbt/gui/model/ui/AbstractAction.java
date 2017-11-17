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

import java.util.Collection;
import java.util.List;
import org.mazarineblue.mbt.gui.model.State;
import org.mazarineblue.mbt.gui.model.Transition;

abstract class AbstractAction {

    private final String name;
    private final TablePopupMenuListener position;
    private final TableModelConvertor convertor;
    private TableManipulationAction action;
    private JModelMenuItem menuItem;

    AbstractAction(String name, TablePopupMenuListener position, TableModelConvertor convertor) {
        this.name = name;
        this.position = position;
        this.convertor = convertor;
    }

    protected Object getValueAt(int row, int column) {
        return convertor.getValueAt(row, column);
    }

    protected List<State> getStates() {
        return convertor.getStates();
    }

    protected List<Transition> getTransitions() {
        return convertor.getTransitions();
    }

    protected void replace(State oldState, State newState) {
        convertor.replace(oldState, newState);
    }

    protected void replace(Transition oldTransition, Transition newTransition) {
        convertor.replace(oldTransition, newTransition);
    }

    protected void remove(State oldState, State newState) {
        convertor.remove(oldState);
    }

    protected void remove(Transition oldTransition, Transition newTransition) {
        convertor.remove(oldTransition);
    }

    protected Collection<String> getViews() {
        return convertor.getModel().getViews();
    }

    public JModelMenuItem createMenuItem() {
        if (menuItem == null) {
            menuItem = new JModelMenuItem(this::isVisible, createAction());
            position.addListener(menuItem);
        }
        return menuItem;
    }

    public abstract boolean isVisible(int row, int column);

    public TableManipulationAction createAction() {
        if (action == null) {
            action = new TableManipulationAction(name, this::doAction);
            position.addListener(action);
        }
        return action;
    }

    public abstract void doAction(int row, int column);
}
