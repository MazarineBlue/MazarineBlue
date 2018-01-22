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
package org.mazarineblue.keyworddriven.util.libraries;

import org.mazarineblue.eventdriven.Invoker;
import org.mazarineblue.keyworddriven.Library;
import org.mazarineblue.keyworddriven.events.AddLibraryEvent;
import org.mazarineblue.keyworddriven.events.RemoveLibraryEvent;

public class AddsLibrary
        extends AbstractTestLibrary {

    private final Library library;

    public AddsLibrary(Library library) {
        this.library = library;
    }

    @Override
    public void doSetup(Invoker invoker) {
        invoker.publish(new AddLibraryEvent(library));
    }

    @Override
    protected void doTeardown(Invoker invoker) {
        invoker.publish(new RemoveLibraryEvent(library));
    }
}
