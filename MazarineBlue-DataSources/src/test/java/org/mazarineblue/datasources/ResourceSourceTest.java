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

import java.util.ListResourceBundle;
import java.util.ResourceBundle;
import java.util.Set;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.datasources.util.AbstractArraySourceTest;
import static org.mazarineblue.datasources.util.SourceUtil.asSet;

/**
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class ResourceSourceTest
        extends AbstractArraySourceTest {

    private ResourceSource source;

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
        ResourceBundle bundle = new ListResourceBundle() {

            @Override
            protected Object[][] getContents() {
                return new Object[][]{{available.getColumn(), available.getData()}};
            }
        };
        source = new ResourceSource(null, bundle);
        setup(source);
    }

    @Test
    @Override
    public void getDataByIndex_AvailableValue_ReturnsObject() {
        next();
        Object data = source.getData(0);
        Assert.assertEquals(null, data);
    }

    @Test
    @Override
    public void setDataUsingIndexThenColumn() {
        next();
        Set<String> expected = asSet(available.getColumn());
        boolean flagSetDataIndex = source.setData(0, available.getData());
        boolean flagSetDataColumn = setData(available);
        Set<String> columns = source.getColumns();
        assertEquals(expected, columns);
        assertEquals(false, flagSetDataIndex);
        assertEquals(false, flagSetDataColumn);
    }

    @Test
    @Override
    public void setDataUsingColumnThenIndex() {
        next();
        Set<String> expected = asSet(available.getColumn());
        boolean flagSetDataColumn = setData(available);
        boolean flagSetDataIndex = source.setData(0, available.getData());
        Set<String> columns = source.getColumns();
        assertEquals(expected, columns);
        assertEquals(false, flagSetDataColumn);
        assertEquals(false, flagSetDataIndex);
    }
}
