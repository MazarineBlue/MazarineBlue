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
package org.mazarineblue.test.datadriven3;

import java.util.Collection;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.mazarineblue.datasources.DataSource;
import org.mazarineblue.datasources.ObjectArraySource;
import org.mazarineblue.test.datadriven3.listeners.ReportListener;
import org.mazarineblue.test.datadriven3.registry.ParallelType;
import org.mazarineblue.test.datadriven3.registry.TestProfile;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.nl>}
 */
public class DataDrivenRunnerTest {

    @BeforeClass
    public static void setupClass() throws Exception {
    }

    @AfterClass
    public static void teardownClass() throws Exception {
    }

    private DataDrivenRunner runner;
    private SuiteA suiteA;
    private SuiteB suiteB;

    @Before
    public void setup() throws Exception {
        DataSource source = new ObjectArraySource("Test source", true);
        runner = new DataDrivenRunner(source);
        runner.add(suiteA = new SuiteA());
        runner.add(suiteB = new SuiteB());
    }

    @After
    public void teardown() throws Exception {
        runner = null;
    }

    @Test
    public void testAdd() {
        ReportListener reportListeners = new TestReportListener();
        Assert.assertEquals(true, runner.add(reportListeners));
    }

    @Test
    public void testRemove() {
        ReportListener reportListeners = new TestReportListener();
        Assert.assertEquals(false, runner.remove(reportListeners));
        Assert.assertEquals(true, runner.add(reportListeners));
        Assert.assertEquals(true, runner.remove(reportListeners));
    }

    @Test
    public void testSetSources() {
        DataSource source = new TestDataSource();
        runner.setParallelType(ParallelType.SUITE);
        runner.setSources(source);
        Assert.assertEquals(ParallelType.NONE, runner.getParallelType());
    }

    @Test
    public void testSetSourcesArray() {
        System.out.println("setSources");
        DataSource source = new TestDataSource();
        DataSource[] sources = new DataSource[]{source, source};
        runner.setParallelType(ParallelType.SUITE);
        runner.setSources(sources);
        Assert.assertEquals(ParallelType.SUITE, runner.getParallelType());
    }

    /**
     * Test of fetchAll method, of class DataDrivenRunner.
     */
    @Test
    public void testFetchAll() {
        Collection<TestProfile> result = runner.fetchAll();
        Assert.assertEquals(true, result.contains(new TestProfile(suiteA, new String[]{"Group 1"}, "Test 1")));
        Assert.assertEquals(true, result.contains(new TestProfile(suiteA, new String[]{"Group 2"}, "Test 2")));
        Assert.assertEquals(true, result.contains(new TestProfile(suiteA, new String[]{"Group 1", "Group 2"}, "Test 3")));
        Assert.assertEquals(true, result.contains(new TestProfile(suiteA, new String[0], "Test 4")));
        Assert.assertEquals(true, result.contains(new TestProfile(suiteA, new String[0], "Test 5")));
        Assert.assertEquals(true, result.contains(new TestProfile(suiteB, new String[0], "Test 6")));
        Assert.assertEquals(true, result.contains(new TestProfile(suiteB, new String[0], "Test 7")));
        Assert.assertEquals(7, result.size());
    }

    @Test
    public void testRunWithNullSource() throws Exception {
        DataSource source = null;
        runner = new DataDrivenRunner(source);
        runner.add(suiteA = new SuiteA());
        List<TestProfile> result = runner.runAll();
        Assert.assertEquals(5, result.size());
        assertResultState(result, 5, TestProfile.State.PASS);
    }

    private void assertResultState(List<TestProfile> result, int n, TestProfile.State expected) {
        for (TestProfile profile : result)
            Assert.assertEquals(expected, profile.getState());
    }

    /**
     * Test of runAll method, of class DataDrivenRunner.
     */
    @Test
    public void testRunAll() throws Exception {
        List<TestProfile> result = runner.runAll();
        Assert.assertEquals(7, result.size());
        assertResultState(result, 7, TestProfile.State.PASS);
    }

