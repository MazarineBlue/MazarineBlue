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
package org.mazarineblue.mbt.gui.model;

import java.util.ArrayList;
import static java.util.Arrays.asList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.event.EventListenerList;
import org.mazarineblue.mbt.gui.exceptions.StateIsUsedException;

class DefaultModel
        implements GraphModel {

    private static final long serialVersionUID = 1L;

    private final List<State> states = new ArrayList<>(16);
    private final List<Transition> transitions = new ArrayList<>(16);
    private final EventListenerList listeners = new EventListenerList();

    @Override
    public String toString() {
        return "states=" + states.size() + ", transitions=" + transitions.size();
    }

    @Override
    public Collection<String> getViews() {
        return states.stream()
                .map(State::getViews)
                .collect(TreeSet::new, Set::addAll, Set::addAll);
    }

    @Override
    public List<State> getAllStates() {
        return states.stream()
                .map(s -> new UnmodifiableState(s))
                .collect(ArrayList::new, List::add, List::addAll);
    }

    @Override
    public List<State> getStatesByName(String name) {
        return states.stream()
                .filter(s -> s.getName().equals(name))
                .map(s -> new UnmodifiableState(s))
                .collect(ArrayList::new, List::add, List::addAll);
    }

    @Override
    public List<State> getStatesByView(String view) {
        return states.stream()
                .filter(s -> s.containsView(view))
                .map(s -> new UnmodifiableState(s))
                .collect(ArrayList::new, List::add, List::addAll);
    }

    @Override
    public void addState(State... arr) {
        List<State> list = asList(arr);
        list.stream().forEach(State::verify);
        states.addAll(list);
        fireAddedStates(Collections.unmodifiableList(list));
    }

    private void fireAddedStates(List<State> list) {
        asList(listeners.getListeners(ModelListener.class)).stream()
                .forEach(l -> l.addedStates(list));
    }

    @Override
    public void replaceState(State oldState, State newState) {
        StateConvertor convertor = new StateConvertor(states, s -> s.equals(oldState) ? newState : s);
        verifyStates(convertor);
        verifyTransition(convertor);
        convert(oldState).copy(newState);
    }

    @Override
    public void removeState(State state) {
        if (transitions.stream().noneMatch(t -> t.contains(state)))
            states.remove(convert(state));
        else
            throw new StateIsUsedException(state);
    }

    void verifyStates(StateConvertor convertor) {
        states.stream()
                .map(s -> convertor.convert(s))
                .forEach(State::verify);
    }

    void verifyTransition(StateConvertor convertor) {
        transitions.stream()
                .map(t -> Transition.createDefault(t, convertor))
                .forEach(Transition::verify);
    }

    private State convert(State s) {
        return states.get(states.indexOf(s));
    }

    @Override
    public List<Transition> getAllTransition() {
        return transitions.stream()
                .map(t -> new UnmodifiableTransition(t))
                .collect(ArrayList::new, List::add, List::addAll);
    }

    @Override
    public List<Transition> getTransitionsByName(String name) {
        return transitions.stream()
                .filter(t -> t.getName().equals(name))
                .map(t -> new UnmodifiableTransition(t))
                .collect(ArrayList::new, List::add, List::addAll);
    }

    @Override
    public List<Transition> getTransitionsByView(String view) {
        return transitions.stream()
                .filter(t -> t.containsSourceWithView(view) && t.containsDestinationWithView(view))
                .collect(ArrayList::new, List::add, List::addAll);
    }

    @Override
    public void addTransition(Transition... arr) {
        List<Transition> list = asList(arr);
        list.stream().forEach(Transition::verify);
        transitions.addAll(list);
        fireAddedTransitions(Collections.unmodifiableList(list));
    }

    @Override
    public void replaceTransition(Transition oldTransition, Transition newTransition) {
        convert(oldTransition).copy(newTransition, new StateConvertor(states));
    }

    @Override
    public void removeTransition(Transition transition) {
        transitions.remove(convert(transition));
    }

    private Transition convert(Transition s) {
        return transitions.get(transitions.indexOf(s));
    }

    private void fireAddedTransitions(List<Transition> list) {
        asList(listeners.getListeners(ModelListener.class)).stream()
                .forEach(l -> l.addedTransitions(list));
    }

    @Override
    public void addModelListener(ModelListener l) {
        listeners.add(ModelListener.class, l);
    }

    @Override
    public void removeModelListener(ModelListener l) {
        listeners.remove(ModelListener.class, l);
    }
}
