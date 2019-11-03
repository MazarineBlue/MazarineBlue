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

import java.util.Set;
import static org.awaitility.Awaitility.await;
import org.awaitility.Duration;
import org.mazarineblue.libraries.web.events.FetchTabsEvent;
import org.mazarineblue.libraries.web.exceptions.TabNotRegisteredForBrowserException;
import org.mazarineblue.libraries.web.exceptions.WindowHandleNotFoundException;
import org.mazarineblue.libraries.web.tabs.Tab;
import org.mazarineblue.libraries.web.tabs.TabRegistry;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

class BrowserImpl
        implements Browser {

    private static final CharSequence PAGE_RELOADED = "Detected a page unload event; script execution does not work across page loads.";
    private static final Duration NEW_TAB_TIMEOUT = Duration.FIVE_MINUTES;

    private final String name;
    private final WebDriver driver;
    private final TabRegistry tabs;

    BrowserImpl(WebDriver driver, String name, String initialTabName) {
        this.driver = driver;
        this.name = name;
        tabs = new TabRegistry(driver, initialTabName);
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public WebDriver driver() {
        return driver;
    }

    @Override
    public void eventHandler(FetchTabsEvent e) {
        e.add(tabs.getAllTabs());
        e.setConsumed(true);
    }

    @Override
    public int tabCount() {
        return tabs.size();
    }

    @Override
    public void openInNewTab(String url, String tabName) {
        Set<String> handles = driver.getWindowHandles();
        executeJavascript("window.open('" + url + "', '_blank')");
        await().atMost(NEW_TAB_TIMEOUT).until(() -> driver.getWindowHandles().size() > handles.size()); // @TODO write a test for the timeout
        tabs.insertTab(tabName, newWindowHandle(handles));
    }

    //<editor-fold defaultstate="collapsed" desc="Helper methods for openInNewTab(url, tabName)">
    private Object executeJavascript(String script) {
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        try {
            return executor.executeScript(script);
        } catch (WebDriverException ex) {
            String msg = ex.getMessage();
            if (msg != null && msg.contains(PAGE_RELOADED))
                return executor.executeScript(script);
            else
                throw ex;
        }
    }

    private String newWindowHandle(Set<String> handles) {
        for (String handle : driver.getWindowHandles())
            if (!handles.contains(handle))
                return handle;
        throw new WindowHandleNotFoundException();
    }
    //</editor-fold>

    @Override
    public void switchToTab(String tabName) {
        try {
            Tab tab = tabs.getTab(tabName);
            driver.switchTo().window(tab.handle());
            tabs.setCurrentTab(tab);
        } catch (TabNotRegisteredForBrowserException ex) {
            throw new TabNotRegisteredForBrowserException(name, tabName, ex);
        }
    }

    @Override
    public void closeTab(String tabName) {
        tabs.removeTab(tabName != null ? tabs.getTab(tabName) : tabs.getCurrentTab());
        if (tabName != null)
            driver.switchTo().window(tabs.getTab(tabName).handle());
        driver.close();
        driver.switchTo().window(tabs.getCurrentTab().handle());
    }
}
