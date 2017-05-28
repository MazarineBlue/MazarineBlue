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

import java.util.function.Supplier;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.eventbus.Event;
import org.mazarineblue.eventbus.link.EventBusLink;
import org.mazarineblue.eventdriven.ExceptionThrowingLink;
import org.mazarineblue.eventdriven.Interpreter;
import org.mazarineblue.eventdriven.feeds.MemoryFeed;
import org.mazarineblue.eventdriven.util.DummyLink;
import org.mazarineblue.eventdriven.util.InterpreterListenerSpy;
import org.mazarineblue.eventdriven.util.PerformActionLink;
import org.mazarineblue.eventdriven.util.TestInvokerEvent;
import org.mazarineblue.links.ConsumeEventsLink;
import org.mazarineblue.logger.events.AddChainModifierListenerEvent;
import org.mazarineblue.logger.events.AddFeedExecutorListenerEvent;
import org.mazarineblue.logger.events.AddInterpreterListenerEvent;
import org.mazarineblue.logger.events.AddPublisherListenerEvent;
import org.mazarineblue.logger.events.EngineLoggerEvent;
import org.mazarineblue.logger.events.RemoveChainModifierListenerEvent;
import org.mazarineblue.logger.events.RemoveFeedExecutorListenerEvent;
import org.mazarineblue.logger.events.RemoveInterpreterListenerEvent;
import org.mazarineblue.logger.events.RemovePublisherListenerEvent;
import org.mazarineblue.util.TestDateFactory;

public class EngineLoggerEventsTest {

    private EngineLogger logger;
    private EventBusLink eventBus;
    private Interpreter interpreter;
    private InterpreterListenerSpy listener1, listener2;

    @Before
    public void setup() {
        logger = new EngineLogger(new TestDateFactory());
        eventBus = new EventBusLink(EngineLoggerEvent.class, null, logger);
        interpreter = Interpreter.getDefaultInstance();
        interpreter.addLink(new ConsumeEventsLink());
        interpreter.addLink(eventBus);
        interpreter.setInterpreterListener(logger);
        listener1 = new InterpreterListenerSpy();
        listener2 = new InterpreterListenerSpy();
    }

    @After
    public void teardown() {
        logger = null;
        eventBus = null;
        interpreter = null;
        listener1 = listener2 = null;
    }

    @Test
    public void testInterpreterListener_InitialTest() {
        Event[] initialEvents = new Event[] {
            new AddInterpreterListenerEvent(listener1),
            new TestInvokerEvent()
        };
        Event nestedEvents = new TestInvokerEvent();
        interpreter.addLink(new PerformActionLink(t -> interpreter.execute(new MemoryFeed(nestedEvents)),
                                                  new OnlyOnceUpOnCondition(TestInvokerEvent.class)));
        interpreter.execute(new MemoryFeed(initialEvents));
        assertListenerEquals(listener1, 1, 1, 2, 0, 2, 0);
    }

    @Test
    public void testInterpreterListener_NestedFeed() {
        Event[] initialEvents = new Event[]{
            new AddInterpreterListenerEvent(listener1),
            new TestInvokerEvent(),
            new RemoveInterpreterListenerEvent(listener1),
            new TestInvokerEvent()
        };
        Event[] nestedEvents = new Event[]{
            new AddInterpreterListenerEvent(listener2),
            new TestInvokerEvent(),
            new RemoveInterpreterListenerEvent(listener2)
        };
        interpreter.addLink(new PerformActionLink(t -> interpreter.execute(new MemoryFeed(nestedEvents)),
                                                  new OnlyOnceUpOnCondition(TestInvokerEvent.class)));
        interpreter.execute(new MemoryFeed(initialEvents));
        assertListenerEquals(listener1, 1, 1, 2, 0, 2, 0);
        assertListenerEquals(listener2, 0, 0, 1, 0, 1, 0);
    }

    @Test
    public void testFeedExecutorListener_InitialTest() {
        Event[] initialEvents = new Event[]{
            new AddFeedExecutorListenerEvent(listener1),
            new TestInvokerEvent()
        };
        Event nestedEvents = new TestInvokerEvent();
        interpreter.addLink(new PerformActionLink(t -> interpreter.execute(new MemoryFeed(nestedEvents)),
                                                  new OnlyOnceUpOnCondition(TestInvokerEvent.class)));
        interpreter.execute(new MemoryFeed(initialEvents));
        assertListenerEquals(listener1, 1, 1, 2, 0, 2, 0);
    }

    @Test
    public void testFeedExecutorListener_ThrownException() {
        Event[] initialEvents = new Event[]{
            new AddFeedExecutorListenerEvent(listener1),
            new TestInvokerEvent(),
            new RemoveFeedExecutorListenerEvent(listener1),
            new TestInvokerEvent()
        };
        interpreter.addLink(new ExceptionThrowingLink(new Supplier<RuntimeException>() {
            @Override
            public RuntimeException get() {
                return new RuntimeException();
            }
        }, new OnlyOnceUpOnCondition(TestInvokerEvent.class)));
        interpreter.execute(new MemoryFeed(initialEvents));
        assertListenerEquals(listener1, 0, 0, 2, 1, 2, 0);
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
        interpreter.addLink(new PerformActionLink(t -> interpreter.execute(new MemoryFeed(nestedEvents)),
                                                  new OnlyOnceUpOnCondition(TestInvokerEvent.class)));
        interpreter.execute(new MemoryFeed(initialEvents));
        assertListenerEquals(listener1, 1, 1, 2, 0, 2, 0);
        assertListenerEquals(listener2, 0, 0, 1, 0, 1, 0);
    }

