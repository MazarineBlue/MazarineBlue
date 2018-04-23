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
package org.mazarineblue.libraries.test;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import org.mazarineblue.eventdriven.Invoker;
import org.mazarineblue.eventnotifier.Event;
import org.mazarineblue.eventnotifier.Subscriber;
import org.mazarineblue.eventnotifier.subscribers.ExceptionThrowingSubscriber;
import org.mazarineblue.keyworddriven.Keyword;
import org.mazarineblue.keyworddriven.Library;
import org.mazarineblue.keyworddriven.Parameters;
import org.mazarineblue.keyworddriven.PassInvoker;
import org.mazarineblue.keyworddriven.events.AddLibraryEvent;
import org.mazarineblue.keyworddriven.events.InstructionLineEvent;
import org.mazarineblue.keyworddriven.events.RemoveLibraryEvent;

public class GivenLibrary
        extends AbstractTestLibrary {

    private void x() {
        Predicate<Event> condi = InstructionLineEvent.matchesAnyKeywords(keywords)
        Predicate<Event> condition = new Predicate<Event>() {
            @Override
            public boolean test(Event t) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
        Function<Event, RuntimeException> supplier = new Function<Event, RuntimeException>() {
            @Override
            public RuntimeException apply(Event t) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
        Subscriber<Event> s = new ExceptionThrowingSubscriber<Event>(condition, supplier);
    }

    @Keyword("And")
    @Parameters(min = 1)
    @PassInvoker
    public void and(Invoker invoker, String phrase) {
        Library x = new XLibrary();
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Keyword("When")
    @PassInvoker
    public void when(Invoker invoker, String phrase) {
        invoker.publish(new RemoveLibraryEvent(this));
        invoker.publish(new AddLibraryEvent(new WhenLibrary()));
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
