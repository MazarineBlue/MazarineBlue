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
package org.mazarineblue.eventdriven;

import org.mazarineblue.eventnotifier.Event;
import org.mazarineblue.eventnotifier.Subscriber;

/**
 * An {@code Invoker} is representation of the object that publishes
 * {@link Event Events} to {@link Subscriber Subscribers}.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public interface Invoker {

    /**
     * Returns the {@code Processor} used to deliver {@link Event events}.
     *
     * @return the {@code Processor} used to deliver {@link Event events}.
     */
    public Processor processor();

    /**
     * Returns the {@code Chain} used to deliver {@link Event events}.
     *
     * @return the {@code Chain} used to deliver {@link Event events}.
     */
    public Chain chain();

    /**
     * Publish a new event on the bus.
     *
     * @param event the event to publish.
     */
    public void publish(Event event);
}
