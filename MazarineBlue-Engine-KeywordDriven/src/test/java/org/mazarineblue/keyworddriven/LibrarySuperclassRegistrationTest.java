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
package org.mazarineblue.keyworddriven;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.eventdriven.Processor;
import org.mazarineblue.eventdriven.feeds.MemoryFeed;
import org.mazarineblue.keyworddriven.events.AddLibraryEvent;
import org.mazarineblue.keyworddriven.events.CountLibrariesEvent;
import org.mazarineblue.keyworddriven.events.ExecuteInstructionLineEvent;
import org.mazarineblue.keyworddriven.util.libraries.AddsLibrary;
import org.mazarineblue.keyworddriven.util.libraries.TestChildLibrary;
import org.mazarineblue.keyworddriven.util.libraries.TestLibrary1;
import org.mazarineblue.keyworddriven.util.libraries.TestParentLibrary;

public class LibrarySuperclassRegistrationTest {

    private Processor processor;
    private TestChildLibrary lib;

    @Before
    public void setup() {
        lib = new TestChildLibrary();
        processor = Processor.newInstance();
        processor.addLink(new LibraryRegistry());
        processor.execute(new MemoryFeed(new AddLibraryEvent(lib)));
    }

    @After
    public void teardown() {
        processor = null;
        lib = null;
    }

    @Test
    public void test_CallParent()
            throws Exception {
        processor.execute(new MemoryFeed(new ExecuteInstructionLineEvent("Parent call")));
        processor.close();
        assertEquals(true, lib.isDoParent());
        assertEquals(false, lib.isDoChild());
    }

    @Test
    public void test_CallChild()
            throws Exception {
        processor.execute(new MemoryFeed(new ExecuteInstructionLineEvent("Child call")));
        processor.close();
        assertEquals(false, lib.isDoParent());
        assertEquals(true, lib.isDoChild());
    }
}
