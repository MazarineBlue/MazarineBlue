/*
 * Copyright (c) 2012 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mazarineblue.eventbus;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mazarineblue.eventbus.events.AbstractEvent;
import org.mazarineblue.eventbus.events.ConsumableEvent;
import org.mazarineblue.eventbus.events.EventSpy;
import org.mazarineblue.eventbus.events.TestEvent;
import org.mazarineblue.eventbus.exceptions.EventHandlerMissingException;
import org.mazarineblue.eventbus.exceptions.EventHandlerRequiresOneParameterException;
import org.mazarineblue.eventbus.exceptions.IllegalEventHandlerException;
import org.mazarineblue.eventbus.exceptions.IllegalEventTypeException;
import org.mazarineblue.eventbus.exceptions.MissingSubscriberExcpetion;
import org.mazarineblue.eventbus.filters.BlockingFilter;
import org.mazarineblue.eventbus.subscribers.DummySubscriber;
import org.mazarineblue.eventbus.subscribers.ReflectionSubscriberSpy;
import org.mazarineblue.eventbus.subscribers.SubscriberSpy;
import org.mazarineblue.utililities.util.TestHashCodeAndEquals;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
@RunWith(HierarchicalContextRunner.class)
public class SimpleEventServiceTest
        extends TestHashCodeAndEquals<EventService<Event>> {

    private static Event event;

    private EventService<Event> service;

    @BeforeClass
    public static void setupClass() {
        event = new TestEvent();
    }

    @AfterClass
    public static void teardownClass() {
        event = null;
    }

    @Before
    public void setup() {
        service = new SimpleEventService<>();
    }

    @After
    public void teardown() {
        service = null;
    }

    @Test(expected = IllegalEventTypeException.class)
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void constructor_WrongType_ThrowsIllegalEventTypeException() {
        new SimpleEventService<>(Object.class);
    }

    @Test
    public void subscribe_TestSubscriber_Succeeds() {
        service.subscribe(null, null, new SubscriberSpy<>());
    }

    @Test(expected = IllegalEventTypeException.class)
    public void subscribe_ObjectType_ThrowsIllegalEventTypeException() {
        service.subscribe(Object.class, null, null);
    }

    @Test(expected = MissingSubscriberExcpetion.class)
    public void subscribe_WithoutSubscriber_ThrowsMissingSubscriberExcpetion() {
        service.subscribe(null, null, null);
    }

    @Test(expected = EventHandlerMissingException.class)
    public void subscribe_EmptyReflectionSubscriber_ThrowsEventHandlerMissingException() {
        ReflectionSubscriber<Event> emptySubscriber = new ReflectionSubscriber<Event>() {
        };
        service.subscribe(null, null, emptySubscriber);
    }

    @Test(expected = EventHandlerRequiresOneParameterException.class)
    public void subscribe_ToFewParametersInEventHandlerReflectionSubscriber_ThrowsEventHandlerMissingException() {
        new ReflectionSubscriber<Event>() {
            @EventHandler
            public void handler() {
            }
        };
    }

    @Test(expected = EventHandlerRequiresOneParameterException.class)
    public void subscribe_ToMannyParametersInReflectionSubscriber_ThrowsEventHandlerMissingException() {
        new ReflectionSubscriber<Event>() {
            @EventHandler
            public void handler(Event a, Event b) {
            }
        };
    }

    @Test(expected = IllegalEventHandlerException.class)
    public void subscribe_InvalidReflectionSubscriber_ThrowsEventHandlerMissingException() {
        ReflectionSubscriber<Event> wrongType = new ReflectionSubscriber<Event>() {
            @EventHandler
            public void handler(Object o) {
            }
        };
        service.subscribe(null, null, wrongType);
    }

    @SuppressWarnings("PublicInnerClass")
    public class GivenSubscriberIsUsed {

        private SubscriberSpy<Event> subscriber;

        @Before
        public void setup() {
            subscriber = new SubscriberSpy<>();
        }

        @Test
        public void subscribe_TestSubscriberDouble_AddsOnlyOne() {
            assertTrue(service.subscribe(null, null, subscriber));
            assertFalse(service.subscribe(null, null, subscriber));
        }

        @Test
        public void publish_TestEvent_received() {
            service.subscribe(null, null, subscriber);
            service.publish(event);

            assertEquals(1, subscriber.receivedEvents());
        }

        @Test
        public void publish_null_Doesnothing() {
            service.subscribe(null, null, subscriber);
            service.publish(null);

            assertEquals(0, subscriber.receivedEvents());
        }

        @Test
        public void publish_TestEvent_SubscriberConsumes() {
            ReflectionSubscriberSpy s = new ReflectionSubscriberSpy();

            service.subscribe(null, null, s);
            service.publish(new ConsumableEvent());

            assertEquals(1, s.receivedEvents());
        }

        @Test
        public void publish_UnassinableEvent_receivedNot() {
            service.subscribe(TestEvent.class, null, subscriber);
            service.publish(new EventSpy());

            assertEquals(0, subscriber.receivedEvents());
        }

        @Test
        public void publish_TestEvent_EventFilteredOut() {
            Filter<Event> filter = new BlockingFilter<>();

            service.subscribe(null, filter, subscriber);
            service.publish(event);

            assertEquals(0, subscriber.receivedEvents());
        }

        @Test
        public void unsubscribe_Subscriber_Unsubsribed() {
            service.subscribe(null, null, subscriber);
            service.unsubscribe(null, null, subscriber);
            service.publish(event);

            assertEquals(0, subscriber.receivedEvents());
        }

        @Test
        public void unsubscribe_Subscriber_UnsubsribedNot() {
            Subscriber<Event> dummy = new DummySubscriber<>();

            service.subscribe(null, null, subscriber);
            boolean result = service.unsubscribe(null, null, dummy);
            service.publish(event);

            assertEquals(1, subscriber.receivedEvents());
            assertFalse(result);
        }
    }

    @Test
    public void hashCode_DifferentBaseEvent() {
        int a = service.hashCode();
        int b = new SimpleEventService<>(AbstractEvent.class).hashCode();
        assertNotEquals(a, b);
    }

    @Test
    public void equals_DifferentBaseEvent() {
        SimpleEventService<Event> other = new SimpleEventService<>(AbstractEvent.class);
        assertFalse(service.equals(other));
    }

    @Override
    protected EventService<Event> getIdenticalObject() {
        return new SimpleEventService<>();
    }

    @Override
    protected EventService<Event> getObject() {
        return service;
    }

    @Override
    protected EventService<Event> getDifferentObject() {
        SimpleEventService<Event> other = new SimpleEventService<>();
        other.subscribe(null, null, new DummySubscriber<>());
        return other;
    }
}
