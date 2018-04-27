/*
 * Copyright (c) 2018 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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
package org.mazarineblue.subscribers.recorder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.mazarineblue.eventnotifier.Event;

public class Recording {

    private final List<Event> list;

    Recording(int initialCapacity) {
        list = new ArrayList<>(initialCapacity);
    }

    @Override
    public String toString() {
        return "size = " + list.size();
    }

    int size() {
        return list.size();
    }

    void add(Event event) {
        list.add(event);
    }

    public Collection<Event> getEvents() {
        return list.isEmpty()
                ? Collections.emptyList()
                : Event.clone(list);
    }

    @Override
    public int hashCode() {
        return 17 * 61 * 61
                + 61 * Objects.hashCode(this.list);
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && getClass() == obj.getClass()
                && Objects.equals(this.list, ((Recording) obj).list);
    }
}
