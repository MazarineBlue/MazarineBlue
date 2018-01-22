/*
 * Copyright (c) 2018 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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

import java.util.Objects;
import org.mazarineblue.eventnotifier.Event;
import org.mazarineblue.eventnotifier.ReflectionSubscriber;
import org.mazarineblue.variablestore.events.GetVariableEvent;
import org.mazarineblue.variablestore.events.SetVariableEvent;

/**
 * A {@code VariableStore} is a {@link Subscriber} that stores symbols and
 * there values.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 * @see SetVariableEvent
 * @see GetVariableEvent
 */
class ThreadSafeVariableStore
        extends ReflectionSubscriber<Event>
        implements VariableStore {

    final VariableStore store;

    ThreadSafeVariableStore(String scope) {
        this.store = new RegularVariableStore(scope);
    }

    @Override
    public String toString() {
        return store.toString();
    }

    @Override
    public int size() {
        return store.size();
    }

    @Override
    public String getScope() {
        return store.getScope();
    }

    @Override
    public boolean containsVariable(String key) {
        return store.containsVariable(key);
    }

    /**
     * Event handlers are not meant to be called directly, instead publish an
     * event to an {@link Processor}; please see the specified event for more
     * information about this event handler.
     *
     * @param event the event this {@code EventHandler} processes.
     * @see SetVariableEvent
     */
    @Override
    public synchronized void eventHandler(SetVariableEvent event) {
        store.eventHandler(event);
    }

    /**
     * Event handlers are not meant to be called directly, instead publish an
     * event to an {@link Processor}; please see the specified event for more
     * information about this event handler.
     *
     * @param event the event this {@code EventHandler} processes.
     * @see GetVariableEvent
     */
    @Override
    public synchronized void eventHandler(GetVariableEvent event) {
        store.eventHandler(event);
    }

    @Override
    public int hashCode() {
        return 17 * 7
                + Objects.hashCode(this.store);
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj ||  obj != null && getClass() == obj.getClass()
                && Objects.equals(this.store, ((ThreadSafeVariableStore) obj).store);
    }
}
