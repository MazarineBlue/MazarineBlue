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
package org.mazarineblue.variablestore.events;

import java.util.Objects;
import org.mazarineblue.eventdriven.InvokerEvent;
import org.mazarineblue.utilities.ObjectWrapper;
import org.mazarineblue.utilities.SerializableClonable;
import org.mazarineblue.variablestore.VariableStore;

/**
 * A {@code VariableStoreEventVariableStoreEvent} is the base event for all
 * events that manipulate variable stores.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public abstract class VariableStoreEvent
        extends InvokerEvent {

    private static final long serialVersionUID = 1L;

    private transient ObjectWrapper<VariableStore> wrapper;

    VariableStoreEvent() {
        this.wrapper = new ObjectWrapper<>(null);
    }

    VariableStoreEvent(VariableStore store) {
        this.wrapper = new ObjectWrapper<>(store);
    }

    public void setVariableStore(VariableStore store) {
        wrapper.set(store);
    }

    public VariableStore getStore() {
        return wrapper.get();
    }

    @Override
    public <T extends SerializableClonable> void copyTransient(T src) {
        wrapper = ((VariableStoreEvent) src).wrapper;
    }

    @Override
    public int hashCode() {
        return 41 * 3
                + Objects.hashCode(this.wrapper);
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj != null && getClass() == obj.getClass()
                && Objects.equals(this.wrapper, ((VariableStoreEvent) obj).wrapper);
    }
}
