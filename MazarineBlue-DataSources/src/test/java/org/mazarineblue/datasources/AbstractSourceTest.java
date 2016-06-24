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

import static java.util.Arrays.asList;
import static java.util.Collections.EMPTY_LIST;
import java.util.List;
import java.util.Set;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class AbstractSourceTest {

    private static final String IDENTIFIER = "Identifier";
    
    @Test
    public void getSourceIdentifier_NullIdentifier() {
        AbstractSource source = new AbstractSourceTestDouble(null);
        assertEquals(EMPTY_LIST, source.getSourceIdentifiers());
    }

    @Test
    public void getSourceIdentifier_StringIdentifiers() {
        AbstractSource source = new AbstractSourceTestDouble(IDENTIFIER);
        assertEquals(asList(IDENTIFIER), source.getSourceIdentifiers());
    }
    
    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void equals_Null() {
        AbstractSource source = new AbstractSourceTestDouble(IDENTIFIER);
        assertEquals(false, source.equals(null));
    }
    
    @Test
    public void equals_Object() {
        AbstractSource source = new AbstractSourceTestDouble(IDENTIFIER);
        assertEquals(false, source.equals(new Integer(1)));
    }
    
    @Test
    public void equals_BackedSource() {
        AbstractSource source = new AbstractSourceTestDouble(IDENTIFIER);
        assertEquals(false, source.equals(new BackedSource(source) {}));
    }
    
    @Test
    public void equals_AbstractSource() {
        AbstractSource source = new AbstractSourceTestDouble(IDENTIFIER);
        assertEquals(true, source.equals(new NullDataSource(IDENTIFIER)));
    }
    
    private static class AbstractSourceTestDouble
            extends AbstractSource {

        private AbstractSourceTestDouble(String sourceIdentifier) {
            super(sourceIdentifier);
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
        }

        @Override
        public List<String> getLineIdentifiers() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public String getLineIdentifier() {
            return "0";
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
