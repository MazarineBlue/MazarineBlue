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
package org.mazarineblue.logger;

import java.util.HashMap;
import java.util.Map;

class WrapperCounter<T, R> {

    private final Map<T, Counter<R>> map = new HashMap<>();

    R add(T originalListener, R wrappedListener) {
        Counter<R> counter = map.containsKey(originalListener) ? map.get(originalListener)
                : new Counter<>(wrappedListener);
        counter.increment();
        map.put(originalListener, counter);
        return wrappedListener;
    }

    R remove(T originalListener) {
        Counter<R> counter = map.get(originalListener);
        counter.decrement();
        if (counter.isUnused())
            map.remove(originalListener);
        return counter.getWrappedListener();
    }

    private static class Counter<U> {

        private int count = 0;
        private final U wrappedListener;

        private Counter(U wrappedListener) {
            this.wrappedListener = wrappedListener;
        }

        @Override
        public String toString() {
            return "count=" + count;
        }

        private void increment() {
            ++count;
        }

        private void decrement() {
            --count;
        }

        private boolean isUnused() {
            return count == 0;
        }

        private U getWrappedListener() {
            return wrappedListener;
        }
    }
}
