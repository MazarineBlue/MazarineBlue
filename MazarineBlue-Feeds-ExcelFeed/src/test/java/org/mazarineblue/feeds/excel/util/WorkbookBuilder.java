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
package org.mazarineblue.feeds.excel.util;

import java.util.HashMap;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_BLANK;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.mazarineblue.eventdriven.Feed;
import org.mazarineblue.feeds.excel.ExcelFeed;

public class WorkbookBuilder {

    private static final String MAIN_SHEET = "main";
    private final Workbook workbook = new HSSFWorkbook();
    private final Map<String, SheetBuilder> sheets = new HashMap<>(4);
    private SheetBuilder sheet;

    public WorkbookBuilder() {
        sheets.put(MAIN_SHEET, sheet = new SheetBuilder(MAIN_SHEET));
    }

    public Feed getFeed() {
        return new ExcelFeed(sheets.get(MAIN_SHEET).sheet);
    }

    public Workbook getWorkbook() {
        return workbook;
    }

    public void switchToMainSheet() {
        sheet = sheets.get(MAIN_SHEET);
        setActiveSheet(MAIN_SHEET);
    }

    public void switchToSheet(String tab) {
        sheet = sheets.get(tab);
        if (sheet == null)
            sheets.put(tab, sheet = new SheetBuilder(tab));
        setActiveSheet(tab);
    }

    private void setActiveSheet(String tab) {
        int index = workbook.getSheetIndex(tab);
        workbook.setActiveSheet(index);
    }

    public final void skipRows(int amount) {
        sheet.skipRows(amount);
    }

    public void removeRow(int row) {
        sheet.removeRow(row);
    }

    public void addRow(Object keyword, Object... arr) {
        sheet.addRow(keyword, arr);
    }

    public void setCell(int row, int cell, Object value) {
        sheet.setCell(row, cell, value);
    }

    public void setCellFormula(int row, int cell, String formula) {
        sheet.setCellFormula(row, cell, formula);
    }

    public void setCellErrorValue(int row, int cell, byte value) {
        sheet.setCellErrorValue(row, cell, value);
    }

    public void clearCell(int row, int cell) {
        sheet.clearCell(row, cell);
    }

    private class SheetBuilder {

        private final Sheet sheet;
        private Integer index;

        SheetBuilder(String tab) {
            this.sheet = workbook.createSheet(tab);
            this.index = 0;
        }

        final void skipRows(int amount) {
            index += amount;
        }

        void removeRow(int index) {
            Row row = sheet.getRow(index);
            sheet.removeRow(row);
        }

        void addRow(Object keyword, Object... arr) {
            Row row = sheet.createRow(++index);
            if (keyword != null)
                setCell(index, 0, keyword);
            for (int i = 0; i < arr.length; ++i)
                setCell(index, i + 1, arr[i]);
        }

        void setCell(int rowIndex, int cellIndex, Object value) {
            Cell cell = getCell(rowIndex, cellIndex);
            if (value instanceof String)
                cell.setCellValue((String) value);
            else if (value instanceof Boolean)
                cell.setCellValue((Boolean) value);
            else if (value instanceof Number)
                cell.setCellValue(((Number) value).doubleValue());
            else
                throw new UnsupportedOperationException("Not yet supported");
        }

        void setCellFormula(int rowIndex, int cellIndex, String formula) {
            getCell(rowIndex, cellIndex).setCellFormula(formula);
        }

        void setCellErrorValue(int rowIndex, int cellIndex, byte value) {
            getCell(rowIndex, cellIndex).setCellErrorValue(value);
        }

        void clearCell(int rowIndex, int cellIndex) {
            getCell(rowIndex, cellIndex).setCellType(CELL_TYPE_BLANK);
        }

        private Cell getCell(int rowIndex, int cellIndex) {
            Row row = sheet.getRow(rowIndex);
            if (row == null)
                row = sheet.createRow(rowIndex);
            return row.createCell(cellIndex);
        }
    }
}
