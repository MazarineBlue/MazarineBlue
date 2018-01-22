/*
 * Copyright (c) 2016 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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
package org.mazarineblue.runners.fitnesse;

import fitnesse.testrunner.MultipleTestSystemFactory;
import fitnesse.testrunner.MultipleTestsRunner;
import fitnesse.testrunner.PagesByTestSystem;
import fitnesse.testsystems.Assertion;
import fitnesse.testsystems.ExceptionResult;
import fitnesse.testsystems.TestExecutionException;
import fitnesse.testsystems.TestPage;
import fitnesse.testsystems.TestResult;
import fitnesse.testsystems.TestSummary;
import fitnesse.testsystems.TestSystem;
import fitnesse.testsystems.TestSystemFactory;
import fitnesse.testsystems.TestSystemListener;
import fitnesse.testsystems.slim.CustomComparatorRegistry;
import fitnesse.testsystems.slim.tables.SlimTableFactory;
import fitnesse.wiki.WikiPage;
import static fitnesse.wiki.WikiPageUtil.setPageContents;
import static fitnesse.wiki.fs.InMemoryPage.makeRoot;
import java.io.Closeable;
import java.io.IOException;
import static java.util.Arrays.asList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.fs.MemoryFileSystem;
import static org.mazarineblue.runners.fitnesse.util.FitnesseUtil.getMemoryFootprint;
import static org.mazarineblue.runners.fitnesse.util.FitnesseUtil.getMemoryFootprintDiff;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class FitnessePluginIT {

    private static final long MB = 1048576;
    private MultipleTestSystemFactory testSystemFactory;

    @Before
    public void setup()
            throws IOException {
        testSystemFactory = new MultipleTestSystemFactory(new SlimTableFactory(), new CustomComparatorRegistry());
        MemoryFileSystem fs = new MemoryFileSystem();
        MazarineBlueFitnessePlugin plugin = new MazarineBlueFitnessePlugin(fs);
        plugin.registerTestSystemFactories(testSystemFactory);
    }

    @Test
    public void testScript()
            throws TestExecutionException {
        long before = getMemoryFootprint();

        String content = "!define TEST_SYSTEM {mazarineblue}\n"
                + "\n"
                + "!| script | login dialog driver | Bob | xyzzy |\n"
                + "| login with username | Bob | and password | xyzzy |\n"
                + "| check | login message | Bob logged in. |\n"
                + "| reject | login with username | Bob | and password | bad password |\n"
                + "| check | login message | Bob not logged in. |\n"
                + "| check not | login message | Bob logged in. |\n"
                + "| ensure | login with username | Bob | and password | xyzzy |\n"
                + "| note | this is a comment |\n"
                + "| show | number of login attempts |\n"
                + "| $symbol= | login message |\n"
                + "";

        // @TODO Assertion need to be added (using logging?)
        runTest(content, 6, 0, 0);

        double diffMB = getMemoryFootprintDiff(before, MB);
        assertEquals(1d, diffMB, 3d);
    }

    // <editor-fold defaultstate="true" desc="Helper methods for runTest()">
    private void runTest(String testcase, int right, int wrong, int exceptions)
            throws TestExecutionException {
        runTest(getTestcase(testcase), this.testSystemFactory, new AssertingListener(right, wrong, exceptions));
    }

    private static PagesByTestSystem getTestcase(String content) {
        WikiPage page = makeRoot("RooT");
        setPageContents(page, content);
        List<WikiPage> pages = asList(page);
        return new PagesByTestSystem(pages, page);
    }

    private static void runTest(PagesByTestSystem testcase, TestSystemFactory factory, AssertingListener listener)
            throws TestExecutionException {
        MultipleTestsRunner runner = new MultipleTestsRunner(testcase, factory);
        runner.addTestSystemListener(listener);
        runner.executeTestPages();
    }

    private static class AssertingListener
            implements TestSystemListener, Closeable {

        private final int right, wrong, exceptions;

        public AssertingListener(int right, int wrong, int exceptions) {
            this.right = right;
            this.wrong = wrong;
            this.exceptions = exceptions;
        }

        @Override
        public void close() {
        }

        @Override
        public void testStarted(TestPage page) {
        }

        @Override
        public void testComplete(TestPage test, TestSummary testSummary) {
            assertEquals(this.toString(), toString(testSummary));
            assertEquals(right, testSummary.getRight());
            assertEquals(wrong, testSummary.getWrong());
            assertEquals(exceptions, testSummary.getExceptions());
        }

        @Override
        public String toString() {
            return toString(right, wrong, exceptions);
        }

        private static String toString(TestSummary testSummary) {
            return toString(testSummary.getRight(), testSummary.getWrong(), testSummary.getExceptions());
        }

        private static String toString(int r, int w, int e) {
            return "r " + r + " w " + w + " e " + e;
        }

        @Override
        public void testSystemStarted(TestSystem system) {
        }

        @Override
        public void testOutputChunk(String chunk) {
        }

        @Override
        public void testSystemStopped(TestSystem system, Throwable throwable) {
        }

        @Override
        public void testAssertionVerified(Assertion assertion, TestResult result) {
        }

        @Override
        public void testExceptionOccurred(Assertion assertion, ExceptionResult result) {
        }
    }
    // </editor-fold>
}
