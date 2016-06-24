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

import org.mazarineblue.events.instructions.InstructionLineEvent;
import org.mazarineblue.keyworddriven.Keyword;
import org.mazarineblue.keyworddriven.librarymanager.Library;
import org.mazarineblue.keyworddriven.links.LibraryLink;
import org.mazarineblue.parser.Parser;
import org.mazarineblue.parser.exceptions.ParserException;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class BreakLibraryLink
        extends LibraryLink {

    private final Parser parser;
    private boolean broken = false;

    public BreakLibraryLink(Library library, Parser parser) {
        super(library);
        this.parser = parser;
    }

    @Override
    protected void processNonkeyEvents(InstructionLineEvent event) {
    }

    @Keyword("Break")
    public void flowBreak() {
        broken = true;
    }

    void reset() {
        broken = false;
    }

    public boolean isBroken() {
        return broken;
    }

    public boolean canExecute(String expression)
            throws ParserException {
        boolean parse = parser.parse(expression, blackboard(), Boolean.class);
        return parse && broken == false;
    }
}
