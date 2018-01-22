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
package org.mazarineblue.libraries.test.model;

import org.mazarineblue.eventdriven.listeners.PublisherListener;
import org.mazarineblue.eventnotifier.Event;
import org.mazarineblue.libraries.test.model.listeners.TestListener;
import org.mazarineblue.libraries.test.model.tests.Test;

class TestPublisherListener
        implements PublisherListener {

    private final TestListener listener;
    private final Test test;

    TestPublisherListener(TestListener listener, Test test) {
        this.listener = listener;
        this.test = test;
    }

    @Override
    public void startPublishingEvent(Event event) {
        test.result().startWithEvent(event);
        listener.startPublishingEvent(test, event);
    }

    @Override
    public void exceptionThrown(Event event, RuntimeException ex) {
        test.result().setFailed();
        listener.exceptionThrown(test, ex);
    }

    @Override
    public void endPublishedEvent(Event event) {
        test.result().endWithEvent(event);
        listener.endPublishedEvent(test, event);
    }
}
