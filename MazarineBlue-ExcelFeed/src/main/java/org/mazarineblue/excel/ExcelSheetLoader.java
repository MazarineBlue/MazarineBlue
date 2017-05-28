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

import java.util.Objects;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.mazarineblue.eventbus.Event;
import org.mazarineblue.eventbus.EventHandler;
import org.mazarineblue.eventbus.ReflectionSubscriber;
import org.mazarineblue.eventdriven.Feed;
import org.mazarineblue.eventdriven.Interpreter;
import org.mazarineblue.excel.events.ExcelEvent;
import org.mazarineblue.excel.events.ExecuteFeedEvent;
import org.mazarineblue.excel.events.WorkbookEvent;
import org.mazarineblue.excel.util.SerializableWorkbookWrapper;

/**
 * An {@code ExcelSheetLoader} is a {@code ReflectionSubscriber} that
 * listens to {@link ExcelEvent ExcelEvents} that work with a
 * {@link Feed}.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
@SuppressWarnings("serial")
public class ExcelSheetLoader
        extends ReflectionSubscriber<Event> {

    private SerializableWorkbookWrapper workbook;

    /**
     * Constructs an {@code ExcelSheetLoader} that works with the specified
     * {@code Workbook}.
     *
     * @param workbook the workbook to perform operations with.
     */
    public ExcelSheetLoader(Workbook workbook) {
        this.workbook = new SerializableWorkbookWrapper(workbook);
    }

    /**
     * Event handlers are not meant to be called directly, instead publish an
     * event to an {@link Interpreter}; please see the specified event for more
     * information about this event handler.
     *
     * @param event the event this {@code EventHandler} processes.
     * @see ExecuteFeedEvent
     */
    @EventHandler
    public void eventHandler(ExecuteFeedEvent event) {
        Sheet sheet = workbook.getSheet(event.getSheetName());
        Feed feed = new ExcelFeed(sheet);
        event.getInvoker().interpreter().execute(feed);
        event.setConsumed(true);
    }

    /**
     * Event handlers are not meant to be called directly, instead publish an
     * event to an {@link Interpreter}; please see the specified event for more
     * information about this event handler.
     *
     * @param event the event this {@code EventHandler} processes.
     * @see WorkbookEvent
     */
    @EventHandler
    public void eventHandler(WorkbookEvent event) {
        workbook = event.getWrapper();
    }

    @Override
    public int hashCode() {
        return 5 * 29
                + Objects.hashCode(this.workbook);
    }

    @Override
    public boolean equals(Object obj) { // @TODO write tests
        return obj != null && getClass() == obj.getClass()
                && Objects.equals(this.workbook, ((ExcelSheetLoader) obj).workbook);
    }
}
