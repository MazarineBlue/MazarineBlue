/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mazarineblue.mail.keywords;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import javax.mail.Header;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.LibraryTestTemplate;
import org.mazarineblue.keyworddriven.exceptions.ProcessorRunningException;

/**
 *
 * @author Mandy Tak {@literal <mandy.tak@MazarineBlue.org>}
 */
public class OpenCloseMostRecentMailTest extends LibraryTestTemplate {

    private final String sleep = "5000";
    private final String from_field = "testersutrecht@gmail.com";
    private final String body_field = "De inhoud van deze email.";
    private final String expectedTo = "testersutrecht@gmail.com, testersutrecht2@gmail.com";
    private String subject_field;
    private final ArrayList<String> to = new ArrayList<>();

    public OpenCloseMostRecentMailTest() {
        to.add("testersutrecht@gmail.com");
        to.add("testersutrecht2@gmail.com");
    }

    @Before
    public void setup() throws Exception {
        subject_field = "Date: " + new Date();

        addInstruction("", "Import", "org.mazarineblue.mail");
        execute();
    }

    @After
    public void teardown() {
    }

    @Test
    public void testCopySubject() throws Exception {
        sendEmail();
        addInstruction("", "Sleep", sleep);
        openMail();
        addInstruction("", "Copy subject", "subject");
        addInstruction("", "Echo", "$subject");
        closeMail();
        execute();

        assertSystemOut(subject_field);
        assertError(0);
    }

    private void sendEmail() {
        setSMTPServerProfile("mailServerProfile");
        addInstruction("", "Connect to smtp mail server", "mailServer", "mailServerProfile", "testersutrecht", "geenidee2");
        addInstruction("", "Compose mail", "mailServer");

        addInstruction("", "From", from_field);

        for (String recipient : to)
            addInstruction("", "To", recipient);

        addInstruction("", "Subject", subject_field);
        addInstruction("", "Body", body_field);
        addInstruction("", "Send mail", "mailServer");
    }

    public void setSMTPServerProfile(String mailServerProfile) {
        addInstruction("", "Mail server profile", mailServerProfile);
        addInstruction("", "Server", MailLibraryConstants.standardSendServer);
        addInstruction("", "SSL", MailLibraryConstants.standardSendSSL);
        addInstruction("", "StartTLS", MailLibraryConstants.standardSendStartTLS);
        addInstruction("", "End mail server profile");
    }


    private void openMail() {
        setIMAPServerProfile("mailServerProfile2");
        addInstruction("", "Connect to imap mail server", "mailServer2", "mailServerProfile2", "testersutrecht", "geenidee2");
        addInstruction("", "Open mailbox", "mailBoxId", "mailServer2", "INBOX");
        addInstruction("", "Open most recent mail", "mailBoxId", "");
    }

    public void setIMAPServerProfile(String mailServerProfile) {
        addInstruction("", "Mail server profile", mailServerProfile);
        addInstruction("", "Server", MailLibraryConstants.standardServer);
        addInstruction("", "SSL", MailLibraryConstants.standardSSL);
        addInstruction("", "StartTLS", MailLibraryConstants.standardStartTLS);
        addInstruction("", "End mail server profile");
    }

    private void closeMail() {
        addInstruction("", "Close most recent mail");
        addInstruction("", "Close mailbox", "mailBoxId");
        addInstruction("", "Close connection to imap mail server", "mailServer2");
    }

    @Test
    public void testCopyTo() throws Exception {
        sendEmail();
        addInstruction("", "Sleep", sleep);
        openMail();
        addInstruction("", "Copy to", "to");
        addInstruction("", "Echo", "$to");
        closeMail();
        execute();

        assertSystemOut(expectedTo);
        assertError(0);
    }

    @Test
    public void testCopyFrom() throws Exception {
        sendEmail();
        addInstruction("", "Sleep", sleep);
        openMail();
        addInstruction("", "Copy from", "from");
        addInstruction("", "Echo", "$from");
        closeMail();
        execute();

        assertSystemOut(from_field);
        assertError(0);
    }

