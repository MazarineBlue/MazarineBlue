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
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;
import org.mazarineblue.datasources.DataSource;
import org.mazarineblue.test.datadriven.exceptions.IllegalMethodException;
import org.mazarineblue.test.datadriven.exceptions.PlatformsNotFoundException;
import org.mazarineblue.test.report.Report;
import org.mazarineblue.test.datadriven.util.MethodSignature;
import org.mazarineblue.util.ResourceBundleRegistry;

/**
 * Collects profiles by calling static method and providing it with a
 * <code>ValidationProfile</code> container.
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public abstract class DataDrivenSuite {

    private static final MethodSignature testSignature = new MethodSignature(
            "Test") {
                {
                    setAnnotationTypes(DataDrivenTest.class);
                    setParameterTypes(DataSource.class, TestContext.class);
                    setReturnType(Boolean.class); // Returning null results in the report not being set.
                }
            };
    private static final MethodSignature profileSignature = new MethodSignature(
            "Profile") {
                {
                    setAnnotationTypes(DataDrivenProfile.class);
                    setParameterTypes(ValidationProfile.class);
                }
            };

    private static final Logger LOGGER = Logger.getLogger(
            DataDrivenSuite.class.getName());

    private final Map<Method, ValidationProfile> profiles = new HashMap<>(4);
    private final ReportListenerGroup reportGroup = new ReportListenerGroup();
    private DefaultReportListener defaultListener = null;
    private String suite;

    protected DataDrivenSuite(String suite, Report report)
            throws IllegalMethodException {
        this();
        setSuite(suite);
        setReport(report);
    }

    public String getSuiteName() {
        return suite;
    }

    public void addListener(ReportListener reportListener) {
        reportGroup.add(reportListener);
    }

    public void removeListener(ReportListener reportListener) {
        reportGroup.remove(reportListener);
    }

    protected DataDrivenSuite()
            throws IllegalMethodException {
        ValidationMethodMap methodMap = new ValidationMethodMap(testSignature,
                                                                profileSignature);
        methodMap.addMethods(getClass().getMethods());
        methodMap.checkResults();
        for (String key : methodMap.keySet()) {
            Method method = methodMap.getTestMethod(key);
            ValidationProfile profile = methodMap.profile(key);
            profiles.put(method, profile);
        }
    }

    private String[] defaultPlatforms;

    public void setPlatforms(String[] platforms) {
        this.defaultPlatforms = platforms;
    }

    public final void setReport(Report report) {
        if (report != null) {
            setPlatforms(report.getPlatforms());
            if (defaultListener == null) {
                defaultListener = new DefaultReportListener(report);
                reportGroup.add(defaultListener);
            }
        } else if (defaultListener != null) {
            reportGroup.remove(defaultListener);
            defaultListener = null;
        }
    }

    public final void setSuite(String suite) {
        this.suite = suite;
    }

    public void validateSource(DataSource source, List<ValidationMessage> conflicts) {
        List<String> mark = source.getLineIdentifiers();
        source.reset();
        try {
            while (source.hasNext()) {
                source.next();
                validateLine(source, conflicts);
            }
        } finally {
            reset(source, mark);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Methods first used in validateSource()">
    private void validateLine(DataSource source, List<ValidationMessage> conflicts) {
        ValidationContext context = new ValidationContext();
        for (Map.Entry<Method, ValidationProfile> entry : profiles.entrySet()) {
            context.setMethod(entry.getKey());
            ValidationProfile profile = entry.getValue();
            profile.validateLine(source, context, conflicts);
            context.reset();
        }
    }

    private void reset(DataSource source, List<String> mark) {
        if (mark == null)
            return;
        source.reset();
        List<String> current = source.getLineIdentifiers();
        while (mark.equals(current) == false) {
            source.next();
            current = source.getLineIdentifiers();
        }
    }
    // </editor-fold>

    public int getTestCount() {
        return profiles.size();
    }

    public void test(DataSource source)
            throws PlatformsNotFoundException {
        test(source, new TestContextMediator());
    }

    public void test(DataSource source,
                     TestContextMediator testContextMedidiator)
            throws PlatformsNotFoundException {
        test(source, defaultPlatforms, testContextMedidiator);
    }

    public void test(DataSource source, String[] platforms)
            throws PlatformsNotFoundException {
        test(source, platforms, new TestContextMediator());
    }

    public void test(DataSource source, String[] platforms,
                     TestContextMediator testContextMedidiator)
            throws PlatformsNotFoundException {
        if (platforms == null)
            throw new PlatformsNotFoundException();
        TestContext context = new TestContext(suite);
        testContextMedidiator.setTestContext(context);
        setupClass(context);
        try {
            for (String platform : platforms) {
                context.setPlatform(platform);
                testPlatform(source, context);
            }
        } finally {
            testContextMedidiator.resetTestContext();
            context.teardownAfterPlatform();
            teardownClass(context);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Methods first used in test()">
    private void testPlatform(DataSource source, TestContext context) {
        String platform = context.getPlatform();
        setupPlatform(context);
        try {
            for (Entry<Method, ValidationProfile> entry : profiles.entrySet()) {
                context.setTestMethod(entry.getKey());
                testMethod(source, context);
            }
        } finally {
            context.teardownAfterPlatform();
            teardownPlatform(context);
        }
    }

    private void testMethod(DataSource source, TestContext context) {
        String testcase = getDefaultTestcase(context);
        context.setTestcase(testcase);
        setupMethod(context);
        List<String> mark = source.getLineIdentifiers();
        source.reset();
        try {
            while (source.hasNext()) {
                source.next();
                testLine(source, context);
            }
        } finally {
            reset(source, mark);
            context.teardownAfterTestcase();
            teardownMethod(context);
        }
    }

    private String getDefaultTestcase(TestContext context) {
        Method test = context.getTestMethod();
        String input = test.getName();
        String postfix = testSignature.getPostfix();
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
        Method test = context.getTestMethod();
        setup(source, context);
        try {
            runTest(source, context);
        } finally {
            context.teardownAfterLine();
            teardown(source, context);
        }
    }

    private void runTest(DataSource source, TestContext context) {
        Method test = context.getTestMethod();
        try {
            Boolean result = (Boolean) test.invoke(this, source, context);
            if (result != null)
                reportStatus(result, context);
        } catch (IllegalAccessException ex) {
            String template = ResourceBundleRegistry.getString("datadriven",
                                                               "datadriven.error.test.illegalAccess");
            reportException(context, ex, String.format(template, test));
        } catch (IllegalArgumentException ex) {
            String template = ResourceBundleRegistry.getString("datadriven",
                                                               "datadriven.error.test.invalidArguments");
            reportException(context, ex, String.format(template, test));
        } catch (NullPointerException ex) {
            String template = ResourceBundleRegistry.getString("datadriven",
                                                               "datadriven.error.test.wrongType");
            reportException(context, ex, String.format(template, test));
        } catch (InvocationTargetException ex) {
            Throwable cause = ex.getCause();
            String template = ResourceBundleRegistry.getString("datadriven",
                                                               "datadriven.error.test.invocation");
            reportException(context, cause, String.format(template, test));
        }
    }

    private void reportException(TestContext context, Throwable ex, String error) {
        String platform = context.getPlatform();
        String testcase = context.getTestcase();
        String step = context.getTeststep();
        if (step == null)
            reportGroup.setFailed(platform, suite, testcase);
        else
            reportGroup.setFailed(platform, suite, testcase, suite);
    }

    protected void reportStatus(boolean result, TestContext context) {
        String platform = context.getPlatform();
        String testcase = context.getTestcase();
        String step = context.getTeststep();
        if (step == null)
            reportGroup.setStatus(result, platform, suite, testcase);
        else
            reportGroup.setStatus(result, platform, suite, testcase, step);
    }
    // </editor-fold>

    protected abstract void setupClass(TestContext context);

    protected abstract void setupPlatform(TestContext context);

    protected abstract void setupMethod(TestContext context);

    protected abstract void setup(DataSource source, TestContext context);

    protected abstract void teardown(DataSource source, TestContext context);

    protected abstract void teardownMethod(TestContext context);

    protected abstract void teardownPlatform(TestContext context);

    protected abstract void teardownClass(TestContext context);
}
