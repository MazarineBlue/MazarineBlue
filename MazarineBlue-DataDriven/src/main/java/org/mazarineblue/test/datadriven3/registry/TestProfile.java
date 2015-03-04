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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import org.mazarineblue.datasources.DataSource;
import org.mazarineblue.test.datadriven3.DataDrivenSuite;
import org.mazarineblue.test.datadriven3.annotations.Dependency;
import org.mazarineblue.test.datadriven3.annotations.Groups;
import org.mazarineblue.test.datadriven3.annotations.Test;
import org.mazarineblue.test.datadriven3.exceptions.IllegalDependencyException;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.nl>}
 */
public class TestProfile implements Profile, Comparable<TestProfile> {

    public static enum State {

        UNSET,
        PASS,
        FAIL,
        SKIP,
    }

    private final Method testMethod;
    private final DataDrivenSuite obj;
    private transient String[] group;
    private transient String test;
    private Collection<TestProfiles> softProfilesDependencies, hardProfilesDependencies;
    private Collection<TestProfile> softProfileDependencies, hardProfileDependencies;
    private Collection<Method> beforeMethods, afterMethods;
    private State state = State.UNSET;
    private transient Exception ex;

    public TestProfile() {
        this.obj = null;
        this.testMethod = null;
    }

    public TestProfile(DataDrivenSuite obj, String[] group, String test) {
        this.obj = obj;
        this.testMethod = null;
        this.group = group;
        this.test = test;
    }

    public TestProfile(Method testMethod, DataDrivenSuite obj) {
        this.obj = obj;
        this.testMethod = testMethod;
    }

    @Override
    public String toString() {
        String group = "";
        for (String g : getGroups())
            group += g + ", ";
        if (group.length() > 3)
            group = group.substring(0, group.length() - 2);
        return "TestProfile{" + "(" + obj.getSuite() + ", [" + group + "], " + getTest() + ")=" + state + '}';
    }

    final String getSuite() {
        return obj.getSuite();
    }

    final String[] getGroups() {
        if (group != null)
            return group;
        Groups annotation = testMethod.getAnnotation(Groups.class);
        if (annotation == null)
            return new String[0];
        String[] groups = annotation.value();
        group = new String[groups.length];
        for (int i = 0; i < groups.length; ++i)
            group[i] = groups[i];
        return group;
    }

    final String getTest() {
        if (test != null)
            return test;
        Test annotation = testMethod.getAnnotation(Test.class);
        return test = annotation.name();
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void error(InvocationTargetException ex) {
        this.state = State.FAIL;
        this.ex = ex;
    }

    public Method getTestMethod() {
        return testMethod;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + Objects.hashCode(this.testMethod);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj instanceof TestProfile == false)
            return false;
        final TestProfile other = (TestProfile) obj;
        if (this.obj.equals(other.obj) == false)
            return false;
        if (Arrays.equals(group, other.group) == false)
            return false;
        return getTest().equals(other.getTest());
    }

    @Override
    public int compareTo(TestProfile other) {
        int s = getSuite().compareTo(other.getSuite());
        if (s != 0)
            return s;
        String[] groups = getGroups();
        String[] otherGroups = other.getGroups();
        int c = groups.length - otherGroups.length;
        if (c != 0)
            return c;
        for (int i = 0; i < groups.length; ++i) {
            int g = groups[i].compareTo(otherGroups[i]);
            if (g != 0)
                return g;
        }
        return getTest().compareTo(other.getTest());
    }

    public void fetchProfiles(Collection<TestProfile> todo) {
        if (todo.contains(this))
            return;
        if (softProfilesDependencies != null)
            for (TestProfiles profiles : softProfilesDependencies)
                profiles.fetchProfiles(todo);
        todo.add(this);
    }

    boolean isDependendOn(TestProfile profile) {
        for (TestProfiles profiles : hardProfilesDependencies)
            if (profiles.isDependendOn(profile))
                return true;
        return false;
    }

    private enum DependencyType {

        SUITE,
        GROUP,
        TEST,
        NONE,
    }

    private DependencyType getDependencyType(ProfileRegistry registry, String s, String g, String t)
            throws IllegalDependencyException {
        DependencyType defaultType;
        if (s.equals("")) {
            defaultType = DependencyType.NONE;
            s = getSuite();
        } else
            defaultType = DependencyType.SUITE;

        if (g.equals(""))
            return t.equals("") ? defaultType : DependencyType.TEST;
        else if (t.equals(""))
            return DependencyType.GROUP;
        else {
            TestProfile profile = registry.getTest(s, t);
            if (profile.hasGroup(g))
                return DependencyType.TEST;
            throw new IllegalDependencyException();
        }
    }

    private boolean hasGroup(String g) {
        for (String group : getGroups())
            if (group.equals(g))
                return true;
        return false;
    }

    public void addDependencies(ProfileRegistry registry) throws IllegalDependencyException {
        addHardDependencies(registry);
//        addSoftDependencies(registry);
    }

