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
package org.mazarineblue.test.datadriven.keywords;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.mazarineblue.datasources.DataSource;
import org.mazarineblue.datasources.ObjectArraySource;
import org.mazarineblue.datasources.exceptions.BlackboardException;
import org.mazarineblue.eventbus.exceptions.SubscriberTargetException;
import org.mazarineblue.keyworddriven.InstructionLine;
import org.mazarineblue.keyworddriven.InterpreterContext;
import org.mazarineblue.keyworddriven.RunningInterpreter;
import org.mazarineblue.keyworddriven.documentMediators.DocumentMediator;
import org.mazarineblue.keyworddriven.feeds.Feed;
import org.mazarineblue.keyworddriven.feeds.FeedBuilder;
import org.mazarineblue.keyworddriven.librarymanager.Library;
import org.mazarineblue.keyworddriven.logs.Log;
import org.mazarineblue.keyworddriven.proceduremanager.Procedure;
import org.mazarineblue.keyworddriven.sheetfactories.SheetFactory;
import org.mazarineblue.test.datadriven.DataDrivenSuite;
import org.mazarineblue.test.datadriven.TestContext;
import org.mazarineblue.test.datadriven.exceptions.IllegalMethodException;
import org.mazarineblue.test.events.SetTestcaseEvent;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class DynamicSuite
        extends DataDrivenSuite {

    private InterpreterContext context;

    private final FeedBuilder feedBuilder;
    private ObjectArraySource source;
    private Testcase setupClass, setupPlatform, setup;
    private Testcase teardownClass, teardownPlatform, teardown;
    private Map<String, Testcase> map;
    private Map<String, Procedure> procedures;
    private Library[] libraries;

    public DynamicSuite(FeedBuilder feedBuilder)
            throws IllegalMethodException {
        super();
        this.feedBuilder = feedBuilder;
        setupClass = setupPlatform = setup
                = teardownClass = teardownPlatform = teardown
                = new Testcase(Collections.EMPTY_LIST);
    }

    Library[] getLibraries() {
        return libraries;
    }

    void setLibrary(Library... libraries) {
        this.libraries = libraries;
    }

    void setProcedures(Map<String, Procedure> procedures) {
        this.procedures = procedures;
    }

    Map<String, Procedure> getProcedures() {
        return procedures;
    }

    protected void setContext(InterpreterContext context) {
        this.context = context;
    }

    public void setSetupClass(Collection<InstructionLine> collection) {
        if (collection != null)
            setupClass = new Testcase(collection);
    }

    public void setSetupPlatform(Collection<InstructionLine> collection) {
        if (collection != null)
            setupPlatform = new Testcase(collection);
    }

    public void setSetup(Collection<InstructionLine> collection) {
        if (collection != null)
            setup = new Testcase(collection);
    }

    public void setTeardown(Collection<InstructionLine> collection) {
        if (collection != null)
            teardown = new Testcase(collection);
    }

    public void setTeardownPlatform(Collection<InstructionLine> collection) {
        if (collection != null)
            teardownPlatform = new Testcase(collection);
    }

    public void setTeardownClass(Collection<InstructionLine> collection) {
        if (collection != null)
            teardownClass = new Testcase(collection);
    }

    public boolean hasMethod(String name) {
        return map.containsKey(name);
    }

    void addTestcases(Map<String, Testcase> testcases) {
        if (testcases == null)
            throw new IllegalArgumentException("There where no testcases added.");
        this.map = new HashMap<>(testcases.size() * 4 / 3 + 4);
        for (String name : testcases.keySet())
            addTestcase(name, testcases.get(name));
    }

    public void addTestcase(String name, Testcase testcase) {
        if (map == null)
            map = new HashMap<>(4);
        map.put(name, testcase);
    }

    private Boolean status = true;

    void resetStatus() {
        status = true;
    }

    void setTestcaseSkipped() {
        status = null;
    }

    @Override
    protected void setupClass(TestContext context) {
        if (setupClass.isEmpty())
            return;
        this.context.log().info("Setup platform: " + context.getPlatform());
        execute(setupClass);
    }

    private String platform;

    @Override
    protected void setupPlatform(TestContext context) {
        platform = context.getPlatform();
        if (setupPlatform.isEmpty())
            return;
        this.context.log().info("Setup platform: " + context.getPlatform());
        execute(setupPlatform);
    }

    @Override
    protected void setupMethod(TestContext context) {
        this.context.log().info("Testcase: " + context);
        this.context.log().incrementNestedInstruction(new Date());
    }

    @Override
    protected void setup(DataSource source, TestContext context) {
        this.context.log().info("Setup");
        this.source = context.getAsDataSource();
        this.context.blackboard().pushSource(this.source);
        execute(setup);
    }

    protected Boolean execute(String name) {
        context.log().info("Testcase: " + name);
        Testcase testcase = map.get(name);
        return execute(testcase);
    }

    private Boolean execute(Testcase testcase) {
        RunningInterpreter executor = context.executor();
        DocumentMediator documentMediator = context.documentMediator();
        SheetFactory sheetFactory = context.sheetFactory();
        Feed feed = feedBuilder.createFeed(testcase.getInstructionLines());
        Log log = context.log();

        try {
            executor.publish(new SetTestcaseEvent(platform, getSuiteName(),
                                                  testcase.getName()));
            executor.executeNested(feed, log, context);
            return status;
        } catch (SubscriberTargetException ex) {
            log.error(ex.getCause());
            return false;
        }
    }

    @Override
    protected void teardown(DataSource source, TestContext context) {
        try {
            this.context.log().info("Teardown");
            execute(teardown);
            this.context.blackboard().popSource(this.source);
            this.source = null;
        } catch (BlackboardException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    protected void teardownMethod(TestContext context) {
        this.context.log().decrementNestedInstruction(new Date());
    }

    @Override
    protected void teardownPlatform(TestContext context) {
        if (teardownPlatform.isEmpty())
            return;
        this.context.log().info("Teardown platform: " + context.getPlatform());
        execute(teardownPlatform);
    }

    @Override
    protected void teardownClass(TestContext context) {
        if (teardownClass.isEmpty())
            return;
        this.context.log().info("Teardown class");
        execute(teardownClass);
    }
}
