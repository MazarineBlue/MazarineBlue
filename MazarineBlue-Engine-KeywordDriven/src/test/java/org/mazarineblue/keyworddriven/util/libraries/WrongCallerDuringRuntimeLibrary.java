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
package org.mazarineblue.keyworddriven.util.libraries;

import org.mazarineblue.keyworddriven.util.TestCaller;

public class WrongCallerDuringRuntimeLibrary
        extends AbstractTestLibrary {

    private TestCaller myCaller = new TestCaller();
    private Object caller = myCaller;

    public WrongCallerDuringRuntimeLibrary() {
        registerInstruction("Wrong caller", myCaller.getPublicMethod(), 1);
        caller = new Object();
    }

    @Override
    protected Object getCaller() {
        return caller;
    }
}
