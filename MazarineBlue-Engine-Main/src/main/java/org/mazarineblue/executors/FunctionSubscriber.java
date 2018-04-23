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
package org.mazarineblue.executors;

import org.mazarineblue.eventnotifier.Event;
import org.mazarineblue.eventnotifier.EventHandler;
import org.mazarineblue.eventnotifier.ReflectionSubscriber;
import org.mazarineblue.keyworddriven.events.ExecuteInstructionLineEvent;
import org.mazarineblue.keyworddriven.events.IsAbleToProcessInstructionLineEvent;
import org.mazarineblue.keyworddriven.events.ValidateInstructionLineEvent;

public class FunctionSubscriber
        extends ReflectionSubscriber<Event> {

    private final FunctionRegistry registry;

    FunctionSubscriber(FunctionRegistry registry) {
        this.registry = registry;
    }

    @EventHandler
    public void eventHandler(IsAbleToProcessInstructionLineEvent event) {
        if (!registry.containsFunction(event.getKeyword()))
            return;
        event.setResult(true);
        event.setConsumed(true);
    }

    @EventHandler
    public void eventHandler(ValidateInstructionLineEvent event) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @EventHandler
    public void eventHandler(ExecuteInstructionLineEvent event) {
        if (registry.containsFunction(event.getKeyword()))
            registry.getFunction(event.getKeyword()).execute(event);
    }
}