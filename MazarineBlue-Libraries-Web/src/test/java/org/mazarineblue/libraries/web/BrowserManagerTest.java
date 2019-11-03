/*
 * Copyright (c) Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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
package org.mazarineblue.libraries.web;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.mazarineblue.eventdriven.feeds.MemoryFeed;
import org.mazarineblue.executors.util.AbstractExecutorTestHelper;
import org.mazarineblue.keyworddriven.events.ExecuteInstructionLineEvent;
import org.mazarineblue.libraries.web.exceptions.BrowserInUseException;
import org.mazarineblue.libraries.web.exceptions.BrowserNameConflictException;
import org.mazarineblue.variablestore.events.GetVariableEvent;

public class BrowserManagerTest
        extends AbstractExecutorTestHelper {

    @Test
    public void openAndCloseBrowser() {
        GetVariableEvent openedBrowsers = new GetVariableEvent("Opened browsers");
        GetVariableEvent closedBrowsers = new GetVariableEvent("Closed browsers");
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "test.mazarineblue.libraries.web"),
                               new ExecuteInstructionLineEvent("Run setup checker", "never"),
                               new ExecuteInstructionLineEvent("Setup chrome"),
                               new ExecuteInstructionLineEvent("Set Headless", "true"),
                               new ExecuteInstructionLineEvent("Start chrome", "browser 1"),
                               new ExecuteInstructionLineEvent("Set", "Opened browsers", "=browser count"),
                               new ExecuteInstructionLineEvent("Stop browser", "browser 1"),
                               new ExecuteInstructionLineEvent("Set", "Closed browsers", "=browser count"),
                               openedBrowsers, closedBrowsers));
        assertSuccess();
        assertEquals(1, openedBrowsers.getValue());
        assertEquals(0, closedBrowsers.getValue());
    }

    @Test(expected = BrowserNameConflictException.class)
    public void openMultipleBrowserAndRegisterUnderSameName_NameConflict() {
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "test.mazarineblue.libraries.web"),
                               new ExecuteInstructionLineEvent("Run setup checker", "never"),
                               new ExecuteInstructionLineEvent("Setup chrome"),
                               new ExecuteInstructionLineEvent("Set Headless", "true"),
                               new ExecuteInstructionLineEvent("Start chrome", "browser 1"),
                               new ExecuteInstructionLineEvent("Setup chrome"),
                               new ExecuteInstructionLineEvent("Set Headless", "true"),
                               new ExecuteInstructionLineEvent("Start chrome", "browser 1")));
        // The previous execute command was aborted due to the exception, but we still need to clean up.
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Stop browser", "browser 1")));
        assertFailure();
    }

    @Test(expected = BrowserInUseException.class)
    public void openMultipleBrowserAndCloseFocusedBrowser_NameConflict() {
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "test.mazarineblue.libraries.web"),
                               new ExecuteInstructionLineEvent("Run setup checker", "never"),
                               new ExecuteInstructionLineEvent("Setup chrome"),
                               new ExecuteInstructionLineEvent("Set Headless", "true"),
                               new ExecuteInstructionLineEvent("Start chrome", "browser 1"),
                               new ExecuteInstructionLineEvent("Setup chrome"),
                               new ExecuteInstructionLineEvent("Set Headless", "true"),
                               new ExecuteInstructionLineEvent("Start chrome", "browser 2"),
                               new ExecuteInstructionLineEvent("Stop browser", "browser 2")));
        // The previous execute command was aborted due to the exception, but we still need to clean up.
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Stop browser", "browser 1"),
                               new ExecuteInstructionLineEvent("Stop browser", "browser 2")));
        assertFailure();
    }

    @Test
    public void openMultipleBrowserAndClose_Succes() {
        GetVariableEvent openedBrowsers = new GetVariableEvent("Opened browsers");
        GetVariableEvent closedBrowsers = new GetVariableEvent("Closed browsers");
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "test.mazarineblue.libraries.web"),
                               new ExecuteInstructionLineEvent("Run setup checker", "never"),
                               new ExecuteInstructionLineEvent("Setup chrome"),
                               new ExecuteInstructionLineEvent("Set Headless", "true"),
                               new ExecuteInstructionLineEvent("Start chrome", "browser 1"),
                               new ExecuteInstructionLineEvent("Setup chrome"),
                               new ExecuteInstructionLineEvent("Set Headless", "true"),
                               new ExecuteInstructionLineEvent("Start chrome", "browser 2"),
                               new ExecuteInstructionLineEvent("Set", "Opened browsers", "=browser count"),
                               new ExecuteInstructionLineEvent("Stop browser", "browser 1"),
                               new ExecuteInstructionLineEvent("Stop browser", "browser 2"),
                               new ExecuteInstructionLineEvent("Set", "Closed browsers", "=browser count"),
                               openedBrowsers, closedBrowsers));
        assertSuccess();
        assertEquals(2, openedBrowsers.getValue());
        assertEquals(0, closedBrowsers.getValue());
    }
}
