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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.eventdriven.Processor;
import org.mazarineblue.eventdriven.feeds.MemoryFeed;
import org.mazarineblue.keyworddriven.events.ValidateInstructionLineEvent;
import org.mazarineblue.keyworddriven.util.libraries.TestCallerLibrary;
import org.mazarineblue.keyworddriven.util.libraries.TestLibrary1;
import org.mazarineblue.keyworddriven.util.libraries.TestLibrary2;

public class UsingValidationInstructionTest {

    private Processor processor;
    private TestLibrary1 lib1;

    @Before
    public void setup() {
        lib1 = new TestLibrary1();
        LibraryRegistry registry = new LibraryRegistry(lib1, new TestLibrary2(), new TestCallerLibrary());
        processor = Processor.newInstance();
        processor.addLink(registry);
    }

    @After
    public void teardown() {
        processor = null;
    }

    @Test
    public void execute_UnkownInstruction() {
        ValidateInstructionLineEvent e = new ValidateInstructionLineEvent("Unkown");
        processor.execute(new MemoryFeed(e));
        assertTrue(e.isInstructionNotFound());
        assertFalse(e.isMultipleInstructionsFound());
        assertFalse(e.isLineHasToFewArguments());
        assertFalse(e.isArgumentsAreIncompatible());
        assertFalse(e.isValid());
        assertFalse(lib1.isDoValidate());
        assertFalse(lib1.isDoBeforeExecution());
        assertFalse(lib1.isDoAfterExecution());
        assertFalse(lib1.isDoWhenExceptionThrown());
    }

    @Test
    public void execute_ConflictedInstruction() {
        ValidateInstructionLineEvent e = new ValidateInstructionLineEvent("Conflicted");
        processor.execute(new MemoryFeed(e));
        assertFalse(e.isInstructionNotFound());
        assertTrue(e.isMultipleInstructionsFound());
        assertFalse(e.isLineHasToFewArguments());
        assertFalse(e.isArgumentsAreIncompatible());
        assertFalse(e.isValid());
        assertFalse(lib1.isDoValidate());
        assertFalse(lib1.isDoBeforeExecution());
        assertFalse(lib1.isDoAfterExecution());
        assertFalse(lib1.isDoWhenExceptionThrown());
    }

    @Test
    public void execute_MinimumParametersArgument() {
        ValidateInstructionLineEvent e = new ValidateInstructionLineEvent("Minimum parameters");
        processor.execute(new MemoryFeed(e));
        assertFalse(e.isInstructionNotFound());
        assertFalse(e.isMultipleInstructionsFound());
        assertTrue(e.isLineHasToFewArguments());
        assertFalse(e.isArgumentsAreIncompatible());
        assertFalse(e.isValid());
        assertTrue(lib1.isDoValidate());
        assertFalse(lib1.isDoBeforeExecution());
        assertFalse(lib1.isDoAfterExecution());
        assertFalse(lib1.isDoWhenExceptionThrown());
    }

    @Test
    public void executeIncompatibleArgument() {
        ValidateInstructionLineEvent e = new ValidateInstructionLineEvent("Incompatible argument", "foo");
        processor.execute(new MemoryFeed(e));
        assertFalse(e.isInstructionNotFound());
        assertFalse(e.isMultipleInstructionsFound());
        assertFalse(e.isLineHasToFewArguments());
        assertTrue(e.isArgumentsAreIncompatible());
        assertFalse(e.isValid());
        assertTrue(lib1.isDoValidate());
        assertFalse(lib1.isDoBeforeExecution());
        assertFalse(lib1.isDoAfterExecution());
        assertFalse(lib1.isDoWhenExceptionThrown());
    }

    @Test
    public void execute_InstructionWithoutArgument() {
        ValidateInstructionLineEvent e = new ValidateInstructionLineEvent("Instruction with no arguments");
        processor.execute(new MemoryFeed(e));
        assertFalse(e.isInstructionNotFound());
        assertFalse(e.isMultipleInstructionsFound());
        assertFalse(e.isLineHasToFewArguments());
        assertFalse(e.isArgumentsAreIncompatible());
        assertTrue(e.isValid());
        assertTrue(lib1.isDoValidate());
        assertFalse(lib1.isDoBeforeExecution());
        assertFalse(lib1.isDoAfterExecution());
        assertFalse(lib1.isDoWhenExceptionThrown());
    }

