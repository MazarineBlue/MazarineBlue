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

import java.io.File;
import java.io.IOException;
import javax.mail.MessagingException;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.eventdriven.feeds.MemoryFeed;
import org.mazarineblue.executors.events.SetFileSystemEvent;
import org.mazarineblue.executors.util.AbstractExecutorTestHelper;
import org.mazarineblue.fs.FileSystem;
import org.mazarineblue.fs.MemoryFileSystem;
import org.mazarineblue.fs.ReadOnlyFileSystem;
import org.mazarineblue.keyworddriven.events.ExecuteInstructionLineEvent;
import org.mazarineblue.libraries.email.exceptions.EmailBackendException;
import static org.mazarineblue.libraries.util.EmailUtil.createMultiPartWithAttachements;
import org.mazarineblue.libraries.util.TestMessage;
import org.mazarineblue.libraries.util.TestStore;

public class AttachmentLibraryTest
        extends AbstractExecutorTestHelper {

    private TestMessage msg;

    @Before
    public void setup()
            throws MessagingException, IOException {
        msg = new TestMessage();
        msg.setContent(createMultiPartWithAttachements(1));
        TestStore.init();
        TestStore.putFolder("foo", TestStore.createFolderContaining(msg));
    }

    @After
    public void teardown() {
        msg = null;
    }

    @Test
    public void saveSelectedAttachmentAs_WritableFileSystem()
            throws IOException {
        MemoryFileSystem fs = new MemoryFileSystem();
        SetFileSystemEvent setFileSystemEvent = new SetFileSystemEvent(fs);
        execute(new MemoryFeed(setFileSystemEvent,
                               new ExecuteInstructionLineEvent("Import library", "test.mazarineblue.libraries.email"),
                               new ExecuteInstructionLineEvent("Connect to pop3 server"),
                               new ExecuteInstructionLineEvent("Open mail folder", "foo"),
                               new ExecuteInstructionLineEvent("Open first message"),
                               new ExecuteInstructionLineEvent("Select attachment at index", "0"),
                               new ExecuteInstructionLineEvent("Save selected attachment"),
                               new ExecuteInstructionLineEvent("Deselect attachment"),
                               new ExecuteInstructionLineEvent("Close message"),
                               new ExecuteInstructionLineEvent("Close mail folder"),
                               new ExecuteInstructionLineEvent("Disconnect from pop3 server")
        ));
        assertSuccess();
        File attachment0 = new File("attachment0.txt");
        assertTrue(fs.exists(attachment0));
        assertEquals("bar", fs.getContent(attachment0));
    }

    @Test(expected = EmailBackendException.class)
    public void saveSelectedAttachmentAs_ReadOnlyFileSystem_ThrowsException()
            throws IOException {
        FileSystem fs = new ReadOnlyFileSystem(new MemoryFileSystem());
        SetFileSystemEvent setFileSystemEvent = new SetFileSystemEvent(fs);
        execute(new MemoryFeed(setFileSystemEvent,
                               new ExecuteInstructionLineEvent("Import library", "test.mazarineblue.libraries.email"),
                               new ExecuteInstructionLineEvent("Connect to pop3 server"),
                               new ExecuteInstructionLineEvent("Open mail folder", "foo"),
                               new ExecuteInstructionLineEvent("Open first message"),
                               new ExecuteInstructionLineEvent("Select attachment at index", "0"),
                               new ExecuteInstructionLineEvent("Save selected attachment")
        ));
        assertFailure();
        File attachment0 = new File("attachment0.txt");
        assertTrue(fs.exists(attachment0));
        assertEquals("bar", fs.getContent(attachment0));
    }
}
