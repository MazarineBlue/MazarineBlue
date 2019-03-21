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
package org.mazarineblue.mazarineblue.libraries.web;

import java.util.Set;
import static org.awaitility.Awaitility.await;
import org.awaitility.Duration;
import org.mazarineblue.keyworddriven.Keyword;
import org.mazarineblue.keyworddriven.Library;
import static org.mazarineblue.mazarineblue.libraries.web.WebDriverLibraryPlugin.NAMESPACE;
import org.mazarineblue.mazarineblue.libraries.web.exceptions.WindowHandleNotFoundException;
import org.mazarineblue.mazarineblue.libraries.web.tabs.Tab;
import org.mazarineblue.mazarineblue.libraries.web.tabs.TabRegistry;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

public class TabLibrary
        extends Library {

    private static final Duration NEW_TAB_TIMEOUT = Duration.FIVE_MINUTES;
    private static final CharSequence PAGE_RELOADED = "Detected a page unload event; script execution does not work across page loads.";

    private final WebDriver driver;
    private final TabRegistry registry;

    public TabLibrary(WebDriver driver) {
        super(NAMESPACE);
        this.driver = driver;
        registry = new TabRegistry(driver);
    }

    @Keyword("Tab count")
    public int tabCount() {
        return registry.size();
    }

    @Keyword("Open in new tab")
    public void openInNewTab(String url, String tabName) {
        Set<String> handles = driver.getWindowHandles();
        executeJavascript("window.open('" + url + "', '_blank');");
        await().atMost(NEW_TAB_TIMEOUT).until(() -> driver.getWindowHandles().size() > handles.size());
        registry.insertTab(tabName, newWindowHandle(handles));
    }

    //<editor-fold defaultstate="collapsed" desc="Helper methods for openInNewTab(url, tabName)">
    private Object executeJavascript(String script, Object... arguments) {
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        try {
            return executor.executeScript(script, arguments);
        } catch (WebDriverException ex) {
            String msg = ex.getMessage();
            if (msg != null && msg.contains(PAGE_RELOADED))
                return executor.executeScript(script, arguments);
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

    @Keyword("Switch to tab")
    public void switchToTab(String tabName) {
        Tab tab = registry.getTab(tabName);
        driver.switchTo().window(tab.getHandle());
        registry.setCurrentTab(tab);
    }

    @Keyword("Close tab")
    public void closeTab() {
        driver.close();
        registry.removeCurrentTab();
    }

    @Keyword("Close tab to the right")
    public void closeTabsToTheRight() {
        boolean closeTab = false;
        for (Tab tab : registry.getAllTabs())
            if (registry.isCurrentTab(tab))
                closeTab = true;
            else if (closeTab)
                closeTab(tab);
    }

    @Keyword("Close tab other right")
    public void closeOtherTabs() {
        for (Tab tab : registry.getAllTabs())
            if (!registry.isCurrentTab(tab))
                closeTab(tab);
    }

    public void closeTab(String tabName) {
        closeTab(registry.getTab(tabName));
        registry.removeTab(tabName);
    }

    //<editor-fold defaultstate="collapsed" desc="Helper methods for closeTab()">
    private void closeTab(Tab tab) {
        String previousHandle = driver.getWindowHandle();
        driver.switchTo().window(tab.getHandle());
        driver.close();
        driver.switchTo().window(previousHandle);
    }
    //</editor-fold>
}
