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
package org.mazarineblue.libraries.awaitility;

import org.mazarineblue.eventdriven.Invoker;
import org.mazarineblue.keyworddriven.Keyword;
import org.mazarineblue.keyworddriven.PassInvoker;
import org.mazarineblue.keyworddriven.events.AddLibraryEvent;
import org.mazarineblue.keyworddriven.events.ContainsLibrariesEvent;
import org.mazarineblue.keyworddriven.events.ExecuteInstructionLineEvent;
import org.mazarineblue.libraries.awaitility.exceptions.AwaitInstructionAlreadyCalledException;

public class MainAwaitilityLibrary
        extends AbstractAwaitilityLibrary {

    @PassInvoker
    @Keyword("Await")
    public void await(Invoker invoker, String path, Object... arguments) {
        ContainsLibrariesEvent e = new ContainsLibrariesEvent(matchesAny(ConditionFactoryLibrary.class));
        invoker.publish(e);
        if (e.found())
            throw new AwaitInstructionAlreadyCalledException();
        invoker.publish(new AddLibraryEvent(new ConditionFactoryLibrary()));
        if (path != null)
            invoker.publish(new ExecuteInstructionLineEvent(path, arguments));
    }
}
