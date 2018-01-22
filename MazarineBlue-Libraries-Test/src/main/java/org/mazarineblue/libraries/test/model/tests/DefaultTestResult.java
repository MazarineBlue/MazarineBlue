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

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import static java.util.Collections.unmodifiableCollection;
import org.mazarineblue.eventnotifier.Event;
import org.mazarineblue.libraries.test.model.Status;
import static org.mazarineblue.libraries.test.model.Status.FAIL;
import static org.mazarineblue.libraries.test.model.Status.PASS;
import org.mazarineblue.libraries.test.model.listeners.TestListener;

class DefaultTestResult
        implements TestResult {

    private final WeakReference<Test> test;
    private TestListener listener;
    private Status status = PASS;
    private EventStatus activeEventStatus;
    private final Collection<EventStatus> eventResults = new ArrayList<>();

    DefaultTestResult(Test test) {
        this.test = new WeakReference<>(test);
    }

    @Override
    public String toString() {
        return "status=" + status;
    }

    @Override
    public void clear() {
        status = PASS;
        eventResults.clear();
    }

    @Override
    public void setTestListener(TestListener listener) {
        this.listener = listener;
    }

    @Override
    public Status status() {
        return status;
    }

    @Override
    public Collection<EventStatus> getEvenStatuses() {
        return unmodifiableCollection(eventResults);
    }

    @Override
    public void startWithEvent(Event event) {
        activeEventStatus = new DefaultEventStatus(event);
    }

    @Override
    public void endWithEvent(Event event) {
        eventResults.add(activeEventStatus);
    }

    @Override
    public void setFailed() {
        status = FAIL;
        activeEventStatus.setStatus(FAIL);
    }
}
