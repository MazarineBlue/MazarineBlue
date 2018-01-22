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
package org.mazarineblue.eventdriven.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.mazarineblue.eventdriven.Chain;
import org.mazarineblue.eventdriven.Invoker;
import org.mazarineblue.eventdriven.Processor;
import org.mazarineblue.eventnotifier.Event;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class TestInvoker
        implements Invoker {

    private final List<Event> events = new ArrayList<>(4);
    private int index = -1;
    private final Processor processor;
    private String identifier;

    public TestInvoker() {
        this(null);
    }

    public TestInvoker(Processor processor) {
        this.processor = processor;
    }

    @Override
    public String toString() {
        return "identifier=" + identifier;
    }

    public TestInvoker setIdentifier(String identifier) {
        this.identifier = identifier;
        return this;
    }

    @Override
    public Processor processor() {
        return processor;
    }

    @Override
    public Chain chain() {
        return processor;
    }

    @Override
    public void publish(Event event) {
        events.add(event);
    }

    public Event next() {
        return events.get(++index);
    }

    @Override
    public int hashCode() {
        return 53 * 3 + Objects.hashCode(this.identifier);
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj != null && getClass() == obj.getClass()
                && Objects.equals(this.identifier, ((TestInvoker) obj).identifier);
    }
}
