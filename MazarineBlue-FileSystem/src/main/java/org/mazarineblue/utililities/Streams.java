/*
 * Copyright (c) 2016 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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
package org.mazarineblue.utililities;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * This is a utility class for actions on streams.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public final class Streams {

    private static final int BUFFER_CAPACITY = 4096;

    /**
     * Copies the bytes from the specified input to the specified output.
     *
     * @param input  the input to read the bytes from.
     * @param output the output to write the bytes to.
     * @throws IOException when an IO error occurs.
     */
    public static void copy(InputStream input, OutputStream output)
            throws IOException {
        int n = BUFFER_CAPACITY;
        byte[] buffer = new byte[BUFFER_CAPACITY];
        while (n == BUFFER_CAPACITY) {
            n = input.read(buffer);
            if (n == -1)
                break;
            output.write(buffer, 0, n);
        }
    }

    private Streams() {
    }
}
