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
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.mazarineblue.eventnotifier;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mazarineblue.eventnotifier.events.AbstractEvent;
import org.mazarineblue.eventnotifier.events.EventSpy;
import org.mazarineblue.eventnotifier.events.TestEvent;
import org.mazarineblue.eventnotifier.util.PrivateOwnerMediator;
import org.mazarineblue.eventnotifier.util.TestOwner;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
@RunWith(HierarchicalContextRunner.class)
public class EventHandlerCallerTest {

    private TestCaller caller;
    private Object owner;

    @After
    public void teardown() {
        caller = null;
        owner = null;
    }

    @SuppressWarnings("PublicInnerClass")
    public class CallerStopsOnConsumption {

        @Before
        public void setup() {
            owner = new TestOwner();
            caller = new TestCaller(owner);
        }

        @Test
        public void publish_catchable() {
            caller.publish(new TestEvent(), Event::isConsumed);
            assertEquals(1, ((TestOwner) owner).getCallCount());
        }

        @Test
        public void publish_uncatchable() {
            caller.publish(new EventSpy(), Event::isConsumed);
            assertEquals(1, caller.uncatched());
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class CallerContinuesOnConsumptions {

        @Before
        public void setup() {
            owner = new TestOwner();
            caller = new TestCaller(owner);
        }

        @Test
        public void publish() {
            caller.publish(new TestEvent(), e -> false);
            assertEquals(2, ((TestOwner) owner).getCallCount());
        }

        @Test
        public void publish_uncatchable() {
            caller.publish(new EventSpy(), (e) -> false);
            assertEquals(1, caller.uncatched());
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class OwnerIsPrivate {

        @Before
        public void setup() {
            owner = PrivateOwnerMediator.createInstance();
            caller = new TestCaller(owner);
        }

        @Test(expected = TestPublicClassDeclarationException.class)
        public void publish() {
            caller.publish(new TestEvent(), (e) -> false);
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class OwnerThrowsException {

        @Before
        public void setup() {
            owner = new Object() {
                @EventHandler
                public void eventHandler(AbstractEvent event) {
                    throw new TestTargetException();
                }
            };
            caller = new TestCaller(owner);
        }

        @Test(expected = TestTargetException.class)
        public void publish() {
            caller.publish(new TestEvent(), (e) -> false);
        }
    }

    private class TestCaller
            extends EventHandlerCaller<Event> {

        private int uncatched;

        @SuppressWarnings("PublicConstructorInNonPublicClass")
        public TestCaller(Object owner) {
            super(owner);
        }

        @Override
        public RuntimeException createPublicClassDeclarationRequiredException() {
            throw new TestPublicClassDeclarationException();
        }

        @Override
        public RuntimeException createTargetException(Throwable cause) {
            throw new TestTargetException();
        }

        @Override
        protected void unpublished(Event event) {
            ++uncatched;
        }

        private int uncatched() {
            return uncatched;
        }
    }

    private class TestPublicClassDeclarationException
            extends RuntimeException {

        private static final long serialVersionUID = 1L;
    }

    private class TestTargetException
            extends RuntimeException {

        private static final long serialVersionUID = 1L;
    }
}
