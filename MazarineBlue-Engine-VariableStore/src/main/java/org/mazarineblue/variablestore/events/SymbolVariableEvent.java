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
package org.mazarineblue.variablestore.events;

import org.mazarineblue.utilities.ObjectWrapper;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Objects;
import org.mazarineblue.utilities.SerializableClonable;
import org.mazarineblue.variablestore.VariableStore;

/**
 * A {@code SymbolVariableEvent} is a {@code VariableStoreEvent} that is the
 * base event for all events that manipulate a {@link VariableStore}.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public abstract class SymbolVariableEvent
        extends VariableStoreEvent {

    private static final long serialVersionUID = 1L;

    private String scope;
    private String symbol;
    private ObjectWrapper<Object> value;

    protected SymbolVariableEvent(String symbol, Object value) {
        this.symbol = symbol;
        this.value = new ObjectWrapper(value);
    }

    protected SymbolVariableEvent(String symbol) {
        this.symbol = symbol;
        this.value = new ObjectWrapper(null);
    }

    public <T extends SymbolVariableEvent> T withScope(String scope) {
        this.scope = scope;
        return (T) this;
    }

    @Override
    public String toString() {
        return "symbol=" + symbol + ", value=" + value;
    }

    public String getScope() {
        return scope;
    }

    public String getSymbol() {
        return symbol;
    }

    public Object getValue() {
        return value.get();
    }

    public void setValue(Object value) {
        this.value.set(value);
    }

    @Override
    public int hashCode() {
        return 7 * 97 * 97
                + 97 * 97 * Objects.hashCode(this.symbol)
                + 97 * Objects.hashCode(this.value.get());
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj != null && getClass() == obj.getClass()
                && Objects.equals(this.symbol, ((SymbolVariableEvent) obj).symbol)
                && Objects.equals(this.value.get(), ((SymbolVariableEvent) obj).value.get());
    }

    private void writeObject(ObjectOutputStream out)
            throws IOException {
        out.writeObject(symbol);
        out.writeObject(value.get() instanceof Serializable ? value.get() : null);
    }

    @Override
    public <E extends SerializableClonable> void copyTransient(E other) {
        super.copyTransient(other);
        symbol = ((SymbolVariableEvent) other).symbol;
        value = ((SymbolVariableEvent) other).value;
    }

    private void readObject(ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        symbol = (String) in.readObject();
        value = new ObjectWrapper(in.readObject());
    }
}
