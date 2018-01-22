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
package org.mazarineblue.variablestore;

import org.junit.After;
import static org.junit.Assert.assertArrayEquals;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.eventdriven.Processor;
import org.mazarineblue.eventdriven.feeds.MemoryFeed;
import org.mazarineblue.keyworddriven.events.ExecuteInstructionLineEvent;
import org.mazarineblue.parser.exceptions.SemanticExpressionException;
import org.mazarineblue.variablestore.events.SetVariableEvent;
import org.mazarineblue.variablestore.subscribers.VariableParserSubscriber;

public class VariableParserSubscriberTest {

    private Processor processor;

    @Before
    public void setup() {
        VariableStoreSubscriber subscriber = new VariableStoreSubscriber("global");
        VariableParserSubscriber parserLink = new VariableParserSubscriber(subscriber);

        processor = Processor.newInstance();
        processor.addLink(subscriber);
        processor.addLink(parserLink);
    }

    @After
    public void teardown() {
        processor = null;
    }

    @Test(expected = SemanticExpressionException.class)
    public void fetchVariable_NonExisting() {
        processor.execute(new MemoryFeed(new ExecuteInstructionLineEvent("Set", "foo", "$key")));
    }

    @Test
    public void fetchVariable_Existing() {
        ExecuteInstructionLineEvent e = new ExecuteInstructionLineEvent("Set", "foo", "$key");
        processor.execute(new MemoryFeed(new SetVariableEvent("key", "bar"), e));
        assertArrayEquals(new Object[]{"foo", "bar"}, e.getArguments());
    }
}
