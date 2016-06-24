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
import org.mazarineblue.events.instructions.InstructionLineEvent;
import org.mazarineblue.keyworddriven.InstructionLine;
import org.mazarineblue.keyworddriven.librarymanager.Instruction;
import org.mazarineblue.keyworddriven.librarymanager.LibraryManager;

/**
 * The LibraryLink listens to InstructionLineEvent.
 *
 * It expects to be the last link and thus throws an exception when the
 * instruction could not be found.
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class LibraryDispatcherLink
        implements Link {

    private final LibraryManager libraryManager;

    public LibraryDispatcherLink(LibraryManager libraryManager) {
        this.libraryManager = libraryManager;
    }

    @EventHandler
    public void eventHandler(InstructionLineEvent event) {
        invoker.invoke(event);
    }

    private InstructionInvoker invoker = new InstructionInvoker() {

        @Override
        protected Instruction getInstruction(InstructionLineEvent event) {
            InstructionLine line = event.getLine();
            String namespace = line.getNamespace();
            String keyword = line.isComment() ? "Comment" : line.getKeyword();
            return libraryManager.getInstructionByPath(namespace, keyword);
        }
    };
}
