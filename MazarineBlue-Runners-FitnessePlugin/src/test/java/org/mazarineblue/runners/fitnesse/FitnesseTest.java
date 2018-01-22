/*
 * Copyright (c) 2016 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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
package org.mazarineblue.runners.fitnesse;

import fitnesse.junit.FitNesseRunner;
import fitnesse.testrunner.MultipleTestsRunner;
import fitnesse.testrunner.PagesByTestSystem;
import fitnesse.testsystems.TestSystemFactory;
import fitnesse.wiki.WikiPage;
import static fitnesse.wiki.WikiPageUtil.setPageContents;
import static fitnesse.wiki.fs.InMemoryPage.makeRoot;
import java.util.ArrayList;
import static java.util.Arrays.asList;
import java.util.List;
import org.junit.Test;
import org.mazarineblue.eventnotifier.Event;
import org.mazarineblue.libraries.fixtures.events.PathEvent;
import org.mazarineblue.runners.fitnesse.events.AssignFitnesseEvent;
import org.mazarineblue.runners.fitnesse.events.CallFitnesseEvent;
import org.mazarineblue.runners.fitnesse.events.CreateFitnesseEvent;
import org.mazarineblue.runners.threadrunner.events.ByeEvent;
import org.mazarineblue.runners.threadrunner.util.ServiceRunnerFactoryDummy;
import org.mazarineblue.utililities.TwoWayPipe;
import org.mazarineblue.utililities.TwoWayPipeFactory;
import org.mazarineblue.utililities.util.TwoWayPipeFactoryAdopter;
import org.mazarineblue.utilities.WriteAssertingTwoWayPipe;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
@FitNesseRunner.FitnesseDir(".")
@FitNesseRunner.OutputDir("./target/fitnesse-results")
public class FitnesseTest {

    @Test
    public void testDecisionTable()
            throws Exception {
        String content = "!define TEST_SYSTEM {mazarineblue}\n"
                + "\n"
                + "| should I buy milk                      |\n"
                + "| pints of milk remaining | go to store? |\n"
                + "| 0                       | yes          |\n"
                + "| 1                       | no           |\n"
                + "\n";

        List<List<String>> table = new ArrayList<>(4);
        table.add(asList("pints of milk remaining", "go to store?"));
        table.add(asList("0", "yes"));
        table.add(asList("1", "no"));

        WriteAssertingTwoWayPipe<Event> expectedResult = new WriteAssertingTwoWayPipe<>();
        expectedResult.add(new CreateFitnesseEvent("decisionTable_0", "ShouldIBuyMilk"));
        expectedResult.add(new CallFitnesseEvent("decisionTable_0", "table", table));
        expectedResult.add(new CallFitnesseEvent("decisionTable_0", "beginTable"));
        expectedResult.add(new CallFitnesseEvent("decisionTable_0", "reset"));
        expectedResult.add(new CallFitnesseEvent("decisionTable_0", "setPintsOfMilkRemaining", "0"));
        expectedResult.add(new CallFitnesseEvent("decisionTable_0", "execute"));
        expectedResult.add(new CallFitnesseEvent("decisionTable_0", "goToStore"));
        expectedResult.add(new CallFitnesseEvent("decisionTable_0", "reset"));
        expectedResult.add(new CallFitnesseEvent("decisionTable_0", "setPintsOfMilkRemaining", "1"));
        expectedResult.add(new CallFitnesseEvent("decisionTable_0", "execute"));
        expectedResult.add(new CallFitnesseEvent("decisionTable_0", "goToStore"));
        expectedResult.add(new CallFitnesseEvent("decisionTable_0", "endTable"));
        expectedResult.add(new ByeEvent());

        runTest(content, expectedResult);
    }

    @Test
    public void testDynamicDecisionTable()
            throws Exception {
        String content = "!define TEST_SYSTEM {mazarineblue}\n"
                + "\n"
                + "| ddt: add up change                                                                    |\n"
                + "| # description                            | 1c          | $1 | total cents? | $ total? |\n"
                + "| some simple additions                    | 52          | 0  | 52           | 0.52     |\n"
                + "| save the total cents in a symbol         | 106         | 20 | $totalCents= | 21.06    |\n"
                + "| now use the total cents that were stored | $totalCents | 10 | 3106         | ~=31.1   | An example for Value Comparisons |\n"
                + "\n";

        List<List<String>> table = new ArrayList<>(4);
        table.add(asList("# description", "1c", "$1", "total cents?", "$ total?"));
        table.add(asList("some simple additions", "52", "0", "52", "0.52"));
        table.add(asList("save the total cents in a symbol", "106", "20", "$totalCents=", "21.06"));
        table.add(asList("now use the total cents that were stored", "$totalCents", "10", "3106", "~=31.1",
                         "An example for Value Comparisons"));

        WriteAssertingTwoWayPipe<Event> expectedResult = new WriteAssertingTwoWayPipe<>();
        expectedResult.add(new CreateFitnesseEvent("dynamicDecisionTable_0", "AddUpChange"));
        expectedResult.add(new CallFitnesseEvent("dynamicDecisionTable_0", "table", table));
        expectedResult.add(new CallFitnesseEvent("dynamicDecisionTable_0", "beginTable"));
        expectedResult.add(new CallFitnesseEvent("dynamicDecisionTable_0", "reset"));
        expectedResult.add(new CallFitnesseEvent("dynamicDecisionTable_0", "set", "1c", "52"));
        expectedResult.add(new CallFitnesseEvent("dynamicDecisionTable_0", "set", "$1", "0"));
        expectedResult.add(new CallFitnesseEvent("dynamicDecisionTable_0", "execute"));
        expectedResult.add(new CallFitnesseEvent("dynamicDecisionTable_0", "get", "total cents"));
        expectedResult.add(new CallFitnesseEvent("dynamicDecisionTable_0", "get", "$ total"));
        expectedResult.add(new CallFitnesseEvent("dynamicDecisionTable_0", "reset"));
        expectedResult.add(new CallFitnesseEvent("dynamicDecisionTable_0", "set", "1c", "106"));
        expectedResult.add(new CallFitnesseEvent("dynamicDecisionTable_0", "set", "$1", "20"));
        expectedResult.add(new CallFitnesseEvent("dynamicDecisionTable_0", "execute"));
        expectedResult.add(new CallFitnesseEvent("dynamicDecisionTable_0", "get", "total cents"));
        expectedResult.add(new AssignFitnesseEvent("totalCents", null));
        expectedResult.add(new CallFitnesseEvent("dynamicDecisionTable_0", "get", "$ total"));
        expectedResult.add(new CallFitnesseEvent("dynamicDecisionTable_0", "reset"));
        expectedResult.add(new CallFitnesseEvent("dynamicDecisionTable_0", "set", "1c", "$totalCents"));
        expectedResult.add(new CallFitnesseEvent("dynamicDecisionTable_0", "set", "$1", "10"));
        expectedResult.add(new CallFitnesseEvent("dynamicDecisionTable_0", "execute"));
        expectedResult.add(new CallFitnesseEvent("dynamicDecisionTable_0", "get", "total cents"));
        expectedResult.add(new CallFitnesseEvent("dynamicDecisionTable_0", "get", "$ total"));
        expectedResult.add(new CallFitnesseEvent("dynamicDecisionTable_0", "endTable"));
        expectedResult.add(new ByeEvent());

        runTest(content, expectedResult);
    }

    @Test
    public void testQueryTable()
            throws Exception {
        String content = "!define TEST_SYSTEM {mazarineblue}\n"
                + "\n"
                + "| Query:employees hired before     | 10-Dec-1980                          |\n"
                + "| company number | employee number | first name | last name | hire date   |\n"
                + "| 4808147        | 9942            | Bill       | Mitchell  | 19-Dec-1966 |\n"
                + "| 4808147        | 1429            | Bob        | Martin    | 10-Oct-1975 |\n"
                + "\n";

        List<List<String>> table = new ArrayList<>(4);
        table.add(asList("company number", "employee number", "first name", "last name", "hire date"));
        table.add(asList("4808147", "9942", "Bill", "Mitchell", "19-Dec-1966"));
        table.add(asList("4808147", "1429", "Bob", "Martin", "10-Oct-1975"));

        WriteAssertingTwoWayPipe<Event> expectedResult = new WriteAssertingTwoWayPipe<>();
        expectedResult.add(new CreateFitnesseEvent("queryTable_0", "EmployeesHiredBefore", "10-Dec-1980"));
        expectedResult.add(new CallFitnesseEvent("queryTable_0", "table", table));
        expectedResult.add(new CallFitnesseEvent("queryTable_0", "query"));
        expectedResult.add(new ByeEvent());

        runTest(content, expectedResult);
    }

    @Test
    public void testScript()
            throws Exception {
        String content = "!define TEST_SYSTEM {mazarineblue}\n"
                + "\n"
                + "!| script | login dialog driver | Bob | xyzzy |\n"
                + "| login with username | Bob | and password | xyzzy |\n"
                + "| check | login message | Bob logged in. |\n"
                + "\n"
                + "!| script | login dialog driver | Bob | xyzzy |\n"
                + "| reject | login with username | Bob | and password | bad password |\n"
                + "| check | login message | Bob not logged in. |\n"
                + "| check not | login message | Bob logged in. |\n"
                + "\n"
                + "!| script | login dialog driver | Bob | xyzzy |\n"
                + "| ensure | login with username | Bob | and password | xyzzy |\n"
                + "| note | this is a comment |\n"
                + "| show | number of login attempts |\n"
                + "| $symbol= | login message |\n"
                + "\n";

        WriteAssertingTwoWayPipe<Event> expectedResult = new WriteAssertingTwoWayPipe<>();
        expectedResult.add(new CreateFitnesseEvent("scriptTableActor", "LoginDialogDriver", "Bob", "xyzzy"));
        expectedResult.add(new CallFitnesseEvent("scriptTableActor", "loginWithUsernameAndPassword", "Bob", "xyzzy"));
        expectedResult.add(new CallFitnesseEvent("scriptTableActor", "loginMessage"),
                           new CallFitnesseEvent("scriptTableActor", "loginMessage").setResult("Bob logged in."));

        expectedResult.add(new CreateFitnesseEvent("scriptTableActor", "LoginDialogDriver", "Bob", "xyzzy"));
        expectedResult.add(new CallFitnesseEvent("scriptTableActor", "loginWithUsernameAndPassword", "Bob",
                                                 "bad password"));
        expectedResult.add(new CallFitnesseEvent("scriptTableActor", "loginMessage"),
                           new CallFitnesseEvent("scriptTableActor", "loginMessage").setResult("Bob not logged in."));
        expectedResult.add(new CallFitnesseEvent("scriptTableActor", "loginMessage"),
                           new CallFitnesseEvent("scriptTableActor", "loginMessage").setResult("Bob not logged in."));

        expectedResult.add(new CreateFitnesseEvent("scriptTableActor", "LoginDialogDriver", "Bob", "xyzzy"));
        expectedResult.add(new CallFitnesseEvent("scriptTableActor", "loginWithUsernameAndPassword", "Bob", "xyzzy"));
        expectedResult.add(new CallFitnesseEvent("scriptTableActor", "numberOfLoginAttempts"));
        expectedResult.add(new CallFitnesseEvent("scriptTableActor", "loginMessage"),
                           new CallFitnesseEvent("scriptTableActor", "loginMessage").setResult("Bob logged in."));
        expectedResult.add(new AssignFitnesseEvent("symbol", "Bob logged in."));
        expectedResult.add(new ByeEvent());

        runTest(content, expectedResult);
    }

    @Test
    public void testTable()
            throws Exception {
        String content = "!define TEST_SYSTEM {mazarineblue}\n"
                + "\n"
                + "| Table:Bowling                                                            |"
                + "| 3| 5| 4| / | X |   | X |   | 3|  4| 6|  /| 7|   2| 3|  4| 9|  -| 4| /|  3|"
                + "|  | 8|  | 28|   | 51|   | 68|  | 75|  | 92|  | 101|  |108|  |117|  |  |130|"
                + "\n";

        List<String> list = asList("", "3", "5", "4", "/", "X", "", "X", "", "3", "4", "6", "/", "7", "2", "3", "4", "9",
                                   "-", "4", "/", "3", "", "", "8", "", "28", "", "51", "", "68", "", "75", "", "92", "",
                                   "101", "", "108", "", "117", "", "", "130");

        WriteAssertingTwoWayPipe<Event> expectedResult = new WriteAssertingTwoWayPipe<>();
        expectedResult.add(new CreateFitnesseEvent("tableTable_0", "Bowling", list.toArray()));
        expectedResult.add(new CallFitnesseEvent("tableTable_0", "doTable", new ArrayList<>(0)));
        expectedResult.add(new ByeEvent());

        runTest(content, expectedResult);
    }

    @Test
    public void testImport()
            throws Exception {
        String content = "!define TEST_SYSTEM {mazarineblue}\n"
                + "\n"
                + "| import             |\n"
                + "| fitnesse.slim.test |\n"
                + "| fitnesse.fixtures  |\n"
                + "\n";

        WriteAssertingTwoWayPipe<Event> expectedResult = new WriteAssertingTwoWayPipe<>();
        expectedResult.add(new PathEvent("fitnesse.slim.test"));
        expectedResult.add(new PathEvent("fitnesse.fixtures"));
        expectedResult.add(new ByeEvent());
        runTest(content, expectedResult);
    }

    @Test
    public void testScenario()
            throws Exception {
        String content = "!define TEST_SYSTEM {mazarineblue}\n"
                + "!| scenario   | make page    | page name    | with      | wikiText |\n"
                + "| create page | @pageName    | with content | @wikiText            |\n"
                + "| check       | request page | @pageName    | 200                  |\n"
                + "\n"
                + "!| scenario | page            | wiki text | renders   | html text |\n"
                + "| make page | MyPage          | with      | @wikiText             |\n"
                + "| ensure    | content matches | @htmlText                         |\n"
                + "| show      | content                                             |\n"
                + "\n"
                + "!| Script                                    |\n"
                + "| page | !3 hello | renders | <h3>hello</h3> |\n"
                + "\n";

        WriteAssertingTwoWayPipe<Event> expectedResult = new WriteAssertingTwoWayPipe<>();
        expectedResult.add(new CallFitnesseEvent("scriptTableActor", "createPageWithContent", "MyPage", "!3 hello"));
        expectedResult.add(new CallFitnesseEvent("scriptTableActor", "requestPage", "MyPage"));
        expectedResult.add(new CallFitnesseEvent("scriptTableActor", "contentMatches", "<h3>hello</h3>"));
        expectedResult.add(new CallFitnesseEvent("scriptTableActor", "content"));
        expectedResult.add(new ByeEvent());

        runTest(content, expectedResult);
    }

    @Test
    public void testLibraries()
            throws Exception {
        String content = "!define TEST_SYSTEM {mazarineblue}\n"
                + "| Library     |\n"
                + "| echo script |\n"
                + "\n"
                + "!| script | my fixture                                     |\n"
                + "| check   | do business logic | 42                         |\n"
                + "| note    | Triggering the \"echo script\" library fixture |\n"
                + "| echo    | Hello world                                    |\n"
                + "\n";

        WriteAssertingTwoWayPipe<Event> expectedResult = new WriteAssertingTwoWayPipe<>();
        expectedResult.add(new CreateFitnesseEvent("library1", "EchoScript"));
        expectedResult.add(new CreateFitnesseEvent("scriptTableActor", "MyFixture"));
        expectedResult.add(new CallFitnesseEvent("scriptTableActor", "doBusinessLogic"));
        expectedResult.add(new CallFitnesseEvent("scriptTableActor", "echo", "Hello world"));
        expectedResult.add(new ByeEvent());

        runTest(content, expectedResult);
    }

    // <editor-fold defaultstate="true" desc="Helper methods for runTest()">
    private static void runTest(String testcase, TwoWayPipe<Event> expectedResult)
            throws Exception {
        PagesByTestSystem testsystem = getTestcase(testcase);
        runTest(testsystem, expectedResult);
    }

    private static PagesByTestSystem getTestcase(String content) {
        WikiPage page = makeRoot("RooT");
        setPageContents(page, content);
        List<WikiPage> pages = asList(page);
        return new PagesByTestSystem(pages, page);
    }

    private static void runTest(PagesByTestSystem testcase, TwoWayPipe<Event> expectedResult)
            throws Exception {
        runTest(testcase, getTestSystemFactory(expectedResult));
    }

    private static TestSystemFactory getTestSystemFactory(TwoWayPipe<Event> pipe) {
        TwoWayPipeFactory<Event> pipeFactory = new TwoWayPipeFactoryAdopter<>(pipe);
        MazarineBlueSlimClient slimClient = new MazarineBlueSlimClient(pipeFactory, new ServiceRunnerFactoryDummy());
        return new MazarineBlueTestSystemFactory(slimClient);
    }

    private static void runTest(PagesByTestSystem system, TestSystemFactory factory)
            throws Exception {
        MultipleTestsRunner runner = new MultipleTestsRunner(system, factory);
        runner.executeTestPages();
    }
    // </editor-fold>
}
