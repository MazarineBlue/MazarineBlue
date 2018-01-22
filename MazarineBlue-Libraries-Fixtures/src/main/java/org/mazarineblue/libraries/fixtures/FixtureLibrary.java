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
package org.mazarineblue.libraries.fixtures;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.mazarineblue.keyworddriven.Library;
import static org.mazarineblue.utilities.Primatives.isPrimative;

/**
 * A {@code FixtureLibrary} is a {@code Library} that wraps all public method
 * or the fixture in to a {@code Library}.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class FixtureLibrary
        extends Library {

    private static final Map<Method, Method> BASE_METHODS = fetchBaseMethods();

    private final Object caller;

    /**
     * Create a {@link Library} with the specified namespace and using the
     * public method of the specified caller.
     *
     * @param namespace the namespace of the new library.
     * @param caller    the methods to use in the new library.
     */
    public FixtureLibrary(String namespace, Object caller) {
        super(namespace);
        this.caller = caller;
        registerObjectMethods(caller);
    }

    private static Map<Method, Method> fetchBaseMethods() {
        Map<Method, Method> map = new HashMap<>(32);
        for (Method m : Object.class.getMethods())
            map.put(m, m);
        return map;
    }

    private void registerObjectMethods(Object caller) {
        for (Method method : caller.getClass().getMethods())
            if (!BASE_METHODS.containsKey(method))
                registerInstruction(method.getName(), method, minimalRequiredArguments(method));
    }

    private static int minimalRequiredArguments(Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        for (int i = parameterTypes.length - 1; i >= 0; --i)
            if (isPrimative(parameterTypes[i]))
                return i + 1;
        return 0;
    }

    @Override
    protected Object getCaller() {
        return caller;
    }

    @Override
    public int hashCode() {
        return 7 * 17 * 17
                + 17 * super.hashCode()
                + Objects.hashCode(this.caller);
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj != null && getClass() == obj.getClass() && super.equals(obj)
                && Objects.equals(this.caller, ((FixtureLibrary) obj).caller);
    }
}
