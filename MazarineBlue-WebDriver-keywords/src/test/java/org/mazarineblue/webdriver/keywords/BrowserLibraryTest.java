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
package org.mazarineblue.webdriver.keywords;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mazarineblue.LibraryTestTemplate;
import org.mazarineblue.datasources.ObjectArraySource;
import org.mazarineblue.keyworddriven.exceptions.InterpreterAlReadyRunningException;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class BrowserLibraryTest
        extends LibraryTestTemplate {

    public BrowserLibraryTest() {
    }

    @Before
    public void setup() {
        ObjectArraySource source = new ObjectArraySource("Sheet.identifier");
        source.setData("Element.type", "id");
        source.setData("Element.key", "_eEe");
        source.setData("NotAvailable.type", "id");
        source.setData("NotAvailable.key", "qwerty!@#$%12345");
        input.putSource("Sheet.config", source);

        input.addInstruction("", "Import", "org.mazarineblue.datasources");
        input.addInstruction("", "Data sheet", "Source.name", "array",
                             "Sheet.config");
        input.addInstruction("", "Select source", "Source.name");
        execute();

        input.addInstruction("", "Import", "org.mazarineblue.test.report");
        input.addInstruction("", "Report", "Report Naam", "Report Title",
                             "Firefox");
        input.addInstruction("", "Select report", "Report Naam");
        input.addInstruction("", "Platform", "Firefox");
        input.addInstruction("", "Suite", "my suite");
        input.addInstruction("", "Testcase", "my testcase");
        input.addInstruction("", "Teststep", "my teststep");
        execute();

        input.addInstruction("", "Import", "org.mazarineblue.webdriver");
        input.addInstruction("", "Open browser", "Main browser", "firefox");
        input.addInstruction("", "Select browser", "Main browser");
        input.addInstruction("", "Load URL", "http://www.google.com/");
        execute();
    }

    @After
    public void teardown() {
        input.addInstruction("", "Close browser", "Main browser");
        execute();
    }

    @Ignore("Needs more attention")
    @Test
    public void testValidateElementExists() {
        input.addInstruction("", "Validate element exists", "Element");
        execute();
        output.assertError(0);
        // @todo assert the event
    }

    @Ignore("Unreliable test")
    @Test
    public void testValidateMatchingScreenshot() {
        input.addInstruction("", "Validate screenshot",
                             "Screenshots\\\\matching.png",
                             "500", "500");
        execute();
        output.assertError(0);
        // @todo assert the event
    }

    @Ignore("Unreliable test")
    @Test
    public void testValidateNonMatchingScreenshot() {
        input.addInstruction("", "Validate screenshot",
                             "Screenshots\\\\non-matching.png", "100", "100");
        execute();
        output.assertError(1);
        // @todo assert the event
    }

    @Ignore("Unreliable test")
    @Test(expected = InterpreterAlReadyRunningException.class)
    public void testValidateScreenshotWithoutAFile() {
        input.addInstruction("", "Validate screenshot",
                             "Screenshots\\\\file_not_found.png", "500", "500");
        execute();
    }

    @Ignore("Unreliable test")
    @Test(expected = InterpreterAlReadyRunningException.class)
    public void testValidateScreenshotWithZeroWidth() {
        input.addInstruction("", "Validate screenshot",
                             "Screenshots\\\\matching.png",
                             "0", "500");
        execute();
    }

    @Ignore("Unreliable test")
    @Test(expected = InterpreterAlReadyRunningException.class)
    public void testValidateScreenshotWithZeroHeight() {
        input.addInstruction("", "Validate screenshot",
                             "Screenshots\\\\matching.png",
                             "500", "0");
        execute();
    }

    @Test
    @Ignore
    public void testValidateScreenshotWithSmallerSize() {
        input.addInstruction("", "Validate screenshot",
                             "Screenshots\\\\matching.jpg",
                             "10", "10");
        execute();
        output.assertError(0);
    }

    @Test
    @Ignore
    public void testValidateScreenshotWithLargerSize() {
        input.addInstruction("", "Validate screenshot",
                             "Screenshots\\\\matching.jpg",
                             "2000", "2000");
        execute();
        output.assertError(0);
    }
}
