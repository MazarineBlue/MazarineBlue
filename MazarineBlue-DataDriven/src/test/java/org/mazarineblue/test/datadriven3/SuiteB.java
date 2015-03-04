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
import org.mazarineblue.test.datadriven3.annotations.Test;
import org.mazarineblue.test.datadriven3.exceptions.IllegalConfigurationException;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.nl>}
 */
public class SuiteB extends DataDrivenSuite {

    private final List<String> list = new ArrayList<>();

    public SuiteB() throws IllegalConfigurationException {
        super("Suite B");
    }

    public List<String> getList() {
        return list;
    }

    @Test(name = "Test 6")
    @Dependency(suite = "Suite A", group = "Group 1")
    public Boolean test6(DataSource source) {
        list.add("Test 6");
        return true;
    }

    @Test(name = "Test 7")
    @Dependency(suite = "Suite A", test = "Test 4")
    public Boolean test7(DataSource source) {
        list.add("Test 7");
        return true;
    }
}
