/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mazarineblue.mail;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Store;
import org.mazarineblue.keyworddriven.exceptions.InstructionException;

/**
 *
 * @author Mandy Tak {@literal <mandy.tak@MazarineBlue.org>}
 */
public class ImapServer implements AutoCloseable {
// @TODO spliten in interface en impl.

    private final Store emailStore;

    public ImapServer(Store emailStore) {
        this.emailStore = emailStore;
    }

    public Mailbox openMailbox(String mailbox) {
        try {
            Folder folder = emailStore.getFolder(mailbox);
            folder.open(Folder.READ_ONLY);
            return new Mailbox(folder);
        } catch (MessagingException ex) {
            throw new InstructionException("Could not find or open mailbox: " + mailbox, ex);
        }
    }

    @Override
    public void close() throws Exception {
        emailStore.close();
    }
}
