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

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mazarineblue.eventbus.filters.BlockingFilter;
import org.mazarineblue.eventbus.subscribers.DummySubscriber;
import org.mazarineblue.utililities.util.TestHashCodeAndEquals;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
@RunWith(HierarchicalContextRunner.class)
public class HashCodeAndEqualsTest {

    @SuppressWarnings("PublicInnerClass")
    public class AbstractSubscriberEventTestHCAE
            extends TestHashCodeAndEquals<DummySubscriberEvent> {

        @Override
        protected DummySubscriberEvent getObject() {
            return new DummySubscriberEvent(new DummySubscriber<>());
        }

        @Override
        protected DummySubscriberEvent getDifferentObject() {
            return new DummySubscriberEvent(new DummyEventSubscriberSpy());
        }

        @Test
        public void hashCode_DifferentTypes() {
            AbstractSubscriberEvent a = new DummySubscriberEvent(new DummySubscriber<>());
            AbstractSubscriberEvent b = new DummySubscriberEvent(new DummySubscriber<>());
            b.setType(AbstractSubscriberEvent.class);
            assertNotEquals(a.hashCode(), b.hashCode());
        }

        @Test
        public void hashCode_DifferentFilters() {
            AbstractSubscriberEvent a = new DummySubscriberEvent(new DummySubscriber<>());
            AbstractSubscriberEvent b = new DummySubscriberEvent(new DummySubscriber<>());
            b.setFilter(new BlockingFilter<>());
            assertNotEquals(a.hashCode(), b.hashCode());
        }

        @Test
        public void equals_DifferentTypes() {
            AbstractSubscriberEvent a = new DummySubscriberEvent(new DummySubscriber<>());
            AbstractSubscriberEvent b = new DummySubscriberEvent(new DummySubscriber<>());
            b.setType(AbstractSubscriberEvent.class);
            assertFalse(a.equals(b));
        }

        @Test
        public void equals_DifferentFilters() {
            AbstractSubscriberEvent a = new DummySubscriberEvent(new DummySubscriber<>());
            AbstractSubscriberEvent b = new DummySubscriberEvent(new DummySubscriber<>());
            b.setFilter(new BlockingFilter<>());
            assertFalse(a.equals(b));
        }
    }
}
