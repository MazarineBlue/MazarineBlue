/*
 * Copyright (c) Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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
package org.mazarineblue.variablestore;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.After;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mazarineblue.eventnotifier.Event;
import org.mazarineblue.utilities.util.TestHashCodeAndEquals;
import org.mazarineblue.variablestore.events.GetGlobalVariableStoreEvent;
import org.mazarineblue.variablestore.events.SetGlobalVariableStoreEvent;
import org.mazarineblue.variablestore.events.SetVariableEvent;
import org.mazarineblue.variablestore.events.StartVariableScopeEvent;
import org.mazarineblue.variablestore.events.SymbolVariableEvent;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
@RunWith(HierarchicalContextRunner.class)
public class HashCodeAndEqualsTest {

    @SuppressWarnings("PublicInnerClass")
    public class ThreadSafeVariableStoreHCAE
            extends TestHashCodeAndEquals<VariableStore> {

        @Override
        protected VariableStore getObject() {
            VariableStore store = new ThreadSafeVariableStore("scope");
            store.eventHandler(new SetVariableEvent("symbol", "value"));
            return store;
        }

        @Override
        protected VariableStore getDifferentObject() {
            return new ThreadSafeVariableStore("scope");
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class RegularVariableStoreHCAE
            extends TestHashCodeAndEquals<VariableStore> {

        @Override
        protected VariableStore getObject() {
            VariableStore store = new RegularVariableStore("scope");
            store.eventHandler(new SetVariableEvent("symbol", "value"));
            return store;
        }

        @Override
        protected VariableStore getDifferentObject() {
            return new RegularVariableStore("scope");
        }
    }

    /* ********************************************************************** *
     *                               Libraries                                *
     * ********************************************************* ,,^..^,, *** */

    @SuppressWarnings("PublicInnerClass")
    public class ScopedVariableStoreLibraryHCAE
            extends TestHashCodeAndEquals<VariableStoreSubscriber> {

        private VariableStoreSubscriber variableStore;

        @Before
        public void setup() {
            variableStore = new VariableStoreSubscriber("global");
        }

        @After
        public void teardown() {
            variableStore = null;
        }

        @Override
        protected VariableStoreSubscriber getIdenticalObject() {
            VariableStoreSubscriber store = new VariableStoreSubscriber("global");
            store.eventHandler(new SetVariableEvent("key", "foo"));
            return store;
        }

        @Override
        protected VariableStoreSubscriber getObject() {
            variableStore.eventHandler(new SetVariableEvent("key", "foo"));
            return variableStore;
        }

        @Override
        protected VariableStoreSubscriber getDifferentObject() {
            VariableStoreSubscriber store = new VariableStoreSubscriber("global");
            store.eventHandler(new SetVariableEvent("key", "oof"));
            return store;
        }

        @Test
        public void hashCode_VariableStoresWithDifferentStackSizes() {
            VariableStoreSubscriber other = new VariableStoreSubscriber("global");
            other.eventHandler(new SetVariableEvent("key", "oof"));
            assertNotEquals(variableStore.hashCode(), other.hashCode());
        }

        @Test
        public void equals_VariableStoresWithDifferentStackSizes() {
            VariableStoreSubscriber other = new VariableStoreSubscriber("global");
            other.eventHandler(new SetVariableEvent("key", "oof"));
            assertFalse(variableStore.equals(other));
        }
    }

    /* ********************************************************************** *
     *                                 Events                                 *
     * ********************************************************* ,,^..^,, *** */

    @SuppressWarnings("PublicInnerClass")
    public class SymbolVariableEventHCAE
            extends TestHashCodeAndEquals<Event> {

        @Test
        public void hashCode_DifferentSymbol() {
            SymbolVariableEvent a = new SetVariableEvent("symbol", "variable");
            SymbolVariableEvent b = new SetVariableEvent("lobmys", "variable");
            assertNotEquals(a.hashCode(), b.hashCode());
        }

        @Test
        public void equals_DifferentSymbol() {
            SymbolVariableEvent a = new SetVariableEvent("symbol", "variable");
            SymbolVariableEvent b = new SetVariableEvent("lobmys", "variable");
            assertFalse(a.equals(b));
        }

        @Override
        protected Event getObject() {
            return new SetVariableEvent("symbol", "variable");
        }

        @Override
        protected Event getDifferentObject() {
            return new SetVariableEvent("symbol", "elbairav");
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class StartVariableScopeEventHCAE
            extends TestHashCodeAndEquals<Event>{

        @Override
        protected Event getObject() {
            return new StartVariableScopeEvent("foo");
        }

        @Override
        protected Event getDifferentObject() {
            return new StartVariableScopeEvent("bar");
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class GetGlobalVariableStoreEventHCAE
            extends TestHashCodeAndEquals<Event>{

        @Override
        protected Event getObject() {
            GetGlobalVariableStoreEvent e  = new GetGlobalVariableStoreEvent();
            e.setVariableStore(new RegularVariableStore("foo"));
            return e;
        }

        @Override
        protected Event getDifferentObject() {
            GetGlobalVariableStoreEvent e  = new GetGlobalVariableStoreEvent();
            e.setVariableStore(new RegularVariableStore("bar"));
            return e;
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class SetGlobalVariableStoreEventHCAE
            extends TestHashCodeAndEquals<Event>{

        @Override
        protected Event getObject() {
            return new SetGlobalVariableStoreEvent(new RegularVariableStore("foo"));
        }

        @Override
        protected Event getDifferentObject() {
            return new SetGlobalVariableStoreEvent(new RegularVariableStore("bar"));
        }
    }
}
