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
import java.util.List;
import java.util.Set;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mazarineblue.datasources.exceptions.RowIndexOutOfBoundsException;

/**
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
@RunWith(HierarchicalContextRunner.class)
public class ArraySourceTest {

    private static final String IDENTIFIER = "source";
    private ArraySource source;

    public class GivenDefaultArraySource
            extends GivenUninitilisedArraySource {

        @Before
        @Override
        public void setup() {
            source = new AbstractArraySourceTestDouble(IDENTIFIER);
        }
    }

    public class GivenUninitilisedArraySource {

        @Before
        public void setup() {
            source = new AbstractArraySourceTestDouble(IDENTIFIER, false);
        }

        @Test
        public void hasNext() {
            boolean hasNext = source.hasNext();
            assertEquals(true, hasNext);
        }

        @Test
        public void next() {
            source.next();
            assertEquals(true, source.isInitilized());
        }

        @Test
        public void reset() {
            source.next();
            source.reset();
            assertEquals(false, source.isInitilized());
        }

        @Test
        public void getLineIdentifier_WithoutCallingNext_ThrowsException() {
            assertEquals(asList(IDENTIFIER + ":-1"), source.getLineIdentifiers());
        }

        @Test
        public void getLineIdentifier_AfterCallingNext_ReturnsEmptyIdentifier() {
            source.next();
            List<String> lineIdentifiers = source.getLineIdentifiers();
            assertEquals(asList(IDENTIFIER + ":0"), lineIdentifiers);
        }
    }

    public class GivenInitializedArraySource {

        @Before
        public void setup() {
            source = new AbstractArraySourceTestDouble(IDENTIFIER, true);
        }

        @Test
        public void hasNext() {
            boolean hasNext = source.hasNext();
            assertEquals(false, hasNext);
        }

        @Test(expected = RowIndexOutOfBoundsException.class)
        public void next() {
            source.next();
        }

        @Test
        public void reset() {
            source.reset();
            assertEquals(false, source.isInitilized());
        }

        @Test
        public void getLineIdentifier_WithoutCallingNext_ReturnsEmptyIdentifier() {
            List<String> lineIdentifiers = source.getLineIdentifiers();
            assertEquals(asList(IDENTIFIER + ":0"), lineIdentifiers);
        }
    }

    private static class AbstractArraySourceTestDouble
            extends ArraySource {

        private AbstractArraySourceTestDouble(String sourceIdentifier) {
            super(sourceIdentifier);
        }

        private AbstractArraySourceTestDouble(String sourceIdentifier, boolean initialize) {
            super(sourceIdentifier, initialize);
        }

        @Override
        public int getIndex(String column) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Object getData(String column) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Object getData(int index) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public boolean setData(String column, Object value) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public boolean setData(int index, Object value) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Set<String> getColumns() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }
}
