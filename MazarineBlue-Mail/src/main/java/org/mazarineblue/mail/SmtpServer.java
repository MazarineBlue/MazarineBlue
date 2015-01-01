/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mazarineblue.mail;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import org.mazarineblue.keyworddriven.exceptions.InstructionException;

/**
 *
 * @author Mandy Tak {@literal <mandy.tak@MazarineBlue.org>}
 */
public class SmtpServer implements AutoCloseable {

    private final Session emailSession;

    public SmtpServer(Session emailSession) {
        this.emailSession = emailSession;
    }

    public void sendMail(Mail mail) {
        if (mail.hasMessage() == false)
            mail.createMessage(emailSession);
        try {
            Transport.send(mail.getMessage());
        } catch (MessagingException ex) {
            throw new InstructionException("Something went wrong when trying to send the email message", ex);
        }
    }

    // @TODO deze methode mag niet bestaan
    public Session getEmailSession() {
        return emailSession;
    }

    @Override
    public void close() throws Exception {
        // @TODO hier zal iets mee moeten worden gedaan.
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
