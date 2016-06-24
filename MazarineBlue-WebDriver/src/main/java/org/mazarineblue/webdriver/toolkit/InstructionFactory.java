/*
 * Copyright (c) 2012-2014 Alex de Kruijff
 * Copyright (c) 2014-2015 Specialisterren
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
import org.mazarineblue.webdriver.WebToolkit;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class InstructionFactory {

    private final WebDriver driver;
    private final WebElementFetcher fetcher;

    private WebToolkit.TargetInstructions targetInstructions;
    private WebToolkit.WindowInstructions windowInstructions;
    private WebToolkit.NavigationInstructions navigationInstructions;
    private WebToolkit.PageInstructions pageInstructions;
    private WebToolkit.ElementInstructions elementInstructions;
    private WebToolkit.KeyboardInstructions keyboardInstructions;
    private WebToolkit.MouseInstructions mouseInstructions;
    private WebToolkit.ScreenshotInstructions screenshotInstructions;
    private final Set<String> windowHandles;

    public InstructionFactory(WebDriver driver) {
        this.driver = driver;
        windowHandles = driver.getWindowHandles();
        WebElementFocuser focuser = new WebElementFocuser(driver);
        fetcher = new WebElementFetcher(driver) {

            @Override
            protected void action(WebElement e) {
                focuser.focus(e);
            }
        };
    }

    public WebToolkit.TargetInstructions createTargetInstructions() {
        if (targetInstructions == null)
            targetInstructions = new TargetInstructionsImpl(driver, fetcher,
                                                            windowHandles);
        return targetInstructions;
    }

    public WebToolkit.WindowInstructions createWindowInstructions() {
        if (windowInstructions == null)
            windowInstructions = new WindowInstructionImpl(driver);
        return windowInstructions;
    }

    public WebToolkit.NavigationInstructions createNavigationInstructions() {
        if (navigationInstructions == null)
            navigationInstructions = new NavigationInstructionsImpl(driver);
        return navigationInstructions;
    }

    public WebToolkit.PageInstructions createPageInstructions() {
        if (pageInstructions == null)
            pageInstructions = new PageInstructionsImpl(driver);
        return pageInstructions;
    }

    public WebToolkit.ElementInstructions createElementInstructions() {
        if (elementInstructions == null)
            elementInstructions = new ElementInstructionImpl(fetcher);
        return elementInstructions;
    }

    public WebToolkit.KeyboardInstructions createKeyboardInstructions() {
        if (keyboardInstructions == null)
            keyboardInstructions = new KeyboardInstructionsImpl(fetcher);
        return keyboardInstructions;
    }

    public WebToolkit.MouseInstructions createMouseInstructions() {
        if (mouseInstructions == null)
            mouseInstructions = new MouseInstructionImpl(driver, fetcher);
        return mouseInstructions;
    }

    public WebToolkit.ScreenshotInstructions createScreenshotInstructions() {
        if (screenshotInstructions == null) {
            TakesScreenshot takeScreenshot = driver instanceof TakesScreenshot
                    ? (TakesScreenshot) driver : null;
            screenshotInstructions = new ScreenshotInstructionsImpl(
                    takeScreenshot);
        }
        return screenshotInstructions;
    }
}
