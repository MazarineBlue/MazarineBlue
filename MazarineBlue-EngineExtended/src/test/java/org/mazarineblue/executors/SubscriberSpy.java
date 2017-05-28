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
package org.mazarineblue.executors;

import java.util.ArrayList;
import java.util.List;
import org.mazarineblue.eventbus.Event;
import org.mazarineblue.eventbus.Subscriber;

public class SubscriberSpy
        implements Subscriber<Event> {

    private List<Event> list = new ArrayList<>();

    @Override
    public String toString() {
        return "count=" + list.size();
    }

    @Override
    public void eventHandler(Event event) {
        list.add(event);
    }

    public int getCount() {
        return list.size();
    }
}
