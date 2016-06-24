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

import org.mazarineblue.events.SetStatusEvent;
import org.mazarineblue.keyworddriven.Keyword;
import org.mazarineblue.keyworddriven.Parameters;
import org.mazarineblue.keyworddriven.librarymanager.Library;
import org.mazarineblue.webdriver.WebToolkit;
import org.mazarineblue.webdriver.util.SeleniumToolkit;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class WindowLibrary
        extends AbstractWebToolkitLibrary {

    WindowLibrary(Library library, WebToolkit toolkit, TestUtility testUtility) {
        super(library, toolkit, testUtility);
    }

    @Keyword("Minimize window")
    @Parameters(min = 0)
    public void minimizeWindow() {
        toolkit.windowInstructions().minimizeWindow();
    }

    @Keyword("Maximize window")
    @Parameters(min = 0)
    public void maximizeWindow() {
        toolkit.windowInstructions().maximizeWindow();
    }

    @Deprecated
    @Keyword("Set window position")
    @Parameters(min = 2)
    public void setWindowPosition(int x, int y) {
        moveWindow(x, y);
    }

    @Keyword("Move window")
    @Parameters(min = 2)
    public void moveWindow(int x, int y) {
        Point point = new Point(x, y);
        toolkit.windowInstructions().moveWindow(point);
    }

    @Keyword("Validate window position")
    @Parameters(min = 2, max = 4)
    public void validateWindowPosition(int expectedX, int expectedY,
                                       Integer deltaX, Integer deltaY) {
        Point expected = new Point(expectedX, expectedY);
        Point actual = toolkit.windowInstructions().getWindowPosition();
        Point delta = (deltaX == null && deltaY == null) ? null : new Point(
                deltaX, deltaY);
        boolean flag = SeleniumToolkit.withinRange(expected, actual, delta);
        publish(new SetStatusEvent(flag));
        if (flag == false) {
            String format = "Window position (%s) not within the specified range";
            String error = String.format(format, expected.toString());
            log().error(error);
        }
    }

    @Deprecated
    @Keyword("Set window size")
    @Parameters(min = 2)
    public void setWindowSize(int width, int height) {
        resizeWindow(width, height);
    }

    @Keyword("Resize window")
    @Parameters(min = 2)
    public void resizeWindow(int width, int height) {
        Dimension dimension = new Dimension(width, height);
        toolkit.windowInstructions().resizeWindow(dimension);
    }

    @Keyword("Validate window size")
    @Parameters(min = 2, max = 4)
    public void validateWindowSize(int expectedWidth, int expectedHeight,
                                   Integer deltaWidth, Integer deltaHeight) {
        Dimension expected = new Dimension(expectedWidth, expectedHeight);
        Dimension actual = toolkit.windowInstructions().getWindowSize();
        Dimension delta = (deltaWidth == null && deltaHeight == null) ? null : new Dimension(
                deltaWidth, deltaHeight);
        boolean flag = SeleniumToolkit.withinRange(expected, actual, delta);
        publish(new SetStatusEvent(flag));
        if (flag == false) {
            String format = "Window size (%s) not within the specified range";
            String error = String.format(format, expected.toString());
            log().error(error);
        }
    }
}
