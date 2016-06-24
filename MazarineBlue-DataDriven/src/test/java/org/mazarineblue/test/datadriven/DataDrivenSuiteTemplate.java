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

import org.mazarineblue.datasources.DataSource;
import org.mazarineblue.test.datadriven.exceptions.IllegalMethodException;
import org.mazarineblue.test.report.Report;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
class DataDrivenSuiteTemplate
        extends DataDrivenSuite {

    @SuppressWarnings("PackageVisibleField")
    int testCount;

    @SuppressWarnings("PackageVisibleField")
    int setupClass, setupPlatform, setupMethod, setup;

    @SuppressWarnings("PackageVisibleField")
    int teardownClass, teardownPlatform, teardownMethod, teardown;

    DataDrivenSuiteTemplate(Report report)
            throws IllegalMethodException {
        super("Suite", report);
        testCount = 0;
    }

    @DataDrivenProfile
    static public void oneProfile(ValidationProfile profile) {
    }

    @DataDrivenTest
    public Boolean oneTest(DataSource source, TestContext context) {
        ++testCount;
        return null;
    }

    @DataDrivenProfile
    static public void twoProfile(ValidationProfile profile) {
    }

    @DataDrivenTest
    public Boolean twoTest(DataSource source, TestContext context) {
        ++testCount;
        return null;
    }

    @DataDrivenProfile
    static public void threeProfile(ValidationProfile profile) {
    }

    @DataDrivenTest
    public Boolean threeTest(DataSource source, TestContext context) {
        ++testCount;
        return null;
    }

    @DataDrivenProfile
    static public void passProfile(ValidationProfile profile) {
    }

    @DataDrivenTest
    public Boolean passTest(DataSource source, TestContext context) {
        ++testCount;
        return true;
    }

    @DataDrivenProfile
    static public void failProfile(ValidationProfile profile) {
    }

    @DataDrivenTest
    public Boolean failTest(DataSource source, TestContext context) {
        ++testCount;
        return false;
    }

    @Override
    protected void setupClass(TestContext context) {
        ++setupClass;
    }

    @Override
    protected void setupPlatform(TestContext context) {
        ++setupPlatform;
    }

    @Override
    protected void setupMethod(TestContext context) {
        ++setupMethod;
    }

    @Override
    protected void setup(DataSource source, TestContext context) {
        ++setup;
    }

    @Override
    protected void teardown(DataSource source, TestContext context) {
        ++teardown;
    }

    @Override
    protected void teardownMethod(TestContext context) {
        ++teardownMethod;
    }

    @Override
    protected void teardownPlatform(TestContext context) {
        ++teardownPlatform;
    }

    @Override
    protected void teardownClass(TestContext context) {
        ++teardownClass;
    }
}
