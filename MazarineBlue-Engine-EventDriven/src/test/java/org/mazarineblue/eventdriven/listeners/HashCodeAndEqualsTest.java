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
package org.mazarineblue.eventdriven.listeners;

import org.mazarineblue.utilities.util.TestHashCodeAndEquals;

public class HashCodeAndEqualsTest {

    @SuppressWarnings("PublicInnerClass")
    public class DummyChainModifierListenerTest
            extends TestHashCodeAndEquals<DummyChainModifierListener> {

        @Override
        @SuppressWarnings("NonPublicExported")
        protected DummyChainModifierListener getObject() {
            return new DummyChainModifierListener();
        }

        @Override
        @SuppressWarnings("NonPublicExported")
        protected DummyChainModifierListener getDifferentObject() {
            return new DummyChainModifierListener() {
            };
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class DummyFeedExecutorListenerTest
            extends TestHashCodeAndEquals<DummyFeedExecutorListener> {

        @Override
        @SuppressWarnings("NonPublicExported")
        protected DummyFeedExecutorListener getObject() {
            return new DummyFeedExecutorListener();
        }

        @Override
        @SuppressWarnings("NonPublicExported")
        protected DummyFeedExecutorListener getDifferentObject() {
            return new DummyFeedExecutorListener() {
            };
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class DummyPublisherListenerTest
            extends TestHashCodeAndEquals<DummyPublisherListener> {

        @Override
        protected DummyPublisherListener getObject() {
            return new DummyPublisherListener();
        }

        @Override
        protected DummyPublisherListener getDifferentObject() {
            return new DummyPublisherListener() {
            };
        }
    }
}
