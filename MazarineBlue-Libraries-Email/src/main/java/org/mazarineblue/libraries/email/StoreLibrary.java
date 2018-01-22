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

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Store;
import org.mazarineblue.eventdriven.Invoker;
import org.mazarineblue.keyworddriven.Keyword;
import org.mazarineblue.keyworddriven.Library;
import org.mazarineblue.keyworddriven.PassInvoker;
import org.mazarineblue.keyworddriven.events.AddLibraryEvent;
import org.mazarineblue.keyworddriven.events.RemoveLibraryEvent;
import static org.mazarineblue.libraries.email.EmailLibrary.doesInvokerContainsLibraries;
import static org.mazarineblue.libraries.email.EmailLibraryPlugin.NAMESPACE;
import org.mazarineblue.libraries.email.exceptions.EmailBackendException;
import org.mazarineblue.libraries.email.util.Range;

public class StoreLibrary
        extends Library {

    private final Store store;
    private FolderLibrary folderLibrary;

    public StoreLibrary(Store store) {
        super(NAMESPACE);
        this.store = store;
    }

    @Keyword("Disconnect from pop3 server")
    @Keyword("Disconnect from imap server")
    @PassInvoker
    public void disconnet(Invoker invoker) {
        invoker.publish(new RemoveLibraryEvent(this));
    }

    @Override
    protected void doTeardown(Invoker invoker) {
        if (folderLibrary == null || !doesInvokerContainsLibraries(invoker, FolderLibrary.class))
            return;
        invoker.publish(new RemoveLibraryEvent(folderLibrary));
        folderLibrary = null;
    }

    @Keyword("Open mail folder")
    @PassInvoker
    public void openFolder(Invoker invoker, String folder, Integer max) {
        if (folderLibrary != null)
            folderLibrary.closeFolder(invoker);
        if (folder == null)
            folder = "inbox";
        try {
            Folder f = store.getFolder(folder);
            folderLibrary = new FolderLibrary(f, createRange(f, max));
            invoker.publish(new AddLibraryEvent(folderLibrary));
        } catch (MessagingException ex) {
            throw new EmailBackendException(ex);
        }
    }

    private Range createRange(Folder f, Integer max) {
        try {
            return max == null ? createFullRange(f)
                    : max > 0 ? createRangeAtTop(f, max)
                            : createRangeAtBotom(f, max);
        } catch (MessagingException ex) {
            throw new EmailBackendException(ex);
        }
    }

    private static Range createFullRange(Folder f)
            throws MessagingException {
        return new Range(1, f.getMessageCount());
    }

    private Range createRangeAtTop(Folder f, Integer max)
            throws MessagingException {
        int n = f.getMessageCount();
        int count = max > n ? n : max;
        return new Range(1, count);
    }

    private Range createRangeAtBotom(Folder f, Integer max)
            throws MessagingException {
        int n = f.getMessageCount();
        int count = -max > n ? n : -max;
        return new Range(1 + n - count, n);
    }
}
