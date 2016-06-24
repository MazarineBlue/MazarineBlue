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

import static java.util.Collections.EMPTY_SET;
import java.util.Set;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class NullDataSource
        extends ArraySource {

    private static int instancesCount = 0;
    private static final String DEFAULT_IDENTIFIER = "Null: ";
    private final boolean accepting;

    public NullDataSource() {
        this(generateIdentifier());
    }
    
    public NullDataSource(String sourceIdentifier) {
        this(sourceIdentifier, true);
    }

    public NullDataSource(boolean accepting) {
        this(generateIdentifier(), accepting);
    }

    public NullDataSource(String sourceIdentifier, boolean accepting) {
        super(sourceIdentifier);
        this.accepting = accepting;
    }

    private static String generateIdentifier() {
        return DEFAULT_IDENTIFIER + id();
    }

    private static int id() {
        return instancesCount++;
    }

    @Override
    public int getIndex(String column) {
        return -1;
    }

    @Override
    public Object getData(String column) {
        return null;
    }

    @Override
    public Object getData(int index) {
        return null;
    }

    @Override
    public boolean setData(String column, Object value) {
        return accepting;
    }

    @Override
    public boolean setData(int index, Object value) {
        return accepting;
    }

    @Override
    public Set<String> getColumns() {
        return EMPTY_SET;
    }
}
