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
package org.mazarineblue.executors.links;

import java.util.Objects;
import org.mazarineblue.eventbus.EventHandler;
import org.mazarineblue.eventdriven.Interpreter;
import org.mazarineblue.eventdriven.Link;
import org.mazarineblue.executors.FeedExecutorFactory;
import org.mazarineblue.executors.events.CreateFeedExecutorEvent;

/**
 * A {@code DefaulExecutorFactoryLink} is a link that contains default
 * functionality.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class DefaulExecutorFactoryLink
        extends Link {

    private final FeedExecutorFactory factory;
    
    public DefaulExecutorFactoryLink(FeedExecutorFactory factory) {
        this.factory = factory;
    }
    
    /**
     * Event handlers are not meant to be called directly, instead publish an
     * event to an {@link Interpreter}; please see the specified event for more
     * information about this event handler.
     *
     * @param event the event this {@code EventHandler} processes.
     * @see CreateFeedExecutorEvent
     */
    @EventHandler
    public void eventHandler(CreateFeedExecutorEvent event) {
        event.setResult(factory.create());
        event.setConsumed(true);
    }

    @Override
    public int hashCode() {
        return 41 * 7 + Objects.hashCode(this.factory);
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj ||  obj != null && getClass() == obj.getClass()
                && Objects.equals(this.factory, ((DefaulExecutorFactoryLink) obj).factory);
    }
}
