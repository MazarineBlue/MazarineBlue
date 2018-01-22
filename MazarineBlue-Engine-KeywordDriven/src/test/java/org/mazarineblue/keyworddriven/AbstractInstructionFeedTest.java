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
package org.mazarineblue.keyworddriven;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.mazarineblue.eventnotifier.Event;
import org.mazarineblue.keyworddriven.events.ExecuteInstructionLineEvent;
import org.mazarineblue.keyworddriven.events.ValidateInstructionLineEvent;
import org.mazarineblue.keyworddriven.util.events.TestExecuteInstructionLineEvent;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class AbstractInstructionFeedTest {

    @Test
    public void createEvent_NormalOperation_ReturnsExecuteInstructionLineEvent() {
        AbstractInstructionFeed feed = new AbstractInstructionFeedSpy();

        Event expected = new ExecuteInstructionLineEvent("path", "arguments");
        Event actual = feed.createEvent("path", "arguments");
        assertEquals(expected, actual);
    }

    @Test
    public void createEvent_SimulatedException_ReturnsValidateInstructionLineEvent() {
        AbstractInstructionFeed feed = new AbstractInstructionFeedSpy();

        TestExecuteInstructionLineEvent exceptionThrown = new TestExecuteInstructionLineEvent("", "");
        exceptionThrown.setException(new RuntimeException());
        feed.eventHandler(exceptionThrown);

        Event expected = new ValidateInstructionLineEvent("path", "arguments");
        Event actual = feed.createEvent("path", "arguments");
        assertEquals(expected, actual);
    }

    @Test
    public void createEvent_ResetAfterSimulatedException_ReturnsExecuteInstructionLineEvent() {
        AbstractInstructionFeed feed = new AbstractInstructionFeedSpy();

        TestExecuteInstructionLineEvent exceptionThrown = new TestExecuteInstructionLineEvent("", "");
        exceptionThrown.setException(new RuntimeException());
        feed.eventHandler(exceptionThrown);
        feed.reset();

        Event expected = new ExecuteInstructionLineEvent("path", "arguments");
        Event actual = feed.createEvent("path", "arguments");
        assertEquals(expected, actual);
    }

    private static class AbstractInstructionFeedSpy
            extends AbstractInstructionFeed {

        private int linesExecuted = 0;
        private int linesValidated = 0;

        public int getLinesExecuted() {
            return linesExecuted;
        }

        public int getLinesValidated() {
            return linesValidated;
        }

        @Override
        protected ExecuteInstructionLineEvent createExecuteInstructionLineEvent(String path, Object... arguments) {
            ++linesExecuted;
            return super.createExecuteInstructionLineEvent(path, arguments);
        }

        @Override
        protected ValidateInstructionLineEvent createValidateInstructionLineEvent(String path, Object... arguments) {
            ++linesValidated;
            return super.createValidateInstructionLineEvent(path, arguments);
        }

        @Override
        public boolean hasNext() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Event next() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}
