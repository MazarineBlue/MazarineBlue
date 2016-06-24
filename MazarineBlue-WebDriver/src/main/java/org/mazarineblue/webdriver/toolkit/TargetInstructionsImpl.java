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

import java.util.Set;
import org.mazarineblue.webdriver.WebKey;
import org.mazarineblue.webdriver.WebToolkit;
import org.mazarineblue.webdriver.exceptions.ExecutionException;
import org.mazarineblue.webdriver.exceptions.PopupException;
import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
class TargetInstructionsImpl
        implements WebToolkit.TargetInstructions {

    private final WebDriver driver;
    private final WebElementFetcher fetcher;
    private final WindowsHandleRegistry windowHandleRegistry;

    TargetInstructionsImpl(WebDriver driver, WebElementFetcher fetcher,
                           Set<String> windowHandles) {
        this.driver = driver;
        this.fetcher = fetcher;
        this.windowHandleRegistry = new WindowsHandleRegistry(driver,
                                                              windowHandles);
    }

    @Override
    public AlertInstructionsImpl switchToAlert() {
        Alert alert = driver.switchTo().alert();
        return new AlertInstructionsImpl(alert);
    }

    @Override
    public void expectPopup(String popupName)
            throws PopupException {
        WindowHandle handle = windowHandleRegistry.fetchExpectedWindowHandle();
        windowHandleRegistry.storeNewWindowHandle(popupName, handle);
    }

    @Override
    public void switchToPopup(String popupName)
            throws PopupException {
        WindowHandle handle = windowHandleRegistry.getWindowHandle(popupName);
        driver.switchTo().window(handle.getWindowHandle());
    }

    @Override
    public void switchToDefaultWindow() {
        WindowHandle handle = windowHandleRegistry.getDefaultHandle();
        driver.switchTo().window(handle.getWindowHandle());
    }

    @Override
    public void closePopup(String popupName)
            throws PopupException {
        WindowHandle handle = windowHandleRegistry.getWindowHandle(popupName);
        if (handle.isActiveHandle(driver))
            throw new PopupException("Can not close active popup");
        windowHandleRegistry.closeHandle(popupName);
    }

    @Override
    public void closeAllPopups() {
        windowHandleRegistry.closeAllHandles();
    }

    @Override
    public void switchToIFrame(WebKey key)
            throws ExecutionException {
        selectFrame.execute(key);
    }

    private final RecoverFromStaleElementAction<Void, Object> selectFrame = new RecoverFromStaleElementAction<Void, Object>() {

        @Override
        protected Void action(WebKey key, Object... param)
                throws ExecutionException {
            WebElement e = fetcher.fetchElement(key);
            driver.switchTo().frame(e);
            return null;
        }
    };

    @Override
    public void switchToDefaultFrame() {
        driver.switchTo().defaultContent();
    }
}
