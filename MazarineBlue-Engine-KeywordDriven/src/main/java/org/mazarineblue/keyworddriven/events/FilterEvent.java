/*
 * Copyright (c) 2015 Specialisterren
 * Copyright (c) Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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
package org.mazarineblue.keyworddriven.events;

import java.util.Objects;
import java.util.function.Predicate;
import org.mazarineblue.eventdriven.InvokerEvent;
import org.mazarineblue.utilities.SerializableClonable;

abstract class FilterEvent<T>
        extends InvokerEvent
        implements Predicate<T> {

    private static final long serialVersionUID = 1L;

    private transient Predicate<T> filter;

    protected FilterEvent() {
        this(lib -> true);
    }

    protected FilterEvent(Predicate<T> filter) {
        this.filter = filter;
    }

    @Override
    public boolean test(T t) {
        return filter.test(t);
    }

    @Override
    public int hashCode() {
        return 5 * 17
                + Objects.hashCode(filter);
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj != null && getClass() == obj.getClass()
                && Objects.equals(this.filter, ((FilterEvent) obj).filter);
    }

    @Override
    public <T extends SerializableClonable> void copyTransient(T other) {
        super.copyTransient(other);
        filter = ((FilterEvent) other).filter;
    }
}
