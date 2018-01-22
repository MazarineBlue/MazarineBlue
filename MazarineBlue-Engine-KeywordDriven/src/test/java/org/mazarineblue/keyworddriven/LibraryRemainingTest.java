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
package org.mazarineblue.keyworddriven;

import java.lang.reflect.Method;
import static java.util.Arrays.stream;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.mazarineblue.keyworddriven.util.libraries.TestLibrary1;
import org.mazarineblue.keyworddriven.util.libraries.TestLibrary2;
import org.mazarineblue.keyworddriven.util.libraries.TestLibrary3;

public class LibraryRemainingTest {

    @Test
    public void test_ToString_LibraryRegistry() {
        LibraryRegistry registry = new LibraryRegistry(new TestLibrary1(), new TestLibrary2(), new TestLibrary3());
        assertEquals("size=3", registry.toString());
    }

    @Test
    public void test_ToString_Library() {
        assertEquals("test [abc, bar, foo]", new TestLibrary3().toString());
    }

    @Test
    public void test_ToString_Instruction_OneParamters() {
        Instruction instruction = new Instruction(getMethod("first", 0), 1);
        assertEquals("method=LibraryRemainingTest.first, minParameters=1, beta=false, deprecated=false",
                     instruction.toString());
    }

    @Test
    public void test_ToString_Instruction_ThreeParamters() {
        Instruction instruction = new Instruction(getMethod("first", 0), 3);
        assertEquals("method=LibraryRemainingTest.first, minParameters=3, beta=false, deprecated=false",
                     instruction.toString());
    }

    @Test
    public void test_ToString_Instruction_Beta() {
        Instruction instruction = new Instruction(getMethod("beta", 0), 3);
        assertEquals("method=LibraryRemainingTest.beta, minParameters=3, beta=true, deprecated=false",
                     instruction.toString());
    }

    @Test
    public void test_ToString_Instruction_Deprecated() {
        Instruction instruction = new Instruction(getMethod("deprecated", 0), 3);
        assertEquals("method=LibraryRemainingTest.deprecated, minParameters=3, beta=false, deprecated=true",
                     instruction.toString());
    }

    /* ********************************************************************** *
     *                             Helper methods                             *
     * ********************************************************* ,,^..^,, *** */
    public Method getMethod(String name, int parameterCount) {
        return stream(getClass().getMethods())
                .filter(m -> m.getDeclaringClass().equals(getClass()))
                .filter(m -> m.getName().equals(name))
                .filter(m -> m.getParameterCount() == parameterCount)
                .findAny()
                .orElseThrow(() -> new UnsupportedOperationException("Method unavailable."));
    }

    // These methods are called using reflection by getMethod
    public void first() {
        // For testing purposes, there is no need for an implemantion.
    }

    @Beta
    public void beta() {
        // For testing purposes, there is no need for an implemantion.
    }

    @Deprecated
    public void deprecated() {
        // For testing purposes, there is no need for an implemantion.
    }
}
