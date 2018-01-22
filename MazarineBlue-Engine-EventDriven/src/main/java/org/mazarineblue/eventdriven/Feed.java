/*
 * Copyright (c) 2012-2014 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 * Copyright (c) 2014-2015 Specialisterren
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

import org.mazarineblue.eventdriven.exceptions.NoEventsLeftException;
import org.mazarineblue.eventnotifier.Event;

/**
 * A {@code Feed} represent a stream of events that can be executed on a
 * {@link Processor}.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 * @see Processor
 */
public interface Feed {

    /**
     * Returns true if this feed has one or more events to pass on calling
     * {@link #next()}.
     *
     * @return {@code true} if {@code next()} can return another event.
     */
    public boolean hasNext();

    /**
     * Creates and returns the next event, is available. The ownership of the
     * event is passed.
     *
     * @return the next event.
     *
     * @throws NoEventsLeftException when the feed has no events left to pass.
     */
    public Event next();

    /**
     * Called after the event was processed. The ownership of the event is
     * passed back to the feed.
     *
     * @param event the event consumed event.
     */
    public void done(Event event);

    /**
     * Resets the feed to the state it was in before next() was called for the
     * first time.
     */
    public void reset();
}
