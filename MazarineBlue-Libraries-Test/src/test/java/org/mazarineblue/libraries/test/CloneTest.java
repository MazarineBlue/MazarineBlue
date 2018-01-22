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
package org.mazarineblue.libraries.test;

import static org.junit.Assert.assertEquals;
import org.mazarineblue.eventnotifier.Event;
import org.mazarineblue.libraries.test.events.ExecuteSetupEvent;
import org.mazarineblue.libraries.test.events.ExecuteTestEvent;
import org.mazarineblue.libraries.test.events.SetTestListenerEvent;
import org.mazarineblue.libraries.test.model.listeners.TestListener;
import org.mazarineblue.libraries.test.model.suites.Suite;
import org.mazarineblue.libraries.test.model.tests.Test;
import org.mazarineblue.libraries.test.util.NamedDummyTestListener;
import org.mazarineblue.utilities.ObjectsUtil;

public class CloneTest {

    @org.junit.Test
    public void clone_SetTestListenerEvent() {
        TestListener listener = new NamedDummyTestListener("listener");
        Event e = new SetTestListenerEvent(listener);
        assertEquals(e, ObjectsUtil.clone(e));
    }

    @org.junit.Test
    public void clone_SuiteEvent() {
        Suite g = Suite.newInstance(null, "global");
        Suite s = Suite.newInstance(g, "suite");
        Event e = new ExecuteSetupEvent(s);
        assertEquals(e, ObjectsUtil.clone(e));
    }

    @org.junit.Test
    public void clone_TestEvent() {
        Suite g = Suite.newInstance(null, "global");
        Suite s = Suite.newInstance(g, "suite");
        Test t = Test.newInstance(s, "test");
        Event e = new ExecuteTestEvent(t);
        assertEquals(e, ObjectsUtil.clone(e));
    }
}
