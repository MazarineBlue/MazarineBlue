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
package org.mazarineblue.libraries.email.util;

import java.io.IOException;
import java.io.InputStream;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import org.mazarineblue.libraries.email.exceptions.EmailBackendException;

public class Attachment {

    private final BodyPart bodyPart;

    public Attachment(BodyPart bodyPart) {
        this.bodyPart = bodyPart;
    }

    public String getFilename() {
        try {
            return bodyPart.getFileName();
        } catch (MessagingException ex) {
            throw new EmailBackendException(ex);
        }
    }

    public InputStream getInputStream() {
        try {
            return bodyPart.getInputStream();
        } catch (IOException | MessagingException ex) {
            throw new EmailBackendException(ex);
        }
    }
}
