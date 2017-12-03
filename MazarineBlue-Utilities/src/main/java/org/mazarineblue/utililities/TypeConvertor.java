/*
 * Copyright (c) 2012-2014 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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
package org.mazarineblue.utililities;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.mazarineblue.utililities.exceptions.InputConvertException;

/**
 * A {@code TypeConvertor} has the capability of translation one object into
 * another. Currently it supports the conversion of byte, short, integer, long,
 * float, doubles and characters and strings into each other.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class TypeConvertor {

    private Primatives primatives = Primatives.newInstance();
    private final Map<Tupel<Class<?>>, Function<Object, Object>> methods = new HashMap<>(128);

    /**
     * Constructs the a {@code TypeConvertor}.
     */
    public TypeConvertor() {
        methods.put(new Tupel<>(Byte.class, Short.class), (Object input) -> (byte) (short) input);
        methods.put(new Tupel<>(Byte.class, Integer.class), (Object input) -> (byte) (int) input);
        methods.put(new Tupel<>(Byte.class, Long.class), (Object input) -> (byte) (long) input);
        methods.put(new Tupel<>(Byte.class, Float.class), (Object input) -> (byte) (float) input);
        methods.put(new Tupel<>(Byte.class, Double.class), (Object input) -> (byte) (double) input);
        methods.put(new Tupel<>(Byte.class, Character.class), (Object input) -> Byte.parseByte(input.toString()));
        methods.put(new Tupel<>(Byte.class, String.class), (Object input) -> Byte.parseByte((String) input));

        methods.put(new Tupel<>(Short.class, Byte.class), (Object input) -> (short) (byte) input);
        methods.put(new Tupel<>(Short.class, Integer.class), (Object input) -> (short) (int) input);
        methods.put(new Tupel<>(Short.class, Long.class), (Object input) -> (short) (long) input);
        methods.put(new Tupel<>(Short.class, Float.class), (Object input) -> (short) (float) input);
        methods.put(new Tupel<>(Short.class, Double.class), (Object input) -> (short) (double) input);
        methods.put(new Tupel<>(Short.class, Character.class), (Object input) -> Short.parseShort(input.toString()));
        methods.put(new Tupel<>(Short.class, String.class), (Object input) -> Short.parseShort((String) input));

        methods.put(new Tupel<>(Integer.class, Byte.class), (Object input) -> (int) (byte) input);
        methods.put(new Tupel<>(Integer.class, Short.class), (Object input) -> (int) (short) input);
        methods.put(new Tupel<>(Integer.class, Long.class), (Object input) -> (int) (long) input);
        methods.put(new Tupel<>(Integer.class, Float.class), (Object input) -> (int) (float) input);
        methods.put(new Tupel<>(Integer.class, Double.class), (Object input) -> (int) (double) input);
        methods.put(new Tupel<>(Integer.class, Character.class), (Object input) -> Integer.parseInt(input.toString()));
        methods.put(new Tupel<>(Integer.class, String.class), (Object input) -> Integer.parseInt((String) input));

        methods.put(new Tupel<>(Long.class, Byte.class), (Object input) -> (long) (byte) input);
        methods.put(new Tupel<>(Long.class, Short.class), (Object input) -> (long) (short) input);
        methods.put(new Tupel<>(Long.class, Integer.class), (Object input) -> (long) (int) input);
        methods.put(new Tupel<>(Long.class, Float.class), (Object input) -> (long) (float) input);
        methods.put(new Tupel<>(Long.class, Double.class), (Object input) -> (long) (double) input);
        methods.put(new Tupel<>(Long.class, Character.class), (Object input) -> Long.parseLong(input.toString()));
        methods.put(new Tupel<>(Long.class, String.class), (Object input) -> Long.parseLong((String) input));

        methods.put(new Tupel<>(Float.class, Byte.class), (Object input) -> (float) ((byte) input));
        methods.put(new Tupel<>(Float.class, Short.class), (Object input) -> (float) ((short) input));
        methods.put(new Tupel<>(Float.class, Integer.class), (Object input) -> (float) ((int) input));
        methods.put(new Tupel<>(Float.class, Long.class), (Object input) -> (float) ((long) input));
        methods.put(new Tupel<>(Float.class, Double.class), (Object input) -> (float) (double) input);
        methods.put(new Tupel<>(Float.class, Character.class), (Object input) -> Float.parseFloat(input.toString()));
        methods.put(new Tupel<>(Float.class, String.class), (Object input) -> Float.parseFloat((String) input));

        methods.put(new Tupel<>(Double.class, Byte.class), (Object input) -> (double) ((byte) input));
        methods.put(new Tupel<>(Double.class, Short.class), (Object input) -> (double) ((short) input));
        methods.put(new Tupel<>(Double.class, Integer.class), (Object input) -> (double) ((int) input));
        methods.put(new Tupel<>(Double.class, Long.class), (Object input) -> (double) ((long) input));
        methods.put(new Tupel<>(Double.class, Float.class), (Object input) -> (double) (float) input);
        methods.put(new Tupel<>(Double.class, Character.class), (Object input) -> Double.parseDouble(input.toString()));
        methods.put(new Tupel<>(Double.class, String.class), (Object input) -> Double.parseDouble((String) input));

        methods.put(new Tupel<>(Boolean.class, Byte.class), (Object input) -> ((byte) input) != 0);
        methods.put(new Tupel<>(Boolean.class, Short.class), (Object input) -> ((short) input) != 0);
        methods.put(new Tupel<>(Boolean.class, Integer.class), (Object input) -> ((int) input) != 0);
        methods.put(new Tupel<>(Boolean.class, Long.class), (Object input) -> ((long) input) != 0);
        methods.put(new Tupel<>(Boolean.class, Character.class), (Object input) -> ((Character) input) != 0);
        methods.put(new Tupel<>(Boolean.class, String.class), (Object input) -> Boolean.parseBoolean((String) input));

        methods.put(new Tupel<>(Character.class, String.class), (Object input) -> {
                String str = (String) input;
                if (str.length() == 1)
                    return str.charAt(0);
                throw new InputConvertException();
            });

        methods.put(new Tupel<>(String.class, Byte.class), (Object input) -> Byte.toString((byte) input));
        methods.put(new Tupel<>(String.class, Short.class), (Object input) -> Short.toString((short) input));
        methods.put(new Tupel<>(String.class, Integer.class), (Object input) -> Integer.toString((int) input));
        methods.put(new Tupel<>(String.class, Long.class), (Object input) -> Long.toString((long) input));
        methods.put(new Tupel<>(String.class, Float.class), (Object input) -> Float.toString((float) input));
        methods.put(new Tupel<>(String.class, Double.class), (Object input) -> Double.toString((double) input));
        methods.put(new Tupel<>(String.class, Boolean.class), (Object input) -> Boolean.toString((boolean) input));
        methods.put(new Tupel<>(String.class, Character.class), (Object input) -> Character.toString((char) input));
    }

    /**
     * Tests if the specified input can be converted into the specified output
     * type.
     *
     * @param outputType the type to convert the input object in to.
     * @param input      the object to convert.
     * @return {@code true} if the input object can be converted into the specified
     *         output type.
     */
    public boolean isConvertable(Class<?> outputType, Object input) {
        Class<?> type = convertType(outputType);
        return conversionMethodIsAvailable(type, input)
                ? tryConverting(type, input)
                : type.isInstance(input);
    }

    private Class<?> convertType(Class<?> type) {
        return primatives.isPrimative(type) ? primatives.getEquivalentType(type) : type;
    }

    private boolean conversionMethodIsAvailable(Class<?> outputType, Object input) {
        Tupel<Class<?>> tupel = createTupel(outputType, input);
        return methods.containsKey(tupel);
    }

    private boolean tryConverting(Class<?> outputType, Object input) {
        try {
            getMethod(outputType, input).apply(input);
        } catch (RuntimeException ex) {
            return false;
        }
        return true;
    }

    /**
     * Converts the specified input object into an object with the specified
     * output type.
     *
     * @param outputType the type to convert the input object in to.
     * @param input      the object to convert.
     * @return the converted object.
     */
    public Object convert(Class<?> outputType, Object input) {
        Class<?> type = convertType(outputType);
        return type.equals(Object.class) || type.isAssignableFrom(input.getClass()) ? input
                : getMethod(type, input).apply(input);
    }

    private Function<Object, Object> getMethod(Class<?> outputType, Object input) {
        Tupel<Class<?>> tupel = createTupel(outputType, input);
        if (methods.containsKey(tupel))
            return methods.get(tupel);
        throw new InputConvertException();
    }

    private Tupel<Class<?>> createTupel(Class<?> outputType, Object input) {
        return new Tupel<>(outputType, input.getClass());
    }
}
