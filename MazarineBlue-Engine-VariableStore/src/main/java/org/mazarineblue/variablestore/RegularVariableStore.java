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

import java.util.HashMap;
import java.util.Objects;
import org.mazarineblue.eventdriven.Processor;
import org.mazarineblue.eventnotifier.Event;
import org.mazarineblue.eventnotifier.EventHandler;
import org.mazarineblue.eventnotifier.ReflectionSubscriber;
import org.mazarineblue.eventnotifier.Subscriber;
import org.mazarineblue.variablestore.events.GetVariableEvent;
import org.mazarineblue.variablestore.events.SetVariableEvent;
import org.mazarineblue.variablestore.exceptions.VariableNotStoredException;

/**
 * A {@code VariableStore} is a {@link Subscriber} that stores symbols and
 * there values.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 * @see SetVariableEvent
 * @see GetVariableEvent
 */
class RegularVariableStore
        extends ReflectionSubscriber<Event>
        implements VariableStore {

    private final String scope;
    private final HashMap<String, Object> map = new HashMap<>(4);

    RegularVariableStore(String scope) {
        this.scope = scope;
    }

    @Override
    public String toString() {
        return "size = " + map.size();
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public String getScope() {
        return scope;
    }

    @Override
    public boolean containsVariable(String key) {
        return map.containsKey(key);
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
    @EventHandler
    public void eventHandler(SetVariableEvent event) {
        map.put(event.getSymbol(), event.getValue());
        event.setConsumed(true);
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
    @EventHandler
    public void eventHandler(GetVariableEvent event) {
        if (!map.containsKey(event.getSymbol()))
            throw new VariableNotStoredException(event.getSymbol());
        event.setValue(map.get(event.getSymbol()));
        event.setConsumed(true);
    }

    @Override
    public int hashCode() {
        return 3 * 83 * 83
                + 83 * Objects.hashCode(this.scope)
                + Objects.hashCode(this.map);
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj != null && getClass() == obj.getClass()
                && Objects.equals(this.scope, ((RegularVariableStore) obj).scope)
                && Objects.equals(this.map, ((RegularVariableStore) obj).map);
    }
}
