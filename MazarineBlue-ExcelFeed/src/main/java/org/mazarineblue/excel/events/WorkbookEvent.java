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
package org.mazarineblue.excel.events;

import java.util.Objects;
import org.apache.poi.ss.usermodel.Workbook;
import org.mazarineblue.excel.util.SerializableWorkbookWrapper;

/**
 * A {@code WorkbookEvent} is an {@code ExcelEvent} that instructs
 * {@code ExcelSheetLoader} to use the specified {@code Workbook}.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class WorkbookEvent
        extends ExcelEvent {

    private static final double EPSILON = .001;

    private final SerializableWorkbookWrapper wrapper;

    /**
     * Constructs a {@code WorkbookEvent} that causes {@code ExcelSheetLoader}
     * to use the specified {@code Workbook}.
     *
     * @param workbook to use.
     */
    public WorkbookEvent(Workbook workbook) {
        this.wrapper = new SerializableWorkbookWrapper(workbook);
    }

    public SerializableWorkbookWrapper getWrapper() {
        return wrapper;
    }

    @Override
    public int hashCode() {
        return 5 * 23
                + Objects.hashCode(this.wrapper);
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && getClass() == obj.getClass()
                && Objects.equals(this.wrapper, ((WorkbookEvent) obj).wrapper);
    }
}
