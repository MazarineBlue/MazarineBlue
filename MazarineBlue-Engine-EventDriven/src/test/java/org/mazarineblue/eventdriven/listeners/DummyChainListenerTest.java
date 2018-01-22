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
package org.mazarineblue.eventdriven.listeners;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DummyChainListenerTest {

    private DummyChainModifierListener listener;

    @Before
    public void setup() {
        listener = new DummyChainModifierListener();
    }

    @After
    public void teardown() {
        listener = null;
    }

    @Test
    public void addedLink() {
        listener.addedLink(null);
    }

    @Test
    public void addedLink2() {
        listener.addedLink(null, null);
    }

    @Test
    public void removedLink() {
        listener.removedLink(null);
    }
}
