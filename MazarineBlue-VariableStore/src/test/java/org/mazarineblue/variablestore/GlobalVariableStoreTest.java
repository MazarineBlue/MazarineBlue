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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.variablestore.events.AssignVariableEvent;
import org.mazarineblue.variablestore.events.GetVariableEvent;
import org.mazarineblue.variablestore.exceptions.SymbolNotFoundException;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class GlobalVariableStoreTest {

    private static final String BLOCK = "foo";
    private static final String SYMBOL = "symbol";
    private static final String SYMBOL2 = "symbol2";
    private static final String VALUE = "value";
    private VariableStore store;

    @Before
    public void setup() {
        store = new VariableStore();
    }

    @Test(expected = SymbolNotFoundException.class)
    public void getSymbol_EmptyStore_ThrowsSymbolNotFoundException() {
        store.eventHandler(new GetVariableEvent(SYMBOL));
    }

    @Test
    public void assignSymbol_EmptyStore_Accepts() {
        AssignVariableEvent assign = new AssignVariableEvent(SYMBOL, VALUE);
        store.eventHandler(assign);
        assertTrue(assign.isConsumed());

        GetVariableEvent get = new GetVariableEvent(SYMBOL);
        store.eventHandler(get);
        assertTrue(get.isConsumed());
        assertEquals(VALUE, get.getValue());
    }
}
