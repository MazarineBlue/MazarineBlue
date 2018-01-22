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

import java.util.Collection;
import org.mazarineblue.eventnotifier.Event;
import org.mazarineblue.libraries.test.model.Status;
import org.mazarineblue.libraries.test.model.listeners.TestListener;

public interface TestResult {

    public static TestResult newInstance(Test test) {
        return new DefaultTestResult(test);
    }

    public void clear();

    public Status status();

    public Collection<EventStatus> getEvenStatuses();

    public void setTestListener(TestListener listener);

    public void startWithEvent(Event event);

    public void endWithEvent(Event event);

    public void setFailed();
}
