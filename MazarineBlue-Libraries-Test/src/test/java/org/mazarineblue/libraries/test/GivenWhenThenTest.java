/*
 * Copyright (c) 2018 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.eventdriven.feeds.MemoryFeed;
import org.mazarineblue.executors.util.AbstractExecutorTestHelper;
import org.mazarineblue.keyworddriven.events.ExecuteInstructionLineEvent;
import org.mazarineblue.libraries.test.events.SetTestListenerEvent;
import org.mazarineblue.libraries.test.util.ResultCollectorTestListener;

public class GivenWhenThenTest
        extends AbstractExecutorTestHelper {

    private ResultCollectorTestListener results;

    @Before
    public void setup() {
        results = new ResultCollectorTestListener();
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "org.mazarineblue.libraries.test"),
                               new SetTestListenerEvent(results)));
    }

    @After
    public void teardown() {
        results = null;
    }

    @Test
    public void test1() {
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Test set"),
                               new ExecuteInstructionLineEvent( "Test", "Test 1"),
                               new ExecuteInstructionLineEvent("Given", "put the system in a known state"),
                               new ExecuteInstructionLineEvent( "When", "a key action is performed"),
                               new ExecuteInstructionLineEvent( "Then", "some outcome is observed"),
                               new ExecuteInstructionLineEvent("End test set"),
                               new ExecuteInstructionLineEvent("Run tests")));
    }

    @Test
    public void test2() {
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Test set"),
                               new ExecuteInstructionLineEvent("Test", "Test 1"),
                               new ExecuteInstructionLineEvent("Given", "put the system in a known state"),
                               new ExecuteInstructionLineEvent(  "And", "put the system in a known state"),
                               new ExecuteInstructionLineEvent( "When", "a key action is performed"),
                               new ExecuteInstructionLineEvent(  "And", "a key action is performed"),
                               new ExecuteInstructionLineEvent( "Then", "some outcome is observed"),
                               new ExecuteInstructionLineEvent(  "And", "some outcome is observed"),
                               new ExecuteInstructionLineEvent("End test set"),
                               new ExecuteInstructionLineEvent("Run tests")));
    }

    @Test
    public void test3() {
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Test set"),
                               new ExecuteInstructionLineEvent( "Test", "Test 1"),
                               new ExecuteInstructionLineEvent("Given", "the \"first\" state"),
                               new ExecuteInstructionLineEvent( "When", "a click is performed on the \"second\" button"),
                               new ExecuteInstructionLineEvent( "Then", "the system is in the \"second\" state"),
                               new ExecuteInstructionLineEvent("End test set"),
                               new ExecuteInstructionLineEvent("Run tests")));
    }

    @Test
    public void test3() {
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Test set"),
                               new ExecuteInstructionLineEvent( "Test", "Test 1"),
                               new ExecuteInstructionLineEvent("Given", "put the system in a known state"),
                               new ExecuteInstructionLineEvent( "When", "a key action is performed"),
                               new ExecuteInstructionLineEvent( "Then", "some outcome is observed"),
                               new ExecuteInstructionLineEvent("End test set"),
                               new ExecuteInstructionLineEvent("Run tests")));
    }
}
