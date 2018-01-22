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
package org.mazarineblue.eventnotifier;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import static org.mazarineblue.eventnotifier.Event.matchesAny;
import org.mazarineblue.eventnotifier.events.AbstractEvent;
import org.mazarineblue.eventnotifier.events.EventSpy;
import org.mazarineblue.eventnotifier.events.TestEvent;
import org.mazarineblue.eventnotifier.exceptions.UnconsumedEventException;
import org.mazarineblue.eventnotifier.subscribers.ConsumeEventsSubscriber;
import org.mazarineblue.eventnotifier.subscribers.UnconsumedExceptionThrowerSubscriber;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class SubscribersTest {

    @Test
    public void eventHandler_ConsumeEventsSubscriber_OutOfScopeEvent() {
        Subscriber<Event> subscriber = new ConsumeEventsSubscriber<>(matchesAny(AbstractEvent.class));
        EventSpy e = new EventSpy();
        subscriber.eventHandler(e);
        assertEquals(0, e.getCount());
    }

    @Test
    public void eventHandler_ConsumeEventsSubscriber_InScopeEvent() {
        Subscriber<Event> subscriber = new ConsumeEventsSubscriber<>(matchesAny(EventSpy.class));
        EventSpy e = new EventSpy();
        subscriber.eventHandler(e);
        assertEquals(1, e.getCount());
    }

    @Test(expected = UnconsumedEventException.class)
    public void eventHandler_UnconsumedExceptionThrowerSubscriber_EventNotConsumed() {
        Subscriber<Event> subscriber = new UnconsumedExceptionThrowerSubscriber<>(matchesAny(AbstractEvent.class));
        TestEvent e = new TestEvent();
        subscriber.eventHandler(e);
    }

    @Test
    public void eventHandler_UnconsumedExceptionThrowerSubscriber_EventConsumed() {
        Subscriber<Event> subscriber = new UnconsumedExceptionThrowerSubscriber<>(matchesAny(AbstractEvent.class));
        TestEvent e = new TestEvent();
        e.setConsumed(true);
        subscriber.eventHandler(e);
    }
}
