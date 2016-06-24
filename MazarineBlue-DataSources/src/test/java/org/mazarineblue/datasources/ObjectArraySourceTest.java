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
import java.util.Set;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mazarineblue.datasources.util.AbstractArraySourceTest;
import static org.mazarineblue.datasources.util.SourceUtil.asSet;

/**
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
@RunWith(HierarchicalContextRunner.class)
public class ObjectArraySourceTest
        extends AbstractArraySourceTest {

    private static final String IDENTIFIER = "";
    private ObjectArraySource source;

    @Override
    protected void next() {
        source.next();
    }

    @Override
    protected void reset() {
        source.reset();
    }

    @Before
    public void setup() {
        source = new ObjectArraySource(IDENTIFIER);
        setup(source);
    }

    @Test
    @Override
    public void setDataUsingIndexThenColumn() {
        Set<String> expected = asSet("0", available.getColumn());
        setDataUsingIndexThenColumnHelper(true, true, expected);
    }

    @Test
    @Override
    public void setDataUsingColumnThenIndex() {
        String[] expected = new String[]{available.getColumn()};
        setDataUsingColumnThenIndexHelper(true, true, expected);
    }

    public class GivenInitializedDataSet {

        @Before
        public void setup() {
            next();
            setData(available);
        }

        @Test
        public void containsKey_UnavailableKey_ReturnsFalse() {
            boolean containsKey = source.containsColumn(unavailable.getColumn());
            assertEquals(false, containsKey);
        }

        @Test
        public void containsKey_AvailableKey_ReturnsTrue() {
            boolean containsKey = source.containsColumn(available.getColumn());
            assertEquals(true, containsKey);
        }

        @Test
        public void containsValue_UnavailableValue_ReturnsFalse() {
            boolean containsKey = source.containsData(unavailable.getData());
            assertEquals(false, containsKey);
        }

        @Test
        public void containsValue_AvaibleValue_ReturnsTrue() {
            boolean containsKey = source.containsData(available.getData());
            assertEquals(true, containsKey);
        }
    }
}
