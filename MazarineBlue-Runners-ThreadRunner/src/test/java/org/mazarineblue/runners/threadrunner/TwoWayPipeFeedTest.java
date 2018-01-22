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

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mazarineblue.eventnotifier.Event;
import org.mazarineblue.eventnotifier.events.TestEvent;
import org.mazarineblue.runners.threadrunner.events.ByeEvent;
import org.mazarineblue.runners.threadrunner.events.KillEvent;
import org.mazarineblue.utililities.BlockingTwoWayPipeFactory;
import org.mazarineblue.utililities.TwoWayPipe;
import org.mazarineblue.utililities.exceptions.InterruptedRuntimeException;
import org.mazarineblue.utililities.util.TestThread;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
@RunWith(HierarchicalContextRunner.class)
@SuppressWarnings("UtilityClassWithoutPrivateConstructor")
public class TwoWayPipeFeedTest { // bank

    private static final int TIMEOUT = 1000;
    private static final int DOZE = 50;
    private static final int CAPACITY = 16;

    private TwoWayPipe<Event> pipe, redirect;
    private TwoWayPipeFeed feed;

    @Before
    public void setup() {
        pipe = new BlockingTwoWayPipeFactory<Event>().createPipe(CAPACITY);
        redirect = pipe.redirect();
        feed = new TwoWayPipeFeed(pipe);
    }

    @After
    public void teardown() {
        pipe = redirect = null;
        feed = null;
    }

    @Test
    public void hasNext_NoEventInPipe_ReturnsTrue() {
        assertTrue(feed.hasNext());
    }

    @Test(timeout = TIMEOUT)
    public void hasNext_EventAfterBye_ReturnsTrue()
            throws InterruptedException {
        redirect.write(new ByeEvent());
        redirect.write(new TestEvent());
        feed.next();
        assertTrue(feed.hasNext());
    }

    @Test(timeout = TIMEOUT)
    public void hasNext_EventAfterByeFetched_ReturnsFalse()
            throws InterruptedException {
        redirect.write(new ByeEvent());
        redirect.write(new TestEvent());
        feed.next();
        feed.next();
        assertFalse(feed.hasNext());
    }

    @Test(timeout = TIMEOUT)
    public void hasNext_EventAfterKill_ReturnsFalse()
            throws InterruptedException {
        redirect.write(new KillEvent());
        redirect.write(new TestEvent());
        feed.next();
        assertFalse(feed.hasNext());
    }

    @Test(timeout = TIMEOUT, expected = InterruptedRuntimeException.class)
    public void next_InterruptedWhileWaitingOnRead_ThrowsException()
            throws Throwable {
        TestThread thread = new TestThread(DOZE, feed::next);
        thread.start();
        thread.waitTillStarted();
        thread.interrupt();
        thread.waitForThrowable();
        throw thread.getCause();
    }

    @Test(timeout = TIMEOUT)
    public void next_EventReadFromPipe()
            throws InterruptedException {
        Event e = new TestEvent();
        redirect.write(e);
        assertEquals(e, feed.next());
    }

    @Test(timeout = TIMEOUT, expected = InterruptedRuntimeException.class)
    public void done_InterruptedWhileWaitingOnRead_ThrowsException()
            throws Throwable {
        fillPipe();
        TestThread thread = new TestThread(DOZE, () -> feed.done(new TestEvent()));
        thread.start();
        thread.waitTillStarted();
        thread.interrupt();
        thread.waitForThrowable();
        throw thread.getCause();
    }

    private void fillPipe()
            throws InterruptedException {
        for (int i = 0; i < CAPACITY; ++i)
            pipe.write(new TestEvent());
    }

    @Test(timeout = TIMEOUT)
    public void done_EventWriteToPipe()
            throws InterruptedException {
        Event e = new TestEvent();
        feed.done(e);
        assertEquals(e, redirect.read(Event.class));
    }
}
