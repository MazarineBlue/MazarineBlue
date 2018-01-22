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
import javax.activation.DataHandler;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import static javax.mail.Part.ATTACHMENT;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

public class EmailUtil {

    public static Multipart createMultiPartWithAttachements(int n)
            throws MessagingException, IOException {
        MimeBodyPart[] arr = new MimeBodyPart[n];
        for (int i = 0; i < n; ++i) {
            arr[i] = new MimeBodyPart();
            arr[i].setDataHandler(new DataHandler(new ByteArrayDataSource("bar", "image/jpeg")));
            arr[i].setFileName("attachment" + i + ".txt");
            arr[i].setDisposition(ATTACHMENT);
        }
        return new MimeMultipart(arr);
    }
}
