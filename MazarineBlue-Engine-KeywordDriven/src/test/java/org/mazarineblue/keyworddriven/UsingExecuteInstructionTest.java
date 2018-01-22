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
package org.mazarineblue.keyworddriven;

import org.junit.After;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.eventdriven.Processor;
import org.mazarineblue.eventdriven.feeds.MemoryFeed;
import org.mazarineblue.keyworddriven.events.ExecuteInstructionLineEvent;
import org.mazarineblue.keyworddriven.exceptions.ArgumentsAreIncompatibleException;
import org.mazarineblue.keyworddriven.exceptions.InstructionNotFoundException;
import org.mazarineblue.keyworddriven.exceptions.InstructionTargetException;
import org.mazarineblue.keyworddriven.exceptions.InvokerIsMissingException;
import org.mazarineblue.keyworddriven.exceptions.MethodNotInCallerException;
import org.mazarineblue.keyworddriven.exceptions.MultipleInstructionsFoundException;
import org.mazarineblue.keyworddriven.exceptions.ResultTypeIsIncompatibleException;
import org.mazarineblue.keyworddriven.exceptions.ToFewArgumentsException;
import org.mazarineblue.keyworddriven.util.exceptions.TestRuntimeException;
import org.mazarineblue.keyworddriven.util.libraries.TestCallerLibrary;
import org.mazarineblue.keyworddriven.util.libraries.TestLibrary1;
import org.mazarineblue.keyworddriven.util.libraries.TestLibrary2;
import org.mazarineblue.keyworddriven.util.libraries.WrongCallerDuringRuntimeLibrary;

public class UsingExecuteInstructionTest {

    private Processor processor;
    private TestLibrary1 lib1;
    private TestCallerLibrary callerLib;

    @Before
    public void setup() {
        lib1 = new TestLibrary1();
        callerLib = new TestCallerLibrary();
        LibraryRegistry registry = new LibraryRegistry(lib1, new TestLibrary2(), callerLib,
                                                       new WrongCallerDuringRuntimeLibrary());
        processor = Processor.newInstance();
        processor.addLink(registry);
    }

    @After
    public void teardown() {
        processor = null;
    }

    @Test(expected = InstructionNotFoundException.class)
    public void execute_UnkownInstruction() {
        processor.execute(new MemoryFeed(new ExecuteInstructionLineEvent("Unkown")));
    }

    @Test(expected = MultipleInstructionsFoundException.class)
    public void execute_ConflictedInstruction() {
        processor.execute(new MemoryFeed(new ExecuteInstructionLineEvent("Conflicted")));
    }

    @Test(expected = InstructionTargetException.class)
    public void execute_ExceptionThrowingInstruction() {
        processor.execute(new MemoryFeed(new ExecuteInstructionLineEvent("Throw Exception")));
    }

    @Test(expected = TestRuntimeException.class)
    public void execute_RuntimeExceptionThrowingInstruction() {
        processor.execute(new MemoryFeed(new ExecuteInstructionLineEvent("Throw Runtime Exception")));
    }

    @Test(expected = ToFewArgumentsException.class)
    public void execute_MinimumParametersArgument() {
        processor.execute(new MemoryFeed(new ExecuteInstructionLineEvent("Minimum parameters")));
    }

    @Test(expected = InvokerIsMissingException.class)
    public void execute_InvokerMissing() {
        try {
            processor.execute(new MemoryFeed(new ExecuteInstructionLineEvent("Invoker missing")));
        } catch (RuntimeException ex) {
            assertFalse(lib1.isDoValidate());
            assertFalse(lib1.isDoBeforeExecution());
            assertFalse(lib1.isDoAfterExecution());
            assertTrue(lib1.isDoWhenExceptionThrown());
            throw ex;
        }
    }

    @Test(expected = ArgumentsAreIncompatibleException.class)
    public void execute_IncompatibleArgument() {
        try {
            processor.execute(new MemoryFeed(new ExecuteInstructionLineEvent("Incompatible argument", "foo")));
        } catch (RuntimeException ex) {
            assertFalse(lib1.isDoValidate());
            assertFalse(lib1.isDoBeforeExecution());
            assertFalse(lib1.isDoAfterExecution());
            assertTrue(lib1.isDoWhenExceptionThrown());
            throw ex;
        }
    }

    @Test(expected = ResultTypeIsIncompatibleException.class)
    public void execute_ExpectWrongeResult() {
        ExecuteInstructionLineEvent e = new ExecuteInstructionLineEvent("Instruction with no arguments");
        e.setExpectResultType(Integer.TYPE);
        processor.execute(new MemoryFeed(e));
        assertEquals(null, e.getResult());
        assertEquals("first", lib1.getCalledMethodName());
        assertArrayEquals(new Object[0], lib1.getArguments());
        assertFalse(lib1.isDoValidate());
        assertTrue(lib1.isDoBeforeExecution());
        assertTrue(lib1.isDoAfterExecution());
        assertFalse(lib1.isDoWhenExceptionThrown());
    }

