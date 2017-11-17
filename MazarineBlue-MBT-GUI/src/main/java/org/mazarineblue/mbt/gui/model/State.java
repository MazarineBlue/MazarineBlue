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
import java.util.Set;

/**
 * A {@code State} represent a state that the software may occupy.
 * <p>
 * A state can belong to multiple views (on the {@link GraphModel model}).
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public interface State
        extends ModelElement<State> {

    /**
     * Creates a {@code state} with the specified name.
     *
     * @param name the name of the {@code state}.
     * @return the {@code state} created.
     */
    public static State createDefault(String name) {
        return new StateImpl(name);
    }

    /**
     * Creates a deep copy of the specified {@code state}.
     *
     * @param other the {@code state} to make a deep copy off.
     * @return the {@code state} created.
     */
    public static State createDefault(State other) {
        return new StateImpl(other.getName()).copy(other);
    }

    public void verify();

    /**
     * Deep copies the content of the other state into this state.
     *
     * @param other the state to perform the deep copy on.
     * @return the updated state.
     */
    public State copy(State other);

    /**
     * Appends the specified views to this state.
     *
     * @param views the views to append to this state.
     * @return the updated state.
     */
    public State addViews(String... views);

    /**
     * Appends the specified views to this state.
     *
     * @param views the views to append to this state.
     * @return the updated state.
     */
    public State addViews(Collection<String> views);

    /**
     * Fetches the views on the {@link GraphModel model} that this state
     * appears in.
     *
     * @return the views this state appears in.
     */
    public Set<String> getViews();

    /**
     * Test if this state appears in the specified view.
     *
     * @param view the view to use for testing.
     * @return {@code true} if this state appears in the specified view.
     */
    public boolean containsView(String view);
}
