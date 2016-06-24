/*
 * Copyright (c) 2015 Specialisterren
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
package org.mazarineblue;

import java.util.ArrayList;
import java.util.List;
import org.mazarineblue.eventbus.Event;
import org.mazarineblue.keyworddriven.librarymanager.Library;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class CatchEventsLibrary
        extends Library {

    private final List<Event> list = new ArrayList();

    public CatchEventsLibrary() {
        super("test");
    }

    @Override
    protected void setup() {
    }

    @Override
    protected void teardown() {
    }

    @Override
    public void eventHandler(Event e) {
        list.add(e);
    }

    public <T> List<T> fetchEvents(Class<? extends Event> clazz) {
        List<T> list = new ArrayList();
        for (Event e : this.list)
            if (clazz.isAssignableFrom(e.getClass()))
                list.add((T) e);
        return list;
    }
}