    @Test
    public void testCopyBody() throws Exception {
        sendEmail();
        addInstruction("", "Sleep", sleep);
        openMail();
        addInstruction("", "Copy body", "body");
        addInstruction("", "Echo", "$body");
        closeMail();
        execute();

        assertSystemOut(body_field);
        assertError(0);
    }

    @Test
    public void testCopyHeader() throws Exception {
        sendEmail();
        addInstruction("", "Sleep", sleep);
        openMail();
        addInstruction("", "Copy header", "header");
        addInstruction("", "Echo", "$header");
        closeMail();
        execute();

        OpenMostRecentMailLibrary readMailLibrary = fetchLibrary(OpenMostRecentMailLibrary.class);
        Enumeration headers = readMailLibrary.getHeader();
        while (headers.hasMoreElements()) {
            Header h = (Header) headers.nextElement();
            if (h.getName().equals("To"))
                assertEquals(expectedTo, h.getValue());
            if (h.getName().equals("From"))
                assertEquals(from_field, h.getValue());
            if (h.getName().equals("Subject"))
                assertEquals(subject_field, h.getValue());
        }
        assertError(0);
    }

    @Test()
    public void testOpenMostRecentMailFilterSubject() throws Exception {
        String subject1 = new Date().toString();
        String searchSubject1 = "^" + subject1 + "$";
        ArrayList<String> toList = new ArrayList<>();
        toList.add("testersutrecht@gmail.com");

        sendWithSMTPServer(subject1, "De inhoud van deze email.",
                "testersutrecht@gmail.com", toList, "mailServer",
                "mailServerProfile", "testersutrecht", "geenidee2");

        sendWithSMTPServer("Bla", "De inhoud van deze email.",
                "testersutrecht@gmail.com", toList, "mailServer2",
                "mailServerProfileSecond", "testersutrecht", "geenidee2");

        addInstruction("", "Mail filter", "MailFilter");
        addInstruction("", "Subject", searchSubject1);
        addInstruction("", "End mail filter");

        openIMAPServer("mailServer3", "mailServerProfile2", "testersutrecht", "geenidee2");
        addInstruction("", "Copy subject", "subject");
        addInstruction("", "Echo", "$subject");

        closeMailBoxAndIMAPServer("mailServer3");
        execute();

        assertSystemOut(subject1);
        assertError(0);
    }

    private void sendWithSMTPServer(String subject, String body, String from, ArrayList<String> toAddresses,
            String serverId, String mailServerProfileId, String inlogUsername, String inlogPassword) {

        setSMTPServerProfile(mailServerProfileId);
        addInstruction("", "Connect to smtp mail server", serverId, mailServerProfileId, inlogUsername, inlogPassword);
        addInstruction("", "Compose mail", serverId);
        addInstruction("", "From", from);

        for (String toAddresse : toAddresses)
            addInstruction("", "To", toAddresse);

        addInstruction("", "Subject", subject);
        addInstruction("", "Body", body);
        addInstruction("", "Send mail", serverId);
    }

    private void openIMAPServer(String serverId, String mailServerProfileId,
            String inlogUsername, String inlogPassword) {

        setIMAPServerProfile(mailServerProfileId);
        addInstruction("", "Connect to imap mail server", serverId, mailServerProfileId, inlogUsername, inlogPassword);
        addInstruction("", "Open mailbox", "mailBoxId", serverId, "INBOX");
        addInstruction("", "Open most recent mail", "mailBoxId", "MailFilter");
    }

    private void closeMailBoxAndIMAPServer(String serverId) {
        addInstruction("", "Close most recent mail");
        addInstruction("", "Close mailbox", "mailBoxId");
        addInstruction("", "Close connection to imap mail server", serverId);
    }

