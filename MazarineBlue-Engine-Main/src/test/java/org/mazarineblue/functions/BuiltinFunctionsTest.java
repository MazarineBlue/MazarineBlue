/*
 * Copyright (c) 2017 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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

import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.eventdriven.feeds.MemoryFeed;
import org.mazarineblue.executors.Executor;
import org.mazarineblue.executors.events.CreateFeedExecutorEvent;
import org.mazarineblue.executors.events.SetFileSystemEvent;
import org.mazarineblue.executors.util.FeedExecutorOutputSpy;
import org.mazarineblue.executors.util.TestFeedExecutorFactory;
import org.mazarineblue.fs.MemoryFileSystem;
import org.mazarineblue.functions.exceptions.EndFunctionMissingException;
import org.mazarineblue.functions.exceptions.FunctionMissingException;
import org.mazarineblue.functions.exceptions.NestedFunctionDedefintionNotAllowedException;
import org.mazarineblue.keyworddriven.events.ExecuteInstructionLineEvent;
import org.mazarineblue.keyworddriven.exceptions.ToFewArgumentsException;
import org.mazarineblue.plugins.PluginLoader;
import org.mazarineblue.variablestore.events.GetVariableEvent;
import org.mazarineblue.variablestore.exceptions.VariableNotStoredException;

public class BuiltinFunctionsTest {

    private FeedExecutorOutputSpy output;
    private Executor executor;

    @Before
    public void setup() {
        MemoryFileSystem fs = new MemoryFileSystem();
        output = new FeedExecutorOutputSpy();
        executor = TestFeedExecutorFactory.newInstance(fs, output).create();
        executor.execute(new MemoryFeed(new SetFileSystemEvent(fs)));
    }

    @After
    public void teardown() {
        PluginLoader.getInstance().reload();
        output = null;
        executor = null;
    }

    @Test(expected = ToFewArgumentsException.class)
    public void function_FunctionWithoutName() {
        executor.execute(new MemoryFeed(new ExecuteInstructionLineEvent("Function")));
        output.throwFirstException();
    }

    @Test(expected = EndFunctionMissingException.class)
    public void function_FunctionWithoutEndFunction() {
        executor.execute(new MemoryFeed(new ExecuteInstructionLineEvent("Function", "foo")));
        output.throwFirstException();
    }

    @Test(expected = FunctionMissingException.class)
    public void function_EndFunctionWithoutFunction() {
        executor.execute(new MemoryFeed(new ExecuteInstructionLineEvent("End function")));
        output.throwFirstException();
    }

    @Test(expected = NestedFunctionDedefintionNotAllowedException.class)
    public void function_Nested() {
        executor.execute(new MemoryFeed(new ExecuteInstructionLineEvent("Function", "foo"),
                                        new ExecuteInstructionLineEvent("Function", "bar"),
                                        new ExecuteInstructionLineEvent("End function")));
        output.throwFirstException();
    }

    @Test
    public void function_NoReturnValue() {
        ExecuteInstructionLineEvent e = new ExecuteInstructionLineEvent("foo bar");
        executor.execute(new MemoryFeed(new ExecuteInstructionLineEvent("Function", "foo bar"),
                                        new ExecuteInstructionLineEvent("Return"),
                                        new ExecuteInstructionLineEvent("End function"),
                                        e));
        output.throwFirstException();
        assertEquals(null, e.getResult());
    }

    @Test
    public void function_ReturnValue() {
        ExecuteInstructionLineEvent e = new ExecuteInstructionLineEvent("foo");
        executor.execute(new MemoryFeed(new ExecuteInstructionLineEvent("Function", "foo"),
                                        new ExecuteInstructionLineEvent("Return", "bar"),
                                        new ExecuteInstructionLineEvent("End function"),
                                        e));
        output.throwFirstException();
        assertEquals("bar", e.getResult());
    }

    @Test
    public void function_InstructionAfterReturn() {
        GetVariableEvent e = new GetVariableEvent("var");
        executor.execute(new MemoryFeed(new ExecuteInstructionLineEvent("Function", "foo"),
                                        new ExecuteInstructionLineEvent("Return"),
                                        new ExecuteInstructionLineEvent("Set global", "var", "bar"),
                                        new ExecuteInstructionLineEvent("End function"),
                                        new ExecuteInstructionLineEvent("foo"),
                                        e));
        output.throwFirstException();
        assertEquals(null, e.getValue());
    }

    @Test
    public void function_GlobalVariable() {
        GetVariableEvent e = new GetVariableEvent("var");
        executor.execute(new MemoryFeed(new ExecuteInstructionLineEvent("Function", "foo"),
                                        e,
                                        new ExecuteInstructionLineEvent("End function"),
                                        new ExecuteInstructionLineEvent("Set", "var", "bar"),
                                        new ExecuteInstructionLineEvent("foo")));
        output.throwFirstException();
        assertEquals("bar", e.getValue());
    }

    @Test
    public void function_SetGlobalFromWithinFunction() {
        GetVariableEvent e = new GetVariableEvent("var");
        executor.execute(new MemoryFeed(new ExecuteInstructionLineEvent("Function", "foo"),
                                        new ExecuteInstructionLineEvent("Set global", "var", "bar"),
                                        new ExecuteInstructionLineEvent("End function"),
                                        new ExecuteInstructionLineEvent("foo"),
                                        e));
        output.throwFirstException();
        assertEquals("bar", e.getValue());
    }

    @Test(expected = VariableNotStoredException.class)
    public void function_OuterVariableOutOfScope() {
        GetVariableEvent e = new GetVariableEvent("var");
        executor.execute(new MemoryFeed(new ExecuteInstructionLineEvent("Function", "foo"),
                                        new ExecuteInstructionLineEvent("Set", "var", "bar"),
                                        new ExecuteInstructionLineEvent("bar"),
                                        new ExecuteInstructionLineEvent("End function"),
                                        new ExecuteInstructionLineEvent("Function", "bar"),
                                        e,
                                        new ExecuteInstructionLineEvent("End function"),
                                        new ExecuteInstructionLineEvent("foo")));
        output.throwFirstException();
        assertEquals(null, e.getValue());
    }

    @Test(expected = VariableNotStoredException.class)
    public void function_InnerVariableOutOfScope() {
        GetVariableEvent e = new GetVariableEvent("var");
        executor.execute(new MemoryFeed(new ExecuteInstructionLineEvent("Function", "foo"),
                                        new ExecuteInstructionLineEvent("Set", "var", "bar"),
                                        new ExecuteInstructionLineEvent("End function"),
                                        new ExecuteInstructionLineEvent("foo"),
                                        e));
        output.throwFirstException();
    }

    @Test
    public void function_VariableInOfScope() {
        GetVariableEvent e = new GetVariableEvent("var");
        executor.execute(new MemoryFeed(new ExecuteInstructionLineEvent("Function", "foo"),
                                        new ExecuteInstructionLineEvent("Set", "var", "bar"),
                                        e,
                                        new ExecuteInstructionLineEvent("End function"),
                                        new ExecuteInstructionLineEvent("foo")));
        output.throwFirstException();
        assertEquals("bar", e.getValue());
    }

    @Test
    public void function_Argument() {
        GetVariableEvent e = new GetVariableEvent("symbol");
        executor.execute(new MemoryFeed(new ExecuteInstructionLineEvent("Function", "foo", "symbol"),
                                        e,
                                        new ExecuteInstructionLineEvent("End function"),
                                        new ExecuteInstructionLineEvent("foo", "value")));
        output.throwFirstException();
        assertEquals("value", e.getValue());
    }

    @Test
    public void function_Adaptee() {
        CreateFeedExecutorEvent<Executor> e = new CreateFeedExecutorEvent<>();
        executor.execute(new MemoryFeed(new ExecuteInstructionLineEvent("Function", "foo"),
                                        new ExecuteInstructionLineEvent("End function"),
                                        e));
        e.getResult().execute(new MemoryFeed(new ExecuteInstructionLineEvent("foo", "value")));
        output.throwFirstException();
    }
}
