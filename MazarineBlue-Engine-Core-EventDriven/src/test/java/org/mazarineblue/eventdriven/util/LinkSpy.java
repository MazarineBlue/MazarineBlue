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
package org.mazarineblue.eventdriven.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import org.mazarineblue.eventbus.Event;
import static org.mazarineblue.eventbus.Event.matchesNoneAutoConsumable;
import org.mazarineblue.eventdriven.Link;
import org.mazarineblue.utililities.ID;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class LinkSpy
        extends Link {

    private final Predicate<Event> condition;
    private final ID id = new ID();
    private final List<Event> list = new ArrayList<>(4);
    private int index = -1;

    /**
     * Record all events.
     */
    public LinkSpy() {
        this(matchesNoneAutoConsumable());
    }

    /**
     * Records all event that match the specified conditon.
     *
     * @param condition records the event, if it evaluates {@code true} using
     *                  this condition.
     */
    public LinkSpy(Predicate<Event> condition) {
        this.condition = condition;
    }

    @Override
    public String toString() {
        return "LinkSpy{size = " + list.size() + "}";
    }

    @Override
    public void eventHandler(Event event) {
        if (condition.test(event))
            list.add(event);
    }

    public int size() {
        return list.size();
    }

    public Event next() {
        return list.get(++index);
    }

    @Override
    public int hashCode() {
        return 7 * 29
                + Objects.hashCode(id);
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj != null && getClass() == obj.getClass() && Objects.equals(this.id, ((LinkSpy) obj).id);
    }
}
