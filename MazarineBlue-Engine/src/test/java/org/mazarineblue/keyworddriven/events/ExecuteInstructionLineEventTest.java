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
package org.mazarineblue.keyworddriven.events;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class ExecuteInstructionLineEventTest {

    private TestExecuteInstructionLineEvent e;

    @Before
    public void setup() {
        e = new TestExecuteInstructionLineEvent("namespace.keyword");
    }

    private class TestExecuteInstructionLineEvent
            extends ExecuteInstructionLineEvent {

        private TestExecuteInstructionLineEvent(String path) {
            super(path);
        }

        @Override
        public void setException(RuntimeException exception) {
            super.setException(exception);
        }
    }

    @After
    public void teardown() {
        e = null;
    }

    @Test
    public void equals_Null() {
        ExecuteInstructionLineEvent a = new ExecuteInstructionLineEvent("foo", "oof");
        assertNotNull(a);
    }

    @Test
    public void equals_DifferentClasses() {
        ExecuteInstructionLineEvent a = new ExecuteInstructionLineEvent("foo", "oof");
        assertNotEquals(a, "");
    }

    @Test
    public void equals_DifferentResults() {
        ExecuteInstructionLineEvent a = new ExecuteInstructionLineEvent("foo", "oof");
        ExecuteInstructionLineEvent b = new ExecuteInstructionLineEvent("foo", "oof");
        a.setResult(1);
        b.setResult(2);
        assertNotEquals(a, b);
    }

    @Test
    public void hashCode_DifferentResults() {
        ExecuteInstructionLineEvent a = new ExecuteInstructionLineEvent("foo", "oof");
        ExecuteInstructionLineEvent b = new ExecuteInstructionLineEvent("foo", "oof");
        a.setResult(1);
        b.setResult(2);
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void equals_IdenticalEvents() {
        ExecuteInstructionLineEvent a = new ExecuteInstructionLineEvent("foo", "oof");
        ExecuteInstructionLineEvent b = new ExecuteInstructionLineEvent("foo", "oof");
        a.setResult(2);
        b.setResult(2);
        assertEquals(a, b);
    }

    @Test
    public void hashCode_IdenticalEvents() {
        ExecuteInstructionLineEvent a = new ExecuteInstructionLineEvent("foo", "oof");
        ExecuteInstructionLineEvent b = new ExecuteInstructionLineEvent("foo", "oof");
        a.setResult(2);
        b.setResult(2);
        assertEquals(a.hashCode(), b.hashCode());
    }
}
