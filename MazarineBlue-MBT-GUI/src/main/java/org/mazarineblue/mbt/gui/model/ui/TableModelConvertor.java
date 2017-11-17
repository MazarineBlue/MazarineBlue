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
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import org.mazarineblue.mbt.gui.model.GraphModel;
import org.mazarineblue.mbt.gui.model.ModelListener;
import org.mazarineblue.mbt.gui.model.State;
import org.mazarineblue.mbt.gui.model.Transition;

public class TableModelConvertor
        extends AbstractTableModel
        implements TableModel, ModelListener {

    private static final long serialVersionUID = 1L;
    private static final String FIRST_HEADER = "Transitions";
    private static final String NA = "N/A";

    private final GraphModel model;
    private String view;

    public TableModelConvertor(GraphModel model) {
        this.model = model;
        view = getSelectedView(model);
    }

    private String getSelectedView(GraphModel model) {
        Collection<String> views = model.getViews();
        return views.isEmpty() ? null : views.iterator().next();
    }

    GraphModel getModel() {
        return model;
    }

    List<State> getStates() {
        return model.getStatesByView(view);
    }

    List<Transition> getTransitions() {
        return model.getTransitionsByView(view);
    }

    void replace(State oldState, State newState) {
        model.replaceState(oldState, newState);
        fireTableStructureChanged();
    }

    void replace(Transition oldTransition, Transition newTransition) {
        model.replaceTransition(oldTransition, newTransition);
        fireTableDataChanged();
    }

    void remove(State state) {
        model.removeState(state);
        fireTableStructureChanged();
    }

    void remove(Transition transition) {
        model.removeTransition(transition);
        fireTableDataChanged();
    }

    public void setView(String view) {
        this.view = view;
        fireTableStructureChanged();
    }

    String getView() {
        return view;
    }

    @Override
    public int getRowCount() {
        return model.getTransitionsByView(view).size();
    }

    @Override
    public int getColumnCount() {
        return 1 + model.getStatesByView(view).size();
    }

    @Override
    public String getColumnName(int columnIndex) {
        return getHeaderValueAt(columnIndex).toString();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex < 0)
            return getHeaderValueAt(columnIndex);
        List<Transition> transitions = model.getTransitionsByView(view);
        Transition t = transitions.get(rowIndex);
        return isFirstColumn(columnIndex) ? t
                : columnContainsDestinationState(columnIndex, t) ? t.getDestination()
                : NA;
    }

    private Object getHeaderValueAt(int columnIndex) {
        if (columnIndex == 0)
            return FIRST_HEADER;
        List<State> states = model.getStatesByView(view);
        return states.get(columnIndex - 1);
    }

    private static boolean isFirstColumn(int columnIndex) {
        return columnIndex == 0;
    }

    private boolean columnContainsDestinationState(int columnIndex, Transition t) {
        List<State> states = model.getStatesByView(view);
        State column = states.get(columnIndex - 1);
        return t.getSources().contains(column);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columnIndex == 0 ? Transition.class : State.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        // Columns are not editable
    }

    @Override
    public void addedStates(List<State> states) {
        fireTableStructureChanged();
    }

    @Override
    public void addedTransitions(List<Transition> list) {
        fireTableDataChanged();
    }
}
