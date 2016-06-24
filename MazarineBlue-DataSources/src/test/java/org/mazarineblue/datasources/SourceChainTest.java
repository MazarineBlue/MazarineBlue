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
import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mazarineblue.datasources.exceptions.ReachedEndOfDataSourceException;
import org.mazarineblue.datasources.util.DataSourceSpy;

/**
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
@RunWith(HierarchicalContextRunner.class)
public class SourceChainTest {

    private SourceChain source;

    public class GivenEmptyChain {

        @Before
        public void setup() {
            source = new SourceChain();
        }

        @Test
        public void add() {
            source.add(new DataSourceSpy("1", 1));
        }

        @Test
        public void hasNext() {
            boolean hasNext = source.hasNext();
            assertEquals(false, hasNext);
        }

        @Test(expected = ReachedEndOfDataSourceException.class)
        public void next() {
            source.next();
        }
    }

    public class GivenFilledChain {

        private static final int N = 3;
        private DataSourceSpy spy1, spy2, spy3, spy4, spy5;
        private int sum = 1;

        @Before
        public void setup() {
            source = new SourceChain();
            source.add(spy1 = createSpy(0, 0));
            source.add(spy2 = createSpy(1, 2));
            source.add(spy3 = createSpy(2, 1));
            source.add(spy4 = createSpy(3, 3));
            source.add(spy5 = createSpy(4, 0));
        }
        
        private DataSourceSpy createSpy(int id, int rows) {
            if (rows > 0)
                sum *= rows;
            return new DataSourceSpy("" + id, rows);
        }

        @Test
        public void hasNext() {
            boolean hasNext = source.hasNext();
            assertEquals(true, hasNext);
        }
        
        @Test
        public void reset() {
            List<String> expected = source.getLineIdentifiers();
            for (int i = 0; i < sum; ++i)
                source.next();
            source.reset();
            assertEquals(0, spy1.getNextCounter());
            assertEquals(6, spy2.getNextCounter());
            assertEquals(3, spy3.getNextCounter());
            assertEquals(3, spy4.getNextCounter());
            assertEquals(0, spy5.getNextCounter());
            assertEquals(expected, source.getLineIdentifiers());
        }
        
        @Test(expected = ReachedEndOfDataSourceException.class)
        public void next() {
            for (int i = 0; i < sum + 1; ++i)
                source.next();
        }
    }
}
