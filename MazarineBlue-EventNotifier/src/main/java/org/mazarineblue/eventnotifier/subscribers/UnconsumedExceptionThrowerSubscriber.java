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
package org.mazarineblue.eventnotifier.subscribers;

import java.util.function.Predicate;
import org.mazarineblue.eventnotifier.Event;
import org.mazarineblue.eventnotifier.Subscriber;
import org.mazarineblue.eventnotifier.exceptions.UnconsumedEventException;

/**
 * A {@code UnconsumedExceptionThrowerLink} is a {@code TypeBasedLink} that
 * will check if events are consumed and throws an
 * {@link UnconsumedEventException} otherwise.
 *
 * @throws UnconsumedEventException when the passed event was not consumed,
 *                                   unless the event is on the ignore list.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class UnconsumedExceptionThrowerSubscriber<E extends Event>
        extends ExceptionThrowingSubscriber<E> {

    /**
     * Constructs an {@link Subscriber} that checks if {@link Event Events} are
     * consumed and throws an {@link UnconsumedEventException}, when the event
     * is not consumed.
     *
     * @param condition throws an exception if this condition evaluates true.
     */
    public UnconsumedExceptionThrowerSubscriber(Predicate<E> condition) {
        super(condition.and(e -> !e.isConsumed()), UnconsumedEventException::new);
    }
}
