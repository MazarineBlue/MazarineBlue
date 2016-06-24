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

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import org.mazarineblue.pictures.Picture;
import org.mazarineblue.test.report.Ticket;

/**
 * An object of this class contains the ticket information.
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class ManualTestTicket
        extends Ticket {

    @SuppressWarnings("PublicInnerClass")
    public class MetaData {

        private String project = "";

        private MetaData() {
        }

        public String getProject() {
            return project;
        }

        public void setProject(String project) {
            this.project = project == null ? "" : project;
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class People {

        private Person assignee;
        private Person reporter;
        private final Collection<Person> watchers = new ArrayList<>();

        private People() {
        }

        public Person getAssignee() {
            return assignee;
        }

        public void setAssignee(Person person) {
            assignee = person;
        }

        public Person getReporter() {
            return reporter;
        }

        public void setReporter(Person person) {
            reporter = person;
        }

        public Collection<Person> getWatchers() {
            return Collections.unmodifiableCollection(watchers);
        }

        public void clearLabels() {
            watchers.clear();
        }

        public void addWatcher(Person watcher) {
            if (watcher != null)
                watchers.add(watcher);
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class Dates {

        private Date created;
        private Date updated;

        private Dates() {
            created = updated = new Date();
        }

        public Date getCreated() {
            return created;
        }

        public void setCreated(Date date) {
            created = date;
        }

        public Date getUpdated() {
            return updated;
        }

        public void setUpdated(Date date) {
            updated = date;
        }
    };

    @SuppressWarnings("PublicInnerClass")
    public class Details {

        private String issueType = "Bug";
        private String priority = "";
        private String url = "";
        private String environment = "";
        private final Collection<String> browsers = new ArrayList<>();
        private final Collection<String> platforms = new ArrayList<>();
        private final Collection<String> components = new ArrayList<>();
        private final Collection<String> labels = new ArrayList<>();

        private Details() {
        }

        public String getIssueType() {
            return issueType;
        }

        public void setIssueType(String issueType) {
            this.issueType = issueType == null ? "" : issueType;
        }

        public String getPriority() {
            return priority;
        }

        public void setPriority(String priority) {
            this.priority = priority == null ? "" : priority.toLowerCase();
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url == null ? "" : url;
        }

        public String getEnvironment() {
            return environment;
        }

        public void setEnvironment(String environment) {
            this.environment = environment == null ? "" : environment;
        }

        public Collection<String> getBrowsers() {
            return Collections.unmodifiableCollection(browsers);
        }

        public void clearBrowsers() {
            browsers.clear();
        }

        public void setBrowser(String browser) {
            browsers.add(browser == null ? "" : browser);
        }

        public Collection<String> getPlatforms() {
            return Collections.unmodifiableCollection(platforms);
        }

        public void clearPlatforms() {
            platforms.clear();
        }

        public void setPlatform(String platform) {
            platforms.add(platform == null ? "" : platform);
        }

        public Collection<String> getComponents() {
            return Collections.unmodifiableCollection(components);
        }

        public void clearComponents() {
            components.clear();
        }

        public void addComponents(String label) {
            components.add(label == null ? "" : label);
        }

        public Collection<String> getLabels() {
            return Collections.unmodifiableCollection(labels);
        }

        public void clearLabels() {
            labels.clear();
        }

        public void addLabel(String label) {
            labels.add(label == null ? "" : label);
        }
    };

    @SuppressWarnings("PublicInnerClass")
    public class Description {

        private String summary = "";
        private String reproductionSteps = "";
        private String expectedResult = "";
        private String actualResult = "";
        private final Collection<Picture> screenshots = new ArrayList<>();

        private Description() {
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary == null ? "" : summary;
        }

        public String getReproductionSteps() {
            return reproductionSteps;
        }

        public void setReproductionSteps(String steps) {
            this.reproductionSteps = steps == null ? "" : steps;
        }

        public String getExpectedResult() {
            return expectedResult;
        }

        public void setExpectedResult(String expectedResult) {
            this.expectedResult = expectedResult == null ? "" : expectedResult;
        }

        public String getActualResult() {
            return actualResult;
        }

        public void setActualResult(String actualResult) {
            this.actualResult = actualResult == null ? "" : actualResult;
        }

        public Collection<Picture> getScreeshots() {
            return Collections.unmodifiableCollection(screenshots);
        }

        public void clearScreenshots() {
            screenshots.clear();
        }

        public void addScreenshot(Picture screenshot) {
            screenshots.add(screenshot);
        }
    };

    @SuppressWarnings("PublicField")
    public MetaData metadata = new MetaData();

    @SuppressWarnings("PublicField")
    public People people = new People();

    @SuppressWarnings("PublicField")
    public Dates dates = new Dates();

    @SuppressWarnings("PublicField")
    public Details details = new Details();

    @SuppressWarnings("PublicField")
    public Description description = new Description();

    private int id = 0;

    public ManualTestTicket(String suite, String testcase, String platform) {
        super(suite, testcase, platform);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    protected void writeNestedXml(OutputStream output)
            throws IOException {
        String xml = XmlReportUtil.getTicketXml(this);
        output.write(xml.getBytes());
    }
}
