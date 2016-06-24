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

import org.mazarineblue.webdriver.WebToolkit.WindowInstructions;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Window;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
class WindowInstructionImpl
        implements WindowInstructions {

    private Window window;

    WindowInstructionImpl(WebDriver driver) {
        setWebDriver(driver);
    }

    public final void setWebDriver(WebDriver driver) {
        this.window = driver.manage().window();
    }

    @Override
    public void maximizeWindow() {
        window.maximize();
    }

    @Override
    public void minimizeWindow() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void moveWindow(Point targerPosition) {
        window.setPosition(targerPosition);
    }

    @Override
    public void resizeWindow(Dimension targetSize) {
        window.setSize(targetSize);
    }

    @Override
    public boolean validateWindowPosition(Point expected) {
        return validateWindowPosition(expected, new Point(0, 0));
    }

    @Override
    public boolean validateWindowPosition(Point expected, Point epsilon) {
        Point actual = window.getPosition();
        if (withinRange(expected.x, actual.x, epsilon.x) == false)
            return false;
        return withinRange(expected.y, actual.y, epsilon.y);
    }

    private boolean withinRange(int expected, int actual, int epsilon) {
        return expected - actual <= epsilon && expected - actual >= epsilon;
    }

    @Override
    public boolean validateWindowSize(Dimension expected) {
        return validateWindowSize(expected, new Dimension(0, 0));
    }

    @Override
    public boolean validateWindowSize(Dimension expected, Dimension epsilon) {
        Dimension actual = window.getSize();
        if (withinRange(expected.width, actual.width, epsilon.width) == false)
            return false;
        return withinRange(expected.height, actual.height, epsilon.height);
    }

    @Override
    public Point getWindowPosition() {
        return window.getPosition();
    }

    @Override
    public Dimension getWindowSize() {
        return window.getSize();
    }
}
