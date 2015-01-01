/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mazarineblue.mail.keywords;

import java.util.Enumeration;
import javax.mail.Message;
import org.mazarineblue.keyworddriven.Keyword;
import org.mazarineblue.keyworddriven.exceptions.InstructionException;
import org.mazarineblue.keyworddriven.exceptions.KeywordConflictException;
import org.mazarineblue.keyworddriven.librarymanager.Library;
import org.mazarineblue.mail.Mail;

/**
 *
 * @author Mandy Tak {@literal <mandy.tak@MazarineBlue.org>}
 */
public class OpenMostRecentMailLibrary extends Library {

    private Mail mail;

    public OpenMostRecentMailLibrary() throws KeywordConflictException {
        super("org.mazarineblue.mail");
    }

    OpenMostRecentMailLibrary(Message message) throws KeywordConflictException {
        super("org.mazarineblue.mail");
        mail = new Mail(message);
    }

    @Override
    protected void setup() {

    }

    @Override
    protected void teardown() {
    }

    @Keyword("Copy subject")
    public void copySubject(String subjectKey) {
        String subject;
        if (mail == null)
            throw new InstructionException("There is no message to copy the subject from.");
        subject = mail.getMessageSubject();
        blackboard().set(subjectKey, subject);
    }

    @Keyword("Copy body")
    public void copyBody(String bodyKey) {
        String body;
        if (mail == null)
            throw new InstructionException("There is no message to copy the body from.");
        body = (String) mail.getMessageBody();
        blackboard().set(bodyKey, body);
    }

    @Keyword("Copy to")
    public void copyTo(String toKey) {
        if (mail == null)
            throw new InstructionException("There is no message to copy the to from.");
        blackboard().set(toKey, mail.getMessageToAsString());
    }

    @Keyword("Copy from")
    public void copyFrom(String fromKey) {
        if (mail == null)
            throw new InstructionException("There is no message to copy the FROM from.");
        blackboard().set(fromKey, mail.getMessageFrom());
    }

    @Keyword("Copy header")
    public void copyHeader(String headerKey) {
        blackboard().set(headerKey, mail.getMessageHeaderAsString());
    }

    public Enumeration getHeader() {
        return mail.getMessageHeader();
    }

    public Message getMessage() {
        return mail.getMessage();
    }
}
