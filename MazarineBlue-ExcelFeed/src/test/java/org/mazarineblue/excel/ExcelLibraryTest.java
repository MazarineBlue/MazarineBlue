/*
 * Copyright (c) 2016 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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
package org.mazarineblue.excel;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.excel.events.ExecuteFeedEvent;
import org.mazarineblue.excel.util.TestInvoker;
import org.mazarineblue.variablestore.events.EndVariableScopeEvent;
import org.mazarineblue.variablestore.events.StartVariableScopeEvent;

public class ExcelLibraryTest {

    private ExcelLibrary library;

    @Before
    public void setup() {
        library = new ExcelLibrary();
    }

    @After
    public void teardown() {
        library = null;
    }

    @Test
    public void importSheet() {
        TestInvoker invoker = new TestInvoker();
        library.importSheet(invoker, "foo");
        assertEquals(new ExecuteFeedEvent("foo"), invoker.next());
    }

    @Test
    public void callSheet() {
        TestInvoker invoker = new TestInvoker();
        library.callSheet(invoker, "foo");
        assertEquals(new StartVariableScopeEvent(), invoker.next());
        assertEquals(new ExecuteFeedEvent("foo"), invoker.next());
        assertEquals(new EndVariableScopeEvent(), invoker.next());
    }
}
