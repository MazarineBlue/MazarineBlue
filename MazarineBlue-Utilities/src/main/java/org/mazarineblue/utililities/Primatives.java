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

import java.util.HashMap;
import java.util.Map;

/**
 * The {@code Pirmatives] class allows for the conversion of primatives into
 * there class parts.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class Primatives {

    private static Primatives singleton;
    private final Map<Class<?>, Class<?>> map = new HashMap<>(16);

    private Primatives() {
        map.put(boolean.class, Boolean.class);
        map.put(byte.class, Byte.class);
        map.put(char.class, Character.class);
        map.put(short.class, Short.class);
        map.put(int.class, Integer.class);
        map.put(long.class, Long.class);
        map.put(float.class, Float.class);
        map.put(double.class, Double.class);
    }

    public static Primatives getDefaultInstance() {
        if (singleton == null)
            singleton = new Primatives();
        return singleton;
    }

    /**
     * Test if the specified type is a primative.
     *
     * @param type the specified type to test.
     * @return {@code true} if the specified type was a primative.
     */
    public boolean isPrimative(Class<?> type) {
        return map.containsKey(type);
    }

    /**
     * Returns the equivalent type of the specified primative.
     *
     * @param type the primative type to return the qeuivalant type for.
     * @return {@code null} if the specified type was not a primative.
     */
    public Class<?> getEquivalentType(Class<?> type) {
        return map.get(type);
    }
}
