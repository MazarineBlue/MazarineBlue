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
package org.mazarineblue.eventdriven;

import java.util.function.Supplier;
import org.mazarineblue.eventbus.Event;
import org.mazarineblue.eventbus.EventHandler;
import org.mazarineblue.function.Condition;

public class ExceptionThrowingLink
        extends Link {

    private final Supplier<RuntimeException> supplier;
    private final Condition<Event> condition;

    public ExceptionThrowingLink() {
        this(() -> new RuntimeException());
    }

    public ExceptionThrowingLink(Supplier<RuntimeException> supplier) {
        this(supplier, t -> true);
    }

    public ExceptionThrowingLink(Supplier<RuntimeException> supplier, Condition<Event> condition) {
        this.supplier = supplier;
        this.condition = condition;
    }

    @EventHandler
    @Override
    public void eventHandler(Event e) {
        if (condition.apply(e))
            throw supplier.get();
    }
}
