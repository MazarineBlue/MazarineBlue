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
package org.mazarineblue.eventnotifier;

import java.util.ArrayDeque;
import java.util.Deque;
import static org.mazarineblue.eventnotifier.EventServiceUtil.checkSubscriber;
import org.mazarineblue.eventnotifier.exceptions.SubscriberNotRegisteredException;

public class StackEventService<E extends Event>
        implements EventService<E> {

    private final Deque<Subscriber<E>> stack;

    public StackEventService() {
        this.stack = new ArrayDeque<>(4);
    }

    @Override
    public void publish(E event) {
        for (Subscriber<E> subscriber : stack) {
            subscriber.eventHandler(event);
            if (event.isConsumed())
                break;
        }
    }

    @Override
    public void subscribe(Subscriber<E> subscriber) {
        checkSubscriber(subscriber);
        stack.addFirst(subscriber);
    }

    public void subscribeAfter(Subscriber<E> subscriber, Subscriber<E> after) {
        Deque<Subscriber<E>> tmp = splitStackFrom(after);
        stack.addFirst(subscriber);
        addOnTop(tmp);
    }

    @Override
    public void unsubscribe(Subscriber<E> subscriber) {
        Deque<Subscriber<E>> tmp = splitStackFrom(subscriber);
        tmp.removeFirst();
        addOnTop(tmp);
    }

    private Deque<Subscriber<E>> splitStackFrom(Subscriber<E> seek) {
        Deque<Subscriber<E>> tmp = new ArrayDeque<>(16);
        while (!stack.isEmpty()) {
            moveFirstLink(tmp, stack);
            if (isAtSeekedLink(tmp, seek))
                return tmp;
        }
        throw new SubscriberNotRegisteredException(seek);
    }

    private void addOnTop(Deque<Subscriber<E>> tmp) {
        while (!tmp.isEmpty())
            moveFirstLink(stack, tmp);
    }

    private void moveFirstLink(Deque<Subscriber<E>> dst, Deque<Subscriber<E>> src) {
        dst.addFirst(src.removeFirst());
    }

    private boolean isAtSeekedLink(Deque<Subscriber<E>> tmp, Subscriber<E> subscriber) {
        return tmp.peekFirst().equals(subscriber);
    }
}
