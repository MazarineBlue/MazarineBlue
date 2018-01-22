/*
 * Copyright (c) 2015 Specialisterren
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

import java.util.List;
import java.util.function.Predicate;
import org.mazarineblue.keyworddriven.Library;
import org.mazarineblue.keyworddriven.LibraryRegistry;

/**
 * An {@code ContainsLibrariesEvent} is used to query a {@link LibraryRegistry}
 * if it contains a {@link Library} that matches a specified function.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 * @see LibraryRegistry
 * @see Library
 */
public class ContainsLibrariesEvent
        extends FilterLibrariesEvent {

    private static final long serialVersionUID = 1L;

    private boolean found;

    /**
     * Constructs an {@code FetchLibrariesEvent} that fetches all
     * {@code Libraries}, i.e. has a filter that always returns true.
     */
    public ContainsLibrariesEvent() {
        super();
    }

    /**
     * Constructs an {@code FetchLibrariesEvent} with an specified filter
     * function.
     *
     * @param filter the function that used to see if the {@code Library}
     *               should be returned by {@code getLbiraries()}.
     * @see #getLibraries()
     */
    public ContainsLibrariesEvent(Predicate<Library> filter) {
        super(filter);
    }

    @Override
    public String toString() {
        return "found=" + found;
    }

    @Override
    public String responce() {
        return "found=" + found;
    }

    public boolean found() {
        return found;
    }

    public void contains(List<Library> libraries) {
        found = libraries.stream().anyMatch(this);
    }

    @Override
    public int hashCode() {
        return 43 * 43 * 5
                + 43 * super.hashCode()
                + (this.found ? 1 : 0);
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj != null && getClass() == obj.getClass()
                && found == ((ContainsLibrariesEvent) obj).found
                && super.equals(obj);
    }
}
