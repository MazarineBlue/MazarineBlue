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
package org.mazarineblue.runners.fitnesse;

import org.junit.After;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.eventdriven.feeds.MemoryFeed;
import org.mazarineblue.eventnotifier.Event;
import org.mazarineblue.executors.Executor;
import org.mazarineblue.executors.ExecutorBuilder;
import org.mazarineblue.executors.ExecutorFactory;
import org.mazarineblue.fs.MemoryFileSystem;
import org.mazarineblue.keyworddriven.LibraryRegistry;
import org.mazarineblue.keyworddriven.events.ExecuteInstructionLineEvent;
import org.mazarineblue.keyworddriven.events.ValidateInstructionLineEvent;
import org.mazarineblue.libraries.fixtures.events.PathEvent;
import org.mazarineblue.libraries.fixtures.exceptions.FixtureNotFoundException;
import org.mazarineblue.libraries.fixtures.exceptions.FixtureNotPublicException;
import org.mazarineblue.libraries.fixtures.exceptions.NoSuchConstructorException;
import org.mazarineblue.runners.fitnesse.engineplugin.FitnesseSubscriber;
import org.mazarineblue.runners.fitnesse.engineplugin.FixtureLoaderSubscriber;
import org.mazarineblue.runners.fitnesse.events.NewInstanceEvent;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class ClassLoaderSubscriberTest {

    private static final String NAMESPACE = "org.mazarineblue.runners.fitnesse.util";

    private FixtureLoaderSubscriber subscriber;

    @Before
    public void setup() {
        subscriber = new FixtureLoaderSubscriber(NAMESPACE);
    }

    @After
    public void teardown() {
        subscriber = null;
    }

    @Test(expected = FixtureNotFoundException.class)
    public void eventHandler_NoPath_FixtureNotFoundException()
            throws Exception {
        subscriber = new FixtureLoaderSubscriber("a", "b");
        subscriber.eventHandler(new NewInstanceEvent("testActor", "PublicFixture"));
    }

    @Test(expected = FixtureNotFoundException.class)
    public void eventHandler_NonExistingFixture_FixtureNotFoundException()
            throws Exception {
        subscriber.eventHandler(new NewInstanceEvent("testActor", "NonExistingFixture"));
    }

    @Test(expected = FixtureNotPublicException.class)
    public void eventHandler_NonPublicFixture_FixtureNotFoundException()
            throws Exception {
        subscriber.eventHandler(new NewInstanceEvent("testActor", "PackageFixture"));
    }

    @Test(expected = NoSuchConstructorException.class)
    public void eventHandler_NonExistingConstructorFixture_FixtureNotFoundException()
            throws Exception {
        subscriber.eventHandler(new NewInstanceEvent("testActor", "PublicFixture", "arg1", "arg2", "arg3"));
    }

    @Test
    public void test()
            throws Exception {
        ExecutorBuilder builder = new ExecutorBuilder();
        builder.setFileSystem(new MemoryFileSystem());

        ExecutorFactory factory = ExecutorFactory.newInstance(builder);
        factory.addLinkAfterVariableParser(() -> new FitnesseSubscriber());
        factory.addLinkAfterVariableParser(() -> new FixtureLoaderSubscriber());
        factory.addLink(() -> new LibraryRegistry());
        Executor executor = factory.create();

        Event[] arr = new Event[]{
            new PathEvent(NAMESPACE),
            new NewInstanceEvent("test", "PublicFixture", "arg1", "arg2"),
            new NewInstanceEvent("test", "PublicFixture", "arg1", "arg2"),
            new ValidateInstructionLineEvent(".testB", "a", "b"),
            new ExecuteInstructionLineEvent(".testB", "a", "b")
        };
        executor.execute(new MemoryFeed(arr));
        for (Event e : arr)
            assertTrue(e.isConsumed());
    }
}
