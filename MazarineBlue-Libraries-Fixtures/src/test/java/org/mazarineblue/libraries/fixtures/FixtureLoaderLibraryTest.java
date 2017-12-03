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
package org.mazarineblue.libraries.fixtures;

import org.junit.After;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.eventdriven.Feed;
import org.mazarineblue.eventdriven.events.ExceptionThrownEvent;
import org.mazarineblue.eventdriven.feeds.MemoryFeed;
import org.mazarineblue.executors.FeedExecutor;
import org.mazarineblue.executors.FeedExecutorFactory;
import org.mazarineblue.executors.util.FeedExecutorOutputSpy;
import org.mazarineblue.executors.util.TestFeedExecutorFactory;
import org.mazarineblue.fs.MemoryFileSystem;
import org.mazarineblue.keyworddriven.events.ExecuteInstructionLineEvent;
import org.mazarineblue.keyworddriven.exceptions.InstructionNotFoundException;
import org.mazarineblue.libraries.fixtures.events.PathEvent;
import org.mazarineblue.libraries.fixtures.exceptions.FixtureNotFoundException;
import org.mazarineblue.libraries.fixtures.exceptions.FixtureNotPublicException;
import org.mazarineblue.libraries.fixtures.exceptions.NoSuchConstructorException;
import org.mazarineblue.links.UnconsumedExceptionThrowerLink;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class FixtureLoaderLibraryTest {

    private MemoryFileSystem fs;
    private FeedExecutorOutputSpy output;
    private FeedExecutor executor;

    @Before
    public void setup() {
        fs = new MemoryFileSystem();
        output = new FeedExecutorOutputSpy();
        FeedExecutorFactory factory = TestFeedExecutorFactory.newInstance(fs, output);
        factory.addLinkAfterEventBus(() -> new UnconsumedExceptionThrowerLink(ExceptionThrownEvent.class));
        executor = factory.create();
    }

    @After
    public void teardown() {
        fs = null;
        output = null;
        executor = null;
    }

    @Test
    public void importFixtureLoaderLibrary() {
        Feed feed = new MemoryFeed(
                new ExecuteInstructionLineEvent("Import library", "org.mazarineblue.libraries.fixtures"));
        executor.execute(feed);
        output.throwFirstException();
        assertFalse(executor.containsErrors());
    }

    @Test(expected = FixtureNotFoundException.class)
    public void importFixture_UnkownFixture_NoPaths() {
        Feed feed = new MemoryFeed(
                new ExecuteInstructionLineEvent("Import library", "org.mazarineblue.libraries.fixtures"),
                new ExecuteInstructionLineEvent("Import fixture", "NotFoundFixture"));
        executor.execute(feed);
        output.throwFirstException();
        assertTrue(executor.containsErrors());
    }

    @Test(expected = FixtureNotFoundException.class)
    public void importFixture_UnkownFixture_TwoPaths() {
        Feed feed = new MemoryFeed(
                new ExecuteInstructionLineEvent("Import library", "org.mazarineblue.libraries.fixtures"),
                new PathEvent("a"),
                new PathEvent("b"),
                new ExecuteInstructionLineEvent("Import fixture", "NotFoundFixture"));
        executor.execute(feed);
        output.throwFirstException();
        assertTrue(executor.containsErrors());
    }

    @Test(expected = FixtureNotPublicException.class)
    public void importFixture_FixtureNotPublic() {
        Feed feed = new MemoryFeed(
                new ExecuteInstructionLineEvent("Import library", "org.mazarineblue.libraries.fixtures"),
                new ExecuteInstructionLineEvent("Import fixture", "NotPublicFixture"));
        executor.execute(feed);
        output.throwFirstException();
        assertTrue(executor.containsErrors());
    }

    @Test
    public void importFixture_Onces() {
        Feed feed = new MemoryFeed(
                new ExecuteInstructionLineEvent("Import library", "org.mazarineblue.libraries.fixtures"),
                new ExecuteInstructionLineEvent("Import fixture", "LoginDialogDriver", "Bob", "xyzzy"));
        executor.execute(feed);
        output.throwFirstException();
        assertFalse(executor.containsErrors());
    }

    @Test
    public void importFixture_Twice() {
        Feed feed = new MemoryFeed(
                new ExecuteInstructionLineEvent("Import library", "org.mazarineblue.libraries.fixtures"),
                new ExecuteInstructionLineEvent("Import fixture", "LoginDialogDriver", "Bob", "xyzzy"),
                new ExecuteInstructionLineEvent("Import fixture", "LoginDialogDriver", "Bob", "xyzzy"));
        executor.execute(feed);
        output.throwFirstException();
        assertFalse(executor.containsErrors());
    }

    @Test
    public void importFixture_Conflict() {
        Feed feed = new MemoryFeed(
                new ExecuteInstructionLineEvent("Import library", "org.mazarineblue.libraries.fixtures"),
                new PathEvent("a"),
                new PathEvent("b"),
                new ExecuteInstructionLineEvent("Import fixture", "Fixture"),
                new ExecuteInstructionLineEvent("calling A"));
        executor.execute(feed);
        output.throwFirstException();
        assertFalse(executor.containsErrors());
    }

    @Test(expected = InstructionNotFoundException.class)
    public void importFixture_MethodNotAvailable() {
        Feed feed = new MemoryFeed(
                new ExecuteInstructionLineEvent("Import library", "org.mazarineblue.libraries.fixtures"),
                new PathEvent("a"),
                new PathEvent("b"),
                new ExecuteInstructionLineEvent("Import fixture", "a.Fixture"),
                new ExecuteInstructionLineEvent("Calling B"));
        executor.execute(feed);
        output.throwFirstException();
        assertFalse(executor.containsErrors());
    }

    @Test
    public void importFixture_ConflictOverride() {
        Feed feed = new MemoryFeed(
                new ExecuteInstructionLineEvent("Import library", "org.mazarineblue.libraries.fixtures"),
                new PathEvent("a"),
                new PathEvent("b"),
                new ExecuteInstructionLineEvent("Import fixture", "b.Fixture"),
                new ExecuteInstructionLineEvent("Calling B"));
        executor.execute(feed);
        output.throwFirstException();
        assertFalse(executor.containsErrors());
    }

    @Test
    public void importFixture_ImportConflictingFixtures() {
        Feed feed = new MemoryFeed(
                new ExecuteInstructionLineEvent("Import library", "org.mazarineblue.libraries.fixtures"),
                new ExecuteInstructionLineEvent("Import fixture", "a.Fixture"),
                new ExecuteInstructionLineEvent("Import fixture", "b.Fixture"),
                new ExecuteInstructionLineEvent("Calling A"),
                new ExecuteInstructionLineEvent("Calling B"));
        executor.execute(feed);
        output.throwFirstException();
        assertFalse(executor.containsErrors());
    }

    @Test(expected = NoSuchConstructorException.class)
    public void importFixture_NoSuchConstructor() {
        Feed feed = new MemoryFeed(
                new ExecuteInstructionLineEvent("Import library", "org.mazarineblue.libraries.fixtures"),
                new ExecuteInstructionLineEvent("Import fixture", "LoginDialogDriver", "Bob", "xyzzy", "abc"));
        executor.execute(feed);
        output.throwFirstException();
        assertTrue(executor.containsErrors());
    }

    @Test
    public void callFixtureMethod() {
        Feed feed = new MemoryFeed(
                new ExecuteInstructionLineEvent("Import library", "org.mazarineblue.libraries.fixtures"),
                new ExecuteInstructionLineEvent("Import fixture", "a.Fixture"),
                new ExecuteInstructionLineEvent("Primatives first", 1, "abc")
        );
        executor.execute(feed);
        output.throwFirstException();
        assertFalse(executor.containsErrors());
    }
}
