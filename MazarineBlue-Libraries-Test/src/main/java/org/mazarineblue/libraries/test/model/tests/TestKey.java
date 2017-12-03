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
package org.mazarineblue.libraries.test.model.tests;

import java.util.Objects;
import org.mazarineblue.libraries.test.model.Key;
import org.mazarineblue.libraries.test.model.suites.Suite;

class TestKey
        implements Key {

    private static final long serialVersionUID = 1L;

    private final Suite suite;
    private final Key suiteKey;
    private final String name;

    protected TestKey(Suite suite, String name) {
        this.suite = suite;
        this.suiteKey = suite.getKey();
        this.name = name;
    }

    @Override
    public String toString() {
        return "suite=" + suite.name() + ", name=" + name;
    }

    public Suite suite() {
        return suite;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public int hashCode() {
        return 97 * 97 * 3
                + 97 * Objects.hashCode(this.suiteKey)
                + Objects.hashCode(this.name);
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj != null && getClass() == obj.getClass()
                && Objects.equals(this.suiteKey, ((TestKey) obj).suiteKey)
                && Objects.equals(this.name, ((TestKey) obj).name);
    }
}
