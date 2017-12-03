/*
 * Copyright (c) 2016 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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
package org.mazarineblue.eventdriven;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.eventbus.events.TestEvent;
import org.mazarineblue.eventdriven.exceptions.LinkNotInChainException;
import org.mazarineblue.eventdriven.exceptions.NullLinkException;
import org.mazarineblue.eventdriven.util.LinkSpy;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class ChainManagerTest {

    private ChainImpl chain;

    @Before
    public void setup() {
        chain = new ChainImpl();
    }

    @After
    public void teardown() {
        chain = null;
    }

    @Test(expected = NullLinkException.class)
    public void addLink_NULL_ThrowsException() {
        chain.addLink(null);
    }

    @Test
    public void emptyLink() {
        assertEquals(0, chain.countLinks());
    }

    @Test
    public void addLink_SingleLink_Accepted() {
        chain.addLink(new LinkSpy());
        assertEquals(1, chain.countLinks());
    }

    @Test
    public void addLink_TreeLinks_Accepted() {
        LinkSpy first = new LinkSpy();
        LinkSpy second = new LinkSpy();
        LinkSpy thirth = new LinkSpy();
        chain.addLink(first);
        chain.addLink(second);
        chain.addLink(thirth);
        assertEquals(3, chain.countLinks());
        assertEquals(first, chain.removeLink());
        assertEquals(second, chain.removeLink());
        assertEquals(thirth, chain.removeLink());
    }

    @Test(expected = LinkNotInChainException.class)
    public void addLinkInsertAfter_LinkNotInChain_ThrowsException() {
        chain.addLink(new LinkSpy(), new LinkSpy());
    }

    @Test(expected = LinkNotInChainException.class)
    public void addLinkInsertAfter_Null_ThrowsException() {
        chain.addLink(new LinkSpy(), null);
    }

    @Test(expected = NullLinkException.class)
    public void addLinkInsertAfter_Null2_ThrowsException() {
        chain.addLink(null, new LinkSpy());
    }

    @Test(expected = LinkNotInChainException.class)
    public void addLink_InsertNullAfterNonExistingLink_ThrowsException() {
        chain.addLink(new LinkSpy(), null);
    }

    @Test
    public void addLink_InsertAfterLink_Accepted() {
        LinkSpy first = new LinkSpy();
        LinkSpy second = new LinkSpy();
        LinkSpy thirth = new LinkSpy();
        LinkSpy forth = new LinkSpy();
        chain.addLink(second);
        chain.addLink(first);
        chain.addLink(thirth, first);
        chain.addLink(forth, second);
        assertEquals(4, chain.countLinks());
        assertEquals(forth, chain.removeLink());
        assertEquals(second, chain.removeLink());
        assertEquals(thirth, chain.removeLink());
        assertEquals(first, chain.removeLink());
    }

    @Test(expected = NullLinkException.class)
    public void removeLink_NullLink_ThrowsException() {
        chain.removeLink(null);
    }

    @Test(expected = LinkNotInChainException.class)
    public void removeLink_EmptyChain_ThrowsException() {
        chain.removeLink(new LinkSpy());
    }

    @Test(expected = LinkNotInChainException.class)
    public void removeLink_DifferentLink_ThrowsException() {
        chain.addLink(new LinkSpy());
        chain.removeLink(new LinkSpy());
    }

    @Test
    public void removeLink_SingleLink_Accepts() {
        LinkSpy first = new LinkSpy();
        chain.addLink(first);
        chain.removeLink(first);
        assertEquals(0, chain.countLinks());
    }

    @Test
    public void removeLink_MultipleLink_Accepts() {
        LinkSpy first = new LinkSpy();
        LinkSpy second = new LinkSpy();
        LinkSpy thirth = new LinkSpy();
        chain.addLink(first);
        chain.addLink(second);
        chain.addLink(thirth);
        chain.removeLink(first);
        assertEquals(2, chain.countLinks());
        assertEquals(second, chain.removeLink());
        assertEquals(thirth, chain.removeLink());
    }

    @Test(expected = NullPointerException.class)
    public void publish_Null_ThrowsException() {
        LinkSpy first = new LinkSpy();
        chain.addLink(first);
        chain.publish(null);
    }

    @Test
    public void publish_Event_ThrowsException() {
        LinkSpy link = new LinkSpy();
        chain.addLink(link);
        chain.publish(new TestEvent());
        assertEquals(1, link.size());
    }
}
