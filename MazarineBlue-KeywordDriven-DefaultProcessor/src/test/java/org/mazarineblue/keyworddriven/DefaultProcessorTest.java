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
package org.mazarineblue.keyworddriven;

import org.mazarineblue.keyworddriven.util.old.TestFeedBuilder;
import org.mazarineblue.keyworddriven.util.old.TestLogBuilder;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.LibraryTestTemplate;
import org.mazarineblue.keyworddriven.exceptions.ToFewParametersException;
import org.mazarineblue.keyworddriven.feeds.FeedBuilder;
import org.mazarineblue.keyworddriven.logs.LogBuilder;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class DefaultProcessorTest
        extends LibraryTestTemplate {

    @Before
    public void setup() {
        FeedBuilder feedBuilder = new TestFeedBuilder();
        LogBuilder logBuilder = new TestLogBuilder();
        Processor processor = new DefaultProcessor(feedBuilder, logBuilder);
        librarySetup(processor);
    }

    @Test
    public void executeShouldDoNothing() {
        execute();
        output.assertLines(0);
        output.assertExecuted(0);
        output.assertValidated(0);
        output.assertInfo(0);
        output.assertWarning(0);
        output.assertError(0);
    }

    @Test
    public void validateShouldDoNothing() {
        validate();
        output.assertLines(0);
        output.assertExecuted(0);
        output.assertValidated(0);
        output.assertInfo(0);
        output.assertWarning(0);
        output.assertError(0);
    }

    @Test
    public void executeShouldPass() {
        input.addInstruction("", "Info", "message");
        execute();
        output.assertLines(1);
        output.assertExecuted(1);
        output.assertValidated(0);
        output.assertInfo(1);
        output.assertWarning(0);
        output.assertError(0);
    }

    @Test
    public void validateShouldPass() {
        input.addInstruction("", "Info", "message");
        validate();
        validate();
        output.assertLines(1);
        output.assertExecuted(0);
        output.assertValidated(1);
        output.assertInfo(0);
        output.assertWarning(0);
        output.assertError(0);
    }

    @Test
    public void executeShouldPassButGiveWarning() {
        input.addInstruction("", "Warning", "message");
        execute();
        output.assertLines(1);
        output.assertExecuted(1);
        output.assertValidated(0);
        output.assertInfo(0);
        output.assertWarning(1);
        output.assertError(0);
    }

    @Test
    public void validateShouldPassButGiveWarning() {
        input.addInstruction("", "Warning", "message");
        validate();
        output.assertLines(1);
        output.assertExecuted(0);
        output.assertValidated(1);
        output.assertInfo(0);
        output.assertWarning(0);
        output.assertError(0);
    }

    @Test(expected = ToFewParametersException.class)
    public void executeShouldFail() {
        input.addInstruction("", "Error");
        execute();
        output.assertLines(1);
        output.assertExecuted(0);
        output.assertValidated(0);
        output.assertInfo(0);
        output.assertWarning(0);
        output.assertError(1);
    }

    @Test
    public void validateShouldFail() {
        input.addInstruction("", "Error");
        validate();
        output.assertLines(1);
        output.assertExecuted(0);
        output.assertValidated(1);
        output.assertInfo(0);
        output.assertWarning(0);
        output.assertError(1);
    }
}
