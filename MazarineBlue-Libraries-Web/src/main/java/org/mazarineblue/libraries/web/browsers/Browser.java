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
package org.mazarineblue.libraries.web.browsers;

import org.mazarineblue.libraries.web.events.FetchTabsEvent;
import org.mazarineblue.libraries.web.tabs.TabRegistry;
import org.openqa.selenium.WebDriver;

/**
 * Represents a browser.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public interface Browser {

    /**
     * Gets the name of the browser.
     *
     * @return the name of the browser.
     */
    public String name();

    /**
     * Gets the {@link WebDriver} instance associated with this browser.
     *
     * @return the {@link WebDriver} instance associated with this browser.
     */
    public WebDriver driver();

    /**
     * Gets the {@link TabRegistry} instance associated with this browser.
     *
     * @return the {@link TabRegistry} instance associated with this browser.
     */
    //public TabRegistry tabs();

    public void eventHandler(FetchTabsEvent e);

    /**
     * Gets the count of all tabs opened for this browser.
     *
     * @return the count of all tabs opened for this browser.
     */
    public int tabCount();

    /**
     * Opens the specified URL in the specified tab.
     *
     * @param url     The URL to load in the new tab.
     * @param tabName The name of the new tab.
     */
    public void openInNewTab(String url, String tabName);

    /**
     * Switches to the specified tab
     *
     * @param tab the tab to switch to.
     */
    public void switchToTab(String tab);

    /**
     * Close the specified tab.
     *
     * @param tabName if null close the current tab.
     */
    public void closeTab(String tabName);
}
