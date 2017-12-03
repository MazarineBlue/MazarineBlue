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
package org.mazarineblue.utililities;

import org.junit.Test;
import org.mazarineblue.utililities.exceptions.NeverThrownException;
import org.mazarineblue.utililities.exceptions.UnknownIssueException;
import static org.mazarineblue.utililities.util.ExceptionUtil.assertException;

/**
 * This class contains test for the two exceptions that should never be thrown.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class ExceptionsTest {

    @Test
    public void testNeverThrownException() {
        Exception ex = new NeverThrownException();
        assertException(null, null, ex);
    }

    @Test
    public void testNeverThrownException_WithCause() {
        Exception cause = new Exception();
        Exception ex = new NeverThrownException(cause);
        assertException("java.lang.Exception", cause, ex);
    }

    @Test
    public void testUnknownIssueException() {
        Exception ex = new UnknownIssueException();
        assertException("Please report this unknown issue.", null, ex);
    }

    @Test
    public void testUnknownIssueException_WithCause() {
        Exception cause = new Exception();
        Exception ex = new UnknownIssueException(cause);
        assertException("Please report this unknown issue.", cause, ex);
    }

    @Test
    public void testUnknownIssueException_WithMessageAndCause() {
        Exception cause = new Exception();
        Exception ex = new UnknownIssueException("foo", cause);
        assertException("foo", cause, ex);
    }

    @Test
    public void testUnknownIssueException_WithMessage() {
        Exception ex = new UnknownIssueException("foo");
        assertException("foo", null, ex);
    }
}
