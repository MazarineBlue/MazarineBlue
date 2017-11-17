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
package org.mazarineblue.mbt.criteria.step;

import static java.util.Collections.unmodifiableSet;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.mazarineblue.mbt.X;
import org.mazarineblue.mbt.expressions.Expression;
import org.mazarineblue.mbt.model.Event;
import org.mazarineblue.mbt.model.StateConfiguration;
import org.mazarineblue.mbt.model.Transition;

class AbstractStepPattern
        implements StepPattern {

    private final StateConfiguration sc;
    private final Set<Event> e;
//    private final ConditionValueAssignement cva;
    private final Expression cva;
    private final Set<Transition> transitions;

    AbstractStepPattern(StateConfiguration sc, Set<Event> e, Expression cva, List<Transition> t) {
        this.sc = sc;
        this.e = e;
        this.cva = cva;
        this.transitions = t == null ? null : new HashSet<>(t);
    }

    @Override
    public String toString() {
        return "(" + sc + ", " + e + ", " + cva + ", " + transitions + ')';
    }

    @Override
    public StateConfiguration getStateConfiguration() {
        return sc;
    }

    @Override
    public Set<Event> getEvents() {
        return e == null ? null : unmodifiableSet(e);
    }

    @Override
    public Expression getCva() {
        return cva;
    }

    @Override
    public Set<Transition> getTransitions() {
        return transitions == null ? null : unmodifiableSet(transitions);
    }

    /**
     * A step pattern matches this pattern if at least one state, one event,
     * the expression and one transition matches.
     */
    @Override
    public int compareTo(StepPattern other) {
        int c = 0;
        if (sc != null && other.getStateConfiguration() != null && c == 0)
            c = sc.compareTo(other.getStateConfiguration());
        if (e != null && other.getEvents() != null && c == 0)
            c = X.compateTo(e, other.getEvents());
//        if (cva != null && other.cva != null && c == 0)
//            c = cva.compareTo(other.cva);
        return X.compateTo(transitions, other.getTransitions());
    }

    public boolean match(Transition t) {
        if (sc != null && !sc.match(t))
            return false;
        if (e != null)
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        if (cva != null)
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        if (transitions != null)
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return true;
    }

    @Override
    public int hashCode() {
        return 3 * 13 * 13 * 13 * 13
                + 13 * 13 * 13 * Objects.hashCode(sc)
                + 13 * 13 * Objects.hashCode(e)
                + 13 * Objects.hashCode(cva)
                + Objects.hashCode(transitions);
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && getClass() == obj.getClass()
                && Objects.equals(this.sc, ((StepPattern) obj).getStateConfiguration())
                && Objects.equals(this.e, ((StepPattern) obj).getEvents())
                && Objects.equals(this.cva, ((StepPattern) obj).getCva())
                && Objects.equals(this.transitions, ((StepPattern) obj).getTransitions());
    }
}