    /**
     * Test of runSuiteAndDependencies method, of class DataDrivenRunner.
     */
    @Ignore
    @Test
    public void testRunSuiteAndDependencies() throws Exception {
        System.out.println("runSuiteAndDependencies");
        String suite = "";
        DataDrivenRunner instance = null;
        instance.runSuiteAndDependencies(suite);
        // TODO review the generated test code and remove the default call to Assert.fail.
        Assert.fail("The test case is a prototype.");
    }

    /**
     * Test of fetchSuiteAndDependencies method, of class DataDrivenRunner.
     */
    @Ignore
    @Test
    public void testFetchSuiteAndDependencies() {
        System.out.println("fetchSuiteAndDependencies");
        String suite = "";
        DataDrivenRunner instance = null;
        Collection<TestProfile> expResult = null;
        Collection<TestProfile> result = instance.fetchSuiteAndDependencies(suite);
        Assert.assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to Assert.fail.
        Assert.fail("The test case is a prototype.");
    }

    /**
     * Test of runGroupAndDependencies method, of class DataDrivenRunner.
     */
    @Ignore
    @Test
    public void testRunGroupAndDependencies() throws Exception {
        System.out.println("runGroupAndDependencies");
        String suite = "";
        String group = "";
        DataDrivenRunner instance = null;
        instance.runGroupAndDependencies(suite, group);
        // TODO review the generated test code and remove the default call to Assert.fail.
        Assert.fail("The test case is a prototype.");
    }

    /**
     * Test of fetchGroupAndDependencies method, of class DataDrivenRunner.
     */
    @Ignore
    @Test
    public void testFetchGroupAndDependencies() {
        System.out.println("fetchGroupAndDependencies");
        String suite = "";
        String group = "";
        DataDrivenRunner instance = null;
        Collection<TestProfile> expResult = null;
        Collection<TestProfile> result = instance.fetchGroupAndDependencies(suite, group);
        Assert.assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to Assert.fail.
        Assert.fail("The test case is a prototype.");
    }

    /**
     * Test of runTestAndDependencies method, of class DataDrivenRunner.
     */
    @Ignore
    @Test
    public void testRunTestAndDependencies() throws Exception {
        System.out.println("runTestAndDependencies");
        String suite = "";
        String test = "";
        DataDrivenRunner instance = null;
        instance.runTestAndDependencies(suite, test);
        // TODO review the generated test code and remove the default call to Assert.fail.
        Assert.fail("The test case is a prototype.");
    }

    /**
     * Test of fetchTestAndDependencies method, of class DataDrivenRunner.
     */
    @Ignore
    @Test
    public void testFetchTestAndDependencies() {
        System.out.println("fetchTestAndDependencies");
        String suite = "";
        String test = "";
        DataDrivenRunner instance = null;
        Collection<TestProfile> expResult = null;
        Collection<TestProfile> result = instance.fetchTestAndDependencies(suite, test);
        Assert.assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to Assert.fail.
        Assert.fail("The test case is a prototype.");
    }

    /**
     * Test of run method, of class DataDrivenRunner.
     */
    @Ignore
    @Test
    public void testRun() throws Exception {
        System.out.println("run");
        List<TestProfile> profiles = null;
        DataDrivenRunner instance = null;
        instance.run(profiles);
        // TODO review the generated test code and remove the default call to Assert.fail.
        Assert.fail("The test case is a prototype.");
    }

    /**
     * Test of addValidators method, of class DataDrivenRunner.
     */
    @Ignore
    @Test
    public void testAddValidators() {
        System.out.println("addValidators");
        Object obj = null;
        DataDrivenRunner instance = null;
        instance.addValidators(obj);
        // TODO review the generated test code and remove the default call to Assert.fail.
        Assert.fail("The test case is a prototype.");
    }
}
