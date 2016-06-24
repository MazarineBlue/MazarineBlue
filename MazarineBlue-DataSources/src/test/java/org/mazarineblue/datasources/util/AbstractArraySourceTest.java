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
package org.mazarineblue.datasources.util;

import java.util.Set;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.mazarineblue.datasources.ArraySource;
import org.mazarineblue.datasources.exceptions.IllegalSourceStateException;
import org.mazarineblue.datasources.exceptions.NegativeIndexException;
import org.mazarineblue.datasources.exceptions.RowIndexOutOfBoundsException;
import static org.mazarineblue.datasources.util.SourceUtil.asSet;

/**
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public abstract class AbstractArraySourceTest {

    private static final String IDENTIFIER = "";
    private static final TestData constUnavailable = new TestData("yek", "eulav");
    private static final TestData constAvailable = new TestData("key", "value");

    protected TestData unavailable, available;
    private ArraySource source;

    protected void setup(ArraySource source) {
        this.source = source;
        this.unavailable = constUnavailable;
        this.available = constAvailable;
    }

    protected void setup(ArraySource source, TestData unavailable, TestData available) {
        this.source = source;
        this.unavailable = unavailable;
        this.available = available;
    }

    protected boolean setData(TestData obj) {
        return source.setData(obj.column, obj.data);
    }

    protected abstract void next();

    protected abstract void reset();

    @Test(expected = RowIndexOutOfBoundsException.class)
    public void nextCalledTwice() {
        next();
        next();
    }

    @Test(expected = IllegalSourceStateException.class)
    public void getDataByColumn_Uninitialized() {
        source.setData(available.column, available.data);
    }

    @Test
    public void getDataByColumn_UnavailableValue_ReturnsNull() {
        next();
        source.setData(available.column, available.data);
        Object data = source.getData(unavailable.column);
        assertEquals(null, data);
    }

    @Test
    public void getDataByColumn_AvailableValue_ReturnsObject() {
        next();
        source.setData(available.column, available.data);
        Object data = source.getData(available.column);
        assertEquals(available.data, data);
    }

    @Test(expected = IllegalSourceStateException.class)
    public void getDataByIndex_Uninitialized() {
        next();
        source.setData(available.column, available.data);
        int index = source.getIndex(unavailable.column);
        reset();
        source.getData(index);
    }

    @Test
    public void getDataByIndex_UnavailableValue_ReturnsNull() {
        next();
        source.setData(available.column, available.data);
        int index = source.getIndex(unavailable.column);
        Object data = source.getData(index);
        assertEquals(null, data);
    }

    @Test
    public void getDataByIndex_AvailableValue_ReturnsObject() {
        next();
        source.setData(available.column, available.data);
        int index = source.getIndex(available.column);
        Object data = source.getData(index);
        assertEquals(available.data, data);
    }

    @Test(expected = NegativeIndexException.class)
    public void setData_NegativeIndex_ThrowsException() {
        next();
        source.setData(-1, "");
    }
    
    @Test(expected = IllegalSourceStateException.class)
    public void setDataUsingColumn_Uninitialized() {
        source.setData(available.column, available.data);
    }

    @Test(expected = IllegalSourceStateException.class)
    public void setDataUsingIndex_Uninitialized() {
        source.setData(0, available.data);
    }

    @Test
    public void setDataUsingIndexThenColumn() {
        next();
        Set<String> expected = asSet("0", available.column);
        setDataUsingIndexThenColumnHelper(true, true, expected);
    }

    protected void setDataUsingIndexThenColumnHelper(boolean flagSetDataIndex, boolean flagSetDataColumn,
                                                     Set<String> expected) {
        next();
        boolean resultSetDataIndex = source.setData(0, available.data);
        boolean resultSetDataColumn = source.setData(available.column, available.data);
        Set<String> columns = source.getColumns();
        assertEquals(expected, columns);
        assertEquals(flagSetDataIndex, resultSetDataIndex);
        assertEquals(flagSetDataColumn, resultSetDataColumn);
    }

    @Test
    public void setDataUsingColumnThenIndex() {
        String[] expected = new String[]{available.column};
        setDataUsingColumnThenIndexHelper(true, true, expected);
    }
    
    protected void setDataUsingColumnThenIndexHelper(boolean flagSetDataColumn, boolean flagSetDataIndex,
                                                     String... expected) {
        next();
        boolean resultSetDataColumn = source.setData(available.column, available.data);
        boolean resultSetDataIndex = source.setData(0, available.data);
        Set<String> columns = source.getColumns();
        assertEquals(asSet(expected), columns);
        assertEquals(flagSetDataColumn, resultSetDataColumn);
        assertEquals(flagSetDataIndex, resultSetDataIndex);
    }

    @Test
    public void getColumns() {
        next();
        source.setData(available.column, available.data);
        reset();
        Set<String> expected = asSet(available.getColumn());
        Set<String> columns = source.getColumns();
        assertEquals(expected, columns);
    }

    protected static class TestData {

        private final String column;
        private final Object data;

        private TestData(String column, Object data) {
            this.column = column;
            this.data = data;
        }

        public String getColumn() {
            return column;
        }

        public Object getData() {
            return data;
        }
    }
}
