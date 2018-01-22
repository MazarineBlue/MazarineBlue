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

import java.util.Objects;
import org.mazarineblue.eventnotifier.Event;
import org.mazarineblue.keyworddriven.Library;
import org.mazarineblue.utilities.SerializableClonable;

/**
 * A {@code LibraryEvent} is the base {@link Event} of library events within
 * the keyword driven component.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
abstract class LibraryEvent
        extends KeywordDrivenEvent {

    private static final long serialVersionUID = 1L;

    private transient Library library;

    LibraryEvent(Library library) {
        this.library = library;
    }

    @Override
    public String toString() {
        return library.toString();
    }

    @Override
    public String message() {
        return "library=" + library.toString();
    }

    public Library getLibrary() {
        return library;
    }

    @Override
    public int hashCode() {
        return 5 * 13
                + Objects.hashCode(library);
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj != null && getClass() == obj.getClass()
                && Objects.equals(this.library, ((LibraryEvent) obj).library);
    }

    @Override
    public <E extends SerializableClonable> void copyTransient(E other) {
        super.copyTransient(other);
        library = ((LibraryEvent) other).library;
    }
}
