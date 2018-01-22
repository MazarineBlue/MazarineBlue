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
package org.mazarineblue.libraries.test.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import static java.lang.String.format;
import java.net.URL;
import org.junit.ComparisonFailure;
import org.mazarineblue.libraries.test.MainTestLibraryTest;
import org.mazarineblue.utililities.Streams;

public class TestUtilility {

    public static final int BUF_SIZE = 16384;
    public static final String RESOURCE_POSTFIX = ".txt";

    public static String getResourcePrefix(Package pkg, String postfix) {
        return format("%s/%s/", pkg.getName().replace('.', '/'), postfix);
    }

    public static void assertExecutorListenerCalls(String expectedPath, Object actualResult)
            throws IOException {
        String expected = getExpectedOutput(expectedPath);
        String actual = actualResult.toString();
        if (expected.equals(actual))
            return;
        System.out.println("*** EXPECTED *************************************************************** ***");
        System.out.println(expected);
        System.out.println("*** ACTUAL   *************************************************************** ***");
        System.out.println(actual);
        System.out.println("*** *************************************************************** ,,^..^,, ***");
        throw new ComparisonFailure("", (String) expected, (String) actual);
    }

    private static String getExpectedOutput(String expectedPath)
            throws IOException {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream(BUF_SIZE);
            Streams.copy(new FileInputStream(getFile(expectedPath)), out);
            return out.toString();
        } catch (TestResourceNotFoundException ex) {
            return "";
        }
    }

    private static File getFile(String expectedPath) {
        ClassLoader classLoader = MainTestLibraryTest.class.getClassLoader();
        URL resource = classLoader.getResource(expectedPath);
        if (resource == null)
            throw new TestResourceNotFoundException(expectedPath);
        return new File(resource.getFile());
    }

    private TestUtilility() {
    }
}
