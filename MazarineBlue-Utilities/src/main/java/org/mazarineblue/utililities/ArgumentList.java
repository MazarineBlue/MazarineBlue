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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Collection;

/**
 * An {@code ArgumentList} is an container of an array of objects.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class ArgumentList {

    private Object[] arguments;

    /**
     * Constructs an {@code ArgumentList} using the specified arguments.
     *
     * @param arguments the argument to use to construct this
     *                  {@code ArgumentList}.
     */
    public ArgumentList(Object... arguments) {
        this.arguments = Arrays.copyOf(arguments, arguments.length);
    }

    /**
     * Constructs an {@code ArgumentList} using the specified arguments.
     *
     * @param arguments the argument to use to construct this
     *                  {@code ArgumentList}.
     */
    public <T> ArgumentList(Collection<T> arguments) {
        this.arguments = arguments.toArray(new Object[arguments.size()]);
    }

    @Override
    public String toString() {
        switch (arguments.length) {
            case 0: return "";
            case 1: return arguments[0].toString();
            default: return buildString(", ");
        }
    }

    private String buildString(String delimiter) {
        StringBuilder builder = new StringBuilder(arguments.length * 16);
        builder.append(arguments[0]);
        for (int i = 1; i < arguments.length; ++i)
            builder.append(delimiter).append(arguments[i]);
        return builder.toString();
    }

    public Object[] getArguments() {
        return Arrays.copyOf(arguments, arguments.length);
    }

    private void writeObject(ObjectOutputStream output)
            throws IOException {
        output.defaultWriteObject();
        output.writeInt(arguments.length);
        for (int i = 0; i < arguments.length; ++i)
            output.writeObject(arguments[i]);
    }

    private void readObject(ObjectInputStream input)
            throws IOException, ClassNotFoundException {
        arguments = new Object[input.readInt()];
        for (int i = 0; i < arguments.length; ++i)
            arguments[i] = input.readObject();
    }

    @Override
    public int hashCode() {
        return 7 * 43
                + Arrays.deepHashCode(this.arguments);
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && getClass() == obj.getClass()
                && Arrays.deepEquals(this.arguments, ((ArgumentList) obj).arguments);
    }
}
