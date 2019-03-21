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

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.eventdriven.Processor;
import org.mazarineblue.eventdriven.feeds.MemoryFeed;
import org.mazarineblue.variablestore.events.EndVariableScopeEvent;
import org.mazarineblue.variablestore.events.GetGlobalVariableStoreEvent;
import org.mazarineblue.variablestore.events.GetVariableEvent;
import org.mazarineblue.variablestore.events.SetGlobalVariableStoreEvent;
import org.mazarineblue.variablestore.events.SetVariableEvent;
import org.mazarineblue.variablestore.events.StartVariableScopeEvent;
import org.mazarineblue.variablestore.events.SymbolVariableEvent;
import org.mazarineblue.variablestore.exceptions.ScopeAlreadyRegisteredException;
import org.mazarineblue.variablestore.exceptions.ScopeNotFoundException;
import org.mazarineblue.variablestore.exceptions.VariableNotStoredException;
import org.mazarineblue.variablestore.exceptions.VariableScopeOutOfBalanceException;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class VariableStoreSubscriberTest {

    private Processor processor;

    @Before
    public void setup() {
        processor = Processor.newInstance();
        processor.addLink(new VariableStoreSubscriber("global"));
    }

    @After
    public void teardown() {
        processor = null;
    }

    @Test(expected = VariableScopeOutOfBalanceException.class)
    public void endVariableScopeEvent_OutOfBalance() {
        processor.execute(new MemoryFeed(new EndVariableScopeEvent()));
    }

    @Test
    public void getVariable() {
        GetVariableEvent e = new GetVariableEvent("key");
        processor.execute(new MemoryFeed(new SetVariableEvent("key", "value"),
                                           e));
        assertTrue(e.isConsumed());
        assertEquals("value", e.getValue());
    }

    @Test
    public void getVariable_WithinScope() {
        GetVariableEvent e = new GetVariableEvent("key");
        processor.execute(new MemoryFeed(new StartVariableScopeEvent(),
                                           new SetVariableEvent("key", "value"),
                                           e));
        assertTrue(e.isConsumed());
        assertEquals("value", e.getValue());
    }

    @Test(expected = VariableNotStoredException.class)
    public void getVariable_OutOfScope() {
        GetVariableEvent e = new GetVariableEvent("key");
        processor.execute(new MemoryFeed(new StartVariableScopeEvent(),
                                           new SetVariableEvent("key", "value"),
                                           new EndVariableScopeEvent(),
                                           e));
    }

    @Test(expected = VariableNotStoredException.class)
    public void getVariable_LocalVariableUnavailableOutOfScope() {
        GetVariableEvent e = new GetVariableEvent("key");
        processor.execute(new MemoryFeed(new StartVariableScopeEvent(),
                                           new SetVariableEvent("key", "value"),
                                           new StartVariableScopeEvent(),
                                           new SetVariableEvent("foo", "value"),
                                           e));
    }

    @Test
    public void getVariable_LocalVariableUnavailableWithinScope() {
        GetVariableEvent e = new GetVariableEvent("key");
        processor.execute(new MemoryFeed(new StartVariableScopeEvent(),
                                           new SetVariableEvent("key", "value"),
                                           new StartVariableScopeEvent(),
                                           new SetVariableEvent("key", "overwritten"),
                                           e));
        assertTrue(e.isConsumed());
        assertEquals("overwritten", e.getValue());
    }

    @Test
    public void getVariable_LocalVariableAvailableAgainWhenInScope() {
        GetVariableEvent e = new GetVariableEvent("key");
        processor.execute(new MemoryFeed(new StartVariableScopeEvent(),
                                           new SetVariableEvent("key", "value"),
                                           new StartVariableScopeEvent(),
                                           new SetVariableEvent("foo", "value"),
                                           new EndVariableScopeEvent(),
                                           e));
        assertTrue(e.isConsumed());
        assertEquals("value", e.getValue());
    }

    @Test
    public void startVariableScopeEvent() {
        StartVariableScopeEvent e = new StartVariableScopeEvent("foo");
        processor.execute(new MemoryFeed(e));
        assertTrue(e.isConsumed());
    }

    @Test
    public void endVariableScopeEvent() {
        EndVariableScopeEvent e = new EndVariableScopeEvent();
        processor.execute(new MemoryFeed(new StartVariableScopeEvent("foo"),
                                           e));
        assertTrue(e.isConsumed());
    }

    @Test(expected = ScopeAlreadyRegisteredException.class)
    public void startVariableScopeEvent_SetScopeTwice() {
        processor.execute(new MemoryFeed(new StartVariableScopeEvent("foo"),
                                           new StartVariableScopeEvent("foo")));
    }

    @Test
    public void setVariableScopeEvent_AfterEndVariableScopeEvent() {
        processor.execute(new MemoryFeed(new StartVariableScopeEvent("foo"),
                                           new EndVariableScopeEvent(),
                                           new StartVariableScopeEvent("foo")));
    }

    @Test(expected = ScopeNotFoundException.class)
    public void getVariable_UnavailableScope() {
        processor.execute(new MemoryFeed(new StartVariableScopeEvent("foo"),
                                           new SetVariableEvent("key", "value"),
                                           new StartVariableScopeEvent(),
                                           new SetVariableEvent("foo", "value"),
                                           new GetVariableEvent("key").withScope("scope")));
    }

    @Test
    public void getVariable_AvailableScope() {
        GetVariableEvent e = new GetVariableEvent("key").withScope("scope");
        processor.execute(new MemoryFeed(new StartVariableScopeEvent("scope"),
                                           new SetVariableEvent("key", "value"),
                                           new StartVariableScopeEvent(),
                                           new SetVariableEvent("foo", "value"),
                                           e));
        assertTrue(e.isConsumed());
        assertEquals("value", e.getValue());
    }

    @Test
    public void getVariable_Hidden() {
        GetVariableEvent e = new GetVariableEvent("key");
        processor.execute(new MemoryFeed(new StartVariableScopeEvent("scope"),
                                           new SetVariableEvent("key", "foo"),
                                           new StartVariableScopeEvent(),
                                           new SetVariableEvent("key", "value"),
                                           e));
        assertTrue(e.isConsumed());
        assertEquals("value", e.getValue());
    }

    @Test
    public void getVariable_FallThrough() {
        GetVariableEvent e = new GetVariableEvent("key");
        processor.execute(new MemoryFeed(new SetVariableEvent("key", "value"),
                                           new StartVariableScopeEvent(),
                                           new SetVariableEvent("foo", "bar"),
                                           e));
        assertTrue(e.isConsumed());
        assertEquals("value", e.getValue());
    }

    @Test
    public void setVariable_UnavailableScope() {
        SymbolVariableEvent e = new SetVariableEvent("key", "value").withScope("foo");
        processor.execute(new MemoryFeed(new StartVariableScopeEvent("scope"),
                                           new StartVariableScopeEvent("foo"),
                                           e));
        assertTrue(e.isConsumed());
    }

    @Test
    public void getGlobalVariableStoreEvent() {
        GetGlobalVariableStoreEvent e = new GetGlobalVariableStoreEvent();
        processor.execute(new MemoryFeed(new StartVariableScopeEvent("scope"),
                                           new SetVariableEvent("key", "value"),
                                           e));
        assertTrue(e.isConsumed());
        assertTrue(e.getStore() instanceof ThreadSafeVariableStore);
        assertEquals(0, e.getStore().size());
    }

    @Test
    public void setGlobalVariableStoreEvent() {
        VariableStore store = new ThreadSafeVariableStore("global");
        store.eventHandler(new SetVariableEvent("key", "value"));
        SetGlobalVariableStoreEvent e1 = new SetGlobalVariableStoreEvent(store);
        GetVariableEvent e2 = new GetVariableEvent("key");
        processor.execute(new MemoryFeed(e1, new StartVariableScopeEvent("scope"), e2));
        assertTrue(e1.isConsumed());
        assertTrue(e2.isConsumed());
        assertEquals("value", e2.getValue());
    }
}
