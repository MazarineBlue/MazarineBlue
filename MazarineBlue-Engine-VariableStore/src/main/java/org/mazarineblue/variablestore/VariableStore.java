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

import org.mazarineblue.eventdriven.Processor;
import org.mazarineblue.eventnotifier.Subscriber;
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
public interface VariableStore {

    public String getScope();

    public int size();

    /**
     * Event handlers are not meant to be called directly, instead publish an
     * event to an {@link Processor}; please see the specified event for more
     * information about this event handler.
     *
     * @param event the event this {@code EventHandler} processes.
     * @see SetVariableEvent
     */
    public void eventHandler(SetVariableEvent event);

    /**
     * Event handlers are not meant to be called directly, instead publish an
     * event to an {@link Processor}; please see the specified event for more
     * information about this event handler.
     *
     * @param event the event this {@code EventHandler} processes.
     * @see GetVariableEvent
     */
    public void eventHandler(GetVariableEvent event);

    public boolean containsVariable(String key);
}
