/*
 * Copyright (c) 2018 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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
package org.mazarineblue.executors;

import java.util.Calendar;
import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MILLISECOND;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.SECOND;
import java.util.Date;
import java.util.GregorianCalendar;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.eventdriven.feeds.MemoryFeed;
import org.mazarineblue.executors.events.SetFileSystemEvent;
import org.mazarineblue.executors.util.FeedExecutorOutputSpy;
import org.mazarineblue.executors.util.TestFeedExecutorFactory;
import org.mazarineblue.fs.MemoryFileSystem;
import org.mazarineblue.keyworddriven.Keyword;
import org.mazarineblue.keyworddriven.Library;
import org.mazarineblue.keyworddriven.events.ExecuteInstructionLineEvent;
import org.mazarineblue.keyworddriven.exceptions.ArgumentsAreIncompatibleException;
import org.mazarineblue.plugins.PluginLoader;
import org.mazarineblue.plugins.util.LibraryPluginStub;
import org.mazarineblue.variablestore.events.GetVariableEvent;

public class ConvertStringToDateTest {

    private MemoryFileSystem fs;
    private FeedExecutorOutputSpy output;
    private Executor executor;

    @Before
    public void setup() {
        fs = new MemoryFileSystem();
        output = new FeedExecutorOutputSpy();
        executor = TestFeedExecutorFactory.newInstance(fs, output).create();
        executor.execute(new MemoryFeed(new SetFileSystemEvent(fs)));
        PluginLoader.getInstance().injectPlugin(new LibraryPluginStub("foo", new ConvertLibrary()));
    }

    @After
    public void teardown() {
        PluginLoader.getInstance().reload();
        fs = null;
        output = null;
        executor = null;
    }

    @Test(expected = ArgumentsAreIncompatibleException.class)
    public void date_Unparsable() {
        parse("bar");
    }

    @Test
    public void date_Now() {
        String input = "now";
        Date expected = new Date();
        Date actual = (Date) parse(input);
        assertDateRangeDelta(expected, actual, 2000);
    }

    @Test
    public void date_Tomorrow() {
        Date expected = tomorrow();
        Date actual = (Date) parse("+ 1 d");
        assertDateRangeDelta(expected, actual, 2000);
    }

    private Date tomorrow() {
        Calendar c = Calendar.getInstance();
        c.add(DAY_OF_MONTH, 1);
        Date expected = c.getTime();
        return expected;
    }

    @Test
    public void date_Yesterday() {
        Date expected = yesterday();
        Date actual = (Date) parse("- 1 d");
        assertDateRangeDelta(expected, actual, 2000);
    }

    private Date yesterday() {
        Calendar c = Calendar.getInstance();
        c.add(DAY_OF_MONTH, -1);
        Date expected = c.getTime();
        return expected;
    }

    private void assertDateRangeDelta(Date expected, Date actual, long offset) {
        long millis = expected.getTime();
        Date start = new Date(millis - offset);
        Date end = new Date(millis + offset);
        assertDateRange(start, actual, end);
    }

    private void assertDateRange(Date start, Date date, Date end) {
        assertTrue(start.before(date));
        assertTrue(end.after(date));
    }


    @Test
    public void date_DateAndTime() {
        Date expected = new GregorianCalendar(1978, 9, 30, 12, 34, 56).getTime();
        assertEquals(expected, parse("1978-10-30 12:34:56"));
    }

    @Test
    public void date_OnlyDate() {
        Date expected = new GregorianCalendar(1978, 9, 30).getTime();
        assertEquals(expected, parse("1978-10-30"));
    }

    @Test
    public void date_OnlyTime() {
        Date expected = getTime(12, 34, 56);
        assertEquals(expected, parse("12:34:56"));
    }

    private Date getTime(int hour, int minute, int second) {
        Calendar calendar = Calendar.getInstance();
        setTime(calendar, hour, minute, second);
        return calendar.getTime();
    }

    private void setTime(Calendar calendar, int hour, int minute, int second) {
        calendar.set(HOUR_OF_DAY, hour);
        calendar.set(MINUTE, minute);
        calendar.set(SECOND, second);
        calendar.set(MILLISECOND, 0);
    }

    private Object parse(String input) {
        GetVariableEvent e = new GetVariableEvent("var");
        executor.execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "foo"),
                new ExecuteInstructionLineEvent("Set", "var", "=Date", input),
                e));
        output.throwFirstException();
        return e.getValue();
    }

    @SuppressWarnings("PublicInnerClass")
    public static class ConvertLibrary
            extends Library {

        public ConvertLibrary() {
            super("foo");
        }

        @Keyword("Date")
        public Date date(Date date) {
            return date;
        }
    }
}
