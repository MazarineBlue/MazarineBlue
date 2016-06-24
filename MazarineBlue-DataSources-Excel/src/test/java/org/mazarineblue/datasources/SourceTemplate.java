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

import static java.util.Arrays.asList;
import java.util.Map;
import java.util.Map.Entry;
import org.junit.Assert;
import org.junit.Test;
import org.mazarineblue.datasources.exceptions.RowIndexOutOfBoundsException;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public abstract class SourceTemplate {

    protected abstract DataSource getEmptySheet();

    protected abstract DataSource getFilledSheet();

    protected abstract String getSourceIdentifier();

    protected abstract String[] getLineIdentifiers();

    protected abstract String getInvalidDataColumn();

    protected abstract Integer getInvalidDataIndex();

    protected abstract Map<String, Class> getDataColumnRequests();

    protected abstract Map<Integer, Class> getDataIndexRequests();

    @Test
    public void testEmptySource() {
        DataSource source = getEmptySheet();
        String[] line = getLineIdentifiers();
        Assert.assertEquals(false, source.hasNext());
        Assert.assertEquals(asList(line[0]), source.getLineIdentifiers());
    }

    @Test
    public void testNavigation() {
        DataSource source = getFilledSheet();
        String[] line = getLineIdentifiers();
        for (int i = 0; i < line.length - 1; ++i) {
            Assert.assertEquals(true, source.hasNext());
            Assert.assertEquals(asList(line[i]), source.getLineIdentifiers());
            source.next();
        }
        Assert.assertEquals(false, source.hasNext());
        Assert.assertEquals(asList(line[line.length - 1]), source.getLineIdentifiers());
    }

    @Test(expected = RowIndexOutOfBoundsException.class)
    public void next_CallingOneToMannyTime() {
        DataSource source = getFilledSheet();
        String[] line = getLineIdentifiers();
        for (int i = 0; i < line.length; ++i)
            source.next();
    }

    @Test
    public void testRest() {
        DataSource source = getFilledSheet();
        String[] line = getLineIdentifiers();
        for (int i = 0; i < line.length -1; ++i)
            source.next();
        source.reset();
        Assert.assertEquals(true, source.hasNext());
    }

    @Test
    public void testDataWithInvalidColumnArgument() {
        DataSource source = getFilledSheet();
        source.next();
        String column = getInvalidDataColumn();
        Object actual = source.getData(column);
        Assert.assertEquals(null, actual);
    }

    @Test
    public void testDataWithInvalidIndexArgument() {
        DataSource source = getFilledSheet();
        source.next();
        Integer index = getInvalidDataIndex();
        Object actual = source.getData(index);
        Assert.assertEquals(null, actual);
    }

    @Test
    public void testDataWithValidColumnArgument() {
        DataSource source = getFilledSheet();
        source.next();
        Map<String, Class> map = getDataColumnRequests();
        for (Entry<String, Class> entry : map.entrySet()) {
            String column = entry.getKey();
            Class clazz = entry.getValue();
            Object data = source.getData(column);
            boolean actual = data.getClass().isAssignableFrom(clazz);
            Assert.assertEquals(true, actual);
        }
    }

    @Test
    public void testDataWithIndexArgument() {
        DataSource source = getFilledSheet();
        source.next();
        Map<Integer, Class> map = getDataIndexRequests();
        for (Entry<Integer, Class> entry : map.entrySet()) {
            Integer index = entry.getKey();
            Class clazz = entry.getValue();
            Object data = source.getData(index);
            boolean actual = data.getClass().isAssignableFrom(clazz);
            Assert.assertEquals(true, actual);
        }
    }
}
