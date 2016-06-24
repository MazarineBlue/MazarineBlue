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
package org.mazarineblue.test.report.util;

import java.util.Collection;
import java.util.Date;
import org.apache.commons.codec.binary.Base64;
import org.mazarineblue.pictures.Picture;
import org.mazarineblue.test.report.Report;
import org.mazarineblue.test.report.Status;
import org.mazarineblue.test.report.Suite;
import org.mazarineblue.test.report.TestObject;
import org.mazarineblue.test.report.Testcase;
import org.mazarineblue.test.report.Ticket;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class XmlReportUtil {

    private static boolean even = false;

    static public String toXml(Report report) {
        String xml = addRecordOpenTag("report", report);
        xml += metadata(report);
        xml += summary(report);
        xml += suites(report);
        xml += tickets(report);
        xml += addRecordCloseTag("report");
        return xml;
    }

    // <editor-fold defaultstate="collapsed" desc="Core functionality">
    static private String addRecord(String tag, TestObject obj) {
        String xml = addRecordOpenTag(tag, obj);
        xml += addRecordContent(tag, obj);
        xml += addRecordCloseTag(tag);
        return xml;
    }

    static private String addRecordOpenTag(String tag, TestObject obj) {
        String name = obj.getName();
        String status = obj.getStatus().name().toLowerCase();
        int total = obj.count(Testcase.class);
        int pass = obj.count(Testcase.class, Status.passed);
        int fail = obj.count(Testcase.class, Status.failed);
        int unset = obj.count(Testcase.class, Status.unset);
        String xml = add(
                "<%s name='%s' status='%s' total='%d' pass='%d' fail='%d' skipped='%d' even='%s'>",
                tag, name, status, total, pass, fail, unset,
                even ? "even" : "uneven");
        even = !even;
        return xml;
    }

    static private String addRecordContent(String tag, TestObject obj) {
        String xml = "";
        for (String platform : obj.getPlatforms()) {
            String status = obj.getStatus(platform).toString();
            int total = obj.count(Testcase.class, platform);
            int pass = obj.count(Testcase.class, platform, Status.passed);
            int fail = obj.count(Testcase.class, platform, Status.failed);
            int unset = obj.count(Testcase.class, platform, Status.unset);
            xml += add(
                    "<platform name='%s' status='%s' total='%d' pass='%d' fail='%d' skipped='%d'/>",
                    platform, status, total, pass, fail, unset);
        }
        return xml;
    }

    static private String addRecordCloseTag(String tag) {
        return add("</%s>", tag);
    }

    static private String addCollection(String groupTag, String tag,
                                        Collection<?> collection) {
        String xml = add("<%s>", groupTag);
        for (Object obj : collection)
            xml += add(tag, obj);
        xml += add("</%s>", groupTag);
        return xml;
    }

    static private String add(String format, Object... param) {
        return String.format(format, param);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="First used by toXml()">
    static private String metadata(Report report) {
        String xml = "<meta>";
        xml += String.format("<creationDate>%s</creationDate>",
                             report.getCreationDate());
        xml += "</meta>";
        return xml;
    }

    static private String summary(Report report) {
        String xml = addRecordOpenTag("summary", report);
        xml += addRecord("all", report);
        even = false;
        for (Suite suite : report.getChilderen())
            xml += addRecord("suite", suite);
        xml += addRecordCloseTag("summary");
        return xml;
    }

    static private String suites(Report report) {
        String xml = add("<suites>");
        for (Suite suite : report.getChilderen()) {
            xml += addRecordOpenTag("suite", suite);
            xml += addRecord("all", suite);
            even = false;
            for (Testcase testcase : suite.getChilderen())
                xml += addRecord("testcase", testcase);
            xml += addRecordCloseTag("suite");
        }
        xml += add("</suites>");
        return xml;
    }

    static private String tickets(Report report) {
        String body = ticketsBody(report);
        return constructTag(body, "<tickets>", "</tickets>");
    }

    static private String ticketsBody(Report report) {
        String xml = "";
        for (String suite : report.fetchSuites())
            xml += tickets(report, suite);
        return xml;
    }

    static private String constructTag(String body, String format, String close,
                                       Object... param) {
        if (body.equals(""))
            return "";
        return add(format, param) + body + close;
    }

    static private String tickets(Report report, String suite) {
        String body = ticketsBody(report, suite);
        return constructTag(body, "<suite suite='%s'>", "</suite>", suite);
    }

    static private String ticketsBody(Report report, String suite) {
        String xml = "";
        for (String testcase : report.fetchTestcases(suite))
            xml += tickets(report, suite, testcase);
        return xml;
    }

    static private String tickets(Report report, String suite, String testcase) {
        String body = ticketsBody(report, suite, testcase);
        return constructTag(body, "<testcase suite='%s' testcase='%s'>",
                            "</testcase>", suite, testcase);
    }

    static private String ticketsBody(Report report, String suite,
                                      String testcase) {
        String xml = "";
        for (Ticket ticket : report.fetchTickets(suite, testcase))
            xml += ticket.getXml();
        return xml;
    }

    static public String getTicketXml(ManualTestTicket ticket) {
        String xml = add("<ticket id='%d'>", ticket.getId());
        xml += addTicketDates(ticket.dates);
        xml += addTicketPeople(ticket.people);
        xml += addTicketMetadata(ticket);
        xml += addTicketDetails(ticket.details);
        xml += addTicketDescription(ticket.description);
        xml += add("</ticket>");
        return xml;
    }

    // <editor-fold defaultstate="collapsed" desc="First used by ticket()">
    static private String addTicketDates(ManualTestTicket.Dates dates) {
        String xml = add("<dates>");
        xml += addDate("created", dates.getCreated());
        xml += addDate("updated", dates.getUpdated());
        xml += add("</dates>");
        return xml;
    }

    static private String addTicketPeople(ManualTestTicket.People people) {
        String xml = add("<people>");
        xml += addPerson("assignee", people.getAssignee());
        xml += addPerson("reporter", people.getReporter());
        xml += addPeople("watchers", people.getWatchers());
        xml += add("</people>");
        return xml;
    }

    static private String addTicketMetadata(ManualTestTicket ticket) {
        String xml = add("<metadata>");
        xml += add("<project>%s</project>", ticket.metadata.getProject());
        xml += add("<testcase>%s</testcase>", ticket.getTestcase());
        xml += add("</metadata>");
        return xml;
    }

    static private String addTicketDetails(ManualTestTicket.Details details) {
        String xml = add("<details>");
        xml += add("<issueType>%s</issueType>", details.getIssueType());
        xml += add("<priority>%s</priority>", details.getPriority());
        xml += add("<enviroment>%s</enviroment>", details.getEnvironment());
        xml += addCollection("browsers", "<browser>%s</browser>",
                             details.getBrowsers());
        xml += addCollection("components", "<component>%s</component>",
                             details.getComponents());
        xml += addCollection("labels", "<label>%s</label>", details.getLabels());
        xml += addCollection("platforms", "<platform>%s</platform>",
                             details.getPlatforms());
        xml += add("<url>%s</url>", details.getUrl());
        xml += add("</details>");
        return xml;
    }

    static private String addTicketDescription(
            ManualTestTicket.Description description) {
        String xml = add("<description>");
        xml += add("<summary>%s</summary>", description.getSummary());
        xml += add("<reproduction>%s</reproduction>",
                   description.getReproductionSteps());
        xml += add("<actualResults>%s</actualResults>",
                   description.getActualResult());
        xml += add("<expectedResults>%s</expectedResults>",
                   description.getExpectedResult());
        xml += addScreenshots(description.getScreeshots());
        xml += add("</description>");
        return xml;
    }

    static private String addDate(String tag, Date date) {
        return add("<%s date='%s' millis='%d' />", tag, date.toString(),
                   date.getTime());
    }

    static private String addPeople(String tag, Collection<Person> persons) {
        String xml = add("<watchers>");
        for (Person person : persons)
            xml += addPerson("watcher", person);
        xml += add("</watchers>");
        return xml;
    }

    static private String addPerson(String tag, Person person) {
        return add("<%s name='%s' />", tag, person.getName());
    }

    static private String addScreenshots(Collection<Picture> screenhots) {
        String xml = add("<screenshots>");
        for (Picture screenshot : screenhots) {
            String base64 = Base64.encodeBase64String(screenshot.getData());
            xml += add(
                    "<screenshot encoding='base64'><[CDATA[%s]]></screenshot>",
                    base64);
        }
        xml += add("</screenshots>");
        return xml;
    }
    // </editor-fold>
    // </editor-fold>
}
