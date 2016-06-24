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
package org.mazarineblue.test.datadriven;

import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mazarineblue.datasources.DataSource;
import org.mazarineblue.datasources.ObjectArraySource;
import org.mazarineblue.test.datadriven.exceptions.PlatformsNotFoundException;
import org.mazarineblue.test.report.Report;
import org.mazarineblue.test.report.Status;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class DataDrivenSuiteTest {

    private static final String platform = "Win7/Firfox";
    static private DataSource source;
    private Report report;
    private DataDrivenSuiteTemplate suite;
    private List<ValidationMessage> conflicts;

    @BeforeClass
    public static void setupClass() {
        source = new ObjectArraySource("test", true) {
            {
                setData("null", null);
                setData("empty", "");
                setData("numbers", "123456789");
                setData("letters", "abcdefghijklmnopqrstuvwxyz");
                setData("url", "http://www.specialisterren.nl");
                setData("Lorem Ipsum",
                        "Neque porro quisquam est qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit...");
                reset();
            }
        };
    }

    @AfterClass
    public static void teardownClass() {
        source = null;
    }

    @Before
    public void setup()
            throws Exception {
        String[] platforms = {platform};
        report = new Report("Title", platforms);
        conflicts = new ArrayList();
        suite = new DataDrivenSuiteTemplate(report);
    }

    @After
    public void teardown() {
        conflicts = null;
        suite = null;
        report = null;
    }

    @Test
    public void testTest()
            throws PlatformsNotFoundException {
        Assert.assertEquals(0, suite.testCount);
        Assert.assertEquals(5, suite.getTestCount());
        suite.test(source, new TestContextMediator());
        Assert.assertEquals(5, suite.testCount);
        Assert.assertEquals(5, suite.getTestCount());
        Assert.assertEquals(1, suite.setupClass);
        Assert.assertEquals(1, suite.setupPlatform);
        Assert.assertEquals(5, suite.setupMethod);
        Assert.assertEquals(5, suite.setup);
        Assert.assertEquals(1, suite.teardownClass);
        Assert.assertEquals(1, suite.teardownPlatform);
        Assert.assertEquals(5, suite.teardownMethod);
        Assert.assertEquals(5, suite.teardown);
        Assert.assertEquals(Status.failed, report.getStatus(platform));
    }

    @Test
    public void testReportStatusWithoutStep() {
        TestContext context = new TestContext("Suite");
        context.setPlatform(platform);
        context.setTestcase("TC1");
        suite.reportStatus(true, context);
        Assert.assertEquals(Status.passed, report.getStatus(platform));
        Assert.assertEquals(Status.passed, report.getStatus(platform, "Suite",
                                                            "TC1"));
    }

    @Test
    public void testReportStatusWithStep() {
        TestContext context = new TestContext("Suite");
        context.setPlatform(platform);
        context.setTestcase("TC1");
        context.setTeststep("A");
        suite.reportStatus(true, context);
        Assert.assertEquals(Status.passed, report.getStatus(platform));
        Assert.assertEquals(Status.passed, report.getStatus(platform, "Suite",
                                                            "TC1", "A"));
    }
}
