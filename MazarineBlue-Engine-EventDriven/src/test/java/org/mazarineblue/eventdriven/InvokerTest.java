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
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.eventdriven.feeds.MemoryFeed;
import org.mazarineblue.eventdriven.util.events.TestInvokerEvent;
import org.mazarineblue.eventdriven.util.listeners.ProcessorListenerSpy;
import org.mazarineblue.eventnotifier.events.TestEvent;
import org.mazarineblue.eventnotifier.subscribers.ConsumeEventsSubscriber;

public class InvokerTest {

    private Processor processor;
    private ProcessorListenerSpy spy;
    private TestInvokerEvent event;

    @Before
    public void setup() {
        spy = new ProcessorListenerSpy(t -> true);
        processor = Processor.newInstance();
        processor.setChainModifierListener(spy);
        processor.setFeedExecutorListener(spy);
        processor.setPublisherListener(spy);
        event = new TestInvokerEvent();
        processor.execute(new MemoryFeed(event));
    }

    @After
    public void teardown() {
        processor = null;
    }

    @Test
    public void publish_TestEvent() {
        TestEvent e = new TestEvent();
        event.invoker().publish(e);
        assertFalse(e.isConsumed());
        spy.openedFeeds().assertClasses(MemoryFeed.class);
        spy.closedFeeds().assertClasses(MemoryFeed.class);
        spy.startEvents().assertClasses(TestInvokerEvent.class, TestEvent.class);
        spy.exceptions().assertClasses();
        spy.endEvents().assertClasses(TestInvokerEvent.class, TestEvent.class);
    }

    @Test
    public void publish_TestInvokerEvent() {
        TestInvokerEvent e = new TestInvokerEvent();
        event.invoker().publish(e);
        assertFalse(e.isConsumed());
        spy.openedFeeds().assertClasses(MemoryFeed.class);
        spy.closedFeeds().assertClasses(MemoryFeed.class);
        spy.startEvents().assertClasses(TestInvokerEvent.class, TestInvokerEvent.class);
        spy.exceptions().assertClasses();
        spy.endEvents().assertClasses(TestInvokerEvent.class, TestInvokerEvent.class);
        assertSame(processor, e.invoker().processor());
    }

    @Test
    public void chain_AddLink() {
        TestInvokerEvent e = new TestInvokerEvent();
        event.invoker().chain().addLink(new ConsumeEventsSubscriber<>());
        event.invoker().publish(e);
        assertTrue(e.isConsumed());
    }
}
