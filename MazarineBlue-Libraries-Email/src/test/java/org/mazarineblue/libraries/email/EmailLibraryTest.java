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

import org.junit.Test;
import org.mazarineblue.eventdriven.feeds.MemoryFeed;
import org.mazarineblue.executors.util.AbstractExecutorTestHelper;
import org.mazarineblue.keyworddriven.events.ExecuteInstructionLineEvent;

public class EmailLibraryTest
        extends AbstractExecutorTestHelper {

    @Test
    public void connectPopServer_DontDisconnect() {
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "test.mazarineblue.libraries.email"),
                               new ExecuteInstructionLineEvent("Connect to pop3 server")));
        assertSuccess();
    }

    @Test
    public void connectPopServer_ReconnectWithoutDisconnecting() {
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "test.mazarineblue.libraries.email"),
                               new ExecuteInstructionLineEvent("Connect to pop3 server"),
                               new ExecuteInstructionLineEvent("Connect to pop3 server"),
                               new ExecuteInstructionLineEvent("Disconnect from pop3 server")));
        assertSuccess();
    }

    @Test
    public void connectPopServer_DisconnectAndReconnect() {
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "test.mazarineblue.libraries.email"),
                               new ExecuteInstructionLineEvent("Connect to pop3 server"),
                               new ExecuteInstructionLineEvent("Disconnect from pop3 server"),
                               new ExecuteInstructionLineEvent("Connect to pop3 server"),
                               new ExecuteInstructionLineEvent("Disconnect from pop3 server")));
        assertSuccess();
    }

    @Test
    public void connectImapServer_DontDisconnect() {
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "test.mazarineblue.libraries.email"),
                               new ExecuteInstructionLineEvent("Connect to imap server")));
        assertSuccess();
    }

    @Test
    public void connectImapServer_ReconnectWithoutDisconnecting() {
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "test.mazarineblue.libraries.email"),
                               new ExecuteInstructionLineEvent("Connect to imap server"),
                               new ExecuteInstructionLineEvent("Connect to imap server"),
                               new ExecuteInstructionLineEvent("Disconnect from imap server")));
        assertSuccess();
    }

    @Test
    public void connectImapServer_DisconnetAndReconnect() {
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "test.mazarineblue.libraries.email"),
                               new ExecuteInstructionLineEvent("Connect to imap server"),
                               new ExecuteInstructionLineEvent("Disconnect from imap server"),
                               new ExecuteInstructionLineEvent("Connect to imap server"),
                               new ExecuteInstructionLineEvent("Disconnect from imap server")));
        assertSuccess();
    }
}
