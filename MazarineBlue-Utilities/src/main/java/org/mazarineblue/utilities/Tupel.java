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

import static java.util.Arrays.deepEquals;
import static java.util.Arrays.deepHashCode;
import static java.util.Arrays.deepToString;

/**
 * A {@code Tupel} is a data structure that allows to store multiple variables
 * in one record.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 * @param <T> the type to store in the record.
 */
public class Tupel<T> {

    private final T[] array;

    /**
     * Constructs a {@code Tupel} using the specified arguments.
     *
     * @param args the objects to put into the {@code Tupel}.
     */
    @SuppressWarnings("unchecked")
    public Tupel(T... args) {
        this.array = args;
    }

    @Override
    public String toString() {
        return deepToString(array);
    }

    /**
     * Returns the object at the specified index.
     *
     * @param index the index of the object to return.
     * @return the requested object.
     */
    public T get(int index) {
        return array[index];
    }

    /**
     * Returns the amount of objects in this {@code Tupel}.
     *
     * @return the amount of objects in this {@code Tupel}.
     */
    public int size() {
        return array.length;
    }

    @Override
    public int hashCode() {
        return 3 * 79
                + deepHashCode(this.array);
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj != null && getClass() == obj.getClass()
                && deepEquals(this.array, ((Tupel<?>) obj).array);
    }
}
