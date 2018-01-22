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
package org.mazarineblue.utilities;

import static java.lang.Byte.parseByte;
import static java.lang.Double.parseDouble;
import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;
import static java.lang.Short.parseShort;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import static org.mazarineblue.utilities.Primatives.isPrimative;
import org.mazarineblue.utilities.exceptions.ConversionMethodAlreadyRegisteredException;
import org.mazarineblue.utilities.exceptions.ConversionMethodNotFoundException;
import org.mazarineblue.utilities.exceptions.InputConversionException;
import org.mazarineblue.utilities.exceptions.PrimativesNotAllowedException;

/**
 * A {@code TypeConvertor} has the capability of translation one object into
 * another. Currently it supports the conversion of byte, short, integer, long,
 * float, doubles and characters and strings into each other.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class TypeConvertor {

    private static final TypeConvertor CONVERTOR = TypeConvertor.newDefaultInstance();

    private final Map<Tupel<Class<?>>, Function<Object, Object>> methods = new HashMap<>(128);

    public static TypeConvertor getSingletonInstance() {
        return CONVERTOR;
    }

    public static TypeConvertor newDefaultInstance() {
        TypeConvertor convertor = new TypeConvertor();
        convertor.registerConversionFunction(Short.class,     Byte.class, Short::byteValue);
        convertor.registerConversionFunction(Integer.class,   Byte.class, Integer::byteValue);
        convertor.registerConversionFunction(Long.class,      Byte.class, Long::byteValue);
        convertor.registerConversionFunction(Float.class,     Byte.class, Float::byteValue);
        convertor.registerConversionFunction(Double.class,    Byte.class, Double::byteValue);
        convertor.registerConversionFunction(Character.class, Byte.class, input -> parseByte(input.toString()));
        convertor.registerConversionFunction(String.class,    Byte.class, Byte::parseByte);
        convertor.registerConversionFunction(Boolean.class,   Byte.class, input -> input ? 1 : (byte) 0);

        convertor.registerConversionFunction(Byte.class,      Short.class, Byte::shortValue);
        convertor.registerConversionFunction(Integer.class,   Short.class, Integer::shortValue);
        convertor.registerConversionFunction(Long.class,      Short.class, Long::shortValue);
        convertor.registerConversionFunction(Float.class,     Short.class, Float::shortValue);
        convertor.registerConversionFunction(Double.class,    Short.class, Double::shortValue);
        convertor.registerConversionFunction(Character.class, Short.class, input -> parseShort(input.toString()));
        convertor.registerConversionFunction(String.class,    Short.class, Short::parseShort);
        convertor.registerConversionFunction(Boolean.class,   Short.class, input -> input ? 1 : (short) 0);

        convertor.registerConversionFunction(Byte.class,      Integer.class, Byte::intValue);
        convertor.registerConversionFunction(Short.class,     Integer.class, Short::intValue);
        convertor.registerConversionFunction(Long.class,      Integer.class, Long::intValue);
        convertor.registerConversionFunction(Float.class,     Integer.class, Float::intValue);
        convertor.registerConversionFunction(Double.class,    Integer.class, Double::intValue);
        convertor.registerConversionFunction(Character.class, Integer.class, input -> parseInt(input.toString()));
        convertor.registerConversionFunction(String.class,    Integer.class, Integer::parseInt);
        convertor.registerConversionFunction(Boolean.class,   Integer.class, input -> input ? 1 : 0);

        convertor.registerConversionFunction(Byte.class,      Long.class, Byte::longValue);
        convertor.registerConversionFunction(Short.class,     Long.class, Short::longValue);
        convertor.registerConversionFunction(Integer.class,   Long.class, Integer::longValue);
        convertor.registerConversionFunction(Float.class,     Long.class, Float::longValue);
        convertor.registerConversionFunction(Double.class,    Long.class, Double::longValue);
        convertor.registerConversionFunction(Character.class, Long.class, input -> parseLong(input.toString()));
        convertor.registerConversionFunction(String.class,    Long.class, Long::parseLong);
        convertor.registerConversionFunction(Boolean.class,   Long.class, input -> input ? 1L : 0L);

        convertor.registerConversionFunction(Byte.class,      Float.class, Byte::floatValue);
        convertor.registerConversionFunction(Short.class,     Float.class, Short::floatValue);
        convertor.registerConversionFunction(Integer.class,   Float.class, Integer::floatValue);
        convertor.registerConversionFunction(Long.class,      Float.class, Long::floatValue);
        convertor.registerConversionFunction(Double.class,    Float.class, Double::floatValue);
        convertor.registerConversionFunction(Character.class, Float.class, input -> parseFloat(input.toString()));
        convertor.registerConversionFunction(String.class,    Float.class, Float::parseFloat);

        convertor.registerConversionFunction(Byte.class,      Double.class, Byte::doubleValue);
        convertor.registerConversionFunction(Short.class,     Double.class, Short::doubleValue);
        convertor.registerConversionFunction(Integer.class,   Double.class, Integer::doubleValue);
        convertor.registerConversionFunction(Long.class,      Double.class, Long::doubleValue);
        convertor.registerConversionFunction(Float.class,     Double.class, Float::doubleValue);
        convertor.registerConversionFunction(Character.class, Double.class, input -> parseDouble(input.toString()));
        convertor.registerConversionFunction(String.class,    Double.class, Double::parseDouble);

        convertor.registerConversionFunction(Byte.class,      Character.class, input -> toChar(Byte.toString(input)));
        convertor.registerConversionFunction(Short.class,     Character.class, input -> toChar(Short.toString(input)));
        convertor.registerConversionFunction(Integer.class,   Character.class, input -> toChar(Integer.toString(input)));
        convertor.registerConversionFunction(Long.class,      Character.class, input -> toChar(Long.toString(input)));
        convertor.registerConversionFunction(Float.class,     Character.class, input -> toChar(Float.toString(input)));
        convertor.registerConversionFunction(Double.class,    Character.class, input -> toChar(Double.toString(input)));
        convertor.registerConversionFunction(String.class,    Character.class, TypeConvertor::toChar);
        convertor.registerConversionFunction(Boolean.class,   Character.class, input -> input ? 't' : 'f');

        convertor.registerConversionFunction(Byte.class,      String.class, input -> Byte.toString(input));
        convertor.registerConversionFunction(Short.class,     String.class, input -> Short.toString(input));
        convertor.registerConversionFunction(Integer.class,   String.class, input -> Integer.toString(input));
        convertor.registerConversionFunction(Long.class,      String.class, input -> Long.toString(input));
        convertor.registerConversionFunction(Float.class,     String.class, input -> Float.toString(input));
        convertor.registerConversionFunction(Double.class,    String.class, input -> Double.toString(input));
        convertor.registerConversionFunction(Character.class, String.class, input -> input.toString());
        convertor.registerConversionFunction(Boolean.class,   String.class, input -> input ? "true" : "false");

        convertor.registerConversionFunction(Byte.class,      Boolean.class, input -> input != 0);
        convertor.registerConversionFunction(Short.class,     Boolean.class, input -> input != 0);
        convertor.registerConversionFunction(Integer.class,   Boolean.class, input -> input != 0);
        convertor.registerConversionFunction(Long.class,      Boolean.class, input -> input != 0);
        convertor.registerConversionFunction(Character.class, Boolean.class, input -> input.equals('t'));
        convertor.registerConversionFunction(String.class,    Boolean.class, input -> input.equals("true"));
        return convertor;
    }

    private static char toChar(String input) {
        if (input.length() == 1)
            return input.charAt(0);
        throw new InputConversionException(input, Character.class);
    }

    @SuppressWarnings("unchecked")
    public final <T, R> boolean hasConversionMethod(Class<T> inputType, Class<R> outputType) {
        return methods.containsKey(createTupel(inputType, outputType));
    }

    @SuppressWarnings("unchecked")
    public final <T, R> void registerConversionFunction(Class<T> inputType, Class<R> outputType, Function<T,R> func) {
        Tupel<Class<?>> key = createTupel(inputType, outputType);
        if (isPrimative(inputType) || isPrimative(outputType))
            throw new PrimativesNotAllowedException(inputType, outputType);
        if (methods.containsKey(key))
            throw new ConversionMethodAlreadyRegisteredException(inputType, outputType);
        methods.put(key, t -> func.apply((T) t));
    }

    @SuppressWarnings("unchecked")
    public final <T, R> void unregisterConversionFunction(Class<T> inputType, Class<R> outputType) {
        Tupel<Class<?>> key = createTupel(inputType, outputType);
        if (!methods.containsKey(key))
            throw new ConversionMethodNotFoundException(inputType, outputType);
        methods.remove(key);
    }

    /**
     * Converts the specified input object into an object with the specified
     * output type.
     *
     * @param outputType the type to convert the input object in to.
     * @param input      the object to convert.
     * @return the converted object.
     */
    @SuppressWarnings("unchecked")
    public <R> R convert(Object input, Class<R> outputType) {
        Class<?> type = Primatives.convertPrimative(outputType);
        return needsConvertion(input, type) ? doConvert(input, type) : (R) input;
    }

    private static boolean needsConvertion(Object input, Class<?> outputType) {
        return !outputType.equals(Object.class) && !outputType.isAssignableFrom(input.getClass());
    }

    private <R> R doConvert(Object input, Class<?> outputType) {
        return (R) getMethod(input, outputType).apply(input);
    }

    private Function<Object, Object> getMethod(Object input, Class<?> outputType) {
        Tupel<Class<?>> tupel = createTupel(input, outputType);
        if (methods.containsKey(tupel))
            return methods.get(tupel);
        throw new InputConversionException(input, outputType);
    }

    private Tupel<Class<?>> createTupel(Object input, Class<?> outputType) {
        return createTupel(input.getClass(), outputType);
    }


    private Tupel<Class<?>> createTupel(Class<?> inputType, Class<?> outputType) {
        return new Tupel<>(inputType, outputType);
    }
}
