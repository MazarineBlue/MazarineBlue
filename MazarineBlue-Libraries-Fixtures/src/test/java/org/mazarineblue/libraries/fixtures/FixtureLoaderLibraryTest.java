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

import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.mazarineblue.eventdriven.feeds.MemoryFeed;
import org.mazarineblue.eventnotifier.Event;
import org.mazarineblue.executors.util.AbstractExecutorTestHelper;
import org.mazarineblue.keyworddriven.events.ExecuteInstructionLineEvent;
import org.mazarineblue.keyworddriven.exceptions.InstructionNotFoundException;
import org.mazarineblue.libraries.fixtures.events.PathEvent;
import org.mazarineblue.libraries.fixtures.exceptions.FixtureNotFoundException;
import org.mazarineblue.libraries.fixtures.exceptions.FixtureNotPublicException;
import org.mazarineblue.libraries.fixtures.exceptions.NoSuchConstructorException;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class FixtureLoaderLibraryTest
        extends AbstractExecutorTestHelper {

    @Test
    public void importFixtureLoaderLibrary() {
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "org.mazarineblue.libraries.fixtures")));
        assertSuccess();
    }

    @Test(expected = FixtureNotFoundException.class)
    public void importFixture_UnkownFixture_NoPaths() {
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "org.mazarineblue.libraries.fixtures"),
                               new ExecuteInstructionLineEvent("Import fixture", "NotFoundFixture")));
        assertFailure();
    }

    @Test(expected = FixtureNotFoundException.class)
    public void importFixture_UnkownFixture_TwoPaths() {
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "org.mazarineblue.libraries.fixtures"),
                               new PathEvent("a"),
                               new PathEvent("b"),
                               new ExecuteInstructionLineEvent("Import fixture", "NotFoundFixture")));
        assertFailure();
    }

    @Test(expected = FixtureNotPublicException.class)
    public void importFixture_FixtureNotPublic() {
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "org.mazarineblue.libraries.fixtures"),
                               new ExecuteInstructionLineEvent("Import fixture", "NotPublicFixture")));
        assertFailure();
    }

    @Test
    public void importFixture_Onces() {
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "org.mazarineblue.libraries.fixtures"),
                               new ExecuteInstructionLineEvent("Import fixture", "LoginDialogDriver", "Bob", "xyzzy")));
        assertSuccess();
    }

    @Test
    public void importFixture_Twice() {
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "org.mazarineblue.libraries.fixtures"),
                               new ExecuteInstructionLineEvent("Import fixture", "LoginDialogDriver", "Bob", "xyzzy"),
                               new ExecuteInstructionLineEvent("Import fixture", "LoginDialogDriver", "Bob", "xyzzy")));
        assertSuccess();
    }

    @Test
    public void importFixture_Conflict() {
        Event e1, e2;
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "org.mazarineblue.libraries.fixtures"),
                               e1 = new PathEvent("a"),
                               e2 = new PathEvent("b"),
                               new ExecuteInstructionLineEvent("Import fixture", "Fixture"),
                               new ExecuteInstructionLineEvent("calling A")));
        assertSuccess();
        assertTrue(e1.isConsumed());
        assertTrue(e2.isConsumed());
    }

    @Test(expected = InstructionNotFoundException.class)
    public void importFixture_MethodNotAvailable() {
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "org.mazarineblue.libraries.fixtures"),
                               new PathEvent("a"),
                               new PathEvent("b"),
                               new ExecuteInstructionLineEvent("Import fixture", "a.Fixture"),
                               new ExecuteInstructionLineEvent("Calling B")));
        assertFailure();
    }

    @Test
    public void importFixture_ConflictOverride() {
        Event e1, e2;
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "org.mazarineblue.libraries.fixtures"),
                               e1 = new PathEvent("a"),
                               e2 = new PathEvent("b"),
                               new ExecuteInstructionLineEvent("Import fixture", "b.Fixture"),
                               new ExecuteInstructionLineEvent("Calling B")));
        assertSuccess();
        assertTrue(e1.isConsumed());
        assertTrue(e2.isConsumed());
    }

    @Test
    public void importFixture_ImportConflictingFixtures() {
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "org.mazarineblue.libraries.fixtures"),
                               new ExecuteInstructionLineEvent("Import fixture", "a.Fixture"),
                               new ExecuteInstructionLineEvent("Import fixture", "b.Fixture"),
                               new ExecuteInstructionLineEvent("Calling A"),
                               new ExecuteInstructionLineEvent("Calling B")));
        assertSuccess();
    }

    @Test(expected = NoSuchConstructorException.class)
    public void importFixture_NoSuchConstructor() {
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "org.mazarineblue.libraries.fixtures"),
                               new ExecuteInstructionLineEvent("Import fixture", "LoginDialogDriver", "Bob", "xyzzy", "abc")));
        assertFailure();
    }

    @Test
    public void callFixtureMethod() {
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "org.mazarineblue.libraries.fixtures"),
                               new ExecuteInstructionLineEvent("Import fixture", "a.Fixture"),
                               new ExecuteInstructionLineEvent("Primatives first", 1, "abc")));
        assertSuccess();
    }
}
