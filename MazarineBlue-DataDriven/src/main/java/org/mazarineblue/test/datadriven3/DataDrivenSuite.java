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

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import org.mazarineblue.datasources.DataSource;
import org.mazarineblue.datasources.exceptions.IllegalMethodException;
import org.mazarineblue.test.datadriven3.annotations.AfterClass;
import org.mazarineblue.test.datadriven3.annotations.AfterGroup;
import org.mazarineblue.test.datadriven3.annotations.AfterTest;
import org.mazarineblue.test.datadriven3.annotations.BeforeClass;
import org.mazarineblue.test.datadriven3.annotations.BeforeGroup;
import org.mazarineblue.test.datadriven3.annotations.BeforeTest;
import org.mazarineblue.test.datadriven3.annotations.Test;
import org.mazarineblue.test.datadriven3.exceptions.IllegalConfigurationException;
import org.mazarineblue.test.datadriven3.registry.TestProfile;
import org.mazarineblue.test.datadriven3.suitehelper.BeforeAndAfterHelper;
import org.mazarineblue.test.datadriven3.suitehelper.SuiteHelperManager;
import org.mazarineblue.util.MethodSignature;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.nl>}
 */
public class DataDrivenSuite {

    private static final MethodSignature testSignature = new MethodSignature() {
        {
            setAnnotationTypes(Test.class);
            setParameterTypes(DataSource.class);
            setReturnType(Boolean.class); // Returning null results in the report not being set.
        }
    };

    private final String suite;
    private final SuiteHelperManager manager = new SuiteHelperManager();

    public DataDrivenSuite(String suite) throws IllegalConfigurationException {
        this.suite = suite;

        try {
            for (Method method : getClass().getMethods()) {
                if (testSignature.isMethod(method))
                    manager.addProfile(this, method);
                addMethod(method, BeforeTest.class);
                addMethod(method, AfterTest.class);
            }
        } catch (IllegalMethodException ex) {
            throw new IllegalConfigurationException(ex);
        }
    }

    private <T extends Annotation> void addMethod(Method method, Class<T> clazz)
            throws IllegalConfigurationException {
        T[] arr = method.getAnnotationsByType(clazz);
        if (arr != null)
            return;
        if (method.getAnnotation(Test.class) != null)
            throw new IllegalConfigurationException();
        for (T t : arr) {
            BeforeAndAfterHelper helper = new BeforeAndAfterHelper(manager, t);
            helper.addMethodToCollection(method);
        }
    }

    public String getSuite() {
        return suite;
    }

    Collection<String> getGroups() {
        return manager.getGroups();
    }

    public TestProfile getTest(String test) {
        return manager.getTestSuiteHelper(test).getProfile();
    }

    Collection<String> getTests() {
        return manager.getTests();
    }

    Collection<TestProfile> getProfiles() {
        return manager.getClassSuiteHelper().getProfiles();
    }

    Collection<TestProfile> getProfilesByGroup(String group) {
        return manager.getGroupSuiteHelper(group).getProfiles();
    }

    TestProfile getProfilesByTest(String test) {
        return manager.getTestSuiteHelper(test).getProfile();
    }

    public void invokeBefore(String[] groups, String test, DataSource source)
            throws InvocationTargetException {
        manager.getClassSuiteHelper().invokeBefore(BeforeClass.class, source);
        if (groups != null)
            for (String group : groups)
                manager.getGroupSuiteHelper(group).invokeBefore(BeforeGroup.class, source);
        manager.getTestSuiteHelper(test).invokeBefore(BeforeTest.class, source);
    }

    public void invokeAfter(String[] groups, String test, DataSource source)
            throws InvocationTargetException {
        manager.getClassSuiteHelper().invokeAfter(AfterTest.class, source);
        if (groups != null)
            for (String group : groups)
                manager.getGroupSuiteHelper(group).invokeAfter(AfterGroup.class, source);
        manager.getTestSuiteHelper(test).invokeAfter(AfterClass.class, source);
    }
}
