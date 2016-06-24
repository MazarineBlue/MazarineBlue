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

import org.mazarineblue.events.instructions.InstructionLineEvent;
import org.mazarineblue.keyworddriven.librarymanager.Library;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class SkippableLibraryLink
        extends LibraryLink {

    protected static enum Mode {

        WAITING,
        EXECUTING,
        DONE,
    }

    private Mode mode;

    protected SkippableLibraryLink(Library library) {
        super(library);
        mode = Mode.WAITING;
    }

    protected SkippableLibraryLink(String namespace, Library library) {
        super(namespace, library);
        mode = Mode.WAITING;
    }

    protected Mode getMode() {
        return mode;
    }

    protected void next() {
        next(true, true);
    }

    protected void next(boolean waitingCondition, boolean executingCondition) {
        switch (mode) {
            case WAITING:
                if (waitingCondition)
                    mode = Mode.EXECUTING;
                break;
            case EXECUTING:
                if (executingCondition)
                    mode = Mode.DONE;
                break;
            case DONE:
        }
    }

    protected void execute() {
        switch (mode) {
            case WAITING:
                mode = Mode.EXECUTING;
                break;
            case EXECUTING:
                throw new IllegalStateException(
                        "Can not set link state to executing twice.");
            case DONE:
                throw new IllegalStateException(
                        "Can not set link state to executing once its done.");
        }
    }

    protected void done() {
        mode = Mode.DONE;
    }

    @Override
    protected void processNonkeyEvents(InstructionLineEvent event) {
        if (mode == Mode.WAITING || mode == Mode.DONE)
            event.setConsumed();
    }
}
