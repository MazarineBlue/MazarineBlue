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

import java.io.Serializable;
import static java.util.Arrays.copyOf;
import static java.util.Arrays.copyOfRange;
import static java.util.Arrays.deepEquals;
import static java.util.Arrays.deepHashCode;
import java.util.function.Predicate;

/**
 * An {@code ArgumentList} is an container of an array of objects.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class ArgumentList
        implements Serializable {

    private static final long serialVersionUID = 1L;

    private Object[] arguments;

    /**
     * Constructs an {@code ArgumentList} using the specified arguments.
     *
     * @param arguments the argument to use to construct this
     *                  {@code ArgumentList}.
     */
    public ArgumentList(Object... arguments) {
        this.arguments = copyOf(arguments, arguments.length);
    }

    @Override
    public String toString() {
        switch (arguments.length) {
            case 0:
                return "";
            case 1:
                return arguments[0].toString();
            default:
                return buildString(", ");
        }
    }

    private String buildString(String delimiter) {
        StringBuilder builder = new StringBuilder(arguments.length * 16);
        builder.append(arguments[0]);
        for (int i = 1; i < arguments.length; ++i)
            builder.append(delimiter).append(arguments[i]);
        return builder.toString();
    }

    public int indexOf(Predicate<Object> condition) {
        for (int i = 0; i < arguments.length; ++i)
            if (condition.test(arguments[i]))
                return i;
        return -1;
    }

    public int lastIndexOf(Predicate<Object> condition) {
        for (int i = arguments.length - 1; i >= 0; --i)
            if (condition.test(arguments[i]))
                return i;
        return -1;
    }
            
    public Object getArgument(int index) {
        return arguments[index];
    }

    public Object[] getArguments() {
        return getArguments(0, arguments.length);
    }

    public Object[] getArguments(int start) {
        return getArguments(start, arguments.length);
    }

    public final Object[] getArguments(int start, int stop) {
        return copyOfRange(arguments, start, stop);
    }

    public void setArgument(int index, Object argument) {
        arguments[index] = argument;
    }

    public void setArguments(Object... arguments) {
        this.arguments = copyOf(arguments, arguments.length);
    }

    @Override
    public int hashCode() {
        return 7 * 43
                + deepHashCode(this.arguments);
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj != null && getClass() == obj.getClass()
                && deepEquals(this.arguments, ((ArgumentList) obj).arguments);
    }
}
