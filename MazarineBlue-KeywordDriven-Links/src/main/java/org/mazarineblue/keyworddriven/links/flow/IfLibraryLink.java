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

import org.mazarineblue.eventbus.EventHandler;
import org.mazarineblue.events.EndSheetEvent;
import org.mazarineblue.keyworddriven.Keyword;
import org.mazarineblue.keyworddriven.Parameters;
import org.mazarineblue.keyworddriven.exceptions.BlockOpenOnEndSheetException;
import org.mazarineblue.keyworddriven.exceptions.GeneralParseException;
import org.mazarineblue.keyworddriven.librarymanager.Library;
import org.mazarineblue.keyworddriven.links.SkippableLibraryLink;
import org.mazarineblue.parser.Parser;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class IfLibraryLink
        extends SkippableLibraryLink {

    private static enum Mode {

        WAITING,
        EXECUTING,
        DONE,
    }

    private Mode mode;
    private final Parser parser;

    public IfLibraryLink(Library library, Parser parser, String expression) {
        super(library);
        try {
            this.parser = parser;
            if (parser.parse(expression, blackboard(), Boolean.class))
                execute();
        } catch (org.mazarineblue.parser.exceptions.ParserException ex) {
            throw new GeneralParseException(expression);
        }
    }

    @Keyword("Else if")
    @Keyword("Elseif")
    @Parameters(min = 1)
    public void flowElseif(String expression) {
        try {
            boolean waitingCondition = parser.parse(expression, blackboard(),
                                                    Boolean.class);
            next(waitingCondition, true);
        } catch (org.mazarineblue.parser.exceptions.ParserException ex) {
            throw new GeneralParseException(expression);
        }
    }

    @Keyword("Else")
    public void flowElse() {
        next();
    }

    @Keyword("End if")
    @Keyword("Endif")
    public void flowEndif() {
        executor().chain().remove(this);
    }

    @EventHandler
    public void eventHandler(EndSheetEvent event) {
        throw new BlockOpenOnEndSheetException("If statement", event.getSheetName());
    }
}
