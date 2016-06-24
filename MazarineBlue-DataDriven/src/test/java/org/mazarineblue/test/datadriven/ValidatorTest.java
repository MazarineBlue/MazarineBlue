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
import java.util.Set;
import java.util.logging.Level;
import java.util.stream.Collectors;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mazarineblue.datasources.DataSource;
import org.mazarineblue.datasources.ObjectArraySource;
import org.mazarineblue.test.datadriven.validators.NotEmptyValidator;
import org.mazarineblue.test.datadriven.validators.PatternValidator;
import org.mazarineblue.test.datadriven.validators.UrlValidator;
import org.mazarineblue.test.datadriven.validators.Validator;
import org.mazarineblue.test.report.Report;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class ValidatorTest {

    static private Validator notEmpty, numbersOrLetters, isUrl;
    static private Report report;
    static private DataSource source;
    private DataDrivenSuite suite;
    private List<ValidationMessage> conflicts;

    @BeforeClass
    public static void setupClass() {
        notEmpty = new NotEmptyValidator(Level.SEVERE);
        numbersOrLetters = new PatternValidator(Level.SEVERE, "^[0-9A-Za-z]+$");
        isUrl = new UrlValidator(Level.SEVERE);

        String[] platforms = {"Win7/Firfox"};
        report = new Report("Title", platforms);
        source = new ObjectArraySource("test", true) {
            {
                setData("null", null);
                setData("empty", "");
                setData("numbers", "123456789");
                setData("letters", "abcdefghijklmnopqrstuvwxyz");
                setData("url", "http://www.specialisterren.nl");
                setData("Lorem Ipsum",
                        "Neque porro quisquam est qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit...");
            }
        };
        Set<String> columns = source.getColumns();
        ValidatorTemplate.columns = columns.toArray(new String[columns.size()]);
    }

    @AfterClass
    public static void teardownClass() {
        notEmpty = numbersOrLetters = isUrl = null;
        ValidatorTemplate.validator = null;
        ValidatorTemplate.columns = null;

        report = null;
        source = null;
    }

    @Before
    public void setup() {
        conflicts = new ArrayList();
    }

    @After
    public void teardown() {
        conflicts = null;
        suite = null;
    }

    @Test
    public void testZeroValidators()
            throws Exception {
        ValidatorTemplate.validator = null;
        suite = new ValidatorTemplate(report);
        suite.validateSource(source, conflicts);
        Assert.assertEquals(0, conflicts.size());
    }

    @Test
    public void testNotEmptyValidator()
            throws Exception {
        ValidatorTemplate.validator = notEmpty;
        suite = new ValidatorTemplate(report);
        suite.validateSource(source, conflicts);
        Assert.assertEquals(2, conflicts.size());
    }

    @Test
    public void testPatternValidator()
            throws Exception {
        ValidatorTemplate.validator = numbersOrLetters;
        suite = new ValidatorTemplate(report);
        suite.validateSource(source, conflicts);
        Assert.assertEquals(4, conflicts.size());
    }

    @Test
    public void testUrlValidator()
            throws Exception {
        ValidatorTemplate.validator = isUrl;
        suite = new ValidatorTemplate(report);
        suite.validateSource(source, conflicts);
        Assert.assertEquals(5, conflicts.size());
    }
}
