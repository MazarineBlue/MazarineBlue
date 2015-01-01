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
public class ConnectIMAPServerTest extends LibraryTestTemplate {

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
    public void testConnectToIMAPMailServer() throws Exception {
        setIMAPServerProfile("mailServerProfile");
        addInstruction("", "Connect to imap mail server", "mailServer", "mailServerProfile", "testersutrecht", "geenidee2");
        execute();

        MailLibrary library = fetchLibrary(MailLibrary.class);
        assertEquals(true, library.isConnectedToMailServer("mailServer"));
        addInstruction("", "Close connection to imap mail server", "mailServer");
        execute();
        assertEquals(false, library.isConnectedToMailServer("mailServer"));
    }

    public void setIMAPServerProfile(String mailServerProfile) {
        addInstruction("", "Mail server profile", mailServerProfile);
        addInstruction("", "Server", MailLibraryConstants.standardServer);
        addInstruction("", "SSL", MailLibraryConstants.standardSSL);
        addInstruction("", "StartTLS", MailLibraryConstants.standardStartTLS);
        addInstruction("", "End mail server profile");
    }

    @Test(expected = ProcessorRunningException.class)
    public void testConnectToIMAPMailServerNoMailServerIdentifier() throws Exception {
        setIMAPServerProfile("mailServerProfile");
        addInstruction("", "Connect to imap mail server", "identifier", "", "testersutrecht", "geenidee2");
        addInstruction("", "Close connection to imap mail server", "mailServer");
        execute();
    }

    @Test(expected = ProcessorRunningException.class)
    public void testConnectToIMAPMailServerWrongServerProfileIdentifier() throws Exception {
        setIMAPServerProfile("mailServerProfile");
        addInstruction("", "Connect to imap mail server", "mailServer", "mailServerProfile2", "testersutrecht", "geenidee2");
        addInstruction("", "Close connection to imap mail server", "mailServer");
        execute();
    }

    @Test(expected = ProcessorRunningException.class)
    public void testConnectToIMAPMailServerNoUsername() throws Exception {
        setIMAPServerProfile("mailServerProfile");
        addInstruction("", "Connect to imap mail server", "mailServer", "mailServerProfile", "", "geenidee2");
        addInstruction("", "Close connection to imap mail server", "mailServer");
        execute();
    }

    @Test(expected = ProcessorRunningException.class)
    public void testConnectToIMAPMailServerNoPassword() throws Exception {
        setIMAPServerProfile("mailServerProfile");
        addInstruction("", "Connect to imap mail server", "mailServer", "mailServerProfile", "testersutrecht", "");
        addInstruction("", "Close connection to imap mail server", "mailServer");
        execute();
    }

    @Test(expected = ProcessorRunningException.class)
    public void testConnectToIMAPMailServerWrongServer() throws Exception {
        addInstruction("", "Mail server profile", "mailServerProfile");
        addInstruction("", "Server", "imap.gmailz.com:993");
        addInstruction("", "SSL", "yes");
        addInstruction("", "StartTLS", "yes");
        addInstruction("", "End mail server profile");
        addInstruction("", "Connect to imap mail server", "mailServer", "mailServerProfile", "testersutrecht", "geenidee2");
        addInstruction("", "Close connection to imap mail server", "mailServer");
        execute();
    }

    @Test(expected = ProcessorRunningException.class)
    public void testConnectToIMAPMailServerWrongSSL() throws Exception {
        addInstruction("", "Mail server profile", "mailServerProfile");
        addInstruction("", "Server", "imap.gmail.com:993");
        addInstruction("", "SSL", "no");
        addInstruction("", "StartTLS", "yes");
        addInstruction("", "End mail server profile");
        addInstruction("", "Connect to imap mail server", "mailServer", "mailServerProfile", "testersutrecht", "geenidee2");
        addInstruction("", "Close connection to imap mail server", "mailServer");
        execute();
    }

    @Test(expected = ProcessorRunningException.class)
    public void testConnectToIMAPMailServerWrongUsername() throws Exception {
        setIMAPServerProfile("mailServerProfile");
        addInstruction("", "Connect to imap mail server", "mailServer", "mailServerProfile", "testersutrechtWrong", "geenidee2");
        addInstruction("", "Close connection to imap mail server", "mailServer");
        execute();
    }

    @Test(expected = ProcessorRunningException.class)
    public void testConnectToIMAPMailServerWrongPassword() throws Exception {
        setIMAPServerProfile("mailServerProfile");
        addInstruction("", "Connect to imap mail server", "mailServer", "mailServerProfile", "testersutrecht", "geenidee2222222");
        addInstruction("", "Close connection to imap mail server", "mailServer");
        execute();
    }
}
