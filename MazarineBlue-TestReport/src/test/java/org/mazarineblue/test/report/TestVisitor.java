/*
 * Copyright (c) 2012-2014 Alex de Kruijff
 * Copyright (c) 2014-2015 Specialisterren
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
package org.mazarineblue.test.report;

import org.mazarineblue.test.report.visitors.TestObjectVisitor;
import org.mazarineblue.test.report.visitors.TestObjectVisitorException;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
class TestVisitor
        implements TestObjectVisitor {

    int countReports = 0;
    int countSuites = 0;
    int countTestcase = 0;
    int countSteps = 0;
    int countTickets = 0;

    TestVisitor() {
    }

    @Override
    public void openReport(Report report)
            throws TestObjectVisitorException {
        ++countReports;
    }

    @Override
    public void closeReport(Report report)
            throws TestObjectVisitorException {
    }

    @Override
    public void openSuite(Suite suite)
            throws TestObjectVisitorException {
        ++countSuites;
    }

    @Override
    public void closeSuite(Suite suite)
            throws TestObjectVisitorException {
    }

    @Override
    public void openTestcase(Testcase testcase)
            throws TestObjectVisitorException {
        ++countTestcase;
    }

    @Override
    public void closeTestcase(Testcase testcase)
            throws TestObjectVisitorException {
    }

    @Override
    public void openTeststep(Teststep step)
            throws TestObjectVisitorException {
        ++countSteps;
    }

    @Override
    public void closeTeststep(Teststep step)
            throws TestObjectVisitorException {
    }

    @Override
    public void ticket(Ticket ticket)
            throws TestObjectVisitorException {
        ++countTickets;
    }
}
