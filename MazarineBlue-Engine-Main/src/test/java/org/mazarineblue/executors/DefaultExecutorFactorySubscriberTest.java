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
package org.mazarineblue.executors;

import org.junit.After;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mazarineblue.eventdriven.feeds.MemoryFeed;
import org.mazarineblue.executors.events.CreateFeedExecutorEvent;
import org.mazarineblue.executors.util.FeedExecutorOutputSpy;
import org.mazarineblue.executors.util.TestFeedExecutorFactory;
import org.mazarineblue.fs.MemoryFileSystem;

public class DefaultExecutorFactorySubscriberTest {

    private MemoryFileSystem fs;
    private FeedExecutorOutputSpy output;
    private ExecutorFactory factory;
    private Executor executor;

    @Before
    public void setup() {
        fs = new MemoryFileSystem();
        output = new FeedExecutorOutputSpy();
        factory = TestFeedExecutorFactory.newInstance(fs, output);
        executor = factory.create();
    }

    @After
    public void teardown() {
        fs = null;
        output = null;
        factory = null;
        executor = null;
    }

    @Test
    public void test() {
        CreateFeedExecutorEvent<Executor> e = new CreateFeedExecutorEvent();
        MemoryFeed feed = new MemoryFeed(e);
        executor.execute(feed);
        assertTrue(e.isConsumed());
        Executor result = e.getResult();
        assertNotEquals(null, result);
        assertNotEquals(factory, result);
    }

    @Test
    @Ignore("Memory test, time consuming")
    public void testMemory() {
        Runtime runtime = Runtime.getRuntime();
        {
            CreateFeedExecutorEvent e = new CreateFeedExecutorEvent();
            MemoryFeed feed = new MemoryFeed(e);
            executor.execute(feed);
            output.clear();
        }

        runtime.gc();
        long freeMemory = runtime.freeMemory();
        long start = freeMemory;
        long lowest = freeMemory;
        for (int i = 0; i < 1000000; ++i) {
            CreateFeedExecutorEvent e = new CreateFeedExecutorEvent();
            MemoryFeed feed = new MemoryFeed(e);
            executor.execute(feed);
            output.clear();
            fs.deleteAll();
            System.gc();
            runtime.gc();
            freeMemory = runtime.freeMemory();
            if (lowest > freeMemory) {
                lowest = freeMemory;
                long p = 100 * (start - freeMemory) / start;
                System.err.println("Memory=" + freeMemory + " used= " + p + "% (" + (start - freeMemory) + ")");
            }
        }
        long end = runtime.freeMemory();
        long bytes = start - end;
        long kb = bytes/1024;
        long mb = kb/1024;
        long gb = mb/1024;
        System.out.println();
    }
}
