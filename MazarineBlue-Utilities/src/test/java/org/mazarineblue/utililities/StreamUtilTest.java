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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import static org.junit.Assert.assertArrayEquals;
import org.junit.Test;

public class StreamUtilTest {

    @Test(expected = NullPointerException.class)
    public void copy_NullInput()
            throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream(1024);
        StreamUtil.copy(null, output);
    }

    @Test(expected = NullPointerException.class)
    public void copy_NullOutput()
            throws IOException {
        byte[] bytes = new byte[]{0, 1, 2, 3, 4, 5, 6, 7};
        ByteArrayInputStream input = new ByteArrayInputStream(bytes);
        StreamUtil.copy(input, null);
    }

    @Test
    public void copy_Happy()
            throws IOException {
        byte[] bytes = new byte[]{0, 1, 2, 3, 4, 5, 6, 7};
        ByteArrayInputStream input = new ByteArrayInputStream(bytes);
        ByteArrayOutputStream output = new ByteArrayOutputStream(1024);
        StreamUtil.copy(input, output);
        assertArrayEquals(bytes, output.toByteArray());
    }
}
