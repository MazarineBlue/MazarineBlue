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
package org.mazarineblue.utilities;

import static java.util.Arrays.asList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import static org.mazarineblue.utilities.Primatives.convertPrimative;
import static org.mazarineblue.utilities.Primatives.getEquivalentType;
import static org.mazarineblue.utilities.Primatives.isPrimative;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class PrimativesTest {

    @Test
    public void isPrimative_Primative_IsAnPrimative() {
        List<Class<?>> list = asList(byte.class, short.class, int.class, long.class,
                                     float.class, double.class, boolean.class, char.class);
        list.stream().forEach(type -> assertTrue(isPrimative(type)));
    }

    @Test
    public void isPrimative_Object_IsNotAnPrimative() {
        List<Class<?>> list = asList(Byte.class, Short.class, Integer.class, Long.class, String.class,
                                     Float.class, Double.class, Boolean.class, Character.class);
        list.stream().forEach(type -> assertFalse(isPrimative(type)));
    }

    @Test
    public void getEquivalentType_Object_ReturnEquivalentPrimativesOrNull() {
        List<Tupel<Class<?>>> list = asList(
                new Tupel<Class<?>>(Byte.class, byte.class),
                new Tupel<Class<?>>(Short.class, short.class),
                new Tupel<Class<?>>(Integer.class, int.class),
                new Tupel<Class<?>>(Long.class, long.class),
                new Tupel<Class<?>>(Float.class, float.class),
                new Tupel<Class<?>>(Double.class, double.class),
                new Tupel<Class<?>>(Boolean.class, boolean.class),
                new Tupel<Class<?>>(Character.class, char.class),
                new Tupel<Class<?>>(null, String.class));
        list.stream().forEach(tupel -> assertEquals(tupel.get(0), getEquivalentType(tupel.get(1))));
    }

    @Test
    public void convertToObjectClass_Object_ReturnEquivalentPrimativesOrInputType() {
        List<Tupel<Class<?>>> list = asList(
                new Tupel<Class<?>>(Byte.class, byte.class),
                new Tupel<Class<?>>(Short.class, short.class),
                new Tupel<Class<?>>(Integer.class, int.class),
                new Tupel<Class<?>>(Long.class, long.class),
                new Tupel<Class<?>>(Float.class, float.class),
                new Tupel<Class<?>>(Double.class, double.class),
                new Tupel<Class<?>>(Boolean.class, boolean.class),
                new Tupel<Class<?>>(Character.class, char.class),
                new Tupel<Class<?>>(Byte.class, Byte.class),
                new Tupel<Class<?>>(Short.class, Short.class),
                new Tupel<Class<?>>(Integer.class, Integer.class),
                new Tupel<Class<?>>(Long.class, Long.class),
                new Tupel<Class<?>>(Float.class, Float.class),
                new Tupel<Class<?>>(Double.class, Double.class),
                new Tupel<Class<?>>(Boolean.class, Boolean.class),
                new Tupel<Class<?>>(Character.class, Character.class));
        list.stream().forEach(tupel -> assertEquals(tupel.get(0), convertPrimative(tupel.get(1))));
    }
}
