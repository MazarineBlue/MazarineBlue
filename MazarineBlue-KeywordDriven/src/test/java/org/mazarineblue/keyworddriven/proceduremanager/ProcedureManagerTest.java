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
package org.mazarineblue.keyworddriven.proceduremanager;

import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mazarineblue.keyworddriven.InstructionLine;
import org.mazarineblue.keyworddriven.InterpreterContext;
import org.mazarineblue.keyworddriven.ProcessorTest;
import org.mazarineblue.keyworddriven.feeds.Feed;
import org.mazarineblue.keyworddriven.feeds.FeedBuilder;
import org.mazarineblue.keyworddriven.util.old.TestFeedBuilder;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class ProcedureManagerTest {

    static private Procedure emptyProcedure, procedureWithoutParameters, procedureWithParameters;
    static private String nameEmptyProcedure, nameProcedureWithoutParameters, nameProcedureWithParameters;
    static private String keywordEmptyProcedure, keywordProcedureWithoutParameters, keywordProcedureWithParameters;
    static private InstructionLine lineEmptyProcedure, lineProcedureWithoutParameters, lineProcedureWithParameters;
    private FeedBuilder feedBuilder;
    private ProcedureManager manager;

    @BeforeClass
    static public void setupClass() {
        String[] parameters = new String[1];
        parameters[0] = "parameter";
        emptyProcedure = new Procedure(new String[0], new ArrayList());
        procedureWithoutParameters = new Procedure(new String[0], new ArrayList());
        procedureWithParameters = new Procedure(parameters, new ArrayList());

        keywordEmptyProcedure = "empty procedure";
        keywordProcedureWithoutParameters = "procedure without parameters";
        keywordProcedureWithParameters = "procedure with parameters";

        nameEmptyProcedure = "Empty procedure";
        nameProcedureWithoutParameters = "Procedure without parameters";
        nameProcedureWithParameters = "Procedure with parameters";

        String[] arr = new String[1];
        arr[0] = "parameter";
        lineEmptyProcedure = new InstructionLine(keywordEmptyProcedure, "",
                                                 nameEmptyProcedure, new Object[0]);
        lineProcedureWithoutParameters = new InstructionLine(keywordProcedureWithoutParameters, "",
                                                             nameProcedureWithoutParameters, new Object[0]);
        lineProcedureWithParameters = new InstructionLine(keywordProcedureWithParameters, "",
                                                          nameProcedureWithParameters, arr);
    }

    @AfterClass
    static public void teardownClass() {
        nameEmptyProcedure = null;
        emptyProcedure = null;
    }

    @Before
    public void setup() {
        feedBuilder = new TestFeedBuilder();
        manager = new ProcedureManager(feedBuilder);
    }

    @After
    public void teardown() {
        manager = null;
    }

    @Test
    public void testRegister() {
        manager.register(nameEmptyProcedure, emptyProcedure);
        Assert.assertEquals(true, manager.contains(lineEmptyProcedure));
        Assert.assertEquals(1, manager.peekScope().keySet().size());
    }

    @Test
    public void testExecuteWithEmptyFeed()
            throws Exception {
        Feed feed = feedBuilder.createFeed(new InstructionLine[0]);
        InterpreterContext context = ProcessorTest.createExecutorContext(feed);
        manager.register(nameEmptyProcedure, emptyProcedure);
        manager.execute(lineEmptyProcedure, context);
    }

    @Test
    public void testExecuteProcedureWithoutParameters()
            throws Exception {
        Feed feed = feedBuilder.createFeed(new InstructionLine[0]);
        InterpreterContext context = ProcessorTest.createExecutorContext(feed);
        manager.register(nameEmptyProcedure, emptyProcedure);
        manager.execute(lineEmptyProcedure, context);
    }

    @Test
    public void testExecuteProcedureWithParameters()
            throws Exception {
        Feed feed = feedBuilder.createFeed(new InstructionLine[0]);
        InterpreterContext context = ProcessorTest.createExecutorContext(feed);
        manager.register(nameEmptyProcedure, emptyProcedure);
        manager.execute(lineEmptyProcedure, context);
    }

    @Test
    public void testUnregister() {
        manager.register(nameEmptyProcedure, emptyProcedure);
        manager.unregister(nameEmptyProcedure);
        Assert.assertEquals(false, manager.contains(lineEmptyProcedure));
        Assert.assertEquals(0, manager.peekScope().keySet().size());
    }

    @Test
    public void testPushTheScope() {
        manager.register(nameEmptyProcedure, emptyProcedure);
        manager.pushScope();
        Assert.assertEquals(true, manager.contains(lineEmptyProcedure));
        Assert.assertEquals(1, manager.peekScope().keySet().size());
    }

    @Test
    public void testPushAndPopTheScope() {
        manager.register(nameEmptyProcedure, emptyProcedure);
        manager.pushScope();
        manager.popScope();
        Assert.assertEquals(true, manager.contains(lineEmptyProcedure));
        Assert.assertEquals(1, manager.peekScope().keySet().size());
    }
}
