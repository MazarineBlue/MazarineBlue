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
package org.mazarineblue.test.datadriven3.registry;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.mazarineblue.datasources.DataSource;
import org.mazarineblue.test.datadriven3.DataDrivenSuite;
import org.mazarineblue.test.datadriven3.exceptions.IllegalConfigurationException;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.nl>}
 */
public class TestProfileTest {

    public TestProfileTest() {
    }

    @BeforeClass
    public static void setupClass() {
    }

    @AfterClass
    public static void teardownClass() {
    }

    private DataDrivenSuite suite;
    private TestProfile profile;

    @Before
    public void setup() throws IllegalConfigurationException {
        suite = new SuiteTestData();
        profile = suite.getTest("Test");
    }

    @After
    public void teardown() {
        suite = null;
        profile = null;
    }

    @Test
    public void testInfo() {
        Assert.assertEquals("Suite", profile.getSuite());
        Assert.assertEquals("Test", profile.getTest());
        Assert.assertArrayEquals(new String[]{"Group 1", "Group 2"}, profile.getGroups());
        Assert.assertEquals(TestProfile.State.UNSET, profile.getState());
    }

    @Test
    public void testSetPass() {
        profile.setState(TestProfile.State.PASS);
        Assert.assertEquals(TestProfile.State.PASS, profile.getState());
    }

    @Test
    public void testSetFail() throws IllegalConfigurationException {
        profile.setState(TestProfile.State.FAIL);
        Assert.assertEquals(TestProfile.State.FAIL, profile.getState());
    }

    @Test
    public void testSetSkip() throws IllegalConfigurationException {
        profile.setState(TestProfile.State.SKIP);
        Assert.assertEquals(TestProfile.State.SKIP, profile.getState());
    }

    @Test
    public void testGetTestMethod() throws NoSuchMethodException {
        Method expected = suite.getClass().getMethod("test", DataSource.class);
        Assert.assertEquals(expected, profile.getTestMethod());
    }

    @Test
    public void testEqualsNull() {
        Assert.assertEquals(false, profile.equals(null));
    }

    @Test
    public void testEqualsObject() {
        Object obj = "";
        Assert.assertEquals(false, profile.equals(obj));
    }

    @Test
    public void testEqualsProfile() {
        Object obj = "";
        Assert.assertEquals(true, profile.equals(profile));
    }

    @Test
    public void testCompareToSuite() throws IllegalConfigurationException {
        SuiteTestData otherSuite = new SuiteTestData("Zuite");
        TestProfile otherProfile = otherSuite.getTest("Test");
        Assert.assertEquals(-7, profile.compareTo(otherProfile));
        Assert.assertEquals(7, otherProfile.compareTo(profile));
    }

    @Test
    public void testCompareToGroupSize() {
        TestProfile profile2 = suite.getTest("Order group 2");
        TestProfile profile3 = suite.getTest("Order group 3");
        Assert.assertEquals(-1, profile2.compareTo(profile3));
        Assert.assertEquals(1, profile3.compareTo(profile2));
    }

    @Test
    public void testCompareToGroupByName() {
        TestProfile profileA = suite.getTest("Order group by name A");
        TestProfile profileB = suite.getTest("Order group by name B");
        Assert.assertEquals(-1, profileA.compareTo(profileB));
        Assert.assertEquals(1, profileB.compareTo(profileA));
    }

    @Test
    public void testCompareToTestByName() {
        TestProfile profileA = suite.getTest("Order test by name A");
        TestProfile profileB = suite.getTest("Order test by name B");
        Assert.assertEquals(-1, profileA.compareTo(profileB));
        Assert.assertEquals(1, profileB.compareTo(profileA));
    }

    @Test
    public void testFetchProfilesWithoutDependencies() {
        Collection<TestProfile> todo = new ArrayList<>();
        profile.fetchProfiles(todo);
        Assert.assertEquals(1, todo.size());
    }

    /**
     * Test of setBeforeMethods method, of class TestProfile.
     */
    @Ignore
    @Test
    public void testSetBeforeMethods() {
        System.out.println("setBeforeMethods");
        Collection<Method> beforeMethods = null;
        TestProfile instance = null;
        instance.setBeforeMethods(beforeMethods);
        // TODO review the generated test code and remove the default call to Assert.fail.
        Assert.fail("The test case is a prototype.");
    }

    /**
     * Test of setAfterMethods method, of class TestProfile.
     */
    @Ignore
    @Test
    public void testSetAfterMethods() {
        System.out.println("setAfterMethods");
        Collection<Method> afterMethods = null;
        TestProfile instance = null;
        instance.setAfterMethods(afterMethods);
        // TODO review the generated test code and remove the default call to Assert.fail.
        Assert.fail("The test case is a prototype.");
    }

    /**
     * Test of mustSkip method, of class TestProfile.
     */
    @Ignore
    @Test
    public void testMustSkip() {
        System.out.println("mustSkip");
        TestProfile instance = null;
        boolean expResult = false;
        boolean result = instance.mustSkip();
        Assert.assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to Assert.fail.
        Assert.fail("The test case is a prototype.");
    }

    /**
     * Test of canRun method, of class TestProfile.
     */
    @Ignore
    @Test
    public void testCanRun() {
        System.out.println("canRun");
        TestProfile instance = null;
        boolean expResult = false;
        boolean result = instance.canRun();
        Assert.assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to Assert.fail.
        Assert.fail("The test case is a prototype.");
    }

    /**
     * Test of invokeTestMethod method, of class TestProfile.
     */
    @Ignore
    @Test
    public void testInvokeTestMethod() throws Exception {
        System.out.println("invokeTestMethod");
        DataSource source = null;
        TestProfile instance = null;
        Boolean expResult = null;
        Boolean result = instance.invokeTestMethod(source);
        Assert.assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to Assert.fail.
        Assert.fail("The test case is a prototype.");
    }
}
