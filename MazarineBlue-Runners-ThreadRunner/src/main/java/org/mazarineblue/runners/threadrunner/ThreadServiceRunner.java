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

import org.mazarineblue.eventnotifier.Event;
import org.mazarineblue.executors.ExecutorFactory;
import org.mazarineblue.runners.threadrunner.events.ByeEvent;
import org.mazarineblue.runners.threadrunner.events.KillEvent;
import org.mazarineblue.runners.threadrunner.exceptions.TimeoutException;
import org.mazarineblue.utililities.TwoWayPipe;
import org.mazarineblue.utililities.exceptions.InterruptedRuntimeException;

class ThreadServiceRunner
        extends Thread
        implements ServiceRunner {

    private final TwoWayPipe<Event> pipe;
    private final TwoWayPipeFeed feed;
    private final ExecutorFactory factory;
    private final int timeout;

    ThreadServiceRunner(TwoWayPipe<Event> pipe, ExecutorFactory factory, int timeout) {
        this.pipe = pipe.redirect();
        this.feed = new TwoWayPipeFeed(pipe);
        this.factory = factory;
        this.timeout = timeout;
    }

    @Override
    public void run() {
        factory.create().execute(feed);
    }

    @Override
    public void bye() {
        try {
            pipe.write(new ByeEvent());
            join();
        } catch (InterruptedException ex) {
            pipe.clear();
            currentThread().interrupt();
            throw new InterruptedRuntimeException(ex);
        }
    }

    @Override
    public void kill() {
        try {
            if (!pipe.write(new KillEvent(), timeout))
                throw new TimeoutException(timeout);
            join(timeout);
        } catch (InterruptedException ex) {
            pipe.clear();
            currentThread().interrupt();
            throw new InterruptedRuntimeException(ex);
        }
    }
}
