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
package org.mazarineblue.test.report;

import java.util.Collection;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.test.report.util.ManualTestTicket;
import org.mazarineblue.test.report.visitors.TestObjectVisitorException;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class ReportTest {

    private static int idCount = 0;
    private final String[] validPlatforms = new String[]{"Platform A",
                                                         "Platform B"};
    private final String[] suites = new String[]{"Suite a", "Suite b"};
    private final String[] testcases = new String[]{"Testcase 1", "Testcase 2",
                                                    "Testcase 3"};
    private final String[] steps = new String[]{"First step", "Second step",
                                                "Last step"};
    private final String wrongPlatform = "Platform C";
    private Report report;

    public ReportTest() {
    }

    @Before
    public void setup() {
        report = new Report("Title", validPlatforms);
    }

    @After
    public void teardown() {
        report = null;
    }

    @Test
    public void shouldAddAllPlatforms()
            throws TestObjectVisitorException {
        report.declareTestcase(validPlatforms[0], suites[0], testcases[0]);
        assertCounts(report, 1, 1, 1, 0, 0);
        report.declareTestcase(validPlatforms[1], suites[0], testcases[1]);
        assertCounts(report, 1, 1, 2, 0, 0);
        report.declareTestcase(validPlatforms[1], suites[1], testcases[2]);
        assertCounts(report, 1, 2, 3, 0, 0);
    }

    // <editor-fold defaultstate="collapsed" desc="First used in testDeclareTestcaseValidPlatform()">
    private void assertCounts(Report report, int reports, int suites,
                              int testcases, int steps, int ticket)
            throws TestObjectVisitorException {
        TestVisitor visitor = new TestVisitor();
        report.accept(visitor);
        Assert.assertEquals(reports, visitor.countReports);
        Assert.assertEquals(suites, visitor.countSuites);
        Assert.assertEquals(testcases, visitor.countTestcase);
        Assert.assertEquals(steps, visitor.countSteps);
        Assert.assertEquals(ticket, visitor.countTickets);
    }
    // </editor-fold>

    @Test(expected = IllegalPlatformException.class)
    public void shouldThrowIllegalPlatformException() {
        report.declareTestcase(wrongPlatform, "Suite", "Testcase");
    }

    @Test
    public void shouldSetStepPassed()
            throws TestObjectVisitorException {
        report.setPassed(validPlatforms[0], suites[0], testcases[0], steps[0]);
        assertReportStatuses(/*  */report, /*                              */ Status.unset, Status.passed, Status.unset);
        assertSuiteStatuses(/*   */report, suites[0], /*                   */ Status.unset, Status.passed, Status.unset);
        assertTestcaseStatuses(/**/report, suites[0], testcases[0], /*     */ Status.unset, Status.passed, Status.unset);
        assertStepStatuses(/*    */report, suites[0], testcases[0], steps[0],
                                       Status.unset, Status.passed, Status.unset);
    }

    // <editor-fold defaultstate="collapsed" desc="First used in testSetPassedSteps()">
    private void assertReportStatuses(Report report, Status... arr) {
        Assert.assertEquals(arr.length, validPlatforms.length + 1);
        Status[] statuses = new Status[arr.length];
        statuses[0] = report.getStatus();
        for (int i = 0; i < validPlatforms.length; ++i)
            statuses[i + 1] = report.getStatus(validPlatforms[i]);
        Assert.assertArrayEquals(arr, statuses);
    }

    private void assertSuiteStatuses(Report report, String suite, Status... arr) {
        Assert.assertEquals(arr.length, validPlatforms.length + 1);
        Status[] statuses = new Status[arr.length];
        statuses[0] = report.getStatus(null, suite);
        for (int i = 0; i < validPlatforms.length; ++i)
            statuses[i + 1] = report.getStatus(validPlatforms[i], suite);
        Assert.assertArrayEquals(arr, statuses);
    }

    private void assertTestcaseStatuses(Report report, String suite,
                                        String testcase, Status... arr) {
        Assert.assertEquals(arr.length, validPlatforms.length + 1);
        Status[] statuses = new Status[arr.length];
        statuses[0] = report.getStatus(null, suite, testcase);
        for (int i = 0; i < validPlatforms.length; ++i)
            statuses[i + 1] = report.getStatus(validPlatforms[i], suite,
                                               testcase);
        Assert.assertArrayEquals(arr, statuses);
    }

    private void assertStepStatuses(Report report, String suite, String testcase,
                                    String step, Status... arr) {
        Assert.assertEquals(arr.length, validPlatforms.length + 1);
        Status[] statuses = new Status[arr.length];
        statuses[0] = report.getStatus(null, suite, testcase, step);
        for (int i = 0; i < validPlatforms.length; ++i)
            statuses[i + 1] = report.getStatus(validPlatforms[i], suite,
                                               testcase, step);
        Assert.assertArrayEquals(arr, statuses);
    }
// </editor-fold>

    @Test
    public void shouldSetTwoStepsPassed() {
        report.setPassed(validPlatforms[0], suites[0], testcases[0], steps[0]);
        report.setPassed(validPlatforms[1], suites[0], testcases[1], steps[1]);
        assertReportStatuses(/*  */report, /*                              */ Status.unset, Status.unset, Status.unset);
        assertSuiteStatuses(/*   */report, suites[0], /*                   */ Status.unset, Status.unset, Status.unset);
        assertTestcaseStatuses(/**/report, suites[0], testcases[0], /*     */ Status.unset, Status.passed, Status.unset);
        assertStepStatuses(/*    */report, suites[0], testcases[0], steps[0],
                                       Status.unset, Status.passed, Status.unset);
        assertTestcaseStatuses(/**/report, suites[0], testcases[1], /*     */ Status.unset, Status.unset, Status.passed);
        assertStepStatuses(/*    */report, suites[0], testcases[1], steps[1],
                                       Status.unset, Status.unset, Status.passed);
    }

    @Test
    public void shouldOverrideTestcase() {
        report.setPassed(validPlatforms[0], suites[0], testcases[0], steps[0]);
        report.setPassed(validPlatforms[1], suites[0], testcases[1], steps[1]);
        report.setPassed(validPlatforms[0], suites[0], testcases[1]);
        assertReportStatuses(/*  */report, /*                              */ Status.unset, Status.passed, Status.unset);
        assertSuiteStatuses(/*   */report, suites[0], /*                   */ Status.unset, Status.passed, Status.unset);
        assertTestcaseStatuses(/**/report, suites[0], testcases[0], /*     */ Status.unset, Status.passed, Status.unset);
        assertStepStatuses(/*    */report, suites[0], testcases[0], steps[0],
                                       Status.unset, Status.passed, Status.unset);
        assertTestcaseStatuses(/**/report, suites[0], testcases[1], /*     */ Status.passed, Status.passed,
                                       Status.passed);
        assertStepStatuses(/*    */report, suites[0], testcases[1], steps[1],
                                       Status.unset, Status.unset, Status.passed);
    }

    @Test
    public void shouldSetStepFailed() {
        report.setPassed(validPlatforms[0], suites[0], testcases[0], steps[0]);
        report.setFailed(validPlatforms[1], suites[0], testcases[0], steps[0]);
        assertReportStatuses(/*  */report, /*                              */ Status.failed, Status.passed,
                                       Status.failed);
        assertSuiteStatuses(/*   */report, suites[0], /*                   */ Status.failed, Status.passed,
                                       Status.failed);
        assertTestcaseStatuses(/**/report, suites[0], testcases[0], /*     */ Status.failed, Status.passed,
                                       Status.failed);
        assertStepStatuses(/*    */report, suites[0], testcases[0], steps[0],
                                       Status.failed, Status.passed,
                                       Status.failed);
    }

    @Test
    public void shouldSetStepFailedAndDominateGlobalResult() {
        report.setPassed(validPlatforms[0], suites[0], testcases[0], steps[0]);
        report.setFailed(validPlatforms[1], suites[0], testcases[0], steps[0]);
        report.setPassed(validPlatforms[1], suites[0], testcases[1], steps[1]);
        assertReportStatuses(/*  */report, /*                              */ Status.failed, Status.unset,
                                       Status.failed);
        assertSuiteStatuses(/*   */report, suites[0], /*                   */ Status.failed, Status.unset,
                                       Status.failed);
        assertTestcaseStatuses(/**/report, suites[0], testcases[0], /*     */ Status.failed, Status.passed,
                                       Status.failed);
        assertStepStatuses(/*    */report, suites[0], testcases[0], steps[0],
                                       Status.failed, Status.passed,
                                       Status.failed);
        assertTestcaseStatuses(/**/report, suites[0], testcases[1], /*     */ Status.unset, Status.unset, Status.passed);
        assertStepStatuses(/*    */report, suites[0], testcases[1], steps[1],
                                       Status.unset, Status.unset, Status.passed);
    }

    @Test
    public void shouldSetStepFailedOverrideTestcaseStatus() {
        report.setPassed(validPlatforms[0], suites[0], testcases[0], steps[0]);
        report.setPassed(validPlatforms[1], suites[0], testcases[1], steps[1]);
        report.setPassed(validPlatforms[0], suites[0], testcases[1]);
        report.setFailed(validPlatforms[0], suites[0], testcases[1], steps[1]);
        assertReportStatuses(/*  */report, /*                              */ Status.unset, Status.passed, Status.unset);
        assertSuiteStatuses(/*   */report, suites[0], /*                   */ Status.unset, Status.passed, Status.unset);
        assertTestcaseStatuses(/**/report, suites[0], testcases[0], /*     */ Status.unset, Status.passed, Status.unset);
        assertStepStatuses(/*    */report, suites[0], testcases[0], steps[0],
                                       Status.unset, Status.passed, Status.unset);
        assertTestcaseStatuses(/**/report, suites[0], testcases[1], /*     */ Status.passed, Status.passed,
                                       Status.passed);
        assertStepStatuses(/*    */report, suites[0], testcases[1], steps[1],
                                       Status.failed, Status.failed,
                                       Status.passed);
    }

    @Test
    public void shouldAddTicket2Report() {
        report.addTicket(generateTicket("on report level"));
        Collection<Ticket> collection = report.fetchTickets(suites[0],
                                                            testcases[0],
                                                            steps[0]);
        Assert.assertEquals(1, report.fetchTickets().size());
        Assert.assertEquals(0, report.fetchTickets(suites[0]).size());
        Assert.assertEquals(0,
                            report.fetchTickets(suites[0], testcases[0]).size());
        Assert.assertEquals(0, report.fetchTickets(suites[0], testcases[0],
                                                   steps[0]).size());
    }

    @Test
    public void shouldAddTicket2Suite() {
        report.addTicket(generateTicket("on report level"));
        report.addTicket(generateTicket("on suite level"), suites[0]);
        Assert.assertEquals(2, report.fetchTickets().size());
        Assert.assertEquals(1, report.fetchTickets(suites[0]).size());
        Assert.assertEquals(0,
                            report.fetchTickets(suites[0], testcases[0]).size());
        Assert.assertEquals(0, report.fetchTickets(suites[0], testcases[0],
                                                   steps[0]).size());
    }

    @Test
    public void shouldAddTicket2Testcase() {
        report.addTicket(generateTicket("on report level"));
        report.addTicket(generateTicket("on suite level"), suites[0]);
        report.addTicket(generateTicket("on testcase level"), suites[0],
                         testcases[0]);
        Assert.assertEquals(3, report.fetchTickets().size());
        Assert.assertEquals(2, report.fetchTickets(suites[0]).size());
        Assert.assertEquals(1,
                            report.fetchTickets(suites[0], testcases[0]).size());
        Assert.assertEquals(0, report.fetchTickets(suites[0], testcases[0],
                                                   steps[0]).size());
    }

    @Test
    public void shouldAddTicket2Step() {
        report.addTicket(generateTicket("on report level"));
        report.addTicket(generateTicket("on suite level"), suites[0]);
        report.addTicket(generateTicket("on testcase level"), suites[0],
                         testcases[0]);
        report.addTicket(generateTicket("on step level"), suites[0],
                         testcases[0], steps[0]);
        Assert.assertEquals(4, report.fetchTickets().size());
        Assert.assertEquals(3, report.fetchTickets(suites[0]).size());
        Assert.assertEquals(2,
                            report.fetchTickets(suites[0], testcases[0]).size());
        Assert.assertEquals(1, report.fetchTickets(suites[0], testcases[0],
                                                   steps[0]).size());
    }

    private static Ticket generateTicket(String level) {
        ManualTestTicket ticket = new ManualTestTicket("", "", "");
        ticket.setId(++idCount);
        ticket.metadata.setProject("Test Project");
        ticket.details.setPriority("High");
        ticket.details.setUrl("http://www.specialisterren.nl/");
        ticket.description.setSummary("A summary " + level);
        ticket.description.setReproductionSteps("Zo kun je de bug tegen komen.");
        ticket.description.setExpectedResult("Dit verwachte ik, maar...");
        ticket.description.setActualResult("ik kreeg iets anders.");
        return ticket;
    }
}
