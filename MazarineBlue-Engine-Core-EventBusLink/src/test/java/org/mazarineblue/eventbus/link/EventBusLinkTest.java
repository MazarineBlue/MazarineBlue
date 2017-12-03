/*
 * Copyright (c) 2016 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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
package org.mazarineblue.eventbus.link;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mazarineblue.eventbus.events.TestEvent;
import org.mazarineblue.eventbus.subscribers.DummySubscriber;
import org.mazarineblue.utililities.util.TestHashCodeAndEquals;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
@RunWith(HierarchicalContextRunner.class)
public class EventBusLinkTest {

    private EventBusLink link;

    @After
    public void teardown() {
        link = null;
    }

    @SuppressWarnings("PublicInnerClass")
    public class Uninitialized {

        @Test
        public void unsubscribe() {
            link = new EventBusLink(null, null, new DummySubscriber<>());
            boolean flag = link.unsubscribe(null, null, new DummySubscriber<>());
            assertTrue(flag);
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class Intialized
            extends TestHashCodeAndEquals<EventBusLink> {

        @Before
        public void setup() {
            link = new EventBusLink();
        }

        @Test
        public void subscribe() {
            boolean flag = link.subscribe(null, null, new DummySubscriber<>());
            assertTrue(flag);
        }

        @Test
        public void publish_SubscribeEvent_Recieved() {
            DummyEventSubscriberSpy subscriber = new DummyEventSubscriberSpy();
            link.publish(new SubscribeEvent(subscriber));
            link.publish(new TestEvent());
            assertEquals(1, subscriber.count());
        }

        @Test
        public void publish_UnsubscribeEvent_NotRecieved() {
            DummyEventSubscriberSpy subscriber = new DummyEventSubscriberSpy();
            link.publish(new SubscribeEvent(subscriber));
            link.publish(new UnsubscribeEvent(subscriber));
            link.publish(new TestEvent());
            assertEquals(0, subscriber.count());
        }

        @Override
        protected EventBusLink getIdenticalObject() {
            return new EventBusLink();
        }

        @Override
        protected EventBusLink getObject() {
            return link;
        }

        @Override
        protected EventBusLink getDifferentObject() {
            return new EventBusLink(null, null, new DummySubscriber<>());
        }

    }
}
