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

import org.junit.After;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.eventdriven.events.ExceptionThrownEvent;
import org.mazarineblue.eventdriven.exceptions.NullLinkException;
import org.mazarineblue.eventdriven.feeds.MemoryFeed;
import org.mazarineblue.eventdriven.util.events.TestInvokerEvent;
import org.mazarineblue.eventdriven.util.listeners.ProcessorListenerSpy;
import org.mazarineblue.eventnotifier.Event;
import org.mazarineblue.eventnotifier.exceptions.SubscriberNotRegisteredException;
import org.mazarineblue.eventnotifier.subscribers.ConsumeEventsSubscriber;
import org.mazarineblue.eventnotifier.subscribers.ExceptionThrowingSubscriber;
import org.mazarineblue.utilities.util.TestException;

public class ChainManagerTest {

    private Processor processor;
    private ProcessorListenerSpy spy;

    @Before
    public void setup() {
        spy = new ProcessorListenerSpy(t -> true);
        processor = Processor.newInstance();
        processor.setChainModifierListener(spy);
        processor.setFeedExecutorListener(spy);
        processor.setPublisherListener(spy);
    }

    @After
    public void teardown() {
        processor = null;
    }
    @Test(expected = NullLinkException.class)
    public void addLink_Null() {
        processor.addLink(null);
    }

    @Test
    public void addLink() {
        Event e = new TestInvokerEvent();
        processor.addLink(new ConsumeEventsSubscriber<>());
        processor.execute(new MemoryFeed(e));
        assertTrue(e.isConsumed());
        spy.links().assertClasses(ConsumeEventsSubscriber.class);
        spy.openedFeeds().assertClasses(MemoryFeed.class);
        spy.closedFeeds().assertClasses(MemoryFeed.class);
        spy.startEvents().assertClasses(TestInvokerEvent.class);
        spy.exceptions().assertClasses();
        spy.endEvents().assertClasses(TestInvokerEvent.class);
    }

    @Test(expected = NullLinkException.class)
    public void addLink_Null_After() {
        processor.addLink(null, new ConsumeEventsSubscriber<>());
    }

    @Test(expected = SubscriberNotRegisteredException.class)
    public void addLink_After_Null() {
        processor.addLink(new ConsumeEventsSubscriber<>(), null);
    }

    @Test
    public void addLink_After() {
        Event e = new TestInvokerEvent();
        ConsumeEventsSubscriber<Event> after = new ConsumeEventsSubscriber<>();
        processor.addLink(after);
        processor.addLink(new ExceptionThrowingSubscriber<>(t->true, t -> new TestException()), after);
        processor.execute(new MemoryFeed(e));
        assertTrue(e.isConsumed());
        spy.links().assertClasses(ConsumeEventsSubscriber.class, ExceptionThrowingSubscriber.class);
        spy.openedFeeds().assertClasses(MemoryFeed.class);
        spy.closedFeeds().assertClasses(MemoryFeed.class);
        spy.startEvents().assertClasses(TestInvokerEvent.class);
        spy.exceptions().assertClasses();
        spy.endEvents().assertClasses(TestInvokerEvent.class);
    }

    @Test(expected = SubscriberNotRegisteredException.class)
    public void removeLink_Null() {
        processor.removeLink(null);
    }

    @Test(expected = TestException.class)
    public void removeLink() {
        Event e = new TestInvokerEvent();
        try {
            ConsumeEventsSubscriber<Event> after = new ConsumeEventsSubscriber<>();
            processor.addLink(after);
            processor.addLink(new ExceptionThrowingSubscriber<>(t->true, t -> new TestException()));
            processor.removeLink(after);
            processor.execute(new MemoryFeed(e));
        } finally {
            assertFalse(e.isConsumed());
            spy.links().assertClasses(ExceptionThrowingSubscriber.class);
            spy.openedFeeds().assertClasses(MemoryFeed.class);
            spy.closedFeeds().assertClasses(MemoryFeed.class);
            spy.startEvents().assertClasses(TestInvokerEvent.class, ExceptionThrownEvent.class);
            spy.exceptions().assertClasses(TestException.class, TestException.class);
            spy.endEvents().assertClasses(TestInvokerEvent.class, ExceptionThrownEvent.class);
        }
    }
}
