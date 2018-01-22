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
import org.mazarineblue.libraries.fixtures.events.PathEvent;
import org.mazarineblue.runners.fitnesse.events.AssignFitnesseEvent;
import org.mazarineblue.runners.fitnesse.events.CallFitnesseEvent;
import org.mazarineblue.runners.fitnesse.events.CreateFitnesseEvent;
import org.mazarineblue.runners.fitnesse.events.NewInstanceEvent;

public class EventsTest {

    @Test
    public void testAssignFitnesseEvent() {
        Event e = new AssignFitnesseEvent("symbol", "value");
        assertEquals("symbol=value", e.toString());
        assertEquals("symbol=symbol, value=value", e.message());
        assertEquals("", e.responce());
    }

    @Test
    public void testCallFitnesseEvent_BaseLine() {
        CallFitnesseEvent e = new CallFitnesseEvent("instance", "method", "argument 1", "argument 2");

        assertEquals("instance, method, [argument 1, argument 2]", e.toString());
        assertEquals("instance=instance, method=method, arguments=[argument 1, argument 2]", e.message());
        assertEquals("result=null", e.responce());
    }

    @Test
    public void testCallFitnesseEvent_ResultSet() {
        CallFitnesseEvent e = new CallFitnesseEvent("instance", "method", "argument 1", "argument 2");
        e.setResult(5);

        assertEquals("instance, method, [argument 1, argument 2]", e.toString());
        assertEquals("instance=instance, method=method, arguments=[argument 1, argument 2]", e.message());
        assertEquals("result=5", e.responce());
    }

    @Test
    public void testCreateFitnesseEvent() {
        Event e = new CreateFitnesseEvent("instance", "fixture", "argument 1", "argument 2");
        assertEquals("instance, fixture, [argument 1, argument 2]", e.toString());
        assertEquals("instance=instance, fixture=fixture, arguments=[argument 1, argument 2]", e.message());
        assertEquals("", e.responce());
    }

    @Test
    public void testNewInstanceEvent() {
        Event e = new NewInstanceEvent("actor", "fixture", "argument 1", "argument 2");
        assertEquals("actor, fixture, [argument 1, argument 2]", e.toString());
        assertEquals("actor=actor, fixture=fixture, arguments=[argument 1, argument 2]", e.message());
        assertEquals("", e.responce());
    }

    @Test
    public void testPathEvent() {
        Event e = new PathEvent("path");
        assertEquals("path", e.toString());
        assertEquals("path=path", e.message());
        assertEquals("", e.responce());
    }

    @Test
    public void testPathFitnesseEvent() {
        Event e = new PathEvent("path");
        assertEquals("path", e.toString());
        assertEquals("path=path", e.message());
        assertEquals("", e.responce());
    }
}
