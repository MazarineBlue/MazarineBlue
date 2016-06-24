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

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.mazarineblue.datasources.ArraySource;
import org.mazarineblue.datasources.exceptions.RowIndexOutOfBoundsException;
import static org.mazarineblue.datasources.util.SourceUtil.asSet;

/**
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class DataSourceSpy
        extends ArraySource {

    private final String sourceIdentifier;
    private final int simulateHavingRows;
    private int row = -1;

    private final Map<String, Integer> map = new HashMap<>(4);
    private String column;
    private Integer index;
    private Object value;

    public DataSourceSpy(String sourceIdentifier, int simulateHavingRows) {
        super(sourceIdentifier);
        this.sourceIdentifier = sourceIdentifier;
        this.simulateHavingRows = simulateHavingRows;
    }
    
    private void incrementCount(String key) {
        Integer counter = getCounter(key);
        map.put(key, ++counter);
    }
    
    private Integer getCounter(String key) {
        Integer counter = map.get(key);
        if (counter == null)
            map.put(key, counter = 0);
        return counter;
    }
    
    private void resetCount(String key) {
        map.put(key, 0);
    }
    
    public int getHasNextCounter() {
        return getCounter("hasNext");
    }

    public int getNextCounter() {
        return getCounter("next");
    }

    public int getResetCounter() {
        return getCounter("reset");
    }

    public int getSourceIdentifiersCounter() {
        return getCounter("getSourceIdentifiers");
    }

    public int getLineIdentifiersCounter() {
        return getCounter("getLineIdentifiers");
    }

    public int getGetDataByColumnCounter() {
        return getCounter("getDataByColumn");
    }

    public int getGetDataByIndexCounter() {
        return getCounter("getDataByIndex");
    }

    public int getSetDataByColumnCounter() {
        return getCounter("setDataByColumn");
    }

    public int getSetDataByIndexCounter() {
        return getCounter("setDataByIndex");
    }

    public Object getColumnsCounter() {
        return getCounter("getColumns");
    }

    public String getColumn() {
        return column;
    }

    public Integer getIndex() {
        return index;
    }

    public Object getData() {
        return value;
    }

    @Override
    public boolean hasNext() {
        incrementCount("hasNext");
        return row + 1 < simulateHavingRows;
    }

    @Override
    public void next() {
        if (row + 1 >= simulateHavingRows)
            throw new RowIndexOutOfBoundsException(row + 1);
        incrementCount("next");
        ++row;
    }

    @Override
    public void reset() {
        incrementCount("reset");
        resetCount("nextCounter");
        row = -1;
    }

    @Override
    public List<String> getSourceIdentifiers() {
        incrementCount("getSourceIdentifiers");
        List<String> list = asList(sourceIdentifier);
        return unmodifiableList(list);
    }

    @Override
    public String getSourceIdentifier() {
        incrementCount("getSourceIdentifier");
        return sourceIdentifier;
    }

    @Override
    public List<String> getLineIdentifiers() {
        incrementCount("getLineIdentifiers");
        String lineIdentifier = getLineIdentifier(row);
        List<String> list = asList(lineIdentifier);
        return unmodifiableList(list);
    }

    public final String getLineIdentifier(int row) {
        return "source:" + row;
    }

    @Override
    public Object getData(String column) {
        incrementCount("getDataByColumn");
        return value;
    }

    @Override
    public Object getData(int index) {
        incrementCount("getDataByIndex");
        return value;
    }

    @Override
    public boolean setData(String column, Object value) {
        incrementCount("setDataByColumn");
        this.column = column;
        this.index = null;
        this.value = value;
        return true;
    }

    @Override
    public boolean setData(int index, Object value) {
        incrementCount("setDataByIndex");
        this.column = null;
        this.index = index;
        this.value = value;
        return true;
    }

    @Override
    public Set<String> getColumns() {
        incrementCount("getColumns");
        return asSet(column);
    }

    @Override
    public int getIndex(String column) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
