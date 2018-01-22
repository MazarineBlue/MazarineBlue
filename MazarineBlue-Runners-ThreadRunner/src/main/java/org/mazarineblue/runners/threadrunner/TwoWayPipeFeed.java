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
package org.mazarineblue.runners.threadrunner;

import static java.lang.Thread.currentThread;
import org.mazarineblue.eventdriven.Feed;
import org.mazarineblue.eventnotifier.Event;
import org.mazarineblue.runners.threadrunner.events.ByeEvent;
import org.mazarineblue.runners.threadrunner.events.KillEvent;
import org.mazarineblue.utililities.TwoWayPipe;
import org.mazarineblue.utililities.exceptions.InterruptedRuntimeException;

/**
 * A {@code TwoWayPipeFeed} is a {@code Feed} that reads {@link Event events}
 * from a {@code TwoWayPipe} and, when it client is done, writes the (updated)
 * {@code Event} to the pipe.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class TwoWayPipeFeed
        implements Feed {

    private final TwoWayPipe<Event> pipe;
    private boolean bye;
    private boolean kill;

    /**
     * Constructs a {@code TwoWayPipeFeed} using the {@code TwoWayPipe} as an
     * {@code Event} source.
     *
     * @param pipe the pipe to read events from and write the (updated) events
     *             to, when done.
     */
    public TwoWayPipeFeed(TwoWayPipe<Event> pipe) {
        this.pipe = pipe;
    }

    @Override
    public String toString() {
        return pipe.toString();
    }

    @Override
    public boolean hasNext() {
        return kill ? false : !bye || pipe.isReadable();
    }

    @Override
    public Event next() {
        try {
            Event e = pipe.read();
            if (e instanceof ByeEvent)
                bye = true;
            else if (e instanceof KillEvent)
                kill = true;
            return e;
        } catch (InterruptedException ex) {
            pipe.clear();
            currentThread().interrupt();
            throw new InterruptedRuntimeException(ex);
        }
    }

    @Override
    public void done(Event event) {
        try {
            pipe.write(event);
        } catch (InterruptedException ex) {
            pipe.clear();
            currentThread().interrupt();
            throw new InterruptedRuntimeException(ex);
        }
    }

    @Override
    public void reset() {
        // This feed sends messages and thus it can not reset.
    }
}
