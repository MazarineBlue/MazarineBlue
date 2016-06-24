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

import java.util.ArrayList;
import org.mazarineblue.test.report.util.XmlReportUtil;
import org.mazarineblue.test.report.visitors.TestObjectVisitor;
import org.mazarineblue.test.report.visitors.TestObjectVisitorException;
import org.mazarineblue.util.XmlUtil;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class Report
        extends TestObject<Suite> {

    private Date creationDate = new Date();

    public Report(String title, String[] platforms) {
        super(title, null, factory.get(platforms));
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Makes sure the suite and testcase exists. Call this method when testcases
     * might not be executed, but still need to be reported.
     *
     * @param platform - reserved parameter
     * @param suite - the name of the suite containing the referenced testcase.
     * @param testcase - the name of testcase to add.
     */
    public void declareTestcase(String platform, String suite, String testcase) {
        checkPlatform(factory.get(platform));
        getTestcase(suite, testcase);
    }

    /**
     * Adds an ticket to this report.
     *
     * @param ticket - ticket to add to the referenced testcase.
     */
    @Override
    public void addTicket(Ticket ticket) {
        super.addTicket(ticket);
    }

    /**
     * Adds an ticket to a suite.
     *
     * @param ticket - ticket to add to the referenced testcase.
     * @param suite - the name of the suite containing the referenced testcase.
     */
    public void addTicket(Ticket ticket, String suite) {
        Suite s = fetchSuite(suite);
        s.addTicket(ticket);
    }

    /**
     * Adds an ticket to a testcase.
     *
     * @param ticket - ticket to add to the referenced testcase.
     * @param suite - the name of the suite containing the referenced testcase.
     * @param testcase - the name of testcase to which the ticket should be
     * added.
     */
    public void addTicket(Ticket ticket, String suite, String testcase) {
        Testcase c = getTestcase(suite, testcase);
        c.addTicket(ticket);
    }

    /**
     * Adds an ticket to a testcase.
     *
     * @param ticket - ticket to add to the referenced testcase.
     * @param suite - the name of the suite containing the referenced testcase.
     * @param testcase - the name of testcase to which the ticket should be
     * added.
     * @param step - the name of the step to which the ticket should be added.
     */
    public void addTicket(Ticket ticket, String suite, String testcase,
                          String step) {
        Teststep s = getTeststep(suite, testcase, step);
        s.addTicket(ticket);
    }

    /**
     * Fetches tickets that where add to the specified testcase or its
     * childeren.
     *
     * @return an collection of all tickets requisted.
     */
    @Override
    public Collection<Ticket> fetchTickets() {
        return super.fetchTickets();
    }

    /**
     * Fetches tickets that where add to the specified testcase or its
     * childeren.
     *
     * @param suite - the name of the suite containing the referenced testcase.
     * added.
     * @return an collection of all tickets requisted.
     */
    public Collection<Ticket> fetchTickets(String suite) {
        Suite s = fetchSuite(suite);
        return s.fetchTickets();
    }

    /**
     * Fetches tickets that where add to the specified testcase or its
     * childeren.
     *
     * @param suite - the name of the suite containing the referenced testcase.
     * @param testcase - the name of testcase to which the ticket should be
     * added.
     * @return an collection of all tickets requisted.
     */
    public Collection<Ticket> fetchTickets(String suite, String testcase) {
        Testcase c = getTestcase(suite, testcase);
        return c.fetchTickets();
    }

    /**
     * Fetches tickets that where add to the specified step.
     *
     * @param suite - the name of the suite containing the referenced testcase.
     * @param testcase - the name of testcase to which the ticket should be
     * added.
     * @param step - the name of the step to which the ticket should be added.
     * @return an collection of all tickets requisted.
     */
    public Collection<Ticket> fetchTickets(String suite, String testcase,
                                           String step) {
        Teststep s = getTeststep(suite, testcase, step);
        return s.fetchTickets();
    }

    public void setStatus(boolean passed, String platform, String suite,
                          String testcase) {
        if (passed)
            setPassed(platform, suite, testcase);
        else
            setFailed(platform, suite, testcase);
    }

    public void setStatus(boolean passed, String platform, String suite,
                          String testcase, String step) {
        if (passed)
            setPassed(platform, suite, testcase, step);
        else
            setFailed(platform, suite, testcase, step);
    }

    @Override
    public Status getStatus(String platform) {
        if (platform != null)
            checkPlatform(factory.get(platform));
        return platform == null ? getStatus() : super.getStatus(platform);
    }

    public Status getStatus(String platform, String suite) {
        if (platform != null)
            checkPlatform(factory.get(platform));
        Suite s = fetchSuite(suite);
        return platform == null ? s.getStatus() : s.getStatus(platform);
    }

    public Status getStatus(String platform, String suite, String testcase) {
        if (platform != null)
            checkPlatform(factory.get(platform));
        Testcase c = getTestcase(suite, testcase);
        return platform == null ? c.getStatus() : c.getStatus(platform);
    }

    public Status getStatus(String platform, String suite, String testcase,
                            String step) {
        if (platform != null)
            checkPlatform(factory.get(platform));
        Teststep line = getTeststep(suite, testcase, step);
        return platform == null ? line.getStatus() : line.getStatus(platform);
    }

    /**
     * Sets the status of the referenced testcase to passed. Calling
     * {@link #setFailed(java.lang.String, java.lang.String, java.lang.String)}
     * overrides this call.
     *
     * @param platform - for wich the test passed
     * @param suite - the name of the suite containing the referenced testcase
     * @param testcase - the name of the testcase to set the status of to
     * passed. @see setFailed
     */
    public void setPassed(String platform, String suite, String testcase) {
        checkPlatform(factory.get(platform));
        Testcase c = getTestcase(suite, testcase);
        c.setPassed(platform);
    }

    /**
     * Sets the status of the referenced testcase to failed.
     *
     * @param platform - for wich the test failed
     * @param suite - the name of the suite containing the referenced testcase.
     * @param testcase - the name of the testcase to set the status of to
     * failed.
     */
    public void setFailed(String platform, String suite, String testcase) {
        checkPlatform(factory.get(platform));
        Testcase c = getTestcase(suite, testcase);
        c.setFailed(platform);
    }

    /**
     * Sets the status of the referenced line and testcase to passed.
     *
     * Calling {@link #setFailed(String, String, String)} overrides this call.
     *
     * @param platform - for wich the teststep passed
     * @param suite - the name of the suite containing the referenced testcase.
     * @param testcase - the name of the testcase to set the status of to
     * passed.
     * @param step - the name of the teststep
     * @see setFailed
     */
    public void setPassed(String platform, String suite, String testcase,
                          String step) {
        checkPlatform(factory.get(platform));
        Teststep line = getTeststep(suite, testcase, step);
        line.setPassed(platform);
    }

    /**
     * Sets the status of the referenced line and testcase to failed.
     *
     * @param platform - for wich the teststep failed
     * @param suite - the name of the suite containing the referenced testcase.
     * @param testcase - the name of the testcase to set the status of to
     * passed.
     * @param step - the name of the teststep
     * @see setFailed
     */
    public void setFailed(String platform, String suite, String testcase,
                          String step) {
        checkPlatform(factory.get(platform));
        Teststep line = getTeststep(suite, testcase, step);
        line.setFailed(platform);
    }

    private Teststep getTeststep(String suite, String testcase, String step) {
        Testcase c = getTestcase(suite, testcase);
        Teststep line = c.getStep(step);
        return line;
    }

    private Testcase getTestcase(String suite, String testcase) {
        Suite s = fetchSuite(suite);
        Testcase c = s.getCase(testcase);
        return c;
    }

    public Suite fetchSuite(String name) {
        Suite obj = getTestObject(name);
        if (obj == null)
            addTestObject(name, obj = new Suite(name, this, platforms));
        return obj;
    }

    public Collection<String> fetchSuites() {
        List<String> list = new ArrayList();
        for (Suite s : getChilderen())
            list.add(s.name);
        return list;
    }

    public Collection<String> fetchTestcases() {
        return fetchTestcases(null);
    }

    public Collection<String> fetchTestcases(String suite) {
        List<String> list = new ArrayList();
        for (Suite s : getChilderen())
            if (suite == null || s.name.equals(suite))
                for (Testcase t : s.getChilderen())
                    list.add(t.name);
        return list;
    }

    public Collection<String> fetchTeststeps() {
        return fetchTeststeps(null, null);
    }

    public Collection<String> fetchTeststeps(String suite) {
        return fetchTeststeps(suite, null);
    }

    public Collection<String> fetchTeststeps(String suite, String testcase) {
        List<String> list = new ArrayList();
        for (Suite s : getChilderen())
            if (suite == null || s.name.equals(suite))
                for (Testcase t : s.getChilderen())
                    if (testcase == null || t.name.equals(suite))
                        for (Teststep x : t.getChilderen())
                            list.add(x.name);
        return list;
    }

    @Override
    public void accept(TestObjectVisitor visitor)
            throws TestObjectVisitorException {
        visitor.openReport(this);
        super.accept(visitor);
        visitor.closeReport(this);
    }

    public String toXml() {
        String input = XmlUtil.getVersion();
        input += XmlUtil.getStylesheet("report.xsl");
        return input + XmlReportUtil.toXml(this);
    }
}
