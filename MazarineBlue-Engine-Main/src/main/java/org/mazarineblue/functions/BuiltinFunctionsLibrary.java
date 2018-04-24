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
package org.mazarineblue.functions;

import org.mazarineblue.eventdriven.Invoker;
import org.mazarineblue.executors.AbstractMainLibrary;
import org.mazarineblue.executors.exceptions.EndFunctionMissingException;
import org.mazarineblue.executors.exceptions.FunctionMissingException;
import org.mazarineblue.executors.exceptions.NestedFunctionDedefintionNotAllowedException;
import org.mazarineblue.keyworddriven.Keyword;
import org.mazarineblue.keyworddriven.Parameters;
import org.mazarineblue.keyworddriven.PassInvoker;
import static org.mazarineblue.keyworddriven.events.InstructionLineEvent.matchesAnyKeywords;
import org.mazarineblue.subscribers.RecorderSubscriber;

public class BuiltinFunctionsLibrary
        extends AbstractMainLibrary {

    private final FunctionRegistry functionRegistry;
    private Function function;
    private RecorderSubscriber functionRecorder;

    public BuiltinFunctionsLibrary(FunctionRegistry functionRegistry) {
        this.functionRegistry = functionRegistry;
    }

    @Override
    protected void doTeardown(Invoker invoker) {
        super.doTeardown(invoker);
        if (function != null)
            throw new EndFunctionMissingException();
    }

    @Keyword("Function")
    @Parameters(min = 1)
    @PassInvoker
    public void func(Invoker invoker, String name, String... arguments) {
        functionRecorder = new RecorderSubscriber(invoker, matchesAnyKeywords("End function"));
        functionRecorder.addThrowExceptionRule(matchesAnyKeywords("Function"), NestedFunctionDedefintionNotAllowedException::new);
        function = new Function(name, arguments);
        invoker.chain().addLink(functionRecorder);
    }

    @Keyword("End function")
    public void endFunc() {
        if (function == null)
            throw new FunctionMissingException();
        function.setBody(functionRecorder.getRecording());
        functionRegistry.add(function);
        functionRecorder = null;
        function = null;
    }
}
