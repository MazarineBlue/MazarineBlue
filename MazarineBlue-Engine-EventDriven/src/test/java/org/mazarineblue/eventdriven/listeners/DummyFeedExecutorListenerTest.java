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

public class DummyFeedExecutorListenerTest {

    private DummyFeedExecutorListener listener;

    @Before
    public void setup() {
        listener = new DummyFeedExecutorListener();
    }

    @After
    public void teardown() {
        listener = null;
    }

    @Test
    public void openingFeed() {
        listener.openingFeed(null);
    }

    @Test
    public void closingFeed() {
        listener.closingFeed(null);
    }

    @Test
    public void startPublishingEvent() {
        listener.startPublishingEvent(null);
    }

    @Test
    public void exceptionThrown() {
        listener.exceptionThrown(null, null);
    }

    @Test
    public void endPublishedEvent() {
        listener.endPublishedEvent(null);
    }
}
