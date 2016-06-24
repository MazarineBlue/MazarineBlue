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
package org.mazarineblue.keyworddriven.links;

import org.mazarineblue.eventbus.EventHandler;
import org.mazarineblue.eventbus.EventService;
import org.mazarineblue.events.instructions.InstructionLineEvent;
import org.mazarineblue.keyworddriven.InstructionLine;
import org.mazarineblue.keyworddriven.librarymanager.Instruction;
import org.mazarineblue.keyworddriven.librarymanager.Library;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public abstract class LibraryLink
        extends Library
        implements Link {

    protected LibraryLink(Library library) {
        super(library);
    }

    protected LibraryLink(String namespace, Library library) {
        super(namespace, library);
    }

    @Override
    protected void setEvents(EventService service) {
    }

    @Override
    protected void setup() {
    }

    @Override
    protected void teardown() {
    }

    @EventHandler
    public void eventHandler(InstructionLineEvent event) {
        InstructionLine line = event.getLine();
        if (line.isEmpty())
            return;

        if (getKeywords().contains(line.getKeyword()))
            processKeyEvents(event);
        else
            processNonkeyEvents(event);
    }

    abstract protected void processNonkeyEvents(InstructionLineEvent event);

    private void processKeyEvents(InstructionLineEvent event) {
        invoker.invoke(event);
    }

    private final InstructionInvoker invoker = new InstructionInvoker() {

        @Override
        protected boolean skipInvokation(InstructionLineEvent event) {
            InstructionLine line = event.getLine();
            if (line.isEmpty())
                return false;

            String namespace = line.getNamespace();
            if (namespace == null || namespace.isEmpty())
                return false;
            return namespace.equals(getNamespace()) == false;
        }

        @Override
        protected Instruction getInstruction(InstructionLineEvent event) {
            InstructionLine line = event.getLine();
            String keyword = line.getKeyword();
            return getInstructionDispatcher(keyword);
        }
    };

    private Instruction getInstructionDispatcher(String keyword) {
        return getInstruction(keyword);
    }
}