    @Test
    public void execute_InstructionWithoutArgument() {
        ExecuteInstructionLineEvent e = new ExecuteInstructionLineEvent("Instruction with no arguments");
        e.setExpectResultType(Void.TYPE);
        processor.execute(new MemoryFeed(e));
        assertEquals(null, e.getResult());
        assertEquals("first", lib1.getCalledMethodName());
        assertArrayEquals(new Object[0], lib1.getArguments());
        assertFalse(lib1.isDoValidate());
        assertTrue(lib1.isDoBeforeExecution());
        assertTrue(lib1.isDoAfterExecution());
        assertFalse(lib1.isDoWhenExceptionThrown());
    }

    @Test
    public void execute_InstructionWithInvoker() {
        ExecuteInstructionLineEvent e = new ExecuteInstructionLineEvent("Instruction with invoker");
        processor.execute(new MemoryFeed(e));
        assertEquals("withInvoker", lib1.getCalledMethodName());
        assertNotNull(lib1.getArguments()[0]);
        assertFalse(lib1.isDoValidate());
        assertTrue(lib1.isDoBeforeExecution());
        assertTrue(lib1.isDoAfterExecution());
        assertFalse(lib1.isDoWhenExceptionThrown());
    }

    @Test
    public void execute_InstructionWithIntegerInStringForm() {
        ExecuteInstructionLineEvent e = new ExecuteInstructionLineEvent("Instruction with one argument", "1");
        processor.execute(new MemoryFeed(e));
        assertEquals("oneArgument", lib1.getCalledMethodName());
        assertArrayEquals(new Object[]{1}, lib1.getArguments());
        assertFalse(lib1.isDoValidate());
        assertTrue(lib1.isDoBeforeExecution());
        assertTrue(lib1.isDoAfterExecution());
        assertFalse(lib1.isDoWhenExceptionThrown());
    }

    @Test
    public void execute_InstructionWithArgument() {
        ExecuteInstructionLineEvent e = new ExecuteInstructionLineEvent("Instruction with two arguments", "argument");
        e.setExpectResultType(Integer.class);
        processor.execute(new MemoryFeed(e));
        assertEquals(2, e.getResult());
        assertEquals("twoArguments", lib1.getCalledMethodName());
        assertArrayEquals(new Object[]{"argument", null}, lib1.getArguments());
        assertFalse(lib1.isDoValidate());
        assertTrue(lib1.isDoBeforeExecution());
        assertTrue(lib1.isDoAfterExecution());
        assertFalse(lib1.isDoWhenExceptionThrown());
    }

    @Test
    public void execute_InstructionWithVarArgument1() {
        ExecuteInstructionLineEvent e = new ExecuteInstructionLineEvent("Instruction with variable arguments", "arg1", "arg2", "arg3");
        processor.execute(new MemoryFeed(e));
        assertEquals("variableArguments", lib1.getCalledMethodName());
        assertArrayEquals(new Object[]{"arg1", new Object[]{"arg2", "arg3"}}, lib1.getArguments());
        assertFalse(lib1.isDoValidate());
        assertTrue(lib1.isDoBeforeExecution());
        assertTrue(lib1.isDoAfterExecution());
        assertFalse(lib1.isDoWhenExceptionThrown());
    }

    @Test
    public void execute_InstructionWithVarArgument2() {
        ExecuteInstructionLineEvent e = new ExecuteInstructionLineEvent("Instruction with variable arguments");
        processor.execute(new MemoryFeed(e));
        assertEquals("variableArguments", lib1.getCalledMethodName());
        assertArrayEquals(new Object[]{null, new Object[0]}, lib1.getArguments());
        assertFalse(lib1.isDoValidate());
        assertTrue(lib1.isDoBeforeExecution());
        assertTrue(lib1.isDoAfterExecution());
        assertFalse(lib1.isDoWhenExceptionThrown());
    }

    @Test
    public void execute_InstructionWithNullArray_CallingInstructionWithVariableArgument() {
        ExecuteInstructionLineEvent e = new ExecuteInstructionLineEvent("Instruction with array calling instruction with variable argument");
        processor.execute(new MemoryFeed(e));
        assertEquals("array", lib1.getCalledMethodName());
        assertArrayEquals(new Object[]{null, null}, lib1.getArguments());
        assertFalse(lib1.isDoValidate());
        assertTrue(lib1.isDoBeforeExecution());
        assertTrue(lib1.isDoAfterExecution());
        assertFalse(lib1.isDoWhenExceptionThrown());
    }

    @Test(expected = MethodNotInCallerException.class)
    public void execute_InstructionWithWrongCaller() {
        processor.execute(new MemoryFeed(new ExecuteInstructionLineEvent("Wrong caller", "arg1")));
    }

    @Test
    public void execute_InstructionWithCaller() {
        processor.execute(new MemoryFeed(new ExecuteInstructionLineEvent("Caller", "arg1")));
        assertTrue(callerLib.isCalled());
        assertArrayEquals(new Object[]{"arg1", null}, callerLib.getArguments());
        assertFalse(lib1.isDoValidate());
        assertFalse(lib1.isDoBeforeExecution());
        assertFalse(lib1.isDoAfterExecution());
        assertFalse(lib1.isDoWhenExceptionThrown());
    }
}
