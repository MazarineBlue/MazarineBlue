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
package org.mazarineblue.variablestore;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.After;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mazarineblue.eventbus.Event;
import org.mazarineblue.utililities.util.TestHashCodeAndEquals;
import org.mazarineblue.variablestore.events.SetVariableEvent;
import org.mazarineblue.variablestore.events.SymbolVariableEvent;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
@RunWith(HierarchicalContextRunner.class)
public class HashCodeAndEqualsTest {

    @SuppressWarnings("PublicInnerClass")
    public class ScopedVariableStoreLibraryHCAE
            extends TestHashCodeAndEquals<VariableStoreLibrary> {

        private VariableStoreLibrary variableStore;

        @Before
        public void setup() {
            variableStore = new VariableStoreLibrary();
        }

        @After
        public void teardown() {
            variableStore = null;
        }

        @Override
        protected VariableStoreLibrary getIdenticalObject() {
            VariableStoreLibrary store = new VariableStoreLibrary();
            store.eventHandler(new SetVariableEvent("key", "foo"));
            return store;
        }

        @Override
        protected VariableStoreLibrary getObject() {
            variableStore.eventHandler(new SetVariableEvent("key", "foo"));
            return variableStore;
        }

        @Override
        protected VariableStoreLibrary getDifferentObject() {
            VariableStoreLibrary store = new VariableStoreLibrary();
            store.eventHandler(new SetVariableEvent("key", "oof"));
            return store;
        }

        @Test
        public void hashCode_VariableStoresWithDifferentStackSizes() {
            VariableStoreLibrary other = new VariableStoreLibrary();
            other.eventHandler(new SetVariableEvent("key", "oof"));
            assertNotEquals(variableStore.hashCode(), other.hashCode());
        }

        @Test
        public void equals_VariableStoresWithDifferentStackSizes() {
            VariableStoreLibrary other = new VariableStoreLibrary();
            other.eventHandler(new SetVariableEvent("key", "oof"));
            assertFalse(variableStore.equals(other));
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class SymbolVariableEventHCAE
            extends TestHashCodeAndEquals<SetVariableEvent> {

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
        protected SetVariableEvent getObject() {
            return new SetVariableEvent("symbol", "variable");
        }

        @Override
        protected SetVariableEvent getDifferentObject() {
            return new SetVariableEvent("symbol", "elbairav");
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class VariableStoreHCAE
            extends TestHashCodeAndEquals<VariableStore> {

        @Override
        protected VariableStore getObject() {
            VariableStore store = new VariableStore();
            store.eventHandler((Event) new SetVariableEvent("symbol", "value"));
            return store;
        }

        @Override
        protected VariableStore getDifferentObject() {
            return new VariableStore();
        }
    }
}
