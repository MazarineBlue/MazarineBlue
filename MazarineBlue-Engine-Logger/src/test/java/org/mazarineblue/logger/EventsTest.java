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
package org.mazarineblue.logger;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mazarineblue.eventbus.Event;
import static org.mazarineblue.eventbus.Event.matchesNoneAutoConsumable;
import org.mazarineblue.eventdriven.util.ChainModifierListenerSpy;
import org.mazarineblue.eventdriven.util.FeedExecutorListenerSpy;
import org.mazarineblue.eventdriven.util.InterpreterListenerSpy;
import org.mazarineblue.eventdriven.util.PublisherListenerSpy;
import org.mazarineblue.logger.events.AddChainModifierListenerEvent;
import org.mazarineblue.logger.events.AddFeedExecutorListenerEvent;
import org.mazarineblue.logger.events.AddInterpreterListenerEvent;
import org.mazarineblue.logger.events.AddPublisherListenerEvent;
import org.mazarineblue.logger.events.RemoveChainModifierListenerEvent;
import org.mazarineblue.logger.events.RemoveFeedExecutorListenerEvent;
import org.mazarineblue.logger.events.RemoveInterpreterListenerEvent;
import org.mazarineblue.logger.events.RemovePublisherListenerEvent;

@RunWith(HierarchicalContextRunner.class)
public class EventsTest {

    @SuppressWarnings("PublicInnerClass")
    public static abstract class AbstractListenerEventTests1 {

        @Test
        public void hashCode_AddAndRemove() {
            int a = getAddEvent().hashCode();
            int b = getRemoveEvent().hashCode();
            assertEquals(a, b);
        }

        @Test
        public void hashCode_AddAndAdd() {
            int a = getAddEvent().hashCode();
            int b = getAddEvent().hashCode();
            assertEquals(a, b);
        }

        @Test
        public void equals_AddAndRemove() {
            Event a = getAddEvent();
            Event b = getRemoveEvent();
            assertNotEquals(a, b);
        }

        @Test
        public void equals_AddAndAdd() {
            Event a = getAddEvent();
            Event b = getAddEvent();
            assertEquals(a, b);
        }
        
        @Test
        public void clone_AddEvent() {
            Event a = getAddEvent();
            Event b = Event.clone(a);
            assertEquals(a, b);
            assertNotSame(a, b);
        }

        @Test
        public void clone_RemoveEvent() {
            Event a = getRemoveEvent();
            Event b = Event.clone(a);
            assertEquals(a, b);
            assertNotSame(a, b);
        }

        protected abstract Event getAddEvent();
        protected abstract Event getRemoveEvent();
    }

    @SuppressWarnings("PublicInnerClass")
    public class AbstractChainModifierListenerEventTest
        extends AbstractListenerEventTests1 {

        @Override
        protected Event getAddEvent() {
            return new AddChainModifierListenerEvent(null);
        }

        @Override
        protected Event getRemoveEvent() {
            return new RemoveChainModifierListenerEvent(null);
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class AbstractFeedExecutorListenerEventTest
        extends AbstractListenerEventTests1 {

        @Override
        protected Event getAddEvent() {
            return new AddFeedExecutorListenerEvent(null);
        }

        @Override
        protected Event getRemoveEvent() {
            return new RemoveFeedExecutorListenerEvent(null);
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class AbstractInterpreterListenerEventTest
        extends AbstractListenerEventTests1 {

        @Override
        protected Event getAddEvent() {
            return new AddInterpreterListenerEvent(null);
        }

        @Override
        protected Event getRemoveEvent() {
            return new RemoveInterpreterListenerEvent(null);
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class AbstractPublisherListenerEventTest
        extends AbstractListenerEventTests1 {

        @Override
        protected Event getAddEvent() {
            return new AddPublisherListenerEvent(null);
        }

        @Override
        protected Event getRemoveEvent() {
            return new RemovePublisherListenerEvent(null);
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public static abstract class AbstractListenerEventTests2 {

        @Test
        public void hashCode_IdenticalEvent() {
            int a = getEvent().hashCode();
            int b = getEvent().hashCode();
            assertEquals(a, b);
        }

        @Test
        @SuppressWarnings("ObjectEqualsNull")
        public void equals_Null() {
            Event a = getEvent();
            assertFalse(a.equals(null));
        }

        @Test
        @SuppressWarnings("IncompatibleEquals")
        public void equals_DifferentClass() {
            Event a = getEvent();
            assertFalse(a.equals(""));
        }

        @Test
        public void equals_IdenticalEvent() {
            Event a = getEvent();
            Event b = getEvent();
            assertTrue(a.equals(b));
        }

        @Test
        public void equals_SameEvent() {
            Event a = getEvent();
            assertTrue(a.equals(a));
        }

        protected abstract Event getEvent();
    }

    @SuppressWarnings("PublicInnerClass")
    public class AddChainModifierListenerEventTest
            extends AbstractListenerEventTests2 {

        @Override
        protected Event getEvent() {
            return new AddChainModifierListenerEvent(new ChainModifierListenerSpy());
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class AddFeedExecutorListenerEventTest
            extends AbstractListenerEventTests2 {

        @Override
        protected Event getEvent() {
            return new AddFeedExecutorListenerEvent(new FeedExecutorListenerSpy(matchesNoneAutoConsumable()));
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class AddInterpreterListenerEventTest
            extends AbstractListenerEventTests2 {

        @Override
        protected Event getEvent() {
            return new AddInterpreterListenerEvent(new InterpreterListenerSpy());
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class AddPublisherListenerEventTest
            extends AbstractListenerEventTests2 {

        @Override
        protected Event getEvent() {
            return new AddPublisherListenerEvent(new PublisherListenerSpy(matchesNoneAutoConsumable()));
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class RemoveChainModifierListenerEventTest
            extends AbstractListenerEventTests2 {

        @Override
        protected Event getEvent() {
            return new RemoveChainModifierListenerEvent(new ChainModifierListenerSpy());
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class RemoveFeedExecutorListenerEventTest
            extends AbstractListenerEventTests2 {

        @Override
        protected Event getEvent() {
            return new RemoveFeedExecutorListenerEvent(new FeedExecutorListenerSpy(matchesNoneAutoConsumable()));
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class RemoveInterpreterListenerEventTest
            extends AbstractListenerEventTests2 {

        @Override
        protected Event getEvent() {
            return new RemoveInterpreterListenerEvent(new InterpreterListenerSpy());
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class RemovePublisherListenerEventTest
            extends AbstractListenerEventTests2 {

        @Override
        protected Event getEvent() {
            return new RemovePublisherListenerEvent(new PublisherListenerSpy(matchesNoneAutoConsumable()));
        }
    }
}