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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.eventdriven.events.ExceptionThrownEvent;
import org.mazarineblue.eventdriven.feeds.MemoryFeed;
import org.mazarineblue.eventdriven.util.events.TestInvokerEvent;
import org.mazarineblue.eventdriven.util.listeners.ProcessorListenerSpy;
import static org.mazarineblue.eventnotifier.Event.matchesNone;
import org.mazarineblue.eventnotifier.events.TestEvent;
import org.mazarineblue.eventnotifier.exceptions.UnconsumedEventException;
import org.mazarineblue.eventnotifier.subscribers.ConsumeEventsSubscriber;
import org.mazarineblue.eventnotifier.subscribers.ExceptionThrowingSubscriber;
import org.mazarineblue.eventnotifier.subscribers.UnconsumedExceptionThrowerSubscriber;
import org.mazarineblue.utilities.util.TestException;

public class ProcessorTest {

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

    @Test
    public void execute_AbstractEvent_NoLinks() {
        TestEvent e = new TestEvent();
        try {
            processor.execute(new MemoryFeed(e));
        } finally {
            assertFalse(e.isConsumed());
            spy.links().assertClasses();
            spy.openedFeeds().assertClasses(MemoryFeed.class);
            spy.closedFeeds().assertClasses(MemoryFeed.class);
            spy.startEvents().assertClasses(TestEvent.class);
            spy.exceptions().assertClasses();
            spy.endEvents().assertClasses(TestEvent.class);
        }
    }

    @Test(expected = TestException.class)
    public void execute_AbstractEvent_LinkThrowsException() {
        TestEvent e = new TestEvent();
        try {
            processor.addLink(new ExceptionThrowingSubscriber<>(matchesNone(ExceptionThrownEvent.class),
                                                                    t -> new TestException()));
            processor.execute(new MemoryFeed(e));
        } finally {
            assertFalse(e.isConsumed());
            spy.links().assertClasses(ExceptionThrowingSubscriber.class);
            spy.openedFeeds().assertClasses(MemoryFeed.class);
            spy.closedFeeds().assertClasses(MemoryFeed.class);
            spy.startEvents().assertClasses(TestEvent.class, ExceptionThrownEvent.class);
            spy.exceptions().assertClasses(TestException.class);
            spy.endEvents().assertClasses(TestEvent.class, ExceptionThrownEvent.class);
        }
    }

    @Test(expected = TestException.class)
    public void execute_InvokerEvent_LinkThrowsException() {
        TestInvokerEvent e = new TestInvokerEvent();
        TestException testException = new TestException();
        try {
            processor.addLink(new ExceptionThrowingSubscriber<>(matchesNone(ExceptionThrownEvent.class),
                                                                    t -> testException));
            processor.execute(new MemoryFeed(e));
        } finally {
            assertFalse(e.isConsumed());
            spy.links().assertClasses(ExceptionThrowingSubscriber.class);
            spy.openedFeeds().assertClasses(MemoryFeed.class);
            spy.closedFeeds().assertClasses(MemoryFeed.class);
            spy.startEvents().assertClasses(TestInvokerEvent.class, ExceptionThrownEvent.class);
            spy.exceptions().assertClasses(TestException.class);
            spy.endEvents().assertClasses(TestInvokerEvent.class, ExceptionThrownEvent.class);
            assertEquals(e, getExceptionThrownEvent(1).getCause());
            assertEquals(testException, getExceptionThrownEvent(1).getException());
        }
    }

    private ExceptionThrownEvent getExceptionThrownEvent(int index) {
        return spy.startEvents().get(index);
    }

    @Test
    public void execute_ConsumentedEvent_ExceptionNotThrown() {
        processor.addLink(new UnconsumedExceptionThrowerSubscriber<>(t -> true));
        processor.addLink(new ConsumeEventsSubscriber<>(t -> true));
        processor.execute(new MemoryFeed(new TestEvent()));
    }

    @Test(expected = UnconsumedEventException.class)
    public void execute_UnconsumedEvent_ExceptionThrown() {
        processor.addLink(new UnconsumedExceptionThrowerSubscriber<>(t -> true));
        processor.execute(new MemoryFeed(new TestEvent()));
    }

    @Test
    public void close()
            throws Exception {
        processor.close();
        spy.links().assertClasses();
        spy.openedFeeds().assertClasses();
        spy.closedFeeds().assertClasses();
        spy.startEvents().assertClasses(ClosingProcessorEvent.class);
        spy.exceptions().assertClasses();
        spy.endEvents().assertClasses(ClosingProcessorEvent.class);
    }
}
