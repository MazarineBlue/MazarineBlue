/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mazarineblue.mail.keywords;

import java.util.ArrayList;
import org.mazarineblue.keyworddriven.Keyword;
import org.mazarineblue.keyworddriven.exceptions.KeywordConflictException;
import org.mazarineblue.keyworddriven.librarymanager.Library;

/**
 *
 * @author Mandy Tak {@literal <mandy.tak@MazarineBlue.org>}
 */
public class ComposeMailLibrary extends Library {

    private String from;
    private ArrayList<String> to = new ArrayList<String>();
    private String subject;
    private String body;

    public ComposeMailLibrary() throws KeywordConflictException {
        super("org.mazarineblue.mail");
    }

    @Override
    protected void setup() {

    }

    @Override
    protected void teardown() {
    }

    @Keyword("From")
    public void setFrom(String from) {
        this.from = from;
    }

    @Keyword("To")
    public void addTo(String to) {
        this.to.add(to);
    }

    @Keyword("Subject")
    public void setSubject(String subj) {
        subject = subj;
    }

    @Keyword("Body")
    public void setBody(String body) {
        this.body = body;
    }

    public String getFrom() {
        return from;
    }

    public ArrayList<String> getTo() {
        return to;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }
}
