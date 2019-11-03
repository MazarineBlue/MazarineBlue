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
import org.mazarineblue.eventdriven.feeds.MemoryFeed;
import org.mazarineblue.executors.util.AbstractExecutorTestHelper;
import org.mazarineblue.keyworddriven.events.ExecuteInstructionLineEvent;
import org.mazarineblue.libraries.web.events.FetchTabsEvent;
import static org.mazarineblue.libraries.web.tabs.Tab.INITIAL_TAB;
import org.mazarineblue.variablestore.events.GetVariableEvent;

abstract class AbstractSetupLibraryTestHelper
        extends AbstractExecutorTestHelper {

    protected void openAndClose(String browser, ExecuteInstructionLineEvent event, String tab) {
        GetVariableEvent openedBrowsers = new GetVariableEvent("Opened browsers");
        GetVariableEvent closedBrowsers = new GetVariableEvent("Closed browsers");
        FetchTabsEvent tabs = new FetchTabsEvent();
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "test.mazarineblue.libraries.web"),
                               new ExecuteInstructionLineEvent("Run setup checker", "never"),
                               new ExecuteInstructionLineEvent("Setup " + browser),
                               event,
                               new ExecuteInstructionLineEvent("Start " + browser, "browser A", tab),
                               new ExecuteInstructionLineEvent("Set", "Opened browsers", "=browser count"),
                               openedBrowsers, tabs,
                               new ExecuteInstructionLineEvent("Stop browser", "browser A"),
                               new ExecuteInstructionLineEvent("Set", "Closed browsers", "=browser count"),
                               closedBrowsers));
        assertSuccess();
        assertEquals(1, openedBrowsers.getValue());
        assertEquals(0, closedBrowsers.getValue());
        assertEquals(1, tabs.list().size());
        assertEquals(tab != null ? tab : INITIAL_TAB, tabs.list().get(0).name());
    }
}
