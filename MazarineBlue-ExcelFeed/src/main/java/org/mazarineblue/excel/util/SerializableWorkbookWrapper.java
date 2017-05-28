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
package org.mazarineblue.excel.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Objects;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.mazarineblue.utililities.exceptions.UnknownIssueException;

/**
 * A {@code SerializableWorkbookWrapper} is a wrapper of {@link Workbook} that
 * makes the {@code Workbook} {@link Serializable} and implements
 * {@code equals(Object)} : boolean} and {@code hashCode() : int} methods.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class SerializableWorkbookWrapper
        implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final double EPSILON = .001;

    private transient Workbook workbook;

    /**
     * Constructs a {@code SerializableWorkbookWrapper}.
     *
     * @param workbook to wrap.
     */
    public SerializableWorkbookWrapper(Workbook workbook) {
        this.workbook = workbook;
    }

    /**
     * Returns the sheet with the specified name.
     *
     * @param sheetName of the sheet.
     * @return {@code null} if the sheet doesn't exists.
     */
    public Sheet getSheet(String sheetName) {
        return workbook.getSheet(sheetName);
    }

    private void writeObject(ObjectOutputStream output)
            throws IOException {
        output.defaultWriteObject();
        workbook.write(output);
    }

    private void readObject(ObjectInputStream input)
            throws IOException, ClassNotFoundException, InvalidFormatException {
        input.defaultReadObject();
        workbook = WorkbookFactory.create(input);
    }

    @Override
    public int hashCode() {
        int hash = 7 * 83 + Objects.hashCode(workbook.getActiveSheetIndex());
        for (int i = 0; i < workbook.getNumberOfSheets(); ++i)
            hash = 83 * hash + hashCode(workbook.getSheetAt(i));
        return hash;
    }

    private int hashCode(Sheet sheet) {
        int hash = 4002509 + 6889 * Objects.hashCode(sheet.getSheetName())
                + 83 * Objects.hashCode(sheet.getFirstRowNum()) + Objects.hashCode(sheet.getLastRowNum());
        for (int j = sheet.getFirstRowNum(); j <= sheet.getLastRowNum(); ++j)
            hash = 83 * hash + hashCode(sheet.getRow(j));
        return hash;
    }

    private int hashCode(Row row) {
        int hash = 48223 + 83 * Objects.hashCode(row.getFirstCellNum()) + Objects.hashCode(row.getLastCellNum());
        for (int k = row.getFirstCellNum(); k < row.getLastCellNum(); ++k)
            hash = 83 * hash + hashCode(row.getCell(k));
        return hash;
    }

    private int hashCode(Cell cell) {
        return 48223 + 83 * Objects.hashCode(cell.getCellType()) + Objects.hashCode(getCellValue(cell));
    }

    private Object getCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC: return cell.getNumericCellValue();
            case Cell.CELL_TYPE_STRING: return cell.getStringCellValue();
            case Cell.CELL_TYPE_FORMULA: return cell.getCellFormula();
            case Cell.CELL_TYPE_BLANK: return null;
            case Cell.CELL_TYPE_BOOLEAN: return cell.getBooleanCellValue();
            case Cell.CELL_TYPE_ERROR: return cell.getErrorCellValue();
            default: throw new UnknownIssueException("Cell type unsuppored, type: " + cell.getCellType());
        }
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && getClass() == obj.getClass()
                && equalWorkbooks(this.workbook, ((SerializableWorkbookWrapper) obj).workbook);
    }

    private boolean equalWorkbooks(Workbook left, Workbook right) {
        return equalActiveSheetIndexes(left, right) && equalNumberOfSheets(left, right) && equalSheets(left, right);
    }

    private static boolean equalActiveSheetIndexes(Workbook left, Workbook right) {
        return left.getActiveSheetIndex() == right.getActiveSheetIndex();
    }

    private static boolean equalNumberOfSheets(Workbook left, Workbook right) {
        return left.getNumberOfSheets() == right.getNumberOfSheets();
    }

    private boolean equalSheets(Workbook left, Workbook right) {
        for (int i = 0; i < left.getNumberOfSheets(); ++i)
            if (!equalSheets(left.getSheetAt(i), right.getSheetAt(i)))
                return false;
        return true;
    }

    private boolean equalSheets(Sheet left, Sheet right) {
        return equalSheetNames(left, right) && equalRowBoundries(left, right) && equalRows(left, right);
    }

    private static boolean equalSheetNames(Sheet left, Sheet right) {
        return left.getSheetName().equals(right.getSheetName());
    }

    private static boolean equalRowBoundries(Sheet left, Sheet right) {
        return left.getFirstRowNum() == right.getFirstRowNum() && left.getLastRowNum() == right.getLastRowNum();
    }

    private boolean equalRows(Sheet left, Sheet right) {
        for (int i = left.getFirstRowNum(); i <= left.getLastRowNum(); ++i)
            if (!equalRows(left.getRow(i), right.getRow(i)))
                return false;
        return true;
    }

    private boolean equalRows(Row left, Row right) {
        return equalCellBoundries(left, right) && equalCells(left, right);
    }

    private boolean equalCellBoundries(Row left, Row right) {
        return left.getFirstCellNum() == right.getFirstCellNum() && left.getLastCellNum() == right.getLastCellNum();
    }

    private boolean equalCells(Row left, Row right) {
        for (int i = left.getFirstCellNum(); i < left.getLastCellNum(); ++i)
            if (!equalCells(left.getCell(i), right.getCell(i)))
                return false;
        return true;
    }

    private boolean equalCells(Cell left, Cell right) {
        if (left.getCellType() != right.getCellType())
            return false;
        switch (left.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC: return Math.abs(left.getNumericCellValue() - right.getNumericCellValue()) < EPSILON;
            case Cell.CELL_TYPE_STRING: return left.getStringCellValue().equals(right.getStringCellValue());
            case Cell.CELL_TYPE_FORMULA: return left.getCellFormula().equals(right.getCellFormula());
            case Cell.CELL_TYPE_BLANK: return true;
            case Cell.CELL_TYPE_BOOLEAN: return left.getBooleanCellValue() == right.getBooleanCellValue();
            case Cell.CELL_TYPE_ERROR: return left.getErrorCellValue() == right.getErrorCellValue();
            default: throw new UnknownIssueException("Cell type unsuppored, type: " + left.getCellType());
        }
    }
}
