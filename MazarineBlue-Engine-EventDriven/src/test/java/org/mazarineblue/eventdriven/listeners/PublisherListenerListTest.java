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
import org.mazarineblue.eventdriven.util.listeners.ProcessorListenerSpy;
import static org.mazarineblue.eventnotifier.Event.matchesNoneAutoConsumable;
import org.mazarineblue.eventnotifier.events.TestEvent;
import org.mazarineblue.utilities.util.TestException;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class PublisherListenerListTest {

    private ProcessorListenerSpy spy1, spy2;
    private PublisherListenerList list;

    @Before
    public void setup() {
        spy1 = new ProcessorListenerSpy(matchesNoneAutoConsumable());
        spy2 = new ProcessorListenerSpy(matchesNoneAutoConsumable());
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
        list.startPublishingEvent(new TestEvent());
        list.removeListener(spy2);
        list.startPublishingEvent(new TestEvent());
        spy1.startEvents().assertClasses(TestEvent.class, TestEvent.class);
        spy2.startEvents().assertClasses(TestEvent.class);
        spy1.exceptions().assertClasses();
        spy2.exceptions().assertClasses();
        spy1.endEvents().assertClasses();
        spy2.endEvents().assertClasses();
    }

    @Test
    public void exceptionThrown() {
        list.exceptionThrown(new TestEvent(), new TestException());
        list.removeListener(spy2);
        list.exceptionThrown(new TestEvent(), new TestException());
        spy1.startEvents().assertClasses();
        spy2.startEvents().assertClasses();
        spy1.exceptions().assertClasses(TestException.class, TestException.class);
        spy2.exceptions().assertClasses(TestException.class);
        spy1.endEvents().assertClasses();
        spy2.endEvents().assertClasses();
    }

    @Test
    public void endPublishedEvent() {
        list.endPublishedEvent(new TestEvent());
        list.removeListener(spy2);
        list.endPublishedEvent(new TestEvent());
        spy1.startEvents().assertClasses();
        spy2.startEvents().assertClasses();
        spy1.exceptions().assertClasses();
        spy2.exceptions().assertClasses();
        spy1.endEvents().assertClasses(TestEvent.class, TestEvent.class);
        spy2.endEvents().assertClasses(TestEvent.class);
    }
}
