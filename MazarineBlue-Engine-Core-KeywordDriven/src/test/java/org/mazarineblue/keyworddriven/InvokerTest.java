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
package org.mazarineblue.keyworddriven;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import static org.mazarineblue.eventbus.Event.matchesNoneAutoConsumable;
import org.mazarineblue.eventbus.events.TestEvent;
import org.mazarineblue.eventbus.link.EventBusLink;
import org.mazarineblue.eventdriven.Interpreter;
import org.mazarineblue.eventdriven.Invoker;
import org.mazarineblue.eventdriven.feeds.MemoryFeed;
import org.mazarineblue.eventdriven.util.ChainModifierListenerSpy;
import org.mazarineblue.eventdriven.util.DummyLink;
import org.mazarineblue.eventdriven.util.FeedExecutorListenerSpy;
import org.mazarineblue.keyworddriven.events.ExecuteInstructionLineEvent;
import org.mazarineblue.keyworddriven.events.KeywordDrivenEvent;

/**
 * ************************************************************************* *
 * This class lives in the KeywordDriven component, while the Invoker class *
 * lives in EventDriven component, because the KeywordDriven is used to *
 * execute a method. This class can only be moved, if it no longer uses the *
 * KeywordDriven component, but instead only uses the EventDriven component. *
 *                                                                            *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org> *
 * ************************************************************* ,,^..^,, ***
 */
public class InvokerTest {

    @Test
    public void test() {
        ChainModifierListenerSpy chainModifiedListenerSpy = new ChainModifierListenerSpy();
        FeedExecutorListenerSpy feedExecuteListenerSpy = new FeedExecutorListenerSpy(matchesNoneAutoConsumable());
        EventBusLink link = new EventBusLink();
        link.subscribe(KeywordDrivenEvent.class, null, new LibraryRegistry(new TestInvokerLibrary()));

        Interpreter interpreter = Interpreter.newInstance();
        interpreter.addLink(link);
        interpreter.setChainModifierListener(chainModifiedListenerSpy);
        interpreter.setFeedExecutorListener(feedExecuteListenerSpy);
        interpreter.execute(new MemoryFeed(new ExecuteInstructionLineEvent("foo")));

        assertEquals(1, chainModifiedListenerSpy.countLinks());
        assertEquals(2, feedExecuteListenerSpy.countOpeningFeed());
        assertEquals(2, feedExecuteListenerSpy.countClosingFeed());
        assertEquals(4, feedExecuteListenerSpy.countStartEvents());
        assertEquals(0, feedExecuteListenerSpy.countExceptions());
        assertEquals(4, feedExecuteListenerSpy.countEndEvents());
    }

    @SuppressWarnings("PublicInnerClass")
    public static class TestInvokerLibrary
            extends Library {

        TestInvokerLibrary() {
            super("");
        }

        @PassInvoker
        @Keyword("foo")
        public void foo(Invoker invoker) {
            invoker.interpreter().execute(new MemoryFeed(new TestEvent()));
            invoker.chain().addLink(new DummyLink());
            invoker.publish(new ExecuteInstructionLineEvent("oof"));
        }

        @PassInvoker
        @Keyword("oof")
        public void oof(Invoker invoker) {
            invoker.publish(new TestEvent());
        }
    }
}
