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
package org.mazarineblue.test.datadriven2.util;

import java.lang.reflect.Method;
import org.mazarineblue.test.datadriven3.annotations.AfterGroup;
import org.mazarineblue.test.datadriven3.annotations.Dependency;
import org.mazarineblue.test.datadriven3.annotations.Groups;
import org.mazarineblue.test.datadriven3.annotations.Test;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.nl>}
 */
public class TestProfile implements Comparable<TestProfile> {

    private final Method method;

    public TestProfile(Method method) {
        this.method = method;
    }

    public Method getMethod() {
        return method;
    }

    public Class<?> getMethodClass() {
        return method.getClass();
    }

    @Override
    public int compareTo(TestProfile other) {
        String left = getName();
        String right = other.getName();
        return left.compareTo(right);
    }

    public String getName() {
        return getMethodClass().getAnnotation(Test.class).name();
    }

    boolean isDependendOnGroup(TestProfile other) {
        for (Dependency dependency : getMethodClass().getAnnotationsByType(Dependency.class))
            if (compare(dependency.group(), other))
                return true;
        return false;
    }

    boolean isBeforeGroup(TestProfile other) {
        for (AfterGroup after : getClass().getAnnotationsByType(AfterGroup.class))
            if (compare(after.group(), other))
                return true;
        return false;
    }

    private boolean compare(String left, TestProfile other) {
        Groups groups = other.getMethodClass().getAnnotation(Groups.class);
        for (String group : groups.value())
            if (left.endsWith(group))
                return true;
        return false;
    }
}
