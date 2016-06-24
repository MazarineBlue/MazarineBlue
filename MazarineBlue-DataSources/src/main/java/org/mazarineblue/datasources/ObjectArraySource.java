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
import java.util.Set;
import java.util.stream.Collectors;
import org.mazarineblue.datasources.exceptions.NegativeIndexException;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class ObjectArraySource
        extends ArraySource {

    @SuppressWarnings("CollectionWithoutInitialCapacity")
    private final Map<String, Object> data = new HashMap<>();

    @SuppressWarnings("CollectionWithoutInitialCapacity")
    private final Map<Integer, String> columns = new HashMap<>();

    @SuppressWarnings("CollectionWithoutInitialCapacity")
    private final Map<String, Integer> indexes = new HashMap<>();

    public ObjectArraySource(String sourceIdentifier) {
        this(sourceIdentifier, false);
    }

    public ObjectArraySource(String sourceIdentifier, boolean initialize) {
        super(sourceIdentifier);
        if (initialize)
            next();
    }

    public final boolean containsColumn(String key) {
        return data.containsKey(key);
    }

    public boolean containsData(Object value) {
        return data.containsValue(value);
    }

    @Override
    public int getIndex(String column) {
        Integer index = indexes.get(column);
        return index == null ? -1 : index;
    }
    
    @Override
    public Object getData(String column) {
        validateState();
        return data.get(column);
    }

    @Override
    public Object getData(int index) {
        validateState();
        String column = columns.get(index);
        return column == null ? null : data.get(column);
    }

    @Override
    public boolean setData(String column, Object value) {
        validateState();
        if (columnIsIndexed(column) == false)
            registerColumn(column);
        data.put(column, value);
        return true;
    }

    private boolean columnIsIndexed(String column) {
        return indexes.containsKey(column);
    }

    private void registerColumn(String column) {
        int index = columns.size();
        indexes.put(column, index);
        columns.put(index, column);
    }

    @Override
    @SuppressWarnings("CollectionWithoutInitialCapacity")
    public Set<String> getColumns() {
        return columns.values().stream().collect(Collectors.toSet());
    }

    @Override
    public boolean setData(int index, Object value) {
        if (index < 0)
            throw new NegativeIndexException(index);
        validateState();
        String column = hasColumn(index)
                ? getColumn(index)
                : createColumn(index);
        data.put(column, value);
        return true;
    }

    private boolean hasColumn(int index) {
        return columns.containsKey(index);
    }

    private String getColumn(int index) {
        return columns.get(index);
    }

    private String createColumn(int index) {
        String column = "" + index;
        columns.put(index, column);
        indexes.put(column, index);
        return column;
    }
}
