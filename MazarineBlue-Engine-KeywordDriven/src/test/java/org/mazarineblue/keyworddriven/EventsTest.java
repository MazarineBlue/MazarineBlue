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

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.mazarineblue.eventnotifier.Event;
import static org.mazarineblue.eventnotifier.Event.Status.ERROR;
import static org.mazarineblue.eventnotifier.Event.Status.OK;
import static org.mazarineblue.eventnotifier.Event.Status.WARNING;
import org.mazarineblue.keyworddriven.events.ContainsConversionRuleEvent;
import org.mazarineblue.keyworddriven.events.ContainsLibrariesEvent;
import org.mazarineblue.keyworddriven.events.CountLibrariesEvent;
import org.mazarineblue.keyworddriven.events.ExecuteInstructionLineEvent;
import org.mazarineblue.keyworddriven.events.FetchLibrariesEvent;
import org.mazarineblue.keyworddriven.events.RegisterConversionRuleEvent;
import org.mazarineblue.keyworddriven.events.ValidateInstructionLineEvent;
import org.mazarineblue.keyworddriven.util.events.TestExecuteInstructionLineEvent;
import org.mazarineblue.keyworddriven.util.libraries.TestLibrary1;
import org.mazarineblue.keyworddriven.util.libraries.TestLibrary2;
import org.mazarineblue.keyworddriven.util.libraries.TestLibrary3;

public class EventsTest {

    /* ********************************************************************** *
     *                            Library events                              *
     * ********************************************************* ,,^..^,, *** */
    @Test
    public void test_ContainsLibrariesEvent_LibrariesNotFound() {
        ContainsLibrariesEvent e = new ContainsLibrariesEvent();
        assertEquals("found=false", e.toString());
        assertEquals("", e.message());
        assertEquals("found=false", e.responce());
    }

    @Test
    public void test_ContainsLibrariesEvent_LibrariesFound() {
        ContainsLibrariesEvent e = new ContainsLibrariesEvent();
        e.contains(asList(new TestLibrary1(), new TestLibrary2(), new TestLibrary3()));
        assertEquals("found=true", e.toString());
        assertEquals("", e.message());
        assertEquals("found=true", e.responce());
    }

    @Test
    public void test_CountLibrariesEvent_LibrariesNotFound() {
        CountLibrariesEvent e = new CountLibrariesEvent();
        assertEquals("count=0", e.toString());
        assertEquals("", e.message());
        assertEquals("count=0", e.responce());
    }

    @Test
    public void test_CountLibrariesEvent_LibrariesFound() {
        CountLibrariesEvent e = new CountLibrariesEvent();
        e.addToCount(asList(new TestLibrary1(), new TestLibrary2(), new TestLibrary3()));
        assertEquals("count=3", e.toString());
        assertEquals("", e.message());
        assertEquals("count=3", e.responce());
    }

    @Test
    public void test_FetchLibrariesEvent_LibrariesFound() {
        FetchLibrariesEvent e = new FetchLibrariesEvent();
        e.addLibrary(new TestLibrary1(), new TestLibrary2(), new TestLibrary3());
        assertEquals("found=3", e.toString());
        assertEquals("", e.message());
        assertEquals("found=3", e.responce());
        assertEquals(asList(new TestLibrary1(), new TestLibrary2(), new TestLibrary3()), e.getLibraries());
    }

    /* ********************************************************************** *
     *                          Instruction events                            *
     * ********************************************************* ,,^..^,, *** */
    @Test
    public void test_ExecuteInstructionLineEvent() {
        Event e = new ExecuteInstructionLineEvent("namespace.keyword", "argument");
        assertEquals("line={namespace.keyword, argument}, result={}, error={}", e.toString());
        assertEquals("line={namespace.keyword, argument}", e.message());
        assertEquals("", e.responce());
        assertEquals(OK, e.status());
    }

    @Test
    public void test_ExecuteInstructionLineEvent_WithException() {
        TestExecuteInstructionLineEvent e = new TestExecuteInstructionLineEvent("namespace.keyword", "argument");
        e.setException(new RuntimeException("message"));
        assertEquals("line={namespace.keyword, argument}, result={}, error={message}", e.toString());
        assertEquals("line={namespace.keyword, argument}", e.message());
        assertEquals("error={message}", e.responce());
        assertEquals(ERROR, e.status());
    }

