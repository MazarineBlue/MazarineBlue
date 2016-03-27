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

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class TypeConvertorTest {

    private TypeConvertor typeConvertor = new TypeConvertor();

    @Before
    public void setup() {
        typeConvertor = new TypeConvertor();
    }

    @After
    public void teardown() {
        typeConvertor = null;
    }

    @Test
    @SuppressWarnings("UnnecessaryBoxing")
    public void isConvertable() {
        assertIsConvertable(true, Byte.class, (byte) 2);
        assertIsConvertable(true, Byte.class, (short) 3);
        assertIsConvertable(true, Byte.class, 4);
        assertIsConvertable(true, Byte.class, 5L);
        assertIsConvertable(true, Byte.class, 6.0f);
        assertIsConvertable(true, Byte.class, 7.0d);
        assertIsConvertable(false, Byte.class, true);
        assertIsConvertable(true, Byte.class, '9');
        assertIsConvertable(true, Byte.class, "10");
        assertIsConvertable(true, Byte.class, Byte.MAX_VALUE);
        assertIsConvertable(true, Byte.class, Short.MAX_VALUE);
        assertIsConvertable(true, Byte.class, Integer.MAX_VALUE);
        assertIsConvertable(true, Byte.class, Long.MAX_VALUE);
        assertIsConvertable(true, Byte.class, Float.MAX_VALUE);
        assertIsConvertable(true, Byte.class, Double.MAX_VALUE);
        assertIsConvertable(false, Byte.class, "128");
        assertIsConvertable(false, Byte.class, "abc");

        assertIsConvertable(true, Short.class, (byte) 2);
        assertIsConvertable(true, Short.class, (short) 3);
        assertIsConvertable(true, Short.class, 4);
        assertIsConvertable(true, Short.class, 5L);
        assertIsConvertable(true, Short.class, 6.0f);
        assertIsConvertable(true, Short.class, 7.0d);
        assertIsConvertable(false, Short.class, true);
        assertIsConvertable(true, Short.class, '9');
        assertIsConvertable(true, Short.class, "10");
        assertIsConvertable(true, Short.class, Byte.MAX_VALUE);
        assertIsConvertable(true, Short.class, Short.MAX_VALUE);
        assertIsConvertable(true, Short.class, Integer.MAX_VALUE);
        assertIsConvertable(true, Short.class, Long.MAX_VALUE);
        assertIsConvertable(true, Short.class, Float.MAX_VALUE);
        assertIsConvertable(true, Short.class, Double.MAX_VALUE);
        assertIsConvertable(false, Short.class, "32768");
        assertIsConvertable(false, Short.class, "abc");

        assertIsConvertable(true, Integer.class, (byte) 2);
        assertIsConvertable(true, Integer.class, (short) 3);
        assertIsConvertable(true, Integer.class, 4);
        assertIsConvertable(true, Integer.class, 5L);
        assertIsConvertable(true, Integer.class, 6.0f);
        assertIsConvertable(true, Integer.class, 7.0d);
        assertIsConvertable(false, Integer.class, true);
        assertIsConvertable(true, Integer.class, '9');
        assertIsConvertable(true, Integer.class, "10");
        assertIsConvertable(true, Integer.class, Byte.MAX_VALUE);
        assertIsConvertable(true, Integer.class, Short.MAX_VALUE);
        assertIsConvertable(true, Integer.class, Integer.MAX_VALUE);
        assertIsConvertable(true, Integer.class, Long.MAX_VALUE);
        assertIsConvertable(true, Integer.class, Float.MAX_VALUE);
        assertIsConvertable(true, Integer.class, Double.MAX_VALUE);
        assertIsConvertable(false, Integer.class, "2147483648");
        assertIsConvertable(false, Integer.class, "abc");

        assertIsConvertable(true, Long.class, (byte) 2);
        assertIsConvertable(true, Long.class, (short) 3);
        assertIsConvertable(true, Long.class, 4);
        assertIsConvertable(true, Long.class, 5L);
        assertIsConvertable(true, Long.class, 6.0f);
        assertIsConvertable(true, Long.class, 7.0d);
        assertIsConvertable(false, Long.class, true);
        assertIsConvertable(true, Long.class, '9');
        assertIsConvertable(true, Long.class, "10");
        assertIsConvertable(true, Long.class, Byte.MAX_VALUE);
        assertIsConvertable(true, Long.class, Short.MAX_VALUE);
        assertIsConvertable(true, Long.class, Integer.MAX_VALUE);
        assertIsConvertable(true, Long.class, Long.MAX_VALUE);
        assertIsConvertable(true, Long.class, Float.MAX_VALUE);
        assertIsConvertable(true, Long.class, Double.MAX_VALUE);
        assertIsConvertable(false, Long.class, "9223372036854775808");
        assertIsConvertable(false, Long.class, "abc");

        assertIsConvertable(true, Float.class, (byte) 2);
        assertIsConvertable(true, Float.class, (short) 3);
        assertIsConvertable(true, Float.class, 4);
        assertIsConvertable(true, Float.class, 5L);
        assertIsConvertable(true, Float.class, 6.0f);
        assertIsConvertable(true, Float.class, 7.0d);
        assertIsConvertable(false, Float.class, true);
        assertIsConvertable(true, Float.class, '9');
        assertIsConvertable(true, Float.class, "10");
        assertIsConvertable(true, Float.class, Byte.MAX_VALUE);
        assertIsConvertable(true, Float.class, Short.MAX_VALUE);
        assertIsConvertable(true, Float.class, Integer.MAX_VALUE);
        assertIsConvertable(true, Float.class, Long.MAX_VALUE);
        assertIsConvertable(true, Float.class, Float.MAX_VALUE);
        assertIsConvertable(true, Float.class, Double.MAX_VALUE);
        assertIsConvertable(true, Float.class, "3.4028236e+38f");
        assertIsConvertable(false, Float.class, "abc");

        assertIsConvertable(true, Double.class, (byte) 2);
        assertIsConvertable(true, Double.class, (short) 3);
        assertIsConvertable(true, Double.class, 4);
        assertIsConvertable(true, Double.class, 5L);
        assertIsConvertable(true, Double.class, 6.0f);
        assertIsConvertable(true, Double.class, 7.0d);
        assertIsConvertable(false, Double.class, true);
        assertIsConvertable(true, Double.class, '9');
        assertIsConvertable(true, Double.class, "10");
        assertIsConvertable(true, Double.class, "1.7976931348623158e+308");
        assertIsConvertable(false, Double.class, "abc");

        assertIsConvertable(true, Boolean.class, (byte) 2);
        assertIsConvertable(true, Boolean.class, (short) 3);
        assertIsConvertable(true, Boolean.class, 4);
        assertIsConvertable(true, Boolean.class, 5L);
        assertIsConvertable(false, Boolean.class, 6.0f);
        assertIsConvertable(false, Boolean.class, 7.0d);
        assertIsConvertable(true, Boolean.class, true);
        assertIsConvertable(true, Boolean.class, '9');
        assertIsConvertable(true, Boolean.class, "10");

        assertIsConvertable(false, Character.class, (byte) 2);
        assertIsConvertable(false, Character.class, (short) 3);
        assertIsConvertable(false, Character.class, 4);
        assertIsConvertable(false, Character.class, 5L);
        assertIsConvertable(false, Character.class, 6.0f);
        assertIsConvertable(false, Character.class, 7.0d);
        assertIsConvertable(false, Character.class, true);
        assertIsConvertable(true, Character.class, '9');
        assertIsConvertable(true, Character.class, "x");
        assertIsConvertable(false, Character.class, "10");

        assertIsConvertable(true, String.class, (byte) 2);
        assertIsConvertable(true, String.class, (short) 3);
        assertIsConvertable(true, String.class, 4);
        assertIsConvertable(true, String.class, 5L);
        assertIsConvertable(true, String.class, 6.0f);
        assertIsConvertable(true, String.class, 7.0d);
        assertIsConvertable(true, String.class, true);
        assertIsConvertable(true, String.class, '9');
        assertIsConvertable(true, String.class, "10");

        assertIsConvertable(false, byte.class, new Object());
        assertIsConvertable(false, short.class, new Object());
        assertIsConvertable(false, int.class, new Object());
        assertIsConvertable(false, long.class, new Object());
        assertIsConvertable(false, float.class, new Object());
        assertIsConvertable(false, double.class, new Object());
        assertIsConvertable(false, boolean.class, new Object());
        assertIsConvertable(false, char.class, new Object());
        assertIsConvertable(false, String.class, new Object());

        assertIsConvertable(true, Object.class, (byte) 2);
        assertIsConvertable(true, Object.class, (short) 3);
        assertIsConvertable(true, Object.class, 4);
        assertIsConvertable(true, Object.class, 5L);
        assertIsConvertable(true, Object.class, 6.0f);
        assertIsConvertable(true, Object.class, 7.0d);
        assertIsConvertable(true, Object.class, true);
        assertIsConvertable(true, Object.class, 'a');
        assertIsConvertable(true, Object.class, "a");
    }

    private void assertIsConvertable(Object result, Class<?> outputType, Object input) {
        assertEquals(result, typeConvertor.isConvertable(outputType, input));
    }

    @Test
    @SuppressWarnings({"UnnecessaryBoxing", "BooleanConstructorCall"})
    public void convert_Accept() {
        assertConvertEquals(new Byte((byte) 2), Byte.class, (byte) 2);
        assertConvertEquals(new Byte((byte) 3), Byte.class, (short) 3);
        assertConvertEquals(new Byte((byte) 4), Byte.class, 4);
        assertConvertEquals(new Byte((byte) 5), Byte.class, 5L);
        assertConvertEquals(new Byte((byte) 6), Byte.class, 6.0f);
        assertConvertEquals(new Byte((byte) 7), Byte.class, 7.0d);
        assertConvertEquals(new Byte((byte) 9), Byte.class, '9');
        assertConvertEquals(new Byte((byte) 10), Byte.class, "10");
        assertConvertEquals(new Byte(Byte.MAX_VALUE), Byte.class, Byte.MAX_VALUE);
        assertConvertEquals(new Byte((byte) -1), Byte.class, Short.MAX_VALUE);
        assertConvertEquals(new Byte((byte) -1), Byte.class, Integer.MAX_VALUE);
        assertConvertEquals(new Byte((byte) -1), Byte.class, Long.MAX_VALUE);
        assertConvertEquals(new Byte((byte) -1), Byte.class, Float.MAX_VALUE);
        assertConvertEquals(new Byte((byte) -1), Byte.class, Double.MAX_VALUE);

        assertConvertEquals(new Short((short) 2), Short.class, (byte) 2);
        assertConvertEquals(new Short((short) 3), Short.class, (short) 3);
        assertConvertEquals(new Short((short) 4), Short.class, 4);
        assertConvertEquals(new Short((short) 5), Short.class, 5L);
        assertConvertEquals(new Short((short) 6), Short.class, 6.0f);
        assertConvertEquals(new Short((short) 7), Short.class, 7.0d);
        assertConvertEquals(new Short((short) 9), Short.class, '9');
        assertConvertEquals(new Short((short) 10), Short.class, "10");
        assertConvertEquals(new Short(Byte.MAX_VALUE), Short.class, Byte.MAX_VALUE);
        assertConvertEquals(new Short(Short.MAX_VALUE), Short.class, Short.MAX_VALUE);
        assertConvertEquals(new Short((short) -1), Short.class, Integer.MAX_VALUE);
        assertConvertEquals(new Short((short) -1), Short.class, Long.MAX_VALUE);
        assertConvertEquals(new Short((short) -1), Short.class, Float.MAX_VALUE);
        assertConvertEquals(new Short((short) -1), Short.class, Double.MAX_VALUE);

        assertConvertEquals(new Integer(2), Integer.class, (byte) 2);
        assertConvertEquals(new Integer(3), Integer.class, (short) 3);
        assertConvertEquals(new Integer(4), Integer.class, 4);
        assertConvertEquals(new Integer(5), Integer.class, 5L);
        assertConvertEquals(new Integer(6), Integer.class, 6.0f);
        assertConvertEquals(new Integer(7), Integer.class, 7.0d);
        assertConvertEquals(new Integer(9), Integer.class, '9');
        assertConvertEquals(new Integer(10), Integer.class, "10");
        assertConvertEquals(new Integer(Byte.MAX_VALUE), Integer.class, Byte.MAX_VALUE);
        assertConvertEquals(new Integer(Short.MAX_VALUE), Integer.class, Short.MAX_VALUE);
        assertConvertEquals(new Integer(Integer.MAX_VALUE), Integer.class, Integer.MAX_VALUE);
        assertConvertEquals(new Integer(-1), Integer.class, Long.MAX_VALUE);
        assertConvertEquals(new Integer(Integer.MAX_VALUE), Integer.class, Float.MAX_VALUE);
        assertConvertEquals(new Integer(Integer.MAX_VALUE), Integer.class, Double.MAX_VALUE);

        assertConvertEquals(new Long(2), Long.class, (byte) 2);
        assertConvertEquals(new Long(3), Long.class, (short) 3);
        assertConvertEquals(new Long(4), Long.class, 4);
        assertConvertEquals(new Long(5), Long.class, 5L);
        assertConvertEquals(new Long(6), Long.class, 6.0f);
        assertConvertEquals(new Long(7), Long.class, 7.0d);
        assertConvertEquals(new Long(9), Long.class, '9');
        assertConvertEquals(new Long(10), Long.class, "10");
        assertConvertEquals(new Long(Byte.MAX_VALUE), Long.class, Byte.MAX_VALUE);
        assertConvertEquals(new Long(Short.MAX_VALUE), Long.class, Short.MAX_VALUE);
        assertConvertEquals(new Long(Integer.MAX_VALUE), Long.class, Integer.MAX_VALUE);
        assertConvertEquals(new Long(Long.MAX_VALUE), Long.class, Long.MAX_VALUE);
        assertConvertEquals(new Long(Long.MAX_VALUE), Long.class, Float.MAX_VALUE);
        assertConvertEquals(new Long(Long.MAX_VALUE), Long.class, Double.MAX_VALUE);

        assertConvertEquals(new Float(2), Float.class, (byte) 2);
        assertConvertEquals(new Float(3), Float.class, (short) 3);
        assertConvertEquals(new Float(4), Float.class, 4);
        assertConvertEquals(new Float(5), Float.class, 5L);
        assertConvertEquals(new Float(6), Float.class, 6.0f);
        assertConvertEquals(new Float(7), Float.class, 7.0d);
        assertConvertEquals(new Float(9), Float.class, '9');
        assertConvertEquals(new Float(10), Float.class, "10");

        assertConvertEquals(new Double(2), Double.class, (byte) 2);
        assertConvertEquals(new Double(3), Double.class, (short) 3);
        assertConvertEquals(new Double(4), Double.class, 4);
        assertConvertEquals(new Double(5), Double.class, 5L);
        assertConvertEquals(new Double(6), Double.class, 6.0f);
        assertConvertEquals(new Double(7), Double.class, 7.0d);
        assertConvertEquals(new Double(9), Double.class, '9');
        assertConvertEquals(new Double(10), Double.class, "10");

        assertConvertEquals(new Boolean(true), Boolean.class, true);
        assertConvertEquals(new Boolean(true), Boolean.class, (byte) 1);
        assertConvertEquals(new Boolean(true), Boolean.class, (short) 1);
        assertConvertEquals(new Boolean(true), Boolean.class, 1);
        assertConvertEquals(new Boolean(true), Boolean.class, 1L);
        assertConvertEquals(new Boolean(true), Boolean.class, '9');
        assertConvertEquals(new Boolean(true), Boolean.class, "true");
        assertConvertEquals(new Boolean(true), Boolean.class, "True");
        assertConvertEquals(new Boolean(true), Boolean.class, "TRUE");
        assertConvertEquals(new Boolean(true), Boolean.class, "TruE");

        assertConvertEquals(new Boolean(false), Boolean.class, false);
        assertConvertEquals(new Boolean(false), Boolean.class, (byte) 0);
        assertConvertEquals(new Boolean(false), Boolean.class, (short) 0);
        assertConvertEquals(new Boolean(false), Boolean.class, 0);
        assertConvertEquals(new Boolean(false), Boolean.class, 0L);
        assertConvertEquals(new Boolean(false), Boolean.class, (char) 0);
        assertConvertEquals(new Boolean(false), Boolean.class, "0");
        assertConvertEquals(new Boolean(false), Boolean.class, "false");
        assertConvertEquals(new Boolean(false), Boolean.class, "False");
        assertConvertEquals(new Boolean(false), Boolean.class, "FALSE");
        assertConvertEquals(new Boolean(false), Boolean.class, "FalsE");
        assertConvertEquals(new Boolean(false), Boolean.class, "yes");

        assertConvertEquals(new Character('9'), Character.class, '9');
        assertConvertEquals(new Character('x'), Character.class, "x");

        assertConvertEquals("2", String.class, (byte) 2);
        assertConvertEquals("3", String.class, (short) 3);
        assertConvertEquals("4", String.class, 4);
        assertConvertEquals("5", String.class, 5L);
        assertConvertEquals("6.0", String.class, 6.0f);
        assertConvertEquals("7.0", String.class, 7.0d);
        assertConvertEquals("true", String.class, true);
        assertConvertEquals("9", String.class, '9');
        assertConvertEquals("10", String.class, "10");

        assertConvertEquals((byte) 2, Object.class, (byte) 2);
        assertConvertEquals((short) 3, Object.class, (short) 3);
        assertConvertEquals(4, Object.class, 4);
        assertConvertEquals(5L, Object.class, 5L);
        assertConvertEquals(6.0f, Object.class, 6.0f);
        assertConvertEquals(7.0d, Object.class, 7.0d);
        assertConvertEquals(true, Object.class, true);
        assertConvertEquals('a', Object.class, 'a');
        assertConvertEquals("a", Object.class, "a");
    }

    private void assertConvertEquals(Object result, Class<?> outputType, Object input) {
        assertEquals(result, typeConvertor.convert(outputType, input));
    }

    @Test
    @SuppressWarnings({"UnnecessaryBoxing", "BooleanConstructorCall"})
    public void convert_ThrowsException() {
        assertConvertThrowsException(Byte.class, true);
        assertConvertThrowsException(Short.class, true);
        assertConvertThrowsException(Integer.class, true);
        assertConvertThrowsException(Long.class, true);
        assertConvertThrowsException(Float.class, true);
        assertConvertThrowsException(Double.class, true);
        assertConvertThrowsException(Boolean.class, 6.0f);
        assertConvertThrowsException(Boolean.class, 7.0d);
        assertConvertThrowsException(Character.class, (byte) 2);
        assertConvertThrowsException(Character.class, (short) 3);
        assertConvertThrowsException(Character.class, 4);
        assertConvertThrowsException(Character.class, 5L);
        assertConvertThrowsException(Character.class, 6.0f);
        assertConvertThrowsException(Character.class, 7.0d);
        assertConvertThrowsException(Character.class, true);
    }

    private void assertConvertThrowsException(Class<?> outputType, Object input) {
        try {
            typeConvertor.convert(outputType, input);
            fail("RuntimeException should have been thrown.");
        } catch (RuntimeException ex) {
        }
    }
}
