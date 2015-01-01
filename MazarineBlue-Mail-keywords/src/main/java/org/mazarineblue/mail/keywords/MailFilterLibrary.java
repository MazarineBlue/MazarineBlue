/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mazarineblue.mail.keywords;

import javax.mail.search.SearchTerm;
import org.mazarineblue.keyworddriven.Keyword;
import org.mazarineblue.keyworddriven.exceptions.KeywordConflictException;
import org.mazarineblue.keyworddriven.librarymanager.Library;
import org.mazarineblue.mail.MailFilter;

/**
 *
 * @author Mandy Tak {@literal <mandy.tak@MazarineBlue.org>}
 */
public class MailFilterLibrary extends Library {

    private final String identifier;
    private final MailFilter mailFilter;

    public MailFilterLibrary(String identifier) throws KeywordConflictException {
        super("org.mazarineblue.mail");
        this.identifier = identifier;
        mailFilter = new MailFilter();
    }

    @Override
    protected void setup() {
    }

    @Override
    protected void teardown() {
    }

    public String getIdentifier() {
        return identifier;
    }

    @Keyword("To")
    public void addTo(String recipient) {
        mailFilter.addTo(recipient);
    }

    @Keyword("From")
    public void setFrom(String from) {
        mailFilter.setFrom(from);
    }

    @Keyword("Subject")
    public void setSubject(String subject) {
        mailFilter.setSubject(subject);
    }

    @Keyword("Body")
    public void setBody(String body) {
        mailFilter.setBody(body);
    }


    public SearchTerm getSearchCondition() {
        return mailFilter.getSearchCondition();
    }

}
