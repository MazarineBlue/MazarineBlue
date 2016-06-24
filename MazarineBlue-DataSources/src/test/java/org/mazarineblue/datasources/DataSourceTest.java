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
import static java.util.Collections.EMPTY_LIST;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mazarineblue.datasources.exceptions.LineIdentifiersOutOfBoundsException;
import org.mazarineblue.datasources.exceptions.UnexpectedTypeAtColumn;
import org.mazarineblue.datasources.exceptions.UnexpectedTypeAtIndex;
import org.mazarineblue.datasources.util.DataSourceSpy;
import org.mazarineblue.datasources.util.DataSourceReturnsObjectStub;

/**
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
@RunWith(HierarchicalContextRunner.class)
public class DataSourceTest {

    @Test
    public void reset_NullListInput() {
        DataSourceSpy source = new DataSourceSpy("Spy", 2);
        source.reset(null);
        assertEquals(0, source.getNextCounter());
    }

    @Test
    public void reset_EmptyListInput() {
        DataSourceSpy source = new DataSourceSpy("Spy", 2);
        source.reset(EMPTY_LIST);
        assertEquals(0, source.getNextCounter());
    }

    @Test
    public void reset_LineIdentifiersInput() {
        DataSourceSpy source = new DataSourceSpy("Spy", 2);
        source.reset(asList(source.getLineIdentifier(2)));
        assertEquals(2, source.getNextCounter());
    }

    @Test(expected = LineIdentifiersOutOfBoundsException.class)
    public void reset_LineIdentifiersMismatch() {
        DataSourceSpy source = new DataSourceSpy("Spy", 2);
        source.reset(asList(new String[123]));
        assertEquals(3, source.getNextCounter());
    }

    @Test
    public void getDataUsingColumn_StringType_NullOutput() {
        DataSource source = new DataSourceReturnsObjectStub(null);
        String data = source.getData(null, String.class);
        assertEquals(null, data);
    }

    @Test(expected = UnexpectedTypeAtColumn.class)
    public void getDataUsingColumn_ReturnsInteger_ExpectsString_ThrowsException() {
        DataSource source = new DataSourceReturnsObjectStub(1);
        source.getData(null, String.class);
    }

    @Test
    public void getDataUsingColumn_ReturnString_ExpectsString() {
        DataSource source = new DataSourceReturnsObjectStub("foo");
        String data = source.getData(null, String.class);
        assertEquals("foo", data);
    }

    @Test
    public void getDataUsingIndex_StringType_NullOutput() {
        DataSource source = new DataSourceReturnsObjectStub(null);
        String data = source.getData(0, String.class);
        assertEquals(null, data);
    }

    @Test(expected = UnexpectedTypeAtIndex.class)
    public void getDataUsingIndex_ReturnsInteger_ExpectsString_ThrowsException() {
        DataSource source = new DataSourceReturnsObjectStub(1);
        source.getData(0, String.class);
    }

    @Test
    public void getDataUsingIndex_ReturnString_ExpectsString() {
        DataSource source = new DataSourceReturnsObjectStub("foo");
        String data = source.getData(0, String.class);
        assertEquals("foo", data);
    }
}
