/*
 * Copyright (c) 2018 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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
package org.mazarineblue.functions;

import java.util.Collection;
import org.mazarineblue.eventdriven.Invoker;
import org.mazarineblue.eventdriven.feeds.MemoryFeed;
import org.mazarineblue.eventnotifier.Event;
import org.mazarineblue.keyworddriven.Library;
import org.mazarineblue.keyworddriven.events.AddLibraryEvent;
import org.mazarineblue.keyworddriven.events.ExecuteInstructionLineEvent;
import org.mazarineblue.keyworddriven.events.RemoveLibraryEvent;
import static org.mazarineblue.keyworddriven.util.GracefullConvertor.degraceMethod;
import org.mazarineblue.variablestore.events.EndVariableScopeEvent;
import org.mazarineblue.variablestore.events.SetVariableEvent;
import org.mazarineblue.variablestore.events.StartVariableScopeEvent;
import org.mazarineblue.keyworddriven.events.InstructionLineResultEvent;

class Function {

    private final String name;
    private final String[] parameters;
    private Collection<Event> recording;

    Function(String name, String... parameters) {
        this.name = degraceMethod(name);
        this.parameters = parameters;
    }

    String getName() {
        return name;
    }

    void setBody(Collection<Event> recording) {
        this.recording = recording;
    }

    void execute(ExecuteInstructionLineEvent event) {
        event.invoker().publish(new StartVariableScopeEvent());
        Object[] arguments = event.getArguments();
        setArguments(event.invoker(), arguments);
        event.invoker().processor().execute(getScopedFeed(event));
        event.invoker().publish(new EndVariableScopeEvent());
        event.setConsumed(true);
    }

    private void setArguments(Invoker invoker, Object[] arguments) {
        for (int i = 0; i < parameters.length && i < arguments.length; ++i) {
            String symbol = parameters[i];
            Object value = arguments[i];
            invoker.publish(new SetVariableEvent(symbol, value));
        }
    }

    private MemoryFeed getScopedFeed(InstructionLineResultEvent store) {
        Library library = new FunctionExecutionLibrary(store);
        MemoryFeed feed = new MemoryFeed(new AddLibraryEvent(library));
        feed.addAll(recording);
        feed.add(new RemoveLibraryEvent(library));
        return feed;
    }
}
