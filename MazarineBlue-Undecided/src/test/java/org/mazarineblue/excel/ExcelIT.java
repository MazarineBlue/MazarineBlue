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

import org.apache.poi.ss.usermodel.Workbook;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.eventbus.link.EventBusLink;
import org.mazarineblue.eventdriven.Feed;
import org.mazarineblue.eventdriven.Interpreter;
import org.mazarineblue.eventdriven.InterpreterFactory;
import org.mazarineblue.eventdriven.Link;
import org.mazarineblue.eventdriven.util.LinkSpy;
import org.mazarineblue.excel.events.ExcelEvent;
import org.mazarineblue.excel.events.ExecuteFeedEvent;
import org.mazarineblue.excel.util.WorkbookBuilder;
import org.mazarineblue.keyworddriven.Library;
import org.mazarineblue.keyworddriven.LibraryRegistry;
import org.mazarineblue.keyworddriven.events.ExecuteInstructionLineEvent;
import org.mazarineblue.keyworddriven.events.KeywordDrivenEvent;
import org.mazarineblue.libraries.LoggingLibrary;
import org.mazarineblue.libraries.events.LogEvent;
import org.mazarineblue.links.UnconsumedExceptionThrowerLink;
import org.mazarineblue.variablestore.ScopedVariableStoreLibrary;
import org.mazarineblue.variablestore.events.AssignVariableEvent;
import org.mazarineblue.variablestore.events.EndVariableScopeEvent;
import org.mazarineblue.variablestore.events.StartVariableScopeEvent;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class ExcelIT {

    private WorkbookBuilder builder;
    private Feed feed;
    private Interpreter interpreter;
    private LinkSpy spy;

    @Before
    public void setup() {
        builder = new WorkbookBuilder();
        feed = builder.getFeed();
        spy = new LinkSpy();
        InterpreterFactory factory = InterpreterFactory.createDefaultFactory();
        factory.addLink(new UnconsumedExceptionThrowerLink());
        factory.addLink(createEventBusLink(builder.getWorkbook()));
//        factory.addLink(new ConsumeExceptionThrownEventLink()); // Do we need this for excel/log/SwingRunner?
        interpreter = factory.create();
        interpreter.addLink(spy);
    }

    private Link createEventBusLink(Workbook workbook) {
        EventBusLink link = new EventBusLink();

        LibraryRegistry libraryRegistry = new LibraryRegistry(new ExcelLibrary());
        link.subscribe(KeywordDrivenEvent.class, null, libraryRegistry);
        link.subscribe(ExcelEvent.class, null, new ExcelSheetLoader(workbook));

        EventHandlerLibraryRegistry r = new EventHandlerLibraryRegistry(link, libraryRegistry);
        r.add(new ScopedVariableStoreLibrary());
        r.add(new LoggingLibrary());
        Link l = r.getEventBusLink();
        return l;
    }

    private static class EventHandlerLibraryRegistry {

        private final EventBusLink link;
        private final LibraryRegistry libraryRegistry;

        EventHandlerLibraryRegistry(EventBusLink link, LibraryRegistry libraryRegistry) {
            this.link = link;
            this.libraryRegistry = libraryRegistry;
        }

        void add(Library library) {
            link.subscribe(library.getBaseEvent(), null, library);
            libraryRegistry.addLibrary(library);
        }

        Link getEventBusLink() {
            return link;
        }
    }

    @After
    public void teardown() {
        builder = null;
        feed = null;
    }

    @Test
    public void sleep() {
        builder.addRow("Sleep", "1");
    }

    // Sheet instructions
    @Test
    public void importSheet() {
        builder.addRow("Import sheet", "other");
//        workbook.addRow("Echo", "$variable");
        builder.switchToSheet("other");
        builder.addRow("Set", "variable", 1);
        interpreter.execute(feed);
        assertEquals(new ExecuteInstructionLineEvent("Import sheet", "other"), spy.next());
        assertEquals(new ExecuteFeedEvent("other"), spy.next());
        assertEquals(new ExecuteInstructionLineEvent("Set", "variable", 1d), spy.next());
        assertEquals(new AssignVariableEvent("variable", 1d), spy.next());
//        assertEquals(new LogEvent(1d), spy.next());
    }

    @Test
    public void callSheet() {
        builder.addRow("Set", "variable", 1);
        builder.addRow("Call sheet", "other");
        builder.addRow("Echo", "$variable");
        builder.switchToSheet("other");
        builder.addRow("Set", "variable", 2);
        builder.addRow("Echo", "$variable");
        interpreter.execute(feed);
        assertEquals(new AssignVariableEvent("variable", 1d), spy.next());
        assertEquals(new ExecuteInstructionLineEvent("Call sheet", "other"), spy.next());
        assertEquals(new StartVariableScopeEvent(), spy.next());
        assertEquals(new ExecuteFeedEvent("other"), spy.next());
        assertEquals(new ExecuteInstructionLineEvent("Set", "variable", 1d), spy.next());
        assertEquals(new AssignVariableEvent("variable", 2d), spy.next());
        assertEquals(new LogEvent(2d), spy.next());
        assertEquals(new EndVariableScopeEvent(), spy.next());
        assertEquals(new LogEvent(1d), spy.next());
    }

    // Variables instructions
    @Test //(expected = Exception.class)
    public void declareLocalAndThenGlobalVariable() {
        builder.addRow("Declare variable", "variable");
        builder.addRow("Declare Global variable", "variable");
    }

    @Test //(expected = Exception.class)
    public void declareGlobalAndThenLocalaVariable() {
        builder.addRow("Declare Global variable", "variable");
        builder.addRow("Declare variable", "variable");
    }

    @Test
    public void setLocalVariable() {
        builder.addRow("Declare variable", "local variable");
        builder.addRow("Procedure", "name", "param");
        builder.addRow("Set", "local variable", 1);
        builder.addRow("End procedure");
        builder.addRow("Call procedure", "name");
    }

    @Test
    public void setGlobalVariable() {
        builder.addRow("Declare global variable", "global variable");
        builder.addRow("Procedure", "name", "param");
        builder.addRow("Set", "global variable", 1);
        builder.addRow("End procedure");
        builder.addRow("Call procedure", "name");
    }
}
