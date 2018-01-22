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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.eventdriven.feeds.MemoryFeed;
import org.mazarineblue.eventdriven.util.listeners.ProcessorListenerSpy;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class FeedExecutorListenerListTest {

    private ProcessorListenerSpy spy1, spy2;
    private FeedExecutorListenerList list;

    @Before
    public void setup() {
        spy1 = new ProcessorListenerSpy();
        spy2 = new ProcessorListenerSpy();
        list = new FeedExecutorListenerList();
        list.addListener(spy1);
        list.addListener(spy2);
    }

    @After
    public void teardown() {
        spy1 = spy2 = null;
        list = null;
    }

    @Test
    public void openingFeed() {
        list.openingFeed(new MemoryFeed());
        list.removeListener(spy2);
        list.openingFeed(new MemoryFeed());
        spy1.openedFeeds().assertClasses(MemoryFeed.class, MemoryFeed.class);
        spy2.openedFeeds().assertClasses(MemoryFeed.class);
        spy1.closedFeeds().assertClasses();
        spy2.closedFeeds().assertClasses();
    }

    @Test
    public void closingFeed() {
        list.closingFeed(new MemoryFeed());
        list.removeListener(spy2);
        list.closingFeed(new MemoryFeed());
        spy1.openedFeeds().assertClasses();
        spy2.openedFeeds().assertClasses();
        spy1.closedFeeds().assertClasses(MemoryFeed.class, MemoryFeed.class);
        spy2.closedFeeds().assertClasses(MemoryFeed.class);
    }
}
