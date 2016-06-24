/*
 * Copyright (c) 2012 Alex de Kruijff
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
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mazarineblue.eventbus.events.ConsumableEventStub;
import org.mazarineblue.eventbus.events.EventDummy;
import org.mazarineblue.eventbus.events.UnassignableEventDummy;
import org.mazarineblue.eventbus.exceptions.EventHandlerMissingException;
import org.mazarineblue.eventbus.exceptions.IllegalEventHandlerException;
import org.mazarineblue.eventbus.exceptions.IllegalEventTypeException;
import org.mazarineblue.eventbus.exceptions.MissingSubscriberExcpetion;
import org.mazarineblue.eventbus.filters.BlockingFilterStub;
import org.mazarineblue.eventbus.subscribers.ReflectionSubscriberSpy;
import org.mazarineblue.eventbus.subscribers.SubscriberDummy;
import org.mazarineblue.eventbus.subscribers.SubscriberSpy;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
@RunWith(HierarchicalContextRunner.class)
public class SimpleEventServiceTest {

    static private Event event;

    private EventService service;

    @BeforeClass
    static public void setupClass() {
        event = new EventDummy();
    }

    @AfterClass
    static public void teardownClass() {
        event = null;
    }

    @Before
    public void setup() {
        service = new SimpleEventService();
    }

    @Test(expected = IllegalEventTypeException.class)
    public void constructor_WrongType_ThrowsIllegalEventTypeException() {
        new SimpleEventService(Object.class);
    }

    @Test
    public void subscribe_TestSubscriber_Succeeds() {
        service.subscribe(null, null, new SubscriberSpy());
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
        ReflectionSubscriber emptySubscriber = new ReflectionSubscriber() {
        };
        service.subscribe(null, null, emptySubscriber);
    }

    @Test(expected = IllegalEventHandlerException.class)
    public void subscribe_ToFewParametersInEventHandlerReflectionSubscriber_ThrowsEventHandlerMissingException() {
        ReflectionSubscriber toFewParameters = new ReflectionSubscriber() {
            @EventHandler
            public void handler() {
            }
        };
        service.subscribe(null, null, toFewParameters);
    }

    @Test(expected = IllegalEventHandlerException.class)
    public void subscribe_ToMannyParametersInReflectionSubscriber_ThrowsEventHandlerMissingException() {
        ReflectionSubscriber toMennyParameters = new ReflectionSubscriber() {
            @EventHandler
            public void handler(Event a, Event b) {
            }
        };
        service.subscribe(null, null, toMennyParameters);
    }

    @Test(expected = IllegalEventHandlerException.class)
    public void subscribe_InvalidReflectionSubscriber_ThrowsEventHandlerMissingException() {
        ReflectionSubscriber wrongType = new ReflectionSubscriber() {
            @EventHandler
            public void handler(Object o) {
            }
        };

        service.subscribe(null, null, wrongType);
    }

    public class GivenSubscriberIsUsed {

        private SubscriberSpy subscriber;

        @Before
        public void setup() {
            subscriber = new SubscriberSpy();
        }

        @Test
        public void subscribe_TestSubscriberDouble_AddsOnlyOne() {
            assertEquals(true, service.subscribe(null, null, subscriber));
            assertEquals(false, service.subscribe(null, null, subscriber));
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
            service.publish(new ConsumableEventStub());

            assertEquals(1, s.receivedEvents());
        }

        @Test
        public void publish_UnassinableEvent_receivedNot() {
            service.subscribe(EventDummy.class, null, subscriber);
            service.publish(new UnassignableEventDummy());

            assertEquals(0, subscriber.receivedEvents());
        }

        @Test
        public void publish_TestEvent_EventFilteredOut() {
            Filter filter = new BlockingFilterStub();

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
            Subscriber dummy = new SubscriberDummy();

            service.subscribe(null, null, subscriber);
            boolean result = service.unsubscribe(null, null, dummy);
            service.publish(event);

            assertEquals(1, subscriber.receivedEvents());
            assertEquals(false, result);
        }
    }
}