    @Test
    public void testPublisherListener_InitialTest() {
        Event[] initialEvents = new Event[]{
            new AddPublisherListenerEvent(listener1),
            new TestInvokerEvent()
        };
        Event nestedEvents = new TestInvokerEvent();
        interpreter.addLink(new PerformActionLink(t -> interpreter.execute(new MemoryFeed(nestedEvents)),
                                                  new OnlyOnceUpOnCondition(TestInvokerEvent.class)));
        interpreter.execute(new MemoryFeed(initialEvents));
        assertListenerEquals(listener1, 0, 0, 2, 0, 2, 0);
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
        interpreter.addLink(new PerformActionLink(t -> interpreter.execute(new MemoryFeed(nestedEvents)),
                                                  new OnlyOnceUpOnCondition(TestInvokerEvent.class)));
        interpreter.execute(new MemoryFeed(initialEvents));
        assertListenerEquals(listener1, 0, 0, 2, 0, 2, 0);
        assertListenerEquals(listener2, 0, 0, 1, 0, 1, 0);
    }

    @Test
    public void testChainModifierListener_InitialTest() {
        Event[] initialEvents = new Event[]{
            new AddChainModifierListenerEvent(listener1),
            new TestInvokerEvent()
        };
        Event nestedEvents = new TestInvokerEvent();
        interpreter.addLink(new PerformActionLink(t -> interpreter.addLink(new DummyLink()),
                                                  t -> TestInvokerEvent.class.isAssignableFrom(t.getClass())));
        interpreter.addLink(new PerformActionLink(t -> interpreter.execute(new MemoryFeed(nestedEvents)),
                                                  new OnlyOnceUpOnCondition(TestInvokerEvent.class)));
        interpreter.execute(new MemoryFeed(initialEvents));
        assertListenerEquals(listener1, 0, 0, 0, 0, 0, 2);
    }

    @Test
    public void testChainModifierListener_AddLink_NestedFeed() {
        Event[] initialEvents = new Event[]{
            new AddChainModifierListenerEvent(listener1),
            new TestInvokerEvent(),
            new RemoveChainModifierListenerEvent(listener1),
            new TestInvokerEvent()
        };

        interpreter.addLink(new PerformActionLink(t -> interpreter.addLink(new DummyLink()),
                                                  t -> TestInvokerEvent.class.isAssignableFrom(t.getClass())));
        interpreter.execute(new MemoryFeed(initialEvents));
        assertListenerEquals(listener1, 0, 0, 0, 0, 0, 1);
    }

    @Test
    public void testChainModifierListener_AddLinkAfter_NestedFeed() {
        Event[] initialEvents = new Event[]{
            new AddChainModifierListenerEvent(listener1),
            new TestInvokerEvent(),
            new RemoveChainModifierListenerEvent(listener1),
            new TestInvokerEvent()
        };

        interpreter.addLink(new PerformActionLink(t -> interpreter.addLink(new DummyLink(), eventBus),
                                                  t -> TestInvokerEvent.class.isAssignableFrom(t.getClass())));
        interpreter.execute(new MemoryFeed(initialEvents));
        assertListenerEquals(listener1, 0, 0, 0, 0, 0, 1);
    }

    @Test
    public void testChainModifierListener_RemoveLink_NestedFeed() {
        Event[] initialEvents = new Event[]{
            new AddChainModifierListenerEvent(listener1),
            new TestInvokerEvent(),
            new RemoveChainModifierListenerEvent(listener1),
            new TestInvokerEvent()
        };

        DummyLink dummyLink = new DummyLink();
        interpreter.addLink(dummyLink);
        interpreter.addLink(new PerformActionLink(t -> interpreter.removeLink(dummyLink),
                                                  new OnlyOnceUpOnCondition(TestInvokerEvent.class)));
        interpreter.execute(new MemoryFeed(initialEvents));
        assertListenerEquals(listener1, 0, 0, 0, 0, 0, -1);
    }

    private static void assertListenerEquals(InterpreterListenerSpy listenerSpy, int openingFeed, int closingFeed,
                                             int startingEvents, int exceptions, int closingEvents, int countLinks) {
        assertEquals(openingFeed, listenerSpy.countOpeningFeed());
        assertEquals(closingFeed, listenerSpy.countClosingFeed());
        assertEquals(startingEvents, listenerSpy.countStartingEvents());
        assertEquals(exceptions, listenerSpy.countExceptions());
        assertEquals(closingEvents, listenerSpy.countEndingEvents());
        assertEquals(countLinks, listenerSpy.countLinks());
    }
}
