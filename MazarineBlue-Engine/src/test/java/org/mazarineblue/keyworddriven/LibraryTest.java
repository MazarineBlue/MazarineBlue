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
import org.mazarineblue.eventbus.link.EventBusLink;
import org.mazarineblue.keyworddriven.events.ExecuteInstructionLineEvent;
import org.mazarineblue.keyworddriven.events.FetchLibrariesEvent;
import org.mazarineblue.keyworddriven.events.KeywordDrivenEvent;
import org.mazarineblue.keyworddriven.exceptions.DeclaringMethodClassNotEqualToCalleeException;
import org.mazarineblue.keyworddriven.exceptions.InstructionInaccessibleException;
import org.mazarineblue.keyworddriven.exceptions.LibraryInaccessibleException;
import org.mazarineblue.keyworddriven.util.TestFilledLibrarySpy;
import org.mazarineblue.keyworddriven.util.TestLibrary;
import org.mazarineblue.keyworddriven.util.TestLibraryExternalCaller;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class LibraryTest {

    private static final String NAMESPACE = "namespace";
    private static final String KEYWORD = TestFilledLibrarySpy.ZERO_ARGUMENTS_INSTRUCTION;
    private static final String DELIMITER = ".";
    private static final String NAMESPACE_KEYWORD = NAMESPACE + DELIMITER + KEYWORD;

    @Test(expected = LibraryInaccessibleException.class)
    public void execute_PrivateLibrary_ThrowsExeption() {
        EventBusLink link = new EventBusLink();
        link.subscribe(KeywordDrivenEvent.class, null, new LibraryRegistry(new PrivateLibrary()));
    }

    private static class PrivateLibrary
            extends Library {

        PrivateLibrary() {
            super("");
        }

        @Keyword("foo")
        public void foo() {
        }
    }

    @Test(expected = DeclaringMethodClassNotEqualToCalleeException.class)
    public void registerInstruction_CalleeInstructionMismatch_ThrowsInstructionNoMemberOfCalleeException() {
        Library library = new TestLibraryExternalCaller(NAMESPACE);
        library.registerInstruction(KEYWORD, getClass().getMethods()[0], 0);
    }

    @Test
    public void registerInstruction_CalleeInstructionMatch_Passes() {
        TestCallee callee = new TestCallee();
        TestLibraryExternalCaller library = new TestLibraryExternalCaller(NAMESPACE, callee);
        library.registerInstruction(KEYWORD, callee.getThisMethod(), 0);
        LibraryRegistry registry = new LibraryRegistry(library);
        registry.executeInstruction(new ExecuteInstructionLineEvent(NAMESPACE_KEYWORD));
        assertEquals(2, callee.count);
    }

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
    public void fetchLibraries_False_ReturnsNoLibraries() {
        Library library = new TestLibraryExternalCaller(NAMESPACE);
        FetchLibrariesEvent event = new FetchLibrariesEvent(lib -> false);
        library.fetchLibraries(event);
        assertEquals(0, event.getLibraries().size());
    }

    @Test
    public void fetchLibraries_True_ReturnsThatLibrary() {
        Library library = new TestFilledLibrarySpy(NAMESPACE);
        FetchLibrariesEvent event = new FetchLibrariesEvent(lib -> true);
        library.fetchLibraries(event);
        assertEquals("[namespace [One, Zero, invokerOne]]", event.getLibraries().toString());
    }

    @Test(expected = InstructionInaccessibleException.class)
    public void executeInstruction_CallerInstructionMismatch_InstructionInaccessibleExceptionThrown() {
        Library library = new TestLibraryExternalCaller(NAMESPACE, new Object()) {
            @Keyword(KEYWORD)
            public void test() {
            }
        };
        LibraryRegistry registry = new LibraryRegistry(library);
        registry.executeInstruction(new ExecuteInstructionLineEvent(NAMESPACE_KEYWORD));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void equals_Null() {
        Library a = new TestLibrary("foo");
        assertFalse(a.equals(null));
    }

    @Test
    @SuppressWarnings("IncompatibleEquals")
    public void equals_DifferentClass() {
        Library a = new TestLibrary("foo");
        assertFalse(a.equals(""));
    }

    @Test
    public void hashCode_DifferentNamespace() {
        Library a = new TestLibrary("foo");
        Library b = new TestLibrary("oof");
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void equals_DifferentNamespace() {
        Library a = new TestLibrary("foo");
        Library b = new TestLibrary("oof");
        assertNotEquals(a, b);
    }

    @Test
    public void hashCode_DifferentKeywords() {
        Library a = new TestLibrary("foo");
        Library b = new TestFilledLibrarySpy("foo");
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void equals_DifferentKeywords() {
        Library a = new TestLibrary("foo");
        Library b = new TestFilledLibrarySpy("foo");
        assertNotEquals(a, b);
    }

    @Test
    public void hashCode_IdenticalContent() {
        Library a = new TestFilledLibrarySpy("foo");
        Library b = new TestFilledLibrarySpy("foo");
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void equals_IdenticalContent() {
        Library a = new TestFilledLibrarySpy("foo");
        Library b = new TestFilledLibrarySpy("foo");
        assertEquals(a, b);
    }
}
