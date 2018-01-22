/*
 * Copyright (c) 2017 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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
package org.mazarineblue.variablestore.subscribers;

import org.mazarineblue.eventnotifier.Event;
import org.mazarineblue.eventnotifier.EventHandler;
import org.mazarineblue.eventnotifier.ReflectionSubscriber;
import org.mazarineblue.keyworddriven.events.InstructionLineEvent;
import org.mazarineblue.parser.Parser;
import org.mazarineblue.parser.StringVariableParser;
import org.mazarineblue.parser.VariableStoreSource;
import org.mazarineblue.variablestore.VariableStoreSubscriber;

public class VariableParserSubscriber
        extends ReflectionSubscriber<Event> {

    private final Parser<String, Object> parser;

    public VariableParserSubscriber(VariableStoreSubscriber variableStore) {
        parser = new StringVariableParser(new VariableStoreSource(variableStore));
    }

    @EventHandler
    public void eventHandler(InstructionLineEvent event) {
        Object[] argument = event.getArguments();
        for (int i = 0; i < argument.length; ++i)
            if (argument[i] instanceof String)
                argument[i] = parser.parse((String) argument[i]);
        event.setArguments(argument);
    }
}
