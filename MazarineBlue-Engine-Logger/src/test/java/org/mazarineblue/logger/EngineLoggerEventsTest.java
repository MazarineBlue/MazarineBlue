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
package org.mazarineblue.logger;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.eventdriven.Processor;
import org.mazarineblue.eventdriven.feeds.MemoryFeed;
import org.mazarineblue.eventdriven.util.events.TestInvokerEvent;
import org.mazarineblue.eventdriven.util.listeners.ProcessorListenerSpy;
import org.mazarineblue.eventnotifier.Event;
import org.mazarineblue.eventnotifier.subscribers.ConsumeEventsSubscriber;
import org.mazarineblue.eventnotifier.subscribers.DummySubscriber;
import org.mazarineblue.eventnotifier.subscribers.ExceptionThrowingSubscriber;
import org.mazarineblue.eventnotifier.subscribers.PerformActionSubscriber;
import org.mazarineblue.logger.events.AddChainModifierListenerEvent;
import org.mazarineblue.logger.events.AddFeedExecutorListenerEvent;
import org.mazarineblue.logger.events.AddPublisherListenerEvent;
import org.mazarineblue.logger.events.RemoveChainModifierListenerEvent;
import org.mazarineblue.logger.events.RemoveFeedExecutorListenerEvent;
import org.mazarineblue.logger.events.RemovePublisherListenerEvent;
import org.mazarineblue.util.TestDateFactory;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class EngineLoggerEventsTest {

    private EngineLogger logger;
    private Processor processor;
    private ProcessorListenerSpy listener1, listener2;

    @Before
    public void setup() {
        logger = new EngineLogger(new TestDateFactory());
        processor = Processor.newInstance();
        processor.addLink(new ConsumeEventsSubscriber<>());
        processor.addLink(logger);
        processor.setChainModifierListener(logger);
        processor.setFeedExecutorListener(logger);
        processor.setPublisherListener(logger);
        listener1 = new ProcessorListenerSpy();
        listener2 = new ProcessorListenerSpy();
    }

    @After
    public void teardown() {
        logger = null;
        processor = null;
        listener1 = listener2 = null;
    }

    @Test
    public void testChainModifierListener_InitialTest() {
        Event[] initialEvents = new Event[]{
            new AddChainModifierListenerEvent(listener1),
            new TestInvokerEvent()
        };
        Event nestedEvents = new TestInvokerEvent();
        processor.addLink(new PerformActionSubscriber(e -> processor.addLink(new DummySubscriber<>()),
                e -> TestInvokerEvent.class.isAssignableFrom(e.getClass())));
        processor.addLink(new PerformActionSubscriber(e -> processor.execute(new MemoryFeed(nestedEvents)),
                new OnlyOnceUpOnPredicate(TestInvokerEvent.class)));
        processor.execute(new MemoryFeed(initialEvents));
        assertEquals(0, listener1.openedFeeds().size());
        assertEquals(0, listener1.closedFeeds().size());
        assertEquals(0, listener1.startEvents().size());
        assertEquals(0, listener1.exceptions().size());
        assertEquals(0, listener1.endEvents().size());
        assertEquals(2, listener1.links().size());
    }

    @Test
    public void testChainModifierListener_AddLink_NestedFeed() {
        Event[] initialEvents = new Event[]{
            new AddChainModifierListenerEvent(listener1),
            new TestInvokerEvent(),
            new RemoveChainModifierListenerEvent(listener1),
            new TestInvokerEvent()
        };

        processor.addLink(new PerformActionSubscriber(t -> processor.addLink(new DummySubscriber<>()),
                                                  t -> TestInvokerEvent.class.isAssignableFrom(t.getClass())));
        processor.execute(new MemoryFeed(initialEvents));
        assertEquals(0, listener1.openedFeeds().size());
        assertEquals(0, listener1.closedFeeds().size());
        assertEquals(0, listener1.startEvents().size());
        assertEquals(0, listener1.exceptions().size());
        assertEquals(0, listener1.endEvents().size());
        assertEquals(1, listener1.links().size());
    }

    @Test
    public void testChainModifierListener_AddLinkAfter_NestedFeed() {
        Event[] initialEvents = new Event[]{
            new AddChainModifierListenerEvent(listener1),
            new TestInvokerEvent(),
            new RemoveChainModifierListenerEvent(listener1),
            new TestInvokerEvent()
        };

        processor.addLink(new PerformActionSubscriber(t -> processor.addLink(new DummySubscriber<>(), logger),
                                                  t -> TestInvokerEvent.class.isAssignableFrom(t.getClass())));
        processor.execute(new MemoryFeed(initialEvents));
        assertEquals(0, listener1.openedFeeds().size());
        assertEquals(0, listener1.closedFeeds().size());
        assertEquals(0, listener1.startEvents().size());
        assertEquals(0, listener1.exceptions().size());
        assertEquals(0, listener1.endEvents().size());
        assertEquals(1, listener1.links().size());
    }

    @Test
    public void testChainModifierListener_RemoveLink_NestedFeed() {
        Event[] initialEvents = new Event[]{
            new AddChainModifierListenerEvent(listener1),
            new TestInvokerEvent(),
            new RemoveChainModifierListenerEvent(listener1),
            new TestInvokerEvent()
        };

        DummySubscriber<Event> dummySubscriber = new DummySubscriber<>();
        processor.addLink(dummySubscriber);
        processor.addLink(new PerformActionSubscriber(t -> processor.removeLink(dummySubscriber),
                                                  new OnlyOnceUpOnPredicate(TestInvokerEvent.class)));
        processor.execute(new MemoryFeed(initialEvents));
        assertEquals(0, listener1.openedFeeds().size());
        assertEquals(0, listener1.closedFeeds().size());
        assertEquals(0, listener1.startEvents().size());
        assertEquals(0, listener1.exceptions().size());
        assertEquals(0, listener1.endEvents().size());
        assertEquals(0, listener1.links().size());
    }

    @Test
    public void testFeedExecutorListener_InitialTest() {
        Event[] initialEvents = new Event[]{
            new AddFeedExecutorListenerEvent(listener1),
            new TestInvokerEvent()
        };
        Event nestedEvents = new TestInvokerEvent();
        processor.addLink(new PerformActionSubscriber(t -> processor.execute(new MemoryFeed(nestedEvents)),
                                                  new OnlyOnceUpOnPredicate(TestInvokerEvent.class)));
        processor.execute(new MemoryFeed(initialEvents));
        assertEquals(1, listener1.openedFeeds().size());
        assertEquals(1, listener1.closedFeeds().size());
        assertEquals(0, listener1.startEvents().size());
        assertEquals(0, listener1.exceptions().size());
        assertEquals(0, listener1.endEvents().size());
        assertEquals(0, listener1.links().size());
    }

    @Test
    public void testFeedExecutorListener_ThrownException() {
        Event[] initialEvents = new Event[]{
            new AddFeedExecutorListenerEvent(listener1),
            new TestInvokerEvent(),
            new RemoveFeedExecutorListenerEvent(listener1),
            new TestInvokerEvent()
        };
        processor.addLink(new ExceptionThrowingSubscriber<>(new OnlyOnceUpOnPredicate(TestInvokerEvent.class),
                                                            e -> new RuntimeException()));
        processor.execute(new MemoryFeed(initialEvents));
        assertEquals(0, listener1.openedFeeds().size());
        assertEquals(0, listener1.closedFeeds().size());
        assertEquals(0, listener1.startEvents().size());
        assertEquals(0, listener1.exceptions().size());
        assertEquals(0, listener1.endEvents().size());
        assertEquals(0, listener1.links().size());
    }

    @Test
    public void testFeedExecutorListener_NestedFeed() {
        Event[] initialEvents = new Event[]{
            new AddFeedExecutorListenerEvent(listener1),
            new TestInvokerEvent(),
            new RemoveFeedExecutorListenerEvent(listener1),
            new TestInvokerEvent()
        };
        Event[] nestedEvents = new Event[]{
            new AddFeedExecutorListenerEvent(listener2),
            new TestInvokerEvent(),
            new RemoveFeedExecutorListenerEvent(listener2)
        };
        processor.addLink(new PerformActionSubscriber(t -> processor.execute(new MemoryFeed(nestedEvents)),
                                                  new OnlyOnceUpOnPredicate(TestInvokerEvent.class)));
        processor.execute(new MemoryFeed(initialEvents));
        assertEquals(1, listener1.openedFeeds().size());
        assertEquals(1, listener1.closedFeeds().size());
        assertEquals(0, listener1.startEvents().size());
        assertEquals(0, listener1.exceptions().size());
        assertEquals(0, listener1.endEvents().size());
        assertEquals(0, listener1.links().size());
        assertEquals(0, listener2.openedFeeds().size());
        assertEquals(0, listener2.closedFeeds().size());
        assertEquals(0, listener2.startEvents().size());
        assertEquals(0, listener2.exceptions().size());
        assertEquals(0, listener2.endEvents().size());
        assertEquals(0, listener2.links().size());
    }

    @Test
    public void testPublisherListener_InitialTest() {
        Event[] initialEvents = new Event[]{
            new AddPublisherListenerEvent(listener1),
            new TestInvokerEvent()
        };
        Event nestedEvents = new TestInvokerEvent();
        processor.addLink(new PerformActionSubscriber(t -> processor.execute(new MemoryFeed(nestedEvents)),
                                                  new OnlyOnceUpOnPredicate(TestInvokerEvent.class)));
        processor.execute(new MemoryFeed(initialEvents));
        assertEquals(0, listener1.openedFeeds().size());
        assertEquals(0, listener1.closedFeeds().size());
        assertEquals(2, listener1.startEvents().size());
        assertEquals(0, listener1.exceptions().size());
        assertEquals(2, listener1.endEvents().size());
        assertEquals(0, listener1.links().size());
    }

    @Test
    public void testPublisherListener_NestedFeed() {
        Event[] initialEvents = new Event[]{
            new AddPublisherListenerEvent(listener1),
            new TestInvokerEvent(),
            new RemovePublisherListenerEvent(listener1),
            new TestInvokerEvent()
        };
        Event[] nestedEvents = new Event[]{
            new AddPublisherListenerEvent(listener2),
            new TestInvokerEvent(),
            new RemovePublisherListenerEvent(listener2)
        };
        processor.addLink(new PerformActionSubscriber(t -> processor.execute(new MemoryFeed(nestedEvents)),
                                                  new OnlyOnceUpOnPredicate(TestInvokerEvent.class)));
        processor.execute(new MemoryFeed(initialEvents));
        assertEquals(0, listener1.openedFeeds().size());
        assertEquals(0, listener1.closedFeeds().size());
        assertEquals(2, listener1.startEvents().size());
        assertEquals(0, listener1.exceptions().size());
        assertEquals(2, listener1.endEvents().size());
        assertEquals(0, listener1.links().size());
        assertEquals(0, listener2.openedFeeds().size());
        assertEquals(0, listener2.closedFeeds().size());
        assertEquals(1, listener2.startEvents().size());
        assertEquals(0, listener2.exceptions().size());
        assertEquals(1, listener2.endEvents().size());
        assertEquals(0, listener2.links().size());
    }
}
