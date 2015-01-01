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
public class ConnectSMTPServerTest extends LibraryTestTemplate {

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
    public void testConnectToSMTPMailServer() throws Exception {
        setSMTPServerProfile("mailServerProfile");
        addInstruction("", "Connect to smtp mail server", "mailServer", "mailServerProfile", "testersutrecht", "geenidee2");
        execute();

        MailLibrary library = fetchLibrary(MailLibrary.class);
        assertEquals(true, library.containsSMTPMailSession("mailServer"));
    }

    public void setSMTPServerProfile(String mailServerProfile) {
        addInstruction("", "Mail server profile", mailServerProfile);
        addInstruction("", "Server", MailLibraryConstants.standardSendServer);
        addInstruction("", "SSL", MailLibraryConstants.standardSendSSL);
        addInstruction("", "StartTLS", MailLibraryConstants.standardSendStartTLS);
        addInstruction("", "End mail server profile");
    }

    @Test(expected = ProcessorRunningException.class)
    public void testConnectToSMTPMailServerNoMailServerIdentifier() throws Exception {
        setSMTPServerProfile("mailServerProfile");
        addInstruction("", "Connect to smtp mail server", "profile", "", "testersutrecht", "geenidee2");
        execute();
    }

    @Test(expected = ProcessorRunningException.class)
    public void testConnectToSMTPMailServerWrongServerProfileIdentifier() throws Exception {
        setSMTPServerProfile("mailServerProfile");
        addInstruction("", "Connect to smtp mail server", "mailServer", "mailServerProfile2", "testersutrecht", "geenidee2");
        execute();
    }

    @Test(expected = ProcessorRunningException.class)
    public void testConnectToSMTPMailServerNoUsername() throws Exception {
        setSMTPServerProfile("mailServerProfile");
        addInstruction("", "Connect to smtp mail server", "mailServer", "mailServerProfile", "", "geenidee2");
        execute();
    }

    @Test(expected = ProcessorRunningException.class)
    public void testConnectToSMTPMailServerNoPassword() throws Exception {
        setSMTPServerProfile("mailServerProfile");
        addInstruction("", "Connect to smtp mail server", "mailServer", "mailServerProfile", "testersutrecht", "");
        execute();
    }
}
