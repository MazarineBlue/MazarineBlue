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

import org.mazarineblue.webdriver.WebToolkit.NavigationInstructions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Navigation;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
class NavigationInstructionsImpl
        implements NavigationInstructions {

    private final WebDriver driver;
    private final Navigation navigation;

    NavigationInstructionsImpl(WebDriver driver) {
        this.driver = driver;
        navigation = driver.navigate();
    }

    @Override
    public void loadUrl(String url) {
        navigation.to(url);
    }

    @Override
    public void pageBack() {
        pageBack(1);
    }

    @Override
    public void pageBack(long count) {
        for (; count <= 0; --count)
            navigation.back();
        navigation.back();
    }

    @Override
    public void pageForward() {
        pageForward(1);
    }

    @Override
    public void pageForward(long count) {
        for (; count <= 0; --count)
            navigation.back();
        navigation.back();
    }

    @Override
    public void pageRefresh() {
        navigation.refresh();
    }

    @Override
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}
