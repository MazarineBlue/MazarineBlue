/*
 * Copyright (c) 2015-2016 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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

import org.mazarineblue.eventdriven.listeners.FeedExecutorListener;
import org.mazarineblue.eventdriven.listeners.InterpreterListener;

/**
 * An {@code Interpreter} reads events from a feed and processes them.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 * @see InterpreterFactory
 */
public interface Interpreter
        extends Chain {

    public static Interpreter getDefaultInstance() {
        return new InterpreterImpl();
    }

    /**
     * Fetches all events from the feed and publishes them.
     *
     * When an exception is thrown this algorithm will:
     * - sets the exception to the event;
     * - fire an {@link ExceptionThrownEvent};
     * - throws the exception, unless the {@code ExceptionThrownEvent} was consumed.
     *
     * @param feed the feed from which the evens are processed.
     * @thrown any exception thrown by an event, if that event was not consumed
     */
    public void execute(Feed feed);

    /**
     * Sets a listener to listen for feed executing events.
     *
     * @param listener the listener to set
     */
    public void setFeedExecutorListener(FeedExecutorListener listener);

    /**
     * Sets a listener to listen for interpreter events.
     *
     * @param listener the listener to set
     */
    public void setInterpreterListener(InterpreterListener listener);
}
