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

import java.util.Collection;
import java.util.List;

/**
 * A {@code Transition} represent the way to move change from one state to
 * another.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public interface Transition
        extends ModelElement<Transition> {

    /**
     * Creates a {@code transition} with the specified name.
     *
     * @param name the name of the {@code transition}.
     * @return the {@code transition} created.
     */
    public static Transition createDefault(String name) {
        return new TransitionImpl(name);
    }

    /**
     * Creates a deep copy of the specified {@code transition}.
     *
     * @param other the {@code transition} to make a deep copy off.
     * @return the {@code transition} created.
     */
    public static Transition createDefault(Transition other, StateConvertor convertor) {
        return new TransitionImpl(other.getName()).copy(other, convertor);
    }

    public void verify();

    /**
     * Deep copies the content of the other state into this state.
     *
     * @param other     the {@code transition} to perform the deep copy on.
     * @param convertor the convertor to use to get the original
     *                  {@code State states}.
     * @return the updated {@code transition}.
     */
    public Transition copy(Transition other, StateConvertor convertor);

    /**
     * Test if all source {@link State states} contains the specified view.
     *
     * @param view the specified view to look for.
     * @return {@code true} if the {@code transition} source {@code State states}
     *         contains the specified view.
     */
    public boolean containsSourceWithView(String view);

    /**
     * Test if the destination {@link State state} contains the specified view.
     *
     * @param view the specified view to look for.
     * @return {@code true} if the {@code transition} destination {@code State}
     *         contains the specified view.
     */
    public boolean containsDestinationWithView(String view);

    /**
     * Gets the guard expression for this {@code transition}.
     *
     * @return the associated guard expression of this {@code transition}.
     */
    public String getGuard();

    /**
     * Sets the specified guard expression for this {@code transition}.
     *
     * @param guard the guard expression to associated with this
     *              {@code transition}.
     * @return the updated {@code transition}.
     */
    public Transition setGuard(String guard);

    /**
     * Gets the business value associated with for this {@code transition}.
     *
     * @return the associated business value of this {@code transition}.
     */
    public int getBusinessValue();

    /**
     * Sets the specified business value for this {@code transition}.
     *
     * @param value the business value to be associated with this {@code transition}.
     * @return the updated {@code transition}.
     */
    public Transition setBusinessValue(int value);

    /**
     * Fetches the source {@link State states} from which a {@code transition} can be
     * made to the destination {@code state}.
     *
     * @return the source {@code state} associated with this {@code transition}.
     */
    public List<State> getSources();

    /**
     * Sets the source {@link State states} from which a {@code transition} can be
     * made to the destination {@code state}.
     *
     * @param states the source {@code states} to set.
     * @return the updated {@code transition}.
     */
    public Transition setSources(State... states);

    /**
     * Sets the source {@link State states} from which a {@code transition} can be
     * made to the destination {@code state}.
     *
     * @param states the source {@link State states} to set.
     * @return the updated {@code transition}.
     */
    public Transition setSources(Collection<State> states);

    /**
     * Fetches the destination {@link State state} to which a {@code transition} can be
     * made from any of the source {@code states}.
     *
     * @return the source {@code state} associated with this {@code transition}.
     */
    public State getDestination();

    /**
     * Sets the destination {@link State states} from which a {@code transition} can be
     * made to the source {@code states}.
     *
     * @param state the destination {@code states} to set.
     * @return the updated {@code transition}.
     */
    public Transition setDestination(State state);

    /**
     * Test if the specified {@link State state} is used in this {@code transition} as
     * a source or destination.
     *
     * @param state the {@code state} to look for.
     * @return {@code true} if the {@code state} is found.
     */
    public boolean contains(State state);

    /**
     * Test if the specified {@link State state} is used in this {@code transition} as
     * a source.
     *
     * @param state the {@code state} to look for.
     * @return {@code true} if the {@code state} is found.
     */
    public boolean isSource(State state);

    /**
     * Test if the specified {@link State state} is used in this {@code transition} as
     * a destination.
     *
     * @param state the {@code state} to look for.
     * @return {@code true} if the {@code state} is found.
     */
    public boolean isDestination(State state);
}
