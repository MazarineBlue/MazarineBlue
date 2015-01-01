package org.mazarineblue.mail.keywords;

import org.mazarineblue.keyworddriven.exceptions.KeywordConflictException;
import org.mazarineblue.keyworddriven.librarymanager.Library;

/**
 *
 * @author Mandy Tak {@literal <mandy.tak@MazarineBlue.org>}
 */
public class ConnectionLibrary extends Library {

    public ConnectionLibrary() throws KeywordConflictException {
        super("org.mazarineblue.mail");
    }

    @Override
    protected void setup() {
    }

    @Override
    protected void teardown() {
    }
}
