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

import java.util.HashMap;
import java.util.Map;
import org.mazarineblue.libraries.web.exceptions.BrowserInUseException;
import org.mazarineblue.libraries.web.exceptions.BrowserNameConflictException;
import static org.mazarineblue.libraries.web.tabs.Tab.INITIAL_TAB;
import org.mazarineblue.libraries.web.util.WebDriverSetupChecker;
import org.openqa.selenium.WebDriver;

/**
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class BrowserRegistry {

    public static Browser createBrowser(WebDriver driver, String name, String initialTabName) {
        return new BrowserImpl(driver, name, initialTabName != null ? initialTabName : INITIAL_TAB);
    }

    private final WebDriverSetupChecker webDriverSetupChecker = new WebDriverSetupChecker();
    private final Map<String, Browser> map = new HashMap<>();
    private Browser browser;

    public void checkSetup(WebDriverSetupChecker.SetupCheckOption option) {
        webDriverSetupChecker.setRun(option);
    }

    public WebDriverSetupChecker setupChecker() {
        return webDriverSetupChecker;
    }

    /**
     * Registers the browser under the specified name.
     *
     * @param name    the name to register the browser under
     * @param browser the browser to register
     */
    public void register(Browser browser) {
        if (map.containsKey(browser.name()))
            throw new BrowserNameConflictException(browser.name());
        this.browser = browser;
        map.put(browser.name(), browser);
    }

    /**
     * Unregisters the browser with the specified name.
     *
     * @param name the specified name
     */
    public void unregiser(String name) {
        if (this.browser.name().equals(name) && map.size() != 1)
            throw new BrowserInUseException();
        map.remove(name);
    }

    /**
     * Switches the context to the specified browser.
     *
     * @param name the name of the browser to switch to
     */
    public void switchTo(String name) {
        this.browser = map.get(name);
    }

    /**
     * Returns the current browser.
     *
     * @return the current browser.
     */
    public Browser currentBrowser() {
        return browser;
    }
    
    /**
     * Returns the amount of browser instances that are opened.
     *
     * @return the amount of browser instances that are opened.
     */
    public int browserCount() {
        return map.size();
    }
}
