/*
 * Copyright (c) 2015-2016 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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
 * A {@code NoSuchConstructorException} is a {@code FixtureException} that is
 * thrown by {@code FixtureLoaderLink} when the specified class didn't have a
 * constructor with the required parameters.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class NoSuchConstructorException
        extends FixtureException {

    private static final long serialVersionUID = 1L;

    public NoSuchConstructorException(Class<?> fixtureType, Class<?>[] parameterTypes) {
        super("Failed to construct fixture <%s> with arguments: %s",
              fixtureType.getCanonicalName(), toString(parameterTypes));
    }

    private static String toString(Class<?>[] parameterTypes) {
        return parameterTypes.length == 0 ? "" : concat(parameterTypes);
    }

    private static String concat(Class<?>[] parameterTypes) {
        StringBuilder builder = new StringBuilder("(").append(getParameterName(parameterTypes));
        for (int i = 1; i < parameterTypes.length; ++i)
            builder.append(", ").append(getParameterName(parameterTypes));
        return builder.toString();
    }

    private static String getParameterName(Class<?>[] parameterTypes) {
        return parameterTypes.getClass().getSimpleName();
    }
}
