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
package org.mazarineblue.libraries.test.model.suites;

import java.util.Objects;
import org.mazarineblue.libraries.test.model.Key;

class SuiteKey
        implements Key {

    private static final long serialVersionUID = 1L;

    private final String name;

    SuiteKey(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "name=" + name;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public int hashCode() {
        return 59 * 3 + Objects.hashCode(this.name);
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj != null && getClass() == obj.getClass()
                && Objects.equals(this.name, ((SuiteKey) obj).name);
    }
}
