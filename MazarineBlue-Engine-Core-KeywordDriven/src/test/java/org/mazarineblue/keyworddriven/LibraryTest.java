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
package org.mazarineblue.keyworddriven;

import java.lang.reflect.Method;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import org.junit.Test;
import org.mazarineblue.eventdriven.events.ClosingInterpreterEvent;
import org.mazarineblue.keyworddriven.events.ExecuteInstructionLineEvent;
import org.mazarineblue.keyworddriven.events.ValidateInstructionLineEvent;
import org.mazarineblue.keyworddriven.exceptions.DeclaringMethodClassNotEqualToCalleeException;
import org.mazarineblue.keyworddriven.util.DummyLibrary;
import org.mazarineblue.keyworddriven.util.LibrarySpy;
import org.mazarineblue.keyworddriven.util.TestFilledLibrarySpy;
import org.mazarineblue.keyworddriven.util.TestLibraryExternalCaller;
import org.mazarineblue.utililities.util.TestHashCodeAndEquals;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class LibraryTest
        extends TestHashCodeAndEquals<Library> {

    private static final String NAMESPACE = "namespace";
    private static final String KEYWORD = TestFilledLibrarySpy.ZERO_ARGUMENTS_INSTRUCTION;
    private static final String DELIMITER = ".";
    private static final String NAMESPACE_KEYWORD = NAMESPACE + DELIMITER + KEYWORD;

    @Test
    public void setup() {
        LibrarySpy library = new LibrarySpy(NAMESPACE);
        library.doSetup(null);
        assertEquals(1, library.countSetup());
    }

    @Test
    public void teardown() {
        LibrarySpy library = new LibrarySpy(NAMESPACE);
        library.teardown(new ClosingInterpreterEvent());
        assertEquals(1, library.countTeardown());
    }

    @Test(expected = DeclaringMethodClassNotEqualToCalleeException.class)
    public void registerInstruction_CalleeInstructionMismatch_ThrowsInstructionNoMemberOfCalleeException() {
        LibrarySpy spy = new LibrarySpy(NAMESPACE);
        Library library = new TestLibraryExternalCaller(NAMESPACE, spy);
        library.registerInstruction(KEYWORD, getClass().getMethods()[0], 0);
    }

    @Test
    public void registerInstruction_CalleeInstructionMatch_ValidationCalled() {
        LibrarySpy spy = new LibrarySpy(NAMESPACE);
        TestCallee callee = new TestCallee();
        TestLibraryExternalCaller library = new TestLibraryExternalCaller(NAMESPACE, spy, callee);
        library.registerInstruction(KEYWORD, callee.getThisMethod(), 0);
        LibraryRegistry registry = new LibraryRegistry(library);
        registry.validateInstruction(new ValidateInstructionLineEvent(NAMESPACE_KEYWORD));
        assertEquals(1, spy.countValidations());
    }

    @Test
    public void registerInstruction_CalleeInstructionMatch_BeforeExecutionCalled() {
        LibrarySpy spy = new LibrarySpy(NAMESPACE);
        TestCallee callee = new TestCallee();
        TestLibraryExternalCaller library = new TestLibraryExternalCaller(NAMESPACE, spy, callee);
        library.registerInstruction(KEYWORD, callee.getThisMethod(), 0);
        LibraryRegistry registry = new LibraryRegistry(library);
        registry.executeInstruction(new ExecuteInstructionLineEvent(NAMESPACE_KEYWORD));
        assertEquals(1, spy.countBeforeExecutions());
    }

    @Test
    public void registerInstruction_CalleeInstructionMatch_AfterExecutionCalled() {
        LibrarySpy spy = new LibrarySpy(NAMESPACE);
        TestCallee callee = new TestCallee();
        TestLibraryExternalCaller library = new TestLibraryExternalCaller(NAMESPACE, spy, callee);
        library.registerInstruction(KEYWORD, callee.getThisMethod(), 0);
        LibraryRegistry registry = new LibraryRegistry(library);
        registry.executeInstruction(new ExecuteInstructionLineEvent(NAMESPACE_KEYWORD));
        assertEquals(1, spy.countAfterExecutions());
    }

    @Test
    public void registerInstruction_CalleeInstructionMatch_CalleeCalled() {
        LibrarySpy spy = new LibrarySpy(NAMESPACE);
        TestCallee callee = new TestCallee();
        TestLibraryExternalCaller library = new TestLibraryExternalCaller(NAMESPACE, spy, callee);
        library.registerInstruction(KEYWORD, callee.getThisMethod(), 0);
        LibraryRegistry registry = new LibraryRegistry(library);
        registry.executeInstruction(new ExecuteInstructionLineEvent(NAMESPACE_KEYWORD));
        assertEquals(2, callee.count);
    }

    @SuppressWarnings("PublicInnerClass")
    public class TestCallee {

        private int count;

        public Method getThisMethod() {
            ++count;
            for (Method method : getClass().getMethods())
                if (!method.getDeclaringClass().equals(Object.class))
                    return method;
            return null;
        }
    }

    @Test
    public void hashCode_DifferentNamespace() {
        Library a = new DummyLibrary("foo");
        Library b = new DummyLibrary("oof");
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void equals_DifferentNamespace() {
        Library a = new DummyLibrary("foo");
        Library b = new DummyLibrary("oof");
        assertFalse(a.equals(b));
    }

    @Override
    protected Library getObject() {
        return new TestFilledLibrarySpy("foo");
    }

    @Override
    protected Library getDifferentObject() {
        return new DummyLibrary("foo");
    }
}
