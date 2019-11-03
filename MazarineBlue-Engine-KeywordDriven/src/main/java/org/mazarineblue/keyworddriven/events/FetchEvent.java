/*
 * Copyright (c) 2015 Specialisterren
 * Copyright (c) Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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
import java.util.Collection;
import static java.util.Collections.unmodifiableList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;
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
public class FetchEvent<T>
        extends FilterEvent<T> {

    private static final long serialVersionUID = 1L;

    private transient List<T> list = new ArrayList<>(4);

    /**
     * Constructs an {@code FetchEvent} that fetches all {@code T}, i.e.
     * has a filter that always returns true.
     */
    public FetchEvent() {
        super();
    }

    /**
     * Constructs an {@code FetchLibrariesEvent} with an specified filter
     * function.
     *
     * @param filter the function that used to see if the {@code Library}
     *               should be returned by {@code getLbiraries()}.
     * @see #list()
     */
    public FetchEvent(Predicate<T> filter) {
        super(filter);
    }

    @Override
    public String toString() {
        return "found=" + list.size();
    }

    @Override
    public String responce() {
        return "found=" + list.size();
    }

    public List<T> list() {
        return unmodifiableList(list);
    }

    /**
     * Adds a {@code Library} to the list of {@code getLibraries()} when it
     * matches the filter function.
     *
     * @param entries the entries to add the internal list.
     */
    public void add(T... entries) {
        add(stream(entries));
    }
    
    public void add(Collection<T> entries) {
        add(entries.stream());
    }

    private void add(Stream<T> stream) {
        stream.filter(this).forEach(lib -> this.list.add(lib));
    }

    @Override
    public int hashCode() {
        return 41 * 41 * 7
                + 41 * super.hashCode()
                + Objects.hashCode(this.list);
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj)
                && Objects.equals(this.list, ((FetchEvent) obj).list);
    }

    @Override
    public <T extends SerializableClonable> void copyTransient(T other) {
        super.copyTransient(other);
        list = ((FetchEvent) other).list;
    }
}
