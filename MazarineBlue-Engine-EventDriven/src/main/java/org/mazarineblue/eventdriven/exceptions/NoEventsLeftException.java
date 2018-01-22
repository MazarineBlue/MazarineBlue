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
package org.mazarineblue.eventdriven.exceptions;

import org.mazarineblue.eventdriven.Feed;
import org.mazarineblue.eventnotifier.Event;

/**
 * A {@code NoEventsLeftException} is thrown by an {@link Feed} when the
 * {@link next()} method is called and the {@link Feed} doesn't have any
 * {@link Event} to give.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 * @see Feed#next()
 */
public class NoEventsLeftException
        extends EventDrivenException {

    private static final long serialVersionUID = 1L;

    public NoEventsLeftException(Feed feed) {
        super("Feed: " + feed);
    }
}