    private void addHardDependencies(ProfileRegistry registry) throws IllegalDependencyException {
        String name = testMethod.getName();
        for (Dependency dependency : testMethod.getAnnotationsByType(Dependency.class)) {
            String s = dependency.suite();
            String g = dependency.group();
            String t = dependency.test();

            switch (getDependencyType(registry, s, g, t)) {
                case SUITE:
                    addHardDependencies(registry.getSuite(s));
                    continue;
                case GROUP:
                    if (s.equals(""))
                        s = getSuite();
                    addHardDependencies(registry.getGroup(s, g));
                    continue;
                case TEST:
                    if (s.equals(""))
                        s = getSuite();
                    addHardMethodDependencies(registry.getTest(s, t));
                case NONE:
            }
        }
    }

    private void addHardMethodDependencies(TestProfile profile) {
        if (profile == null)
            return;
        if (hardProfileDependencies == null)
            hardProfileDependencies = new ArrayList<>();
        hardProfileDependencies.add(profile);
    }

    private void addSoftMethodDependencies(TestProfile profile) {
        if (profile == null)
            return;
        if (softProfileDependencies == null)
            softProfileDependencies = new ArrayList<>();
        softProfileDependencies.add(profile);
    }

    void addHardDependencies(TestProfiles profiles) {
        if (profiles == null || profiles.isEmpty())
            return;
        if (hardProfilesDependencies == null)
            hardProfilesDependencies = new ArrayList<>();
        hardProfilesDependencies.add(profiles);
    }

    private void addSoftDependencies(ProfileRegistry registry) throws IllegalDependencyException {
        TestProfiles ps = registry.getSuite(getSuite());
        for (String group : getGroups()) {
            TestProfiles pg = registry.getGroup(getSuite(), group);
            for (Dependency dependency : testMethod.getAnnotationsByType(Dependency.class)) {
                String s = dependency.suite();
                String g = dependency.group();
                String t = dependency.test();

                switch (getDependencyType(registry, s, g, t)) {
                    case SUITE:
                        addSoftDependencies(registry.getSuite(s));
                        continue;
                    case GROUP:
                        if (s.equals(""))
                            s = getSuite();
                        else
                            ps.addSoftDependencies(registry.getSuite(s));
                        addSoftDependencies(registry.getGroup(s, g));
                        continue;
                    case TEST:
                        if (s.equals(""))
                            s = getSuite();
                        else
                            ps.addSoftDependencies(registry.getSuite(s));
                        addSoftMethodDependencies(registry.getTest(s, t));
                    case NONE:
                        continue;
                }
            }
        }
    }

    void addSoftDependencies(TestProfiles profiles) {
        if (profiles == null || profiles.isEmpty())
            return;
        if (softProfilesDependencies == null)
            softProfilesDependencies = new ArrayList<>();
        softProfilesDependencies.add(profiles);
    }

    public void setBeforeMethods(Collection<Method> beforeMethods) {
        this.beforeMethods = beforeMethods;
    }

    public void setAfterMethods(Collection<Method> afterMethods) {
        this.afterMethods = afterMethods;
    }

    public boolean mustSkip() {
        if (hardProfilesDependencies != null)
            for (TestProfiles profiles : hardProfilesDependencies)
                switch (profiles.getState()) {
                    case FAIL:
                    case SKIP:
                        return true;
                }
        return false;
    }

    public boolean canRun() {
        if (hardProfilesDependencies != null)
            for (TestProfiles profiles : hardProfilesDependencies)
                switch (profiles.getState()) {
                    case FAIL:
                    case SKIP:
                    case UNSET:
                        return false;
                }
        if (hardProfileDependencies != null)
            for (TestProfile profile : hardProfileDependencies)
                switch (profile.getState()) {
                    case FAIL:
                    case SKIP:
                    case UNSET:
                        return false;
                }
        if (softProfilesDependencies != null)
            for (TestProfiles profiles : softProfilesDependencies)
                if (profiles.getState().equals(TestProfile.State.UNSET))
                    return false;
        if (softProfileDependencies != null)
            for (TestProfile profile : softProfileDependencies)
                if (profile.getState().equals(TestProfile.State.UNSET))
                    return false;
        return true;
    }

    @Override
    public Boolean invokeTestMethod(DataSource source) throws InvocationTargetException {
        try {
            obj.invokeBefore(group, test, source);
            Boolean flag = (Boolean) testMethod.invoke(obj, source);
            if (flag == null)
                state = State.SKIP;
            else if (flag)
                state = State.PASS;
            else
                state = State.FAIL;
            return flag;
        } catch (IllegalAccessException | IllegalArgumentException ex) {
            throw new RuntimeException(ex);
        } catch (InvocationTargetException ex) {
            throw ex;
        } finally {
            obj.invokeAfter(group, test, source);
        }
    }
}
