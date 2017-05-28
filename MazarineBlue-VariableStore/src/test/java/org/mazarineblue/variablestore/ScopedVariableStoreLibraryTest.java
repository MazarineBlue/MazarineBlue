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
package org.mazarineblue.variablestore;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.eventbus.link.EventBusLink;
import org.mazarineblue.eventdriven.Interpreter;
import org.mazarineblue.eventdriven.feeds.MemoryFeed;
import org.mazarineblue.keyworddriven.LibraryRegistry;
import org.mazarineblue.keyworddriven.events.ExecuteInstructionLineEvent;
import org.mazarineblue.keyworddriven.events.InstructionLineEvent;
import org.mazarineblue.variablestore.events.AssignVariableEvent;
import org.mazarineblue.variablestore.events.EndVariableScopeEvent;
import org.mazarineblue.variablestore.events.GetVariableEvent;
import org.mazarineblue.variablestore.events.StartVariableScopeEvent;
import org.mazarineblue.variablestore.events.VariableStoreEvent;
import org.mazarineblue.variablestore.exceptions.SymbolNotFoundException;

public class ScopedVariableStoreLibraryTest {

    private ScopedVariableStoreLibrary variableStore;
    private LibraryRegistry registry;
    private Interpreter interpreter;

    @Before
    public void setup() {
        variableStore = new ScopedVariableStoreLibrary();
        registry = new LibraryRegistry(variableStore);

        EventBusLink link = new EventBusLink();
        link.subscribe(VariableStoreEvent.class, null, variableStore);
        link.subscribe(InstructionLineEvent.class, null, registry);

        interpreter = Interpreter.getDefaultInstance();
        interpreter.addLink(link);
    }

    @After
    public void teardown() {
        variableStore = null;
        registry = null;
        interpreter = null;
    }

    @Test
    public void getVariable_WithinScope_ReturnsTheVariable() {
        interpreter.execute(new MemoryFeed(new StartVariableScopeEvent(),
                                           new ExecuteInstructionLineEvent("Set", "key", "foo")));
        GetVariableEvent e = new GetVariableEvent("key");
        variableStore.eventHandler(e);
        assertEquals("foo", e.getValue());
    }

    @Test(expected = SymbolNotFoundException.class)
    public void getVariable_OutOfScope_ThrowsException() {
        interpreter.execute(new MemoryFeed(new StartVariableScopeEvent(),
                                           new ExecuteInstructionLineEvent("Set", "key", "foo"),
                                           new EndVariableScopeEvent()));
        variableStore.eventHandler(new GetVariableEvent("key"));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void equals_Null_ReturnsFalse() {
        assertFalse(variableStore.equals(null));
    }

    @Test
    @SuppressWarnings("IncompatibleEquals")
    public void equals_String_ReturnsFalse() {
        assertFalse(variableStore.equals(""));
    }

    @Test
    public void equals_VariableStoresWithDifferentStackSizes_ReturnsFalse() {
        ScopedVariableStoreLibrary other = new ScopedVariableStoreLibrary();
        other.eventHandler(new AssignVariableEvent("key", "oof"));
        assertFalse(variableStore.equals(other));
    }

    @Test
    public void hashCode_VariableStoresWithDifferentStackSizes_ReturnsFalse() {
        ScopedVariableStoreLibrary other = new ScopedVariableStoreLibrary();
        other.eventHandler(new AssignVariableEvent("key", "oof"));
        assertNotEquals(variableStore.hashCode(), other.hashCode());
    }

    @Test
    public void equals_DifferentVariableStore_ReturnsFalse() {
        variableStore.eventHandler(new AssignVariableEvent("key", "foo"));
        ScopedVariableStoreLibrary other = new ScopedVariableStoreLibrary();
        other.eventHandler(new AssignVariableEvent("key", "oof"));
        assertFalse(variableStore.equals(other));
    }

    @Test
    public void hashCode_DifferentVariableStore_AreDifferent() {
        variableStore.eventHandler(new AssignVariableEvent("key", "foo"));
        ScopedVariableStoreLibrary other = new ScopedVariableStoreLibrary();
        other.eventHandler(new AssignVariableEvent("key", "oof"));
        assertNotEquals(variableStore.hashCode(), other.hashCode());
    }

    @Test
    public void equals_IdenticalVariableStore_ReturnsFalse() {
        variableStore.eventHandler(new AssignVariableEvent("key", "foo"));
        ScopedVariableStoreLibrary other = new ScopedVariableStoreLibrary();
        other.eventHandler(new AssignVariableEvent("key", "foo"));
        assertTrue(variableStore.equals(other));
    }

    @Test
    public void hashCode_IdenticalVariableStore_AreEqual() {
        variableStore.eventHandler(new AssignVariableEvent("key", "foo"));
        ScopedVariableStoreLibrary other = new ScopedVariableStoreLibrary();
        other.eventHandler(new AssignVariableEvent("key", "foo"));
        assertEquals(variableStore.hashCode(), other.hashCode());
    }
}
