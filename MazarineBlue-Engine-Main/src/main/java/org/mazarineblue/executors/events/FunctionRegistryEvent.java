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
package org.mazarineblue.executors.events;

import org.mazarineblue.eventdriven.InvokerEvent;
import org.mazarineblue.executors.FunctionRegistry;
import org.mazarineblue.utilities.ObjectWrapper;
import org.mazarineblue.utilities.SerializableClonable;

public class FunctionRegistryEvent
        extends InvokerEvent {

    private static final long serialVersionUID = 1L;

    private transient ObjectWrapper<FunctionRegistry> wrapper;

    FunctionRegistryEvent() {
        this.wrapper = new ObjectWrapper<>(null);
    }

    FunctionRegistryEvent(FunctionRegistry registry) {
        this.wrapper = new ObjectWrapper<>(registry);
    }

    public void setVariableStore(FunctionRegistry registry) {
        wrapper.set(registry);
    }

    public FunctionRegistry getRegistry() {
        return wrapper.get();
    }

    @Override
    public <T extends SerializableClonable> void copyTransient(T src) {
        wrapper = ((FunctionRegistryEvent) src).wrapper;
    }
}
