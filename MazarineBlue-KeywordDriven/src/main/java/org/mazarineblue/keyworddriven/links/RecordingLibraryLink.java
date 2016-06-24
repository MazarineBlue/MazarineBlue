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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.mazarineblue.events.instructions.InstructionLineEvent;
import org.mazarineblue.keyworddriven.InstructionLine;
import org.mazarineblue.keyworddriven.librarymanager.Library;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class RecordingLibraryLink
        extends LibraryLink {

    private final List<InstructionLine> list = new ArrayList<>();

    protected RecordingLibraryLink(Library library) {
        super(library);
    }

    private RecordingLibraryLink(String namespace, Library library) {
        super(namespace, library);
    }

    @Override
    protected void processNonkeyEvents(InstructionLineEvent event) {
        InstructionLine line = event.getLine();
        list.add(line);
        event.setConsumed();
    }

    /**
     * Returns the current state of the recording.
     *
     * @return an unmodifiable view on the recorded instructions.
     */
    protected Collection<InstructionLine> getRecording() {
        return list;
    }
}
