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
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.mazarineblue.test.datadriven3.DataDrivenSuite;
import org.mazarineblue.test.datadriven3.annotations.Groups;
import org.mazarineblue.test.datadriven3.annotations.Test;
import org.mazarineblue.test.datadriven3.registry.TestProfile;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.nl>}
 */
public class SuiteHelperManager {

    static private final SuiteHelper emptySuiteHelper = new EmptySuiteHelper();
    private final RealSuiteHelper classSuiteHelper = new RealSuiteHelper();
    private final Map<String, RealSuiteHelper> groupSuiteHelpers = new HashMap<>();
    private final Map<String, RealSuiteHelper> testSuiteHelpers = new HashMap<>();

    public SuiteHelper getClassSuiteHelper() {
        RealSuiteHelper helper = classSuiteHelper;
        if (helper == null)
            return emptySuiteHelper;
        return helper;
    }

    void addBeforeAndAfterClassMethods(Method method, Class<? extends Annotation> clazz) {
        RealSuiteHelper helper = classSuiteHelper;
        if (helper == null)
            return;
        helper.addBeforeAndAfterMethods(method, clazz);
    }

    public SuiteHelper getGroupSuiteHelper(String group) {
        RealSuiteHelper helper = groupSuiteHelpers.get(group);
        if (helper == null)
            return emptySuiteHelper;
        return helper;
    }

    void addBeforeAndAfterGroupMethods(Method method, Class<? extends Annotation> clazz, String group) {
        RealSuiteHelper helper = groupSuiteHelpers.get(group);
        if (helper == null)
            return;
        helper.addBeforeAndAfterMethods(method, clazz);
    }

    public SuiteHelper getTestSuiteHelper(String test) {
        RealSuiteHelper helper = testSuiteHelpers.get(test);
        if (helper == null)
            return emptySuiteHelper;
        return helper;
    }

    void addBeforeAndAfterTestMethods(Method method, Class<? extends Annotation> clazz, String test) {
        RealSuiteHelper helper = testSuiteHelpers.get(test);
        if (helper == null)
            return;
        helper.addBeforeAndAfterMethods(method, clazz);
    }

    public Collection<String> getGroups() {
        return groupSuiteHelpers.keySet();
    }

    public Collection<String> getTests() {
        return testSuiteHelpers.keySet();
    }

    public void addProfile(DataDrivenSuite obj, Method method) {
        Test test = method.getAnnotation(Test.class);
        if (test == null)
            return;

        TestProfile profile = new TestProfile(method, obj);
        classSuiteHelper.addProfile(profile);
        testSuiteHelpers.put(test.name(), new RealSuiteHelper(profile));

        Groups groups = method.getAnnotation(Groups.class);
        if (groups == null)
            return;
        for (String key : groups.value()) {
            RealSuiteHelper helper = groupSuiteHelpers.get(key);
            if (helper == null)
                groupSuiteHelpers.put(key, helper = new RealSuiteHelper());
            helper.addProfile(profile);
        }
    }
}
