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

import java.util.ArrayList;
import java.util.Collection;
import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableCollection;
import java.util.List;
import java.util.Objects;
import static java.util.stream.Collectors.toList;
import org.mazarineblue.eventnotifier.Event;
import org.mazarineblue.libraries.test.model.Key;
import org.mazarineblue.libraries.test.model.listeners.TestListener;
import org.mazarineblue.libraries.test.model.suites.Suite;

class DefaultTest
        extends TestKey
        implements Test {

    private static final long serialVersionUID = 1L;

    private final Collection<Suite> suites;
    private final Collection<Event> events = new ArrayList<>();
    private final TestResult result = TestResult.newInstance(this);

    DefaultTest(Suite suite, String name) {
        super(suite, name);
        suites = getAllParents(suite());
    }

    private Collection<Suite> getAllParents(Suite suite) {
        if (suite == null)
            return emptyList();
        List<Suite> list = new ArrayList<>(getAllParents(suite.parent()));
        list.add(suite);
        return list;
    }

    @Override
    public String toString() {
        return super.toString() + ", status=" + result.status();
    }

    @Override
    public void setTestListener(TestListener listener) {
        result.setTestListener(listener);
    }

    @Override
    public Collection<Suite> getSuites() {
        return unmodifiableCollection(suites);
    }

    @Override
    public Collection<Event> getEvents() {
        return Event.clone(events);
    }

    @Override
    public void setEvents(Collection<Event> list) {
        result.clear();
        events.clear();
        events.addAll(list);
    }

    @Override
    public int hashCode() {
        return 19 * 19 * 7
                + 19 * Objects.hashCode(getAllKeys())
                + Objects.hashCode(super.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj != null && getClass() == obj.getClass()
                && Objects.equals(this.getAllKeys(), ((DefaultTest) obj).getAllKeys())
                && super.equals(obj);
    }

    private Collection<Key> getAllKeys() {
        return suites.stream()
                .map(Suite::getKey)
                .collect(toList());
    }

    @Override
    public TestResult result() {
        return result;
    }
}
