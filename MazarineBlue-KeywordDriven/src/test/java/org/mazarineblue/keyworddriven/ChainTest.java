/*
 * Copyright (c) 2015 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mazarineblue.keyworddriven.links.Chain;
import org.mazarineblue.keyworddriven.links.Link;
import org.mazarineblue.keyworddriven.util.ConsumingLink;
import org.mazarineblue.keyworddriven.util.DummyEvent;
import org.mazarineblue.keyworddriven.util.DummyLink;
import org.mazarineblue.keyworddriven.util.LinkSpy;

/**
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
@RunWith(HierarchicalContextRunner.class)
public class ChainTest {

    private Chain chain;

    public class GivenEmptyChain {

        @Before
        public void setup() {
            chain = new Chain();
        }

        @Test
        public void emptyChain() {
            assertEquals(0, chain.size());
        }

        @Test
        public void insert_Link() {
            Link link = new DummyLink();
            chain.insert(link);
            assertEquals(1, chain.size());
        }
    }

    public class GivenDummyLink {

        private Link link;

        @Before
        public void setup() {
            link = new DummyLink();
            chain = new Chain(link);
        }

        @Test
        public void remove_AddedLink() {
            boolean flag = chain.remove(link);
            assertEquals(true, flag);
            assertEquals(0, chain.size());
        }

        @Test
        public void remove_NotAddedLink() {
            boolean flag = chain.remove(new DummyLink());
            assertEquals(false, flag);
            assertEquals(1, chain.size());
        }
    }

    @Test
    public void publish() {
        LinkSpy spy1 = new LinkSpy();
        LinkSpy spy2 = new LinkSpy();
        chain = new Chain(spy1, new ConsumingLink(), spy2);
        chain.publish(new DummyEvent());
        assertEquals(1, spy1.getEvents());
        assertEquals(0, spy2.getEvents());
    }
}
