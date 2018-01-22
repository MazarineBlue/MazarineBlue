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

import java.io.IOException;
import java.util.Date;
import javax.mail.MessagingException;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.eventdriven.feeds.MemoryFeed;
import org.mazarineblue.executors.util.AbstractExecutorTestHelper;
import org.mazarineblue.keyworddriven.events.ExecuteInstructionLineEvent;
import org.mazarineblue.keyworddriven.exceptions.InstructionNotFoundException;
import org.mazarineblue.libraries.email.exceptions.ContentNotFoundException;
import org.mazarineblue.libraries.email.exceptions.MessageAttachmentNotFoundException;
import static org.mazarineblue.libraries.util.EmailUtil.createMultiPartWithAttachements;
import org.mazarineblue.libraries.util.TestMessage;
import org.mazarineblue.libraries.util.TestStore;
import org.mazarineblue.variablestore.events.GetVariableEvent;

public class MessageLibraryTest
        extends AbstractExecutorTestHelper {

    private TestMessage msg;

    @Before
    public void setup() {
        msg = new TestMessage();
        TestStore.init();
        TestStore.putFolder("foo", TestStore.createFolderContaining(msg));
    }

    @After
    public void teardown() {
        TestStore.clear();
    }

    @Test
    public void getMessageNumber() {
        assertEquals(-1, executeAndReturnValue("Get message number"));
        assertSuccess();
    }

    @Test
    public void getSendDate() {
        msg.setSentDate(new Date(1));
        assertEquals(new Date(1), executeAndReturnValue("Get send date of message"));
        assertSuccess();
    }

    @Test
    public void getReceivedDate() {
        msg.setReceivedDate(new Date(2));
        assertEquals(new Date(2), executeAndReturnValue("Get received date of message"));
        assertSuccess();
    }

    @Test
    public void getSubject() {
        msg.setSubject("bar");
        assertEquals("bar", executeAndReturnValue("Get subject of message"));
        assertSuccess();
    }

    @Test
    public void getTextPlain_SinglePart() {
        msg.setText("bar");
        assertEquals("bar", executeAndReturnValue("Get plain text of message"));
        assertSuccess();
    }

    @Test
    public void getTextPlain_MultiPart()
            throws Exception {
        msg.setContent("bar", "text/plain");
        assertEquals("bar", executeAndReturnValue("Get plain text of message"));
        assertSuccess();
    }

    @Test(expected = ContentNotFoundException.class)
    public void getTextPlain_MultiPart_WrongType()
            throws Exception {
        msg.setContent("bar", "text/html");
        assertEquals(null, executeAndReturnValue("Get plain text of message"));
        assertFailure();
    }

    @Test(expected = ContentNotFoundException.class)
    public void getTextHtml_SinglePart()
            throws Exception {
        msg.setText("bar");
        assertEquals(null, executeAndReturnValue("Get html of message"));
        assertFailure();
    }

    @Test(expected = ContentNotFoundException.class)
    public void getTextHtml_MultiPart_WrongType()
            throws Exception {
        msg.setContent("bar", "text/plain");
        assertEquals(null, executeAndReturnValue("Get html of message"));
        assertFailure();
    }

    @Test
    public void getTextHtml_MultiPart()
            throws Exception {
        msg.setContent("bar", "text/html");
        assertEquals("bar", executeAndReturnValue("Get html of message"));
        assertSuccess();
    }

    @Test
    public void countAttachments_NoAttachements() {
        assertEquals(0, executeAndReturnValue("Count attachments"));
        assertSuccess();
    }

    @Test
    public void countAttachments_OneAttachement()
            throws Exception {
        msg.setContent(createMultiPartWithAttachements(1));
        assertEquals(1, executeAndReturnValue("Count attachments"));
        assertSuccess();
    }

    @Test
    public void countAttachments_TwoAttachements()
            throws Exception {
        msg.setContent(createMultiPartWithAttachements(2));
        assertEquals(2, executeAndReturnValue("Count attachments"));
        assertSuccess();
    }

    private Object executeAndReturnValue(String keyword, Object... parameters) {
        GetVariableEvent e = new GetVariableEvent("var");
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "test.mazarineblue.libraries.email"),
                               new ExecuteInstructionLineEvent("Connect to pop3 server"),
                               new ExecuteInstructionLineEvent("Open mail folder", "foo"),
                               new ExecuteInstructionLineEvent("Open first message"),
                               new ExecuteInstructionLineEvent("Set", "var", "=" + keyword, parameters),
                               new ExecuteInstructionLineEvent("Close message"),
                               new ExecuteInstructionLineEvent("Close mail folder"),
                               new ExecuteInstructionLineEvent("Disconnect from pop3 server"),
                               e));
        return e.getValue();
    }

    @Test(expected = MessageAttachmentNotFoundException.class)
    public void selectAttachement_SelectFirstAttachement_NoAttachements() {
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "test.mazarineblue.libraries.email"),
                               new ExecuteInstructionLineEvent("Connect to pop3 server"),
                               new ExecuteInstructionLineEvent("Open mail folder", "foo"),
                               new ExecuteInstructionLineEvent("Open first message"),
                               new ExecuteInstructionLineEvent("Select attachment at index", "1")
        ));
        assertFailure();
    }

    @Test(expected = InstructionNotFoundException.class)
    public void selectAttachement_SelectFirstAttachement_ButDoNotDeselect()
            throws MessagingException, IOException {
        msg.setContent(createMultiPartWithAttachements(1));
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "test.mazarineblue.libraries.email"),
                               new ExecuteInstructionLineEvent("Connect to pop3 server"),
                               new ExecuteInstructionLineEvent("Open mail folder", "foo"),
                               new ExecuteInstructionLineEvent("Open first message"),
                               new ExecuteInstructionLineEvent("Select attachment at index", "0"),
                               new ExecuteInstructionLineEvent("Close message"),
                               new ExecuteInstructionLineEvent("Close mail folder"),
                               new ExecuteInstructionLineEvent("Disconnect from pop3 server"),
                               new ExecuteInstructionLineEvent("Deselect attachment")
        ));
        assertFailure();
    }

    @Test(expected = InstructionNotFoundException.class)
    public void selectAttachement_SelectAttachementUsingFilename_ButDoNotDeselect()
            throws MessagingException, IOException {
        msg.setContent(createMultiPartWithAttachements(1));
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "test.mazarineblue.libraries.email"),
                               new ExecuteInstructionLineEvent("Connect to pop3 server"),
                               new ExecuteInstructionLineEvent("Open mail folder", "foo"),
                               new ExecuteInstructionLineEvent("Open first message"),
                               new ExecuteInstructionLineEvent("Select attachment with filename", "att"),
                               new ExecuteInstructionLineEvent("Close message"),
                               new ExecuteInstructionLineEvent("Close mail folder"),
                               new ExecuteInstructionLineEvent("Disconnect from pop3 server"),
                               new ExecuteInstructionLineEvent("Deselect attachment")
        ));
        assertFailure();
    }

    @Test(expected = MessageAttachmentNotFoundException.class)
    public void selectAttachement_SelectNonExistingAttachement_OneAttachements()
            throws Exception {
        msg.setContent(createMultiPartWithAttachements(1));
        executeAndDeselectAttachement("Select attachment at index", "1");
        assertSuccess();
    }

    @Test
    public void selectAttachement_SelectFirstAttachement_OneAttachements()
            throws Exception {
        msg.setContent(createMultiPartWithAttachements(1));
        executeAndDeselectAttachement("Select attachment at index", "0");
        assertSuccess();
    }

    @Test(expected = MessageAttachmentNotFoundException.class)
    public void selectAttachementWithFilename_SelectNonExistingAttachement_OneAttachements()
            throws Exception {
        msg.setContent(createMultiPartWithAttachements(1));
        executeAndDeselectAttachement("Select attachment with filename", "foobar");
        assertSuccess();
    }

    @Test
    public void selectAttachementWithFilename_SelectFirstAttachement_OneAttachements()
            throws Exception {
        msg.setContent(createMultiPartWithAttachements(1));
        executeAndDeselectAttachement("Select attachment with filename", "attachment0");
        assertSuccess();
    }

    private void executeAndDeselectAttachement(String keyword, String... arguments) {
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "test.mazarineblue.libraries.email"),
                               new ExecuteInstructionLineEvent("Connect to pop3 server"),
                               new ExecuteInstructionLineEvent("Open mail folder", "foo"),
                               new ExecuteInstructionLineEvent("Open first message"),
                               new ExecuteInstructionLineEvent(keyword, arguments),
                               new ExecuteInstructionLineEvent("Deselect attachment"),
                               new ExecuteInstructionLineEvent("Close message"),
                               new ExecuteInstructionLineEvent("Close mail folder"),
                               new ExecuteInstructionLineEvent("Disconnect from pop3 server")
        ));
    }
}
