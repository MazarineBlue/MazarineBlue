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

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.mazarineblue.eventdriven.Processor;
import org.mazarineblue.eventnotifier.Event;
import org.mazarineblue.eventnotifier.EventHandler;
import org.mazarineblue.eventnotifier.ReflectionSubscriber;
import org.mazarineblue.variablestore.events.EndVariableScopeEvent;
import org.mazarineblue.variablestore.events.GetGlobalVariableStoreEvent;
import org.mazarineblue.variablestore.events.GetVariableEvent;
import org.mazarineblue.variablestore.events.SetGlobalVariableStoreEvent;
import org.mazarineblue.variablestore.events.SetVariableEvent;
import org.mazarineblue.variablestore.events.StartVariableScopeEvent;
import org.mazarineblue.variablestore.events.SymbolVariableEvent;
import org.mazarineblue.variablestore.exceptions.ScopeAlreadyRegisteredException;
import org.mazarineblue.variablestore.exceptions.ScopeNotFoundException;
import org.mazarineblue.variablestore.exceptions.VariableScopeOutOfBalanceException;

public class VariableStoreSubscriber
        extends ReflectionSubscriber<Event> {

    private final Deque<VariableStore> stack = new ArrayDeque<>(16);
    private final Map<String, VariableStore> map = new HashMap<>(32);

    public VariableStoreSubscriber(String scopeName) {
        this(new ThreadSafeVariableStore(scopeName));
    }

    public VariableStoreSubscriber(VariableStore store) {
        stack.push(store);
        map.put(store.getScope(), store);
    }

    @Override
    public String toString() {
        return "VariableStoreSubscriber{stacks=" + stack.size() + ", size=" + size() + '}';
    }

    public int size() {
        return stack.stream().map(VariableStore::size).reduce(0, Integer::sum);
    }

    /**
     * Event handlers are not meant to be called directly, instead publish an
     * event to an {@link Processor}; please see the specified event for more
     * information about this event handler.
     *
     * @param event the event this {@code EventHandler} processes.
     * @see GetGlobalVariableStoreEvent
     */
    @EventHandler
    public void eventHandler(GetGlobalVariableStoreEvent event) {
        event.setVariableStore(stack.peekLast());
        event.setConsumed(true);
    }

    /**
     * Event handlers are not meant to be called directly, instead publish an
     * event to an {@link Processor}; please see the specified event for more
     * information about this event handler.
     *
     * @param event the event this {@code EventHandler} processes.
     * @see StartVariableScopeEvent
     */
    @EventHandler
    public void eventHandler(SetGlobalVariableStoreEvent event) {
        removeGlobalStore();
        addGlobalStore(event.getStore());
        event.setConsumed(true);
    }

    private void removeGlobalStore() {
        VariableStore store = stack.removeLast();
        unregisterScope(store);
    }

    private void addGlobalStore(VariableStore store) {
        stack.addLast(store);
        registerScope(store);
    }

    /**
     * Event handlers are not meant to be called directly, instead publish an
     * event to an {@link Processor}; please see the specified event for more
     * information about this event handler.
     *
     * @param event the event this {@code EventHandler} processes.
     * @see EndVariableScopeEvent
     */
    @EventHandler
    public void eventHandler(EndVariableScopeEvent event) {
        if (stack.size() == 1)
            throw new VariableScopeOutOfBalanceException(event);
        VariableStore store = stack.pop();
        unregisterScope(store);
        event.setConsumed(true);
    }

    private void unregisterScope(VariableStore store) {
        String scope = store.getScope();
        if (scope == null)
            return;
        map.remove(scope);
    }

    /**
     * Event handlers are not meant to be called directly, instead publish an
     * event to an {@link Processor}; please see the specified event for more
     * information about this event handler.
     *
     * @param event the event this {@code EventHandler} processes.
     * @see StartVariableScopeEvent
     */
    @EventHandler
    public void eventHandler(StartVariableScopeEvent event) {
        RegularVariableStore store = new RegularVariableStore(event.getScope());
        stack.push(store);
        registerScope(store);
        event.setConsumed(true);
    }

    private void registerScope(VariableStore store) {
        String scope = store.getScope();
        if (scope == null)
            return;
        if (map.containsKey(scope))
            throw new ScopeAlreadyRegisteredException(scope);
        map.put(store.getScope(), store);
    }

    /**
     * Event handlers are not meant to be called directly, instead publish an
     * event to an {@link Processor}; please see the specified event for more
     * information about this event handler.
     *
     * @param event the event this {@code EventHandler} processes.
     * @see SymbolVariableEvent
     */
    @EventHandler
    public void eventHandler(SetVariableEvent event) {
        String scope = event.getScope();
        if (scope != null)
            map.get(scope).eventHandler(event);
        else
            stack.peek().eventHandler(event);
    }

    /**
     * Event handlers are not meant to be called directly, instead publish an
     * event to an {@link Processor}; please see the specified event for more
     * information about this event handler.
     *
     * @param event the event this {@code EventHandler} processes.
     * @see SymbolVariableEvent
     */
    @EventHandler
    public void eventHandler(GetVariableEvent event) {
        String scope = event.getScope();
        VariableStore store = scope != null ? lookup(scope) : getDefault(event.getSymbol());
        store.eventHandler(event);
    }

    private VariableStore lookup(String scope) {
        VariableStore store = map.get(scope);
        if (store == null)
            throw new ScopeNotFoundException(scope);
        return store;
    }

    private VariableStore getDefault(String key) {
        return stack.peek().containsVariable(key) ? stack.peek() : stack.getLast();
    }

    @Override
    public int hashCode() {
        int hash = 3;
        for (VariableStore store : stack)
            hash = 89 * hash + store.hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object obj) { // @TODO write tests.
        return this == obj || obj != null && getClass() == obj.getClass()
                && this.stack.size() == ((VariableStoreSubscriber) obj).stack.size()
                && equals((VariableStoreSubscriber) obj);
    }

    private boolean equals(VariableStoreSubscriber other) {
        Iterator<VariableStore> it1 = this.stack.iterator();
        Iterator<VariableStore> it2 = other.stack.iterator();
        while (it1.hasNext())
            if (!it1.next().equals(it2.next()))
                return false;
        return true;
    }
}
