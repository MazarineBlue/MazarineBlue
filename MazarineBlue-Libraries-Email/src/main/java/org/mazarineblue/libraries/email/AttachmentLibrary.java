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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import org.mazarineblue.eventdriven.Invoker;
import org.mazarineblue.executors.events.CallFileSystemMethodEvent;
import org.mazarineblue.fs.FileSystem;
import org.mazarineblue.keyworddriven.Keyword;
import org.mazarineblue.keyworddriven.Library;
import org.mazarineblue.keyworddriven.Parameters;
import org.mazarineblue.keyworddriven.PassInvoker;
import org.mazarineblue.keyworddriven.events.RemoveLibraryEvent;
import static org.mazarineblue.libraries.email.EmailLibraryPlugin.NAMESPACE;
import org.mazarineblue.libraries.email.exceptions.EmailBackendException;
import org.mazarineblue.libraries.email.util.Attachment;

public class AttachmentLibrary
        extends Library {

    private final Attachment attachment;

    public AttachmentLibrary(Attachment attachment) {
        super(NAMESPACE);
        this.attachment = attachment;
    }

    @Keyword("Deselect attachment")
    @PassInvoker
    public void deselectAttachment(Invoker invoker) {
        invoker.publish(new RemoveLibraryEvent(this));
    }

    @Keyword("Save selected attachment")
    @PassInvoker
    public void saveSelectedAttachment(Invoker invoker) {
        saveSelectedAttachmentAs(invoker, getFilename());
    }

    @Keyword("Get filename of selected attachment")
    public String getFilename() {
        return attachment.getFilename();
    }

    @Keyword("Save selected attachment as")
    @Parameters(min = 1)
    @PassInvoker
    public void saveSelectedAttachmentAs(Invoker invoker, String file) {
        try (InputStream input = attachment.getInputStream()) {
            Method method = FileSystem.class.getMethod("mkfile", File.class, InputStream.class);
            CallFileSystemMethodEvent e = new CallFileSystemMethodEvent(method, new File(file), input);
            invoker.publish(e);
            if (e.isExceptionThrown())
                e.throwException();
        } catch (IOException | ReflectiveOperationException | RuntimeException ex) {
            throw new EmailBackendException(ex);
        }
    }
}
