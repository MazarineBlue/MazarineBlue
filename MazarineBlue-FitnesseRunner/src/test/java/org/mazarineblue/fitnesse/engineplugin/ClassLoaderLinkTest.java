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

import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.mazarineblue.eventbus.Event;
import org.mazarineblue.eventbus.link.EventBusLink;
import org.mazarineblue.eventdriven.Interpreter;
import org.mazarineblue.eventdriven.feeds.MemoryFeed;
import org.mazarineblue.fitnesse.engineplugin.exceptions.FixtureNotFoundException;
import org.mazarineblue.fitnesse.engineplugin.exceptions.FixtureNotPublicException;
import org.mazarineblue.fitnesse.engineplugin.exceptions.NoSuchConstructorException;
import org.mazarineblue.fitnesse.events.NewInstanceEvent;
import org.mazarineblue.fitnesse.events.PathEvent;
import org.mazarineblue.keyworddriven.LibraryRegistry;
import org.mazarineblue.keyworddriven.events.ExecuteInstructionLineEvent;
import org.mazarineblue.keyworddriven.events.ValidateInstructionLineEvent;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class ClassLoaderLinkTest {

    @Test(expected = FixtureNotFoundException.class)
    public void eventHandler_NoPath_FixtureNotFoundException()
            throws Exception {
        FixtureLoaderLink link = new FixtureLoaderLink("a", "b");
        link.eventHandler(new NewInstanceEvent("testActor", "PublicFixture"));
    }

    @Test(expected = FixtureNotFoundException.class)
    public void eventHandler_NonExistingFixture_FixtureNotFoundException()
            throws Exception {
        FixtureLoaderLink link = new FixtureLoaderLink("org.mazarineblue.fitnesse.util");
        link.eventHandler(new NewInstanceEvent("testActor", "NonExistingFixture"));
    }

    @Test(expected = FixtureNotPublicException.class)
    public void eventHandler_NonPublicFixture_FixtureNotFoundException()
            throws Exception {
        FixtureLoaderLink link = new FixtureLoaderLink("org.mazarineblue.fitnesse.util");
        link.eventHandler(new NewInstanceEvent("testActor", "PackageFixture"));
    }

    @Test(expected = NoSuchConstructorException.class)
    public void eventHandler_NonExistingConstructorFixture_FixtureNotFoundException()
            throws Exception {
        FixtureLoaderLink link = new FixtureLoaderLink("org.mazarineblue.fitnesse.util");
        link.eventHandler(new NewInstanceEvent("testActor", "PublicFixture", "arg1", "arg2", "arg3"));
    }

    @Test
    public void test()
            throws Exception {
        Interpreter interpreter = Interpreter.getDefaultInstance();
        interpreter.addLink(new FixtureLoaderLink());
        interpreter.addLink(new EventBusLink(null, null, new LibraryRegistry()));
        Event[] arr = new Event[]{
            new PathEvent("org.mazarineblue.fitnesse.util"),
            new NewInstanceEvent("test", "PublicFixture", "arg1", "arg2"),
            new NewInstanceEvent("test", "PublicFixture", "arg1", "arg2"),
            new ValidateInstructionLineEvent(".testB", "a", "b"),
            new ExecuteInstructionLineEvent(".testB", "a", "b")
        };
        interpreter.execute(new MemoryFeed(arr));
        for (Event e : arr)
            assertTrue(e.isConsumed());
    }
}
