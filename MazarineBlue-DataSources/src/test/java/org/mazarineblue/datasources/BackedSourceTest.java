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
import java.util.ArrayList;
import static java.util.Arrays.asList;
import java.util.Collection;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mazarineblue.datasources.exceptions.ReachedEndOfDataSourceException;
import org.mazarineblue.datasources.util.DataSourceSpy;
import static org.mazarineblue.datasources.util.SourceUtil.asSet;

/**
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
@RunWith(HierarchicalContextRunner.class)
public class BackedSourceTest {

    private BackedSource source;
    private static final int N = 2;

    private static DataSourceSpy createSpy(String id, int rows, String column, Object value) {
        DataSourceSpy spy = new DataSourceSpy("Spy " + id, rows);
        spy.setData(column, value);
        return spy;
    }

    @Before
    public void setup() {
        source = new BackedSource() {};
    }

    @Test(expected = EmptyStackException.class)
    public void remove_EmptyList() {
        source.remove(null);
        assertEquals(0, source.size());
        assertEquals(true, source.isEmpty());
    }

    @Test
    public void remove_Null() {
        source.add(new DataSourceSpy("Spy", 1));
        boolean remove = source.remove(null);
        assertEquals(false, remove);
        assertEquals(1, source.size());
        assertEquals(false, source.isEmpty());
    }

    @Test
    public void remove_Source() {
        DataSourceSpy spy = new DataSourceSpy("Spy", 1);
        source.add(spy);
        boolean remove = source.remove(new NullDataSource(spy.getSourceIdentifier()));
        assertEquals(true, remove);
        assertEquals(0, source.size());
        assertEquals(true, source.isEmpty());
    }

    @Test(expected = EmptyStackException.class)
    public void pop() {
        source.pop();
    }

    @Test
    public void push_pop() {
        DataSourceSpy spy = new DataSourceSpy("Spy", 1);
        source.push(spy);
        DataSource src = source.pop();
        assertEquals(spy, src);
        assertEquals(0, source.size());
        assertEquals(true, source.isEmpty());
    }

    @Test
    public void push() {
        DataSourceSpy spy = new DataSourceSpy("Spy", 1);
        source.push(spy);
        assertEquals(1, source.size());
        assertEquals(false, source.isEmpty());
    }

    public class GivenEmptyBackend {

        @Test
        public void nothing() {
            assertEquals(0, source.size());
            assertEquals(true, source.isEmpty());
        }

        @Test
        public void hasNext() {
            assertEquals(false, source.hasNext());
        }

        @Test(expected = ReachedEndOfDataSourceException.class)
        public void next() {
            source.next();
        }

        @Test
        @SuppressWarnings("ObjectEqualsNull")
        public void equals_Null() {
            assertEquals(false, source.equals(null));
        }

        @Test
        @SuppressWarnings("IncompatibleEquals")
        public void equals_Object() {
            assertEquals(false, source.equals(1));
        }

        @Test
        @SuppressWarnings("IncompatibleEquals")
        public void equals_AbstractSource() {
            assertEquals(false, source.equals(new NullDataSource("")));
        }

        @Test
        public void equals_BackedSource() {
            source.add(new NullDataSource("foo"));
            BackedSource tmp = new BackedSource(new NullDataSource("foo")) {};
            assertEquals(true, source.equals(tmp));
        }
    }

    public class GivenBackendWithSpiesWithNoRows {

        DataSourceSpy spy1, spy2;

        @Before
        public void setup() {
            source.add(spy1 = createSpy("1", 1, "a", "b"));
            source.add(spy2 = createSpy("2", 0, "c", "d"));
        }

        @Test
        public void clear() {
            source.clear();
            assertEquals(0, source.size());
            assertEquals(true, source.isEmpty());
        }

        @Test
        public void hasNext() {
            assertEquals(false, source.hasNext());
        }

        @Test(expected = ReachedEndOfDataSourceException.class)
        public void next() {
            source.next();
            assertEquals(0, spy1.getNextCounter()); // Just to make sure!
        }
    }

    public class GivenBackendWithMultipleSpiesWithRows {

        private DataSourceSpy spy1, spy2, spy3;
        private Collection<DataSourceSpy> list;
        private String[] column, data;

        @Before
        public void setup() {
            list = new ArrayList<>(3);
            column = new String[3];
            data = new String[3];
            list.add(spy1 = createSpy("1", 1, column[0] = "a", data[0] = "b"));
            list.add(spy2 = createSpy("2", 1, column[1] = "c", data[1] = "d"));
            list.add(spy3 = createSpy("3", 1, column[2] = "a", data[2] = "f"));
            source.addAll(list);
        }

        @Test
        public void size() {
            assertEquals(3, source.size());
            assertEquals(false, source.isEmpty());
        }

        @Test
        public void stream() {
            source.stream().forEach(DataSource::next);
            assertEquals(true, source.stream().allMatch(src -> ((DataSourceSpy) src).getNextCounter() == 1));
            assertEquals(list.size(), source.size());
            assertEquals(false, source.isEmpty());
        }

        @Test
        public void parallelStream() {
            source.parallelStream().forEach(DataSource::next);
            assertEquals(true, source.stream().allMatch(src -> ((DataSourceSpy) src).getNextCounter() == 1));
            assertEquals(list.size(), source.size());
            assertEquals(false, source.isEmpty());
        }

        @Test
        public void hasNext() {
            assertEquals(true, source.hasNext());
        }

        @Test
        public void next() {
            source.next();
            assertEquals(1, spy1.getNextCounter());
        }

        @Test
        public void reset() {
            List<String> lineIdentifier = source.getLineIdentifiers();
            source.next();
            source.reset();
            assertEquals(lineIdentifier, source.getLineIdentifiers());
        }
        
        @Test
        public void getSourceIdentifiers() {
            List<String> sourceIdentifier = source.getSourceIdentifiers();
            assertEquals(asList("Spy 1", "Spy 2", "Spy 3"), sourceIdentifier);
        }

        @Test
        public void getColumns() {
            assertEquals(asSet("a", "c"), source.getColumns());
        }
        
        @Test
        public void getDataByColumn() {
            Object obj = source.getData(column[0]);
            assertEquals(data[0], obj);
        }
        
        @Test
        public void getDataByIndex() {
            Object obj = source.getData(0);
            assertEquals(data[0], obj);
        }
        @Test
        public void setDataByColumn() {
            source.setData(column[0], data[1]);
            assertEquals(data[1], source.getData(column[0]));
        }
        
        @Test
        public void setDataByIndex() {
            source.setData(0, data[1]);
            assertEquals(data[1], source.getData(0));
        }
    }
}
