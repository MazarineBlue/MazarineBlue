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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.mazarineblue.datasources.DataSource;
import org.mazarineblue.datasources.exceptions.IllegalMethodException;
import org.mazarineblue.test.datadriven3.annotations.Validator;
import org.mazarineblue.test.datadriven3.exceptions.ExecutionException;
import org.mazarineblue.test.datadriven3.exceptions.IllegalDependencyException;
import org.mazarineblue.test.datadriven3.listeners.GroupReportListeners;
import org.mazarineblue.test.datadriven3.listeners.ReportListener;
import org.mazarineblue.test.datadriven3.registry.ExecutionManager;
import org.mazarineblue.test.datadriven3.registry.ParallelType;
import org.mazarineblue.test.datadriven3.registry.ProfileRegistry;
import org.mazarineblue.test.datadriven3.registry.TestProfile;
import org.mazarineblue.util.MethodSignature;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.nl>}
 */
public class DataDrivenRunner {

    private static final MethodSignature validatorSignature = new MethodSignature() {
        {
            setAnnotationTypes(Validator.class);
            setParameterTypes(DataSource.class, String.class);
        }
    };

    private final ProfileRegistry profileRegistry = new ProfileRegistry();
    private final Map<String, Method> validatorMap = new HashMap<>();
    private final GroupReportListeners groupReportListeners = new GroupReportListeners();
    private DataSource[] sources;
    private ParallelType parallelType;

    public DataDrivenRunner(DataSource source) {
        this(new DataSource[]{source}, ParallelType.NONE);
    }

    public DataDrivenRunner(DataSource[] sources) {
        this(sources, ParallelType.SUITE);
    }

    public DataDrivenRunner(DataSource[] sources, ParallelType parallelType) {
        this.sources = sources;
        this.parallelType = parallelType;
    }

    public boolean add(ReportListener reportListeners) {
        return groupReportListeners.add(reportListeners);
    }

    public boolean remove(ReportListener reportListeners) {
        return groupReportListeners.remove(reportListeners);
    }

    public void setSources(DataSource source) {
        this.sources = new DataSource[]{source};
        this.parallelType = ParallelType.NONE;
    }

    public void setSources(DataSource[] sources) {
        this.sources = sources;
    }

    public ParallelType getParallelType() {
        return parallelType;
    }

    public void setParallelType(ParallelType parallelType) {
        this.parallelType = parallelType;
    }

    public List<TestProfile> runAll() throws ExecutionException {
        List<TestProfile> list = new ArrayList<>(fetchAll());
        return run(list);
    }

    public final Collection<TestProfile> fetchAll() {
        return profileRegistry.fetchAll();
    }

    /**
     * Runs all testcases of the specified suite and there dependencies.
     *
     * @param suite - the name of the suite
     */
    public List<TestProfile> runSuiteAndDependencies(String suite) throws ExecutionException {
        List<TestProfile> list = new ArrayList<>(fetchSuiteAndDependencies(suite));
        return run(list);
    }

    /**
     * Fetches all testcases of the specified suite and there dependencies.
     *
     * @param suite - the name of the suite
     * @return an collection contains alle the required profiles.
     */
    public final Collection<TestProfile> fetchSuiteAndDependencies(String suite) {
        return profileRegistry.fetchSuiteAndDependencies(suite);
    }

    /**
     * Runs all testcases of the specified group and there dependencies.
     *
     * @param suite - the name of the suite
     * @param group - the name of the group
     */
    public List<TestProfile> runGroupAndDependencies(String suite, String group)
            throws ExecutionException {
        List<TestProfile> list = new ArrayList<>(fetchGroupAndDependencies(suite, group));
        return run(list);
    }

    /**
     * Fetches alle the testcases of the specified group and it dependencies.
     *
     * @param suite - the name of the suite
     * @param group - the name of the group
     * @return an collection contains alle the required profiles.
     */
    public final Collection<TestProfile> fetchGroupAndDependencies(String suite, String group) {
        return profileRegistry.fetchGroupAndDependencies(suite, group);
    }

    /**
     * Runs alle the testcases of the specified testcase and its dependencies.
     *
     * @param suite - the name of the suite
     * @param test  - the name of the testcase
     */
    public List<TestProfile> runTestAndDependencies(String suite, String test)
            throws ExecutionException {
        List<TestProfile> list = new ArrayList<>(fetchTestAndDependencies(suite, test));
        return run(list);
    }

    /**
     * Fetches the specified testcase and its dependencies.
     *
     * @param suite - the name of the suite
     * @param test  - the name of the testcase
     * @return an collection contains alle the required profiles.
     */
    public final Collection<TestProfile> fetchTestAndDependencies(String suite, String test) {
        return profileRegistry.fetchTestAndDependencies(suite, test);
    }

    /**
     * Runs alle the testcases on the specified list. The list is first sorted
     * by natural order.
     *
     * @param profiles - the specified profiles to run
     */
    public final List<TestProfile> run(List<TestProfile> profiles) throws ExecutionException {
        Collections.sort(profiles);
        ExecutionManager facade = new ExecutionManager(sources, parallelType, groupReportListeners);
        facade.process(profiles);
        return profiles;
    }

    public void add(DataDrivenSuite suite) throws IllegalDependencyException {
        addSuite(suite);
        addGroups(suite);
        addTests(suite);
        addDependencies(suite);
    }

    private void addSuite(DataDrivenSuite obj) {
        profileRegistry.addSuite(obj.getSuite(), obj.getProfiles());
    }

    private void addGroups(DataDrivenSuite obj) {
        String suite = obj.getSuite();
        for (String group : obj.getGroups())
            profileRegistry.addGroup(suite, group, obj.getProfilesByGroup(group));
    }

    private void addTests(DataDrivenSuite obj) {
        String suite = obj.getSuite();
        for (String test : obj.getTests())
            profileRegistry.addTest(suite, test, obj.getProfilesByTest(test));
    }

    private void addDependencies(DataDrivenSuite obj) throws IllegalDependencyException {
        for (TestProfile profile : obj.getProfiles())
            profile.addDependencies(profileRegistry);
    }

    public void addValidators(Object obj) {
        Class<?> clazz = obj.getClass();
        for (Method method : clazz.getMethods())
            try {
                if (validatorSignature.isMethod(method) == false)
                    continue;
                Validator validator = method.getAnnotation(Validator.class);
                validatorMap.put(validator.value(), method);
            } catch (IllegalMethodException ex) {
                throw new RuntimeException(ex);
            }
    }
}
