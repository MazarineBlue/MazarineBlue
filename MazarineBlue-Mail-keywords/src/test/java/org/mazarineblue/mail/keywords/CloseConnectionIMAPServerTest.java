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
import static org.mazarineblue.mail.keywords.MailLibraryConstants.standardSSL;
import static org.mazarineblue.mail.keywords.MailLibraryConstants.standardServer;
import static org.mazarineblue.mail.keywords.MailLibraryConstants.standardStartTLS;

/**
 *
 * @author Mandy Tak {@literal <mandy.tak@MazarineBlue.org>}
 */
public class CloseConnectionIMAPServerTest extends LibraryTestTemplate {

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
    public void testCloseConnectionToIMAPMailServer() throws Exception {
        String serverID = "mailServer";
        setIMAPServerProfile("mailServerProfile");
        addInstruction("", "Connect to imap mail server", serverID, "mailServerProfile", "testersutrecht", "geenidee2");
        execute();

        MailLibrary lib = fetchLibrary(MailLibrary.class);
        assertEquals(true, lib.isConnectedToMailServer(serverID));
        addInstruction("", "Close connection to imap mail server", serverID);
        execute();
        assertEquals(false, lib.isConnectedToMailServer(serverID));
    }

    public void setIMAPServerProfile(String mailServerProfile) {
        addInstruction("", "Mail server profile", mailServerProfile);
        addInstruction("", "Server", standardServer);
        addInstruction("", "SSL", standardSSL);
        addInstruction("", "StartTLS", standardStartTLS);
        addInstruction("", "End mail server profile");
    }

    @Test(expected = ProcessorRunningException.class)
    public void testCloseConnectionToIMAPMailServerEmptyIdentifier() throws Exception {
        setIMAPServerProfile("mailServerProfile");
        addInstruction("", "Connect to mail server", "mailServer", "mailServerProfile", "testersutrecht", "geenidee2");
        addInstruction("", "Close connection to mail server", "");
        execute();
    }

    @Test(expected = ProcessorRunningException.class)
    public void testCloseConnectionToIMAPMailServerWrongIdentifier() throws Exception {
        setIMAPServerProfile("mailServerProfile");
        addInstruction("", "Connect to mail server", "mailServer2", "mailServerProfile", "testersutrecht", "geenidee2");
        addInstruction("", "Close connection to mail server", "mailServer");
        execute();
    }
}
