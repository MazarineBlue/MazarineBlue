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
package org.mazarineblue.test.datadriven2;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mazarineblue.datasources.DataSource;
import org.mazarineblue.datasources.exceptions.IllegalMethodException;
import org.mazarineblue.test.datadriven2.exceptions.NoSuchValidationException;
import org.mazarineblue.test.datadriven.exceptions.PlatformsNotFoundException;
import org.mazarineblue.test.datadriven2.util.TestOrder;
import org.mazarineblue.test.datadriven2.util.TestProfile;
import org.mazarineblue.test.datadriven3.annotations.Test;
import org.mazarineblue.test.datadriven3.annotations.Validation;
import org.mazarineblue.test.report.Report;
import org.mazarineblue.util.MethodSignature;
import org.mazarineblue.util.ResourceBundleRegistry;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.nl>}
 */
public abstract class DataDrivenSuite extends DefaultValidators {

    private static final MethodSignature signature = new MethodSignature() {
        {
            setAnnotationTypes(Test.class);
            setParameterTypes(DataSource.class, TestContext.class);
            setReturnType(Boolean.class); // Returning null results in the status skipped
        }
    };

    private List<TestProfile> cache = null;
    private Report report;
    private String suite;

    protected DataDrivenSuite(String suite, Report report) {
        this();
        setSuite(suite);
        setReport(report);
    }

    public final void setReport(Report report) {
        this.report = report;
    }

    public final void setSuite(String suite) {
        this.suite = suite;
    }

    protected DataDrivenSuite() {
        this.suite = "";
    }

    private List<TestProfile> getSortedTestProfiles() {
        return getSortedTestProfiles(null);
    }

    private List<TestProfile> getSortedTestProfiles(Comparator<TestProfile> comparator) {
        List<TestProfile> list = getTestProfiles();
        Collections.sort(list, comparator);
        return list;
    }

    private List<TestProfile> getTestProfiles() {
        if (cache != null)
            return cache;
        cache = new ArrayList<>();
        for (Method method : getClass().getMethods())
            try {
                if (signature.isMethod(method))
                    cache.add(new TestProfile(method));
            } catch (IllegalMethodException ex) {
                Logger.getLogger(DataDrivenSuite.class.getName()).log(Level.SEVERE, null, ex);
            }
        return cache;
    }

