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
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mazarineblue.eventdriven.feeds.MemoryFeed;
import org.mazarineblue.executors.util.AbstractExecutorTestHelper;
import org.mazarineblue.keyworddriven.events.ExecuteInstructionLineEvent;
import org.mazarineblue.libraries.test.events.SetTestListenerEvent;
import org.mazarineblue.libraries.test.util.ResultCollectorTestListener;
import org.mazarineblue.variablestore.events.GetVariableEvent;

@Ignore
public class BDDTest
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
        GetVariableEvent e = new GetVariableEvent("var");
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Set", "var", 0),
                               new ExecuteInstructionLineEvent("Test set"),
                               new ExecuteInstructionLineEvent(" Test", "Test 1"),
                               new ExecuteInstructionLineEvent("Given", "put the system in a known state"),
                               new ExecuteInstructionLineEvent(" When", "a key action is performed"),
                               new ExecuteInstructionLineEvent(" Then", "some outcome is observed"),
                               new ExecuteInstructionLineEvent("End test set"),
                               new ExecuteInstructionLineEvent("Function", "Given put the system in a known state"),
                               new ExecuteInstructionLineEvent("Set global", "var", "=Evaluate expression", "1+$var"),
                               new ExecuteInstructionLineEvent("End function"),
                               new ExecuteInstructionLineEvent("Function", "When a key action is performed"),
                               new ExecuteInstructionLineEvent("Set global", "var", "=Evaluate expression", "1+$var"),
                               new ExecuteInstructionLineEvent("End function"),
                               new ExecuteInstructionLineEvent("Function", "Then some outcome is observed"),
                               new ExecuteInstructionLineEvent("Set global", "var", "=Evaluate expression", "1+$var"),
                               new ExecuteInstructionLineEvent("End function"),
                               new ExecuteInstructionLineEvent("Run tests"),
                               e));
        assertSuccess();
        results.throwFirstException();
        assertEquals(1, results.getTestCount());
        assertEquals(0, results.getFailedTestCount());
        assertEquals(3, e.getValue());
    }

    @Test
    public void test2() {
        GetVariableEvent e = new GetVariableEvent("var");
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Set", "var", 0),
                               new ExecuteInstructionLineEvent("Test set"),
                               new ExecuteInstructionLineEvent(" Test", "Test 1"),
                               new ExecuteInstructionLineEvent("Given", "put the system in a known state"),
                               new ExecuteInstructionLineEvent("  And", "put the system in a known state"),
                               new ExecuteInstructionLineEvent(" When", "a key action is performed"),
                               new ExecuteInstructionLineEvent("  And", "a key action is performed"),
                               new ExecuteInstructionLineEvent(" Then", "some outcome is observed"),
                               new ExecuteInstructionLineEvent("  And", "some outcome is observed"),
                               new ExecuteInstructionLineEvent("End test set"),
                               new ExecuteInstructionLineEvent("Function", "Given put the system in a known state"),
                               new ExecuteInstructionLineEvent("Set global", "var", "=Evaluate expression", "1+$var"),
                               new ExecuteInstructionLineEvent("End function"),
                               new ExecuteInstructionLineEvent("Function", "When a key action is performed"),
                               new ExecuteInstructionLineEvent("Set global", "var", "=Evaluate expression", "1+$var"),
                               new ExecuteInstructionLineEvent("End function"),
                               new ExecuteInstructionLineEvent("Function", "Then some outcome is observed"),
                               new ExecuteInstructionLineEvent("Set global", "var", "=Evaluate expression", "1+$var"),
                               new ExecuteInstructionLineEvent("End function"),
                               new ExecuteInstructionLineEvent("Run tests"),
                               e));
        assertSuccess();
        results.throwFirstException();
        assertEquals(1, results.getTestCount());
        assertEquals(0, results.getFailedTestCount());
        assertEquals(6, e.getValue());
    }

    @Test
    public void test3() {
        GetVariableEvent e = new GetVariableEvent("var");
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Set", "var", 0),
                               new ExecuteInstructionLineEvent("Test set"),
                               new ExecuteInstructionLineEvent(" Test", "Test 1"),
                               new ExecuteInstructionLineEvent("Given", "the \"first\" state"),
                               new ExecuteInstructionLineEvent(" When", "action \"button clicked\" performed"),
                               new ExecuteInstructionLineEvent(" Then", "outcome \"second\" observed"),
                               new ExecuteInstructionLineEvent("End test set"),
                               new ExecuteInstructionLineEvent("Function", "Given state", "argument"),
                               new ExecuteInstructionLineEvent("Set global", "var", "=Evaluate expression", "1+$var"),
                               new ExecuteInstructionLineEvent("End function"),
                               new ExecuteInstructionLineEvent("Function", "When action performed", "argument"),
                               new ExecuteInstructionLineEvent("Set global", "var", "=Evaluate expression", "1+$var"),
                               new ExecuteInstructionLineEvent("End function"),
                               new ExecuteInstructionLineEvent("Function", "Then outcome observed", "argument"),
                               new ExecuteInstructionLineEvent("Set global", "var", "=Evaluate expression", "1+$var"),
                               new ExecuteInstructionLineEvent("End function"),
                               new ExecuteInstructionLineEvent("Run tests"),
                               e));
        assertSuccess();
        results.throwFirstException();
        assertEquals(1, results.getTestCount());
        assertEquals(0, results.getFailedTestCount());
        assertEquals(3, e.getValue());
    }
}
