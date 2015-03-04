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
package org.mazarineblue.test.datadriven3.registry;

import org.mazarineblue.datasources.DataSource;
import org.mazarineblue.test.datadriven3.DataDrivenSuite;
import org.mazarineblue.test.datadriven3.annotations.AfterClass;
import org.mazarineblue.test.datadriven3.annotations.AfterGroup;
import org.mazarineblue.test.datadriven3.annotations.AfterTest;
import org.mazarineblue.test.datadriven3.annotations.BeforeClass;
import org.mazarineblue.test.datadriven3.annotations.BeforeGroup;
import org.mazarineblue.test.datadriven3.annotations.BeforeTest;
import org.mazarineblue.test.datadriven3.annotations.Dependency;
import org.mazarineblue.test.datadriven3.annotations.Groups;
import org.mazarineblue.test.datadriven3.annotations.Test;
import org.mazarineblue.test.datadriven3.exceptions.IllegalConfigurationException;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.nl>}
 */
class SuiteTestData extends DataDrivenSuite {

    @BeforeClass(group = "Group 1")
    public void beforeClass() {
    }

    @AfterClass(group = "Group 2")
    public void afterClass() {
    }

    @BeforeGroup(group = "Group 1")
    public void beforeGroup() {
    }

    @AfterGroup(group = "Group 2")
    public void afterGroup() {
    }

    @BeforeTest(group = "Group 1")
    public void beforeTest() {
    }

    @AfterTest(group = "Group 2")
    public void afterTest() {
    }

    public SuiteTestData() throws IllegalConfigurationException {
        super("Suite");
    }

    public SuiteTestData(String suite) throws IllegalConfigurationException {
        super(suite);
    }

    @Test(name = "Test")
    @Groups({"Group 1", "Group 2"})
    public Boolean test(DataSource source) {
        return true;
    }

    @Test(name = "Change state")
    public Boolean changeState(DataSource source) {
        return true;
    }

    @Test(name = "Order group 2")
    @Groups({"Group 1", "Group 2"})
    public Boolean orderGroup2(DataSource source) {
        return true;
    }

    @Test(name = "Order group 3")
    @Groups({"Group 1", "Group 2", "Group 3"})
    public Boolean orderGroup3(DataSource source) {
        return true;
    }

    @Test(name = "Order group by name A")
    @Groups({"Group 1"})
    public Boolean orderGroupByNameA(DataSource source) {
        return true;
    }

    @Test(name = "Order group by name B")
    @Groups({"Group 2"})
    public Boolean orderGroupByNameB(DataSource source) {
        return true;
    }

    @Test(name = "Order test by name A")
    public Boolean orderTestByNameA(DataSource source) {
        return true;
    }

    @Test(name = "Order test by name B")
    public Boolean orderTestByNameB(DataSource source) {
        return true;
    }

    @Test(name = "Group dependency")
    @Dependency(group = "Group 1")
    public Boolean groupDepencency(DataSource source) {
        return true;
    }

    @Test(name = "Test dependency")
    @Dependency(test = "Test")
    public Boolean testDepencency(DataSource source) {
        return true;
    }
}
