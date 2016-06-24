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
package org.mazarineblue.runner.sheetFactorySelector;

import java.io.IOException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.mazarineblue.datasources.DataSource;
import org.mazarineblue.datasources.ExcelSource;
import org.mazarineblue.keyworddriven.documentMediators.DocumentMediator;
import org.mazarineblue.keyworddriven.feeds.FeedBuilder;
import org.mazarineblue.keyworddriven.sheetfactories.SheetFactory;
import org.mazarineblue.runner.exceptions.FactoryHasNoFileException;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class ExcelSheetFactory
        extends AbstractSheetFactory
        implements SheetFactory {

    private String location = "";
    private Workbook workbook;

    public ExcelSheetFactory(FeedBuilder feedBuilder) {
        super(feedBuilder, null);
        this.workbook = null;
    }

    public ExcelSheetFactory(FeedBuilder feedBuilder, DocumentMediator mediator)
            throws IOException {
        super(feedBuilder, mediator);
        try {
            workbook = WorkbookFactory.create(mediator.getInputStream());
            location = mediator.getInputLocation();
        } catch (InvalidFormatException ex) {
            throw new IOException(ex);
        }
    }

    @Override
    public String[] getSheetNames() {
        checkState();
        String[] arr = new String[workbook.getNumberOfSheets()];
        for (int i = 0; i < arr.length; ++i)
            arr[i] = workbook.getSheetName(i);
        return arr;
    }

    private void checkState() {
        if (workbook == null)
            throw new FactoryHasNoFileException();
    }

    @Override
    protected DataSource getSource(String sourceIdentifier, String sheetName, boolean headers) {
        checkState();
        Sheet sheet = workbook.getSheet(sheetName);
        if (sheet == null)
            throw new SheetNotFoundException("Sheet not found: " + sheetName);
        return new ExcelSource(sourceIdentifier, sheet, headers);
    }
}
