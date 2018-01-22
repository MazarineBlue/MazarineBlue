/*
 * Copyright (c) 2018 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.mazarineblue.libraries.email;

import static java.util.Arrays.stream;
import java.util.Comparator;
import java.util.List;
import static java.util.stream.Collectors.toList;
import java.util.stream.Stream;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import org.mazarineblue.eventdriven.Invoker;
import org.mazarineblue.keyworddriven.Keyword;
import org.mazarineblue.keyworddriven.Library;
import org.mazarineblue.keyworddriven.Parameters;
import org.mazarineblue.keyworddriven.PassInvoker;
import org.mazarineblue.keyworddriven.events.AddLibraryEvent;
import org.mazarineblue.keyworddriven.events.RemoveLibraryEvent;
import static org.mazarineblue.libraries.email.EmailLibrary.doesInvokerContainsLibraries;
import static org.mazarineblue.libraries.email.EmailLibraryPlugin.NAMESPACE;
import org.mazarineblue.libraries.email.exceptions.EmailBackendException;
import org.mazarineblue.libraries.email.exceptions.MessageNotFoundException;
import org.mazarineblue.libraries.email.util.Range;

public class FolderLibrary
        extends Library {

    private final FilterLibrary filter = new FilterLibrary();
    private final Folder folder;
    private final Range range;
    private MessageLibrary messageLibrary;
        
    public FolderLibrary(Folder folder, Range range) {
        super(NAMESPACE);
        this.folder = folder;
        this.range = range;
        openFolder();
    }

    private void openFolder() {
        try {
            folder.open(Folder.READ_ONLY);
        } catch (MessagingException ex) {
            throw new EmailBackendException(ex);
        }
    }

    @Override
    public void doSetup(Invoker invoker) {
        invoker.publish(new AddLibraryEvent(filter));
    }

    @Keyword("Close mail folder")
    @PassInvoker
    public void closeFolder(Invoker invoker) {
        invoker.publish(new RemoveLibraryEvent(this));
    }

    @Override
    protected void doTeardown(Invoker invoker) {
        invoker.publish(new RemoveLibraryEvent(filter));
        if (messageLibrary == null || !doesInvokerContainsLibraries(invoker, MessageLibrary.class))
            return;
        invoker.publish(new RemoveLibraryEvent(messageLibrary));
        closeFolder();
    }

    private void closeFolder() {
        try {
            folder.close();
        } catch (MessagingException ex) {
            throw new EmailBackendException(ex);
        }
    }

    @Keyword("Count messages")
    public int countMessages() {
        return (int) getMessagesStream()
                .filter(filter)
                .count();
    }

    @Keyword("Open first message")
    @PassInvoker
    public void openFirstMessage(Invoker invoker) {
        openMessage(invoker, getMessagesStream()
                    .filter(filter)
                    .min(naturalOrder())
                    .orElse(null));
    }

    @Keyword("Open message at index")
    @PassInvoker
    @Parameters(min = 1)
    public void openMailAtIndex(Invoker invoker, int index) {
        openMessage(invoker, getMessage(index));
    }

    private Message getMessage(int index) {
        List<Message> list = getMessagesStream()
                .filter(filter)
                .collect(toList());
        if (index >= list.size())
            return null;
        list.sort(naturalOrder());
        return list.get(index);
    }

    @Keyword("Open last message")
    @PassInvoker
    public void openLastMessage(Invoker invoker) {
        openMessage(invoker, getMessagesStream()
                    .max(naturalOrder())
                    .orElse(null));
    }

    public Stream<Message> getMessagesStream() {
        try {
            Message[] messages = folder.getMessages(range.start(), range.end());
            return stream(messages);
        } catch (MessagingException ex) {
            throw new EmailBackendException(ex);
        }
    }

    private static Comparator<Message> naturalOrder() {
        return (left, right) -> left.getMessageNumber() - right.getMessageNumber();
    }

    private void openMessage(Invoker invoker, Message msg) {
        if (msg == null)
            throw new MessageNotFoundException();
        messageLibrary = new MessageLibrary(msg);
        invoker.publish(new AddLibraryEvent(messageLibrary));
    }
}
