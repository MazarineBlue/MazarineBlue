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
package org.mazarineblue.eventdriven;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.eventbus.events.TestEvent;
import org.mazarineblue.eventdriven.events.ExceptionThrownEvent;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class ExceptionThrownEventTest {

    private RuntimeException ex;
    private TestEvent e;
    private ExceptionThrownEvent a;

    @Before
    public void setup() {
        ex = new RuntimeException();
        e = new TestEvent();
        a = new ExceptionThrownEvent(e, ex);
    }

    @After
    public void teardown() {
        ex = null;
        e = null;
        a = null;
    }

    @Test
    public void equals_Null() {
        assertNotNull(a);
    }

    @Test
    public void equals_DifferentClasses() {
        assertNotEquals(a, "");
    }

    @Test
    public void equals_DifferentEvents() {
        ExceptionThrownEvent b = new ExceptionThrownEvent(new TestEvent(), ex);
        assertNotEquals(a, b);
    }

    @Test
    public void hashCode_DifferentEvents() {
        ExceptionThrownEvent b = new ExceptionThrownEvent(new TestEvent(), ex);
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void equals_DifferentExceptions() {
        ExceptionThrownEvent b = new ExceptionThrownEvent(e, new RuntimeException());
        assertNotEquals(a, b);
    }

    @Test
    public void hashCode_DifferentExceptions() {
        ExceptionThrownEvent b = new ExceptionThrownEvent(e, new RuntimeException());
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void equals_IdenticalExceptionThrownEvents() {
        ExceptionThrownEvent b = new ExceptionThrownEvent(e, ex);
        assertEquals(a, b);
    }

    @Test
    public void hashCode_IdenticalExceptionThrownEvents() {
        ExceptionThrownEvent b = new ExceptionThrownEvent(e, ex);
        assertEquals(a.hashCode(), b.hashCode());
    }
}
