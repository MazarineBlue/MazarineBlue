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
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mazarineblue.datasources.util.DataSourceSpy;

/**
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
@RunWith(HierarchicalContextRunner.class)
public class SourceWrapperTest {

    private static final String IDENTIFIER = "Source";
    private static final String unavailableColumn = "oof";
    private static final String availableColumn = "foo";
    private static final String availableData = "data";
    private static final int availableIndex = 0;

    private DataSourceSpy spy;
    private SourceWrapper wrapper;

    @Before
    public void setup() {
        spy = new DataSourceSpy("Spy", 1);
    }

    public class GivenEmptyWrapper {

        @Before
        public void setup() {
            wrapper = new SourceWrapper();
        }

        @Test
        public void nothing() {
            assertEquals(false, wrapper.isSet());
            assertEquals(0, wrapper.size());
        }
        
        @Test
        public void setSource() {
            wrapper.setSource(spy);
            assertEquals(true, wrapper.isSet());
            assertEquals(1, wrapper.size());
        }
    }

    public class GivenInitializedWrapper {

        @Before
        public void setup() {
            wrapper = new SourceWrapper(spy);
        }

        @Test
        public void nothing() {
            assertEquals(true, wrapper.isSet());
            assertEquals(1, wrapper.size());
        }
        
        @Test
        public void resetSource() {
            wrapper.resetSource();
            assertEquals(false, wrapper.isSet());
            assertEquals(0, wrapper.size());
        }
    }
}
