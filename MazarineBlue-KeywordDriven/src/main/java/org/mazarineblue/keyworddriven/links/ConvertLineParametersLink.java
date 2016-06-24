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

import org.mazarineblue.datasources.DataSource;
import org.mazarineblue.eventbus.EventHandler;
import org.mazarineblue.events.instructions.InstructionLineEvent;
import org.mazarineblue.keyworddriven.InstructionLine;
import org.mazarineblue.keyworddriven.InterpreterContext;
import org.mazarineblue.keyworddriven.logs.Log;
import org.mazarineblue.parser.Parser;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class ConvertLineParametersLink
        implements Link {

    private final Parser parser;

    public ConvertLineParametersLink(Parser parser) {
        this.parser = parser;
    }

    @EventHandler
    public void eventHandler(InstructionLineEvent event) {
        InstructionLine line = event.getLine();
        if (line.isEmpty())
            return;
        DataSource source = event.getDataSource();
        InstructionLine convertedLine = convertInstructionLine(line, source);
        log(line, event);
        event.push(convertedLine);
    }

    private InstructionLine convertInstructionLine(InstructionLine line, DataSource source) {
        Object[] parameters = line.getParameters();
        Object[] convertedParameters = convertParameters(parameters, source);
        return line.cloneAndReplaceParameters(convertedParameters);
    }

    private Object[] convertParameters(Object[] parameters, DataSource source) {
        for (int i = 0; i < parameters.length; ++i)
            parameters[i] = parser.parse((String) parameters[i], source);
        return parameters;
    }

    private void log(InstructionLine line, InstructionLineEvent event) {
        Object[] parameters = line.getParameters();
        InterpreterContext context = event.getContext();
        Log log = context.log();
        log.setActualParameters(parameters);
    }
}
