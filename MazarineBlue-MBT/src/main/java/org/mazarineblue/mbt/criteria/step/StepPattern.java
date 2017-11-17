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

import java.util.List;
import java.util.Set;
import org.mazarineblue.mbt.expressions.Expression;
import org.mazarineblue.mbt.model.Event;
import org.mazarineblue.mbt.model.StateConfiguration;
import org.mazarineblue.mbt.model.Transition;

public interface StepPattern
        extends Comparable<StepPattern> {

    /**
     * Creates a single step pattern that is required.
     */
    public static StepPattern required(StateConfiguration sc, Set<Event> e, Expression cva, List<Transition> t) {
        return new RequiredStepPattern(sc, e, cva, t);
    }

    /**
     * Creates a step pattern that is option and can be repeated many times.
     */
    public static StepPattern any(StateConfiguration sc, Set<Event> e, Expression cva, List<Transition> t) {
        return new AnyStepPattern(sc, e, cva, t);
    }

    public StateConfiguration getStateConfiguration();

    public Set<Event> getEvents();

    public Expression getCva();

    public Set<Transition> getTransitions();

    public boolean match(Transition t);
}
