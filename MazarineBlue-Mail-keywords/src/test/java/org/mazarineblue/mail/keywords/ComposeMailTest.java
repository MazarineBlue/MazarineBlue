/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mazarineblue.mail.keywords;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mazarineblue.LibraryTestTemplate;


/**
 *
 * @author Mandy Tak {@literal <mandy.tak@MazarineBlue.org>}
 */
public class ComposeMailTest extends LibraryTestTemplate {

    private final String from = "testersutrecht@gmail.com";
    private final String to = "testersutrecht@gmail.com";
    private final String body = "De inhoud van deze email.";
    private final String subject = "The ComposeMailTest";

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
    public void testComposeMailSubject() throws Exception {
        sendAndReceiveEmail();
        addInstruction("", "Copy subject", "subject");
        addInstruction("", "Echo", "$subject");
        closeConnections();
        execute();

        assertSystemOut(subject);
        assertError(0);
    }

    private void sendAndReceiveEmail() {
        setSMTPServerProfile("mailServerProfile");
        addInstruction("", "Connect to smtp mail server", "mailServer", "mailServerProfile", "testersutrecht", "geenidee2");
        addInstruction("", "Compose mail", "mailServer");
        addInstruction("", "From", from);
        addInstruction("", "To", to);
        addInstruction("", "Subject", subject);
        addInstruction("", "Body", body);
        addInstruction("", "Send mail", "mailServer");

        setIMAPServerProfile("mailServerProfile2");
        addInstruction("", "Connect to imap mail server", "mailServer2", "mailServerProfile2", "testersutrecht", "geenidee2");
        addInstruction("", "Open mailbox", "mailBoxId", "mailServer2", "INBOX");
        addInstruction("", "Open most recent mail", "mailBoxId", "");
    }

    public void setSMTPServerProfile(String mailServerProfile) {
        addInstruction("", "Mail server profile", mailServerProfile);
        addInstruction("", "Server", MailLibraryConstants.standardSendServer);
        addInstruction("", "SSL", MailLibraryConstants.standardSendSSL);
        addInstruction("", "StartTLS", MailLibraryConstants.standardSendStartTLS);
        addInstruction("", "End mail server profile");
    }

    public void setIMAPServerProfile(String mailServerProfile) {
        addInstruction("", "Mail server profile", mailServerProfile);
        addInstruction("", "Server", MailLibraryConstants.standardServer);
        addInstruction("", "SSL", MailLibraryConstants.standardSSL);
        addInstruction("", "StartTLS", MailLibraryConstants.standardStartTLS);
        addInstruction("", "End mail server profile");
    }

    private void closeConnections() {
        addInstruction("", "Close most recent mail");
        addInstruction("", "Close mailbox", "mailBoxId");
        addInstruction("", "Close connection to imap mail server", "mailServer2");
    }

    @Test
    public void testComposeMailFrom() throws Exception {
        sendAndReceiveEmail();
        addInstruction("", "Copy from", "from");
        addInstruction("", "Echo", "$from");
        closeConnections();
        execute();

        assertSystemOut(from);
        assertError(0);
    }

    @Test
    public void testComposeMailTo() throws Exception {
        sendAndReceiveEmail();
        addInstruction("", "Copy to", "to");
        addInstruction("", "Echo", "$to");
        closeConnections();
        execute();

        assertSystemOut(to);
        assertError(0);
    }

    @Test
    public void testComposeMailBody() throws Exception {
        sendAndReceiveEmail();
        addInstruction("", "Copy body", "body");
        addInstruction("", "Echo", "$body");
        closeConnections();
        execute();

        assertSystemOut(body);
        assertError(0);
    }
}
