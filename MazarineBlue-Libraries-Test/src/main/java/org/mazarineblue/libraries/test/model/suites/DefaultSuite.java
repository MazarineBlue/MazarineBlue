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

import org.mazarineblue.subscribers.recorder.Recording;

class DefaultSuite
        extends SuiteKey
        implements Suite {

    private static final long serialVersionUID = 1L;

    private final Suite parent;
    private Recording setup;
    private Recording teardown;

    DefaultSuite(Suite parent, String name) {
        super(name);
        this.parent = parent;
    }

    @Override
    public Suite parent() {
        return parent;
    }

    @Override
    public boolean containsSetup() {
        return setup != null;
    }

    @Override
    public void setSetup(Recording recording) {
        setup = recording;
    }

    @Override
    public Recording getSetup() {
        return setup != null ? setup : new Recording(0);
    }

    @Override
    public boolean containsTeardown() {
        return teardown != null;
    }

    @Override
    public void setTeardown(Recording recording) {
        teardown = recording;
    }

    @Override
    public Recording getTeardown() {
        return teardown != null ? teardown : new Recording(0);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
