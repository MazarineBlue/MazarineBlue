/*
 * Copyright (c) 2017 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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
package org.mazarineblue.fitnesse;

import fitnesse.ContextConfigurator;
import fitnesse.FitNesseContext;
import fitnesse.testrunner.WikiPageIdentity;
import fitnesse.testrunner.WikiTestPage;
import fitnesse.testsystems.ClassPath;
import fitnesse.testsystems.CompositeExecutionLogListener;
import fitnesse.testsystems.Descriptor;
import fitnesse.testsystems.ExecutionLogListener;
import fitnesse.testsystems.TestPage;
import fitnesse.testsystems.TestSystem;
import fitnesse.wiki.WikiPage;
import fitnesse.wiki.WikiPageFactory;
import static fitnesse.wiki.WikiPageUtil.setPageContents;
import static fitnesse.wiki.fs.InMemoryPage.makeRoot;
import fitnesse.wikitext.parser.VariableSource;
import java.io.File;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mazarineblue.eventdriven.Feed;
import org.mazarineblue.eventdriven.events.ClosingInterpreterEvent;
import org.mazarineblue.eventdriven.feeds.MemoryFeed;
import org.mazarineblue.executors.FeedExecutor;
import org.mazarineblue.executors.FeedExecutorFactory;
import org.mazarineblue.executors.util.FeedExecutorOutputSpy;
import org.mazarineblue.executors.util.TestFeedExecutorFactory;
import org.mazarineblue.fs.MemoryFileSystem;
import org.mazarineblue.keyworddriven.events.ExecuteInstructionLineEvent;
import org.mazarineblue.links.UnconsumedExceptionThrowerLink;

@Ignore
public class FitnessePluginLoaderIT {

    private MemoryFileSystem fs;
    private FeedExecutor executor;
    private FeedExecutorOutputSpy output;

    @Before
    public void setup() {
        fs = new MemoryFileSystem();
        output = new FeedExecutorOutputSpy();
        FeedExecutorFactory factory = TestFeedExecutorFactory.getDefaultInstance(fs, output);
        factory.addLinkAfterEventBus(() -> new UnconsumedExceptionThrowerLink(ClosingInterpreterEvent.class));
        executor = factory.create();
    }

    @Test
    public void testFixture_AllKnownInstructions_Passes() {
        Feed feed = new MemoryFeed(
                new ExecuteInstructionLineEvent("Import library", "org.mazarineblue.fitnesse.pluginloader"),
                new ExecuteInstructionLineEvent("Use fixture", "login dialog driver", "Bob", "xyzzy"),
                new ExecuteInstructionLineEvent("login message"),
                new ExecuteInstructionLineEvent("login message"),
                new ExecuteInstructionLineEvent("login with username and password", "Bob", "xyzzy"),
                new ExecuteInstructionLineEvent("login with username and password", "Bob", "bad password"));
        executor.execute(feed);
        assertFalse(executor.containsErrors());
    }

    @Test
    public void testFixture_UnknownInstruction_CausesError() {
        Feed feed = new MemoryFeed(
                new ExecuteInstructionLineEvent("Import library", "org.mazarineblue.fitnesse.pluginloader"),
                new ExecuteInstructionLineEvent("Use fixture", "login dialog driver", "Bob", "xyzzy"),
                new ExecuteInstructionLineEvent("xxx", "Bob", "bad password"));
        executor.execute(feed);
        assertTrue(executor.containsErrors());
    }

    @Test
    public void testx() {
        Feed feed = new MemoryFeed(
                new ExecuteInstructionLineEvent("Import library", "org.mazarineblue.fitnesse.pluginloader"),
                new ExecuteInstructionLineEvent("Open fitnesse plugin", "test"),
                
                new ExecuteInstructionLineEvent("script", "login dialog driver", "Bob", "xyzzy"),
                new ExecuteInstructionLineEvent("login with username", "Bob", "and password", "xyzzy"),
                new ExecuteInstructionLineEvent("check", "login message", "Bob logged in."),
                new ExecuteInstructionLineEvent("reject", "login with username", "Bob", "and password", "bad password"),
                new ExecuteInstructionLineEvent("check", "login message", "Bob not logged in."),
                new ExecuteInstructionLineEvent("check not", "login message", "Bob logged in."),
                new ExecuteInstructionLineEvent("ensure", "login with username", "Bob", "and password", "xyzzy"),
                new ExecuteInstructionLineEvent("note", "this is a comment"),
                new ExecuteInstructionLineEvent("show", "number of login attempts"),
                new ExecuteInstructionLineEvent("$symbol=", "login message"),

                new ExecuteInstructionLineEvent("Close fitnesse plugin"));
        executor.execute(feed);
        output.throwFirstException();
        assertFalse(executor.containsErrors());
    }

    @Test
    public void test()
            throws Exception {
        CompositeExecutionLogListener executionLogListener = new CompositeExecutionLogListener();
        Descriptor d = new Descriptor() {
            @Override
            public String getTestSystem() {
                String testSystemName = getVariable(WikiPageIdentity.TEST_SYSTEM);
                return testSystemName == null ? "test" : testSystemName;
            }

            @Override
            public String getTestSystemType() {
                return getTestSystem().split(":")[0];
            }

            @Override
            public ClassPath getClassPath() {
                return new ClassPath(".", System.getProperty("path.separator"));
            }

            @Override
            public boolean runInProcess() {
                return false;
            }

            @Override
            public boolean isDebug() {
                return false;
            }

            @Override
            public String getVariable(String key) {
                if (key.equals("TEST_SYSTEM"))
                    return "test";
                if (key.equals("COMMAND_PATTERN"))
                    return "java -cp %p %m";
                if (key.equals("TEST_RUNNER"))
                    return "fit.FitServer";
                if (key.equals("PATH_SEPARATOR"))
                    return System.getProperty("path.separator");
                if (key.equals("CLASSPATH_PROPERTY"))
                    return System.getProperty("java.class.path");
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public ExecutionLogListener getExecutionLogListener() {
                return executionLogListener;
            }
        };
        WikiPageFactory wikiPageFactory = new WikiPageFactory() {
            @Override
            public WikiPage makePage(File file, String string, WikiPage wp, VariableSource vs) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public boolean supports(File file) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };

        // https://github.com/unclebob/fitnesse/blob/master/src/fitnesse/ContextConfigurator.java
        ContextConfigurator cc = ContextConfigurator.empty();
        cc.withWikiPageFactory(wikiPageFactory);
        FitNesseContext fitNesseContext = cc.makeFitNesseContext();

        String content = "!define TEST_SYSTEM {test}\n"
                + "\n"
                + "!| script | login dialog driver | Bob | xyzzy |\n"
                + "| login with username | Bob | and password | xyzzy |\n"
                + "| check | login message | Bob logged in. |\n"
                + "| reject | login with username | Bob | and password | bad password |\n"
                + "| check | login message | Bob not logged in. |\n"
                + "| check not | login message | Bob logged in. |\n"
                + "| ensure | login with username | Bob | and password | xyzzy |\n"
                + "| note | this is a comment |\n"
                + "| show | number of login attempts |\n"
                + "| $symbol= | login message |\n"
                + "";

        TestSystem testSystem = fitNesseContext.testSystemFactory.create(d);
        testSystem.start();
        WikiPage sourcePage = makeRoot("RooT");
        setPageContents(sourcePage, content);
        TestPage tp = new WikiTestPage(sourcePage);
        testSystem.runTests(tp);
        testSystem.bye();
    }

    @Test
    public void test2()
            throws Exception {
        Feed feed = new MemoryFeed(
                new ExecuteInstructionLineEvent("Import library", "org.mazarineblue.fitnesse.pluginloader"),
                new ExecuteInstructionLineEvent("Use fixture", "login dialog driver", "Bob", "xyzzy"),
                new ExecuteInstructionLineEvent("login message"),
                new ExecuteInstructionLineEvent("login message"),
                new ExecuteInstructionLineEvent("login with username and password", "Bob", "xyzzy"),
                new ExecuteInstructionLineEvent("login with username and password", "Bob", "bad password"),
// Test
                new ExecuteInstructionLineEvent("Import library", "org.mazarineblue.test"),
                new ExecuteInstructionLineEvent("Suite", "Suite A"),
                new ExecuteInstructionLineEvent("Description", "description"),
                new ExecuteInstructionLineEvent("Test", "Test 1"),
                new ExecuteInstructionLineEvent("Ignore"),
                new ExecuteInstructionLineEvent("Description", "description"),
                new ExecuteInstructionLineEvent("Test ID", "TC-1"),
                new ExecuteInstructionLineEvent("Issue", "PFM-1"),
                new ExecuteInstructionLineEvent("Severity", "blocker", "critical", "normal", "minor", "trivial"),
                new ExecuteInstructionLineEvent("Step", "Step a"),
                new ExecuteInstructionLineEvent("Description", "description"),
                new ExecuteInstructionLineEvent("End step"),
                new ExecuteInstructionLineEvent("Note", "This is a note"),
                new ExecuteInstructionLineEvent("Take screenshot"),
                new ExecuteInstructionLineEvent("Use fixture", "login with username", "Bob", "xyzzy"),
                new ExecuteInstructionLineEvent("Check", "login message", "Bob logged in"),
                new ExecuteInstructionLineEvent("Check not", "login message", "Bob logged in"),
                new ExecuteInstructionLineEvent("Ensure", "login with username and password", "Bob", "xyzzy"),
                new ExecuteInstructionLineEvent("Reject", "login with username and password", "Bob", "bad password"),
                new ExecuteInstructionLineEvent("Check", "actual value", "expected", "type"),
                new ExecuteInstructionLineEvent("Check not", "actual value", "not expected", "type"),
                new ExecuteInstructionLineEvent("Check screenshot", "map"),
                new ExecuteInstructionLineEvent("Check screenshot not", "map"),
                new ExecuteInstructionLineEvent("Show", "number of login attempts"));
// End test
        executor.execute(feed);
    }
}
