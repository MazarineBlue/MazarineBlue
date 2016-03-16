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
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.eventdriven.util.PublisherListenerSpy;

public class PublisherListenerListTest {

    private PublisherListenerSpy spy1, spy2;
    private PublisherListenerList list;

    @Before
    public void setup() {
        spy1 = new PublisherListenerSpy();
        spy2 = new PublisherListenerSpy();
        list = new PublisherListenerList();
        list.addListener(spy1);
        list.addListener(spy2);
    }

    @After
    public void teardown() {
        spy1 = spy2 = null;
        list = null;
    }

    @Test
    public void startPublishingEvent() {
        list.startPublishingEvent(null);
        list.removeListener(spy2);
        list.startPublishingEvent(null);
        assertEquals(2, spy1.countStartEvents());
        assertEquals(1, spy2.countStartEvents());
        assertEquals(0, spy1.countExceptions());
        assertEquals(0, spy2.countEndEvents());
        assertEquals(0, spy1.countEndEvents());
        assertEquals(0, spy2.countEndEvents());
    }

    @Test
    public void exceptionThrown() {
        list.exceptionThrown(null, null);
        list.removeListener(spy2);
        list.exceptionThrown(null, null);
        assertEquals(0, spy1.countStartEvents());
        assertEquals(0, spy2.countStartEvents());
        assertEquals(2, spy1.countExceptions());
        assertEquals(1, spy2.countExceptions());
        assertEquals(0, spy1.countEndEvents());
        assertEquals(0, spy2.countEndEvents());
    }

    @Test
    public void endPublishedEvent() {
        list.endPublishedEvent(null);
        list.removeListener(spy2);
        list.endPublishedEvent(null);
        assertEquals(0, spy1.countStartEvents());
        assertEquals(0, spy2.countStartEvents());
        assertEquals(0, spy1.countExceptions());
        assertEquals(0, spy2.countExceptions());
        assertEquals(2, spy1.countEndEvents());
        assertEquals(1, spy2.countEndEvents());
    }
}
