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
package org.mazarineblue.keyworddriven.events;

import org.mazarineblue.keyworddriven.Library;

/**
 * An {@code AddLibraryEvent} instructs a {@link LibraryRegistry} to unregister
 * the contained {@link Library}.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 * @see LibraryRegistry
 */
public class RemoveLibraryEvent
        extends LibraryEvent {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a {@code RemoveLibraryEvent} with an specified
     * {@code Library} to remove from a {@code LibraryRegistry}.
     *
     * @param library the library to add to a {@code LibraryRegistry}.
     */
    public RemoveLibraryEvent(Library library) {
        super(library);
    }
}
