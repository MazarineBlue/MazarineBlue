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
import java.util.Collections;
import java.util.List;
import org.mazarineblue.eventbus.EventHandler;
import org.mazarineblue.events.instructions.ExecuteInstructionLineEvent;
import org.mazarineblue.keyworddriven.InstructionLine;
import org.mazarineblue.keyworddriven.links.Link;

/**
 * The RecordingInstructionLink records instruction lines. It has the ability to
 * consume the events or eavesdrop.
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 * @see RecordingLibraryLink
 */
@Deprecated
public class RecordingInstructionLink
        implements Link {

    private final List<InstructionLine> list = new ArrayList();
    private final KeywordCollection keywordCollection;
    private final boolean consumeEvents;

    /**
     * Constructs a link that listens to instruction lines. It skip over the
     * specified instruction.
     *
     * @param namespace The namespace of the instruction lines to skip over.
     * @param closingKeywords The keywords of the instruction lines to skip
     * over.
     */
    @Deprecated
    public RecordingInstructionLink(String namespace, String... closingKeywords) {
        keywordCollection = new KeywordCollection(namespace, closingKeywords);
        this.consumeEvents = true;
    }

    /**
     * Constructs a link that listens to instruction lines. It doesn't listenen
     * to the specified instruction.
     *
     * @param namespace The namespace of the instruction lines to skip over.
     * @param consumeEvents Consumes ExecutionInstructionLineEvent when true.
     * @param closingKeywords The keywords of the instruction lines to skip
     * over.
     */
    @Deprecated
    public RecordingInstructionLink(String namespace, boolean consumeEvents,
                                    String... closingKeywords) {
        keywordCollection = new KeywordCollection(namespace, closingKeywords);
        this.consumeEvents = consumeEvents;
    }

    /**
     * Returns the current state of the recording.
     *
     * @return an unmodifiable view on the recorded instructions.
     */
    public Collection<InstructionLine> getRecording() {
        return Collections.unmodifiableCollection(list);
    }

    @EventHandler
    public void eventHandler(ExecuteInstructionLineEvent event) {
        InstructionLine line = event.getLine();
        if (isEnding(line))
            return;
        if (line.isEmpty() == false)
            list.add(line);
        if (consumeEvents)
            event.setConsumed();
    }

    private boolean isEnding(InstructionLine line) {
        return keywordCollection.containsLine(line);
    }
}
