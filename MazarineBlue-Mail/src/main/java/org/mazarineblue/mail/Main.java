/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mazarineblue.mail;

import java.util.Properties;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import org.mazarineblue.keyworddriven.exceptions.InstructionException;

/**
 *
 * @author Mandy Tak {@literal <mandy.tak@MazarineBlue.org>}
 */
public class Main {

    public static String standardServer = "imap.gmail.com:993";
    public static String standardSSL = "yes";
    public static String standardStartTLS = "yes";

    public static String standardSendServer = "smtp.gmail.com:587";
    public static String standardSendSSL = "no";
    public static String standardSendStartTLS = "yes";

    public static String standardHost = "imap.gmail.com";
    public static String standardProtocolSSL = "imaps";

    public static SmtpServer connectToSMTPServer(ServerProfile profile, String username, String password) {
// @TODO return new SmtpServer(profile, username, password);

        Session session = getEmailSession(profile, username, password, "smtp");
        return new SmtpServer(session);
    }

    public static ImapServer connectToImapServer(ServerProfile profile, String username, String password) {
// @TODO return new ImapServer(profile, username, password);

        try {
            Session session = getEmailSession(profile, username, password, "imap");
            Properties properties = profile.getProperties();
            String protocol = properties.getProperty("mail.store.protocol");
            Store store = session.getStore(protocol);
            store.connect(profile.getHost(), username, password);
            return new ImapServer(store);
        } catch (NoSuchProviderException ex) {
	    // @TODO In deze module gooien geen InstructionException op, want die is alleen voor de interpreter, maar gooien we onze eigen waardevolle excepties op. -- verplaatsen naar ImapServer
            throw new InstructionException("Cannot retrieve the email store using protocol: " + protocol, ex);
        } catch (MessagingException ex) {
            throw new InstructionException("Cannot connect to the mail server with host: " + profile.getHost(), ex);
        }
    }

    // @TODO methode is overbodig
    private static Session getEmailSession(ServerProfile profile, String username, String password, String protocol) {
	// @TODO andere exception opgooien
        if (username == null || username.isEmpty())
            throw new InstructionException("No username is specified.");
        if (password == null || password.isEmpty())
            throw new InstructionException("No password is specified.");

        profile.setProtocol(protocol); // @TODO deze zou al gezet moeten zijn
        return createEmailSession(profile, username, password);
    }

    // @TODO code onder de methode waar deze voor het eerst aangeroepen wordt.
    private static Session createEmailSession(ServerProfile profile, String username, String password) {
        System.setProperty("mail.mime.decodetext.strict", "false");
        Session session = null; // @TODO overbodig
        session = Session.getInstance(profile.getProperties(), new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        session.setDebug(false);
        return session;
    }
}
