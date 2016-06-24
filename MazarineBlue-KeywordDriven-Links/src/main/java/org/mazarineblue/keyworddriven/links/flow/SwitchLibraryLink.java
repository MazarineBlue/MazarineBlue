/*
 * Copyright (c) 2012-2014 Alex de Kruijff
 * Copyright (c) 2014-2015 Specialisterren
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
package org.mazarineblue.keyworddriven.links.flow;

import org.mazarineblue.eventbus.EventHandler;
import org.mazarineblue.events.EndSheetEvent;
import org.mazarineblue.events.instructions.InstructionLineEvent;
import org.mazarineblue.keyworddriven.Keyword;
import org.mazarineblue.keyworddriven.Parameters;
import org.mazarineblue.keyworddriven.exceptions.BlockOpenOnEndSheetException;
import org.mazarineblue.keyworddriven.librarymanager.Library;
import org.mazarineblue.keyworddriven.links.SkippableLibraryLink;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class SwitchLibraryLink
        extends SkippableLibraryLink {

    private final BreakLibraryLink breakLink;
    private final Object data;

    public SwitchLibraryLink(Library library, BreakLibraryLink breakLink,
                             Object data) {
        super(library);
        this.breakLink = breakLink;
        this.data = data;
    }

    @Keyword("Case")
    @Parameters(min = 1)
    public void flowCase(Object data) {
        breakLink.reset();
        next(this.data.equals(data), breakLink.isBroken());
    }

    @Keyword("Default case")
    public void flowDefaultCase() {
        breakLink.reset();
        next(true, breakLink.isBroken());
    }

    @Keyword("End switch")
    public void flowEndswitch() {
        executor().chain().remove(this);
        executor().chain().remove(breakLink);
    }

    @Override
    protected void processNonkeyEvents(InstructionLineEvent event) {
        next(false, breakLink.isBroken());
        super.processNonkeyEvents(event);
    }

    @EventHandler
    public void eventHandler(EndSheetEvent event) {
        throw new BlockOpenOnEndSheetException("Switch statement", event.getSheetName());
    }
}
