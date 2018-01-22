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
package org.mazarineblue.eventnotifier.util;

import java.util.ArrayList;
import static java.util.Arrays.stream;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestList<T> {

    private final List<T> list = new ArrayList<>(4);
    private int index = -1;

    @Override
    public String toString() {
        return "size=" + list.size() + ", index=" + index;
    }

    public void add(T event) {
        list.add(event);
    }

    public <R> R get(int index) {
        return (R) list.get(index);
    }

    public void remove(T link) {
        list.remove(link);
    }

    public int size() {
        return list.size();
    }

    public void assertClasses(Class<?>... expectedTypes) {
        assertEquals(expectedTypes.length, list.size());
        stream(expectedTypes).forEach(t -> assertTrue(getMessage(list), nextClass().isAssignableFrom(t)));
    }

    public void assertObjects(T... events) {
        assertEquals(events.length, list.size());
        stream(events).forEach(e -> assertEquals(getMessage(list), e, next()));
    }

    private Class<?> nextClass() {
        return next().getClass();
    }

    public T next() {
        return list.get(++index);
    }

    private String getMessage(List<T> list) {
        StringBuilder builder = null;
        for (int i = 0; i < list.size(); ++i)
            if (builder == null)
                builder = new StringBuilder().append("actual: ").append(list.get(i).getClass().getSimpleName());
            else
                builder.append(", ").append(list.get(i).getClass().getSimpleName());
        return builder == null ? "" : builder.toString();
    }
}
