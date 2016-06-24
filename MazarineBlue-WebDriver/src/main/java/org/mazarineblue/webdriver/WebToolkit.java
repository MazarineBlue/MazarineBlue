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
package org.mazarineblue.webdriver;

import java.io.IOException;
import org.mazarineblue.pictures.Picture;
import org.mazarineblue.webdriver.exceptions.ExecutionException;
import org.mazarineblue.webdriver.exceptions.PopupException;
import org.mazarineblue.webdriver.toolkit.InstructionFactory;
import org.openqa.selenium.Beta;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class WebToolkit
        implements AutoCloseable {

    private WebDriver driver;
    private InstructionFactory factory;

    public WebToolkit(WebDriver driver) {
        this.driver = driver;
        factory = new InstructionFactory(driver);
    }

    public WebDriver getWebDriver() {
        return driver;
    }

    @Override
    public void close() {
        if (driver == null)
            return;
        driver.close();
        driver = null;
        factory = null;
    }

    public TargetInstructions targetInstructions() {
        return factory.createTargetInstructions();
    }
    

    @SuppressWarnings("PublicInnerClass")
    public static interface TargetInstructions {

        public AlertInstructions switchToAlert();

        public void expectPopup(String popupName)
                throws PopupException;

        public void switchToPopup(String popupName)
                throws PopupException;

        public void switchToDefaultWindow();

        public void closePopup(String popupName)
                throws PopupException;

        public void closeAllPopups();

        public void switchToIFrame(WebKey key)
                throws ExecutionException;

        public void switchToDefaultFrame();
    }

    @SuppressWarnings("PublicInnerClass")
    public static interface AlertInstructions {

        public void accept();

        public void dismiss();

        public String getText();

        public void type(String text);

        @Beta
        public void basicAuthenticate(String username, String password);
    }

    public WindowInstructions windowInstructions() {
        return factory.createWindowInstructions();
    }

    @SuppressWarnings("PublicInnerClass")
    public static interface WindowInstructions {

        public void maximizeWindow();

        public void minimizeWindow();

        public void moveWindow(Point target);

        public void resizeWindow(Dimension target);

        @Deprecated
        public boolean validateWindowPosition(Point expected);

        @Deprecated
        public boolean validateWindowPosition(Point expected, Point epsilon);

        public Point getWindowPosition();

        @Deprecated
        public boolean validateWindowSize(Dimension expected);

        @Deprecated
        public boolean validateWindowSize(Dimension expected, Dimension epsilon);

        public Dimension getWindowSize();
    }

    public NavigationInstructions navigationInstructions() {
        return factory.createNavigationInstructions();
    }

    @SuppressWarnings("PublicInnerClass")
    public static interface NavigationInstructions {

        public void loadUrl(String url);

        public void pageBack();

        public void pageBack(long count);

        public void pageForward();

        public void pageForward(long count);

        public void pageRefresh();

        public String getCurrentUrl();
    }

    public PageInstructions pageInstructions() {
        return factory.createPageInstructions();
    }

    @SuppressWarnings("PublicInnerClass")
    public static interface PageInstructions {

        public String getPageTitle();
    }

    public ElementInstructions elementInstructions() {
        return factory.createElementInstructions();
    }

    @SuppressWarnings("PublicInnerClass")
    public static interface ElementInstructions {

        public void submit(WebKey key)
                throws ExecutionException;

        public int getElementCount(WebKey key)
                throws ExecutionException;

        public String getElementAttributeValue(WebKey key, String attribute)
                throws ExecutionException;

        public String getElementCssValue(WebKey key, String css)
                throws ExecutionException;

        public String getElementTageName(WebKey key)
                throws ExecutionException;

        public boolean isElementSelected(WebKey key)
                throws ExecutionException;

        public boolean isElementDisplayed(WebKey key)
                throws ExecutionException;

        public boolean isElementEnabled(WebKey key)
                throws ExecutionException;

        public String getElementText(WebKey key)
                throws ExecutionException;
    }

    public KeyboardInstructions keyboardInstructions() {
        return factory.createKeyboardInstructions();
    }

    @SuppressWarnings("PublicInnerClass")
    public static interface KeyboardInstructions {

        default void type(WebKey key, CharSequence input)
                throws ExecutionException {
            type(key, input, true);
        }

        public void type(WebKey key, CharSequence input, boolean clear)
                throws ExecutionException;

        public void pressKey(WebKey key, String... input)
                throws ExecutionException;
    }

    public MouseInstructions mouseInstructions() {
        return factory.createMouseInstructions();
    }

    @SuppressWarnings("PublicInnerClass")
    public static interface MouseInstructions {

        public void click(WebKey key)
                throws ExecutionException;

        public void clickAndHold(WebKey key)
                throws ExecutionException;

        public void contextClick(WebKey key)
                throws ExecutionException;

        public void dragAndDrop(WebKey from, WebKey to)
                throws ExecutionException;

        public void dragAndDropByOffset(WebKey from, Offset to)
                throws ExecutionException;

        public void doubleClick(WebKey key)
                throws ExecutionException;

        public void moveByOffset(Offset offset);

        public void moveToElement(WebKey key)
                throws ExecutionException;

        public void moveToElementByOffset(WebKey key, Offset offset)
                throws ExecutionException;

        public void releaseMouse(WebKey key)
                throws ExecutionException;
    }

    public ScreenshotInstructions screenshotInstructions() {
        return factory.createScreenshotInstructions();
    }

    @SuppressWarnings("PublicInnerClass")
    public static interface ScreenshotInstructions {

        public Picture takeScreenshot() throws IOException;
    }
}
