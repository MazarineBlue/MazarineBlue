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

import java.util.ArrayList;
import java.util.Collection;
import static java.util.Collections.emptyList;
import java.util.List;
import org.mazarineblue.eventnotifier.Event;

class DefaultSuite
        extends SuiteKey
        implements Suite {

    private static final long serialVersionUID = 1L;

    private final Suite parent;
    private List<Event> setup;
    private List<Event> teardown;

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
    public void setSetup(Collection<Event> list) {
        setup = new ArrayList<>(list);
    }

    @Override
    public Collection<Event> getSetup() {
        return setup == null || setup.isEmpty() ? emptyList() : Event.clone(setup);
    }

    @Override
    public boolean containsTeardown() {
        return teardown != null;
    }

    @Override
    public void setTeardown(Collection<Event> list) {
        teardown = new ArrayList<>(list);
    }

    @Override
    public Collection<Event> getTeardown() {
        return teardown == null || teardown.isEmpty() ? emptyList() : Event.clone(teardown);
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
