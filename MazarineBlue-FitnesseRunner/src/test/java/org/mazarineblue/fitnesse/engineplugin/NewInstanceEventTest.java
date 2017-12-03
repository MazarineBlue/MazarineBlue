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
package org.mazarineblue.fitnesse.engineplugin;

import org.junit.After;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.fitnesse.events.NewInstanceEvent;
import org.mazarineblue.utililities.util.TestHashCodeAndEquals;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class NewInstanceEventTest
        extends TestHashCodeAndEquals<NewInstanceEvent> {

    private NewInstanceEvent a;

    @Before
    public void setup() {
        a = new NewInstanceEvent("actor", "fixture", "arg1", "arg2");
    }

    @After
    public void teardown() {
        a = null;
    }

    @Test
    public void hashCode_DifferentInstance() {
        NewInstanceEvent b = new NewInstanceEvent("rotca", "fixture", "arg1", "arg2");
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void hashCode_DifferentFixture() {
        NewInstanceEvent b = new NewInstanceEvent("actor", "erutxif", "arg1", "arg2");
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void equals_DifferentInstance() {
        NewInstanceEvent b = new NewInstanceEvent("rotca", "fixture", "arg1", "arg2");
        assertFalse(a.equals(b));
    }

    @Test
    public void equals_DifferentFixture() {
        NewInstanceEvent b = new NewInstanceEvent("actor", "erutxif", "arg1", "arg2");
        assertFalse(a.equals(b));
    }

    @Override
    protected NewInstanceEvent getIdenticalObject() {
        return new NewInstanceEvent("actor", "fixture", "arg1", "arg2");
    }

    @Override
    protected NewInstanceEvent getObject() {
        return a;
    }

    @Override
    protected NewInstanceEvent getDifferentObject() {
        return new NewInstanceEvent("actor", "fixture", "arg2", "arg1");
    }
}
