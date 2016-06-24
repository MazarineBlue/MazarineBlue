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

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class ResolveKeywordConflictsLink
        implements Link {

    private final KeywordCollection keywordCollection;

    public ResolveKeywordConflictsLink(String namespace,
                                       String... keywords) {
        keywordCollection = new KeywordCollection(namespace, keywords);
    }

    @EventHandler
    public void eventHandler(InstructionLineEvent event) {
        InstructionLine line = event.getLine();
        InstructionLine updatedLine = changeNamespace(line);
        event.push(updatedLine);
    }

    private InstructionLine changeNamespace(InstructionLine line) {
        if (line.getNamespace().isEmpty() == false)
            return line;
        String keyword = line.getKeyword();
        if (keywordCollection.containsKeyword(keyword) == false)
            return line;
        String namespace = keywordCollection.getNamespace();
        String lineIdentifier = line.getLineIdentifier();
        Object[] parameters = line.getParameters();
        return new InstructionLine(lineIdentifier, namespace, keyword,
                                   parameters);
    }
}
