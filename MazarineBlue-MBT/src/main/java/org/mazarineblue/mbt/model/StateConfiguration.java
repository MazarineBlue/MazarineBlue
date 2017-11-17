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

import static java.util.Arrays.asList;
import java.util.Collection;
import static java.util.Collections.unmodifiableCollection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.mazarineblue.mbt.X;

public class StateConfiguration
        implements Comparable<StateConfiguration> {

    private final Set<State> states;

    public StateConfiguration(State... states) {
        this.states = new HashSet<>(asList(states));
    }

    @Override
    public String toString() {
        return X.toString(states, "{\"", "\", \"", "\"}", "{}");
    }

    public void add(State s) {
        if (!states.add(s))
            throw new IllegalStateException(s.toString());
    }

    public void remove(State s) {
        if (!states.remove(s))
            throw new IllegalStateException(s.toString());
    }

    public Collection<State> getStates() {
        return unmodifiableCollection(states);
    }

    public boolean contains(State s) {
        return states.contains(s);
    }

    @Override
    public int compareTo(StateConfiguration other) {
        return X.compateTo(states, other.states);
    }

    public boolean match(Transition t) {
        return states.contains(t.getSource()) || states.contains(t.getTarget());
    }

    @Override
    public int hashCode() {
        return 7 * 83
                + Objects.hashCode(this.states);
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && getClass() == obj.getClass()
                && Objects.equals(this.states, ((StateConfiguration) obj).states);
    }
}
