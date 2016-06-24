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
package org.mazarineblue.test.datadriven;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.mazarineblue.test.datadriven.exceptions.IllegalMethodException;
import org.mazarineblue.test.datadriven.exceptions.ValidationProfileException;
import org.mazarineblue.test.datadriven.util.MethodSignature;
import org.mazarineblue.util.ResourceBundleRegistry;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
class ValidationMethodMap {

    private final MethodSignature testSignature, profileSignature;

    private class Pair {

        Method profile;
        Method test;
    }

    private final Map<String, Pair> map = new HashMap();

    ValidationMethodMap(MethodSignature testSignature,
                        MethodSignature profileSignature) {
        this.testSignature = testSignature;
        this.profileSignature = profileSignature;
    }

    void addMethods(Method[] methods)
            throws IllegalMethodException {
        for (Method method : methods)
            if (profileSignature.isMethod(method))
                store(method, true);
            else if (testSignature.isMethod(method))
                store(method, false);
    }

    private void store(Method method, boolean key) {
        MethodSignature signature = key ? profileSignature : testSignature;
        String name = method.getName();
        name = name.substring(0, name.length() - signature.getPostfix().length());
        Pair pair;
        if (map.containsKey(name))
            pair = map.get(name);
        else
            map.put(name, pair = new Pair());
        if (key)
            pair.profile = method;
        else
            pair.test = method;
    }

    void checkResults()
            throws ValidationProfileException {
        Iterator<Map.Entry<String, Pair>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Pair> entry = it.next();
            Pair pair = entry.getValue();
            if (pair.profile == null) {
                String template = ResourceBundleRegistry.getString("datadriven",
                                                                   "datadriven.error.profile.missing");
                throw new ValidationProfileException(template, pair.test);
            }
            if (pair.test == null)
                it.remove();
        }
    }

    Set<String> keySet() {
        return map.keySet();
    }

    ValidationProfile profile(String key) {
        ValidationProfile profile = new ValidationProfile();
        Pair pair = map.get(key);
        pair.profile.setAccessible(true);
        try {
            pair.profile.invoke(null, profile);
        } catch (IllegalAccessException ex) {
            String template = ResourceBundleRegistry.getString("datadriven",
                                                               "datadriven.error.profile.illegalAccess");
            throw new ValidationProfileException(template, pair.profile);
        } catch (IllegalArgumentException | NullPointerException ex) {
            String template = ResourceBundleRegistry.getString("datadriven",
                                                               "datadriven.error.profile.wrongType");
            throw new ValidationProfileException(template, pair.profile);
        } catch (InvocationTargetException ex) {
            Throwable cause = ex.getCause();
            String template = ResourceBundleRegistry.getString("datadriven",
                                                               "datadriven.error.profile.invocation");
            throw new ValidationProfileException(template, pair.profile, cause);
        }
        return profile;
    }

    Method getTestMethod(String key) {
        Pair pair = map.get(key);
        return pair.test;
    }

    Method getProfileMethod(String key) {
        Pair pair = map.get(key);
        return pair.profile;
    }
}