    @Test
    public void execute_InstructionWithInvoker() {
        ValidateInstructionLineEvent e = new ValidateInstructionLineEvent("Instruction with invoker");
        processor.execute(new MemoryFeed(e));
        assertFalse(e.isInstructionNotFound());
        assertFalse(e.isMultipleInstructionsFound());
        assertFalse(e.isLineHasToFewArguments());
        assertFalse(e.isArgumentsAreIncompatible());
        assertTrue(e.isValid());
        assertTrue(lib1.isDoValidate());
        assertFalse(lib1.isDoBeforeExecution());
        assertFalse(lib1.isDoAfterExecution());
        assertFalse(lib1.isDoWhenExceptionThrown());
    }

    @Test
    public void execute_InstructionWithIntegerInStringForm() {
        ValidateInstructionLineEvent e = new ValidateInstructionLineEvent("Instruction with one argument", "1");
        processor.execute(new MemoryFeed(e));
        assertFalse(e.isInstructionNotFound());
        assertFalse(e.isMultipleInstructionsFound());
        assertFalse(e.isLineHasToFewArguments());
        assertFalse(e.isArgumentsAreIncompatible());
        assertTrue(e.isValid());
        assertTrue(lib1.isDoValidate());
        assertFalse(lib1.isDoBeforeExecution());
        assertFalse(lib1.isDoAfterExecution());
        assertFalse(lib1.isDoWhenExceptionThrown());
    }

    @Test
    public void execute_InstructionWithArgument() {
        ValidateInstructionLineEvent e = new ValidateInstructionLineEvent("Instruction with two arguments", "argument");
        processor.execute(new MemoryFeed(e));
        assertFalse(e.isInstructionNotFound());
        assertFalse(e.isMultipleInstructionsFound());
        assertFalse(e.isLineHasToFewArguments());
        assertFalse(e.isArgumentsAreIncompatible());
        assertTrue(e.isValid());
        assertTrue(lib1.isDoValidate());
        assertFalse(lib1.isDoBeforeExecution());
        assertFalse(lib1.isDoAfterExecution());
        assertFalse(lib1.isDoWhenExceptionThrown());
    }

    @Test
    public void execute_InstructionWithVarArgument1() {
        ValidateInstructionLineEvent e = new ValidateInstructionLineEvent("Instruction with variable arguments", "arg1", "arg2", "arg3");
        processor.execute(new MemoryFeed(e));
        assertFalse(e.isInstructionNotFound());
        assertFalse(e.isMultipleInstructionsFound());
        assertFalse(e.isLineHasToFewArguments());
        assertFalse(e.isArgumentsAreIncompatible());
        assertTrue(e.isValid());
        assertTrue(lib1.isDoValidate());
        assertFalse(lib1.isDoBeforeExecution());
        assertFalse(lib1.isDoAfterExecution());
        assertFalse(lib1.isDoWhenExceptionThrown());
    }

    @Test
    public void execute_InstructionWithVarArgument2() {
        ValidateInstructionLineEvent e = new ValidateInstructionLineEvent("Instruction with variable arguments");
        processor.execute(new MemoryFeed(e));
        assertFalse(e.isInstructionNotFound());
        assertFalse(e.isMultipleInstructionsFound());
        assertFalse(e.isLineHasToFewArguments());
        assertFalse(e.isArgumentsAreIncompatible());
        assertTrue(e.isValid());
        assertTrue(lib1.isDoValidate());
        assertFalse(lib1.isDoBeforeExecution());
        assertFalse(lib1.isDoAfterExecution());
        assertFalse(lib1.isDoWhenExceptionThrown());
    }

    @Test
    public void execute_InstructionWithNullArray_CallingInstructionWithVariableArgument() {
        ValidateInstructionLineEvent e = new ValidateInstructionLineEvent("Instruction with array calling instruction with variable argument");
        processor.execute(new MemoryFeed(e));
        assertFalse(e.isInstructionNotFound());
        assertFalse(e.isMultipleInstructionsFound());
        assertFalse(e.isLineHasToFewArguments());
        assertFalse(e.isArgumentsAreIncompatible());
        assertTrue(e.isValid());
        assertTrue(lib1.isDoValidate());
        assertFalse(lib1.isDoBeforeExecution());
        assertFalse(lib1.isDoAfterExecution());
        assertFalse(lib1.isDoWhenExceptionThrown());
    }
}
