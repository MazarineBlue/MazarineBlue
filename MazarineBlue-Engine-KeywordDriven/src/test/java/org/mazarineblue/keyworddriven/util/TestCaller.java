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
package org.mazarineblue.keyworddriven.util;

import java.lang.reflect.Method;
import static java.util.Arrays.stream;

public class TestCaller {

    private boolean called;
    private Object[] arguments;

    private static Method getMethod(String name) {
        return stream(TestCaller.class.getDeclaredMethods())
                .filter(m -> m.getDeclaringClass().equals(TestCaller.class))
                .filter(m -> m.getName().equals(name))
                .findAny()
                .orElseThrow(() -> new RuntimeException());
    }

    public Method getPublicMethod() {
        return getMethod("callerInstruction");
    }

    public Method getPrivateMethod() {
        return getMethod("callerPrivateInstruction");
    }

    public void callerInstruction(String a, String b) {
        called = true;
        arguments = new Object[]{a, b};
    }

    private void callerPrivateInstruction(String a, String b) {
        throw new UnsupportedOperationException("Never executed.");
    }

    public boolean isCalled() {
        return called;
    }

    public Object[] getArguments() {
        return arguments;
    }
}
