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
package org.mazarineblue.eventdriven;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.mazarineblue.eventdriven.exceptions.NoEventsLeftException;
import org.mazarineblue.eventdriven.feeds.MemoryFeed;
import org.mazarineblue.eventnotifier.events.TestEvent;

public class MemoryFeedTest {

    @Test
    public void init_VariableArguments() {
        MemoryFeed feed = new MemoryFeed(new TestEvent(), new TestEvent());
        assertContainsEvent(feed, 2);
    }

    @Test
    public void init_Collection() {
        MemoryFeed feed = new MemoryFeed(asList(new TestEvent()));
        assertContainsEvent(feed, 1);
    }

    @Test
    public void init_EmptyFeed() {
        MemoryFeed feed = new MemoryFeed(4);
        assertFalse(feed.hasNext());
    }

    @Test(expected = NoEventsLeftException.class)
    public void next_EmptyFeed() {
        new MemoryFeed(4).next();
    }

    @Test
    public void add() {
        MemoryFeed feed = new MemoryFeed(4);
        feed.add(new TestEvent(), new TestEvent());
        assertContainsEvent(feed, 2);
    }

    @Test
    public void addAll_MemoryFeed() {
        MemoryFeed feed = new MemoryFeed();
        feed.addAll(new MemoryFeed(new TestEvent()));
        assertContainsEvent(feed, 1);
    }

    @Test
    public void addAll_Collection() {
        MemoryFeed feed = new MemoryFeed();
        feed.addAll(asList(new TestEvent()));
        assertContainsEvent(feed, 1);
    }

    private void assertContainsEvent(MemoryFeed feed, int nEvents) {
        for (int i = 0; i < nEvents; ++i) {
            assertTrue(feed.hasNext());
            feed.next();
        }
        assertFalse(feed.hasNext());
    }
}
