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
package org.mazarineblue.libraries.fixtures.exceptions;

/**
 * A {@code FixtureNotPublicException} is a {@code FixtureException} that is
 * thrown by {@code FixtureLoaderLink} when the specified class was found but
 * was not public.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class FixtureNotPublicException
        extends FixtureException {

    private static final long serialVersionUID = 1L;

    public FixtureNotPublicException(String fixture) {
        super("Fixture <%s> must be public", fixture);
    }
}
