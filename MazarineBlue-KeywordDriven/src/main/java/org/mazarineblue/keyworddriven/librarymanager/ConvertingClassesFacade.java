/*
 * Copyright (c) 2012-2014 Alex de Kruijff
 * Copyright (c) 2014-2015 Specialisterren
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
package org.mazarineblue.keyworddriven.librarymanager;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
class ConvertingClassesFacade {

    static private ConvertingClassesFacade singleton = null;

    static ConvertingClassesFacade getInstance() {
        if (singleton == null)
            singleton = new ConvertingClassesFacade();
        return singleton;
    }

    private final Map<Class<?>, Class<?>> map = new HashMap<>(6 * 4 / 3 + 1);

    ConvertingClassesFacade() {
        map.put(char.class, Character.class);
        map.put(byte.class, Byte.class);
        map.put(int.class, Integer.class);
        map.put(long.class, Long.class);
        map.put(float.class, Float.class);
        map.put(double.class, Double.class);
    }

    Class<?>[] convert(Class<?>[] input) {
        Class<?>[] output = new Class<?>[input.length];
        for (int i = 0; i < input.length; ++i)
            output[i] = convert(input[i]);
        return output;
    }

    Class<?> convert(Class<?> clazz) {
        return map.containsKey(clazz)
                ? map.get(clazz)
                : clazz;
    }
}
