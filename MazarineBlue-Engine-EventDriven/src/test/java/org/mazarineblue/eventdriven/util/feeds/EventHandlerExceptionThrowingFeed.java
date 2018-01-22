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
package org.mazarineblue.eventdriven.util.feeds;

import org.mazarineblue.eventdriven.Processor;
import org.mazarineblue.eventdriven.feeds.MemoryFeed;
import org.mazarineblue.eventnotifier.EventHandler;
import org.mazarineblue.eventnotifier.events.TestEvent;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class EventHandlerExceptionThrowingFeed
        extends MemoryFeed {

    /**
     * Event handlers are not meant to be called directly, instead publish an
     * event to an {@link Processor}; please see the specified event for more
     * information about this event handler.
     *
     * @param event the event this {@code EventHandler} processes.
     * @see TestEvent
     */
    @EventHandler
    public void eventHandler(TestEvent event) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
