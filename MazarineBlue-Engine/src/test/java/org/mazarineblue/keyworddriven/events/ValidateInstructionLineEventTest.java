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

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
@RunWith(HierarchicalContextRunner.class)
public class ValidateInstructionLineEventTest {

    private ValidateInstructionLineEvent e;

    @Before
    public void setup() {
        e = new ValidateInstructionLineEvent("namespace.keyword", "arg1", "arg2", "arg3");
    }

    @After
    public void teardown() {
        e = null;
    }

    @Test
    public void equals_Null() {
        ValidateInstructionLineEvent a = new ValidateInstructionLineEvent("foo", "oof");
        assertNotNull(a);
    }

    @Test
    public void equals_DifferentClasses() {
        ValidateInstructionLineEvent a = new ValidateInstructionLineEvent("foo", "oof");
        assertNotEquals(a, "");
    }

    @Test
    public void equals_DifferentFlags() {
        ValidateInstructionLineEvent a = new ValidateInstructionLineEvent("foo", "oof");
        ValidateInstructionLineEvent b = new ValidateInstructionLineEvent("foo", "oof");
        a.setArgumentsAreIncompatible();
        b.setInstructionsNotFound();
        assertNotEquals(a, b);
    }

    @Test
    public void hashCode_DifferentFlags() {
        ValidateInstructionLineEvent a = new ValidateInstructionLineEvent("foo", "oof");
        ValidateInstructionLineEvent b = new ValidateInstructionLineEvent("foo", "oof");
        a.setArgumentsAreIncompatible();
        b.setInstructionsNotFound();
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void equals_DifferentCustomFlags() {
        ValidateInstructionLineEvent a = new ValidateInstructionLineEvent("foo", "oof");
        ValidateInstructionLineEvent b = new ValidateInstructionLineEvent("foo", "oof");
        a.setUserErrorFlags(1);
        b.setUserErrorFlags(2);
        assertNotEquals(a, b);
    }

    @Test
    public void hashCode_DifferentCustomFlags() {
        ValidateInstructionLineEvent a = new ValidateInstructionLineEvent("foo", "oof");
        ValidateInstructionLineEvent b = new ValidateInstructionLineEvent("foo", "oof");
        a.setUserErrorFlags(1);
        b.setUserErrorFlags(2);
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void equals_IdenticalEvents() {
        ValidateInstructionLineEvent a = new ValidateInstructionLineEvent("foo", "oof");
        ValidateInstructionLineEvent b = new ValidateInstructionLineEvent("foo", "oof");
        a.setArgumentsAreIncompatible();
        b.setArgumentsAreIncompatible();
        a.setUserErrorFlags(7);
        b.setUserErrorFlags(7);
        assertEquals(a, b);
    }

    @Test
    public void hashCode_IdenticalEvents() {
        ValidateInstructionLineEvent a = new ValidateInstructionLineEvent("foo", "oof");
        ValidateInstructionLineEvent b = new ValidateInstructionLineEvent("foo", "oof");
        a.setArgumentsAreIncompatible();
        b.setArgumentsAreIncompatible();
        a.setUserErrorFlags(7);
        b.setUserErrorFlags(7);
        assertEquals(a.hashCode(), b.hashCode());
    }
}
