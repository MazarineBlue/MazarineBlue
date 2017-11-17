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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import org.mazarineblue.mbt.Variable;

public interface StateMachine {

    public static StateMachine createDefault() {
        return new StateMachineImpl();
    }

    public State createState(String title);

    public Transition createTransition(State source, State target);

    public void setIntialStates(State... states);

    public StateConfiguration getInitialConfiguration();

    default Set<StateConfiguration> getConfigurations(Predicate<StateConfiguration> predicate) {
        return getConfigurations().stream().filter(predicate).collect(HashSet::new, Set::add, Set::addAll);
    }

    /**
     * Fetches a set of state configurations, indicating which states can be
     * active at the same time.
     *
     * @return a set of state configurations.
     */
    public Set<StateConfiguration> getConfigurations();

    public Set<Variable> getVariables();

    default Set<State> getStates(Predicate<State> predicate) {
        return getStates().stream().filter(predicate).collect(HashSet::new, Set::add, Set::addAll);
    }

    /**
     * Returns the set of states within this state machine.
     *
     * @return a set of states.
     */
    public Set<State> getStates();

    /**
     * Returns the set of outgoing transitions.
     *
     * @param s the source state of all returned transitions.
     * @return a set of outgoing transitions.
     */
    default Set<Transition> getOutgoing(State s) {
        return getTransitions(t -> t.isSource(s));
    }

    /**
     * Returns the set of incomming transitions.
     *
     * @param s the target state of all returned transitions.
     * @return a set of incomming transitions.
     */
    default Set<Transition> getIncomming(State s) {
        return getTransitions(t -> t.isTarget(s));
    }

    default Set<Transition> getTransitionsDefining(Variable var) {
        return getTransitions(t -> t.isVariableDefined(var));
    }

    default Set<Transition> getTransitionsUsing(Variable var) {
        return getTransitions(t -> t.isVariableUsed(var));
    }

    default Set<Transition> getTransitions(Predicate<Transition> predicate) {
        return getTransitions().stream().filter(predicate).collect(HashSet::new, Set::add, Set::addAll);
    }

    /**
     * Returns the set of transitions within this state machine.
     *
     * @return a set of transitions.
     */
    public Set<Transition> getTransitions();

    /**
     * Tests if a initial state is the source of the specified transition.
     *
     * @param t the specified transition to test.
     * @return {@code true} if source of transition {@code t} is a initial
     *         state.
     */
    public boolean isSourceCompleting(Transition t);

    /**
     * Tests if a initial state is the target of the specified transition.
     *
     * @param t the specified transition to test.
     * @return {@code true} if target of transition {@code t} is a initial
     *         state.
     */
    public boolean isTargetCompleting(Transition t);

    public default List<Transition> not(Collection<Transition> transitions) {
        return getTransitions().stream()
                .filter(t -> !transitions.contains(t))
                .collect(ArrayList::new, List::add, List::addAll);
    }
}
