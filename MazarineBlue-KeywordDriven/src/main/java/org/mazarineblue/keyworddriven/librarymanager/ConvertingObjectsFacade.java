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
import java.util.Objects;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
class ConvertingObjectsFacade {

    static private ConvertingObjectsFacade singleton = null;

    static ConvertingObjectsFacade getInstance() {
        if (singleton == null)
            singleton = new ConvertingObjectsFacade();
        return singleton;
    }

    private class Profile {

        private Class<?> inputType, outputType;

        private Profile(Class<? extends Object> inputType, Class<?> outputType) {
            this.inputType = inputType;
            this.outputType = outputType;
        }

        @Override
        public String toString() {
            return "(" + inputType + ", " + outputType + ')';
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 59 * hash + Objects.hashCode(inputType);
            hash = 59 * hash + Objects.hashCode(outputType);
            hash = 59 * hash + Objects.hashCode(getClass());
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            final Profile other = (Profile) obj;
            if (!Objects.equals(this.inputType, other.inputType))
                return false;
            if (!Objects.equals(this.outputType, other.outputType))
                return false;
            return true;
        }
    }

    private interface ConvertingMethod {

        Object convert(Object input);
    }

    private final Map<Profile, ConvertingMethod> map = new HashMap<>(
            11 * 4 / 3 + 1);

    ConvertingObjectsFacade() {
        addStringConversions();
        addDoubleConversions();
        addFloatConversions();
    }

    private void addStringConversions() {
        map.put(new Profile(String.class, Byte.class), (Object input) -> {
            return Byte.parseByte((String) input);
        });
        map.put(new Profile(String.class, Integer.class), (Object input) -> {
            return Integer.parseInt((String) input);
        });
        map.put(new Profile(String.class, Long.class), (Object input) -> {
            return Long.parseLong((String) input);
        });
        map.put(new Profile(String.class, Float.class), (Object input) -> {
            return Float.parseFloat((String) input);
        });
        map.put(new Profile(String.class, Double.class), (Object input) -> {
            return Double.parseDouble((String) input);
        });
    }

    private void addDoubleConversions() {
        map.put(new Profile(Double.class, Byte.class), (Object input) -> {
            return (byte) Math.round((Double) input);
        });
        map.put(new Profile(Double.class, Integer.class), (Object input) -> {
            return (int) Math.round((Double) input);
        });
        map.put(new Profile(Double.class, Long.class), (Object input) -> {
            return Math.round((Double) input);
        });
    }

    private void addFloatConversions() {
        map.put(new Profile(Float.class, Byte.class), (Object input) -> {
            return (byte) Math.round((Float) input);
        });
        map.put(new Profile(Float.class, Integer.class), (Object input) -> {
            return (int) Math.round((Float) input);
        });
        map.put(new Profile(Float.class, Long.class), (Object input) -> {
            return Math.round((Float) input);
        });
    }

    Object[] convert(Object[] input, Class<?>[] expectedType) {
        int capacity = expectedType.length > input.length
                ? expectedType.length
                : input.length;
        Object[] arr = new Object[capacity];
        int n = expectedType.length < input.length
                ? expectedType.length
                : input.length;
        for (int i = 0; i < n; ++i)
            arr[i] = convert(input[i], expectedType[i]);
        for (int i = n; i < input.length; ++i)
            arr[i] = input[i];
        for (int i = n; i < expectedType.length; ++i)
            arr[i] = null;
        return arr;
    }

    Object convert(Object input, Class<?> expectedType) {
        Class<?> clazz = input.getClass();
        Profile profile = new Profile(clazz, expectedType);
        ConvertingMethod convertMethod = map.get(profile);
        try {
            if (convertMethod != null)
                return convertMethod.convert(input);
            if (expectedType == String.class && clazz != String.class)
                return input.toString();
            return input;
        } catch (RuntimeException ex) {
            return null;
        }
    }
}
