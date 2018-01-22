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
package org.mazarineblue.utilities.exceptions;

/**
 * An {@code UnknownIssueException} is thrown in lines of code where an issue
 * might occure, that is not covered by the original scope.
 * <p>
 * When it is verified that an exception should never be thrown, then the
 * {@code NeverThrownException} should be thrown.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 * @see NeverThrownException
 */
public class UnknownIssueException
        extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs an {@code UnknownIssueException} with a default message, but
     * without a cause.
     */
    public UnknownIssueException() {
        super("Please report this unknown issue.");
    }

    /**
     * Constructs an {@code UnknownIssueException} with a message, but without a
     * cause.
     *
     * @param message the detail message (which is saved for later retrieval
     *                by the {@link #getMessage()} method).
     */
    public UnknownIssueException(String message) {
        super(message);
    }

    /**
     * Constructs an {@code UnknownIssueException} with a default message and a cause.
     *
     * @param cause the cause (which is saved for later retrieval by the
     *              {@link #getCause()} method). (A {@code null} value is
     *              permitted, and indicates that the cause is nonexistent or
     *              unknown.)
     */
    public UnknownIssueException(Throwable cause) {
        super("Please report this unknown issue: " + cause.getMessage(), cause);
    }

    /**
     * Constructs an {@code UnknownIssueException} with a message and a cause.
     *
     * @param message the detail message (which is saved for later retrieval
     *                by the {@link #getMessage()} method).
     * @param cause   the cause (which is saved for later retrieval by the
     *                {@link #getCause()} method). (A {@code null} value is
     *                permitted, and indicates that the cause is nonexistent or
     *                unknown.)
     */
    public UnknownIssueException(String message, Throwable cause) {
        super(message, cause);
    }
}
