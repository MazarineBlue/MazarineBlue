/*
 * Copyright (c) Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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

import java.util.function.Predicate;
import org.mazarineblue.eventdriven.Invoker;
import org.mazarineblue.eventnotifier.Event;
import org.mazarineblue.executors.AbstractMainLibrary;
import org.mazarineblue.keyworddriven.Keyword;
import org.mazarineblue.keyworddriven.PassInvoker;
import static org.mazarineblue.keyworddriven.events.InstructionLineEvent.matchesAnyKeywords;
import org.mazarineblue.keyworddriven.events.InstructionLineResultEvent;
import org.mazarineblue.subscribers.SkipEventsSubscriber;

public class FunctionExecutionLibrary
        extends AbstractMainLibrary {

    private final InstructionLineResultEvent store;
    private final Predicate<Event> skipUntilEndFunction = matchesAnyKeywords("End function");

    FunctionExecutionLibrary(InstructionLineResultEvent store) {
        this.store = store;
    }

    @Keyword("Return")
    @PassInvoker
    public void result(Invoker invoker, Object result) {
        store.setResult(result);
        invoker.processor().addLink(new SkipEventsSubscriber(invoker, skipUntilEndFunction));
    }
}
