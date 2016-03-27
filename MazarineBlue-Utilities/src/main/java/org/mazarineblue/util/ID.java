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
package org.mazarineblue.util;

/**
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class ID {

    private static int count = 0;
    private final int id;

    public static int count() {
        return count;
    }

    public ID() {
        this.id = ++count;
    }

    ID(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return Integer.toString(id);
    }

    @Override
    public int hashCode() {
        return 355 + this.id;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && getClass() == obj.getClass() && equals((ID) obj);
    }

    private boolean equals(ID other) {
        return this.id == other.id;
    }
}
