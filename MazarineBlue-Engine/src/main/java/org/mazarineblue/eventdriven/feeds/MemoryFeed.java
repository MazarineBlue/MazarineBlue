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
package org.mazarineblue.eventdriven.feeds;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.mazarineblue.eventbus.Event;
import org.mazarineblue.eventdriven.AbstractFeed;
import org.mazarineblue.eventdriven.exceptions.NoEventsLeftException;

/**
 * A {@code MemoryFeed} is a {@link Feed} that contains a list of
 * {@link Event} in memory.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class MemoryFeed
        extends AbstractFeed {

    private final List<Event> list;
    private int index;

    /**
     * Constructs an empty {@link Feed} with the specified initial capacity.
     *
     * @param initialCapacity the initial capacity of the feed.
     */
    public MemoryFeed(int initialCapacity) {
        list = new ArrayList<>(initialCapacity);
    }

    /**
     * Constructs an {@link Feed} with the specified initial
     * {@link Event Events}, in the order they are returned by the
     * {@code next()} method.
     *
     * @param events the events.
     */
    public MemoryFeed(Event... events) {
        list = new ArrayList<>(Arrays.asList(events));
    }

    @Override
    public String toString() {
        super.toString();
        return "MemoryFeed{ID=" + Integer.toHexString(hashCode()) + ", size=" + list.size() + ", index=" + index + '}';
    }

    /**
     * Adds the specified {@link Event Events} to the end of internal list.
     *
     * @param events the {@code Events} to the list.
     */
    public void add(Event... events) {
        list.addAll(Arrays.asList(events));
    }

    @Override
    public Event next() {
        if (!hasNext())
            throw new NoEventsLeftException(this);
        return list.get(index++);
    }

    @Override
    public final boolean hasNext() {
        return index < list.size();
    }

    @Override
    public void reset() {
        index = 0;
    }
}
