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

import org.mazarineblue.eventdriven.Invoker;
import org.mazarineblue.excel.events.ExecuteFeedEvent;
import org.mazarineblue.keyworddriven.Keyword;
import org.mazarineblue.keyworddriven.Library;
import org.mazarineblue.keyworddriven.Parameters;
import org.mazarineblue.keyworddriven.PassInvoker;
import org.mazarineblue.variablestore.events.EndVariableScopeEvent;
import org.mazarineblue.variablestore.events.StartVariableScopeEvent;

/**
 * An {@code ExcelLibrary} is a {@code Library} that implements basis excel
 * instructions.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class ExcelLibrary
        extends Library {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a {@code ExcelLibrary} with the namespace
     * 'org.mazarineblue.excel'.
     */
    public ExcelLibrary() {
        super("org.mazarineblue.excel");
    }

    @Keyword("Import sheet")
    @Parameters(min = 1)
    @PassInvoker
    public void importSheet(Invoker invoker, String sheet) {
        invoker.publish(new ExecuteFeedEvent(sheet));
    }

    @Keyword("Call sheet")
    @Parameters(min = 1)
    @PassInvoker
    public void callSheet(Invoker invoker, String sheet) {
        invoker.publish(new StartVariableScopeEvent());
        invoker.publish(new ExecuteFeedEvent(sheet));
        invoker.publish(new EndVariableScopeEvent());
    }
}
