/*
 * Copyright (c) 2016 Alex de Kruijff
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

/**
 * An {@code ID} is an class for which each object has an unique value.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class ID
        implements Serializable {

    private static final long serialVersionUID = 1L;
    private static int count = 0;

    private final int value;

    /**
     * Constructs an ID with an unique, that is never used before, value.
     */
    public ID() {
        this.value = ++count;
    }

    ID(int id) {
        this.value = id;
    }

    /**
     * Returns the number of unique {@code ID} objects create.
     *
     * @return the number of unique {@code ID} objects create.
     */
    public static int getCount() {
        return count;
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }

    @Override
    public int hashCode() {
        return 5 * 71
                + this.value;
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj != null && getClass() == obj.getClass()
                && this.value == ((ID) obj).value;
    }
}
