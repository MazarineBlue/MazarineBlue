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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import org.mazarineblue.keyworddriven.Library;
import org.mazarineblue.keyworddriven.LibraryRegistry;
import org.mazarineblue.utililities.SerializableClonable;

/**
 * A {@code CountLibrariesEvent} is used to count all {@code Library Libraries}
 * registered with the {@link LibraryRegstry LibraryRegisties}.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 * @see LibraryRegistry
 */
public class CountLibrariesEvent
        extends KeywordDrivenEvent {

    private static final long serialVersionUID = 1L;

    private int count;
    private transient Predicate<Library> filter;

    /**
     * Constructs a {@code CountLibrariesEvent} that counts all libraries.
     */
    public CountLibrariesEvent() {
        this(lib -> true);
    }

    /**
     * Construct a {@code CountLibrariesEvent} that counts libraries that match.
     * the matcher function.
     *
     * @param filter count the libraries that match the filter.
     */
    public CountLibrariesEvent(Predicate<Library> filter) {
        count = 0;
        this.filter = filter;
    }

    @Override
    public String toString() {
        return "count=" + count;
    }

    @Override
    public String responce() {
        return "count=" + count;
    }

    public int getCount() {
        return count;
    }

    /**
     * Increment the count for each of the specified libraries, if they match
     * the specified credentials.
     *
     * @param libraries the libraries to test.
     * @see #CountLibrariesEvent(Function)
     */
    public void addToCount(List<Library> libraries) {
        libraries.stream()
                .filter(filter)
                .forEach(item -> ++count);
    }

    @Override
    public int hashCode() {
        return 7 * 23 * 23
                + 23 * this.count
                + Objects.hashCode(filter);
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj != null && getClass() == obj.getClass()
                && this.count == ((CountLibrariesEvent) obj).count
                && Objects.equals(this.filter, ((CountLibrariesEvent) obj).filter);
    }

    private void writeObject(ObjectOutputStream out)
            throws IOException {
        out.write(count);
    }

    private void readObject(ObjectInputStream in)
            throws IOException {
        count = in.read();
    }

    @Override
    public <E extends SerializableClonable> void copyTransient(E other) {
        super.copyTransient(other);
        filter = ((CountLibrariesEvent) other).filter;
    }
}
