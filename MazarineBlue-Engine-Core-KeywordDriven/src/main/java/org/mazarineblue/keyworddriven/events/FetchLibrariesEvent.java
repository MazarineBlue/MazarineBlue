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

import java.util.ArrayList;
import static java.util.Collections.unmodifiableList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import org.mazarineblue.keyworddriven.Library;
import org.mazarineblue.keyworddriven.LibraryRegistry;
import org.mazarineblue.utililities.SerializableClonable;

/**
 * An {@code FetchLibrariesEvent} is used to query a {@link LibraryRegistry}
 * for {@link Library Libraries} that matches a specified function.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 * @see LibraryRegistry
 * @see Library
 */
public class FetchLibrariesEvent
        extends KeywordDrivenEvent {

    private static final long serialVersionUID = 1L;

    private transient List<Library> libraries;
    private transient Predicate<Library> filter;

    /**
     * Constructs an {@code FetchLibrariesEvent} that fetches all
     * {@code Libraries}, i.e. has a filter that always returns true.
     */
    public FetchLibrariesEvent() {
        this(lib -> true);
    }

    /**
     * Constructs an {@code FetchLibrariesEvent} with an specified filter
     * function.
     *
     * @param filter the function that used to see if the {@code Library}
     *               should be returned by {@code getLbiraries()}.
     * @see #getLibraries()
     */
    public FetchLibrariesEvent(Predicate<Library> filter) {
        this.libraries = new ArrayList<>(4);
        this.filter = filter;
    }

    @Override
    public String toString() {
        return "matcher=" + filter + ", found=" + libraries.size();
    }

    @Override
    public String message() {
        return "matcher=" + filter;
    }

    @Override
    public String responce() {
        return "count=" + libraries.size();
    }

    public List<Library> getLibraries() {
        return unmodifiableList(libraries);
    }

    /**
     * Adds a {@code Library} to the list of {@code getLibraries()} when it
     * matches the filter function.
     *
     * @param library the library to add the internal list.
     * @see #FetchLibrariesEvent(Function)
     */
    public void addLibrary(Library library) {
        if (filter.test(library))
            libraries.add(library);
    }

    @Override
    public int hashCode() {
        return 5 * 17 * 17
                + 17 * Objects.hashCode(libraries)
                + Objects.hashCode(filter);
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj != null && getClass() == obj.getClass()
                && Objects.equals(this.libraries, ((FetchLibrariesEvent) obj).libraries)
                && Objects.equals(this.filter, ((FetchLibrariesEvent) obj).filter);
    }

    @Override
    public <T extends SerializableClonable> void copyTransient(T other) {
        super.copyTransient(other);
        filter = ((FetchLibrariesEvent) other).filter;
        libraries = ((FetchLibrariesEvent) other).libraries;
    }
}
