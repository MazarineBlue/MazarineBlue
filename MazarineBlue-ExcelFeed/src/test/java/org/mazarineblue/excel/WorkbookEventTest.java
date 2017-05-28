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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.excel.events.WorkbookEvent;
import org.mazarineblue.excel.util.WorkbookBuilder;

public class WorkbookEventTest {

    private WorkbookEvent a;

    @Before
    public void setup() {
        WorkbookBuilder builder = defaultBuilder();
        a = new WorkbookEvent(builder.getWorkbook());
    }

    private static WorkbookBuilder defaultBuilder() {
        WorkbookBuilder builder = new WorkbookBuilder();
        addDefaultRow(builder);
        addDefaultSheet(builder, "second");
        return builder;
    }

    private static void addDefaultSheet(WorkbookBuilder builder, String sheet) {
        builder.switchToSheet(sheet);
        addDefaultRow(builder);
        builder.switchToMainSheet();
    }

    private static void addDefaultRow(WorkbookBuilder builder) {
        builder.addRow("foo", 123, "arg2");
        builder.setCellFormula(1, 3, "SUM(A1:A2)");
        builder.clearCell(1, 4);
        builder.setCell(1, 5, true);
        builder.setCellErrorValue(1, 6, (byte) 0);
    }

    @After
    public void teardown() {
        a = null;
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void equals_Null() {
        assertFalse(a.equals(null));
    }

    @Test
    @SuppressWarnings("IncompatibleEquals")
    public void equals_DifferentClass() {
        assertFalse(a.equals(""));
    }

    @Test
    public void hashCode_DifferentActiveSheets() {
        WorkbookBuilder builder = defaultBuilder();
        builder.switchToSheet("second");
        WorkbookEvent b = new WorkbookEvent(builder.getWorkbook());
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void equals_DifferentActiveSheets() {
        WorkbookBuilder builder = defaultBuilder();
        builder.switchToSheet("second");
        WorkbookEvent b = new WorkbookEvent(builder.getWorkbook());
        assertNotEquals(a, b);
    }

    @Test
    public void hashCode_DifferentNumberSheets() {
        WorkbookBuilder builder = defaultBuilder();
        addDefaultSheet(builder, "third");
        WorkbookEvent b = new WorkbookEvent(builder.getWorkbook());
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void equals_DifferentNumberSheets() {
        WorkbookBuilder builder = defaultBuilder();
        addDefaultSheet(builder, "third");
        WorkbookEvent b = new WorkbookEvent(builder.getWorkbook());
        assertNotEquals(a, b);
    }

    @Test
    public void hashCode_DifferentSheetNames() {
        WorkbookBuilder builder = new WorkbookBuilder();
        addDefaultRow(builder);
        addDefaultSheet(builder, "third");
        WorkbookEvent b = new WorkbookEvent(builder.getWorkbook());
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void equals_DifferentSheetNames() {
        WorkbookBuilder builder = new WorkbookBuilder();
        addDefaultRow(builder);
        addDefaultSheet(builder, "third");
        WorkbookEvent b = new WorkbookEvent(builder.getWorkbook());
        assertNotEquals(a, b);
    }

    @Test
    public void hashCode_DifferentFirstRow() {
        WorkbookBuilder builder = new WorkbookBuilder();
        addDefaultRow(builder);
        addDefaultRow(builder);
        addDefaultSheet(builder, "second");
        WorkbookEvent b = new WorkbookEvent(builder.getWorkbook());
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void equals_DifferentFirstRow() {
        WorkbookBuilder builder = new WorkbookBuilder();
        addDefaultRow(builder);
        addDefaultRow(builder);
        builder.removeRow(1);
        addDefaultSheet(builder, "second");
        WorkbookEvent b = new WorkbookEvent(builder.getWorkbook());
        assertNotEquals(a, b);
    }

    @Test
    public void hashCode_DifferentLastRow() {
        WorkbookBuilder builder = new WorkbookBuilder();
        addDefaultRow(builder);
        addDefaultRow(builder);
        addDefaultSheet(builder, "second");
        WorkbookEvent b = new WorkbookEvent(builder.getWorkbook());
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void equals_DifferentLastRow() {
        WorkbookBuilder builder = new WorkbookBuilder();
        addDefaultRow(builder);
        addDefaultRow(builder);
        addDefaultSheet(builder, "second");
        WorkbookEvent b = new WorkbookEvent(builder.getWorkbook());
        assertNotEquals(a, b);
    }

    @Test
    public void hashCode_DifferentFirstCell() {
        WorkbookBuilder builder = new WorkbookBuilder();
        builder.setCell(1, 2, 123);
        addDefaultSheet(builder, "second");
        WorkbookEvent b = new WorkbookEvent(builder.getWorkbook());
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void equals_DifferentFirstCell() {
        WorkbookBuilder builder = new WorkbookBuilder();
        builder.setCell(1, 1, 123);
        addDefaultSheet(builder, "second");
        WorkbookEvent b = new WorkbookEvent(builder.getWorkbook());
        assertNotEquals(a, b);
    }

    @Test
    public void hashCode_DifferentLastCell() {
        WorkbookBuilder builder = defaultBuilder();
        builder.setCell(1, 3, "arg3");
        WorkbookEvent b = new WorkbookEvent(builder.getWorkbook());
        assertNotEquals(a, b);
    }

    @Test
    public void equals_DifferentLastCell() {
        WorkbookBuilder builder = defaultBuilder();
        builder.setCell(1, 100, "arg3");
        WorkbookEvent b = new WorkbookEvent(builder.getWorkbook());
        assertNotEquals(a, b);
    }

    @Test
    public void hashCode_DifferentCellType() {
        WorkbookBuilder builder = defaultBuilder();
        builder.setCell(1, 1, "arg1");
        WorkbookEvent b = new WorkbookEvent(builder.getWorkbook());
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void equals_DifferentCellType() {
        WorkbookBuilder builder = defaultBuilder();
        builder.setCell(1, 1, "arg1");
        WorkbookEvent b = new WorkbookEvent(builder.getWorkbook());
        assertNotEquals(a, b);
    }

    @Test
    public void hashCode_DifferentNumericContent() {
        WorkbookBuilder builder = defaultBuilder();
        builder.setCell(1, 1, 321);
        WorkbookEvent b = new WorkbookEvent(builder.getWorkbook());
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void equals_DifferentNumericContent() {
        WorkbookBuilder builder = defaultBuilder();
        builder.setCell(1, 1, 321);
        WorkbookEvent b = new WorkbookEvent(builder.getWorkbook());
        assertNotEquals(a, b);
    }

    @Test
    public void hashCode_DifferentStringContent() {
        WorkbookBuilder builder = defaultBuilder();
        builder.setCell(1, 2, "arg3");
        WorkbookEvent b = new WorkbookEvent(builder.getWorkbook());
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void equals_DifferentStringContent() {
        WorkbookBuilder builder = defaultBuilder();
        builder.setCell(1, 2, "arg3");
        WorkbookEvent b = new WorkbookEvent(builder.getWorkbook());
        assertNotEquals(a, b);
    }

    @Test
    public void hashCode_DifferentFormula() {
        WorkbookBuilder builder = defaultBuilder();
        builder.setCellFormula(1, 3, "SUM(A1:Z9)");
        WorkbookEvent b = new WorkbookEvent(builder.getWorkbook());
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void equals_DifferentFormula() {
        WorkbookBuilder builder = defaultBuilder();
        builder.setCellFormula(1, 3, "SUM(A1:Z9)");
        WorkbookEvent b = new WorkbookEvent(builder.getWorkbook());
        assertNotEquals(a, b);
    }

    @Test
    public void hashCode_DifferentBoolean() {
        WorkbookBuilder builder = defaultBuilder();
        builder.setCell(1, 5, false);
        WorkbookEvent b = new WorkbookEvent(builder.getWorkbook());
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void equals_DifferentBoolean() {
        WorkbookBuilder builder = defaultBuilder();
        builder.setCell(1, 5, false);
        WorkbookEvent b = new WorkbookEvent(builder.getWorkbook());
        assertNotEquals(a, b);
    }

    @Test
    public void hashCode_DifferentErrorValue() {
        WorkbookBuilder builder = defaultBuilder();
        builder.setCellErrorValue(1, 6, (byte) 42);
        WorkbookEvent b = new WorkbookEvent(builder.getWorkbook());
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void equals_DifferentErrorValue() {
        WorkbookBuilder builder = defaultBuilder();
        builder.setCellErrorValue(1, 6, (byte) 42);
        WorkbookEvent b = new WorkbookEvent(builder.getWorkbook());
        assertNotEquals(a, b);
    }

    @Test
    public void hashCode_IdenticalContent() {
        WorkbookBuilder builder = defaultBuilder();
        WorkbookEvent b = new WorkbookEvent(builder.getWorkbook());
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void equals_IdenticalContent() {
        WorkbookBuilder builder = defaultBuilder();
        WorkbookEvent b = new WorkbookEvent(builder.getWorkbook());
        assertEquals(a, b);
    }
}
