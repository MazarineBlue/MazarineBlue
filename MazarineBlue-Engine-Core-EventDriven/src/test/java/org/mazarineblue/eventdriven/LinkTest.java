/*
 * Copyright (c) 2015-2016 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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
import org.mazarineblue.eventdriven.feeds.MemoryFeed;
import org.mazarineblue.eventdriven.util.FireEvent;
import org.mazarineblue.eventdriven.util.FireLink;
import org.mazarineblue.eventdriven.util.LinkSpy;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class LinkTest {

    private Interpreter interpretor;

    @Before
    public void setup() {
        interpretor = Interpreter.newInstance();
    }

    @After
    public void teardown() {
        interpretor = null;
    }

    @Test
    public void test() {
        MemoryFeed feed = new MemoryFeed(4);
        feed.add(new FireEvent());
        LinkSpy link = new LinkSpy();
        interpretor.addLink(link);
        interpretor.addLink(new FireLink());
        interpretor.execute(feed);
        assertEquals(1, link.size());
    }
}
