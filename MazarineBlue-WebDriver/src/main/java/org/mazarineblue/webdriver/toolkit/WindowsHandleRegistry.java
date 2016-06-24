/*
 * Copyright (c) 2015 Specialisterren
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
package org.mazarineblue.webdriver.toolkit;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.mazarineblue.webdriver.exceptions.PopupException;
import org.openqa.selenium.WebDriver;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
class WindowsHandleRegistry {

    private final WebDriver driver;
    private final Map<String, WindowHandle> popups = new HashMap();
    private Set<String> windowHandles;

    WindowsHandleRegistry(WebDriver driver, Set<String> windowHandles) {
        this.driver = driver;
        this.windowHandles = new TreeSet(windowHandles);
        popups.put(null, getDefaultHandle(windowHandles));
    }

    private WindowHandle getDefaultHandle(Set<String> handles) {
        String handle = handles.iterator().next();
        return new WindowHandle(handle);
    }

    WindowHandle getDefaultHandle() {
        return popups.get(null);
    }

    public void storeNewWindowHandle(String popupName, WindowHandle handle)
            throws PopupException {
        popups.put(popupName, handle);
        windowHandles = driver.getWindowHandles();
    }

    public WindowHandle fetchExpectedWindowHandle()
            throws PopupException {
        throwErrorOnMultipleNewHandles();

        for (String windowHandle : driver.getWindowHandles())
            if (this.windowHandles.contains(windowHandle) == false)
                return new WindowHandle(windowHandle);
        throw new PopupException("One popup required: No popups found");
    }

    private void throwErrorOnMultipleNewHandles()
            throws PopupException {
        Set<String> windowHandles = driver.getWindowHandles();
        int expected = this.windowHandles.size() + 1;
        int actual = windowHandles.size();
        if (expected < actual)
            throw new PopupException("One popup required: multiple popups found");
    }

    WindowHandle getWindowHandle(String popupName)
            throws PopupException {
        if (popups.containsKey(popupName))
            return popups.get(popupName);
        throw new PopupException("No popup registered under " + popupName);
    }

    void closeHandle(String popupName) {
        WindowHandle handle = popups.get(popupName);
        closeHandle(handle);
        popups.remove(popupName);
        windowHandles = driver.getWindowHandles();
    }

    private void closeHandle(WindowHandle handle) {
        String currentHandle = driver.getWindowHandle();
        driver.switchTo().window(handle.getWindowHandle());
        driver.close();
        driver.switchTo().window(currentHandle);
    }

    void closeAllHandles() {
        for (WindowHandle handle : popups.values())
            closeHandle(handle);
        popups.clear();
        windowHandles = driver.getWindowHandles();
    }

    void refresh() {
        windowHandles = driver.getWindowHandles();
    }
}
