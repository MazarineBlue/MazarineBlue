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

import java.io.IOException;
import static org.awaitility.Awaitility.await;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.mazarineblue.eventdriven.feeds.MemoryFeed;
import org.mazarineblue.executors.util.AbstractExecutorTestHelper;
import org.mazarineblue.keyworddriven.events.ExecuteInstructionLineEvent;
import org.mazarineblue.libraries.web.events.FetchTabsEvent;
import org.mazarineblue.libraries.web.exceptions.TabNameTakenException;
import org.mazarineblue.libraries.web.exceptions.TabNotRegisteredForBrowserException;
import static org.mazarineblue.libraries.web.tabs.Tab.INITIAL_TAB;
import org.mazarineblue.libraries.web.util.HttpServer;
import org.mazarineblue.libraries.web.util.containers.IsCalledListener;
import org.mazarineblue.libraries.web.util.containers.TargetEchoWebpageContainer;
import org.mazarineblue.variablestore.events.GetVariableEvent;

public class TabLibraryTest
        extends AbstractExecutorTestHelper {

    @Test
    public void initial_TabUnidentifier() {
        GetVariableEvent title = new GetVariableEvent("title");
        GetVariableEvent count = new GetVariableEvent("tabs count");
        FetchTabsEvent tabs = new FetchTabsEvent();
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "test.mazarineblue.libraries.web"),
                               new ExecuteInstructionLineEvent("Run setup checker", "never"),
                               new ExecuteInstructionLineEvent("Setup chrome"),
                               new ExecuteInstructionLineEvent("Set headless", true),
                               new ExecuteInstructionLineEvent("Start chrome", "browser", null),
                               new ExecuteInstructionLineEvent("Set", "title", "=Webpage title"),
                               new ExecuteInstructionLineEvent("Set", "tabs count", "=tab count"),
                               title, count, tabs,
                               new ExecuteInstructionLineEvent("Stop browser", "browser"),
                               new ExecuteInstructionLineEvent("Set", "closed tabs", "=browser count")));
        assertSuccess();
        assertEquals("", title.getValue());
        assertEquals(1, count.getValue());
        assertEquals(1, tabs.list().size());
        assertEquals(INITIAL_TAB, tabs.list().get(0).name());
    }

    @Test
    public void initial_TabA() {
        GetVariableEvent title = new GetVariableEvent("title");
        GetVariableEvent count = new GetVariableEvent("tabs count");
        FetchTabsEvent tabs = new FetchTabsEvent();
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "test.mazarineblue.libraries.web"),
                               new ExecuteInstructionLineEvent("Run setup checker", "never"),
                               new ExecuteInstructionLineEvent("Setup chrome"),
                               new ExecuteInstructionLineEvent("Set headless", true),
                               new ExecuteInstructionLineEvent("Start chrome", "browser", "tab A"),
                               new ExecuteInstructionLineEvent("Set", "title", "=Webpage title"),
                               new ExecuteInstructionLineEvent("Set", "tabs count", "=tab count"),
                               title, count, tabs,
                               new ExecuteInstructionLineEvent("Stop browser", "browser"),
                               new ExecuteInstructionLineEvent("Set", "closed tabs", "=browser count")));
        assertSuccess();
        assertEquals("", title.getValue());
        assertEquals(1, count.getValue());
        assertEquals(1, tabs.list().size());
        assertEquals("tab A", tabs.list().get(0).name());
    }

    @Test
    public void newTab_UniqueTabName() {
        GetVariableEvent title = new GetVariableEvent("title");
        GetVariableEvent count = new GetVariableEvent("tabs count");
        FetchTabsEvent tabs = new FetchTabsEvent();
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "test.mazarineblue.libraries.web"),
                               new ExecuteInstructionLineEvent("Run setup checker", "never"),
                               new ExecuteInstructionLineEvent("Setup chrome"),
                               new ExecuteInstructionLineEvent("Set headless", true),
                               new ExecuteInstructionLineEvent("Start chrome", "browser", "tab A"),
                               new ExecuteInstructionLineEvent("New tab", "tab B"),
                               new ExecuteInstructionLineEvent("Set", "title", "=Webpage title"),
                               new ExecuteInstructionLineEvent("Set", "tabs count", "=tab count"),
                               title, tabs, count,
                               new ExecuteInstructionLineEvent("Stop browser", "browser"),
                               new ExecuteInstructionLineEvent("Set", "closed tabs", "=browser count")));
        assertSuccess();
        assertEquals("", title.getValue());
        assertEquals(2, count.getValue());
        assertEquals(2, tabs.list().size());
        assertEquals("tab A", tabs.list().get(0).name());
        assertEquals("tab B", tabs.list().get(1).name());
    }

    @Test(expected = TabNameTakenException.class)
    public void newTab_SameTabName() {
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "test.mazarineblue.libraries.web"),
                               new ExecuteInstructionLineEvent("Run setup checker", "never"),
                               new ExecuteInstructionLineEvent("Setup chrome"),
                               new ExecuteInstructionLineEvent("Set headless", true),
                               new ExecuteInstructionLineEvent("Start chrome", "browser", "tab A"),
                               new ExecuteInstructionLineEvent("New tab", "tab A")));
        // The previous execute command was aborted due to the exception, but we still need to clean up.
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Stop browser", "browser")));
        assertFailure();
    }

    @Test
    public void openInNewTab_UniqueTabName()
            throws IOException {
        IsCalledListener listener = new IsCalledListener();
        try (HttpServer server = new HttpServer().setContainer(listener).start()) {
            GetVariableEvent title = new GetVariableEvent("title");
            GetVariableEvent count = new GetVariableEvent("tabs count");
            FetchTabsEvent tabs = new FetchTabsEvent();
            execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "test.mazarineblue.libraries.web"),
                                   new ExecuteInstructionLineEvent("Run setup checker", "never"),
                                   new ExecuteInstructionLineEvent("Setup chrome"),
                                   new ExecuteInstructionLineEvent("Set headless", true),
                                   new ExecuteInstructionLineEvent("Start chrome", "browser", "tab A"),
                                   new ExecuteInstructionLineEvent("Open in new tab", "http://" + server.getBase(), "tab B"),
                                   new ExecuteInstructionLineEvent("Set", "title", "=Webpage title"),
                                   new ExecuteInstructionLineEvent("Set", "tabs count", "=tab count"),
                                   title, tabs, count,
                                   new ExecuteInstructionLineEvent("Stop browser", "browser")));
            await().until(listener::isCalled);
            assertEquals("", title.getValue());
            assertSuccess();
            assertEquals(2, count.getValue());
            assertEquals(2, tabs.list().size());
            assertEquals("tab A", tabs.list().get(0).name());
            assertEquals("tab B", tabs.list().get(1).name());
        }
    }

    @Test(expected = TabNameTakenException.class)
    public void openInNewTab_SameTabName()
            throws IOException {
        IsCalledListener listener = new IsCalledListener();
        try (HttpServer server = new HttpServer().setContainer(listener).start()) {
            execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "test.mazarineblue.libraries.web"),
                                   new ExecuteInstructionLineEvent("Run setup checker", "never"),
                                   new ExecuteInstructionLineEvent("Setup chrome"),
                                   new ExecuteInstructionLineEvent("Set headless", true),
                                   new ExecuteInstructionLineEvent("Start chrome", "browser", "tab A"),
                                   new ExecuteInstructionLineEvent("Open in new tab", "http://" + server.getBase(), "tab A")));
            await().until(listener::isCalled);
            // The previous execute command was aborted due to the exception, but we still need to clean up.
            execute(new MemoryFeed(new ExecuteInstructionLineEvent("Stop browser", "browser")));
            assertFailure();
        }
    }

    @Test
    public void switchToTab_ExistingTab()
            throws IOException {
        try (HttpServer server = new HttpServer().setContainer(new TargetEchoWebpageContainer()).start()) {
            GetVariableEvent title = new GetVariableEvent("title");
            execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "test.mazarineblue.libraries.web"),
                                   new ExecuteInstructionLineEvent("Run setup checker", "never"),
                                   new ExecuteInstructionLineEvent("Setup chrome"),
                                   new ExecuteInstructionLineEvent("Set headless", true),
                                   new ExecuteInstructionLineEvent("Start chrome", "browser", "tab A"),
                                   new ExecuteInstructionLineEvent("Open in new tab", "http://" + server.getBase() + "/tab B", "tab B"),
                                   new ExecuteInstructionLineEvent("Switch to tab", "tab B"),
                                   new ExecuteInstructionLineEvent("Set", "title", "=Webpage title"),
                                   title,
                                   new ExecuteInstructionLineEvent("Stop browser", "browser")));
            assertSuccess();
            assertEquals("/tab B", title.getValue());
        }
    }

    @Test(expected = TabNotRegisteredForBrowserException.class)
    public void switchToTab_NonExistingTab()
            throws IOException {
        try (HttpServer server = new HttpServer().setContainer(new TargetEchoWebpageContainer()).start()) {
            GetVariableEvent title = new GetVariableEvent("title");
            execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "test.mazarineblue.libraries.web"),
                                   new ExecuteInstructionLineEvent("Run setup checker", "never"),
                                   new ExecuteInstructionLineEvent("Setup chrome"),
                                   new ExecuteInstructionLineEvent("Set headless", true),
                                   new ExecuteInstructionLineEvent("Start chrome", "browser", "tab A"),
                                   new ExecuteInstructionLineEvent("Switch to tab", "third tab")));
            // The previous execute command was aborted due to the exception, but we still need to clean up.
            execute(new MemoryFeed(new ExecuteInstructionLineEvent("Stop browser", "browser")));
            assertSuccess();
            assertEquals("/tab B", title.getValue());
        }
    }

    @Test
    public void closeTab_TabAB_FocusTabA_CloseTabB_ResultFocustOnTabA()
            throws IOException {
        try (HttpServer server = new HttpServer().setContainer(new TargetEchoWebpageContainer()).start()) {
            GetVariableEvent title = new GetVariableEvent("title");
            execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "test.mazarineblue.libraries.web"),
                                   new ExecuteInstructionLineEvent("Run setup checker", "never"),
                                   new ExecuteInstructionLineEvent("Setup chrome"),
                                   new ExecuteInstructionLineEvent("Set headless", true),
                                   new ExecuteInstructionLineEvent("Start chrome", "browser", "tab A"),
                                   new ExecuteInstructionLineEvent("Open in new tab", "http://" + server.getBase() + "/tab B", "tab B"),
                                   new ExecuteInstructionLineEvent("Switch to tab", "tab A"),
                                   new ExecuteInstructionLineEvent("Close tab", "tab B"),
                                   new ExecuteInstructionLineEvent("Set", "title", "=Webpage title"),
                                   title,
                                   new ExecuteInstructionLineEvent("Stop browser", "browser")));
            assertSuccess();
            assertEquals("", title.getValue());
        }
    }

    @Test
    public void closeTab_TabAB_FocusTabB_CloseTabNull_ResultFocustOnTabA()
            throws IOException {
        try (HttpServer server = new HttpServer().setContainer(new TargetEchoWebpageContainer()).start()) {
            GetVariableEvent title = new GetVariableEvent("title");
            execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "test.mazarineblue.libraries.web"),
                                   new ExecuteInstructionLineEvent("Run setup checker", "never"),
                                   new ExecuteInstructionLineEvent("Setup chrome"),
                                   new ExecuteInstructionLineEvent("Set headless", true),
                                   new ExecuteInstructionLineEvent("Start chrome", "browser", "tab A"),
                                   new ExecuteInstructionLineEvent("Open in new tab", "http://" + server.getBase() + "/tab B", "tab B"),
                                   new ExecuteInstructionLineEvent("Switch to tab", "tab B"),
                                   new ExecuteInstructionLineEvent("Close tab"),
                                   new ExecuteInstructionLineEvent("Set", "title", "=Webpage title"),
                                   title,
                                   new ExecuteInstructionLineEvent("Stop browser", "browser")));
            assertSuccess();
            assertEquals("", title.getValue());
        }
    }

    @Test
    public void closeTab_TabABC_FocusTabB_CloseTabNull_ResultFocustOnTabC()
            throws IOException {
        try (HttpServer server = new HttpServer().setContainer(new TargetEchoWebpageContainer()).start()) {
            GetVariableEvent title = new GetVariableEvent("title");
            execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "test.mazarineblue.libraries.web"),
                                   new ExecuteInstructionLineEvent("Run setup checker", "never"),
                                   new ExecuteInstructionLineEvent("Setup chrome"),
                                   new ExecuteInstructionLineEvent("Set headless", true),
                                   new ExecuteInstructionLineEvent("Start chrome", "browser", "tab A"),
                                   new ExecuteInstructionLineEvent("Open in new tab", "http://" + server.getBase() + "/tab B", "tab B"),
                                   new ExecuteInstructionLineEvent("Open in new tab", "http://" + server.getBase() + "/tab C", "tab C"),
                                   new ExecuteInstructionLineEvent("Switch to tab", "tab B"),
                                   new ExecuteInstructionLineEvent("Close tab"),
                                   new ExecuteInstructionLineEvent("Set", "title", "=Webpage title"),
                                   title,
                                   new ExecuteInstructionLineEvent("Stop browser", "browser")));
            assertSuccess();
            assertEquals("/tab C", title.getValue());
        }
    }
}
