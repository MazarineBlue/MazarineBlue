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
package org.mazarineblue.eventdriven.listeners;

import org.mazarineblue.eventnotifier.Event;

/**
 * A {@code PublisherListener} is a listener that, when a event is published,
 * reports on the status.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public interface PublisherListener {

    public static PublisherListener getDummy() {
        return new DummyPublisherListener();
    }

    /**
     * Called when an event was published on the chain.
     *
     * @param event the event that was published
     */
    public void startPublishingEvent(Event event);

    /**
     * Called when an exception was thrown during the publishing of the event.
     *
     * @param event the event that was published
     * @param ex    the exception that was thrown
     */
    public void exceptionThrown(Event event, RuntimeException ex);

    /**
     * Called when publishing has ended.
     *
     * @param event the event that was published
     */
    public void endPublishedEvent(Event event);
}
