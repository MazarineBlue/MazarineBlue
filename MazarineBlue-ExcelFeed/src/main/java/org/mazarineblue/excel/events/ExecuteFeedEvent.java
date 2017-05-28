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
import org.mazarineblue.excel.ExcelFeed;

/**
 * An {@code ExecuteFeedEvent} is an {@code ExcelEvent} that instructs a
 * {@code ExcelFeedSubscriber} to create a new {@code Feed} using the specified
 * sheet name and return to the previous {@code Feed} when it has completed.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class ExecuteFeedEvent
        extends ExcelEvent {

    private final String sheet;

    /**
     * Constructs an {@code ExecuteFeedEvent} that causes the creation and
     * execution of another {@link ExcelFeed} by loading the specified sheet
     * on the current {@link Workbook}.
     *
     * @param sheet the sheet to read.
     */
    public ExecuteFeedEvent(String sheet) {
        this.sheet = sheet;
    }

    @Override
    public String toString() {
        return "sheet={" + sheet + '}';
    }

    public String getSheetName() {
        return sheet;
    }

    @Override
    public int hashCode() {
        return 5 * 89
                + Objects.hashCode(this.sheet);
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && getClass() == obj.getClass() && super.equals(obj)
                && Objects.equals(this.sheet, ((ExecuteFeedEvent) obj).sheet);
    }
}
