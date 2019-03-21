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
package org.mazarineblue.libraries.test.events;

import java.util.Collection;
import java.util.Objects;
import org.mazarineblue.eventdriven.InvokerEvent;
import org.mazarineblue.eventnotifier.Event;
import org.mazarineblue.libraries.test.model.tests.Test;
import org.mazarineblue.utilities.SerializableClonable;

abstract class AbstractTestEvent
        extends InvokerEvent {

    private static final long serialVersionUID = 1L;

    private transient Test test;

    AbstractTestEvent(Test test) {
        this.test = test;
    }

    @Override
    public String toString() {
        return "suite=" + test.suite().name() + ", name=" + test.name() + ", result" + test.result();
    }

    @Override
    public String message() {
        return "suite=" + test.suite().name() + ", name=" + test.name();
    }

    @Override
    public String responce() {
        return test.result().toString();
    }

    public Collection<Event> getEvents() {
        return test.getEvents();
    }

    @Override
    public int hashCode() {
        return 97 * 7
                + Objects.hashCode(this.test);
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj != null && getClass() == obj.getClass()
                && Objects.equals(this.test, ((AbstractTestEvent) obj).test);
    }

    @Override
    public <T extends SerializableClonable> void copyTransient(T src) {
        super.copyTransient(src);
        this.test = ((AbstractTestEvent) src).test;
    }
}
