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

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * A data model for graph, with {@link State states} and
 * {@link Transition transitions}.
 * <p>
 * The model can be split into several views, each showing a part of the model.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public interface GraphModel
        extends Serializable {

    public static GraphModel createDefault() {
        return new DefaultModel();
    }

    /**
     * Fetches the all views on this model.
     *
     * @return all views on this model.
     */
    public Collection<String> getViews();

    /**
     * Fetches all states registed with this model.
     *
     * @return all states registed with this model.
     */
    public List<State> getAllStates();

    /**
     * Fetches the states with the specified name.
     *
     * @param name the name to filter on.
     * @return the states with the specified name.
     */
    public List<State> getStatesByName(String name);

    /**
     * Fetches those states that contain the specified view
     *
     * @param view the view to filter on.
     * @return the states with the specified view.
     */
    public List<State> getStatesByView(String view);

    /**
     * Add the specified states to the model.
     *
     * @param states the states to add to the model.
     */
    public void addState(State... states);

    public void replaceState(State oldState, State newState);

    /**
     * Removes the specified state from the model.
     *
     * @param state the state to remove from the model.
     */
    public void removeState(State state);

    /**
     * Fetches all transitions registed with this model.
     *
     * @return all transitions registed with this model.
     */
    public List<Transition> getAllTransition();

    /**
     * Fetches the transitions with the specified name.
     *
     * @param name the name to filter on.
     * @return the transitions with the specified name.
     */
    public List<Transition> getTransitionsByName(String name);

    /**
     * Fetches the transitions with the specified view.
     *
     * @param view the name to filter on.
     * @return the transitions with the specified view.
     */
    public List<Transition> getTransitionsByView(String view);

    /**
     * Add the specified transitions to the model.
     *
     * @param transitions the transitions to add to the model.
     */
    public void addTransition(Transition... transition);

    public void replaceTransition(Transition oldTransition, Transition newTransition);

    /**
     * Removes the specified transition from the model.
     *
     * @param transition the transition to remove from the model.
     */
    public void removeTransition(Transition transition);

    public void addModelListener(ModelListener l);

    public void removeModelListener(ModelListener l);
}
