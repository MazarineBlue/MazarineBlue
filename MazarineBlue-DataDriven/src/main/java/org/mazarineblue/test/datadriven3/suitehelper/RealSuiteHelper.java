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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.mazarineblue.datasources.DataSource;
import org.mazarineblue.test.datadriven3.registry.TestProfile;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.nl>}
 */
class RealSuiteHelper implements SuiteHelper {

    private final TestProfile profile;
    private final Collection<TestProfile> profiles = new ArrayList<>();
    private Map<Class, Collection<Method>> beforeAndAfterMethods;

    public RealSuiteHelper() {
        profile = null;
    }

    public RealSuiteHelper(TestProfile profile) {
        this.profile = profile;
    }

    @Override
    public TestProfile getProfile() {
        return profile;
    }

    void addProfile(TestProfile profile) {
        profiles.add(profile);
    }

    @Override
    public Collection<TestProfile> getProfiles() {
        return profiles;
    }

    void addBeforeAndAfterMethods(Method method, Class<? extends Annotation> clazz) {
        if (beforeAndAfterMethods == null)
            beforeAndAfterMethods = new HashMap<>();
        Collection<Method> collection = beforeAndAfterMethods.get(clazz);
        if (collection == null)
            beforeAndAfterMethods.put(clazz, collection = new ArrayList<>());
        collection.add(method);
    }

    private int invocationCount = 0;

    @Override
    public void invokeBefore(Class<? extends Annotation> clazz, DataSource source)
            throws InvocationTargetException {
        if (invocationCount > 0)
            return;
        invocationCount = profiles.size();
        invoke(clazz, source);
    }

    @Override
    public void invokeAfter(Class<? extends Annotation> clazz, DataSource source)
            throws InvocationTargetException {
        if (--invocationCount > 0)
            return;
        invoke(clazz, source);
    }

    private void invoke(Class<? extends Annotation> clazz, DataSource source)
            throws InvocationTargetException {
        try {
            if (beforeAndAfterMethods != null)
                for (Method method : beforeAndAfterMethods.get(clazz))
                    synchronized (method) {
                        method.invoke(this, source);
                    }
        } catch (IllegalAccessException | IllegalArgumentException ex) {
            throw new RuntimeException(ex);
        } catch (InvocationTargetException ex) {
            throw ex;
        }
    }
}
