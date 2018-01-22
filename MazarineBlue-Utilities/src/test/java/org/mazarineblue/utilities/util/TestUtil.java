/*
 * Copyright (c) 2017 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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
package org.mazarineblue.utilities.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.regex.Pattern;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestUtil {

    public static void assertRegex(String regex, String actual) {
        String[] split1 = actual.split("\\n");
        String[] split2 = regex.split("\\n");
        assertEquals("Different line count on expected and actual lines", split1.length, split2.length);
        for (int i = 0; i < split1.length; ++i) {
            boolean flag = Pattern.matches(split2[i], split1[i]);
            assertTrue("Assertion failed on line: " + i, flag);
        }
    }

    public static <T> void assertClasses(List<T> list,
                                         Class<?>... types) {
        assertEquals(types.length, list.size());
        for (int i = 0; i < types.length; ++i)
            assertTrue(types[i].isAssignableFrom(list.get(i).getClass()));
    }

    public static void assertException(String str, Exception cause, Exception ex) {
        assertEquals(str, ex.getMessage());
        assertEquals(cause, ex.getCause());
    }

    private static <T> ByteArrayOutputStream writeObject(T expected)
            throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(expected);
        return bos;
    }

    @SuppressWarnings(value = "unchecked")
    private static <T> T readObject(ByteArrayOutputStream bos)
            throws IOException, ClassNotFoundException {
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bis);
        return (T) ois.readObject();
    }

    @SuppressWarnings(value = "unchecked")
    public static <T> T copy(T expected)
            throws IOException, ClassNotFoundException {
        return readObject(writeObject(expected));
    }

    private TestUtil() {
    }
}
