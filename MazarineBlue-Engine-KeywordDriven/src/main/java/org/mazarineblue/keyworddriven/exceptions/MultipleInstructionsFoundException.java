/*
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
package org.mazarineblue.keyworddriven.exceptions;

import org.mazarineblue.eventdriven.Feed;
import org.mazarineblue.keyworddriven.Keyword;
import org.mazarineblue.keyworddriven.Library;
import org.mazarineblue.keyworddriven.LibraryRegistry;
import org.mazarineblue.keyworddriven.Parameters;
import org.mazarineblue.keyworddriven.events.ExecuteInstructionLineEvent;

/**
 * A {@code MultipleInstructionsFoundException} is thrown by the
 * {@link LibrariesRegistry} when executing a {@link Feed} and an
 * {@link ExecuteInstructionLineEvent} is fired for which there where multiple
 * {@link Library Libraries} implement a {@link Keyword} and the namespace was
 * not specified.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 * @see Keyword
 * @see Parameters
 * @see LibraryRegistry
 */
public class MultipleInstructionsFoundException
        extends KeywordDrivenException {

    private static final long serialVersionUID = 1L;

    public MultipleInstructionsFoundException(ExecuteInstructionLineEvent event) {
        super("Event: " + event);
    }
}
