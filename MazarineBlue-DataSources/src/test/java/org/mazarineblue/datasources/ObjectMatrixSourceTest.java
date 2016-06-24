/*
 * Copyright (c) 2015 Alex de Kruijff
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
package org.mazarineblue.datasources;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import static java.util.Arrays.asList;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mazarineblue.datasources.exceptions.ColumnIndexOutOfBoundsException;
import org.mazarineblue.datasources.exceptions.NegativeMatrixArrayException;
import org.mazarineblue.datasources.exceptions.RowIndexOutOfBoundsException;

/**
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
@RunWith(HierarchicalContextRunner.class)
public class ObjectMatrixSourceTest {

    private static final String IDENTIFIER = "";
    private static final int n = 2, m = 3;
    private static final String[] toSmallHeader = {"A"};
    private static final String[] toLargeHeader = {"A", "B", "C"};

    private ObjectMatrixSource source;
    
    private String lineIdentifier(int row) {
        return IDENTIFIER + ":" + row;
    }

    @Test(expected = NegativeMatrixArrayException.class)
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void createMatrixWithNegativeN() {
        new ObjectMatrixSource(IDENTIFIER, -1, 0);
    }

    @Test(expected = NegativeMatrixArrayException.class)
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void createMatrixWithNegativeM() {
        new ObjectMatrixSource(IDENTIFIER, 0, -1);
    }

    public class GivenMatrix00 {

        @Before
        public void setup() {
            source = new ObjectMatrixSource(IDENTIFIER, 0, 0);
        }

        @Test
        public void hasNext() {
            boolean hasNext = source.hasNext();
            assertEquals(false, hasNext);
        }

        @Test(expected = RowIndexOutOfBoundsException.class)
        public void next() {
            source.next();
            assertEquals(asList(lineIdentifier(0)), source.getLineIdentifiers());
        }

        @Test
        public void reset() {
            source.reset();
            assertEquals(asList(lineIdentifier(-1)), source.getLineIdentifiers());
        }
    }

    public class GivenMatrixNM {

        @Before
        public void setup() {
            source = new ObjectMatrixSource(IDENTIFIER, n, m);
            initialiseData();
        }

        private void initialiseData() {
            for (int i = 0; i < n; ++i)
                for (int j = 0; j < m; ++j)
                    source.setData(i, j, "" + i + "+" + j);
        }

        @Test
        public void setHeader_ToFew() {
            source.setHeader(toSmallHeader);
        }

        @Test
        public void setHeader_ToLarge() {
            source.setHeader(toLargeHeader);
        }

        @Test
        public void hasNext_WithInitilisedMap() {
            boolean hasNext = source.hasNext();
            assertEquals(true, hasNext);
        }

        @Test
        public void hasNext_AfterCallingNextNTimes() {
            for (int i = 0; i < n; ++i)
                source.next();
            boolean hasNext = source.hasNext();
            assertEquals(false, hasNext);
        }

        @Test(expected = RowIndexOutOfBoundsException.class)
        public void next_CalledToManyTimes() {
            for (int i = 0; i <= n; ++i)
                source.next();
        }

        public class GivenNextCalled {

            @Before
            public void setup() {
                source.next();
            }

            @Test
            public void reset() {
                source.reset();
                assertEquals(asList(lineIdentifier(-1)), source.getLineIdentifiers());
            }

            @Test(expected = ColumnIndexOutOfBoundsException.class)
            public void getData_ToSmallColumnIndex() {
                source.getData(-1);
            }

            @Test(expected = ColumnIndexOutOfBoundsException.class)
            public void getData_ToLargeColumnIndex() {
                source.getData(m);
            }

            @Test
            public void getData_00Index() {
                Object data = source.getData(0);
                assertEquals("0+0", data);
            }

            @Test(expected = ColumnIndexOutOfBoundsException.class)
            public void setData_ToSmallColumnIndex() {
                source.setData(-1, "a");
            }

            @Test(expected = ColumnIndexOutOfBoundsException.class)
            public void setData_ToLargeColumnIndex() {
                source.setData(m, "z");
            }

            @Test
            public void setData_00Index() {
                String data = "a+z";
                boolean isSet = source.setData(0, data);
                assertEquals(true, isSet);
                assertEquals(data, source.getData(0));
            }

            @Test(expected = ColumnIndexOutOfBoundsException.class)
            public void setHeader_HeaderWithWrongIndexes() {
                Map<String, Integer> map = new HashMap<>();
                map.put("A", 6);
                source.setHeader(map);
                source.getData("A");
            }
        }
    }
}
