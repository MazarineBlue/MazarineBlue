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

/**
 * A {@code CommentInstructionLineEvent} is used to add comments during
 * executions. A comment does by its nature nothing during execution, but it
 * can be used for logging or reporting.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class CommentInstructionLineEvent
        extends InstructionLineEvent {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a {@code CommentInstructionLineEvent} with specified
     * arguments.
     *
     * @param arguments the arguments to use in the comment.
     */
    public CommentInstructionLineEvent(Object... arguments) {
        super("", arguments);
    }

    @Override
    public String toString() {
        return super.getArgumentList().toString();
    }

    @Override
    public String message() {
        return "comment={" + super.getArgumentList().toString() + '}';
    }
}
