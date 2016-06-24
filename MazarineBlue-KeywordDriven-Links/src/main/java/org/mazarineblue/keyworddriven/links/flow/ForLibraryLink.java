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
import org.mazarineblue.parser.Parser;
import org.mazarineblue.parser.exceptions.ParserException;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class ForLibraryLink
        extends RecordingLibraryLink {

    private final String variable;
    private final String expression;
    private final String increment;
    private final LoopTester loopTester;
    private final Parser variableParser, operatorParser;

    public ForLibraryLink(Library library, LoopTester loopTester,
                          Parser variableParser, Parser operatorParser,
                          String variable, String terminination,
                          String increment) {
        super(library);
        this.variableParser = variableParser;
        this.operatorParser = operatorParser;
        this.loopTester = loopTester;
        this.variable = variable;
        this.expression = terminination;
        this.increment = increment;
    }

    @Keyword("End for")
    public void endFor() {
        executor().chain().remove(this);
        Collection<InstructionLine> list = getRecording();
        try {
            while (loopTester.continueLoop(expression)) {
                for (InstructionLine line : list) {
                    publish(new ExecuteInstructionLineEvent(line, this));
                    if (loopTester.isBroken())
                        break;
                }
                increment();
            }
        } catch (org.mazarineblue.parser.exceptions.ParserException ex) {
            throw new GeneralParseException(expression);
        } finally {
            loopTester.removeLink(executor().chain());
        }
    }

    private void increment()
            throws ParserException {
        String tmp = variableParser.parse(increment, blackboard(), String.class);
        Object data = operatorParser.parse(tmp, blackboard());
        blackboard().setData(variable, data);
    }

    @EventHandler
    public void eventHandler(EndSheetEvent event) {
        throw new BlockOpenOnEndSheetException("For lus", event.getSheetName());
    }
}
