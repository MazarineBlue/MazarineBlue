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

import java.util.List;
import java.util.Set;
import org.mazarineblue.datasources.exceptions.LineIdentifiersOutOfBoundsException;
import org.mazarineblue.datasources.exceptions.UnexpectedTypeAtColumn;
import org.mazarineblue.datasources.exceptions.UnexpectedTypeAtIndex;
import org.mazarineblue.parser.VariableSource;

/**
 * The interface for fetching keys and data from a source.
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 * @see AbstractSource
 * @see ArraySource
 * @see ResourceSource
 * @see ExcelSource
 */
public interface DataSource
        extends VariableSource {

    /**
     * Return true if the data source has more rows.
     *
     * @return true if the data source has more rows.
     */
    public boolean hasNext();

    /**
     * Moves the data source to the next row.
     */
    public void next();

    /**
     * Resets the data source.
     */
    public void reset();

    /**
     * Reset the data source, starting at the first line identifier that contains the specified mark.
     *
     * @param mark the line identifier to look for.
     */
    default public void reset(List<String> mark) {
        List<String> current = getLineIdentifiers();
        reset();
        if (mark == null || mark.size() == 0)
            return;
        if (mark.size() != current.size())
            throw new LineIdentifiersOutOfBoundsException(current, mark);
        while (hasNext() && mark.equals(current) == false) {
            next();
            current = getLineIdentifiers();
        }
    }

    /**
     * Returns the source identifier.
     *
     * @return the source identifier
     */
    public List<String> getSourceIdentifiers();

    /**
     * Returns the line identifier.
     *
     * @return the line identifier.
     */
    public List<String> getLineIdentifiers();

    /**
     * Return the object at the specified column.
     *
     * @param <T>
     * @param column the location where the object resides.
     * @param objectType the type of the return data
     * @return the object at the specified index or null if it could not be fetched.
     */
    @SuppressWarnings("unchecked")
    default <T> T getData(String column, Class<T> objectType) {
        Object obj = getData(column);
        if (obj == null)
            return null;
        if (obj.getClass().equals(objectType) == false)
            throw new UnexpectedTypeAtColumn(column);
        return (T) obj;
    }

    /**
     * Return the object at the specified column.
     *
     * @param column the location where the object resides.
     * @return the object at the specified index or null if it could not be fetched.
     */
    @Override
    public Object getData(String column);

    @SuppressWarnings("unchecked")
    default <T> T getData(int index, Class<T> objectType) {
        Object obj = getData(index);
        if (obj == null)
            return null;
        if (obj.getClass().equals(objectType) == false)
            throw new UnexpectedTypeAtIndex(index);
        return (T) obj;
    }

    public Object getData(int index);

    /**
     * Return the object at the specified index.
     *
     * @param column the location where the object should be stored.
     * @param value the object to store.
     * @return the object at the specified index or null if it could not be fetched.
     */
    @Override
    public boolean setData(String column, Object value);

    /**
     * Sets the specified value to the specified column.
     *
     * @param index the location where the object should be stored.
     * @param value the object to store.
     * @return the object at the specified index or null if it could not be fetched.
     */
    public boolean setData(int index, Object value);

    /**
     * Returns an array of supported columns.
     *
     * @return an list of supported columns or an empty array when none are supported by this data source.
     */
    public Set<String> getColumns();
}
