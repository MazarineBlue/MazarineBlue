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

import org.mazarineblue.variablestore.VariableStore;

/**
 * A {@code SetVariableEvent} is a {@code SymbolVariableEvent} that
 * instructs a {@link VariableStore} to store the specified value under the
 * specified symbol.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class SetVariableEvent
        extends SymbolVariableEvent {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a {@code AssignVariableEvent} that stores the specified value
     * under the specified symbol.
     *
     * @param symbol the symbol to store the specified value under.
     * @param value  the value to store under the specified symbol.
     */
    public SetVariableEvent(String symbol, Object value) {
        super(symbol, value);
    }
}
