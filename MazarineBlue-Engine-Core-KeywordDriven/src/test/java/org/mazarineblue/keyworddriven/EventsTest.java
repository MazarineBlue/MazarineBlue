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

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.mazarineblue.eventbus.Event;
import static org.mazarineblue.eventbus.Event.Status.OK;
import static org.mazarineblue.eventbus.Event.Status.WARNING;
import org.mazarineblue.keyworddriven.events.AddLibraryEvent;
import org.mazarineblue.keyworddriven.events.CommentInstructionLineEvent;
import org.mazarineblue.keyworddriven.events.CountLibrariesEvent;
import org.mazarineblue.keyworddriven.events.ExecuteInstructionLineEvent;
import org.mazarineblue.keyworddriven.events.RemoveLibraryEvent;
import org.mazarineblue.keyworddriven.events.ValidateInstructionLineEvent;
import org.mazarineblue.keyworddriven.util.DummyLibrary;
import org.mazarineblue.keyworddriven.util.TestLibraryWithInstructions;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class EventsTest {

    /* ********************************************************************** *
     *                            Library events                              *
     * ********************************************************* ,,^..^,, *** */
    @Test
    public void testAddLibraryEvent() {
        Library lib = new DummyLibrary("Namespace");
        Event e = new AddLibraryEvent(lib);

        assertEquals("namespace []", e.toString());
        assertEquals("library=namespace []", e.message());
        assertEquals("", e.responce());
    }

    @Test
    public void testAddLibraryEvent_WithRegisteredInstructions() {
        Library lib = new TestLibraryWithInstructions("Namespace");
        Event e = new AddLibraryEvent(lib);

        assertEquals("namespace [abc, keyword, test]", e.toString());
        assertEquals("library=namespace [abc, keyword, test]", e.message());
        assertEquals("", e.responce());
    }

    @Test
    public void testCountLibrariesEvent_NoLibrariesFound() {
        Event e = new CountLibrariesEvent(lib -> lib.getNamespace().equals("count"));
        assertEquals("count=0", e.toString());
        assertEquals("", e.message());
        assertEquals("count=0", e.responce());
    }

    @Test
    public void testCountLibrariesEvent_TwoLibrariesFound() {
        List<Library> libraries = new ArrayList<>();
        libraries.add(new DummyLibrary("foo"));
        libraries.add(new DummyLibrary("oof"));
        CountLibrariesEvent e = new CountLibrariesEvent(lib -> lib.getNamespace().equals("foo"));
        e.addToCount(libraries);

        assertEquals("count=1", e.toString());
        assertEquals("", e.message());
        assertEquals("count=1", e.responce());
    }

    @Test
    public void testRemoveLibraryEvent_WithKeyword() {
        DummyLibrary lib = new DummyLibrary("Namespace");
        Event e = new RemoveLibraryEvent(lib);

        assertEquals("namespace []", e.toString());
        assertEquals("library=namespace []", e.message());
        assertEquals("", e.responce());
    }

    @Test
    public void testRemoveLibraryEvent_WithRegisteredInstructions() {
        Event e = new RemoveLibraryEvent(new TestLibraryWithInstructions("Namespace"));

        assertEquals("namespace [abc, keyword, test]", e.toString());
        assertEquals("library=namespace [abc, keyword, test]", e.message());
        assertEquals("", e.responce());
    }

    /* ********************************************************************** *
     *                          Instruction events                            *
     * ********************************************************* ,,^..^,, *** */
    @Test
    public void testCommentInstructionLineEvent_WithKeyword() {
        Event e = new CommentInstructionLineEvent();
        assertEquals("", e.toString());
        assertEquals("comment={}", e.message());
        assertEquals("", e.responce());
    }

    @Test
    public void testCommentInstructionLineEvent_WithKeywordAndArguments() {
        Event e = new CommentInstructionLineEvent("argument 1", "argument 2");
        assertEquals("argument 1, argument 2", e.toString());
        assertEquals("comment={argument 1, argument 2}", e.message());
        assertEquals("", e.responce());
    }

    @Test
    public void testExecuteInstructionLineEvent_WithKeyword() {
        Event e = new ExecuteInstructionLineEvent("Namespace.Keyword");
        assertEquals("line={namespace.keyword}, result={}", e.toString());
        assertEquals("line={namespace.keyword}", e.message());
        assertEquals("", e.responce());
    }

    @Test
    public void testExecuteInstructionLineEvent_WithKeywordAndArguments() {
        Event e = new ExecuteInstructionLineEvent("Namespace.Keyword", "Argument 1", "argument 2");
        assertEquals("line={namespace.keyword, Argument 1, argument 2}, result={}", e.toString());
        assertEquals("line={namespace.keyword, Argument 1, argument 2}", e.message());
        assertEquals("", e.responce());
    }

    @Test
    public void testExecuteInstructionLineEvent_WithResult() {
        ExecuteInstructionLineEvent e = new ExecuteInstructionLineEvent("Namespace.Keyword");
        e.setResult("result");
        assertEquals("line={namespace.keyword}, result={result}", e.toString());
        assertEquals("line={namespace.keyword}", e.message());
        assertEquals("result={result}", e.responce());
    }

    @Test
    public void testValidateInstructionLineEvent_WithKeyword() {
        Event e = new ValidateInstructionLineEvent("Namespace.Keyword");
        assertEquals("line={namespace.keyword}, flags={ok}, customFlags=0", e.toString());
        assertEquals("line={namespace.keyword}", e.message());
        assertEquals("flags={ok}, customFlags=0", e.responce());
        assertEquals(OK, e.status());
    }

    @Test
    public void testValidateInstructionLineEvent_WithKeywordAndArguments() {
        Event e = new ValidateInstructionLineEvent("Namespace.Keyword", "Argument 1", "argument 2");
        assertEquals("line={namespace.keyword, Argument 1, argument 2}, flags={ok}, customFlags=0", e.toString());
        assertEquals("line={namespace.keyword, Argument 1, argument 2}", e.message());
        assertEquals("flags={ok}, customFlags=0", e.responce());
        assertEquals(OK, e.status());
    }

    @Test
    public void testValidateInstructionLineEvent_InstructionNotFound() {
        ValidateInstructionLineEvent e = new ValidateInstructionLineEvent("Namespace.Keyword");
        e.setInstructionsNotFound();
        assertEquals("line={namespace.keyword}, flags={instruction not found}, customFlags=0", e.toString());
        assertEquals("line={namespace.keyword}", e.message());
        assertEquals("flags={instruction not found}, customFlags=0", e.responce());
        assertEquals(WARNING, e.status());
    }

    @Test
    public void testValidateInstructionLineEvent_MultipleInstructionsFound() {
        ValidateInstructionLineEvent e = new ValidateInstructionLineEvent("Namespace.Keyword");
        e.setMultipleInstructionsFound();
        assertEquals("line={namespace.keyword}, flags={keyword conflict}, customFlags=0", e.toString());
        assertEquals("line={namespace.keyword}", e.message());
        assertEquals("flags={keyword conflict}, customFlags=0", e.responce());
        assertEquals(WARNING, e.status());
    }

    @Test
    public void testValidateInstructionLineEvent_ToFewArguments() {
        ValidateInstructionLineEvent e = new ValidateInstructionLineEvent("Namespace.Keyword");
        e.setTooFewArguments();
        assertEquals("line={namespace.keyword}, flags={too few arguments}, customFlags=0", e.toString());
        assertEquals("line={namespace.keyword}", e.message());
        assertEquals("flags={too few arguments}, customFlags=0", e.responce());
        assertEquals(WARNING, e.status());
    }

    @Test
    public void testValidateInstructionLineEvent_ArgumentsTypeMismatch() {
        ValidateInstructionLineEvent e = new ValidateInstructionLineEvent("Namespace.Keyword");
        e.setArgumentsAreIncompatible();
        assertEquals("line={namespace.keyword}, flags={wrong argument types}, customFlags=0", e.toString());
        assertEquals("line={namespace.keyword}", e.message());
        assertEquals("flags={wrong argument types}, customFlags=0", e.responce());
        assertEquals(WARNING, e.status());
    }

    @Test
    public void testValidateInstructionLineEvent_All() {
        ValidateInstructionLineEvent e = new ValidateInstructionLineEvent("Namespace.Keyword");
        e.setInstructionsNotFound();
        e.setMultipleInstructionsFound();
        e.setTooFewArguments();
        e.setArgumentsAreIncompatible();
        String str = "flags={instruction not found, keyword conflict, too few arguments, wrong argument types}, customFlags=0";
        assertEquals("line={namespace.keyword}, " + str, e.toString());
        assertEquals("line={namespace.keyword}", e.message());
        assertEquals(str, e.responce());
        assertEquals(WARNING, e.status());
    }
}
