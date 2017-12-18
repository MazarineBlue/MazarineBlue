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
package org.mazarineblue.procedures;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mazarineblue.eventbus.events.TestEvent;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
@RunWith(HierarchicalContextRunner.class)
public class ProcedureTest {

    private Procedure a;

    @Before
    public void setup() {
        a = new Procedure("foo");
    }

    @After
    public void teardown() {
        a = null;
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void equals_Null() {
        assertFalse(a.equals(null));
    }

    @Test
    @SuppressWarnings("IncompatibleEquals")
    public void equals_DifferentClass() {
        assertFalse(a.equals(""));
    }

    @Test
    public void hashCode_DifferentName() {
        Procedure b = new Procedure("oof");
        assertNotEquals(a, b);
    }

    @Test
    public void equals_DifferentName() {
        Procedure b = new Procedure("oof");
        assertNotEquals(a, b);
    }

    @Test
    public void hashCode_DifferentInstructions() {
        Procedure b = new Procedure(a.getName());
        b.add(new TestEvent());
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void equals_DifferentInstructions() {
        Procedure b = new Procedure(a.getName());
        b.add(new TestEvent());
        assertNotEquals(a, b);
    }

    @Test
    public void hashCode_IdenticalContent() {
        Procedure b = new Procedure(a.getName());
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void equals_IdenticalContent() {
        Procedure b = new Procedure(a.getName());
        assertEquals(a, b);
    }
}
