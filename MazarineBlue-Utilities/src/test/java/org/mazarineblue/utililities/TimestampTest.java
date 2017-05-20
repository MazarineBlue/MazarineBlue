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
package org.mazarineblue.utililities;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import static java.lang.String.format;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Pattern;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(HierarchicalContextRunner.class)
public class TimestampTest {

    @Test
    public void getTimestamp_DefaultFormat() {
        assertEquals("1970-01-01 01:00:00 +01", new Timestamp().getTimestamp(new Date(0)));
    }

    @Test
    public void getTimestamp_SpecifiedFormat() {
        assertEquals("01-01-1970 01:00:00 +01", new Timestamp("dd-MM-yyyy HH:mm:ss X").getTimestamp(new Date(0)));
    }

    @Test
    public void getTimestamp_CurrentDate() {
        Measure measure = new Measure();
        String str = measure.getTimestamp();
        String regex = measure.getRegularExpession();
        assertTrue(Pattern.compile(regex).matcher(str).find());
    }

    private static class Measure {

        private final Calendar start;
        private Calendar end;

        private Measure() {
            start = new GregorianCalendar();
        }

        private String getRegularExpession() {
            String year = getFormattedField(Calendar.YEAR, 4);
            String month = getFormattedField(Calendar.MONTH, 2);
            String day = getFormattedField(Calendar.DAY_OF_MONTH, 2);
            String hour = getFormattedField(Calendar.HOUR_OF_DAY, 2);
            String minute = getFormattedField(Calendar.MINUTE, 2);
            String second = getFormattedField(Calendar.SECOND, 2);
            return format("%s-%s-%s %s:%s:%s %s", year, month, day, hour, minute, second, "[-+]\\d{2}");
        }

        private String getTimestamp() {
            String timestamp = new Timestamp().getTimestamp();
            end = new GregorianCalendar();
            return timestamp;
        }

        private String getFormattedField(int calendarField, int n) {
            int component = start.get(calendarField);
            int offset = calendarField == Calendar.MONTH ? 1 : 0;
            return component != end.get(calendarField) ? "\\d{" + n + '}' : build(component + offset, n);
        }

        private String build(int component, int n) {
            StringBuilder builder = new StringBuilder(24);
            String str = Integer.toString(component);
            for (int i = str.length(); i < n; ++i)
                builder.append('0');
            builder.append(str);
            return builder.toString();
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class EqualsAndHashCode {

        private Timestamp a;

        @Before
        public void setup() {
            a = new Timestamp();
        }

        @After
        public void teardown() {
            a = null;
        }

        @Test
        @SuppressWarnings("ObjectEqualsNull")
        public void equals_Null() {
            assertFalse(a.equals(null));
        }

        @Test
        @SuppressWarnings("IncompatibleEquals")
        public void equals_DifferentClass() {
            assertFalse(a.equals(""));
        }

        @Test
        public void hashCode_DifferentFormat() {
            Timestamp b = new Timestamp("");
            assertNotEquals(a.hashCode(), b.hashCode());
        }

        @Test
        public void equals_DifferentFormat() {
            Timestamp b = new Timestamp("");
            assertNotEquals(a, b);
        }

        @Test
        public void hashCode_IdenticalFormat() {
            Timestamp b = new Timestamp();
            assertEquals(a.hashCode(), b.hashCode());
        }

        @Test
        public void equals_IdenticalFormat() {
            Timestamp b = new Timestamp();
            assertEquals(a, b);
        }
    }
}
