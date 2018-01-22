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
package org.mazarineblue.libraries.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Enumeration;
import javax.activation.DataHandler;
import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Header;
import javax.mail.Message;
import static javax.mail.Message.RecipientType.BCC;
import static javax.mail.Message.RecipientType.CC;
import static javax.mail.Message.RecipientType.TO;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

public class TestMessage
        extends Message {

    private Address[] replyTo = new Address[0];
    private Address[] from = new Address[0];
    private Address[] to = new Address[0];
    private Address[] cc = new Address[0];
    private Address[] bcc = new Address[0];
    private Date sendDate = new Date(0);
    private Date recievedDate = new Date(0);
    private Flags flags = new Flags();
    private String subject = "";
    private Object content;

    public TestMessage() {
    }

    public TestMessage(Folder folder, int msgnum) {
        super(folder, msgnum);
    }

    public TestMessage(Session session) {
        super(session);
    }

    @Override
    public Address[] getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(String address)
            throws AddressException {
        setReplyTo(new InternetAddress(address));
    }

    public void setReplyTo(Address address) {
        replyTo = new Address[1];
        replyTo[0] = address;
    }

    @Override
    public Address[] getFrom() {
        return from;
    }

    @Override
    public void setFrom() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setFrom(String address)
            throws AddressException {
        setFrom(new InternetAddress(address));
    }

    @Override
    public void setFrom(Address address) {
        from = new Address[1];
        from[0] = address;
    }

    @Override
    public void addFrom(Address[] addresses) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Address[] getRecipients(RecipientType type) {
        if (type.equals(TO))
            return to;
        if (type.equals(CC))
            return cc;
        if (type.equals(BCC))
            return bcc;
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setRecipient(RecipientType type, String address)
            throws AddressException {
        Address[] addresses = new Address[1];
        addresses[0] = new InternetAddress(address);
        setRecipients(type, addresses);
    }

    @Override
    public void setRecipients(RecipientType type, Address[] addresses) {
        if (type.equals(TO))
            to = addresses;
        else if (type.equals(CC))
            cc = addresses;
        else if (type.equals(BCC))
            bcc = addresses;
        else
            throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addRecipients(RecipientType type, Address[] addresses) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getSubject() {
        return subject;
    }

    @Override
    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public Date getSentDate() {
        return sendDate;
    }

    @Override
    public void setSentDate(Date date) {
        sendDate = date;
    }

    @Override
    public Date getReceivedDate() {
        return recievedDate;
    }

    public void setReceivedDate(Date date) {
        this.recievedDate = date;
    }

    @Override
    public Flags getFlags() {
        return flags;
    }

    @Override
    public void setFlags(Flags flag, boolean set) {
        if (set)
            flags.add(flag);
        else
            flags.remove(flag);
    }

    @Override
    public Message reply(boolean replyToAll) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void saveChanges() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getSize() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getLineCount() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getContentType() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isMimeType(String mimeType) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getDisposition() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setDisposition(String disposition) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getDescription() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setDescription(String description) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getFileName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setFileName(String filename) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public InputStream getInputStream()
            throws IOException, MessagingException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DataHandler getDataHandler() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object getContent()
            throws IOException, MessagingException {
        return content;
    }

    @Override
    public void setDataHandler(DataHandler dh) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setContent(Object content, String type)
            throws MessagingException {
        MimeBodyPart part = new MimeBodyPart();
        part.setContent(content, type);
        part.setHeader("Content-Type", type);
        this.content = new MimeMultipart(part);
    }

    @Override
    public void setText(String text) {
        this.content = text;
    }

    @Override
    public void setContent(Multipart mp) {
        this.content = mp;
    }

    @Override
    public void writeTo(OutputStream os)
            throws IOException, MessagingException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String[] getHeader(String header_name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setHeader(String header_name, String header_value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addHeader(String header_name, String header_value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removeHeader(String header_name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Enumeration<Header> getAllHeaders() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Enumeration<Header> getMatchingHeaders(String[] header_names) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Enumeration<Header> getNonMatchingHeaders(String[] header_names) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
