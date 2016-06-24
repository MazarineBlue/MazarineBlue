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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mazarineblue.datasources.exceptions.HeaderMissing;
import static org.mazarineblue.datasources.util.SourceUtil.asSet;

/**
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
@RunWith(HierarchicalContextRunner.class)
public class MatrixSourceTest {

    private static final String IDENTIFIER = "";
    private static final String unavailableColumn = "oof";
    private static final String availableColumn = "foo";
    private static final String availableData = "data";
    private static final int availableIndex = 0;

    private MatrixSource source;

    @Before
    public void setup() {
        source = new AbstractMatrixSourceStub(IDENTIFIER);
    }

    public class GivenMatrixWithoutHeaders {

        @Test
        public void hasHeaders() {
            boolean hasHeaders = source.hasHeaders();
            assertEquals(false, hasHeaders);
        }

        @Test(expected = HeaderMissing.class)
        public void getData_String() {
            source.getData(null);
        }

        @Test(expected = HeaderMissing.class)
        public void setData_String() {
            source.setData(null, null);
        }

        @Test(expected = HeaderMissing.class)
        public void getColumns() {
            source.getColumns();
        }
    }

    public class GivenMatrixWithHeaders {

        @Before
        public void setup() {
            Map<String, Integer> map = new HashMap<>();
            map.put(availableColumn, availableIndex);
            source.setHeader(map);
        }

        @Test
        public void hasHeaders() {
            boolean hasHeaders = source.hasHeaders();
            assertEquals(true, hasHeaders);
        }

        @Test
        public void getData_UnavailableColumn() {
            Object data = source.getData(unavailableColumn);
            assertEquals(null, data);
        }

        @Test
        public void getData_AvailableColumn() {
            Object data = source.getData(availableColumn);
            assertEquals(availableData, data);
        }

        @Test
        public void setData_UnavailableColumn() {
            boolean set = source.setData(unavailableColumn, null);
            assertEquals(false, set);
        }

        @Test
        public void setData_AvailableColumn() {
            boolean set = source.setData(availableColumn, null);
            assertEquals(true, set);
        }

        @Test
        public void getColumns() {
            Set<String> columns = source.getColumns();
            assertEquals(asSet(availableColumn), columns);
        }
    }

    private static class AbstractMatrixSourceStub
            extends MatrixSource {

        private AbstractMatrixSourceStub(String sourceIdentifier) {
            super(sourceIdentifier);
        }

        @Override
        public Object getData(int index) {
            return availableData;
        }

        @Override
        public boolean setData(int index, Object value) {
            return true;
        }

        @Override
        public boolean hasNext() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void next() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void reset() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public List<String> getLineIdentifiers() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public String getLineIdentifier() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }
}
