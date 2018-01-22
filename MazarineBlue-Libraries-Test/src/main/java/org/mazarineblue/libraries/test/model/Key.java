/*
 * Copyright (c) 2017 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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
package org.mazarineblue.libraries.test.model;

import java.util.Comparator;
import java.util.Map;

/**
 * A {@code Key} is a object, which can be used to store a {@link Suite} or
 * {@link Test} in a {@link Map}.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public interface Key {

    /**
     * Returns a {@link Comperator} that can be used to sort keys in natural order.
     *
     * @return a {@code Comperator}.
     */
    public static Comparator<Key> naturalOrder() {
        return (first, second) -> first.name().compareTo(second.name());
    }

    public String name();

    @Override
    public int hashCode();

    @Override
    public boolean equals(Object obj);
}
