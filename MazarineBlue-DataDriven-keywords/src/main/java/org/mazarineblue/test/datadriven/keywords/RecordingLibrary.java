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
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.NotFoundException;
import org.mazarineblue.keyworddriven.InstructionLine;
import org.mazarineblue.keyworddriven.Keyword;
import org.mazarineblue.keyworddriven.Parameters;
import org.mazarineblue.keyworddriven.librarymanager.Library;
import org.mazarineblue.keyworddriven.links.RecordingInstructionLink;
import org.mazarineblue.test.datadriven.exceptions.ClassNotDynamicallyFoundException;
import org.mazarineblue.test.datadriven.exceptions.DynamicCompileException;
import org.mazarineblue.test.datadriven.exceptions.DynamicInstantationException;
import org.mazarineblue.test.datadriven.exceptions.IllegalDynamicAccessException;
import org.mazarineblue.test.datadriven.exceptions.SuiteHasNoTestcasesException;
import org.mazarineblue.test.events.GetPlatformsFromReportEvent;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class RecordingLibrary
        extends AbstractChildLibrary {

    RecordingLibrary(String suiteName, DataDrivenLibrary parent) {
        super(suiteName, parent);
    }

    private enum State {

        NONE, TESTCASE,
        SETUP_CLASS, SETUP_PLATFORM, SETUP,
        TEARDOWN_CLASS, TEARDOWN_PLATFORM, TEARDOWN,
    };

    private State state = State.NONE;
    private String testcase = null;
    private Map<String, Testcase> testcases;
    private Map<State, Collection<InstructionLine>> other;

    @Keyword("Setup class")
    @Parameters(min = 0, max = 0)
    public void dataDrivenSetupClass() {
        processRecording();
        state = State.SETUP_CLASS;
        record();
    }

    @Keyword("Setup platform")
    @Parameters(min = 0, max = 0)
    public void dataDrivenSetupPlatform() {
        processRecording();
        state = State.SETUP_PLATFORM;
        record();
    }

    @Keyword("Setup")
    @Parameters(min = 0, max = 0)
    public void dataDrivenSetup() {
        processRecording();
        state = State.SETUP;
        record();
    }

    @Keyword("Cleanup")
    @Keyword("Teardown")
    @Parameters(min = 0, max = 0)
    public void dataDrivenTeardown() {
        processRecording();
        state = State.TEARDOWN;
        record();
    }

    @Keyword("Cleanup platform")
    @Keyword("Teardown platform")
    @Parameters(min = 0, max = 0)
    public void dataDrivenTeardownPlatform() {
        processRecording();
        state = State.TEARDOWN_PLATFORM;
        record();
    }

    @Keyword("Cleanup class")
    @Keyword("Teardown class")
    @Parameters(min = 0, max = 0)
    public void dataDrivenTeardownClass() {
        processRecording();
        state = State.TEARDOWN_CLASS;
        record();
    }

    @Keyword("Testcase")
    @Parameters(min = 1, max = 1)
    public void dataDrivenTestcase(String testcase) {
        processRecording();
        state = State.TESTCASE;
        this.testcase = testcase;
        if (testcases == null)
            testcases = new HashMap<>(4);
        testcases.put(testcase, new Testcase(testcase));
        record();
    }

    private void record() {
        Set<String> keywords = getKeywords();
        String[] arr = keywords.toArray(new String[keywords.size()]);
        parent.record(arr);
    }

    private void processRecording() {
        if (state == State.NONE)
            return;
        RecordingInstructionLink recording = parent.endRecording();
        Collection<InstructionLine> lines = recording.getRecording();
        switch (state) {
            default:
                if (other == null)
                    other = new HashMap<>(4);
                other.put(state, lines);
                break;
            case TESTCASE:
                testcases.get(testcase).setInstructionLines(lines);
            case NONE:
        }
    }

    static private long id = 0;

    DynamicSuite finish(Library... libraries) {
        if (testcases == null)
            throw new SuiteHasNoTestcasesException(suiteName);
        processRecording();
        try {
            CtHelper helper = new CtHelper(++id);
            CtClass clazz = helper.createClassDefinition();
            helper.addMethods(clazz, testcases);

            GetPlatformsFromReportEvent platforms = new GetPlatformsFromReportEvent();
            publish(platforms);
            DynamicSuite suite = helper.createInstance(clazz);
            suite.setLibrary(libraries);
            suite.addListener(new LibraryReportListener(parent));
            suite.setPlatforms(platforms.getPlatforms());
            suite.setContext(context());
            if (other != null) {
                suite.setSetupClass(other.get(State.SETUP_CLASS));
                suite.setSetupPlatform(other.get(State.SETUP_PLATFORM));
                suite.setSetup(other.get(State.SETUP));
                suite.setTeardown(other.get(State.TEARDOWN));
                suite.setTeardownPlatform(other.get(State.TEARDOWN_PLATFORM));
                suite.setTeardownClass(other.get(State.TEARDOWN_CLASS));
            }
            suite.addTestcases(testcases);
            return suite;
        } catch (NotFoundException ex) {
            throw new ClassNotDynamicallyFoundException(ex);
        } catch (CannotCompileException ex) {
            throw new DynamicCompileException(ex);
        } catch (InstantiationException ex) {
            throw new DynamicInstantationException(ex);
        } catch (IllegalAccessException ex) {
            throw new IllegalDynamicAccessException(ex);
        }
    }
}
