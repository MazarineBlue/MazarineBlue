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
package org.mazarineblue.test.report.keywords;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.mazarineblue.eventbus.EventHandler;
import org.mazarineblue.events.SetStatusEvent;
import org.mazarineblue.keyworddriven.Keyword;
import org.mazarineblue.keyworddriven.Parameters;
import org.mazarineblue.keyworddriven.librarymanager.Library;
import org.mazarineblue.test.events.SetTestcaseEvent;
import org.mazarineblue.test.report.Report;
import org.mazarineblue.test.report.exceptions.InstructionTestcaseNotCalledException;
import org.mazarineblue.test.report.exceptions.InstructionTeststepNotCalledException;
import org.mazarineblue.test.report.exceptions.NoActiveReportException;
import org.mazarineblue.test.report.exceptions.PlatformInstructionNotCalledException;
import org.mazarineblue.test.report.exceptions.PlatformNotFoundException;
import org.mazarineblue.test.report.exceptions.SuiteInstructionNotCalledException;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class InnerReportLibrary
        extends Library {

    private final ReportLibrary parent;
    private String platform, suite, testcase, step;

    InnerReportLibrary(ReportLibrary parent) {
        super("org.mazarineblue.test.report");
        this.parent = parent;
    }

    @Override
    protected void setup() {
    }

    @Override
    protected void teardown() {
    }

    private String getPlatform() {
        if (platform == null)
            throw new PlatformInstructionNotCalledException();
        return platform;
    }

    private String getSuite() {
        if (suite == null)
            throw new SuiteInstructionNotCalledException();
        return suite;
    }

    private String getTestcase() {
        if (suite == null)
            throw new InstructionTestcaseNotCalledException();
        return testcase;
    }

    private String getStep() {
        if (suite == null)
            throw new InstructionTeststepNotCalledException();
        return step;
    }

    @Keyword("Platform")
    @Parameters(min = 1, max = 1)
    public void platform(String platform) {
        if (getActiveReport().containsPlatform(platform) == false)
            throw new PlatformNotFoundException(platform);
        this.platform = platform;
    }

    private Report getActiveReport() {
        Report report = parent.getActiveReport();
        if (report == null)
            throw new NoActiveReportException();
        return report;
    }

    @Keyword("Suite")
    @Parameters(min = 1, max = 1)
    public void suite(String suite) {
        this.suite = suite;
    }

    @Keyword("Testcase")
    @Parameters(min = 1, max = 1)
    public void testcase(String testcase) {
        this.testcase = testcase;
        getActiveReport().declareTestcase(getPlatform(), getSuite(), testcase);
    }

    @Keyword("Teststep")
    @Parameters(min = 1, max = 1)
    public void teststep(String teststep) {
        this.step = teststep;
    }

    @Keyword("Testcase validate")
    public void testcaseValidate(String variable, String regex,
                                 String errorMessage) {
        if (validate(variable, regex))
            testcasePassed();
        else {
            log().error(errorMessage);
            testcaseFailed();
        }
    }

    private boolean validate(String variable, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(variable);
        return matcher == null ? false : matcher.find();
    }

    @EventHandler
    public void eventHandler(SetTestcaseEvent event) {
        platform = event.getPlatform();
        suite = event.getSuite();
        testcase = event.getTestcase();
    }

    @EventHandler
    public void eventHandler(SetStatusEvent event) {
        if (platform != null)
            getActiveReport().setStatus(event.passed(), platform, suite,
                                        testcase);
    }

    @Keyword("Testcase passed")
    @Parameters(min = 0, max = 0)
    public final void testcasePassed() {
        getActiveReport().setPassed(getPlatform(), getSuite(), getTestcase());
    }

    @Keyword("Testcase failed")
    @Parameters(min = 0, max = 0)
    public final void testcaseFailed() {
        getActiveReport().setFailed(getPlatform(), getSuite(), getTestcase());
    }

    @Keyword("Testcase status")
    @Parameters(min = 1, max = 1)
    public void testcasePassed(boolean flag) {
        getActiveReport().setStatus(flag, getPlatform(), getSuite(),
                                    getTestcase());
    }

    @Keyword("Teststep validate")
    public void teststepValidate(String variable, String regex,
                                 String errorMessage) {
        if (validate(variable, regex))
            teststepPassed();
        else {
            log().error(errorMessage);
            teststepFailed();
        }
    }

    @Keyword("Teststep passed")
    @Parameters(min = 0, max = 0)
    public final void teststepPassed() {
        getActiveReport().setPassed(getPlatform(), getSuite(), getTestcase(),
                                    getStep());
    }

    @Keyword("Teststep failed")
    @Parameters(min = 0, max = 0)
    public final void teststepFailed() {
        getActiveReport().setFailed(getPlatform(), getSuite(), getTestcase(),
                                    getStep());
    }

    @Keyword("Teststep status")
    @Parameters(min = 1, max = 1)
    public void teststepPassed(boolean flag) {
        getActiveReport().setStatus(flag, getPlatform(), getSuite(),
                                    getTestcase(), getStep());
    }
}
