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
package org.mazarineblue.libraries.test;

import org.mazarineblue.eventdriven.Invoker;
import org.mazarineblue.eventnotifier.Event;
import org.mazarineblue.keyworddriven.Keyword;
import org.mazarineblue.keyworddriven.Parameters;
import org.mazarineblue.keyworddriven.PassInvoker;
import org.mazarineblue.keyworddriven.events.AddLibraryEvent;
import org.mazarineblue.keyworddriven.events.ExecuteInstructionLineEvent;
import org.mazarineblue.keyworddriven.events.RemoveLibraryEvent;

public class BDDLibrary
        extends AbstractTestLibrary {

    @Keyword("Given")
    @PassInvoker
    public void given(Invoker invoker, String phrase) {
        invoker.publish(new RemoveLibraryEvent(this));
        invoker.publish(convertTestPhrase("given", phrase));
        invoker.publish(new AddLibraryEvent(new GivenLibrary()));
    }

    private static Event convertTestPhrase(String prefix, String phrase) {
        String keyword = prefix.trim() + " " + phrase.trim();
        return new ExecuteInstructionLineEvent(keyword);
    }

    @SuppressWarnings("PublicInnerClass")
    public static class GivenLibrary
            extends AbstractTestLibrary {

        private GivenLibrary() {
            // Only after a call to "Given" should this library be instantiated.
        }

        @Keyword("And")
        @Parameters(min = 1)
        @PassInvoker
        public void and(Invoker invoker, String phrase) {
            invoker.publish(new RemoveLibraryEvent(this));
            invoker.publish(convertTestPhrase("given", phrase));
            invoker.publish(new AddLibraryEvent(new GivenLibrary()));
        }

        @Keyword("When")
        @PassInvoker
        public void when(Invoker invoker, String phrase) {
            invoker.publish(new RemoveLibraryEvent(this));
            invoker.publish(convertTestPhrase("when", phrase));
            invoker.publish(new AddLibraryEvent(new WhenLibrary()));
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public static class WhenLibrary
            extends AbstractTestLibrary {

        private WhenLibrary() {
            // Only after a call to "When" should this library be instantiated.
        }

        @Keyword("And")
        @PassInvoker
        public void and(Invoker invoker, String phrase) {
            invoker.publish(new RemoveLibraryEvent(this));
            invoker.publish(convertTestPhrase("when", phrase));
            invoker.publish(new AddLibraryEvent(new WhenLibrary()));
        }

        @Keyword("Then")
        @PassInvoker
        public void then(Invoker invoker, String phrase) {
            invoker.publish(new RemoveLibraryEvent(this));
            invoker.publish(convertTestPhrase("then", phrase));
            invoker.publish(new AddLibraryEvent(new ThenLibrary()));
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public static class ThenLibrary
            extends AbstractTestLibrary {

        private ThenLibrary() {
            // Only after a call to "Then" should this library be instantiated.
        }

        @Keyword("And")
        @PassInvoker
        public void and(Invoker invoker, String phrase) {
            invoker.publish(new RemoveLibraryEvent(this));
            invoker.publish(convertTestPhrase("then", phrase));
            invoker.publish(new AddLibraryEvent(new ThenLibrary()));
        }
    }
}
