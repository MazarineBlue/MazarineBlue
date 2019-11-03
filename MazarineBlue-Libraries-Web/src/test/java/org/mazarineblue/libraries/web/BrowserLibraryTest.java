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
import org.mazarineblue.libraries.web.events.FetchTabsEvent;
import static org.mazarineblue.libraries.web.tabs.Tab.INITIAL_TAB;
import org.mazarineblue.variablestore.events.GetVariableEvent;

public class BrowserLibraryTest
        extends AbstractExecutorTestHelper {

    @Test
    public void openAndCloseChromeBrowser_TabUnidentified() {
        test("chrome", null);
    }

    @Test
    public void openAndCloseChromeBrowser_TabA() {
        test("chrome", "tab A");
    }

    @Test
    public void openAndCloseFirefoxBrowser_TabUnidentified() {
        test("firefox", null);
    }

    @Test
    public void openAndCloseFirefoxBrowser_TabA() {
        test("firefox", "tab A");
    }

    @Test
    public void openAndCloseHtmlUnitBrowser_TabUnidentified() {
        test("htmlunit", null);
    }

    @Test
    public void openAndCloseHtmlUnitBrowser_TabA() {
        test("htmlunit", "tab A");
    }

    @Test
    public void openAndClosePhantomJSBrowser_TabUnidentified() {
        test("phantomjs", null);
    }

    @Test
    public void openAndClosePhantomJSBrowser_TabA() {
        test("phantomjs", "tab A");
    }

    private void test(String browser, String tab) {
        GetVariableEvent openedBrowsers = new GetVariableEvent("Opened browsers");
        GetVariableEvent closedBrowsers = new GetVariableEvent("Closed browsers");
        FetchTabsEvent tabs = new FetchTabsEvent();
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "test.mazarineblue.libraries.web"),
                               new ExecuteInstructionLineEvent("Run setup checker", "never"),
                               new ExecuteInstructionLineEvent("Start " + browser, "browser", tab),
                               new ExecuteInstructionLineEvent("Set", "Opened browsers", "=browser count"),
                               openedBrowsers, tabs,
                               new ExecuteInstructionLineEvent("Stop browser", "browser"),
                               new ExecuteInstructionLineEvent("Set", "Closed browsers", "=browser count"),
                               closedBrowsers));
        assertSuccess();
        assertEquals(1, openedBrowsers.getValue());
        assertEquals(0, closedBrowsers.getValue());
        assertEquals(1, tabs.list().size());
        assertEquals(tab != null ? tab : INITIAL_TAB, tabs.list().get(0).name());
    }
}
