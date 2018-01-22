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
package org.mazarineblue.eventnotifier.subscribers;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;
import org.mazarineblue.eventnotifier.Event;
import org.mazarineblue.eventnotifier.Subscriber;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class PerformActionSubscriber
        implements Subscriber<Event> {

    private final Consumer<Event> consumer;
    private final Predicate<Event> condition;

    public PerformActionSubscriber(Consumer<Event> consumer, Predicate<Event> condition) {
        this.consumer = consumer;
        this.condition = condition;
    }

    @Override
    public void eventHandler(Event event) {
        if (condition.test(event))
            consumer.accept(event);
    }

    @Override
    public int hashCode() {
        return 71 * 7
                + Objects.hashCode(this.consumer);
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj.getClass().equals(getClass())
                && Objects.equals(this.consumer, ((PerformActionSubscriber) obj).consumer);
    }
}
