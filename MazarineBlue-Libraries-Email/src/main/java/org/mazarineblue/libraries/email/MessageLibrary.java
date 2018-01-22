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

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.Date;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import org.mazarineblue.eventdriven.Invoker;
import org.mazarineblue.keyworddriven.Keyword;
import org.mazarineblue.keyworddriven.Library;
import org.mazarineblue.keyworddriven.Parameters;
import org.mazarineblue.keyworddriven.PassInvoker;
import org.mazarineblue.keyworddriven.events.AddLibraryEvent;
import org.mazarineblue.keyworddriven.events.RemoveLibraryEvent;
import static org.mazarineblue.libraries.email.EmailLibrary.doesInvokerContainsLibraries;
import static org.mazarineblue.libraries.email.EmailLibraryPlugin.NAMESPACE;
import org.mazarineblue.libraries.email.exceptions.ContentNotFoundException;
import org.mazarineblue.libraries.email.exceptions.EmailBackendException;
import org.mazarineblue.libraries.email.exceptions.MessageAttachmentNotFoundException;
import org.mazarineblue.libraries.email.util.Attachment;
import org.mazarineblue.utilities.exceptions.NeverThrownException;

public class MessageLibrary
        extends Library {

    private static final int ATTACHMENT_COUNT_UNDETERMINED = -1;

    private SoftReference<Attachment[]> attachments = new SoftReference<>(null);
    private int attachmentCount = ATTACHMENT_COUNT_UNDETERMINED;
    private final Message message;
    private AttachmentLibrary attachmentLibrary;

    public MessageLibrary(Message message) {
        super(NAMESPACE);
        this.message = message;
    }

    @Keyword("Close message")
    @PassInvoker
    public void closeMessage(Invoker invoker) {
        invoker.publish(new RemoveLibraryEvent(this));
    }

    @Override
    protected void doTeardown(Invoker invoker) {
        if (attachmentLibrary == null || !doesInvokerContainsLibraries(invoker, AttachmentLibrary.class))
            return;
        invoker.publish(new RemoveLibraryEvent(attachmentLibrary));
        attachmentLibrary = null;
    }

    @Keyword("Get message number")
    public int getMessageNumber() {
        return message.getMessageNumber() - 1;
    }

    @Keyword("Get send date of message")
    public Date getSendDate() {
        try {
            return message.getSentDate();
        } catch (MessagingException ex) {
            throw new EmailBackendException(ex);
        }
    }

    @Keyword("Get received date of message")
    public Date getReceivedDate() {
        try {
            return message.getReceivedDate();
        } catch (MessagingException ex) {
            throw new EmailBackendException(ex);
        }
    }

    @Keyword("Get subject of message")
    public String getSubject() {
        try {
            return message.getSubject();
        } catch (MessagingException ex) {
            throw new EmailBackendException(ex);
        }
    }

    @Keyword("Get plain text of message")
    public String getTextPlain() {
        try {
            Object content = message.getContent();
            return content instanceof Multipart
                   ? firstBodyPart((Multipart) content, "text/plain").getContent().toString()
                   : content.toString();
        } catch (IOException | MessagingException ex) {
            throw new EmailBackendException(ex);
        }
    }

    @Keyword("Get html of message")
    public String getTextHtml() {
        try {
            Object content = message.getContent();
            if (content instanceof Multipart)
                return firstBodyPart((Multipart) content, "text/html").getContent().toString();
            throw new ContentNotFoundException();
        } catch (IOException | MessagingException ex) {
            throw new EmailBackendException(ex);
        }
    }

    private BodyPart firstBodyPart(Multipart multiPart, String contentType)
            throws MessagingException {
        for (int i = 0; i < multiPart.getCount(); ++i) {
            BodyPart bodyPart = multiPart.getBodyPart(i);
            if (contentType.equals(bodyPart.getContentType()))
                return bodyPart;
        }
        throw new ContentNotFoundException();
    }

    @Keyword("Select attachment with filename")
    @Parameters(min = 1)
    @PassInvoker
    public void selectAttachmentWithFilename(Invoker invoker, String pattern) {
        try {
            Attachment found = findAttachementWithFilenamePattern(pattern);
            if (found == null)
                throw new MessageAttachmentNotFoundException();
            attachmentLibrary = new AttachmentLibrary(found);
            invoker.publish(new AddLibraryEvent(attachmentLibrary));
        } catch (IOException | MessagingException ex) {
            throw new EmailBackendException(ex);
        }
    }

    private Attachment findAttachementWithFilenamePattern(String pattern)
            throws MessagingException, IOException {
        for (Attachment attachment : getAttachments(message.getContent()))
            if (attachment.getFilename().contains(pattern))
                return attachment;
        return null;
    }

    @Keyword("Select attachment at index")
    @Parameters(min = 1)
    @PassInvoker
    public void selectAttachment(Invoker invoker, int index) {
        try {
            Attachment[] arr = getAttachments(message.getContent());
            if (index >= arr.length)
                throw new MessageAttachmentNotFoundException();
            attachmentLibrary = new AttachmentLibrary(arr[index]);
            invoker.publish(new AddLibraryEvent(attachmentLibrary));
        } catch (IOException | MessagingException ex) {
            throw new EmailBackendException(ex);
        }
    }

    private Attachment[] getAttachments(Object content)
            throws MessagingException {
        Attachment[] arr = attachments.get();
        if (arr == null)
            arr = getAttachmentAndSetCache(content);
        return arr;
    }

    private Attachment[] getAttachmentAndSetCache(Object content)
            throws MessagingException {
        if (!(content instanceof Multipart))
            throw new MessageAttachmentNotFoundException();
        Attachment[] arr = collectAttachements((Multipart) content);
        attachments = new SoftReference<>(arr);
        return arr;
    }

    private Attachment[] collectAttachements(Multipart multiPart)
            throws MessagingException {
        AttachmentFetcher fetcher = new AttachmentFetcher(multiPart);
        Attachment[] arr = new Attachment[countAttachments()];
        for (int i = 0; i < arr.length; ++i)
            arr[i] = fetcher.getNextAttachement();
        return arr;
    }

    private class AttachmentFetcher {

        private final Multipart multiPart;
        private int bodyPartIndex = 0;

        private AttachmentFetcher(Multipart multiPart) {
            this.multiPart = multiPart;
        }

        private Attachment getNextAttachement()
                throws MessagingException {
            for (; bodyPartIndex < multiPart.getCount(); ++bodyPartIndex) {
                BodyPart bodyPart = multiPart.getBodyPart(bodyPartIndex);
                if (isAttachment(bodyPart))
                    return new Attachment(bodyPart);
            }
            throw new NeverThrownException();
        }
    }

    @Keyword("Count attachments")
    public int countAttachments() {
        try {
            Object content = message.getContent();
            return content instanceof Multipart
                   ? countAttachments((Multipart) content)
                   : 0;
        } catch (IOException | MessagingException ex) {
            throw new EmailBackendException(ex);
        }
    }

    private int countAttachments(Multipart multiPart)
            throws MessagingException {
        if (attachmentCount == ATTACHMENT_COUNT_UNDETERMINED)
            attachmentCount = countAttachmentsHelper(multiPart);
        return attachmentCount;
    }

    private int countAttachmentsHelper(Multipart multiPart)
            throws MessagingException {
        int count = 0;
        for (int i = 0; i < multiPart.getCount(); ++i)
            if (isAttachment(multiPart.getBodyPart(i)))
                ++count;
        return count;
    }

    private static boolean isAttachment(BodyPart bodyPart)
            throws MessagingException {
        return Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition());
    }
}
