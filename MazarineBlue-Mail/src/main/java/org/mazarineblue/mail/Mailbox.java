/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mazarineblue.mail;

import java.util.concurrent.TimeoutException;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.search.SearchTerm;
import org.mazarineblue.keyworddriven.exceptions.InstructionException;
import org.mazarineblue.mail.keywords.MessageNotFoundException;

/**
 *
 * @author Mandy Tak {@literal <mandy.tak@MazarineBlue.org>}
 */
public class Mailbox implements AutoCloseable {
// @TODO spliten in interface en impl.

    private final Folder folder;
    private final int timeout = 30000;

    public Mailbox(Folder folder) {
        this.folder = folder;
    }

    public void waitForMail(long millis) throws TimeoutException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Mail getMostRecentMail(MailFilter filter) throws InterruptedException, MessageNotFoundException {
        SearchTerm searchTerm = filter == null ? null : filter.getSearchCondition();
        Message message = getMail(folder, searchTerm);
        return new Mail(message);
    }

    private Message getMail(Folder folder, SearchTerm searchTerm) throws MessageNotFoundException, InterruptedException {
        return getMail(folder, System.currentTimeMillis() + timeout, searchTerm);
    }

    // @TODO kan samen worden gevoegd met bovenstaande methode
    private Message getMail(Folder folder, long endTime, SearchTerm searchTerm) throws MessageNotFoundException, InterruptedException {
        if (searchTerm == null)
            return getMail(folder, endTime);

        FetchMail fetchMail = new FetchMail() {
            @Override
            protected Message fetchMailInterval(Folder folder) throws MessagingException {
                Message[] messages = folder.search(searchTerm);
                if (messages.length > 0)
                    return messages[messages.length - 1];
                return null;
            }
        };
        return fetchMail.fetchMail(folder, endTime);
    }

    private abstract class FetchMail {
        private Message fetchMail(Folder folder, long endTime) throws MessageNotFoundException, InterruptedException {
            try {
                long endIntervalTime = System.currentTimeMillis() + timeout / 10;
                while (System.currentTimeMillis() < endTime) {
                    Message msg = fetchMailInterval(folder);
                    if (msg != null)
                        return msg;
                    if (System.currentTimeMillis() < endIntervalTime)
                        Thread.sleep(endIntervalTime - System.currentTimeMillis());
                }
            } catch (MessagingException ex) {
                throw new InstructionException("Developer issue: IO error while reading mail folder");
            }
            throw new MessageNotFoundException("Could not find the message in " + timeout + " ms");
        }

        protected abstract Message fetchMailInterval(Folder folder) throws MessagingException;
    }

    private Message getMail(Folder folder, long endTime) throws MessageNotFoundException, InterruptedException {
        FetchMail fetchMail = new FetchMail() {
            @Override
            protected Message fetchMailInterval(Folder folder) throws MessagingException {
                if (folder.getMessageCount() > 0)
                    return folder.getMessage(folder.getMessageCount());
                return null;
            }
        };
        return fetchMail.fetchMail(folder, endTime);
    }

    @Override
    public void close() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
