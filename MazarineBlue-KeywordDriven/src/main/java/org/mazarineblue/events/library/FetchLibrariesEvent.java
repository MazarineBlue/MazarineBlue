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

import java.util.ArrayList;
import java.util.List;
import org.mazarineblue.eventbus.AbstractEvent;
import org.mazarineblue.keyworddriven.librarymanager.Library;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class FetchLibrariesEvent<T extends Library>
        extends AbstractEvent {

    private final Class<T> clazz;
    private final List<T> libraries = new ArrayList<>();

    public FetchLibrariesEvent(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public String toString() {
        return "FetchLibraries{" + "clazz=" + clazz + '}';
    }

    public List<T> getLibraries() {
        return new ArrayList(libraries);
    }

    public void addLibrary(T library) {
        libraries.add(library);
    }

    public boolean containsLibraryClass(Class<? extends Library> clazz) {
        return this.clazz.isAssignableFrom(clazz);
    }
}
