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
package org.mazarineblue.executors;

import org.mazarineblue.eventdriven.Invoker;
import org.mazarineblue.keyworddriven.Keyword;
import org.mazarineblue.keyworddriven.Parameters;
import org.mazarineblue.keyworddriven.PassInvoker;
import org.mazarineblue.variablestore.VariableStoreSubscriber;
import org.mazarineblue.variablestore.events.SetVariableEvent;

/**
 * The {@code BuiltinLibrary} containsSheet the instructions that a build into the
 * {@link DefaultProcessorFactory}.
 *
 * @see DefaultProcessorFactory
 * @see VariableStoreSubscriber
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class BuiltinVariableStoreLibrary
        extends AbstractMainLibrary {

    private final String topVariableScopeName;

    BuiltinVariableStoreLibrary(String topVariableScopeName) {
        this.topVariableScopeName = topVariableScopeName;
    }

    @Keyword("Set")
    @Parameters(min = 2)
    @PassInvoker
    public void set(Invoker invoker, String symbol, Object value) {
        invoker.publish(new SetVariableEvent(symbol, value));
    }

    @Keyword("Set global")
    @Parameters(min = 2)
    @PassInvoker
    public void setGlobal(Invoker invoker, String symbol, Object value) {
        invoker.publish(new SetVariableEvent(symbol, value).withScope(topVariableScopeName));
    }
}
