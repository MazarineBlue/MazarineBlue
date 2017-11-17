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
package org.mazarineblue.mbt.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.mazarineblue.mbt.Variable;

class StateMachineImpl
        implements StateMachine {

    private StateConfiguration intial;
    private final Set<State> states = new HashSet<>();
    private final Set<Transition> transitions = new HashSet<>();

    @Override
    public State createState(String title) {
        State s = new StateImpl(title);
        if (!states.add(s))
            throw new IllegalStateException();
        return s;
    }

    @Override
    public Transition createTransition(State source, State target) {
        Transition t = new TransitionImpl(source, target);
        if (!transitions.add(t))
            throw new IllegalStateException();
        return t;
    }

    @Override
    public void setIntialStates(State... states) {
        intial = new StateConfiguration(states);
    }

    @Override
    public StateConfiguration getInitialConfiguration() {
        return intial;
    }

    @Override
    public Set<StateConfiguration> getConfigurations() {
        return states.stream()
                .map(s -> new StateConfiguration(s))
                .collect(HashSet::new, Set::add, Set::addAll);
    }

    @Override
    public Set<State> getStates() {
        return Collections.unmodifiableSet(states);
    }

    @Override
    public Set<Transition> getTransitions() {
        return Collections.unmodifiableSet(transitions);
    }

    @Override
    public boolean isSourceCompleting(Transition t) {
        return intial.contains(t.getSource());
    }

    @Override
    public boolean isTargetCompleting(Transition t) {
        return intial.contains(t.getTarget());
    }

    @Override
    public Set<Variable> getVariables() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