    @Test()
    public void testOpenMostRecentMailFilterSubjectRegExp() throws Exception {
        String subject = "Het eerste onderwerp";
        String subject2 = "Het tweede onderwerp";
        String searchSubject = "^Het eerste.*$";
        ArrayList<String> toList = new ArrayList<>();
        toList.add("testersutrecht@gmail.com");

        sendWithSMTPServer(subject, "De inhoud van deze email.",
                "testersutrecht@gmail.com", toList, "mailServer",
                "mailServerProfile", "testersutrecht", "geenidee2");

        sendWithSMTPServer(subject2, "De inhoud van deze email.",
                "testersutrecht@gmail.com", toList, "mailServer2",
                "mailServerProfileSecond", "testersutrecht", "geenidee2");

        addInstruction("", "Mail filter", "MailFilter");
        addInstruction("", "Subject", searchSubject);
        addInstruction("", "End mail filter");

        openIMAPServer("mailServer3", "mailServerProfile2", "testersutrecht", "geenidee2");
        addInstruction("", "Copy subject", "subject");
        addInstruction("", "Echo", "$subject");
        closeMailBoxAndIMAPServer("mailServer3");
        execute();

        assertSystemOut(subject);
        assertError(0);
    }

    @Test()
    public void testOpenMostRecentMailFilterBody() throws Exception {
        String subject = new Date().toString();
        String body1 = "Ik heb de goede inhoud";
        String body2 = "Ik heb de foute inhoud";
        ArrayList<String> toList = new ArrayList<>();
        toList.add("testersutrecht@gmail.com");

        sendWithSMTPServer(subject, body1,
                "testersutrecht@gmail.com", toList, "mailServer",
                "mailServerProfile", "testersutrecht", "geenidee2");

        sendWithSMTPServer(subject, body2,
                "testersutrecht@gmail.com", toList, "mailServer2",
                "mailServerProfileSecond", "testersutrecht", "geenidee2");

        addInstruction("", "Mail filter", "MailFilter");
        addInstruction("", "Body", "^" + body1 + "$");
        addInstruction("", "End mail filter");

        openIMAPServer("mailServer3", "mailServerProfile2", "testersutrecht", "geenidee2");
        addInstruction("", "Copy body", "body");
        addInstruction("", "Echo", "$body");

        closeMailBoxAndIMAPServer("mailServer3");

        execute();

        assertSystemOut(body1);
        assertError(0);
    }

    @Test()
    public void testOpenMostRecentMailFilterBodyRegExp() throws Exception {
        String subject = new Date().toString();
        String body1 = "^.*goede.*$";
        String body1Complete = "Ik heb de goede inhoud";
        String body2Complete = "Ik heb de foute inhoud";

        ArrayList<String> toList = new ArrayList<>();
        toList.add("testersutrecht@gmail.com");

        sendWithSMTPServer(subject, body1Complete,
                "testersutrecht@gmail.com", toList, "mailServer",
                "mailServerProfile", "testersutrecht", "geenidee2");

        sendWithSMTPServer(subject, body2Complete,
                "testersutrecht@gmail.com", toList, "mailServer2",
                "mailServerProfileSecond", "testersutrecht", "geenidee2");

        addInstruction("", "Mail filter", "MailFilter");
        addInstruction("", "Body", body1);
        addInstruction("", "End mail filter");

        openIMAPServer("mailServer3", "mailServerProfile2", "testersutrecht", "geenidee2");
        addInstruction("", "Copy body", "body");
        addInstruction("", "Echo", "$body");

        closeMailBoxAndIMAPServer("mailServer3");

        execute();

        assertSystemOut(body1Complete);
        assertError(0);
    }

