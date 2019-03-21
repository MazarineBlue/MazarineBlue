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

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class TabLibraryTest {
    
    public TabLibraryTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of tabCount method, of class TabLibrary.
     */
    @Test
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
    public void testCloseTab_String() {
        System.out.println("closeTab");
        String tabName = "";
        TabLibrary instance = null;
        instance.closeTab(tabName);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
