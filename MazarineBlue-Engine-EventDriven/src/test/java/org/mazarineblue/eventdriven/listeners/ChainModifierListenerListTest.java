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
import org.mazarineblue.eventdriven.util.listeners.ProcessorListenerSpy;
import org.mazarineblue.eventnotifier.subscribers.DummySubscriber;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class ChainModifierListenerListTest {

    private ProcessorListenerSpy spy1, spy2;
    private ChainModifierListenerList list;

    @Before
    public void setup() {
        spy1 = new ProcessorListenerSpy();
        spy2 = new ProcessorListenerSpy();
        list = new ChainModifierListenerList();
        list.addListener(spy1);
        list.addListener(spy2);
    }

    @After
    public void teardown() {
        spy1 = spy2 = null;
        list = null;
    }

    @Test
    public void addedLink() {
        list.addedLink(new DummySubscriber<>());
        list.removeListener(spy2);
        list.addedLink(new DummySubscriber<>());
        spy1.links().assertClasses(DummySubscriber.class, DummySubscriber.class);
        spy2.links().assertClasses(DummySubscriber.class);
    }

    @Test
    public void addedLink_AfterLink() {
        list.addedLink(new DummySubscriber<>(), new DummySubscriber<>());
        list.removeListener(spy2);
        list.addedLink(new DummySubscriber<>(), new DummySubscriber<>());
        spy1.links().assertClasses(DummySubscriber.class, DummySubscriber.class);
        spy2.links().assertClasses(DummySubscriber.class);
    }

    @Test
    public void removedLink() {
        spy1.addedLink(new DummySubscriber<>());
        spy1.addedLink(new DummySubscriber<>());
        spy1.addedLink(new DummySubscriber<>());
        spy2.addedLink(new DummySubscriber<>());
        spy2.addedLink(new DummySubscriber<>());
        list.removedLink(new DummySubscriber<>());
        list.removeListener(spy2);
        list.removedLink(new DummySubscriber<>());
        spy1.links().assertClasses(DummySubscriber.class);
        spy2.links().assertClasses(DummySubscriber.class);
    }
}