    public void validateSource(DataSource source, List<ValidationMessage> conflicts)
            throws NoSuchValidationException {
        String mark = source.reset();
        ValidationContext context = new ValidationContext(report, suite);
        context.setConflictList(conflicts);
        try {
            while (source.hasNext()) {
                source.next();
                validateLine(source, context);
            }
        } finally {
            reset(source, mark);
        }
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // <editor-fold defaultstate="collapsed" desc="Methods first used in validateSource()">
    private void validateLine(DataSource source, ValidationContext context)
            throws NoSuchValidationException {
        try {
            for (TestProfile profile : getSortedTestProfiles(TestOrder.getDefaultInstance())) {
                context.setProfile(profile);
                validateProfile(source, context);
            }
        } finally {
            context.resetProfile();
        }
    }

    private void validateProfile(DataSource source, ValidationContext context)
            throws NoSuchValidationException {
        try {
            Class<?> clazz = context.getMethod().getClass();
            for (Validation validation : context.getValidations())
                try {
                    context.setValidation(validation);
                    String name = validation.method();
                    Method method = clazz.getMethod(name, DataSource.class, ValidationContext.class);
                    method.invoke(this, source, context);
                } catch (NoSuchMethodException ex) {
                    String template = ResourceBundleRegistry.getString("datadriven", "datadriven.error.validate.noSuchValidation");
                    String msg = String.format(template, validation.method(), context.getSuite());
                    throw new NoSuchValidationException(msg);
                } catch (SecurityException ex) {
                    String template = ResourceBundleRegistry.getString("datadriven", "datadriven.error.validate.security");
                    String msg = String.format(template);
                    throw new RuntimeException(msg, ex);
                } catch (IllegalAccessException ex) {
                    String template = ResourceBundleRegistry.getString("datadriven", "datadriven.error.validate.illegalAccess");
                    String msg = String.format(template, validation.method(), validation.column());
                    throw new RuntimeException(msg, ex);
                } catch (IllegalArgumentException ex) {
                    String template = ResourceBundleRegistry.getString("datadriven", "datadriven.error.validate.illegalArgument");
                    String msg = String.format(template, validation.method(), validation.column());
                    throw new RuntimeException(msg, ex);
                } catch (InvocationTargetException ex) {
                    String template = ResourceBundleRegistry.getString("datadriven", "datadriven.error.validate.invocationTarget");
                    String msg = String.format(template, validation.method(), validation.column(), ex.getMessage());
                    throw new RuntimeException(msg, ex);
                }
        } finally {
            context.resetValidation();
        }
    }

    private void reset(DataSource source, String mark) {
        if (mark == null)
            return;
        String current = source.reset();
        while (mark.equals(current) == false)
            current = source.next();
    }
    // </editor-fold>

    public void test(DataSource source) throws PlatformsNotFoundException {
        test(source, new TestContextMediator());
    }

    public void test(DataSource source, TestContextMediator testContextMedidiator)
            throws PlatformsNotFoundException {
        if (report == null)
            throw new PlatformsNotFoundException();
        test(source, report.getPlatforms(), testContextMedidiator);
    }

    public void test(DataSource source, String[] platforms) throws PlatformsNotFoundException {
        test(source, platforms, new TestContextMediator());
    }

    public void test(DataSource source, String[] platforms, TestContextMediator testContextMedidiator)
            throws PlatformsNotFoundException {
        if (report == null)
            throw new PlatformsNotFoundException();
        TestContext context = new TestContext(report, suite);
        testContextMedidiator.setTestContext(context);
        setupClass(context);
        try {
            for (String platform : platforms) {
                context.setPlatform(platform);
                testPlatform(source, context);
            }
        } finally {
            testContextMedidiator.resetTestContext();
            context.resetPlatform();
            teardownClass(context);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Methods first used in test()">
    private void testPlatform(DataSource source, TestContext context) {
        String platform = context.getPlatform();
        setupPlatform(context);
        try {
            for (TestProfile profile : getSortedTestProfiles(TestOrder.getDefaultInstance())) {
                context.setProfile(profile);
                testProfile(source, context);
            }
        } finally {
            context.resetProfile();
            teardownPlatform(context);
        }
    }

    private void testProfile(DataSource source, TestContext context) {
        String testcase = getDefaultTestcaseTitle(context);
        context.setTestcase(testcase);
        setupProfile(context);
        String mark = source.reset();
        try {
            while (source.hasNext()) {
                source.next();
                testLine(source, context);
            }
        } finally {
            reset(source, mark);
            context.resetTestcase();
            teardownProfile(context);
        }
    }

    private String getDefaultTestcaseTitle(TestContext context) {
        TestProfile profile = context.getProfile();
        String input = profile.getName();
        String postfix = signature.getPostfix();
        if (input.length() <= postfix.length())
            return "";

        boolean isFirst = true;
        char previous = input.charAt(0);
        StringBuilder output = new StringBuilder(input.length() + 16);
        int n = input.length() - postfix.length();
        output.append(Character.toUpperCase(previous));
        for (int i = 1; i < n; ++i) {
            char current = input.charAt(i);
            if (Character.isUpperCase(current) && Character.isUpperCase(previous) == false)
                output.append(' ');
            output.append(Character.toLowerCase(current));
        }
        return output.toString();
    }

    private void testLine(DataSource source, TestContext context) {
        Method test = context.getMethod();
        setup(source, context);
        try {
            runTest(source, context);
        } finally {
            context.resetTeststep();
            teardown(source, context);
        }
    }

    private void runTest(DataSource source, TestContext context) {
        Method test = context.getMethod();
        try {
            Boolean result = (Boolean) test.invoke(this, source, context);
            if (result != null)
                reportStatus(result, context);
        } catch (IllegalAccessException ex) {
            String template = ResourceBundleRegistry.getString("datadriven", "datadriven.error.test.illegalAccess");
            reportException(context, String.format(template, test));
        } catch (IllegalArgumentException ex) {
            String template = ResourceBundleRegistry.getString("datadriven", "datadriven.error.test.invalidArguments");
            reportException(context, String.format(template, test));
        } catch (NullPointerException ex) {
            String template = ResourceBundleRegistry.getString("datadriven", "datadriven.error.test.wrongType");
            reportException(context, String.format(template, test));
        } catch (InvocationTargetException ex) {
            String template = ResourceBundleRegistry.getString("datadriven", "datadriven.error.test.invocation");
            reportException(context, String.format(template, test));
        }
    }

    private void reportException(TestContext context, String error) {
        String platform = context.getPlatform();
        String testcase = context.getTestcase();
        String step = context.getTeststep();
        if (step == null)
            report.setFailed(platform, suite, testcase);
        else
            report.setFailed(platform, suite, testcase, suite);
    }

    protected void reportStatus(boolean result, TestContext context) {
        String platform = context.getPlatform();
        String testcase = context.getTestcase();
        String step = context.getTeststep();
        if (step == null)
            report.setStatus(result, platform, suite, testcase);
        else
            report.setStatus(result, platform, suite, testcase, step);
    }

    // </editor-fold>
    protected abstract void setupClass(TestContext context);

    protected abstract void setupPlatform(TestContext context);

    protected abstract void setupProfile(TestContext context);

    protected abstract void setup(DataSource source, TestContext context);

    protected abstract void teardown(DataSource source, TestContext context);

    protected abstract void teardownProfile(TestContext context);

    protected abstract void teardownPlatform(TestContext context);

    protected abstract void teardownClass(TestContext context);
}
