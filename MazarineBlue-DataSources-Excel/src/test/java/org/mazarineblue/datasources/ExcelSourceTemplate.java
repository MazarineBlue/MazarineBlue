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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public abstract class ExcelSourceTemplate
        extends SourceTemplate {

    private class Entry {

        private final String column;
        private final Class type;

        private Entry(String column, Class type) {
            this.column = column;
            this.type = type;
        }
    };

    private final String sourceIdentifier = "Excel source";
    private final static String sheetName = "sheet";
    private final Random rand = new Random(System.currentTimeMillis());
    private final String invalidColumn = "Not Found";
    private final List<Entry> columnTypes = new ArrayList() {
        {
            add(new Entry("Blank", String.class));
            add(new Entry("Date", Date.class));
            add(new Entry("Double", Double.class));
            add(new Entry("Formula", String.class));
            add(new Entry("Boolean", Boolean.class));
            add(new Entry("Error", Byte.class));
            add(new Entry("String", String.class));
        }
    };
    private final int firstRow = 1;
    private final int lastRow = 10;

    @Override
    protected DataSource getEmptySheet() {
        boolean withHeader = hasHeader();
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet(sheetName);
        if (withHeader)
            setHeaderRow(sheet.createRow(1));
        return new ExcelSource(sourceIdentifier, sheet, withHeader);
    }

    // <editor-fold defaultstate="collapsed" desc="Old code">
    private void setHeaderRow(Row header) {
        for (int i = 0; i < columnTypes.size(); ++i) {
            Cell cell = header.createCell(i, Cell.CELL_TYPE_STRING);
            String value = columnTypes.get(i).column;
            cell.setCellValue(value);
        }
    }

    private void setDataRow(Row row) {
        byte[] b = new byte[1];
        rand.nextBytes(b);
        setDataColumn(row, "Blank", "");
        setDataColumn(row, "Date", new Date());
        setDataColumn(row, "Double", rand.nextDouble());
        setDataColumn(row, "Formula", "=A2");
        setDataColumn(row, "Boolean", rand.nextBoolean());
        setDataColumn(row, "Error", b[0]);
        setDataColumn(row, "String", "Bla bla " + b[0]);
    }

    private void setDataColumn(Row row, String column, Object obj) {
        int index = getIndex(column);
        Cell cell = row.createCell(index);
        setCell(column, cell, obj);
    }

    private int getIndex(String column) {
        for (int i = 0; i < columnTypes.size(); ++i) {
            String field = columnTypes.get(i).column;
            if (field.equals(column))
                return i;
        }
        throw new IllegalStateException("Index not found for column " + column);
    }

    private void setCell(String column, Cell cell, Object obj) {
        switch (column) {
            case "Blank":
                cell.setCellValue("");
                break;
            case "Date": {
                setDateFormat(cell);
                cell.setCellValue((Date) obj);
                break;
            }
            case "Double":
                cell.setCellValue((Double) obj);
                break;
            case "Formula":
                cell.setCellValue((String) obj);
                break;
            case "Boolean":
                cell.setCellValue((Boolean) obj);
                break;
            case "Error":
                cell.setCellErrorValue((byte) 42);
                break;
            case "String":
                cell.setCellValue((String) obj);
                break;
            default:
                throw new IllegalArgumentException(
                        "Column " + column + " is not supported");
        }
    }

    private void setDateFormat(Cell cell) {
        Sheet sheet = cell.getSheet();
        Workbook workbook = sheet.getWorkbook();

        CreationHelper helper = workbook.getCreationHelper();
        short dateFormat = helper.createDataFormat().getFormat(
                "yyyy-dd-MM hh:mm:ss");

        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setDataFormat(dateFormat);
        cell.setCellStyle(cellStyle);
    }
    // </editor-fold>

    @Override
    protected DataSource getFilledSheet() {
        boolean withHeader = hasHeader();
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet(sheetName);
        int i = firstRow - 1;
        if (withHeader)
            setHeaderRow(sheet.createRow(++i));
        while (i < lastRow)
            setDataRow(sheet.createRow(++i));
        return new ExcelSource(sourceIdentifier, sheet, withHeader);
    }

    @Override
    protected String getSourceIdentifier() {
        return sourceIdentifier;
    }

    @Override
    protected String[] getLineIdentifiers() {
        int start = firstRow;
        if (hasHeader())
            ++start;
        int n = lastRow - start + 1;
        String[] arr = new String[n + 1];
        for (int i = 0; i < n; ++i)
            arr[i+1] = sheetName + ":" + (i + start);
        arr[0] = sheetName + ":-1";
        return arr;
    }

    protected abstract boolean hasHeader();

    @Override
    protected String getInvalidDataColumn() {
        return invalidColumn;
    }

    @Override
    protected Integer getInvalidDataIndex() {
        return columnTypes.size();
    }

    @Override
    protected Map<String, Class> getDataColumnRequests() {
        Map<String, Class> map = new HashMap();
        for (Entry entry : columnTypes)
            map.put(entry.column, entry.type);
        return map;
    }

    @Override
    protected Map<Integer, Class> getDataIndexRequests() {
        Map<Integer, Class> map = new HashMap();
        for (int i = 0; i < columnTypes.size(); ++i) {
            Class clazz = columnTypes.get(i).type;
            map.put(i, clazz);
        }
        return map;
    }
}
