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
package org.mazarineblue.links;

import java.util.function.Predicate;
import org.mazarineblue.eventbus.Event;
import org.mazarineblue.eventdriven.Link;

/**
 * A {@code ConditionLink} is a {@code Link} that represents a condition that
 * accepts a event.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public abstract class ConditionLink
        extends Link {

    private final Predicate<Event> condition;

    /**
     * Constructs a {@code ConsumeEventsLink} that which {@link applyCondition}
     * returns {@code true} if the specified condition does too.
     *
     * @param condition consume the event if this condition evaluates true.
     */
    protected ConditionLink(Predicate<Event> condition) {
        this.condition = condition;
    }

    /**
     * Applies this condition to the given argument.
     *
     * @param event the condition argument
     * @return {@code true} when the condition is met.
     */
    protected boolean applyCondition(Event event) {
        return condition.test(event);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
