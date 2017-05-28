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
package org.mazarineblue.excel.util;

import java.util.ArrayList;
import java.util.List;
import org.mazarineblue.eventbus.Event;
import org.mazarineblue.eventdriven.Chain;
import org.mazarineblue.eventdriven.Interpreter;
import org.mazarineblue.eventdriven.Invoker;

public class TestInvoker
        implements Invoker {

    private final List<Event> events = new ArrayList<>(4);
    private int index = -1;

    @Override
    public Interpreter interpreter() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Chain chain() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void publish(Event event) {
        events.add(event);
    }

    public Event next() {
        return events.get(++index);
    }
}
