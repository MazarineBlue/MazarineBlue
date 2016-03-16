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
package org.mazarineblue.eventdriven.listeners;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.mazarineblue.eventdriven.util.InterpreterCreationListenerSpy;

public class InterpreterCreationListenerListTest {

    @Test
    public void interpreterCreated() {
        InterpreterCreationListenerSpy spy1 = new InterpreterCreationListenerSpy();
        InterpreterCreationListenerSpy spy2 = new InterpreterCreationListenerSpy();
        InterpreterCreationListenerList list = new InterpreterCreationListenerList();
        list.addListener(spy1);
        list.addListener(spy2);
        list.interpreterCreated(null);
        list.removeListener(spy2);
        list.interpreterCreated(null);
        assertEquals(2, spy1.count());
        assertEquals(1, spy2.count());
    }
}
