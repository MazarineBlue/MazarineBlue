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

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.After;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mazarineblue.eventdriven.events.ExceptionThrownEvent;
import org.mazarineblue.eventdriven.listeners.ChainModifierListenerList;
import org.mazarineblue.eventdriven.listeners.FeedExecutorListenerList;
import org.mazarineblue.eventdriven.listeners.PublisherListenerList;
import org.mazarineblue.eventnotifier.events.TestEvent;
import org.mazarineblue.utilities.util.TestException;
import org.mazarineblue.utilities.util.TestHashCodeAndEquals;

@RunWith(HierarchicalContextRunner.class)
public class HashCodeAndEqualsTest {

    @SuppressWarnings("PublicInnerClass")
    public class ExceptionThrownEventTest
            extends TestHashCodeAndEquals<ExceptionThrownEvent> {

        private RuntimeException ex;
        private TestEvent event;
        private ExceptionThrownEvent thrownEvent;

        @Before
        public void setup() {
            ex = new RuntimeException();
            event = new TestEvent();
            thrownEvent = new ExceptionThrownEvent(event, ex);
        }

        @After
        public void teardown() {
            ex = null;
            event = null;
            thrownEvent = null;
        }

        @Test
        public void hashCode_DifferentEvents() {
            ExceptionThrownEvent b = new ExceptionThrownEvent(new TestEvent(), ex);
            assertNotEquals(thrownEvent.hashCode(), b.hashCode());
        }

        @Test
        public void equals_DifferentEvents() {
            ExceptionThrownEvent b = new ExceptionThrownEvent(new TestEvent(), ex);
            assertFalse(thrownEvent.equals(b));
        }

        @Override
        protected ExceptionThrownEvent getObject() {
            return thrownEvent;
        }

        @Override
        protected ExceptionThrownEvent getDifferentObject() {
            return new ExceptionThrownEvent(event, new TestException());
        }

        @Override
        protected ExceptionThrownEvent getIdenticalObject() {
            return new ExceptionThrownEvent(event, ex);
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class ChainModifierListenerListHCAE
            extends TestHashCodeAndEquals<Object> {

        @Override
        protected Object getObject() {
            return new ChainModifierListenerList();
        }

        @Override
        protected Object getDifferentObject() {
            return 1;
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class FeedExecutorListenerListHCAE
            extends TestHashCodeAndEquals<Object> {

        @Override
        protected Object getObject() {
            return new FeedExecutorListenerList();
        }

        @Override
        protected Object getDifferentObject() {
            return 1;
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class PublisherListenerListHCAE
            extends TestHashCodeAndEquals<Object> {

        @Override
        protected Object getObject() {
            return new PublisherListenerList();
        }

        @Override
        protected Object getDifferentObject() {
            return 1;
        }
    }
}
