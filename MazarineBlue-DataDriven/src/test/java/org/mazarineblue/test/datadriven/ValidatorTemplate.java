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
import org.mazarineblue.test.datadriven.validators.Validator;
import org.mazarineblue.test.report.Report;

class ValidatorTemplate
        extends DataDrivenSuite {

    @SuppressWarnings("PackageVisibleField")
    static Validator validator;

    @SuppressWarnings("PackageVisibleField")
    static String[] columns;

    ValidatorTemplate(Report report)
            throws IllegalMethodException {
        super("Suite", report);
    }

    @DataDrivenProfile
    static public void simpleProfile(ValidationProfile profile) {
        if (validator == null)
            return;
        for (String column : columns)
            profile.addValidation(column, validator);
    }

    @DataDrivenTest
    public Boolean simpleTest(DataSource source, TestContext context) {
        return null;
    }

    @Override
    protected void setupClass(TestContext context) {
    }

    @Override
    protected void setupPlatform(TestContext context) {
    }

    @Override
    protected void setupMethod(TestContext context) {
    }

    @Override
    protected void setup(DataSource source, TestContext context) {
    }

    @Override
    protected void teardown(DataSource source, TestContext context) {
    }

    @Override
    protected void teardownMethod(TestContext context) {
    }

    @Override
    protected void teardownPlatform(TestContext context) {
    }

    @Override
    protected void teardownClass(TestContext context) {
    }
}
