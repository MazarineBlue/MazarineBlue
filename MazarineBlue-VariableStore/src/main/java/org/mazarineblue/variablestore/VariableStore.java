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
import org.mazarineblue.eventbus.Event;
import org.mazarineblue.eventbus.EventHandler;
import org.mazarineblue.eventbus.ReflectionSubscriber;
import org.mazarineblue.eventbus.Subscriber;
import org.mazarineblue.eventdriven.Interpreter;
import org.mazarineblue.variablestore.events.AssignVariableEvent;
import org.mazarineblue.variablestore.events.GetVariableEvent;
import org.mazarineblue.variablestore.exceptions.SymbolNotFoundException;

/**
 * A {@code VariableStore} is a {@link Subscriber} that stores symbols and
 * there values.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 * @see AssignVariableEvent
 * @see GetVariableEvent
 */
public class VariableStore
        extends ReflectionSubscriber<Event> {

    private final HashMap<String, Object> map = new HashMap<>(4);

    @Override
    public String toString() {
        return "size = " + map.size();
    }

    public int size() {
        return map.size();
    }

    /**
     * Event handlers are not meant to be called directly, instead publish an
     * event to an {@link Interpreter}; please see the specified event for more
     * information about this event handler.
     *
     * @param event the event this {@code EventHandler} processes.
     * @see AssignVariableEvent
     */
    @EventHandler
    public void eventHandler(AssignVariableEvent event) {
        map.put(event.getSymbol(), event.getValue());
        event.setConsumed(true);
    }

    /**
     * Event handlers are not meant to be called directly, instead publish an
     * event to an {@link Interpreter}; please see the specified event for more
     * information about this event handler.
     *
     * @param event the event this {@code EventHandler} processes.
     * @see GetVariableEvent
     */
    @EventHandler
    public void eventHandler(GetVariableEvent event) {
        if (!map.containsKey(event.getSymbol()))
            throw new SymbolNotFoundException(event.getSymbol());
        event.setValue(map.get(event.getSymbol()));
        event.setConsumed(true);
    }

    @Override
    public int hashCode() {
        return 3 * 83
                + Objects.hashCode(this.map);
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && getClass() == obj.getClass() && Objects.equals(this.map, ((VariableStore) obj).map);
    }
}
