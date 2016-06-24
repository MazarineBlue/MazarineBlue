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
package org.mazarineblue.events.instructions;

import org.mazarineblue.datasources.DataSource;
import org.mazarineblue.eventbus.AbstractEvent;
import org.mazarineblue.keyworddriven.InstructionLine;
import org.mazarineblue.keyworddriven.InterpreterContext;
import org.mazarineblue.keyworddriven.librarymanager.Library;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public abstract class InstructionLineEvent
        extends AbstractEvent {

    private InstructionLine line;
    private DataSource source;
    private InterpreterContext context;

    public InstructionLineEvent(InstructionLine line) {
        this.line = line;
    }

    public InstructionLineEvent(InstructionLine line, Library library) {
        this.line = line;
        this.source = library.blackboard();
        this.context = library.context();
    }

    @Override
    public String toString() {
        return "InstructionLineEvent{" + "line=" + line.toString() + "}";
    }

    public InstructionLine getLine() {
        return line;
    }

    public void push(InstructionLine line) {
        if (context != null)
            context.add(line);
        this.line = line;
    }

    public void setDataSource(DataSource source) {
        this.source = source;
    }

    public DataSource getDataSource() {
        return source;
    }

    public void setContext(InterpreterContext context) {
        this.context = context;
    }

    public InterpreterContext getContext() {
        return context;
    }
}
