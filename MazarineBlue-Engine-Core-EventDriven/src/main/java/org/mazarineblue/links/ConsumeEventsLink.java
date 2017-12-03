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
import static org.mazarineblue.eventbus.Event.matches;
import static org.mazarineblue.eventbus.Event.matchesAutoConsumable;

/**
 * A {@code COnsumeEventsLink} is a {@code TypeBasedLink} that will consume any
 * {@link Event} on the specified whitelist.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class ConsumeEventsLink
        extends ConditionLink {

    /**
     * Constructs a {@code ConsumeEventsLink} that consumes every event that is
     * on the specified whitelist.
     */
    public ConsumeEventsLink() {
        this(t -> true);
    }

    /**
     * Constructs a {@code ConsumeEventsLink} that consumes any event that is on
     * the specified whitelist.
     *
     * @param consumeTypes the types of {@link Event events} to consume.
     */
    public ConsumeEventsLink(Class<?>... consumeTypes) {
        this(matches(consumeTypes).or(matchesAutoConsumable()));
    }

    /**
     * Constructs a {@code ConsumeEventsLink} that consumes any event that is on
     * the specified whitelist.
     *
     * @param condition consume the event if this condition evaluates true.
     */
    public ConsumeEventsLink(Predicate<Event> condition) {
        super(condition.or(Event::isAutoConsumable));
    }

    @Override
    public void eventHandler(Event event) {
        if (applyCondition(event))
            event.setConsumed(true);
    }
}
