/*
 * Copyright (c) 2015 Specialisterren
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
package org.mazarineblue.events.library;

import org.mazarineblue.eventbus.AbstractEvent;
import org.mazarineblue.keyworddriven.exceptions.MultipleLibrariesFoundException;
import org.mazarineblue.keyworddriven.librarymanager.Library;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class FetchLibraryEvent<T extends Library>
        extends AbstractEvent {

    private final Class type;
    private T library;

    public FetchLibraryEvent(Class<T> type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "FetchLibrary{" + "type=" + type + '}';
    }

    public T getLibrary() {
        return library;
    }

    public void setLibrary(T library) {
        if (this.library != null)
            throw new MultipleLibrariesFoundException(type);
        this.library = library;
    }

    public boolean containsLibraryClass(Class<? extends Library> type) {
        return this.type.isAssignableFrom(type);
    }
}
