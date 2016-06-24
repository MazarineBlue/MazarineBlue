/*
 * Copyright (c) 2015 Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
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
package org.mazarineblue.util;

import org.hamcrest.BaseMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class TestUtil {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    protected void expect(Class<? extends Throwable> exception,
                          Class<? extends Throwable> cause) {
        thrown.expect(exception);
        expectCause(cause);
    }

    protected void expectCause(Class<? extends Throwable> cause) {
        BaseMatcher coreMatcher = new IsInstanceOf(cause);
        thrown.expectCause(coreMatcher);
    }
}
