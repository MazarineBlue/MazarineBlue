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
package org.mazarineblue.test.datadriven3.suitehelper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import org.mazarineblue.test.datadriven3.annotations.AfterClass;
import org.mazarineblue.test.datadriven3.annotations.AfterGroup;
import org.mazarineblue.test.datadriven3.annotations.AfterTest;
import org.mazarineblue.test.datadriven3.annotations.BeforeClass;
import org.mazarineblue.test.datadriven3.annotations.BeforeGroup;
import org.mazarineblue.test.datadriven3.annotations.BeforeTest;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.nl>}
 */
public class BeforeAndAfterHelper {

    private final SuiteHelperManager manager;
    private final Class<? extends Annotation> clazz;
    private final String group;
    private final String test;

    public <T extends Annotation> BeforeAndAfterHelper(SuiteHelperManager manager, T t) {
        this.manager = manager;
        clazz = t.annotationType();
        if (t instanceof BeforeClass) {
            group = ((BeforeClass) t).group();
            test = ((BeforeClass) t).test();
        } else if (t instanceof AfterClass) {
            group = ((AfterClass) t).group();
            test = ((AfterClass) t).test();
        } else if (t instanceof BeforeGroup) {
            group = ((BeforeGroup) t).group();
            test = ((BeforeGroup) t).test();
        } else if (t instanceof AfterGroup) {
            group = ((AfterGroup) t).group();
            test = ((AfterGroup) t).test();
        } else if (t instanceof BeforeTest) {
            group = ((BeforeTest) t).group();
            test = ((BeforeTest) t).test();
        } else if (t instanceof AfterTest) {
            group = ((AfterTest) t).group();
            test = ((AfterTest) t).test();
        } else
            group = test = "";
    }

    public void addMethodToCollection(Method method) {
        if (group.equals("") && test.equals(""))
            manager.addBeforeAndAfterClassMethods(method, clazz);
        else if (group.equals("") == false)
            manager.addBeforeAndAfterGroupMethods(method, clazz, group);
        else
            manager.addBeforeAndAfterTestMethods(method, clazz, test);
    }
}
