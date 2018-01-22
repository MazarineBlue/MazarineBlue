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
package org.mazarineblue.eventnotifier;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mazarineblue.eventnotifier.events.TestEvent;
import org.mazarineblue.eventnotifier.exceptions.EventHandlerMissingException;
import org.mazarineblue.eventnotifier.exceptions.MissingSubscriberExcpetion;
import org.mazarineblue.eventnotifier.exceptions.SubscriberAlreadyRegisteredException;
import org.mazarineblue.eventnotifier.exceptions.SubscriberNotRegisteredException;
import org.mazarineblue.eventnotifier.subscribers.ConsumeEventsSubscriber;
import org.mazarineblue.eventnotifier.subscribers.ReflectionSubscriberSpy;
import org.mazarineblue.eventnotifier.subscribers.SubscriberSpy;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
@RunWith(HierarchicalContextRunner.class)
public class SimpleEventServiceTest {

    private EventService<Event> service;

    @Before
    public void setup() {
        service = new SimpleEventService<>();
    }

    @After
    public void teardown() {
        service = null;
    }

    @SuppressWarnings("PublicInnerClass")
    public class GivenEmptySimpleEventService {

        @Test(expected = MissingSubscriberExcpetion.class)
        public void subscribe_Null() {
            service.subscribe(null);
        }

        @Test
        public void subscribe_SubscriberSpy() {
            service.subscribe(new SubscriberSpy<>());
        }

        @Test(expected = EventHandlerMissingException.class)
        public void subscribe_EmptyReflectionSubscriber() {
            service.subscribe(new ReflectionSubscriber<Event>() {
                // For testing purposes, there is no need for an implemantion.
            });
        }

        @Test
        public void subscribe_ReflectionSubscriber_WithCorrectHandlerType() {
            service.subscribe(new ReflectionSubscriber<Event>() {
                @EventHandler
                public void handler(TestEvent e) {
                    // For testing purposes, there is no need for an implemantion.
                }
            });
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class GivenSimpleEventService_WithSubscriberSpy {

        private SubscriberSpy<Event> spy;

        @Before
        public void setup() {
            spy = new SubscriberSpy<>();
            service.subscribe(spy);
        }

        @After
        public void teardown() {
            spy = null;
        }

        @Test
        public void publish_TestEvent() {
            TestEvent e = new TestEvent();
            service.publish(e);
            assertEquals(1, spy.receivedEvents());
            assertFalse(e.isConsumed());
        }

        @Test(expected = SubscriberAlreadyRegisteredException.class)
        public void subscribe_SubscribeAgain() {
            service.subscribe(spy);
        }

        @Test
        public void unsubscribe_RegisteredSubscriber() {
            service.unsubscribe(spy);
        }

        @Test(expected = SubscriberNotRegisteredException.class)
        public void unsubscribe_UnegisteredSubscriber() {
            service.unsubscribe(new ReflectionSubscriberSpy<>());
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class GivenConsumeEventsSubscriber {

        @Before
        public void setup() {
            service.subscribe(new ConsumeEventsSubscriber<>());
        }

        @Test
        public void publish_TestEvent() {
            TestEvent e = new TestEvent();
            service.publish(e);
            assertTrue(e.isConsumed());
        }
    }
}
