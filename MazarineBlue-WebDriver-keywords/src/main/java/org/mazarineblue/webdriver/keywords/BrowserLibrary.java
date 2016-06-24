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
package org.mazarineblue.webdriver.keywords;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.mazarineblue.keyworddriven.Keyword;
import org.mazarineblue.keyworddriven.Parameters;
import org.mazarineblue.keyworddriven.librarymanager.Library;
import org.mazarineblue.webdriver.WebKey;
import org.mazarineblue.webdriver.WebToolkit;
import org.mazarineblue.webdriver.exceptions.ExecutionException;
import org.mazarineblue.webdriver.exceptions.PopupClosePreviousFirstException;
import org.mazarineblue.webdriver.exceptions.PopupException;
import org.mazarineblue.webdriver.exceptions.PopupNotFoundException;
import org.mazarineblue.webdriver.exceptions.UnableToSwitchToFrameException;
import org.mazarineblue.webdriver.exceptions.WebKeyTypeUnsupportedException;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class BrowserLibrary
        extends AbstractWebToolkitLibrary
        implements AutoCloseable {

    private static interface Mode
            extends AutoCloseable {
    }
    private static final String switchError = "Unexcepted exception thrown";

    private Mode mode;
    private final DefaultMode defaultMode;
    private final WindowLibrary windowLibrary;
    private final NavigationLibrary navigationLibrary;

    BrowserLibrary(Library library, WebToolkit toolkit) {
        super(library, toolkit, new TestUtility(library));

        windowLibrary = new WindowLibrary(library, toolkit, testUtility);
        navigationLibrary = new NavigationLibrary(library, toolkit, testUtility);

        executor().libraries().register(windowLibrary);
        executor().libraries().register(navigationLibrary);

        mode = defaultMode = new DefaultMode(library, toolkit, testUtility);
    }

    private class DefaultMode
            implements Mode {

        private final PageLibrary pageLibrary;
        private final ElementLibrary elementLibrary;
        private final KeyboardLibrary keyboardLibrary;
        private final MouseLibrary mouseLibrary;
        private final ScreenshotLibrary screenshotLibrary;

        private DefaultMode(Library library, WebToolkit toolkit,
                            TestUtility testUtility) {
            pageLibrary = new PageLibrary(library, toolkit, testUtility);
            elementLibrary = new ElementLibrary(library, toolkit, testUtility);
            keyboardLibrary = new KeyboardLibrary(library, toolkit, testUtility);
            mouseLibrary = new MouseLibrary(library, toolkit, testUtility);
            screenshotLibrary = new ScreenshotLibrary(library, toolkit,
                                                      testUtility);
            open();
        }

        public void open() {
            executor().libraries().register(pageLibrary);
            executor().libraries().register(elementLibrary);
            executor().libraries().register(keyboardLibrary);
            executor().libraries().register(mouseLibrary);
            executor().libraries().register(screenshotLibrary);
        }

        @Override
        public void close() {
            executor().libraries().unregister(pageLibrary);
            executor().libraries().unregister(elementLibrary);
            executor().libraries().unregister(keyboardLibrary);
            executor().libraries().unregister(mouseLibrary);
            executor().libraries().unregister(screenshotLibrary);
        }
    }

    private void switchToDefaultMode() {
        if (mode == defaultMode)
            return;
        closeMode();
        mode = defaultMode;
        defaultMode.open();
    }

    private void closeMode() {
        try {
            mode.close();
        } catch (Exception ex) {
            throw new AssertionError(switchError, ex);
        }
    }

    @Override
    public void close()
            throws Exception {
        executor().libraries().unregister(windowLibrary);
        executor().libraries().unregister(navigationLibrary);
        mode.close();
        toolkit.close();
    }

    @Override
    void switchTo(WebToolkit toolkit) {
        super.switchTo(toolkit);
    }

    static boolean check(String input, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        boolean flag = matcher == null ? false : matcher.find();
        return flag;
    }

    @Keyword("Switch to alert")
    public void switchToAlert() {
        closeMode();
        mode = new AlertMode(this);
    }

    private class AlertMode
            implements Mode {

        private final AlertLibrary alertLibrary;

        public AlertMode(Library library) {
            WebToolkit.AlertInstructions instructions = toolkit.targetInstructions().switchToAlert();
            alertLibrary = new AlertLibrary(instructions, library);
            executor().libraries().register(alertLibrary);
        }

        @Override
        public void close() {
            executor().libraries().unregister(alertLibrary);
        }
    }

    @Keyword("Expect popup")
    @Parameters(min = 1)
    public void expectPopup(String popupName) {
        try {
            toolkit.targetInstructions().expectPopup(popupName);
        } catch (PopupException ex) {
            throw new PopupClosePreviousFirstException(popupName);
        }
    }

    @Keyword("Switch to popup")
    @Parameters(min = 1)
    public void switchToPopup(String popupName) {
        switchToDefaultMode();
        try {
            toolkit.targetInstructions().switchToPopup(popupName);
        } catch (PopupException ex) {
            throw new PopupNotFoundException(popupName);
        }
    }

    @Keyword("Switch to default window")
    public void switchToDefaultWindow() {
        switchToDefaultMode();
        toolkit.targetInstructions().switchToDefaultWindow();
    }

    @Keyword("Close popup")
    @Parameters(min = 1)
    public void closePopup(String popupName) {
        try {
            toolkit.targetInstructions().closePopup(popupName);
        } catch (PopupException ex) {
            throw new PopupNotFoundException(popupName, ex);
        }
    }

    @Keyword("Close all popups")
    public void closeAllPopups() {
        toolkit.targetInstructions().closeAllPopups();
    }

    @Keyword("Switch to frame")
    @Parameters(min = 0)
    public void switchToFrame(String keyName)
            throws WebKeyTypeUnsupportedException {
        switchToDefaultMode();
        WebKey key = convertToWebKey(keyName);
        try {
            toolkit.targetInstructions().switchToIFrame(key);
        } catch (ExecutionException ex) {
            throw new UnableToSwitchToFrameException(keyName);
        }
    }

    @Keyword("Switch to default frame")
    @Parameters(min = 0)
    public void switchToDefaultFrame() {
        switchToDefaultMode();
        toolkit.targetInstructions().switchToDefaultFrame();
    }
}
