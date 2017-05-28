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

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import org.mazarineblue.eventbus.EventHandler;
import org.mazarineblue.eventdriven.Invoker;
import org.mazarineblue.keyworddriven.Keyword;
import org.mazarineblue.keyworddriven.Library;
import org.mazarineblue.keyworddriven.Parameters;
import org.mazarineblue.keyworddriven.PassInvoker;
import org.mazarineblue.variablestore.events.AssignVariableEvent;
import org.mazarineblue.variablestore.events.EndVariableScopeEvent;
import org.mazarineblue.variablestore.events.StartVariableScopeEvent;
import org.mazarineblue.variablestore.events.SymbolVariableEvent;

public class ScopedVariableStoreLibrary
        extends Library {

    private final Deque<VariableStore> stack = new ArrayDeque<>(16);

    public ScopedVariableStoreLibrary() {
        super("org.mazarineblue");
        stack.push(new VariableStore());
    }

    @Override
    public String toString() {
        return "stacks=" + stack.size() + ", size=" + size();
    }

    public int size() {
        return stack.stream().map(VariableStore::size).reduce(0, Integer::sum);
    }

    @Keyword("Set")
    @Parameters(min = 2)
    @PassInvoker
    public void set(Invoker invoker, String symbol, Object value) {
        invoker.publish(new AssignVariableEvent(symbol, value));
    }

    @EventHandler
    public void eventHandler(StartVariableScopeEvent event) {
        stack.push(new VariableStore());
        event.setConsumed(true);
    }

    @EventHandler
    public void eventHandler(EndVariableScopeEvent event) {
        stack.pop();
        event.setConsumed(true);
    }

    @EventHandler
    public void eventHandler(SymbolVariableEvent event) {
        stack.peek().eventHandler(event);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        for (VariableStore store : stack)
            hash = 89 * hash + store.hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object obj) { // @TODO write tests.
        return obj != null && getClass() == obj.getClass()
                && this.stack.size() == ((ScopedVariableStoreLibrary) obj).stack.size()
                && equals((ScopedVariableStoreLibrary) obj);
    }

    private boolean equals(ScopedVariableStoreLibrary other) {
        Iterator<VariableStore> it1 = this.stack.iterator();
        Iterator<VariableStore> it2 = other.stack.iterator();
        while (it1.hasNext())
            if (!it1.next().equals(it2.next()))
                return false;
        return true;
    }
}
