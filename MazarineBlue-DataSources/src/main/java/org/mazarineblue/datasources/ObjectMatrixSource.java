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
package org.mazarineblue.datasources;

import java.util.HashMap;
import java.util.Map;
import org.mazarineblue.datasources.exceptions.ColumnIndexOutOfBoundsException;
import org.mazarineblue.datasources.exceptions.NegativeMatrixArrayException;
import org.mazarineblue.datasources.exceptions.RowIndexOutOfBoundsException;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class ObjectMatrixSource
        extends MatrixSource {

    private final Object[][] matrix;
    private int rowIndex = 0;

    public ObjectMatrixSource(String sourceIdentifier, int n, int m) {
        super(sourceIdentifier);
        if (n < 0 || m < 0)
            throw new NegativeMatrixArrayException(n, m);
        this.matrix = new Object[n][m];
        this.rowIndex = -1;
    }

    public void setHeader(String[] header) {
        Map<String, Integer> map = createHeaderMap(header);
        setHeader(map);
    }

    static private Map<String, Integer> createHeaderMap(String[] header) {
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < header.length; ++i)
            map.put(header[i], i);
        return map;
    }

    @Override
    public boolean hasNext() {
        return rowIndex + 1 < matrix.length;
    }

    @Override
    public void next() {
        checkRowBoundy(rowIndex + 1);
        ++rowIndex;
    }

    @Override
    public void reset() {
        rowIndex = -1;
    }

    @Override
    public final String getLineIdentifier() {
        return getSourceIdentifier() + ":" + rowIndex;
    }

    @Override
    public Object getData(int columnIndex) {
        return getData(rowIndex, columnIndex);
    }

    public final Object getData(int rowIndex, int columnIndex) {
        checkBoundies(rowIndex, columnIndex);
        return matrix[rowIndex][columnIndex];
    }

    @Override
    public boolean setData(int columnIndex, Object value) {
        return setData(rowIndex, columnIndex, value);
    }

    public final boolean setData(int rowIndex, int columnIndex, Object value) {
        checkBoundies(rowIndex, columnIndex);
        matrix[rowIndex][columnIndex] = value;
        return true;
    }
    
    private void checkBoundies(int rowIndex, int columnIndex) {
        checkRowBoundy(rowIndex);
        checkColumnBoundry(rowIndex, columnIndex);
    }
    
    private void checkRowBoundy(int rowIndex) {
        if (rowIndex < 0 || rowIndex >= matrix.length)
            throw new RowIndexOutOfBoundsException(rowIndex);
    }
    
    private void checkColumnBoundry(int rowIndex, int columnIndex) {
        if (columnIndex < 0 || columnIndex >= matrix[rowIndex].length)
            throw new ColumnIndexOutOfBoundsException(rowIndex, columnIndex);
    }
}
