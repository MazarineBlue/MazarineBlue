/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mazarineblue.mail.keywords;

import java.util.Properties;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mazarineblue.LibraryTestTemplate;
import org.mazarineblue.keyworddriven.exceptions.InstructionException;
import org.mazarineblue.keyworddriven.exceptions.ProcessorRunningException;
import org.mazarineblue.mail.ServerProfile;


/**
 *
 * @author Mandy Tak {@literal <mandy.tak@MazarineBlue.org>}
 */
public class MailServerProfileTest extends LibraryTestTemplate {

    public MailServerProfileTest() {
    }

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
    public void testMailServerProfile() throws Exception {
        String profileName = "profileName";
        addInstruction("", "Mail server profile", profileName);
        execute();

        MailLibrary lib = fetchLibrary(MailLibrary.class);
        assertEquals(true, lib.isActiveServerProfile(profileName));
        addInstruction("", "End mail server profile");
        execute();
        assertEquals(false, lib.isActiveServerProfile(profileName));
        assertEquals(true, lib.containsMailServerProfile(profileName));
    }

    @Test(expected = ProcessorRunningException.class)
    public void testMailServerProfileIdentifierEmptyString() throws Exception {
        addInstruction("", "Mail server profile", "");
        addInstruction("", "Server", "imap.gmail.com:993");
        addInstruction("", "SSL", "yes");
        addInstruction("", "StartTLS", "yes");
        addInstruction("", "End mail server profile");
        execute();
    }

    @Test(expected = ProcessorRunningException.class)
    public void testMailServerProfileIdentifierAlreadyUsed() throws Exception {
        setIMAPServerProfile("mailServerProfile");
        setIMAPServerProfile("mailServerProfile");
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
    public void testMailServerProfileCalledTwice() throws Exception {
        for (int v = 0; v < 2; v++) {
            addInstruction("", "Mail server profile", "mailServerProfile2");
            addInstruction("", "Server", "imap.gmail.com:993");
            addInstruction("", "SSL", "yes");
            addInstruction("", "StartTLS", "yes");
        }
        addInstruction("", "End mail server profile");
        execute();
    }

    @Test(expected = ProcessorRunningException.class)
    public void testEndMailServerProfileWhenNotStarted() throws Exception {
        addInstruction("", "End mail server profile");
        execute();
    }

    @Test
    public void testSetServer() throws Exception {
        addInstruction("", "Mail server profile", "ID");
        addInstruction("", "Server", MailLibraryConstants.standardHost);
        addInstruction("", "SSL", "yes");
        addInstruction("", "StartTLS", "no");
        addInstruction("", "End mail server profile");
        execute();

        ServerProfileLibrary library = fetchLibrary(ServerProfileLibrary.class);
        Properties p = library.getProperties();
        String host = p.getProperty(library.getHostKey(MailLibraryConstants.standardProtocolSSL));
        String ssl = p.getProperty(library.getSSLKey(MailLibraryConstants.standardProtocolSSL));
        String startTLS = p.getProperty(library.getStartTLSKey(MailLibraryConstants.standardProtocolSSL));
        String protocol = p.getProperty(library.getProtocolKey());
        String key = library.getPortKey(MailLibraryConstants.standardProtocolSSL);
        String port = p.getProperty(key);

        assertEquals(host, MailLibraryConstants.standardHost);
        assertEquals(library.booleanConvertor(ssl), true);
        assertEquals(library.booleanConvertor(startTLS), false);
        assertEquals(MailLibraryConstants.standardProtocolSSL, protocol);
        assertEquals(port, Integer.toString(ServerProfile.imapSslPort));
    }

    @Test(expected = ProcessorRunningException.class)
    public void testSetServerEmptyServer() throws Exception {
        addInstruction("", "Mail server profile", "ID");
        addInstruction("", "Server", "");
        addInstruction("", "SSL", "no");
        addInstruction("", "StartTLS", "no");
        addInstruction("", "End mail server profile");
        execute();
    }

    @Test(expected = ProcessorRunningException.class)
    public void testSetServerEmptyHost() throws Exception {
        addInstruction("", "Mail server profile", "ID");
        addInstruction("", "Server", ":993");
        addInstruction("", "SSL", "no");
        addInstruction("", "StartTLS", "no");
        addInstruction("", "End mail server profile");
        execute();
    }

    @Test(expected = ProcessorRunningException.class)
    public void testSetServerWrongFormat() throws Exception {
        addInstruction("", "Mail server profile", "ID");
        addInstruction("", "Server", "imap.gmail.com:wrong:993");
        addInstruction("", "SSL", "no");
        addInstruction("", "StartTLS", "no");
        addInstruction("", "End mail server profile");
        execute();
    }

    @Test(expected = ProcessorRunningException.class)
    public void testSetPortIsNotNumerical() throws Exception {
        addInstruction("", "Mail server profile", "ID");
        addInstruction("", "Server", "imap.gmail.com:wrong");
        addInstruction("", "SSL", "no");
        addInstruction("", "StartTLS", "no");
        addInstruction("", "End mail server profile");
        execute();
    }

    @Test(expected = InstructionException.class)
    public void testGetPropertiesNoHost() throws Exception {
        addInstruction("", "Mail server profile", "ID");
        addInstruction("", "SSL", "yes");
        addInstruction("", "StartTLS", "no");
        addInstruction("", "End mail server profile");
        execute();

        ServerProfileLibrary library = fetchLibrary(ServerProfileLibrary.class);
        library.getProperties();
    }

    @Test
    public void testBooleanConverterYes() throws Exception {
        addInstruction("", "Mail server profile", "ID");
        addInstruction("", "End mail server profile");
        execute();

        ServerProfileLibrary library = fetchLibrary(ServerProfileLibrary.class);
        boolean result = library.booleanConvertor("yes");
        assertEquals(true, result);
    }

    @Test
    public void testBooleanConverterTrue() throws Exception {
        addInstruction("", "Mail server profile", "ID");
        addInstruction("", "End mail server profile");
        execute();

        ServerProfileLibrary library = fetchLibrary(ServerProfileLibrary.class);
        boolean result = library.booleanConvertor("true");
        assertEquals(true, result);
    }

    @Test
    public void testBooleanConverterNo() throws Exception {
        addInstruction("", "Mail server profile", "ID");
        addInstruction("", "End mail server profile");
        execute();

        ServerProfileLibrary library = fetchLibrary(ServerProfileLibrary.class);
        boolean result = library.booleanConvertor("no");
        assertEquals(false, result);
    }

    @Test
    public void testBooleanConverterFalse() throws Exception {
        addInstruction("", "Mail server profile", "ID");
        addInstruction("", "End mail server profile");
        execute();

        ServerProfileLibrary library = fetchLibrary(ServerProfileLibrary.class);
        boolean result = library.booleanConvertor("false");
        assertEquals(false, result);
    }

    @Test(expected = InstructionException.class)
    public void testBooleanConverterWrongInput() throws Exception {
        addInstruction("", "Mail server profile", "ID");
        addInstruction("", "End mail server profile");
        execute();

        ServerProfileLibrary library = fetchLibrary(ServerProfileLibrary.class);
        library.booleanConvertor("wrong");
    }
}
