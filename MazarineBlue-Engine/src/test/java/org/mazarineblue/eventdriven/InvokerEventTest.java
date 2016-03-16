/*
 * Copyright (c) 2016 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.eventbus.Event.Status;
import org.mazarineblue.eventdriven.events.ExceptionThrownEvent;
import org.mazarineblue.eventdriven.exceptions.EventNotConsumedException;
import org.mazarineblue.eventdriven.feeds.MemoryFeed;
import org.mazarineblue.eventdriven.util.TestExceptionThrownEventLinkSpy;
import org.mazarineblue.eventdriven.util.TestInvokerEvent;
import org.mazarineblue.links.ConsumeEventsLink;
import org.mazarineblue.links.UnconsumedExceptionThrowerLink;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class InvokerEventTest {

    private InvokerEvent event;

    @Before
    public void setup() {
        event = new TestInvokerEvent();
    }

    @After
    public void teardown() {
        event = null;
    }

    @Test
    public void event_Initial() {
        assertEquals(Status.OK, event.status());
        assertFalse(event.hasException());
        assertEquals(null, event.getException());
    }

    @Test
    public void event_Fired() {
        TestExceptionThrownEventLinkSpy spy = new TestExceptionThrownEventLinkSpy();

        Interpreter interpreter = Interpreter.getDefaultInstance();
        interpreter.addLink(new ConsumeEventsLink());
        interpreter.addLink(new UnconsumedExceptionThrowerLink(ExceptionThrownEvent.class));
        interpreter.execute(new MemoryFeed(event));
        interpreter.addLink(spy);
        interpreter.execute(new MemoryFeed(event));

        assertEquals(Status.ERROR, event.status());
        assertTrue(event.hasException());
        assertEquals(EventNotConsumedException.class, event.getException().getClass());
        assertEquals(EventNotConsumedException.class, spy.getException().getClass());
        assertEquals(event, spy.getOwner());
        assertEquals("error=Unconsumed event org.mazarineblue.eventdriven.util.TestInvokerEvent with value: consumed=false", spy.message());
    }
}
