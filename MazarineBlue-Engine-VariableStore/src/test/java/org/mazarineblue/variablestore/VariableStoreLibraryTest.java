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
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.eventbus.Event;
import org.mazarineblue.eventbus.link.EventBusLink;
import org.mazarineblue.eventdriven.Interpreter;
import org.mazarineblue.eventdriven.feeds.MemoryFeed;
import org.mazarineblue.keyworddriven.LibraryRegistry;
import org.mazarineblue.keyworddriven.events.ExecuteInstructionLineEvent;
import org.mazarineblue.variablestore.events.EndVariableScopeEvent;
import org.mazarineblue.variablestore.events.GetVariableEvent;
import org.mazarineblue.variablestore.events.StartVariableScopeEvent;
import org.mazarineblue.variablestore.exceptions.SymbolNotFoundException;
import org.mazarineblue.variablestore.exceptions.VariableScopeOutOfBalanceException;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class VariableStoreLibraryTest {

    private Interpreter interpreter;

    @Before
    public void setup() {
        VariableStoreLibrary variableStore = new VariableStoreLibrary();
        LibraryRegistry registry = new LibraryRegistry(variableStore);
        
        EventBusLink link = new EventBusLink();
        link.subscribe(Event.class, null, registry);

        interpreter = Interpreter.newInstance();
        interpreter.addLink(link);
    }

    @After
    public void teardown() {
        interpreter = null;
    }

    @Test(expected = VariableScopeOutOfBalanceException.class)
    public void endVariableScopeEvent_OutOfBalance() {
        interpreter.execute(new MemoryFeed(new EndVariableScopeEvent()));
    }

    @Test
    public void getVariable() {
        GetVariableEvent e = new GetVariableEvent("key");
        interpreter.execute(new MemoryFeed(new ExecuteInstructionLineEvent("Set", "key", "foo"),
                                           e));
        assertEquals("foo", e.getValue());
    }

    @Test
    public void getVariable_WithinScope() {
        GetVariableEvent e = new GetVariableEvent("key");
        interpreter.execute(new MemoryFeed(new StartVariableScopeEvent(),
                                           new ExecuteInstructionLineEvent("Set", "key", "foo"),
                                           e));
        assertEquals("foo", e.getValue());
    }

    @Test(expected = SymbolNotFoundException.class)
    public void getVariable_OutOfScope() {
        GetVariableEvent e = new GetVariableEvent("key");
        interpreter.execute(new MemoryFeed(new StartVariableScopeEvent(),
                                           new ExecuteInstructionLineEvent("Set", "key", "foo"),
                                           new EndVariableScopeEvent(),
                                           e));
    }

    @Test(expected = SymbolNotFoundException.class)
    public void getVariable_GlobalVariableUnavailableWithinScope() {
        GetVariableEvent e = new GetVariableEvent("key");
        interpreter.execute(new MemoryFeed(new ExecuteInstructionLineEvent("Set", "key", "global"),
                                           new StartVariableScopeEvent(),
                                           new ExecuteInstructionLineEvent("Set", "foo", "local"),
                                           e));
        assertEquals("local", e.getValue());
    }

    @Test
    public void getVariable_LocalVariableUnavailableWithinScope() {
        GetVariableEvent e = new GetVariableEvent("key");
        interpreter.execute(new MemoryFeed(new ExecuteInstructionLineEvent("Set", "foo", "global"),
                                           new StartVariableScopeEvent(),
                                           new ExecuteInstructionLineEvent("Set", "key", "local"),
                                           e));
        assertEquals("local", e.getValue());
    }

    @Test
    public void getVariable_GlobalVariableAvailableAgainWhenOutOfScope() {
        GetVariableEvent e = new GetVariableEvent("key");
        interpreter.execute(new MemoryFeed(new ExecuteInstructionLineEvent("Set", "key", "global"),
                                           new StartVariableScopeEvent(),
                                           new ExecuteInstructionLineEvent("Set", "foo", "local"),
                                           new EndVariableScopeEvent(),
                                           e));
        assertEquals("global", e.getValue());
    }
}
