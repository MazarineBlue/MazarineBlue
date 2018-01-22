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

import org.mazarineblue.eventdriven.exceptions.FeedNotPublicException;
import org.mazarineblue.eventdriven.exceptions.FeedTargetException;
import org.mazarineblue.eventnotifier.Event;
import org.mazarineblue.eventnotifier.EventHandlerCaller;

/**
 * A {@code AbstractFeed} is a {@code Feed}, which provides a default implementation
 * for the {@code done} method.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public abstract class AbstractFeed
        implements Feed {

    private final EventHandlerCaller<Event> caller = new ExceptionCreationCaller(this);

    @Override
    public void done(Event event) {
        caller.publish(event, e -> false);
    }

    private static class ExceptionCreationCaller
            extends EventHandlerCaller<Event> {

        ExceptionCreationCaller(Feed owner) {
            super(owner);
        }

        @Override
        public RuntimeException createPublicClassDeclarationRequiredException() {
            throw new FeedNotPublicException(getOwner());
        }

        @Override
        public RuntimeException createTargetException(Throwable cause) {
            throw new FeedTargetException(cause);
        }

        @Override
        protected void unpublished(Event event) {
            // Nothing we can do with unpublished events.
        }
    }
}
