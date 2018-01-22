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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public abstract class TestUtilityClass {

    private final Class<?> type;

    public TestUtilityClass(Class<?> type) {
        this.type = type;
    }

    @Test
    public void class_IsPrivate() {
        assertTrue("class must be final", Modifier.isFinal(type.getModifiers()));
    }

    @Test
    public void constructor_IsTheOnlyOne()
            throws SecurityException {
        assertEquals("There must be only one constructor", 1, type.getDeclaredConstructors().length);
    }

    @Test
    public void constructor_IsPrivate()
            throws NoSuchMethodException {
        Constructor<?> constructor = type.getDeclaredConstructor();
        if (constructor.isAccessible() || !Modifier.isPrivate(constructor.getModifiers()))
            fail("constructor is not private");
    }

    @Test
    public void constructor_Accessed()
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Constructor<?> constructor = type.getDeclaredConstructor();
        constructor.setAccessible(true);
        constructor.newInstance();
        constructor.setAccessible(false);
    }

    @Test
    public void methods_AreStatic()
            throws SecurityException {
        for (final Method method : type.getMethods())
            if (!Modifier.isStatic(method.getModifiers()) && method.getDeclaringClass().equals(type))
                fail("there exists a non-static method:" + method);
    }
}
