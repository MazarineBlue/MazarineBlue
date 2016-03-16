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
import org.mazarineblue.eventdriven.util.InterpreterListenerSpy;

public class InterpreterListenerListTest {

    private InterpreterListenerSpy spy1, spy2;
    private InterpreterListenerList list;

    @Before
    public void setup() {
        spy1 = new InterpreterListenerSpy();
        spy2 = new InterpreterListenerSpy();
        list = new InterpreterListenerList();
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
        list.openingFeed(null);
        list.removeListener(spy2);
        list.openingFeed(null);
        assertEquals(2, spy1.countOpeningFeed());
        assertEquals(1, spy2.countOpeningFeed());
        assertEquals(0, spy1.countClosingFeed());
        assertEquals(0, spy2.countClosingFeed());
        assertEquals(0, spy1.countStartingEvents());
        assertEquals(0, spy2.countStartingEvents());
        assertEquals(0, spy1.countExceptions());
        assertEquals(0, spy2.countExceptions());
        assertEquals(0, spy1.countEndingEvents());
        assertEquals(0, spy2.countEndingEvents());
        assertEquals(0, spy2.countLinks());
        assertEquals(0, spy2.countLinks());
    }

    @Test
    public void closingFeed() {
        list.closingFeed(null);
        list.removeListener(spy2);
        list.closingFeed(null);
        assertEquals(0, spy1.countOpeningFeed());
        assertEquals(0, spy2.countOpeningFeed());
        assertEquals(2, spy1.countClosingFeed());
        assertEquals(1, spy2.countClosingFeed());
        assertEquals(0, spy1.countStartingEvents());
        assertEquals(0, spy2.countStartingEvents());
        assertEquals(0, spy1.countExceptions());
        assertEquals(0, spy2.countExceptions());
        assertEquals(0, spy1.countEndingEvents());
        assertEquals(0, spy2.countEndingEvents());
        assertEquals(0, spy2.countLinks());
        assertEquals(0, spy2.countLinks());
    }

    @Test
    public void startPublishingEvent() {
        list.startPublishingEvent(null);
        list.removeListener(spy2);
        list.startPublishingEvent(null);
        assertEquals(0, spy1.countOpeningFeed());
        assertEquals(0, spy2.countOpeningFeed());
        assertEquals(0, spy1.countClosingFeed());
        assertEquals(0, spy2.countClosingFeed());
        assertEquals(2, spy1.countStartingEvents());
        assertEquals(1, spy2.countStartingEvents());
        assertEquals(0, spy1.countExceptions());
        assertEquals(0, spy2.countEndingEvents());
        assertEquals(0, spy1.countEndingEvents());
        assertEquals(0, spy2.countEndingEvents());
        assertEquals(0, spy2.countLinks());
        assertEquals(0, spy2.countLinks());
    }

    @Test
    public void exceptionThrown() {
        list.exceptionThrown(null, null);
        list.removeListener(spy2);
        list.exceptionThrown(null, null);
        assertEquals(0, spy1.countOpeningFeed());
        assertEquals(0, spy2.countOpeningFeed());
        assertEquals(0, spy1.countClosingFeed());
        assertEquals(0, spy2.countClosingFeed());
        assertEquals(0, spy1.countStartingEvents());
        assertEquals(0, spy2.countStartingEvents());
        assertEquals(2, spy1.countExceptions());
        assertEquals(1, spy2.countExceptions());
        assertEquals(0, spy1.countEndingEvents());
        assertEquals(0, spy2.countEndingEvents());
        assertEquals(0, spy2.countLinks());
        assertEquals(0, spy2.countLinks());
    }

    @Test
    public void endPublishedEvent() {
        list.endPublishedEvent(null);
        list.removeListener(spy2);
        list.endPublishedEvent(null);
        assertEquals(0, spy1.countOpeningFeed());
        assertEquals(0, spy2.countOpeningFeed());
        assertEquals(0, spy1.countClosingFeed());
        assertEquals(0, spy2.countClosingFeed());
        assertEquals(0, spy1.countStartingEvents());
        assertEquals(0, spy2.countStartingEvents());
        assertEquals(0, spy1.countExceptions());
        assertEquals(0, spy2.countExceptions());
        assertEquals(2, spy1.countEndingEvents());
        assertEquals(1, spy2.countEndingEvents());
        assertEquals(0, spy2.countLinks());
        assertEquals(0, spy2.countLinks());
    }

    @Test
    public void addedLink() {
        list.addedLink(null);
        list.removeListener(spy2);
        list.addedLink(null);
        assertEquals(0, spy1.countOpeningFeed());
        assertEquals(0, spy2.countOpeningFeed());
        assertEquals(0, spy1.countClosingFeed());
        assertEquals(0, spy2.countClosingFeed());
        assertEquals(0, spy1.countStartingEvents());
        assertEquals(0, spy2.countStartingEvents());
        assertEquals(0, spy1.countExceptions());
        assertEquals(0, spy2.countExceptions());
        assertEquals(0, spy1.countEndingEvents());
        assertEquals(0, spy2.countEndingEvents());
        assertEquals(2, spy1.countLinks());
        assertEquals(1, spy2.countLinks());
    }

    @Test
    public void addedLink_AfterLink() {
        list.addedLink(null, null);
        list.removeListener(spy2);
        list.addedLink(null, null);
        assertEquals(0, spy1.countOpeningFeed());
        assertEquals(0, spy2.countOpeningFeed());
        assertEquals(0, spy1.countClosingFeed());
        assertEquals(0, spy2.countClosingFeed());
        assertEquals(0, spy1.countStartingEvents());
        assertEquals(0, spy2.countStartingEvents());
        assertEquals(0, spy1.countExceptions());
        assertEquals(0, spy2.countExceptions());
        assertEquals(0, spy1.countEndingEvents());
        assertEquals(0, spy2.countEndingEvents());
        assertEquals(2, spy1.countLinks());
        assertEquals(1, spy2.countLinks());
    }

    @Test
    public void removedLink() {
        list.addedLink(null);
        list.addedLink(null);
        list.removeListener(spy2);
        list.removedLink(null);
        assertEquals(0, spy1.countOpeningFeed());
        assertEquals(0, spy2.countOpeningFeed());
        assertEquals(0, spy1.countClosingFeed());
        assertEquals(0, spy2.countClosingFeed());
        assertEquals(0, spy1.countStartingEvents());
        assertEquals(0, spy2.countStartingEvents());
        assertEquals(0, spy1.countExceptions());
        assertEquals(0, spy2.countExceptions());
        assertEquals(0, spy1.countEndingEvents());
        assertEquals(0, spy2.countEndingEvents());
        assertEquals(1, spy1.countLinks());
        assertEquals(2, spy2.countLinks());
    }
}
