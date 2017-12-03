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
package org.mazarineblue.links;

import static java.util.Arrays.asList;
import java.util.Collection;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.mazarineblue.eventbus.Event;
import org.mazarineblue.eventbus.events.TestEvent;
import org.mazarineblue.eventbus.util.TestPredicate;
import org.mazarineblue.eventdriven.Interpreter;
import org.mazarineblue.eventdriven.util.TestInvoker;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class RecorderLinkTest {

    @Test
    public void getRecording_Intially() {
        RecorderLink link = new RecorderLink(null, null);
        Collection<Event> actual = link.getRecording();
        assertEquals(0, actual.size());
    }

    @Test
    public void getRecording_CatchedEvents() {
        Collection<Event> expected = asList(new TestEvent(), new TestEvent());

        Interpreter interpreter = Interpreter.newInstance();
        TestPredicate<Event> stopCondition = new TestPredicate<>(false);
        RecorderLink link = new RecorderLink(new TestInvoker(interpreter), stopCondition);
        interpreter.addLink(link);
        expected.stream().forEach(link::eventHandler);
        stopCondition.setResult(true);
        link.eventHandler(new TestEvent());
        Collection<Event> actual = link.getRecording();

        assertEquals(expected, actual);
    }
}