    @Test()
    public void testOpenMostRecentMailFilterSubjectAndBody() throws Exception {

        String correctSubject = new Date().toString();
        String correctBody = "Ik heb de goede inhoud";
        String wrongBody = "Ik heb de foute inhoud";

        ArrayList<String> toList = new ArrayList<>();
        toList.add("testersutrecht@gmail.com");

        sendWithSMTPServer(correctSubject, correctBody,
                "testersutrecht@gmail.com", toList, "mailServer",
                "mailServerProfile", "testersutrecht", "geenidee2");

        sendWithSMTPServer(correctSubject, wrongBody,
                "testersutrecht@gmail.com", toList, "mailServer2",
                "mailServerProfileSecond", "testersutrecht", "geenidee2");

        addInstruction("", "Mail filter", "MailFilter");
        addInstruction("", "Body", "^" + correctBody + "$");
        addInstruction("", "End mail filter");

        openIMAPServer("mailServer3", "mailServerProfile2", "testersutrecht", "geenidee2");
        addInstruction("", "Copy body", "body");
        addInstruction("", "Echo", "$body");

        closeMailBoxAndIMAPServer("mailServer3");

        execute();

        assertSystemOut(correctBody);
        assertError(0);
    }

    @Test()
    public void testOpenMostRecentMailFilterFrom() throws Exception {
        String subject = new Date().toString();

        ArrayList<String> toList = new ArrayList<>();
        toList.add("testersutrecht@gmail.com");

        sendWithSMTPServer(subject, "De inhoud van deze mail.",
                "testersutrecht2@gmail.com", toList, "mailServer2",
                "mailServerProfileSecond", "testersutrecht2", "geenidee2");

        sendWithSMTPServer(subject, "De inhoud van deze mail.",
                "testersutrecht@gmail.com", toList, "mailServer",
                "mailServerProfile", "testersutrecht", "geenidee2");

        addInstruction("", "Mail filter", "MailFilter");
        addInstruction("", "From", "^testersutrecht2@gmail.com$");
        addInstruction("", "End mail filter");

        openIMAPServer("mailServer3", "mailServerProfile2", "testersutrecht", "geenidee2");
        addInstruction("", "Copy from", "from");
        addInstruction("", "Echo", "$from");

        closeMailBoxAndIMAPServer("mailServer3");

        execute();

        assertSystemOut("testersutrecht2@gmail.com");
        assertError(0);
    }

    @Test()
    public void testOpenMostRecentMailFilterFromRegExp() throws Exception {
        String subject = new Date().toString();

        ArrayList<String> toList = new ArrayList<>();
        toList.add("testersutrecht@gmail.com");

        sendWithSMTPServer(subject, "De inhoud van deze mail.",
                "testersutrecht2@gmail.com", toList, "mailServer2",
                "mailServerProfileSecond", "testersutrecht2", "geenidee2");

        sendWithSMTPServer(subject, "De inhoud van deze mail.",
                "testersutrecht@gmail.com", toList, "mailServer",
                "mailServerProfile", "testersutrecht", "geenidee2");

        addInstruction("", "Mail filter", "MailFilter");
        addInstruction("", "From", "^.*2.*$");
        addInstruction("", "End mail filter");

        openIMAPServer("mailServer3", "mailServerProfile2", "testersutrecht", "geenidee2");
        addInstruction("", "Copy from", "from");
        addInstruction("", "Echo", "$from");

        closeMailBoxAndIMAPServer("mailServer3");

        execute();

        assertSystemOut("testersutrecht2@gmail.com");
        assertError(0);
    }

