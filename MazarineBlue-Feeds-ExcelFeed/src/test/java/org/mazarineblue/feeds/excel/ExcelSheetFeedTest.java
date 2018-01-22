/*
 * Copyright (c) 2016 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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
package org.mazarineblue.feeds.excel;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.eventdriven.Feed;
import org.mazarineblue.eventdriven.exceptions.NoEventsLeftException;
import org.mazarineblue.feeds.excel.exceptions.KeywordMustBeTextException;
import org.mazarineblue.feeds.excel.util.TestExecuteInstructionLineEvent;
import org.mazarineblue.feeds.excel.util.WorkbookBuilder;
import org.mazarineblue.keyworddriven.events.CommentInstructionLineEvent;
import org.mazarineblue.keyworddriven.events.ExecuteInstructionLineEvent;
import org.mazarineblue.keyworddriven.events.ValidateInstructionLineEvent;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class ExcelSheetFeedTest {

    private WorkbookBuilder workbook;
    private Feed feed;

    @Before
    public void setup() {
        workbook = new WorkbookBuilder();
        feed = workbook.getFeed();
    }

    @After
    public void teardown() {
        workbook = null;
        feed = null;
    }

    @Test
    public void hasNext_EmptySheet_ReturnsFalse() {
        assertFalse(feed.hasNext());
    }

    @Test(expected = NoEventsLeftException.class)
    public void next_EmptySheet_ThrowsException() {
        feed.next();
    }

    @Test
    public void hasNext_NullKeyword_ReturnFalse() {
        workbook.addRow(null);
        assertFalse(feed.hasNext());
    }

    @Test
    public void hasNext_BooleanKeyword_ReturnFalse() {
        workbook.addRow(true);
        assertFalse(feed.hasNext());
    }

    @Test
    public void hasNext_DoubleKeyword_ReturnFalse() {
        workbook.addRow(1d);
        assertFalse(feed.hasNext());
    }

    @Test
    public void hasNext_StringKeyword_ReturnTrue() {
        workbook.addRow("keyword");
        assertTrue(feed.hasNext());
    }

    @Test
    public void hasNext_SheetWithJustGaps_RetunsFalse() {
        workbook.skipRows(3);
        assertFalse(feed.hasNext());
    }

    @Test
    public void hasNext_SheetWithStartingGaps_RetunsTrue() {
        workbook.skipRows(3);
        workbook.addRow("keyword");
        assertTrue(feed.hasNext());
    }

    @Test
    public void hasNext_SheetWithMiddleGaps_RetunsTrue() {
        workbook.addRow("keyword");
        workbook.skipRows(3);
        workbook.addRow("keyword");
        feed.next();
        assertTrue(feed.hasNext());
    }

    @Test
    public void hasNext_SheetWithEndingGaps_RetunsTrue() {
        workbook.addRow("keyword");
        workbook.skipRows(3);
        assertTrue(feed.hasNext());
    }

    @Test(expected = NoEventsLeftException.class)
    public void next_NullKeyword_ThrowsException() {
        workbook.addRow(null);
        feed.next();
    }

    @Test(expected = KeywordMustBeTextException.class)
    public void next_BooleanKeyword_ThrowsException() {
        workbook.addRow(true);
        feed.next();
    }

    @Test
    public void next_StringKeyword_ReturnExecuteInstructionLine() {
        workbook.addRow("keyword");
        assertEquals(new ExecuteInstructionLineEvent("keyword"), feed.next());
    }

    @Test(expected = NoEventsLeftException.class)
    public void next_SheetWithOnlyGaps_ThrowsException() {
        workbook.skipRows(3);
        feed.next();
    }

    @Test
    public void next_SheetWithGaps_RetunsLines() {
        workbook.skipRows(3);
        workbook.addRow("keyword");
        workbook.skipRows(2);
        workbook.addRow("keyword");
        workbook.skipRows(3);
        assertEquals(new ExecuteInstructionLineEvent("keyword"), feed.next());
        assertEquals(new ExecuteInstructionLineEvent("keyword"), feed.next());
    }

    @Test
    public void next_BooleanCommentLine_ReturnsCommentInstructionLineEvent() {
        workbook.addRow(null, true);
        assertEquals(new CommentInstructionLineEvent(true), feed.next());
    }

    @Test
    public void next_DoubleCommentLine_ReturnsCommentInstructionLineEvent() {
        workbook.addRow(null, 1d);
        assertEquals(new CommentInstructionLineEvent(1d), feed.next());
    }

    @Test
    public void next_StringCommentLine_ReturnsCommentInstructionLineEvent() {
        workbook.addRow(null, "comment");
        assertEquals(new CommentInstructionLineEvent("comment"), feed.next());
    }

    @Test
    public void next_SingleLineWithArguments_ReturnsExecuteInstructionLineEvent() {
        workbook.addRow("keyword", "arg1", "arg2");
        assertEquals(new ExecuteInstructionLineEvent("keyword", "arg1", "arg2"), feed.next());
    }

    @Test
    public void done_OkResult_TwoExecutionInstructionLinEvents() {
        workbook.addRow("keyword", "arg1", "arg2");
        workbook.addRow("keyword", "arg1", "arg3");
        ExecuteInstructionLineEvent e = (ExecuteInstructionLineEvent) feed.next();
        e.setResult(1);
        feed.done(e);
        assertEquals(new ExecuteInstructionLineEvent("keyword", "arg1", "arg3"), feed.next());
    }

    @Test
    public void done_ExceptionResult_TwoExecutionInstructionLinEvents() {
        workbook.addRow("keyword", "arg1", "arg2");
        workbook.addRow("keyword", "arg1", "arg3");
        ExecuteInstructionLineEvent e = (ExecuteInstructionLineEvent) feed.next();
        e = new TestExecuteInstructionLineEvent(e, new RuntimeException());
        feed.done(e);
        assertEquals(new ValidateInstructionLineEvent("keyword", "arg1", "arg3"), feed.next());
    }

    @Test
    public void reset_SingleLine_ExecutedTwice() {
        workbook.addRow("keyword", "arg1", "arg2");
        ExecuteInstructionLineEvent e = (ExecuteInstructionLineEvent) feed.next();
        e = new TestExecuteInstructionLineEvent(e, new RuntimeException());
        e.setConsumed(true);
        feed.done(e);
        feed.reset();
        assertEquals(new ExecuteInstructionLineEvent("keyword", "arg1", "arg2"), feed.next());
    }
}