    @Test
    public void test_ExecuteInstructionLineEvent_WithResult() {
        TestExecuteInstructionLineEvent e = new TestExecuteInstructionLineEvent("namespace.keyword", "argument");
        e.setResult("message");
        assertEquals("line={namespace.keyword, argument}, result={message}, error={}", e.toString());
        assertEquals("line={namespace.keyword, argument}", e.message());
        assertEquals("result={message}", e.responce());
        assertEquals(OK, e.status());
    }

    @Test
    public void test_ValidateInstructionLineEvent() {
        ValidateInstructionLineEvent e = new ValidateInstructionLineEvent("namespace.keyword", "argument");
        assertEquals("line={namespace.keyword, argument}, flags={ok}", e.toString());
        assertEquals("line={namespace.keyword, argument}", e.message());
        assertEquals("flags={ok}", e.responce());
        assertEquals(OK, e.status());
    }

    @Test
    public void test_ValidateInstructionLineEvent_ArgumentsAreIncompatible() {
        ValidateInstructionLineEvent e = new ValidateInstructionLineEvent("namespace.keyword", "argument");
        e.setArgumentsAreIncompatible();
        assertEquals("line={namespace.keyword, argument}, flags={wrong argument types}", e.toString());
        assertEquals("line={namespace.keyword, argument}", e.message());
        assertEquals("flags={wrong argument types}", e.responce());
        assertEquals(WARNING, e.status());
    }

    @Test
    public void test_ValidateInstructionLineEvent_InstructionNotFound() {
        ValidateInstructionLineEvent e = new ValidateInstructionLineEvent("namespace.keyword", "argument");
        e.setInstructionsNotFound();
        assertEquals("line={namespace.keyword, argument}, flags={instruction not found}", e.toString());
        assertEquals("line={namespace.keyword, argument}", e.message());
        assertEquals("flags={instruction not found}", e.responce());
        assertEquals(WARNING, e.status());
    }

    @Test
    public void test_ValidateInstructionLineEvent_MultipleInstructionsFound() {
        ValidateInstructionLineEvent e = new ValidateInstructionLineEvent("namespace.keyword", "argument");
        e.setMultipleInstructionsFound();
        assertEquals("line={namespace.keyword, argument}, flags={keyword conflict}", e.toString());
        assertEquals("line={namespace.keyword, argument}", e.message());
        assertEquals("flags={keyword conflict}", e.responce());
        assertEquals(WARNING, e.status());
    }

    @Test
    public void test_ValidateInstructionLineEvent_TooFewArguments() {
        ValidateInstructionLineEvent e = new ValidateInstructionLineEvent("namespace.keyword", "argument");
        e.setTooFewArguments();
        assertEquals("line={namespace.keyword, argument}, flags={too few arguments}", e.toString());
        assertEquals("line={namespace.keyword, argument}", e.message());
        assertEquals("flags={too few arguments}", e.responce());
        assertEquals(WARNING, e.status());
    }

    @Test
    public void test_ValidateInstructionLineEvent_AllMessages() {
        ValidateInstructionLineEvent e = new ValidateInstructionLineEvent("namespace.keyword", "argument");
        e.setArgumentsAreIncompatible();
        e.setInstructionsNotFound();
        e.setMultipleInstructionsFound();
        e.setTooFewArguments();
        String str = "instruction not found, keyword conflict, too few arguments, wrong argument types";
        assertEquals(format("line={namespace.keyword, argument}, flags={%s}", str), e.toString());
        assertEquals("line={namespace.keyword, argument}", e.message());
        assertEquals(format("flags={%s}", str), e.responce());
        assertEquals(WARNING, e.status());
    }

    /* ********************************************************************** *
     *                             Other events                               *
     * ********************************************************* ,,^..^,, *** */
    @Test
    public void test_ContainsConverionRule() {
        Event e = new ContainsConversionRuleEvent(String.class, Integer.class);
        assertEquals("inputType=String, outputType=Integer", e.toString());
        assertEquals("inputType=java.lang.String, outputType=java.lang.Integer", e.message());
        assertEquals("", e.responce());
    }

    @Test
    public void test_RegisterConverionRule() {
        Event e = new RegisterConversionRuleEvent(String.class, Integer.class, null);
        assertEquals("inputType=String, outputType=Integer", e.toString());
        assertEquals("inputType=java.lang.String, outputType=java.lang.Integer", e.message());
        assertEquals("", e.responce());
    }
}