    @Test()
    public void testOpenMostRecentMailFilterToRegExp() throws Exception {
        String subject = new Date().toString();

        ArrayList<String> toList = new ArrayList<>();
        toList.add("testersutrecht@gmail.com");

        ArrayList<String> toList2 = new ArrayList<>();
        toList2.add("testersutrecht@gmail.com");
        toList2.add("testersutrecht2@gmail.com");

        sendWithSMTPServer(subject, "De inhoud van deze mail.",
                "testersutrecht@gmail.com", toList2, "mailServer2",
                "mailServerProfileSecond", "testersutrecht", "geenidee2");

        sendWithSMTPServer(subject, "De inhoud van deze mail.",
                "testersutrecht@gmail.com", toList, "mailServer",
                "mailServerProfile", "testersutrecht", "geenidee2");

        addInstruction("", "Mail filter", "MailFilter");
        addInstruction("", "To", "^testersutrecht@.*$");
        addInstruction("", "To", "^.*2.*$");
        addInstruction("", "End mail filter");

        openIMAPServer("mailServer3", "mailServerProfile2", "testersutrecht", "geenidee2");
        addInstruction("", "Copy to", "to");
        addInstruction("", "Echo", "$to");

        closeMailBoxAndIMAPServer("mailServer3");

        execute();

        assertSystemOut("testersutrecht@gmail.com, testersutrecht2@gmail.com");
        assertError(0);
    }

    @Test()
    public void testOpenMostRecentMailFilterTo() throws Exception {
        String subject = new Date().toString();

        ArrayList<String> toList = new ArrayList<>();
        toList.add("testersutrecht@gmail.com");

        ArrayList<String> toList2 = new ArrayList<>();
        toList2.add("testersutrecht@gmail.com");
        toList2.add("testersutrecht2@gmail.com");

        sendWithSMTPServer(subject, "De inhoud van deze mail.",
                "testersutrecht@gmail.com", toList2, "mailServer2",
                "mailServerProfileSecond", "testersutrecht", "geenidee2");

        sendWithSMTPServer(subject, "De inhoud van deze mail.",
                "testersutrecht@gmail.com", toList, "mailServer",
                "mailServerProfile", "testersutrecht", "geenidee2");

        addInstruction("", "Mail filter", "MailFilter");
        addInstruction("", "To", "^testersutrecht@gmail.com$");
        addInstruction("", "To", "^testersutrecht2@gmail.com$");
        addInstruction("", "End mail filter");

        openIMAPServer("mailServer3", "mailServerProfile2", "testersutrecht", "geenidee2");
        addInstruction("", "Copy to", "to");
        addInstruction("", "Echo", "$to");

        closeMailBoxAndIMAPServer("mailServer3");

        execute();

        assertSystemOut("testersutrecht@gmail.com, testersutrecht2@gmail.com");
        assertError(0);
    }




    @Test(expected = ProcessorRunningException.class)
    public void testOpenMostRecentMailNoMailFits() throws Exception {
        String date = new Date().toString();
        String subject = date;
        String subject2 = date + "1";
        ArrayList<String> toList = new ArrayList<>();
        toList.add("testersutrecht@gmail.com");

        sendWithSMTPServer(subject, "De inhoud van deze email.",
                "testersutrecht@gmail.com", toList, "mailServer",
                "mailServerProfile", "testersutrecht", "geenidee2");

        sendWithSMTPServer(subject2, "De inhoud van deze email.",
                "testersutrecht@gmail.com", toList, "mailServer2",
                "mailServerProfileSecond", "testersutrecht", "geenidee2");

        addInstruction("", "Mail filter", "MailFilter");
        addInstruction("", "Subject", "^" + date + "2$");
        addInstruction("", "End mail filter");

        openIMAPServer("mailServer3", "mailServerProfile2", "testersutrecht", "geenidee2");
        addInstruction("", "Copy subject", "subject");
        addInstruction("", "Echo", "$subject");

        closeMailBoxAndIMAPServer("mailServer3");

        execute();

        assertSystemOut(subject);
    }

    @Test
    public void testOpenMostRecentMailNoMailFilterIdentifier() throws Exception {
        setIMAPServerProfile("mailServerProfile");
        addInstruction("", "Connect to imap mail server", "mailServer", "mailServerProfile", "testersutrecht", "geenidee2");
        addInstruction("", "Open mailbox", "mailBoxId", "mailServer", "INBOX");
        addInstruction("", "Open most recent mail", "mailBoxId", "");
        addInstruction("", "Close most recent mail");
        addInstruction("", "Close mailbox", "mailBoxId");
        addInstruction("", "Close connection to imap mail server", "mailServer");
        execute();
    }

