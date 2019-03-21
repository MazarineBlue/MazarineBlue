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
package org.mazarineblue.subscribers;

import static java.util.Arrays.asList;
import java.util.Collection;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.mazarineblue.eventdriven.Processor;
import org.mazarineblue.eventdriven.util.TestInvoker;
import org.mazarineblue.eventnotifier.Event;
import org.mazarineblue.eventnotifier.events.TestEvent;
import org.mazarineblue.utilities.TestPredicate;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class RecorderSubscriberTest {

    @Test
    public void getRecording_Intially() {
        RecorderSubscriber recorder = new RecorderSubscriber(new TestInvoker(Processor.newInstance()),
                                                             new TestPredicate<>(false));
        Collection<Event> actual = recorder.getRecording();
        assertEquals(0, actual.size());
    }

    @Test
    public void getRecording_CatchedEvents() {
        TestEvent e1 = new TestEvent();
        TestEvent e2 = new TestEvent();
        TestEvent e3 = new TestEvent();

        Processor processor = Processor.newInstance();
        TestPredicate<Event> stopCondition = new TestPredicate<>(false);
        RecorderSubscriber recorder = new RecorderSubscriber(new TestInvoker(processor), stopCondition);
        processor.addLink(recorder);

        recorder.eventHandler(e1);
        recorder.eventHandler(e2);
        stopCondition.setResult(true);
        recorder.eventHandler(e3);

        Collection<Event> expected = asList(e1, e2);
        assertEquals(expected, recorder.getRecording());
        assertTrue(e1.isConsumed());
        assertTrue(e2.isConsumed());
        assertFalse(e3.isConsumed());
    }
}
