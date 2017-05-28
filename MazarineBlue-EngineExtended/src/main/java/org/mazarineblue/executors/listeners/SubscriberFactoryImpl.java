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
package org.mazarineblue.executors.listeners;

import org.mazarineblue.eventbus.Event;
import org.mazarineblue.eventbus.Filter;
import org.mazarineblue.eventbus.Subscriber;

class SubscriberFactoryImpl
        implements SubscriberFactory {

    private final Class<?> eventType;
    private final Filter<Event> filter;
    private final Subscriber<Event> subscriber;

    SubscriberFactoryImpl(Class<?> eventType, Filter<Event> filter, Subscriber<Event> subscriber) {
        this.eventType = eventType;
        this.filter = filter;
        this.subscriber = subscriber;
    }

    @Override
    public Class<?> getEventType() {
        return eventType;
    }

    @Override
    public Filter<Event> getFilter() {
        return filter;
    }

    @Override
    public Subscriber<Event> getSubscriber() {
        return subscriber;
    }
}
