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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Objects;
import org.mazarineblue.utililities.SerializableClonable;
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

    private String symbol;
    private Object value;

    protected SymbolVariableEvent(String symbol, Object value) {
        this.symbol = symbol;
        this.value = value;
    }

    protected SymbolVariableEvent(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return "symbol=" + symbol + ", value=" + value;
    }

    public String getSymbol() {
        return symbol;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        return 7 * 97 * 97
                + 97 * 97 * Objects.hashCode(this.symbol)
                + 97 * Objects.hashCode(this.value);
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj != null && getClass() == obj.getClass()
                && Objects.equals(this.symbol, ((SymbolVariableEvent) obj).symbol)
                && Objects.equals(this.value, ((SymbolVariableEvent) obj).value);
    }

    private void writeObject(ObjectOutputStream out)
            throws IOException {
        out.writeObject(symbol);
        out.writeObject(value instanceof Serializable ? value : null);
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
        value = in.readObject();
    }
}
