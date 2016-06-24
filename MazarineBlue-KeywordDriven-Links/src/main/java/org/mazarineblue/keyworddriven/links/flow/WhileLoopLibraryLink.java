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
package org.mazarineblue.keyworddriven.links.flow;

import java.util.Collection;
import org.mazarineblue.eventbus.EventHandler;
import org.mazarineblue.events.EndSheetEvent;
import org.mazarineblue.events.instructions.ExecuteInstructionLineEvent;
import org.mazarineblue.keyworddriven.InstructionLine;
import org.mazarineblue.keyworddriven.Keyword;
import org.mazarineblue.keyworddriven.exceptions.GeneralParseException;
import org.mazarineblue.keyworddriven.exceptions.BlockOpenOnEndSheetException;
import org.mazarineblue.keyworddriven.librarymanager.Library;
import org.mazarineblue.keyworddriven.links.RecordingLibraryLink;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class WhileLoopLibraryLink
        extends RecordingLibraryLink {

    private final String expression;
    private final LoopTester loopTester;

    public WhileLoopLibraryLink(Library library, LoopTester loopTester,
                                String expression) {
        super(library);
        this.expression = expression;
        this.loopTester = loopTester;
    }

    @Keyword("End while")
    public void endWhile() {
        executor().chain().remove(this);
        try {
            Collection<InstructionLine> list = getRecording();
            while (loopTester.continueLoop(expression))
                for (InstructionLine line : list) {
                    publish(new ExecuteInstructionLineEvent(line, this));
                    if (loopTester.isBroken())
                        break;
                }
        } catch (org.mazarineblue.parser.exceptions.ParserException ex) {
            throw new GeneralParseException(expression);
        } finally {
            loopTester.removeLink(executor().chain());
        }
    }

    @EventHandler
    public void eventHandler(EndSheetEvent event) {
        throw new BlockOpenOnEndSheetException("While lus", event.getSheetName());
    }
}
