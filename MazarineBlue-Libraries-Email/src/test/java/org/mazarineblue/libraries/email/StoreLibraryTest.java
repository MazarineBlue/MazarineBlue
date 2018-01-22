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
package org.mazarineblue.libraries.email;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.eventdriven.feeds.MemoryFeed;
import org.mazarineblue.executors.util.AbstractExecutorTestHelper;
import org.mazarineblue.keyworddriven.events.ExecuteInstructionLineEvent;
import org.mazarineblue.keyworddriven.exceptions.InstructionNotFoundException;
import org.mazarineblue.libraries.util.TestStore;
import org.mazarineblue.variablestore.events.GetVariableEvent;

public class StoreLibraryTest
        extends AbstractExecutorTestHelper {

    @Before
    public void setup() {
        TestStore.init();
    }

    @After
    public void teardown() {
        TestStore.clear();
    }

    @Test(expected = InstructionNotFoundException.class)
    public void openMailFolder_DontCloseFolder() {
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "test.mazarineblue.libraries.email"),
                               new ExecuteInstructionLineEvent("Connect to pop3 server"),
                               new ExecuteInstructionLineEvent("Open mail folder"),
                               new ExecuteInstructionLineEvent("Set", "var", "=Count messages"),
                               new ExecuteInstructionLineEvent("Disconnect from pop3 server"),
                               new ExecuteInstructionLineEvent("Close mail folder")));
        assertFailure();
    }

    @Test
    public void openMailFolder_Default() {
        GetVariableEvent e = new GetVariableEvent("var");
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "test.mazarineblue.libraries.email"),
                               new ExecuteInstructionLineEvent("Connect to pop3 server"),
                               new ExecuteInstructionLineEvent("Open mail folder"),
                               new ExecuteInstructionLineEvent("Set", "var", "=Count messages"),
                               new ExecuteInstructionLineEvent("Close mail folder"),
                               new ExecuteInstructionLineEvent("Disconnect from pop3 server"),
                               e));
        assertSuccess();
        assertEquals(20, e.getValue());
    }

    @Test
    public void openMailFolder_Inbox() {
        GetVariableEvent e = new GetVariableEvent("var");
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "test.mazarineblue.libraries.email"),
                               new ExecuteInstructionLineEvent("Connect to pop3 server"),
                               new ExecuteInstructionLineEvent("Open mail folder", "inbox"),
                               new ExecuteInstructionLineEvent("Set", "var", "=Count messages"),
                               new ExecuteInstructionLineEvent("Close mail folder"),
                               new ExecuteInstructionLineEvent("Disconnect from pop3 server"),
                               e));
        assertSuccess();
        assertEquals(20, e.getValue());
    }

    @Test
    public void openMailFolder_Foo() {
        TestStore.putFolder("foo", TestStore.newFolder(2));
        GetVariableEvent e = new GetVariableEvent("var");
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "test.mazarineblue.libraries.email"),
                               new ExecuteInstructionLineEvent("Connect to pop3 server"),
                               new ExecuteInstructionLineEvent("Open mail folder", "foo"),
                               new ExecuteInstructionLineEvent("Set", "var", "=Count messages"),
                               new ExecuteInstructionLineEvent("Close mail folder"),
                               new ExecuteInstructionLineEvent("Disconnect from pop3 server"),
                               e));
        assertSuccess();
        assertEquals(2, e.getValue());
    }

    @Test
    public void openMailFolder_ReconnectOtherFolderWithoutClosingFolder() {
        TestStore.putFolder("foo", TestStore.newFolder(2));
        GetVariableEvent e = new GetVariableEvent("var");
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "test.mazarineblue.libraries.email"),
                               new ExecuteInstructionLineEvent("Connect to pop3 server"),
                               new ExecuteInstructionLineEvent("Open mail folder"),
                               new ExecuteInstructionLineEvent("Open mail folder", "foo"),
                               new ExecuteInstructionLineEvent("Set", "var", "=Count messages"),
                               new ExecuteInstructionLineEvent("Close mail folder"),
                               new ExecuteInstructionLineEvent("Disconnect from pop3 server"),
                               e));
        assertSuccess();
        assertEquals(2, e.getValue());
    }

    @Test
    public void openMailFolder_OtherFolder_TooMannyMessages() {
        GetVariableEvent e = new GetVariableEvent("var");
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "test.mazarineblue.libraries.email"),
                               new ExecuteInstructionLineEvent("Connect to pop3 server"),
                               new ExecuteInstructionLineEvent("Open mail folder", "inbox", "999999"),
                               new ExecuteInstructionLineEvent("Set", "var", "=Count messages"),
                               new ExecuteInstructionLineEvent("Close mail folder"),
                               new ExecuteInstructionLineEvent("Disconnect from pop3 server"),
                               e));
        assertSuccess();
        assertEquals(20, e.getValue());
    }

    @Test
    public void openMailFolder_OtherFolder_TooFewMessages() {
        GetVariableEvent e = new GetVariableEvent("var");
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "test.mazarineblue.libraries.email"),
                               new ExecuteInstructionLineEvent("Connect to pop3 server"),
                               new ExecuteInstructionLineEvent("Open mail folder", "inbox", "-999999"),
                               new ExecuteInstructionLineEvent("Set", "var", "=Count messages"),
                               new ExecuteInstructionLineEvent("Close mail folder"),
                               new ExecuteInstructionLineEvent("Disconnect from pop3 server"),
                               e));
        assertSuccess();
        assertEquals(20, e.getValue());
    }

    @Test
    public void openMailFolder_OtherFolder_FirstFiveMessages() {
        GetVariableEvent e = new GetVariableEvent("var");
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "test.mazarineblue.libraries.email"),
                               new ExecuteInstructionLineEvent("Connect to pop3 server"),
                               new ExecuteInstructionLineEvent("Open mail folder", "inbox", "5"),
                               new ExecuteInstructionLineEvent("Set", "var", "=Count messages"),
                               new ExecuteInstructionLineEvent("Close mail folder"),
                               new ExecuteInstructionLineEvent("Disconnect from pop3 server"),
                               e));
        assertSuccess();
        assertEquals(5, e.getValue());
    }

    @Test
    public void openMailFolder_OtherFolder_LastFiveMessages() {
        GetVariableEvent e = new GetVariableEvent("var");
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "test.mazarineblue.libraries.email"),
                               new ExecuteInstructionLineEvent("Connect to pop3 server"),
                               new ExecuteInstructionLineEvent("Open mail folder", "inbox", "-5"),
                               new ExecuteInstructionLineEvent("Set", "var", "=Count messages"),
                               new ExecuteInstructionLineEvent("Close mail folder"),
                               new ExecuteInstructionLineEvent("Disconnect from pop3 server"),
                               e));
        assertSuccess();
        assertEquals(5, e.getValue());
    }
}
