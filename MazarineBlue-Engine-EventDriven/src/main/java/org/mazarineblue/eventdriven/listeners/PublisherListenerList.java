/*
 * Copyright (c) Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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
 * A {@code PublisherListenerList} is a {@code PublisherListener} that
 * contains a list of {@code PublisherListener} to which it passes all
 * messages.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class PublisherListenerList
        extends ListenerList<PublisherListener>
        implements PublisherListener {

    @Override
    public void startPublishingEvent(Event event) {
        forEach(listener -> listener.startPublishingEvent(event));
    }

    @Override
    public void exceptionThrown(Event event, RuntimeException ex) {
        forEach(listener -> listener.exceptionThrown(event, ex));
    }

    @Override
    public void endPublishedEvent(Event event) {
        forEach(listener -> listener.endPublishedEvent(event));
    }
}
