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
package org.mazarineblue.logger;

import static java.util.Arrays.stream;
import org.mazarineblue.eventbus.Event;
import org.mazarineblue.eventdriven.listeners.PublisherListener;

/**
 * A {@code PublisherListenerFilter} is a {@code PublisherListener} that
 * filters messages based on the class types and whether or not there is is a
 * matching opening message.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
class PublisherListenerFilter
        implements PublisherListener {

    private int level = 0;
    private final PublisherListener adaptee;
    private final Class<?>[] ignoreTypes;

    PublisherListenerFilter(PublisherListener adaptee, Class<?>... ignoreTypes) {
        this.adaptee = adaptee;
        this.ignoreTypes = ignoreTypes;
    }

    @Override
    public void startPublishingEvent(Event event) {
        if (isEventToBeIgnored(event))
            return;
        increaseLevel();
        adaptee.startPublishingEvent(event);
    }

    protected void increaseLevel() {
        ++level;
    }

    @Override
    public void exceptionThrown(Event event, RuntimeException ex) {
        if (isEventToBeIgnored(event))
            return;
        adaptee.exceptionThrown(event, ex);
    }

    @Override
    public void endPublishedEvent(Event event) {
        if (isEventToBeIgnored(event) || isOutOfRange())
            return;
        decrease();
        adaptee.endPublishedEvent(event);
    }

    private boolean isEventToBeIgnored(Event event) {
        return stream(ignoreTypes).anyMatch(type -> (type.isAssignableFrom(event.getClass())));
    }

    protected boolean isOutOfRange() {
        return level <= 0;
    }

    protected void decrease() {
        --level;
    }
}
