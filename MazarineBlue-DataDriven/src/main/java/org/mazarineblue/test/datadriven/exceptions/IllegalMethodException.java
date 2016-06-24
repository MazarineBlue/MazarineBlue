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
package org.mazarineblue.test.datadriven.exceptions;

import java.lang.reflect.Method;
import org.mazarineblue.datasources.exceptions.SourceException;
import org.mazarineblue.test.datadriven.util.MethodSignature;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class IllegalMethodException
        extends SourceException {

    private final Method method;
    private final MethodSignature signature;

    public IllegalMethodException(Method method, MethodSignature signature) {
        super("Method " + method.getName() + " does not meet specifications");
        this.method = method;
        this.signature = signature;
    }
}
