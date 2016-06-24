/*
 * Copyright (c) 2015 Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
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

import org.mazarineblue.keyworddriven.links.Chain;
import org.mazarineblue.parser.Parser;
import org.mazarineblue.parser.VariableSource;
import org.mazarineblue.parser.exceptions.ParserException;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class LoopTester {

    private final Parser variableparser;
    private final BreakLibraryLink breakLink;
    private final VariableSource source;

    public LoopTester(Parser variableParser, BreakLibraryLink breakLink,
                      VariableSource source) {
        this.variableparser = variableParser;
        this.breakLink = breakLink;
        this.source = source;
    }

    public boolean continueLoop(String expression)
            throws ParserException {
        String result = variableparser.parse(expression, source, String.class);
        return breakLink.canExecute(result);
    }

    boolean isBroken() {
        return breakLink.isBroken();
    }

    void removeLink(Chain chain) {
        chain.remove(breakLink);
    }
}
