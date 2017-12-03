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
package org.mazarineblue.fitnesse;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import org.junit.Test;
import org.mazarineblue.fitnesse.events.CallFitnesseEvent;
import org.mazarineblue.utililities.util.TestHashCodeAndEquals;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class CallFitnesseEventTest
        extends TestHashCodeAndEquals<CallFitnesseEvent> {

    @Test
    public void hashCode_DifferentInstances() {
        CallFitnesseEvent a = new CallFitnesseEvent("instance", "method", "arg1", "arg2");
        CallFitnesseEvent b = new CallFitnesseEvent("ecnatsni", "method", "arg1", "arg2");
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void hashCode_DifferentMethod() {
        CallFitnesseEvent a = new CallFitnesseEvent("instance", "method", "arg1", "arg2");
        CallFitnesseEvent b = new CallFitnesseEvent("instance", "dohtem", "arg1", "arg2");
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void equals_DifferentInstances() {
        CallFitnesseEvent a = new CallFitnesseEvent("instance", "method", "arg1", "arg2");
        CallFitnesseEvent b = new CallFitnesseEvent("ecnatsni", "method", "arg1", "arg2");
        assertFalse(a.equals(b));
    }

    @Test
    public void equals_DifferentMethod() {
        CallFitnesseEvent a = new CallFitnesseEvent("instance", "method", "arg1", "arg2");
        CallFitnesseEvent b = new CallFitnesseEvent("instance", "dohtem", "arg1", "arg2");
        assertFalse(a.equals(b));
    }

    @Override
    protected CallFitnesseEvent getObject() {
        return new CallFitnesseEvent("instance", "method", "arg1", "arg2");
    }

    @Override
    protected CallFitnesseEvent getDifferentObject() {
        return new CallFitnesseEvent("instance", "method", "arg2", "arg1");
    }
}
