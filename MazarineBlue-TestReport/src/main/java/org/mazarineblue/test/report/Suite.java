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
import java.util.Map;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class Suite
        extends TestObject<Testcase> {

    Suite(String name, Report parent, Map<String, Platform> platforms) {
        super(name, parent, platforms);
    }

    Testcase getCase(String name) {
        Testcase obj = getTestObject(name);
        if (obj == null)
            addTestObject(name, obj = new Testcase(name, this, platforms));
        return (Testcase) obj;
    }

    @Override
    public void accept(TestObjectVisitor visitor)
            throws TestObjectVisitorException {
        visitor.openSuite(this);
        super.accept(visitor);
        visitor.closeSuite(this);
    }
}
