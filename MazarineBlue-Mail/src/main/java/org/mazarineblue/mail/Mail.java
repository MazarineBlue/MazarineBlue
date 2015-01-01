/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mazarineblue.mail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import javax.mail.Address;
import javax.mail.Header;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.mazarineblue.keyworddriven.exceptions.InstructionException;

/**
 *
 * @author Mandy Tak {@literal <mandy.tak@MazarineBlue.org>}
 */

public class Mail {
    private String from, subject, body;
    private ArrayList<String> cc;
    private ArrayList<String> bcc;
    private ArrayList<String> to = new ArrayList<>();
    private Message message; // @TODO niet in deze class

// @TODO Collection boven List, List boven ArrayList!
    public Mail(String from, ArrayList<String> to, String subject, String body) {
        this.from = from;
        this.to.addAll(to); // @TODO geef de voorkeur aan new ArrayList(to);
        this.subject = subject;
        this.body = body;
    }

    public Mail(Message message) {
        this.message = message;
// @TODO waarden uit de message halen
    }

    public String getFrom() {
	return from;
    }

    public String getSubject() {
	return subject;
    }

    public String getBody() {
	return body;
    }

    public void addCc(String cc) {
        if (this.cc == null)
	    cc = new ArrayList<>();
	this.cc.add(cc);
    }

    public List<String> getCc() {
	if (cc == null)
	    return Collections.EMPTY_LIST;
	return new ArrayList<>(cc)
    }

    public void addBcc(String bcc) {
        if (this.bcc == null)
	    bcc = new ArrayList<>();
	this.bcc.add(bcc);
    }

    public List<String> getBcc() {
	if (bcc == null)
	    return Collections.EMPTY_LIST;
	return new ArrayList<>(bcc);
    }

// @TODO verwijderen
    public Message getMessage() {
        return message;
    }

// @TODO verwijderen
    public boolean hasMessage() {
        return message != null;
    }

// @TODO verwijderen
    public void createMessage(Session session) {
        message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(from));
            for (String recipient : to)
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject(subject);
            message.setText(body);
        } catch (MessagingException ex) {
            throw new InstructionException("Something went wrong when trying to send the email message", ex);
        }
    }

// @TODO verwijderen
    public String getMessageSubject() {
        try {
            if (message == null)
                throw new InstructionException("There is no message to copy the subject from.");
            return message.getSubject();
        } catch (MessagingException ex) {
            throw new InstructionException("The subject of the email message could not be read", ex);
        }
    }

// @TODO verwijderen
    public String getMessageBody() {
        try {
            if (message == null)
                throw new InstructionException("There is no message to copy the body from.");
            return (String) message.getContent();
        } catch (IOException | MessagingException ex) {
            throw new InstructionException("The body of the email message could not be read", ex);
        }
    }

// @TODO verwijderen
    public String getMessageToAsString() {
        if (message == null)
            throw new InstructionException("There is no message to copy the to from.");

        try {
            Address[] recipients = message.getRecipients(Message.RecipientType.TO);
            String to = recipients[0].toString();
            for (int i = 1; i < recipients.length; i++)
                to = to + ", " + recipients[i].toString();
            return to;
        } catch (MessagingException ex) {
            throw new InstructionException("The TO recipients of the email message could not be read", ex);
        }
    }

// @TODO verwijderen
    public ArrayList<String> getMessageToAsList() {
        try {
            Address[] recipients = message.getRecipients(Message.RecipientType.TO);
            ArrayList<String> stringList = new ArrayList<>();
            for (Address recipient : recipients)
                stringList.add(recipient.toString());
            return stringList;
        } catch (MessagingException ex) {
            throw new InstructionException("The TO recipients of the email message could not be read", ex);
        }
    }

// @TODO verwijderen
    public Address[] getMessageToAsAddresses() {
        try {
            return message.getRecipients(Message.RecipientType.TO);
        } catch (MessagingException ex) {
            throw new InstructionException("The TO recipients of the email message could not be read", ex);
        }
    }

// @TODO verwijderen
    public String getMessageFrom() {
        if (hasMessage() == false)
            throw new InstructionException("There is no message to copy the FROM from.");
        return determineMessageFrom();
    }

// @TODO verwijderen
    private String determineMessageFrom() {
        try {
            Address[] froms = message.getFrom();
            return (froms == null) ? null : ((InternetAddress) froms[0]).getAddress();
        } catch (MessagingException ex) {
            throw new InstructionException("The from of the email message could not be read", ex);
        }
    }

// @TODO verwijderen
    public String getMessageHeaderAsString() {
        try {
            String completeHeader = "";
            Enumeration headers = message.getAllHeaders();
            while (headers.hasMoreElements()) {
                Header h = (Header) headers.nextElement();
                completeHeader = completeHeader + h.getName() + ": " + h.getValue();
            }
            return completeHeader;
        } catch (MessagingException ex) {
            throw new InstructionException("Error occurred while reading the email header", ex);
        }
    }

// @TODO verwijderen
    public Enumeration getMessageHeader() {
        try {
            return message.getAllHeaders();
        } catch (MessagingException ex) {
            throw new InstructionException("Error occurred while reading the email header", ex);
        }
    }
}
