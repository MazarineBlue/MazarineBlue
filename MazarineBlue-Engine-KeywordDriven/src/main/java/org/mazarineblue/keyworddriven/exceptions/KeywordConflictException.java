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
package org.mazarineblue.keyworddriven.exceptions;

import java.lang.reflect.Method;
import org.mazarineblue.keyworddriven.Keyword;
import org.mazarineblue.keyworddriven.Library;

/**
 * A {@code KeywordConflictException} is thrown by a {@link Library} when
 * trying to register an instruction for a {@link Keyword} that was already
 * taken, through either:
 * <ol type="a"><li>
 * using the {@link Keyword} annotation multiple times within a Library; or
 * </li><li>
 * trying to register a method for a keyword that already is taken.
 * </li></ol>
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 * @see Library#Library(String)
 * @see Library#registerInstruction(String, Method, int)
 */
public class KeywordConflictException
        extends KeywordDrivenException {

    private static final long serialVersionUID = 1L;

    public KeywordConflictException(String keyword) {
        super("keyword: " + keyword);
    }
}
