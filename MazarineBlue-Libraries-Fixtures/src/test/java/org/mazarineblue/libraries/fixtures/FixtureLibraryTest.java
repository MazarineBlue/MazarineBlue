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
package org.mazarineblue.libraries.fixtures;

import static org.junit.Assert.assertFalse;
import org.junit.Test;
import org.mazarineblue.libraries.fixtures.util.CallerUtil;
import org.mazarineblue.utilities.ID;
import org.mazarineblue.utilities.util.TestHashCodeAndEquals;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class FixtureLibraryTest
        extends TestHashCodeAndEquals<FixtureLibrary> {

    private final ID id = new ID();

    @Override
    protected FixtureLibrary getObject() {
        return new FixtureLibrary("a", new CallerUtil(id));
    }

    @Override
    protected FixtureLibrary getDifferentObject() {
        return new FixtureLibrary("b", new CallerUtil());
    }

    @Test
    public void equals_DifferentCallers_ReturnFalse() {
        Object a = getObject();
        Object b = new FixtureLibrary("b", new CallerUtil(id));
        assertFalse(a.equals(b));
    }
}
