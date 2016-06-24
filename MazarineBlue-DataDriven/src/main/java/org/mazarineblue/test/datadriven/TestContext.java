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

import java.lang.reflect.Method;
import org.mazarineblue.datasources.ObjectArraySource;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class TestContext {

    private final String suite;
    private String platform = "", testcase = "", teststep = null;
    private Method test = null;

    TestContext(String suite) {
        this.suite = suite;
    }

    @Override
    public String toString() {
        return testcase + ", " + platform;
    }

    public ObjectArraySource getAsDataSource() {
        ObjectArraySource source = new ObjectArraySource("Context", true);
        source.setData("Platform", platform);
        source.setData("Suite", suite);
        source.setData("Testcase", testcase);
        source.setData("Teststep", teststep);
        source.setData("", platform);
        return source;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getPlatform() {
        return platform;
    }

    // @TODO remove public
    public void teardownAfterPlatform() {
        platform = null;
    }

    void setTestMethod(Method test) {
        this.test = test;
    }

    public Method getTestMethod() {
        return test;
    }

    void teardownAfterMethod() {
        test = null;
    }

    public void setTestcase(String testcase) {
        this.testcase = testcase;
    }

    public String getTestcase() {
        return testcase;
    }

    void teardownAfterTestcase() {
        testcase = null;
    }

    public void setTeststep(String step) {
        this.teststep = step;
    }

    String getTeststep() {
        return teststep;
    }

    void teardownAfterLine() {
        teststep = null;
    }
}
