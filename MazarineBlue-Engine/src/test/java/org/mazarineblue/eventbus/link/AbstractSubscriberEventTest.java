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
package org.mazarineblue.eventbus.link;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.mazarineblue.eventbus.Event;
import org.mazarineblue.eventbus.Filter;
import org.mazarineblue.eventbus.Subscriber;
import org.mazarineblue.eventbus.filters.BlockingFilter;
import org.mazarineblue.eventbus.subscribers.DummySubscriber;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class AbstractSubscriberEventTest {

    @Test
    public void message_MinimalSubscriberEvent() {
        Subscriber<Event> subscriber = new DummySubscriber<>();
        AbstractSubscriberEvent e = new DummySubscriberEvent(subscriber);
        assertEquals("type=Event, filter={null}, subscriber={DummySubscriber}", e.message());
    }

    @Test
    public void message_MaximumSubscriberEvent() {
        Subscriber<Event> subscriber = new DummySubscriber<>();
        AbstractSubscriberEvent e = new DummySubscriberEvent(subscriber);
        e.setType(DummySubscriber.class);
        e.setFilter(new DummyFilter<>());
        String expected = "type=DummySubscriber, filter={DummyFilter}, subscriber={DummySubscriber}";
        String actual = e.message();
        assertEquals(expected, e.message());
    }

    @Test
    public void subscriber_ReturnsInput_Matches() {
        Subscriber<Event> subscriber = new DummySubscriber<>();
        AbstractSubscriberEvent e = new DummySubscriberEvent(subscriber);
        assertEquals(subscriber, e.getSubscriber());
    }

    @Test
    public void type_ReturnsInput_Matches() {
        AbstractSubscriberEvent e = new DummySubscriberEvent(new DummySubscriber<>());
        e.setType(Integer.class);
        assertEquals(Integer.class, e.getType());
    }

    @Test
    public void filter_ReturnsInput_Matches() {
        Filter<Event> filter = new DummyFilter<>();
        AbstractSubscriberEvent e = new DummySubscriberEvent(new DummySubscriber<>());
        e.setFilter(filter);
        assertEquals(filter, e.getFilter());
    }

    @Test
    public void equals_Null() {
        AbstractSubscriberEvent a = new DummySubscriberEvent(new DummySubscriber<>());
        assertNotNull(a);
    }

    @Test
    public void equals_DifferentTypes() {
        AbstractSubscriberEvent a = new DummySubscriberEvent(new DummySubscriber<>());
        AbstractSubscriberEvent b = new DummySubscriberEvent(new DummySubscriber<>());
        b.setType(AbstractSubscriberEvent.class);
        assertNotEquals(a, b);
    }

    @Test
    public void hashCode_DifferentTypes() {
        AbstractSubscriberEvent a = new DummySubscriberEvent(new DummySubscriber<>());
        AbstractSubscriberEvent b = new DummySubscriberEvent(new DummySubscriber<>());
        b.setType(AbstractSubscriberEvent.class);
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void equals_DifferentFilters() {
        AbstractSubscriberEvent a = new DummySubscriberEvent(new DummySubscriber<>());
        AbstractSubscriberEvent b = new DummySubscriberEvent(new DummySubscriber<>());
        b.setFilter(new BlockingFilter<>());
        assertNotEquals(a, b);
    }

    @Test
    public void hashCode_DifferentFilters() {
        AbstractSubscriberEvent a = new DummySubscriberEvent(new DummySubscriber<>());
        AbstractSubscriberEvent b = new DummySubscriberEvent(new DummySubscriber<>());
        b.setFilter(new BlockingFilter<>());
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void equals_DifferentSubscribers() {
        AbstractSubscriberEvent a = new DummySubscriberEvent(new DummySubscriber<>());
        AbstractSubscriberEvent b = new DummySubscriberEvent(new DummyEventSubscriberSpy());
        assertNotEquals(a, b);
    }

    @Test
    public void hashCode_DifferentSubscribers() {
        AbstractSubscriberEvent a = new DummySubscriberEvent(new DummySubscriber<>());
        AbstractSubscriberEvent b = new DummySubscriberEvent(new DummyEventSubscriberSpy());
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void equals_TwoIdenticalEvents() {
        AbstractSubscriberEvent a = new DummySubscriberEvent(new DummySubscriber<>());
        AbstractSubscriberEvent b = new DummySubscriberEvent(new DummySubscriber<>());
        assertEquals(a, b);
    }

    @Test
    public void hashCode_TwoIdenticalEvents() {
        AbstractSubscriberEvent a = new DummySubscriberEvent(new DummySubscriber<>());
        AbstractSubscriberEvent b = new DummySubscriberEvent(new DummySubscriber<>());
        assertEquals(a.hashCode(), b.hashCode());
    }
}
