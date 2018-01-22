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
package org.mazarineblue.libraries.test.util;

import java.util.Collection;
import org.mazarineblue.eventnotifier.Event;
import org.mazarineblue.libraries.test.model.listeners.TestListener;
import org.mazarineblue.libraries.test.model.suites.Suite;
import org.mazarineblue.libraries.test.model.tests.Test;
import org.mazarineblue.libraries.test.model.tests.TestResult;

public class TestStub
        implements Test {

    private final SuiteStub suite;
    private final String name;

    public TestStub(String suite, String test) {
        this.suite = new SuiteStub(suite);
        this.name = test;
    }

    public TestStub(SuiteStub suite, String test) {
        this.suite = suite;
        this.name = test;
    }

    @Override
    public Suite suite() {
        return suite;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Collection<Suite> getSuites() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setTestListener(TestListener listener) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setEvents(Collection<Event> recording) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection<Event> getEvents() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TestResult result() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
