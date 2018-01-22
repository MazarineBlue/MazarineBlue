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
package org.mazarineblue.engine.nestedinstructions;

import org.mazarineblue.eventnotifier.Event;
import org.mazarineblue.eventnotifier.EventHandler;
import org.mazarineblue.eventnotifier.ReflectionSubscriber;
import org.mazarineblue.keyworddriven.events.ExecuteInstructionLineEvent;
import org.mazarineblue.keyworddriven.events.InstructionLineEvent;
import org.mazarineblue.keyworddriven.events.ValidateInstructionLineEvent;

/**
 * This {@code NestedInstructionSubscriber} scans {@code InstructionLineEvent} for a argment that starts with an equals
 * symbol. If an equals symbol is found, then another {@code InstructionLineEvent} is created and published to the bus
 * and the result is used put in the current event as the last argument.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class NestedInstructionExecutor
        extends ReflectionSubscriber<Event> {

    private static final String NESTED_SYMBOL = "=";

    @EventHandler
    public void eventHandler(ValidateInstructionLineEvent event) {
        InstructionLineEventExtractor extractor = new InstructionLineEventExtractor(event);
        if (!extractor.hasNextedInstructionLineEvent())
            return;
        ValidateInstructionLineEvent e = new ValidateInstructionLineEvent(extractor.keyword(), extractor.arguments());
        event.invoker().publish(e);
        /*
         * We can not validate the main event, unless we have instance of the
         * result type. We can not get an instance by creating and publishing
         * an {@code ExceuteInstructionLineEvent} and, because this may have
         * side effect. The only alternative is to use a default constructor.
         * However, the most used objects (Numbers, Strings) do not have a
         * default constructor and we would waist time trying, therefore we
         * assume a default constructor never available.
         */
        event.setConsumed(true);
    }
    
    @EventHandler
    public void eventHandler(ExecuteInstructionLineEvent event) {
        InstructionLineEventExtractor extractor = new InstructionLineEventExtractor(event);
        if (!extractor.hasNextedInstructionLineEvent())
            return;
        ExecuteInstructionLineEvent e = new ExecuteInstructionLineEvent(extractor.keyword(), extractor.arguments());
        event.invoker().publish(e);
        extractor.setLastArgument(e.getResult());
    }

    private class InstructionLineEventExtractor {

        private final InstructionLineEvent event;
        private final int indexOfKeyword;

        public InstructionLineEventExtractor(InstructionLineEvent event) {
            this.event = event;
            this.indexOfKeyword = event.indexOf(t -> t instanceof String && t.toString().startsWith(NESTED_SYMBOL));
        }

        private boolean hasNextedInstructionLineEvent() {
            return indexOfKeyword != -1;
        }

        private String keyword() {
            return event.getArgument(indexOfKeyword).toString().substring(NESTED_SYMBOL.length());
        }

        private Object[] arguments() {
            return event.getArguments(indexOfKeyword + 1);
        }

        private void setLastArgument(Object result) {
            Object[] arguments = event.getArguments(0, indexOfKeyword + 1);
            arguments[indexOfKeyword] = result;
            event.setArguments(arguments);
        }
    }
}
