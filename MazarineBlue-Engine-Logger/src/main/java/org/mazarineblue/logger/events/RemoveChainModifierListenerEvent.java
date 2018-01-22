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
package org.mazarineblue.logger.events;

import org.mazarineblue.eventdriven.listeners.ChainModifierListener;
import org.mazarineblue.logger.EngineLogger;

/**
 * A {@code RemoveChainModifierListenerEvent} is an event that signals to
 * {@link EngineLogger} to unregister the contained listener to the notified
 * list.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class RemoveChainModifierListenerEvent
        extends AbstractChainModifierListenerEvent {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a {@code RemoveChainModifierListenerEvent} to request to register
     * the specified listener.
     *
     * @param listener the listener to register.
     */
    public RemoveChainModifierListenerEvent(ChainModifierListener listener) {
        super(listener);
    }
}
