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
package org.mazarineblue.procedures;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.mazarineblue.eventbus.Event;
import org.mazarineblue.eventdriven.Feed;
import org.mazarineblue.utililities.ObjectsUtil;

/**
 * A {@code Procedure} is a group of {@link Events events} that are previously
 * recorded.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
class Procedure
        implements Feed, Serializable {

    private static final long serialVersionUID = 1L;

    private final String name;
    private final List<Event> instructions = new ArrayList<>(4);
    private int index = 0;

    Procedure(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "name = " + name;
    }

    public String getName() {
        return name;
    }

    void add(Event e) {
        instructions.add(e);
    }

    @Override
    public boolean hasNext() {
        return index < instructions.size();
    }

    @Override
    public Event next() {
        Event event = ObjectsUtil.clone(instructions.get(index++));
        event.setConsumed(false);
        return event;
    }

    @Override
    public void done(Event e) {
        // We do not need to reset the feed, because we make copies of the events.
    }

    @Override
    public void reset() {
        index = 0;
    }

    @Override
    public int hashCode() {
        return 7 * 13 * 13
                + 13 * Objects.hashCode(this.name)
                + Objects.hashCode(this.instructions);
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && getClass() == obj.getClass()
                && Objects.equals(this.name, ((Procedure) obj).name)
                && Objects.equals(this.instructions, ((Procedure) obj).instructions);
    }
}
