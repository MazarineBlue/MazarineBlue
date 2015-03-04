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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.nl>}
 */
public class ProfileRegistry {

    private final Map<SuiteKey, TestProfiles> suiteMap = new HashMap<>();
    private final Map<GroupKey, TestProfiles> groupMap = new HashMap<>();
    private final Map<TestKey, TestProfile> testMap = new HashMap<>();

    TestProfiles getSuite(String suite) {
        return suiteMap.get(new SuiteKey(suite));
    }

    final TestProfiles getGroup(String suite, String group) {
        return groupMap.get(new GroupKey(suite, group));
    }

    final TestProfile getTest(String suite, String test) {
        return testMap.get(new TestKey(suite, test));
    }

    public final Collection<TestProfile> fetchAll() {
        TreeSet<TestProfile> set = new TreeSet<>();
        for (TestProfiles profiles : suiteMap.values())
            profiles.fetchProfiles(set);
        return set;
    }

    public final Collection<TestProfile> fetchSuiteAndDependencies(String suite) {
        TreeSet<TestProfile> set = new TreeSet<>();
        TestProfiles profiles = suiteMap.get(new SuiteKey(suite));
        profiles.fetchProfiles(set);
        return set;
    }

    public final Collection<TestProfile> fetchGroupAndDependencies(String suite, String group) {
        TreeSet<TestProfile> set = new TreeSet<>();
        TestProfiles profiles = groupMap.get(new GroupKey(suite, group));
        profiles.fetchProfiles(set);
        return set;
    }

    public final Collection<TestProfile> fetchTestAndDependencies(String suite, String test) {
        TreeSet<TestProfile> set = new TreeSet<>();
        TestProfile profile = testMap.get(new TestKey(suite, test));
        profile.fetchProfiles(set);
        return set;
    }

    public void addSuite(String suite, Collection<TestProfile> profiles) {
        SuiteKey key = new SuiteKey(suite);
        TestProfiles value = new TestProfiles(profiles);
        suiteMap.put(key, value);
    }

    public void addGroup(String suite, String group, Collection<TestProfile> value) {
        GroupKey key = new GroupKey(suite, group);
        groupMap.put(key, new TestProfiles(value));
    }

    public void addTest(String suite, String test, TestProfile profiles) {
        TestKey key = new TestKey(suite, test);
        testMap.put(key, profiles);
    }
}
