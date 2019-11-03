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

import org.mazarineblue.eventnotifier.EventHandler;
import org.mazarineblue.keyworddriven.Keyword;
import org.mazarineblue.libraries.web.browsers.BrowserRegistry;
import org.mazarineblue.libraries.web.events.FetchTabsEvent;

public class TabLibrary
        extends AbstractBrowserLibrary {

    public TabLibrary(BrowserRegistry registry) {
        super(registry);
    }

    @EventHandler
    public void eventHandler(FetchTabsEvent e) {
        currentBrowser().eventHandler(e);
    }

    @Keyword("Tab count")
    public int tabCount() {
        return currentBrowser().tabCount();
    }

    @Keyword("New tab")
    public void newTab(String tabName) {
        currentBrowser().openInNewTab("", tabName);
    }

    @Keyword("Open in new tab")
    public void openInNewTab(String url, String tabName) {
        currentBrowser().openInNewTab(url, tabName);
    }

    @Keyword("Switch to tab")
    public void switchToTab(String tabName) {
        currentBrowser().switchToTab(tabName);
    }

    @Keyword("Close tab")
    public void closeTab(String tabName) {
        currentBrowser().closeTab(tabName);
    }
}
