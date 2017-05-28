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
package org.mazarineblue.libraries;

import java.util.List;
import org.mazarineblue.eventdriven.Invoker;
import org.mazarineblue.keyworddriven.Keyword;
import org.mazarineblue.keyworddriven.Library;
import org.mazarineblue.keyworddriven.Parameters;
import org.mazarineblue.keyworddriven.PassInvoker;
import org.mazarineblue.keyworddriven.events.AddLibraryEvent;
import org.mazarineblue.keyworddriven.exceptions.LibraryNotFoundException;
import org.mazarineblue.plugins.LibraryService;

/**
 * The {@code DefaultLibrary} contains the instructions that a build into the
 * {@link DefaultInterpreterFactory}.
 *
 * @see DefaultInterpreterFactory
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class DefaultLibrary
        extends Library {

    public DefaultLibrary() {
        super("org.mazarineblue");
    }

    @PassInvoker
    @Keyword("Import")
    @Parameters(min = 1)
    public void importLibrary(Invoker invoker, String namespace) {
        if (!LibraryService.hasLibrary(namespace))
            throw new LibraryNotFoundException(namespace);
        importLibraries(invoker, LibraryService.getLibraries(namespace));
    }

    private void importLibraries(Invoker invoker, List<Library> libraries) {
        libraries.stream()
                .map(AddLibraryEvent::new)
                .forEach(invoker::publish);
    }
}
