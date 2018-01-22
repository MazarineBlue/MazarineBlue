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

import java.util.function.Predicate;
import org.mazarineblue.eventnotifier.Event;
import org.mazarineblue.eventnotifier.Subscriber;
import org.mazarineblue.utilities.ID;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class SubscriberSpy<E extends Event>
        implements Subscriber<E> {

    private final Predicate<E> condition;
    private final ID id = new ID();
    private final TestList<E> list = new TestList<>();

    /**
     * Records all event that match the specified condition.
     *
     * @param condition records the event, if it evaluates {@code true} using
     * this condition.
     */
    public SubscriberSpy(Predicate<E> condition) {
        this.condition = condition;
    }

    @Override
    public String toString() {
        return "ID=" + id + "count=" + list.size();
    }

    @Override
    public void eventHandler(E event) {
        if (condition.test(event))
            list.add(event);
    }

    public void assertEvents(E... events) {
        list.assertObjects(events);
    }

    public void assertClasses(Class<?>... expectedTypes) {
        list.assertClasses(expectedTypes);
    }

    public int getCount() {
        return list.size();
    }
}
