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
import fitnesse.plugins.PluginException;
import fitnesse.testrunner.WikiTestPage;
import fitnesse.testsystems.ClassPath;
import fitnesse.testsystems.Descriptor;
import fitnesse.testsystems.ExecutionLogListener;
import fitnesse.testsystems.TestExecutionException;
import fitnesse.testsystems.TestPage;
import fitnesse.testsystems.TestSystem;
import fitnesse.testsystems.UnableToStartException;
import fitnesse.testsystems.UnableToStopException;
import fitnesse.wiki.WikiPage;
import fitnesse.wiki.WikiPageFactory;
import static fitnesse.wiki.WikiPageUtil.setPageContents;
import static fitnesse.wiki.fs.InMemoryPage.makeRoot;
import fitnesse.wikitext.parser.VariableSource;
import java.io.File;
import java.io.IOException;
import org.mazarineblue.eventdriven.Invoker;
import org.mazarineblue.eventdriven.Link;
import org.mazarineblue.fitnesse.engineplugin.FixtureLoaderLink;
import org.mazarineblue.fitnesse.events.NewInstanceEvent;
import org.mazarineblue.fitnesse.exceptions.FitnesseNotEnabledException;
import org.mazarineblue.keyworddriven.Keyword;
import org.mazarineblue.keyworddriven.Library;
import org.mazarineblue.keyworddriven.PassInvoker;
import org.mazarineblue.keyworddriven.util.GracefullConvertor;

public class FitnesseLibrary
        extends Library {

    private Link link;

    public FitnesseLibrary() {
        super("org.mazarineblue.fitnesse.pluginloader");
    }

    @Override
    public void doSetup(Invoker invoker) {
        link = new FixtureLoaderLink();
        invoker.interpreter().addLink(link);
    }

    @Override
    public void doTeardown(Invoker invoker) {
        if (link == null)
            throw new FitnesseNotEnabledException();
        invoker.interpreter().removeLink(link);
        link = null;
    }

//                new ExecuteInstructionLineEvent("Use fixture", "login with username", "Bob", "xyzzy"),
//                new ExecuteInstructionLineEvent("login message"),
//                new ExecuteInstructionLineEvent("login message"),
//                new ExecuteInstructionLineEvent("login with username and password", "Bob", "xyzzy"),
//                new ExecuteInstructionLineEvent("login with username and password", "Bob", "bad password"),
    @PassInvoker
    @Keyword("Use fixture")
    public void useFixture(Invoker invoker, String fixture, Object... args) {
        String actor = null;
        String degraceFixture = GracefullConvertor.degraceClass(fixture);
        NewInstanceEvent e = new NewInstanceEvent(actor, degraceFixture, args);
        invoker.publish(e);
    }

    private PluginRecorderLink pluginRecorder;

    @PassInvoker
    @Keyword("Open fitnesse plugin")
    public void importFinessePlugin(Invoker invoker, String plugin)
            throws IOException, PluginException {
        pluginRecorder = new PluginRecorderLink();
        invoker.chain().addLink(pluginRecorder);
    }

    @Keyword("Close fitnesse plugin")
    public void closeFinessePlugin()
            throws UnableToStartException, TestExecutionException, UnableToStopException, IOException, PluginException {
        String content = pluginRecorder.getContent();
        pluginRecorder = null;

        ContextConfigurator cc = ContextConfigurator.empty();
        cc.withWikiPageFactory(new WikiPageFactory() {
            @Override
            public WikiPage makePage(File file, String string, WikiPage wp, VariableSource vs) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public boolean supports(File file) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
        FitNesseContext fitNesseContext = cc.makeFitNesseContext();
        TestSystem testSystem = fitNesseContext.testSystemFactory.create(new Descriptor() {
            @Override
            public String getTestSystem() {
                return "mazarineblue";
            }

            @Override
            public String getTestSystemType() {
                return getTestSystem().split(":")[0];
            }

            @Override
            public ClassPath getClassPath() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public boolean runInProcess() {
                return true;
            }

            @Override
            public boolean isDebug() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public String getVariable(String string) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public ExecutionLogListener getExecutionLogListener() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
        testSystem.start();
        WikiPage sourcePage = makeRoot("RooT");
        setPageContents(sourcePage, content);
        TestPage tp = new WikiTestPage(sourcePage);
        testSystem.runTests(tp);
        testSystem.bye();
    }
}
