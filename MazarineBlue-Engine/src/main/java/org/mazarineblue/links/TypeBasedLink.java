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
package org.mazarineblue.links;

import java.util.Arrays;
import org.mazarineblue.eventbus.Event;
import org.mazarineblue.eventdriven.Link;

/**
 * A {@code TypeBasedLink} holds an array of types and implements basic
 * methods on them.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public abstract class TypeBasedLink
        extends Link {

    protected final Class<?>[] types;

    /**
     * Constructs a {@code TypeBasedLink} that consumes every event that is on
     * the specified whitelist.
     */
    public TypeBasedLink() {
        this(Event.class);
    }

    /**
     * Constructs a {@code TypeBasedLink} that consumes any event that is on
     * the specified whitelist.
     *
     * @param types the types of {@link Event events} to consume.
     */
    public TypeBasedLink(Class<?>... types) {
        this.types = types;
    }

    @Override
    public String toString() {
        return "types = " + Arrays.toString(types);
    }

    @Override
    public int hashCode() {
        return 3 * 89
                + Arrays.deepHashCode(this.types);
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && getClass() == obj.getClass()
                && Arrays.deepEquals(this.types, ((TypeBasedLink) obj).types);
    }
}
