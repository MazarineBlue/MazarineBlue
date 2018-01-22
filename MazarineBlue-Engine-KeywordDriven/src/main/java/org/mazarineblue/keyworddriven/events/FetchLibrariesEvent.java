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
import static java.util.Arrays.stream;
import static java.util.Collections.unmodifiableList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import org.mazarineblue.keyworddriven.Library;
import org.mazarineblue.keyworddriven.LibraryRegistry;
import org.mazarineblue.utilities.SerializableClonable;

/**
 * An {@code FetchLibrariesEvent} is used to query a {@link LibraryRegistry}
 * for {@link Library Libraries} that matches a specified function.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 * @see LibraryRegistry
 * @see Library
 */
public class FetchLibrariesEvent
        extends FilterLibrariesEvent {

    private static final long serialVersionUID = 1L;

    private transient List<Library> libraries = new ArrayList<>(4);

    /**
     * Constructs an {@code FetchLibrariesEvent} that fetches all
     * {@code Libraries}, i.e. has a filter that always returns true.
     */
    public FetchLibrariesEvent() {
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
    public FetchLibrariesEvent(Predicate<Library> filter) {
        super(filter);
    }

    @Override
    public String toString() {
        return "found=" + libraries.size();
    }

    @Override
    public String responce() {
        return "found=" + libraries.size();
    }

    public List<Library> getLibraries() {
        return unmodifiableList(libraries);
    }

    /**
     * Adds a {@code Library} to the list of {@code getLibraries()} when it
     * matches the filter function.
     *
     * @param libraries the libraries to add the internal list.
     */
    public void addLibrary(Library... libraries) {
        stream(libraries)
                .filter(this)
                .forEach(lib -> this.libraries.add(lib));
    }

    @Override
    public int hashCode() {
        return 41 * 41 * 7
                + 41 * super.hashCode()
                + Objects.hashCode(this.libraries);
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj)
                && Objects.equals(this.libraries, ((FetchLibrariesEvent) obj).libraries);
    }

    @Override
    public <T extends SerializableClonable> void copyTransient(T other) {
        super.copyTransient(other);
        libraries = ((FetchLibrariesEvent) other).libraries;
    }
}
