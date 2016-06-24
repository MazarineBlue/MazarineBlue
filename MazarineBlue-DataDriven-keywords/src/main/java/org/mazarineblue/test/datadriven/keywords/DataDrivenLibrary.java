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

import java.util.Arrays;
import java.util.Map;
import org.mazarineblue.datasources.DataSource;
import org.mazarineblue.eventbus.EventHandler;
import org.mazarineblue.events.EndSheetEvent;
import org.mazarineblue.keyworddriven.Keyword;
import org.mazarineblue.keyworddriven.Parameters;
import org.mazarineblue.keyworddriven.librarymanager.Library;
import org.mazarineblue.keyworddriven.links.RecordingInstructionLink;
import org.mazarineblue.keyworddriven.links.ResolveKeywordConflictsLink;
import org.mazarineblue.keyworddriven.logs.dom.Status;
import org.mazarineblue.keyworddriven.proceduremanager.Procedure;
import org.mazarineblue.test.datadriven.TestContextMediator;
import org.mazarineblue.test.datadriven.exceptions.CallDataDrivenSuiteFirstException;
import org.mazarineblue.test.datadriven.exceptions.PlatformsNotFoundException;
import org.mazarineblue.test.datadriven.exceptions.ReportNotSelectedException;
import org.mazarineblue.test.datadriven.exceptions.SuiteAlreadyRunningException;
import org.mazarineblue.test.datadriven.exceptions.SuiteCloseWithoutSuiteOpenException;
import org.mazarineblue.test.datadriven.exceptions.SuiteDuplicateException;
import org.mazarineblue.test.datadriven.exceptions.SuiteNotFoundException;
import org.mazarineblue.test.datadriven.exceptions.SuiteOpenBeforeEndSuiteException;
import org.mazarineblue.test.events.MiniLogEvent;
import org.mazarineblue.test.links.MiniLogLink;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class DataDrivenLibrary
        extends Library {

    private RecordingLibrary recordingLibrary;
    private SuiteLibrary suiteLibrary;
    private final SuiteStore store = new SuiteStore();

    public DataDrivenLibrary() {
        super("org.mazarineblue.test.datadriven");
    }

    @Override
    protected void setup() {
    }

    @Override
    protected void teardown() {
    }

    @Keyword("Data driven suite")
    @Parameters(min = 1, max = 1)
    public void dataDrivenSuite(String suiteName) {
        if (store.containsSuite(suiteName))
            throw new SuiteDuplicateException(suiteName);
        if (recordingLibrary != null)
            throw new SuiteOpenBeforeEndSuiteException(suiteName);
        recordingLibrary = new RecordingLibrary(suiteName, this);
        recordingLibrary.setup(this);
        suiteLibrary = new SuiteLibrary(suiteName, this);
        suiteLibrary.setup(this);
        executor().libraries().register(recordingLibrary);
        executor().libraries().register(suiteLibrary);
    }

    @EventHandler
    public void eventHandler(EndSheetEvent event) {
        if (recordingLibrary == null)
            return;
        throw new SuiteOpenBeforeEndSuiteException(event.getSheetName());
    }

    @Keyword("End data driven suite")
    @Parameters(min = 0, max = 0)
    public void endDataDrivenSuite() {
        if (recordingLibrary == null)
            throw new SuiteCloseWithoutSuiteOpenException();
        String name = recordingLibrary.getSuiteName();
        DynamicSuite suite = recordingLibrary.finish(suiteLibrary);
        suite.setProcedures(executor().procedures().peekScope());
        suite.setSuite(name);
        store.putSuite(name, suite);
        executor().libraries().unregister(recordingLibrary);
        executor().libraries().unregister(suiteLibrary);
        recordingLibrary = null;
    }

    private TestContextMediator testContextMediator = null;

    void setTestcase(String testcase) {
        if (testContextMediator == null)
            throw new CallDataDrivenSuiteFirstException();
        testContextMediator.setTestcase(testcase);
    }

    void setTeststep(String teststep) {
        if (testContextMediator == null)
            throw new CallDataDrivenSuiteFirstException();
        testContextMediator.setTeststep(teststep);
    }

    @Keyword("Call data driven suite")
    @Parameters(min = 1, max = 1)
    public void callDataDrivenSuite(String suiteName) {
        try {
            if (store.containsSuite(suiteName) == false)
                throw new SuiteNotFoundException(suiteName);
            DynamicSuite suite = store.getSuite(suiteName);
            if (suite == null)
                throw new SuiteNotFoundException(suiteName);
            if (testContextMediator != null)
                throw new SuiteAlreadyRunningException();

            // Variables
            suite.resetStatus();
            testContextMediator = new TestContextMediator();
            Map<String, Procedure> procedures = suite.getProcedures();

            // Pre
            MiniLogLink log = new MiniLogLink(Status.WARNING, Status.ERROR);
            executor().chain().insert(log);
            executor().libraries().register(suiteLibrary);
            executor().procedures().pushScope(procedures);

            DataSource source = getDataSource();
            suite.test(source, testContextMediator);

            // Post
            testContextMediator = null;
            executor().procedures().popScope();
            executor().libraries().unregister(suiteLibrary);
            executor().chain().remove(log);

            publish(new MiniLogEvent(log));
        } catch (PlatformsNotFoundException ex) {
            throw new ReportNotSelectedException();
        }
    }

    private ResolveKeywordConflictsLink resolveConflicts;
    private RecordingInstructionLink recording;

    void record(String... closingKeywords) {
        String[] arr = Arrays.copyOf(closingKeywords, closingKeywords.length + 1,
                                     String[].class);
        arr[closingKeywords.length] = "End data driven suite";
        recording = new RecordingInstructionLink(getNamespace(), arr);
        resolveConflicts = new ResolveKeywordConflictsLink(getNamespace(),
                                                           closingKeywords);
        executor().chain().insert(recording);
        executor().chain().insert(resolveConflicts);
    }

    RecordingInstructionLink endRecording() {
        executor().chain().remove(resolveConflicts);
        executor().chain().remove(recording);
        return recording;
    }
}
