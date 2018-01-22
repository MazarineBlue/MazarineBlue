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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.eventdriven.ClosingProcessorEvent;
import org.mazarineblue.eventdriven.Processor;
import org.mazarineblue.eventdriven.feeds.MemoryFeed;
import static org.mazarineblue.keyworddriven.Library.matchesAnyNamespace;
import org.mazarineblue.keyworddriven.events.AddLibraryEvent;
import org.mazarineblue.keyworddriven.events.ContainsConversionRuleEvent;
import org.mazarineblue.keyworddriven.events.ContainsLibrariesEvent;
import org.mazarineblue.keyworddriven.events.CountLibrariesEvent;
import org.mazarineblue.keyworddriven.events.ExecuteInstructionLineEvent;
import org.mazarineblue.keyworddriven.events.RegisterConversionRuleEvent;
import org.mazarineblue.keyworddriven.events.RemoveLibraryEvent;
import org.mazarineblue.keyworddriven.exceptions.LibraryNotFoundException;
import org.mazarineblue.keyworddriven.util.Converted;
import org.mazarineblue.keyworddriven.util.Input;
import org.mazarineblue.keyworddriven.util.Output;
import org.mazarineblue.keyworddriven.util.libraries.AddsLibrary;
import org.mazarineblue.keyworddriven.util.libraries.DummyLibrary;
import org.mazarineblue.keyworddriven.util.libraries.TestLibrary1;
import org.mazarineblue.utilities.exceptions.ConversionMethodAlreadyRegisteredException;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class LibraryRegistrationTest {

    private Processor processor;

    @Before
    public void setup() {
        DummyLibrary fooLib = new DummyLibrary("foo");
        DummyLibrary barLib = new DummyLibrary("bar");
        processor = Processor.newInstance();
        processor.addLink(new LibraryRegistry());
        processor.execute(new MemoryFeed(new AddLibraryEvent(fooLib),
                                         new AddLibraryEvent(barLib)));
    }

    @After
    public void teardown() {
        processor = null;
    }

    @Test
    public void execute_ClosingProcessorEvent() {
        ClosingProcessorEvent e = new ClosingProcessorEvent();
        TestLibrary1 lib = new TestLibrary1();
        processor.execute(new MemoryFeed(new AddLibraryEvent(lib),
                                         e));
        assertTrue(lib.isDoSetup());
        assertTrue(lib.isDoTeardown());
    }

    @Test
    public void execute_AddLibraryEvent_LibraryAddsLibrary()
            throws Exception {
        CountLibrariesEvent e = new CountLibrariesEvent();
        processor.execute(new MemoryFeed(new AddLibraryEvent(new AddsLibrary(new TestLibrary1())),
                                         new ExecuteInstructionLineEvent("Instruction with no arguments"),
                                         e));
        processor.close();
        assertEquals(4, e.getCount());
    }

    @Test
    public void execute_AddLibraryEvent() {
        CountLibrariesEvent e = new CountLibrariesEvent();
        TestLibrary1 lib = new TestLibrary1();
        processor.execute(new MemoryFeed(new AddLibraryEvent(lib),
                                         new ExecuteInstructionLineEvent("Instruction with no arguments"),
                                         e));
        assertTrue(lib.isDoSetup());
        assertEquals(3, e.getCount());
    }

    @Test(expected = LibraryNotFoundException.class)
    public void execute_RemoveLibraryEvent_LibraryUnavailable() {
        processor.execute(new MemoryFeed(new RemoveLibraryEvent(new TestLibrary1())));
    }

    @Test
    public void execute_RemoveLibraryEvent_LibraryAvailable() {
        CountLibrariesEvent e = new CountLibrariesEvent();
        TestLibrary1 lib = new TestLibrary1();
        processor.execute(new MemoryFeed(new AddLibraryEvent(lib),
                                         new RemoveLibraryEvent(lib),
                                         e));
        assertTrue(lib.isDoSetup());
        assertTrue(lib.isDoTeardown());
        assertEquals(2, e.getCount());
    }

    @Test
    public void execute_ContainsLibrariesEvent_NoneLibrary() {
        ContainsLibrariesEvent e = new ContainsLibrariesEvent(lib -> false);
        processor.execute(new MemoryFeed(e));
        assertFalse(e.found());
    }

    @Test
    public void execute_ContainsLibrariesEvent_OneLibrary() {
        ContainsLibrariesEvent e = new ContainsLibrariesEvent(matchesAnyNamespace("foo"));
        processor.execute(new MemoryFeed(e));
        assertTrue(e.found());
    }

    @Test
    public void execute_ContainsLibrariesEvent_AllLibrary() {
        ContainsLibrariesEvent e = new ContainsLibrariesEvent();
        processor.execute(new MemoryFeed(e));
        assertTrue(e.found());
    }

    @Test
    public void execute_CountLibrariesEvent_NoneLibrary() {
        CountLibrariesEvent e = new CountLibrariesEvent(lib -> false);
        processor.execute(new MemoryFeed(e));
        assertEquals(0, e.getCount());
    }

    @Test
    public void execute_CountLibrariesEvent_OneLibrary() {
        CountLibrariesEvent e = new CountLibrariesEvent(matchesAnyNamespace("foo"));
        processor.execute(new MemoryFeed(e));
        assertEquals(1, e.getCount());
    }

    @Test
    public void execute_CountLibrariesEvent_AllLibrary() {
        CountLibrariesEvent e = new CountLibrariesEvent();
        processor.execute(new MemoryFeed(e));
        assertEquals(2, e.getCount());
    }

    @Test
    public void execute_RegisterConversionRuleEvent() {
        ExecuteInstructionLineEvent e = new ExecuteInstructionLineEvent("Conversion", new Input());
        processor.execute(new MemoryFeed(
                new AddLibraryEvent(new TestLibrary1()),
                new RegisterConversionRuleEvent<>(Input.class, Converted.class, t -> new Converted()),
                e));
        assertTrue(e.getResult().getClass().isAssignableFrom(Output.class));
    }

    @Test(expected = ConversionMethodAlreadyRegisteredException.class)
    public void execute_RegisterConversionRuleEvent_Twice() {
        ExecuteInstructionLineEvent e = new ExecuteInstructionLineEvent("Conversion", new Input());
        processor.execute(new MemoryFeed(
                new AddLibraryEvent(new TestLibrary1()),
                new RegisterConversionRuleEvent<>(Input.class, Converted.class, t -> new Converted()),
                new RegisterConversionRuleEvent<>(Input.class, Converted.class, t -> new Converted()),
                e));
        assertTrue(e.getResult().getClass().isAssignableFrom(Output.class));
    }

    @Test
    public void execute_ContainsConversionRuleEvent_NotRegistered() {
        ContainsConversionRuleEvent<Input, Converted> e = new ContainsConversionRuleEvent<>(Input.class, Converted.class);
        processor.execute(new MemoryFeed(e));
        assertFalse(e.isConversionMethodAvailable());
    }

    @Test
    public void execute_ContainsConversionRuleEvent_Registered() {
        ContainsConversionRuleEvent<Input, Converted> e = new ContainsConversionRuleEvent<>(Input.class, Converted.class);
        processor.execute(new MemoryFeed(
                new RegisterConversionRuleEvent<>(Input.class, Converted.class, t -> new Converted()),
                e));
        assertTrue(e.isConversionMethodAvailable());
    }
}
