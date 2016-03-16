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

/**
 * A {@code InterpreterListener} is a listener that, when a feed is opened or
 * closed or the chain was modified, reports the feed that was opened or
 * closed or the link that was added or removed from the chain.
 * <p>
 * A {@code InterpreterListener} can usually be implementing
 * {@link FeedExecutorListener} and {@link ChainModifierListener} and created a
 * {@link InterpreterListenerAdapter} with those. This is recommended, if
 * possible, because it allows those implementation to be used separately.
 *
 * @see InterpreterListenerAdapter
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public interface InterpreterListener
        extends FeedExecutorListener, ChainModifierListener {

    public static InterpreterListener getDummy() {
        return new InterpreterListenerAdapter(new DummyFeedExecutorListener(), new DummyChainModifierListener());
    }
}
