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
package org.mazarineblue.runners.fitnesse;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.mazarineblue.eventnotifier.Event;
import org.mazarineblue.runners.fitnesse.events.AssignFitnesseEvent;
import org.mazarineblue.runners.fitnesse.events.CallFitnesseEvent;
import org.mazarineblue.runners.fitnesse.events.CreateFitnesseEvent;
import org.mazarineblue.runners.fitnesse.events.NewInstanceEvent;

public class CloneTest {

    @Test
    public void clone_AssignFitnesseEvent() {
        Event e = new AssignFitnesseEvent("sybol", "value");
        assertEquals(e, Event.clone(e));
    }

    @Test
    public void clone_CallFitnesseEvent_WithoutResult() {
        Event e = new CallFitnesseEvent("instance", "method", "arg1", "arg2");
        assertEquals(e, Event.clone(e));
    }

    @Test
    public void clone_CallFitnesseEvent_WithResult() {
        CallFitnesseEvent e = new CallFitnesseEvent("instance", "method", "arg1", "arg2");
        e.setResult(1);
        assertEquals(e, Event.clone(e));
    }

    @Test
    public void clone_CreateFitnesseEvent() {
        Event e = new CreateFitnesseEvent("instance", "fixture", "arg1", "arg2");
        assertEquals(e, Event.clone(e));
    }

    @Test
    public void clone_NewInstanceEvent() {
        Event e = new NewInstanceEvent("actor", "fixture", "arg1", "arg2");
        assertEquals(e, Event.clone(e));
    }
}
