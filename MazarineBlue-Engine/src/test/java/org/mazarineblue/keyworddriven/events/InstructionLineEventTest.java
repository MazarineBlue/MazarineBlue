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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.mazarineblue.keyworddriven.util.TestInstructionLineEvent;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class InstructionLineEventTest {

    @Test
    public void message() {
        InstructionLineEvent e = new InstructionLineEvent("namespace.keyword", "arg1", "arg2", "arg3") {};
        assertEquals("line={namespace.keyword, arg1, arg2, arg3}", e.message());
    }

    @Test
    public void equals_Null() {
        InstructionLineEvent a = new TestInstructionLineEvent("foo", "oof");
        assertNotNull(a);
    }

    @Test
    public void equals_DifferentClass() {
        InstructionLineEvent a = new TestInstructionLineEvent("foo", "oof");
        assertNotEquals(a, "");
    }

    @Test
    public void hashCode_DifferentInstructionLine() {
        InstructionLineEvent a = new TestInstructionLineEvent("foo", "foo");
        InstructionLineEvent b = new TestInstructionLineEvent("oof", "oof");
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void equals_DifferentInstructionLine() {
        InstructionLineEvent a = new TestInstructionLineEvent("foo", "foo");
        InstructionLineEvent b = new TestInstructionLineEvent("oof", "oof");
        assertNotEquals(a, b);
    }

    @Test
    public void hashCode_IdenticalInstructionLine() {
        InstructionLineEvent a = new TestInstructionLineEvent("foo", "foo");
        InstructionLineEvent b = new TestInstructionLineEvent("foo", "foo");
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void equals_IdenticalInstructionLine() {
        InstructionLineEvent a = new TestInstructionLineEvent("foo", "foo");
        InstructionLineEvent b = new TestInstructionLineEvent("foo", "foo");
        assertEquals(a, b);
    }
}
