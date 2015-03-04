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
package org.mazarineblue.test.datadriven3;

import java.util.ArrayList;
import java.util.List;
import org.mazarineblue.datasources.DataSource;
import org.mazarineblue.test.datadriven3.annotations.Dependency;
import org.mazarineblue.test.datadriven3.annotations.Groups;
import org.mazarineblue.test.datadriven3.annotations.Test;
import org.mazarineblue.test.datadriven3.exceptions.IllegalConfigurationException;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.nl>}
 */
public class SuiteA extends DataDrivenSuite {

    private final List<String> list = new ArrayList<>();

    public SuiteA() throws IllegalConfigurationException {
        super("Suite A");
    }

    public List<String> getList() {
        return list;
    }

    @Test(name = "Test 1")
    @Groups({"Group 1"})
    public Boolean test1(DataSource source) {
        list.add("Test 1");
        return true;
    }

    @Test(name = "Test 2")
    @Groups({"Group 2"})
    public Boolean test2(DataSource source) {
        list.add("Test 2");
        return true;
    }

    @Test(name = "Test 3")
    @Groups({"Group 1", "Group 2"})
    public Boolean test3(DataSource source) {
        list.add("Test 3");
        return true;
    }

    @Test(name = "Test 4")
    @Dependency(test = "Test 1")
    public Boolean test4(DataSource source) {
        list.add("Test 4");
        return true;
    }

    @Test(name = "Test 5")
    @Dependency(group = "Group 1")
    public Boolean test5(DataSource source) {
        list.add("Test 5");
        return true;
    }
}
