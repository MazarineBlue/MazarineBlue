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
package org.mazarineblue.utilities;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.After;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
@RunWith(HierarchicalContextRunner.class)
public class ArgumentListTest {

    private Object[] arr;
    private ArgumentList list;

    @After
    public void teardown() {
        arr = null;
        list = null;
    }

    @SuppressWarnings("PublicInnerClass")
    public class ZeroArguments {

        @Before
        public void setup() {
            arr = new Object[0];
            list = new ArgumentList();
        }

        @Test
        public void tostring_ZeroArguments() {
            assertEquals(arrayToString(arr), list.toString());
        }

        @Test
        public void getArguments_ZeroArguments() {
            assertEquals(arrayToString(arr), list.toString());
        }

        @Test
        public void setArguments() {
            list.setArguments("arg1", "arg2");
            assertEquals(arrayToString("arg1", "arg2"), list.toString());
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class OneArguments {

        @Before
        public void setup() {
            arr = new Object[]{"foo"};
            list = new ArgumentList(arr);
        }

        @Test
        public void toString_OneArguments() {
            assertEquals(arrayToString(arr), list.toString());
        }

        @Test
        public void getArguments_OneArguments() {
            assertArrayEquals(arr, list.getArguments());
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class TwoArguments {

        @Before
        public void setup() {
            arr = new String[]{"foo", "oof"};
            list = new ArgumentList(arr);
        }

        @Test
        public void toString_TwoArguments() {
            assertEquals(arrayToString(arr), list.toString());
        }

        @Test
        public void getArguments_TwoArguments() {
            assertArrayEquals(arr, list.getArguments());
        }
    }

    private static Object arrayToString(Object... arr) {
        switch (arr.length) {
            case 0:
                return "";
            case 1:
                return arr[0];
            default:
                return buildString(arr, ", ");
        }
    }

    private static String buildString(Object[] arr, String delimiter) {
        StringBuilder builder = new StringBuilder(arr.length * 8);
        builder.append(arr[0]);
        for (int i = 1; i < arr.length; ++i)
            builder.append(delimiter).append(arr[i]);
        return builder.toString();
    }
}
