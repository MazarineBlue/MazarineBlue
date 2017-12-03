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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.eventbus.Event;
import org.mazarineblue.eventdriven.Interpreter;
import org.mazarineblue.eventdriven.feeds.MemoryFeed;
import org.mazarineblue.eventdriven.util.LinkSpy;
import org.mazarineblue.fitnesse.events.NewInstanceEvent;
import org.mazarineblue.libraries.fixtures.events.PathEvent;
import org.mazarineblue.utililities.util.TestHashCodeAndEquals;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class FixtureLoaderLinkTest
        extends TestHashCodeAndEquals<FixtureLoaderLink> {

    private Interpreter interpreter;
    private FixtureLoaderLink actual;
    private LinkSpy spy;

    @Before
    public void setup() {
        actual = new FixtureLoaderLink();
        spy = new LinkSpy();
        interpreter = Interpreter.newInstance();
        interpreter.addLink(actual);
        interpreter.addLink(spy = new LinkSpy());
    }

    @After
    public void teardown() {
        interpreter = null;
        actual = null;
        spy = null;
    }

    @Test
    public void path() {
        Event event = new PathEvent("info.fitnesse.fixture");
        interpreter.execute(new MemoryFeed(event));
        assertEquals(new PathEvent("info.fitnesse.fixture"), spy.next());
        assertTrue(event.isConsumed());
    }

    @Test
    public void hashCode_DifferentRegistrations()
            throws ReflectiveOperationException {
        interpreter.execute(new MemoryFeed(new NewInstanceEvent("actor", "LoginDialogDriver", "bob", "secret")));
        assertNotEquals(new FixtureLoaderLink().hashCode(), actual.hashCode());
    }

    @Test
    public void equals_DifferentRegistrations()
            throws ReflectiveOperationException {
        interpreter.execute(new MemoryFeed(new NewInstanceEvent("actor", "LoginDialogDriver", "bob", "secret")));
        assertFalse(new FixtureLoaderLink().equals(actual));
    }

    @Override
    protected FixtureLoaderLink getObject() {
        return new FixtureLoaderLink();
    }

    @Override
    protected FixtureLoaderLink getDifferentObject() {
        return new FixtureLoaderLink("foo");
    }
}
