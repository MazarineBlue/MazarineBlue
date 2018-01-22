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
package org.mazarineblue.keyworddriven.exceptions;

import org.mazarineblue.keyworddriven.Keyword;
import org.mazarineblue.keyworddriven.Library;
import org.mazarineblue.keyworddriven.Parameters;

/**
 * A {@code PrimativesNotAllowedByondMinimumBorderException} is thrown by a
 * {@link Library} when initializing it and an {@link instruction} has
 * primatives beyond the minimum border, as specified by the {@link Parameters}
 * annotation.
 * <p>
 * An call to instruction is required to supply a minimum of argument.
 * Arguments are options beyond that limit and thus can be null. Therefor
 * primatives are not allowed beyond the minimum limit.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 * @see Keyword
 * @see Parameters
 * @see Library
 */
public class PrimativesNotAllowedByondMinimumBorderException
        extends InstructionException {

    private static final long serialVersionUID = 1L;

    public PrimativesNotAllowedByondMinimumBorderException(String namespace, String keyword) {
        super(namespace, keyword);
    }
}
