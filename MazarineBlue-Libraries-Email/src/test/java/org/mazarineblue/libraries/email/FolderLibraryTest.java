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
import org.mazarineblue.libraries.email.exceptions.MessageNotFoundException;
import org.mazarineblue.libraries.util.TestStore;
import org.mazarineblue.variablestore.events.GetVariableEvent;

public class FolderLibraryTest
        extends AbstractExecutorTestHelper {

    @Before
    public void setup() {
        TestStore.init();
        TestStore.putFolder("empty", TestStore.newFolder(0));
    }

    @After
    public void teardown() {
        TestStore.clear();
    }

    @Test
    public void countMessages() {
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

    @Test(expected = MessageNotFoundException.class)
    public void openFirstMail_EmptyFolder() {
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "test.mazarineblue.libraries.email"),
                               new ExecuteInstructionLineEvent("Connect to pop3 server"),
                               new ExecuteInstructionLineEvent("Open mail folder", "empty"),
                               new ExecuteInstructionLineEvent("Open first message")));
        assertFailure();
    }

    @Test(expected = MessageNotFoundException.class)
    public void openSecondMail_EmptyFolder() {
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "test.mazarineblue.libraries.email"),
                               new ExecuteInstructionLineEvent("Connect to pop3 server"),
                               new ExecuteInstructionLineEvent("Open mail folder", "empty"),
                               new ExecuteInstructionLineEvent("Open message at index", "2")));
        assertFailure();
    }

    @Test
    public void openSecondMail_InboxFolder() {
        GetVariableEvent e = new GetVariableEvent("var");
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "test.mazarineblue.libraries.email"),
                               new ExecuteInstructionLineEvent("Connect to pop3 server"),
                               new ExecuteInstructionLineEvent("Open mail folder", "inbox"),
                               new ExecuteInstructionLineEvent("Open message at index", "1"),
                               new ExecuteInstructionLineEvent("Set", "var", "=Get message number"),
                               new ExecuteInstructionLineEvent("Close mail folder"),
                               new ExecuteInstructionLineEvent("Disconnect from pop3 server"),
                               e));
        assertSuccess();
        assertEquals(1, e.getValue());
    }

    @Test(expected = MessageNotFoundException.class)
    public void openLastMail_EmptyFolder() {
        GetVariableEvent e = new GetVariableEvent("var");
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "test.mazarineblue.libraries.email"),
                               new ExecuteInstructionLineEvent("Connect to pop3 server"),
                               new ExecuteInstructionLineEvent("Open mail folder", "empty"),
                               new ExecuteInstructionLineEvent("Open first message")));
        assertFailure();
    }

    @Test
    public void openFirstMail() {
        GetVariableEvent e = new GetVariableEvent("var");
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "test.mazarineblue.libraries.email"),
                               new ExecuteInstructionLineEvent("Connect to pop3 server"),
                               new ExecuteInstructionLineEvent("Open mail folder"),
                               new ExecuteInstructionLineEvent("Open first message"),
                               new ExecuteInstructionLineEvent("Set", "var", "=Get message number"),
                               new ExecuteInstructionLineEvent("Close message"),
                               new ExecuteInstructionLineEvent("Close mail folder"),
                               new ExecuteInstructionLineEvent("Disconnect from pop3 server"),
                               e));
        assertSuccess();
        assertEquals(0, e.getValue());
    }

    @Test(expected = InstructionNotFoundException.class)
    public void openFirstMail_DontCloseMail() {
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "test.mazarineblue.libraries.email"),
                               new ExecuteInstructionLineEvent("Connect to pop3 server"),
                               new ExecuteInstructionLineEvent("Open mail folder"),
                               new ExecuteInstructionLineEvent("Open first message"),
                               new ExecuteInstructionLineEvent("Set", "var", "=Get message number"),
                               new ExecuteInstructionLineEvent("Close mail folder"),
                               new ExecuteInstructionLineEvent("Disconnect from pop3 server"),
                               new ExecuteInstructionLineEvent("Close message")));
        assertFailure();
    }

    @Test
    public void openSecondMail() {
        GetVariableEvent e = new GetVariableEvent("var");
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "test.mazarineblue.libraries.email"),
                               new ExecuteInstructionLineEvent("Connect to pop3 server"),
                               new ExecuteInstructionLineEvent("Open mail folder"),
                               new ExecuteInstructionLineEvent("Open message at index", "1"),
                               new ExecuteInstructionLineEvent("Set", "var", "=Get message number"),
                               new ExecuteInstructionLineEvent("Close message"),
                               new ExecuteInstructionLineEvent("Close mail folder"),
                               new ExecuteInstructionLineEvent("Disconnect from pop3 server"),
                               e));
        assertSuccess();
        assertEquals(1, e.getValue());
    }

    @Test(expected = InstructionNotFoundException.class)
    public void openSecondMail_DontCloseMail() {
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "test.mazarineblue.libraries.email"),
                               new ExecuteInstructionLineEvent("Connect to pop3 server"),
                               new ExecuteInstructionLineEvent("Open mail folder"),
                               new ExecuteInstructionLineEvent("Open message at index", "1"),
                               new ExecuteInstructionLineEvent("Set", "var", "=Get message number"),
                               new ExecuteInstructionLineEvent("Close mail folder"),
                               new ExecuteInstructionLineEvent("Disconnect from pop3 server"),
                               new ExecuteInstructionLineEvent("Close message")));
        assertFailure();
    }

    @Test
    public void openLastMail() {
        GetVariableEvent e = new GetVariableEvent("var");
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "test.mazarineblue.libraries.email"),
                               new ExecuteInstructionLineEvent("Connect to pop3 server"),
                               new ExecuteInstructionLineEvent("Open mail folder"),
                               new ExecuteInstructionLineEvent("Open last message"),
                               new ExecuteInstructionLineEvent("Set", "var", "=Get message number"),
                               new ExecuteInstructionLineEvent("Close message"),
                               new ExecuteInstructionLineEvent("Close mail folder"),
                               new ExecuteInstructionLineEvent("Disconnect from pop3 server"),
                               e));
        assertSuccess();
        assertEquals(19, e.getValue());
    }

    @Test(expected = InstructionNotFoundException.class)
    public void openLastMail_DontCloseMail() {
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "test.mazarineblue.libraries.email"),
                               new ExecuteInstructionLineEvent("Connect to pop3 server"),
                               new ExecuteInstructionLineEvent("Open mail folder"),
                               new ExecuteInstructionLineEvent("Open last message"),
                               new ExecuteInstructionLineEvent("Set", "var", "=Get message number"),
                               new ExecuteInstructionLineEvent("Close mail folder"),
                               new ExecuteInstructionLineEvent("Disconnect from pop3 server"),
                               new ExecuteInstructionLineEvent("Close message")));
        assertFailure();
    }
}
