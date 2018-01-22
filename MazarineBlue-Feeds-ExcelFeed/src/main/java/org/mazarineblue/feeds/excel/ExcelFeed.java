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
package org.mazarineblue.feeds.excel;

import org.apache.poi.ss.usermodel.Cell;
import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_BOOLEAN;
import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC;
import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.mazarineblue.eventdriven.Feed;
import org.mazarineblue.eventdriven.exceptions.NoEventsLeftException;
import org.mazarineblue.eventnotifier.Event;
import org.mazarineblue.feeds.excel.exceptions.KeywordMustBeTextException;
import org.mazarineblue.feeds.excel.exceptions.UnsupportedCellTypeException;
import org.mazarineblue.keyworddriven.AbstractInstructionFeed;
import org.mazarineblue.keyworddriven.events.CommentInstructionLineEvent;

/**
 * A {@code ExcelFeed} is a {@code Feed} that takes its input from a excel
 * sheet.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class ExcelFeed
        extends AbstractInstructionFeed
        implements Feed {

    private final Sheet sheet;
    private int rowIndex;

    /**
     * Constructs a {@code ExcelFeed} to reed from the specified excel sheet.
     *
     * @param sheet
     */
    public ExcelFeed(Sheet sheet) {
        this.sheet = sheet;
        rowIndex = sheet.getFirstRowNum();
    }

    @Override
    public String toString() {
        return "sheet=" + sheet.getSheetName() + ", rowIndex=" + rowIndex + ", " + super.toString();
    }

    @Override
    public boolean hasNext() {
        while (rowIsWithinBounds(rowIndex)) {
            if (rowIsValid(rowIndex) && !isEndOfFile(rowIndex) && keywordIsValid(rowIndex))
                return true;
            ++rowIndex;
        }
        return false;
    }

    @Override
    public Event next() {
        while (rowIsWithinBounds(rowIndex)) {
            if (rowIsValid(rowIndex) && !isEndOfFile(rowIndex))
                return createEvent(rowIndex++);
            ++rowIndex;
        }
        throw new NoEventsLeftException(this);
    }

    // <editor-fold defaultstate="collapsed" desc="Helper methods for hasNext() and next()">
    private boolean rowIsWithinBounds(int rowIndex) {
        return rowIndex <= sheet.getLastRowNum();
    }

    private boolean rowIsValid(int rowIndex) {
        return sheet.getRow(rowIndex) != null;
    }

    private boolean isEndOfFile(int rowIndex) {
        Row row = sheet.getRow(rowIndex);
        return row.getFirstCellNum() >= row.getLastCellNum();
    }

    private boolean keywordIsValid(int rowIndex) {
        Cell cell = getCell(rowIndex, 0);
        return cell.getCellType() == CELL_TYPE_STRING;
    }

    private Event createEvent(int rowIndex) {
        return rowContainsComment(rowIndex)
                ? new CommentInstructionLineEvent(getArguments(rowIndex))
                : createEvent(getKeyword(rowIndex), getArguments(rowIndex));
    }

    private boolean rowContainsComment(int rowIndex) {
        return getCell(rowIndex, 0) == null;
    }

    private String getKeyword(int rowIndex) {
        if (!keywordIsValid(rowIndex))
            throw new KeywordMustBeTextException(rowIndex);
        return getCell(rowIndex, 0).getStringCellValue();
    }

    private Cell getCell(int rowIndex1, int cellIndex) {
        return sheet.getRow(rowIndex1).getCell(cellIndex);
    }

    private Object[] getArguments(int rowIndex) {
        return getArguments(sheet.getRow(rowIndex));
    }

    private Object[] getArguments(Row row) {
        Object[] args = new Object[countArguments(row)];
        for (int i = 0; i < args.length; ++i)
            args[i] = getData(row, i + 1);
        return args;
    }

    private int countArguments(Row row) {
        return row.getLastCellNum() - 1;
    }

    private static Object getData(Row row, int index) {
        Cell cell = row.getCell(index);
        switch (cell.getCellType()) {
            case CELL_TYPE_BOOLEAN:
                return cell.getBooleanCellValue();
            case CELL_TYPE_NUMERIC:
                return cell.getNumericCellValue();
            case CELL_TYPE_STRING:
                return cell.getStringCellValue();
            default:
                throw new UnsupportedCellTypeException(cell.getCellType());
        }
    }
    // </editor-fold>

    @Override
    public void reset() {
        rowIndex = 0;
        super.reset();
    }
}