    @Test(expected = ProcessorRunningException.class)
    public void testOpenMostRecentMailNoMailBoxIdentifier() throws Exception {
        setIMAPServerProfile("mailServerProfile");
        addInstruction("", "Connect to mail server", "mailServer", "mailServerProfile", "testersutrecht", "geenidee2");
        addInstruction("", "Open mailbox", "mailBoxId", "mailServer", "INBOX");
        addInstruction("", "Open most recent mail", "", "");
        addInstruction("", "Close most recent mail");
        addInstruction("", "Close mailbox", "mailBoxId");
        addInstruction("", "Close connection to mail server", "mailServer");
        execute();
    }

    @Test(expected = ProcessorRunningException.class)
    public void testOpenMostRecentMailWrongMailBoxIdentifier() throws Exception {
        setIMAPServerProfile("mailServerProfile");
        addInstruction("", "Connect to mail server", "mailServer", "mailServerProfile", "testersutrecht", "geenidee2");
        addInstruction("", "Open mailbox", "mailBoxId", "mailServer", "INBOX");
        addInstruction("", "Open most recent mail", "mailBoxId2", "");
        addInstruction("", "Close most recent mail");
        addInstruction("", "Close mailbox", "mailBoxId");
        addInstruction("", "Close connection to mail server", "mailServer");
        execute();
    }

    @Test(expected = ProcessorRunningException.class)
    public void testOpenMostRecentMailWrongMailFilterIdentifier() throws Exception {
        setIMAPServerProfile("mailServerProfile");
        addInstruction("", "Connect to mail server", "mailServer", "mailServerProfile", "testersutrecht", "geenidee2");
        addInstruction("", "Open mailbox", "mailBoxId", "mailServer", "INBOX");
        addInstruction("", "Mail filter", "filter");
        addInstruction("", "End mail filter", "");
        addInstruction("", "Open most recent mail", "mailBoxId", "filter2");
        addInstruction("", "Close most recent mail");
        addInstruction("", "Close mailbox", "mailBoxId");
        addInstruction("", "Close connection to mail server", "mailServer");
        execute();
    }

    @Test(expected = ProcessorRunningException.class)
    public void testCloseMostRecentMail() throws Exception {
        setIMAPServerProfile("mailServerProfile");
        addInstruction("", "Connect to imap mail server", "mailServer", "mailServerProfile", "testersutrecht", "geenidee2");
        addInstruction("", "Open mailbox", "mailBoxId", "mailServer", "INBOX");
        addInstruction("", "Open most recent mail", "mailBoxId", "");
        addInstruction("", "Close most recent mail");
        addInstruction("", "Copy subject", "subject");
        addInstruction("", "Close mailbox", "mailBoxId");
        addInstruction("", "Close connection to imap mail server", "mailServer");
        execute();
    }

    @Test
    public void testCloseMostRecentMailActiveLibrary() throws Exception {
        setIMAPServerProfile("mailServerProfile");
        addInstruction("", "Connect to imap mail server", "mailServer", "mailServerProfile", "testersutrecht", "geenidee2");
        addInstruction("", "Open mailbox", "mailBoxId", "mailServer", "INBOX");
        addInstruction("", "Open most recent mail", "mailBoxId", "");
        execute();
        MailLibrary lib = fetchLibrary(MailLibrary.class);
        assertEquals(false, lib.getActiveMostRecentMailLibrary() == null);
        addInstruction("", "Close most recent mail");
        execute();
        assertEquals(true, lib.getActiveMostRecentMailLibrary() == null);
        addInstruction("", "Close mailbox", "mailBoxId");
        addInstruction("", "Close connection to imap mail server", "mailServer");
        execute();
    }
}
