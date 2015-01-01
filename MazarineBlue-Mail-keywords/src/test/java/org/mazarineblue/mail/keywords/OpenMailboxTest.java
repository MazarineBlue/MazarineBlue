/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mazarineblue.mail.keywords;

import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mazarineblue.LibraryTestTemplate;
import org.mazarineblue.keyworddriven.exceptions.ProcessorRunningException;


/**
 *
 * @author Mandy Tak {@literal <mandy.tak@MazarineBlue.org>}
 */
public class OpenMailboxTest extends LibraryTestTemplate {

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setup() throws Exception {
        addInstruction("", "Import", "org.mazarineblue.mail");
        execute();
    }

    @After
    public void teardown() {
    }

    @Test
    public void testOpenMailbox() throws Exception {
        String mailBoxId = "mailBoxId";
        setIMAPServerProfile("mailServerProfile");
        addInstruction("", "Connect to imap mail server", "mailServer", "mailServerProfile", "testersutrecht", "geenidee2");
        addInstruction("", "Open mailbox", mailBoxId, "mailServer", "INBOX");
        execute();

        MailLibrary lib = fetchLibrary(MailLibrary.class);
        assertEquals(true, lib.isMailboxOpen(mailBoxId));
        addInstruction("", "Close mailbox", "mailBoxId");
        execute();

        assertEquals(false, lib.isMailboxOpen(mailBoxId));
        addInstruction("", "Close connection to imap mail server", "mailServer");
        execute();
    }

    public void setIMAPServerProfile(String mailServerProfile) {
        addInstruction("", "Mail server profile", mailServerProfile);
        addInstruction("", "Server", MailLibraryConstants.standardServer);
        addInstruction("", "SSL", MailLibraryConstants.standardSSL);
        addInstruction("", "StartTLS", MailLibraryConstants.standardStartTLS);
        addInstruction("", "End mail server profile");
    }

    @Test(expected = ProcessorRunningException.class)
    public void testOpenMailboxWithEmptyMailIdentifier() throws Exception {
        setIMAPServerProfile("mailServerProfile");
        addInstruction("", "Connect to mail server", "mailServer", "mailServerProfile", "testersutrecht", "geenidee2");
        addInstruction("", "Open mailbox", "", "mailServer", "INBOX");
        addInstruction("", "Close mailbox", "mailBoxId");
        addInstruction("", "Close connection to mail server", "mailServer");
        execute();
    }

    @Test(expected = ProcessorRunningException.class)
    public void testOpenMailboxWithEmptyMailServerIdentifier() throws Exception {
        setIMAPServerProfile("mailServerProfile");
        addInstruction("", "Connect to mail server", "mailServer", "mailServerProfile", "testersutrecht", "geenidee2");
        addInstruction("", "Open mailbox", "mailBoxId", "", "INBOX");
        addInstruction("", "Close mailbox", "mailBoxId");
        addInstruction("", "Close connection to mail server", "mailServer");
        execute();
    }

    @Test(expected = ProcessorRunningException.class)
    public void testOpenMailboxWithEmptyMailFolderName() throws Exception {
        setIMAPServerProfile("mailServerProfile");
        addInstruction("", "Connect to mail server", "mailServer", "mailServerProfile", "testersutrecht", "geenidee2");
        addInstruction("", "Open mailbox", "mailBoxId", "mailServer", "");
        addInstruction("", "Close mailbox", "mailBoxId");
        addInstruction("", "Close connection to mail server", "mailServer");
        execute();
    }

    @Test(expected = ProcessorRunningException.class)
    public void testOpenMailboxWithWrongMailServerIdentifier() throws Exception {
        setIMAPServerProfile("mailServerProfile");
        addInstruction("", "Connect to mail server", "mailServer", "mailServerProfile", "testersutrecht", "geenidee2");
        addInstruction("", "Open mailbox", "mailBoxId", "mailServer2", "INBOX");
        addInstruction("", "Close mailbox", "mailBoxId");
        addInstruction("", "Close connection to mail server", "mailServer");
        execute();
    }

    @Test(expected = ProcessorRunningException.class)
    public void testOpenMailboxWithWrongNonExistentFolder() throws Exception {
        setIMAPServerProfile("mailServerProfile");
        addInstruction("", "Connect to mail server", "mailServer", "mailServerProfile", "testersutrecht", "geenidee2");
        addInstruction("", "Open mailbox", "mailBoxId", "mailServer", "INBOX2");
        addInstruction("", "Close mailbox", "mailBoxId");
        addInstruction("", "Close connection to mail server", "mailServer");
        execute();
    }

    @Test(expected = ProcessorRunningException.class)
    public void testOpenMailboxWithoutServer() throws Exception {
        addInstruction("", "Open mailbox", "mailBoxId", "mailServer", "INBOX");
        execute();
    }

}
