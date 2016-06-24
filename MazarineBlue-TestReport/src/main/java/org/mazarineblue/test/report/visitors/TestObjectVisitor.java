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
package org.mazarineblue.test.report.visitors;

import org.mazarineblue.test.report.Report;
import org.mazarineblue.test.report.Teststep;
import org.mazarineblue.test.report.Suite;
import org.mazarineblue.test.report.Testcase;
import org.mazarineblue.test.report.Ticket;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public interface TestObjectVisitor {

    public void openReport(Report report)
            throws TestObjectVisitorException;

    public void closeReport(Report report)
            throws TestObjectVisitorException;

    public void openSuite(Suite suite)
            throws TestObjectVisitorException;

    public void closeSuite(Suite suite)
            throws TestObjectVisitorException;

    public void openTestcase(Testcase testcase)
            throws TestObjectVisitorException;

    public void closeTestcase(Testcase testcase)
            throws TestObjectVisitorException;

    public void openTeststep(Teststep step)
            throws TestObjectVisitorException;

    public void closeTeststep(Teststep step)
            throws TestObjectVisitorException;

    public void ticket(Ticket ticket)
            throws TestObjectVisitorException;
}
