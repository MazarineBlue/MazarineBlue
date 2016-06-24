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
package org.mazarineblue.keyworddriven.librarymanager;

import java.util.Date;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mazarineblue.LibraryTestTemplate;
import org.mazarineblue.events.SetStatusEvent;
import org.mazarineblue.keyworddriven.DefaultProcessor;
import org.mazarineblue.keyworddriven.util.old.TestFeedBuilder;
import org.mazarineblue.keyworddriven.util.old.TestLogBuilder;
import org.mazarineblue.keyworddriven.exceptions.ProcedureOpenOnSheetEndException;
import org.mazarineblue.keyworddriven.feeds.FeedBuilder;
import org.mazarineblue.keyworddriven.logs.LogBuilder;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class DefaultLibraryTest
        extends LibraryTestTemplate {

    @Before
    public void setup() {
        FeedBuilder feedBuilder = new TestFeedBuilder();
        LogBuilder logBuilder = new TestLogBuilder();
        DefaultProcessor processor = new DefaultProcessor(feedBuilder, logBuilder);
        librarySetup(processor);
    }
    
    @Test
    public void testEcho()
            throws Exception {
        input.addInstruction("", "Echo", "command");
        execute();
        output.assertSystemOut("command");
        output.assertLines(1);
        output.assertExecuted(1);
        output.assertValidated(0);
        output.assertInfo(0);
        output.assertWarning(0);
        output.assertError(0);
    }

    @Test
    public void testInfo()
            throws Exception {
        input.addInstruction("", "Info", "command");
        execute();
        output.assertSystemOut("");
        output.assertLines(1);
        output.assertExecuted(1);
        output.assertValidated(0);
        output.assertInfo(1);
        output.assertWarning(0);
        output.assertError(0);
    }

    @Test
    public void testSleep()
            throws Exception {
        long sleep = 500;
        long start = new Date().getTime();
        input.addInstruction("", "Sleep", "" + sleep);
        execute();
        long end = new Date().getTime();
        long time = end - start;
        if (time < sleep)
            Assert.fail("Time must be below " + sleep);
    }

    @Ignore
    @Test
    public void testSetDelay()
            throws Exception {
    }

    @Ignore
    @Test
    public void testImport()
            throws Exception {
    }

    @Ignore
    @Test
    public void testCallSheet()
            throws Exception {
    }

    @Ignore
    @Test
    public void testImportSheet()
            throws Exception {
        input.addInstruction("", "Import", "com.specialisterren");
        input.addInstruction("", "Echo", "$variable");
        execute();
        output.assertSystemOut("123 456");
    }

    @Test
    public void testOpenProcedure()
            throws Exception {
        expectCause(ProcedureOpenOnSheetEndException.class);
        input.addInstruction("", "Procedure", "foo", "arg1", "arg2");
        execute();
        chain.assertLinkCount(1);
    }

    @Test
    public void testDefineProcedure()
            throws Exception {
        input.addInstruction("", "Procedure", "foo", "arg1", "arg2");
        input.addInstruction("", "End procedure");
        execute();
        chain.assertLinkCount(0);
    }

    @Test
    public void testCallProcedure()
            throws Exception {
        input.addInstruction("", "Procedure", "foo", "arg1", "arg2");
        input.addInstruction("", "Echo", "$arg1 $arg2");
        input.addInstruction("", "End procedure");
        input.addInstruction("", "foo", "123", "456");
        execute();
        output.assertSystemOut("123 456");
    }

    @Ignore
    @Test
    public void testDeclareGlobalVariable()
            throws Exception {
    }

    @Ignore
    @Test
    public void testDeclareSheetVariable()
            throws Exception {
    }

    @Ignore
    @Test
    public void testDeclareLocalVariable()
            throws Exception {
    }

    @Ignore
    @Test
    public void testSet()
            throws Exception {
    }

    @Test
    public void testSubstring()
            throws Exception {
        input.addInstruction("", "Substring", "123 bla 456", "variable",
                             "([0-9]+)+[^0-9]*([0-9]+)", "\\$1 \\$2");
        input.addInstruction("", "Echo", "$variable");
        execute();
        output.assertSystemOut("123 456");
    }

    @Test
    public void testValidateConditionShouldPass()
            throws Exception {
        input.addInstruction("", "Validate condition",
                             "Zelfde tekst == Zelfde tekst");
        execute();
        List<SetStatusEvent> list = events.fetchFiredEvents(SetStatusEvent.class);
        output.assertError(0);
        Assert.assertEquals(1, list.size());
        for (SetStatusEvent event : list)
            Assert.assertEquals(true, event.passed());
    }

    @Test
    public void testValidateConditionShouldFail()
            throws Exception {
        input.addInstruction("", "Validate condition",
                             "Zelfde tekst != Zelfde tekst");
        execute();
        output.assertError(0);
        List<SetStatusEvent> list = events.fetchFiredEvents(SetStatusEvent.class);
        Assert.assertEquals(1, list.size());
        for (SetStatusEvent event : list)
            Assert.assertEquals(false, event.passed());
    }

    // <editor-fold desc="Flow instructions">
    @Test
    public void testIf()
            throws Exception {
        input.addInstruction("", "If", "A == A");
        input.addInstruction("", "Info", "A should be the same as A");
        input.addInstruction("", "Else");
        input.addInstruction("", "Error", "A should be the same as A");
        input.addInstruction("", "Endif");
        execute();
        output.assertInfo(1);
        output.assertWarning(0);
        output.assertError(0);
    }

    @Test
    public void testIfElse()
            throws Exception {
        input.addInstruction("", "If", "A == B");
        input.addInstruction("", "Error", "A should not be the same as B");
        input.addInstruction("", "Else");
        input.addInstruction("", "Info", "A should not be the same as B");
        input.addInstruction("", "Endif");
        execute();
        output.assertInfo(1);
        output.assertWarning(0);
        output.assertError(0);
    }

    @Test
    public void testIfElseif()
            throws Exception {
        input.addInstruction("", "If", "A == B");
        input.addInstruction("", "Error", "A should not be the same as B");
        input.addInstruction("", "Elseif", "A == A");
        input.addInstruction("", "Info", "A should not be the same as B");
        input.addInstruction("", "Else");
        input.addInstruction("", "Error", "A should be the same as B");
        input.addInstruction("", "Endif");
        execute();
        output.assertInfo(1);
        output.assertWarning(0);
        output.assertError(0);
    }

    @Test
    public void testSwitchFallThough()
            throws Exception {
        input.addInstruction("", "Switch", "A");
        input.addInstruction("", "Case", "A");
        input.addInstruction("", "Case", "B");
        input.addInstruction("", "Info", "PASS");
        input.addInstruction("", "End switch");
        execute();
        output.assertInfo(1);
        output.assertWarning(0);
        output.assertError(0);
    }

    @Test
    public void testSwitchBreak()
            throws Exception {
        input.addInstruction("", "Switch", "A");
        input.addInstruction("", "Case", "A");
        input.addInstruction("", "Info", "PASS");
        input.addInstruction("", "Break");
        input.addInstruction("", "Error", "FAIL");
        input.addInstruction("", "Case", "B");
        input.addInstruction("", "Error", "FAIL");
        input.addInstruction("", "End switch");
        execute();
        output.assertInfo(1);
        output.assertWarning(0);
        output.assertError(0);
    }

    @Test
    public void testSwitchDefault()
            throws Exception {
        input.addInstruction("", "Switch", "B");
        input.addInstruction("", "Case", "A");
        input.addInstruction("", "Error", "FAIL");
        input.addInstruction("", "Break");
        input.addInstruction("", "Default case");
        input.addInstruction("", "Info", "PASS");
        input.addInstruction("", "End switch");
        execute();
        output.assertInfo(1);
        output.assertWarning(0);
        output.assertError(0);
    }

    @Test
    public void testDo()
            throws Exception {
        input.addInstruction("", "Set", "counter", "0");
        input.addInstruction("", "Do");
        input.addInstruction("", "Set condition", "counter", "$counter + 1");
        input.addInstruction("", "Info", "PASS");
        input.addInstruction("", "End do", "$counter < 2");
        execute();
        output.assertInfo(2);
        output.assertWarning(0);
        output.assertError(0);
    }

    @Test
    public void testDoBreak()
            throws Exception {
        input.addInstruction("", "Set", "counter", "0");
        input.addInstruction("", "Do");
        input.addInstruction("", "Set condition", "counter", "$counter + 1");
        input.addInstruction("", "Info", "PASS");
        input.addInstruction("", "Break");
        input.addInstruction("", "Error", "FAIL");
        input.addInstruction("", "End do", "$counter < 2");
        execute();
        output.assertInfo(1);
        output.assertWarning(0);
        output.assertError(0);
    }

    @Test
    public void testWhile()
            throws Exception {
        input.addInstruction("", "Set", "counter", "0");
        input.addInstruction("", "While", "$counter < 2");
        input.addInstruction("", "Set condition", "counter", "$counter + 1");
        input.addInstruction("", "Info", "PASS");
        input.addInstruction("", "End while");
        execute();
        output.assertInfo(2);
        output.assertWarning(0);
        output.assertError(0);
    }

    @Test
    public void testWhileBreak()
            throws Exception {
        input.addInstruction("", "Set", "counter", "0");
        input.addInstruction("", "While", "$counter < 2");
        input.addInstruction("", "Set condition", "counter", "$counter + 1");
        input.addInstruction("", "Info", "PASS");
        input.addInstruction("", "Break");
        input.addInstruction("", "Error", "FAIL");
        input.addInstruction("", "End while");
        execute();
        output.assertInfo(1);
        output.assertWarning(0);
        output.assertError(0);
    }

    @Test
    public void testFor()
            throws Exception {
        input.addInstruction("", "Set", "counter", "0");
        input.addInstruction("", "For", "counter", "0", "$counter < 2", "$counter + 1");
        input.addInstruction("", "Info", "PASS");
        input.addInstruction("", "End for");
        execute();
        output.assertInfo(2);
        output.assertWarning(0);
        output.assertError(0);
    }

    @Test
    public void testForBreak()
            throws Exception {
        input.addInstruction("", "Set", "counter", "0");
        input.addInstruction("", "For", "counter", "0", "$counter < 2", "$counter + 1");
        input.addInstruction("", "Info", "PASS");
        input.addInstruction("", "Break");
        input.addInstruction("", "End for");
        execute();
        output.assertInfo(1);
        output.assertWarning(0);
        output.assertError(0);
    }
    // </editor-fold>
}
