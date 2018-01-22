/*
 * Copyright (c) 2017 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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
package org.mazarineblue.subscribers;

import java.util.function.Predicate;
import org.mazarineblue.eventdriven.Invoker;
import org.mazarineblue.eventnotifier.Event;

/**
 * A {@code SkipEventsSubscriber} is a {@code Subscriber} that skips event
 * until a stop condition is met.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class SkipEventsSubscriber
        extends AbstractProcessingSubscriber {

    /**
     * Constructs a {@code SkipEventsSubscriber} that removes it self when the
     * specified stopCondition is evaluates to true.
     *
     * @param invoker       used to remove this link from.
     * @param stopCondition stop recording event when this condition evaluates true.
     */
    public SkipEventsSubscriber(Invoker invoker, Predicate<Event> stopCondition) {
        super(invoker, stopCondition);
    }

    @Override
    protected void processEvent(Event event) {
        event.setConsumed(true);
    }
}
