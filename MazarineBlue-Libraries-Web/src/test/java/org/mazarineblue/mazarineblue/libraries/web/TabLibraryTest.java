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

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.mazarineblue.executors.util.AbstractExecutorTestHelper;
import org.mazarineblue.keyworddriven.Library;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class TabLibraryTest
        extends AbstractExecutorTestHelper {

    private WebDriver driver;
    private Library lib;

    @BeforeClass
    public static void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @Before
    public void setup() {
        driver = new ChromeDriver();
        lib = new TabLibrary(driver);
    }

    @After
    public void teardown() {
        if (driver != null)
            driver.quit();
        driver = null;
    }

    @Test
    public void test() {
        System.out.print("");
    }

    /**
     * Test of tabCount method, of class TabLibrary.
     */
    @Test
    @Ignore
    public void testTabCount() {
        System.out.println("tabCount");
        TabLibrary instance = null;
        int expResult = 0;
        int result = instance.tabCount();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of openInNewTab method, of class TabLibrary.
     */
    @Test
    @Ignore
    public void testOpenInNewTab() {
        System.out.println("openInNewTab");
        String url = "";
        String tabName = "";
        TabLibrary instance = null;
        instance.openInNewTab(url, tabName);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of switchToTab method, of class TabLibrary.
     */
    @Test
    @Ignore
    public void testSwitchToTab() {
        System.out.println("switchToTab");
        String tabName = "";
        TabLibrary instance = null;
        instance.switchToTab(tabName);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of closeTab method, of class TabLibrary.
     */
    @Test
    @Ignore
    public void testCloseTab_0args() {
        System.out.println("closeTab");
        TabLibrary instance = null;
        instance.closeTab();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of closeTabsToTheRight method, of class TabLibrary.
     */
    @Test
    @Ignore
    public void testCloseTabsToTheRight() {
        System.out.println("closeTabsToTheRight");
        TabLibrary instance = null;
        instance.closeTabsToTheRight();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of closeOtherTabs method, of class TabLibrary.
     */
    @Test
    @Ignore
    public void testCloseOtherTabs() {
        System.out.println("closeOtherTabs");
        TabLibrary instance = null;
        instance.closeOtherTabs();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of closeTab method, of class TabLibrary.
     */
    @Test
    @Ignore
    public void testCloseTab_String() {
        System.out.println("closeTab");
        String tabName = "";
        TabLibrary instance = null;
        instance.closeTab(tabName);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
