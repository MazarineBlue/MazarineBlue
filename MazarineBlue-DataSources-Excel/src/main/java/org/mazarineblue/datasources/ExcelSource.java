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

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.mazarineblue.datasources.exceptions.IllegalSourceStateException;
import org.mazarineblue.datasources.exceptions.RowIndexOutOfBoundsException;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class ExcelSource
        extends MatrixSource {

    private final Sheet sheet;
    private final int lastRow;
    private int firstRow = -1;
    private Row currentRow = null;
    private Integer index;

    static private Map<String, Integer> getHeaders(Sheet sheet) {
        int firstRow = sheet.getFirstRowNum();
        Row header = sheet.getRow(firstRow);
        int i = header.getFirstCellNum();
        int n = header.getLastCellNum();

        Map<String, Integer> headers = new HashMap<>();
        for (; i < n; ++i) {
            Cell cell = header.getCell(i);
            if (cell == null)
                continue;
            String columnName = cell.getStringCellValue();
            headers.put(columnName, i);
        }
        return headers;
    }

    /**
     * Opens a sheet of an excel file as a DataSource.
     *
     * @param sourceIdentifier the name of this source
     * @param sheet the excel sheet specified sheet.
     * @param hasHeader true when the first line contains the header
     */
    public ExcelSource(String sourceIdentifier, Sheet sheet, boolean hasHeader) {
        super(sourceIdentifier);
        if (hasHeader)
            setHeader(getHeaders(sheet));
        this.sheet = sheet;
        lastRow = sheet.getLastRowNum();
    }

    /**
     * Opens a sheet of an excel file as a DataSource.
     *
     * The source does not contain a header line.
     *
     * @param sourceIdentifier the name of this source
     * @param sheet the excel sheet specified sheet.
     */
    public ExcelSource(String sourceIdentifier, Sheet sheet) {
        super(sourceIdentifier);
        this.sheet = sheet;
        lastRow = sheet.getLastRowNum();
    }

    @Override
    @SuppressWarnings("LocalVariableHidesMemberVariable")
    public boolean hasNext() {
        int index;
        if (firstRow < 0) {
            index = sheet.getFirstRowNum();
            if (hasHeaders()) {
                ++index;
                return index <= lastRow;
            } else {
                Row row = getRow(index);
                return row != null;
            }
        } else
            index = getNextContentRow();
        return index <= lastRow;
    }

    @Override
    @SuppressWarnings({"NestedAssignment", "ValueOfIncrementOrDecrementUsed"})
    public void next() {
        if (needsInitilization() == false && index == lastRow)
            throw new RowIndexOutOfBoundsException(index + 1);
        try {
            if (needsInitilization()) {
                index = firstRow = getFirstContentRow();
                currentRow = getRow(firstRow);
            } else if (index < lastRow)
                currentRow = getRow(++index);
        } catch (RuntimeException ex) {
            Logger logger = Logger.getLogger(ExcelSource.class.getName());
            logger.log(Level.SEVERE, null, ex);
        }
    }

    private boolean needsInitilization() {
        return firstRow < 0;
    }

    @SuppressWarnings("LocalVariableHidesMemberVariable")
    private int getFirstContentRow() {
        int index = sheet.getFirstRowNum();
        if (hasHeaders())
            ++index;
        return index;
    }

    private int getNextContentRow() {
        return index + 1;
    }

    private Row getRow(int index) {
        return index > lastRow ? null : sheet.getRow(index);
    }

    @Override
    public void reset() {
        firstRow = -1;
        currentRow = null;
        index = null;
    }

    @Override
    public String getLineIdentifier() {
        return lineIdentifier();
    }

    private String lineIdentifier() {
        String lineIdentifier = sheet.getSheetName() + ":";
        lineIdentifier += (index == null || index > lastRow || lastRow == 0) ? -1 : index;
        return lineIdentifier;
    }

    @Override
    public Object getData(int index) {
        if (firstRow < 0)
            throw new IllegalSourceStateException(
                    "The method next was never called");
        if (currentRow == null)
            return index == 0 ? "" : null;
        Cell cell = currentRow.getCell(index);
        return getData(cell);
    }

    static private Object getData(Cell cell) {
        if (cell == null)
            return null;
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC:
                if (HSSFDateUtil.isCellDateFormatted(cell))
                    return cell.getDateCellValue();
                return cell.getNumericCellValue();
            case Cell.CELL_TYPE_STRING:
                return cell.getStringCellValue();
            case Cell.CELL_TYPE_FORMULA:
                return cell.getCellFormula();
            case Cell.CELL_TYPE_BLANK:
                return "";
            case Cell.CELL_TYPE_BOOLEAN:
                return cell.getBooleanCellValue();
            case Cell.CELL_TYPE_ERROR:
                return cell.getErrorCellValue();
            default:
                return cell.getStringCellValue();
        }
    }

    @Override
    public boolean setData(String column, Object value) {
        return false; // @TODO implement?
    }

    @Override
    public boolean setData(int index, Object value) {
        return false; // @TODO implement?
    }
}
