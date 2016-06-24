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

import java.util.List;
import java.util.Set;
import org.mazarineblue.datasources.DataSource;

/**
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class DataSourceReturnsObjectStub
        implements DataSource {

    private final Object obj;

    public DataSourceReturnsObjectStub(Object obj) {
        this.obj = obj;
    }

    @Override
    public Object getData(String column) {
        return obj;
    }

    @Override
    public Object getData(int index) {
        return obj;
    }

    @Override
    public boolean hasNext() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void next() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<String> getSourceIdentifiers() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<String> getLineIdentifiers() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean setData(String column, Object value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean setData(int index, Object value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Set<String> getColumns() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
