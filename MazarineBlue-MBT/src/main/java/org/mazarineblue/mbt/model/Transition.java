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

import java.util.Collection;
import java.util.Set;
import org.mazarineblue.mbt.Variable;
import org.mazarineblue.mbt.expressions.Expression;

public interface Transition
        extends Comparable<Transition> {

    public static Transition createDefault(State source, State target) {
        return new TransitionImpl(source, target);
    }

    public State getSource();

    public boolean isSource(State s);

    public State getTarget();

    public boolean isTarget(State s);

    default boolean isVariableDefined(Variable var) { // can only be in effect
        return getGuard().isVariableDefined(var) || getEffect().isVariableDefined(var);
    }

    default boolean isVariableUsed(Variable var) { // search guard and effect
        return getGuard().isVariableUsed(var) || getEffect().isVariableUsed(var);
    }

    public Transition setEvents(Event... events);

    public Transition setGuard(String expression);

    public Transition setEffect(String title, Collection<Expression> expressions);

    public Set<Event> getEvents();

    public Guard getGuard();

    public Effect getEffect();

    public boolean isSatified(ValueAssignment va);
    // public boolean isActive();
}
