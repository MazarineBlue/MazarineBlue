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
package org.mazarineblue.eventdriven.util;

import java.util.function.Consumer;
import org.mazarineblue.eventbus.Event;
import org.mazarineblue.eventdriven.Link;
import org.mazarineblue.function.Condition;

public class PerformActionLink
        extends Link {

    private final Consumer<Event> consumer;
    private final Condition<Event> condition;

    public PerformActionLink(Consumer<Event> consumer, Condition<Event> condition) {
        this.consumer = consumer;
        this.condition = condition;
    }

    @Override
    public void eventHandler(Event event) {
        if (condition.apply(event))
            consumer.accept(event);
    }
}
