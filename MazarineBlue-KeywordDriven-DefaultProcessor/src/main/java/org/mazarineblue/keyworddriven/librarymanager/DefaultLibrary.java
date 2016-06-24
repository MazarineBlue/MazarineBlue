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
package org.mazarineblue.keyworddriven.librarymanager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.mazarineblue.datasources.BlackboardSource;
import org.mazarineblue.datasources.DataSource;
import org.mazarineblue.datasources.ObjectArraySource;
import org.mazarineblue.datasources.exceptions.BlackboardException;
import org.mazarineblue.eventbus.Event;
import org.mazarineblue.eventbus.EventHandler;
import org.mazarineblue.eventbus.EventService;
import org.mazarineblue.events.EndSheetEvent;
import org.mazarineblue.events.SetStatusEvent;
import org.mazarineblue.events.StartSheetEvent;
import org.mazarineblue.keyworddriven.InstructionLine;
import org.mazarineblue.keyworddriven.InterpreterContext;
import org.mazarineblue.keyworddriven.Keyword;
import org.mazarineblue.keyworddriven.Parameters;
import org.mazarineblue.keyworddriven.exceptions.ConsumableException;
import org.mazarineblue.keyworddriven.exceptions.InstructionUnaccessableException;
import org.mazarineblue.keyworddriven.exceptions.LibraryNotFoundException;
import org.mazarineblue.keyworddriven.exceptions.LibraryUninstantiableException;
import org.mazarineblue.keyworddriven.exceptions.NoMatchException;
import org.mazarineblue.keyworddriven.exceptions.ParseExpressionException;
import org.mazarineblue.keyworddriven.exceptions.ProcedureOpenOnSheetEndException;
import org.mazarineblue.keyworddriven.exceptions.ProcedureOpenOnStartException;
import org.mazarineblue.keyworddriven.exceptions.ScopeNotFoundException;
import org.mazarineblue.keyworddriven.exceptions.SheetNotFoundException;
import org.mazarineblue.keyworddriven.exceptions.VariableAlreadyDeclaredException;
import org.mazarineblue.keyworddriven.feeds.Feed;
import org.mazarineblue.keyworddriven.links.LibraryLink;
import org.mazarineblue.keyworddriven.links.flow.BreakLibraryLink;
import org.mazarineblue.keyworddriven.links.flow.DoLoopLibraryLink;
import org.mazarineblue.keyworddriven.links.flow.ForLibraryLink;
import org.mazarineblue.keyworddriven.links.flow.IfLibraryLink;
import org.mazarineblue.keyworddriven.links.flow.LoopTester;
import org.mazarineblue.keyworddriven.links.flow.SwitchLibraryLink;
import org.mazarineblue.keyworddriven.links.flow.WhileLoopLibraryLink;
import org.mazarineblue.keyworddriven.logs.Log;
import org.mazarineblue.keyworddriven.proceduremanager.ProcedureManager;
import org.mazarineblue.parser.Parser;
import org.mazarineblue.parser.precedenceclimbing.DefaultOperatorParser;
import org.mazarineblue.parser.variable.VariableParser;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class DefaultLibrary
        extends Library {

    private final ClassLoader classLoader;
    private final ProcedureManager procedureManager;
    private static final Parser parser = new VariableParser();
    private static final DefaultOperatorParser operatorParser = new DefaultOperatorParser();
    private static final String[] libraryPostfixes = {".keywords.DefaultLibrary", ".DefaultLibrary"};

    public DefaultLibrary(EventService<Event> eventService, ProcedureManager procedureManager) {
        super("org.mazarineblue");
        setEvents(eventService);
        classLoader = getClass().getClassLoader();
        this.procedureManager = procedureManager;
    }

    @Override
    protected void setup() {
    }

    @Override
    protected void teardown() {
    }

    @Keyword("Import")
    @Parameters(min = 1, max = 1)
    public void importLibrary(String path) {
        Class<?> type = loadClass(path, libraryPostfixes);
        if (isLibrary(type) == false)
            throw new LibraryNotFoundException(path);
        Library library = createLibrary(path, type);
        executor().libraries().register(library);
    }

    // <editor-fold defaultstate="collapsed" desc="Helper methods for importLibrary()">
    private Class<?> loadClass(String path, String[] arr) {
        for (String str : arr)
            if (hasClass(path + str))
                return loadClass(path + str);
        return loadClass(path);
    }

    private boolean hasClass(String path) {
        try {
            classLoader.loadClass(path);
            return true;
        } catch (ClassNotFoundException ex) {
            return false;
        }
    }

    private Class<?> loadClass(String path) {
        try {
            return classLoader.loadClass(path);
        } catch (ClassNotFoundException ex) {
            throw new LibraryNotFoundException(path);
        }
    }

    private boolean isLibrary(Class<?> type) {
        return Library.class.isAssignableFrom(type);
    }

    private Library createLibrary(String path, Class<?> type) {
        try {
            Library library = (Library) type.newInstance();
            library.setup(this);
            return library;
        } catch (IllegalAccessException ex) {
            throw new InstructionUnaccessableException(path, ex);
        } catch (InstantiationException ex) {
            throw new LibraryUninstantiableException(path, ex);
        }
    }
    // </editor-fold>

    @Keyword("Comment")
    @Parameters(min = 0)
    public void comment(String... param) {
    }

    // <editor-fold desc="Messages instructions">
    @Keyword("Echo")
    @Parameters(min = 1)
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public void echo(String... param) {
        System.out.println(toSingleLine(param));
    }

    private String toSingleLine(String... param) {
        String output = "";
        for (String str : param)
            output += str + ", ";
        int n = output.length();
        if (output.length() >= 2)
            output = output.substring(0, n - 2);
        return output;
    }

    @Keyword("Info")
    @Parameters(min = 1)
    public void info(String... msg) {
        log().info(toSingleLine(msg));
    }

    @Keyword("Warning")
    @Parameters(min = 1)
    public void warning(String... msg) {
        log().warning(toSingleLine(msg));
    }

    @Keyword("Error")
    @Parameters(min = 1)
    public void error(String... msg) {
        log().error(toSingleLine(msg));
    }

    @Keyword("Exception")
    @Parameters(min = 1)
    public void exception(String... msg) {
        throw new ConsumableException("Error message: " + toSingleLine(
                msg));
    }
    // </editor-fold>

    @Keyword("Sleep")
    @Parameters(min = 1, max = 1)
    public void sleep(long millis)
            throws InterruptedException {
        Thread.sleep(millis);
    }

    @Keyword("Set delay")
    @Parameters(min = 1, max = 1)
    public void setDelay(long millis)
            throws InterruptedException {
        executor().setSleep(millis);
    }

    // <editor-fold desc="Sheets instructions">
    @Keyword("Call sheet")
    public void callSheet(String sheetName) {
        try {
            procedureManager.pushScope();
            blackboard().pushSource(BlackboardSource.SHEET);
            importSheet(sheetName);
            blackboard().popSource(BlackboardSource.SHEET);
            procedureManager.popScope();
        } catch (BlackboardException ex) {
            throw new SheetNotFoundException(sheetName, ex);
        }
    }

    @Keyword("Import sheet")
    public final void importSheet(String sheetName) {
        publish(new StartSheetEvent(sheetName));
        Feed feed = sheetFactory().getSheetFeed(sheetName);
        Log log = log();
        InterpreterContext context = context();
        executor().executeNested(feed, log, context);
        publish(new EndSheetEvent(sheetName));
    }
    // </editor-fold>

    // <editor-fold desc="Procedure instructions">
    private ProcedureLibraryLink procedure;

    @Keyword("Procedure")
    @Parameters(min = 1)
    public void procedure(String name, String... paramters) {
        if (procedure != null && procedure.recoding())
            throw new ProcedureOpenOnStartException(procedure.getName());
        procedure = new ProcedureLibraryLink(this, procedureManager, name, paramters);
        executor().chain().insertFirst(procedure);
    }

    @EventHandler
    public void eventHandler(EndSheetEvent event) {
        if (procedure != null && procedure.recoding())
            throw new ProcedureOpenOnSheetEndException(event.getSheetName());
    }

    @Keyword("Call procedure")
    @Parameters(min = 1)
    public void callProcedure(String name, Object... parameters) {
        InterpreterContext context = context();
        InstructionLine original = context.getUnprocessedLine();
        String identifier = original.getLineIdentifier();
        String namespace = original.getLineIdentifier();
        InstructionLine line = new InstructionLine(identifier, namespace, name, parameters);
        procedureManager.execute(line, context);
    }
    // </editor-fold>

    // <editor-fold desc="Variable instructions">
    @Keyword("Declare global variable")
    @Parameters(min = 1)
    public void declareGlobalVariable(String name) {
        try {
            blackboard().declareVariable(name);
        } catch (BlackboardException ex) {
            throw new VariableAlreadyDeclaredException(name, ex);
        }
    }

    @Deprecated
    @Keyword("Declare sheet variable")
    @Parameters(min = 1)
    public void declareSheetVariable(String name) {
        try {
            blackboard().declareVariable(name, BlackboardSource.SHEET);
        } catch (BlackboardException ex) {
            throw new VariableAlreadyDeclaredException(name, ex);
        }
    }

    @Keyword("Declare variable")
    @Parameters(min = 1)
    public void declareVariable(String name) {
        try {
            blackboard().declareVariable(name, BlackboardSource.LOCAL);
        } catch (BlackboardException ex) {
            throw new VariableAlreadyDeclaredException(name, ex);
        }
    }

    @Keyword("Set")
    @Parameters(min = 1)
    public void set(String name, Object value) {
        blackboard().setData(name, value);
    }

    @Keyword("Set condition")
    public void setCondiation(String name, String expression) {
        try {
            DefaultOperatorParser operatorParser = new DefaultOperatorParser();
            Object value = operatorParser.parse(expression, blackboard(), Object.class);
            blackboard().setData(name, value);
        } catch (org.mazarineblue.parser.exceptions.ParserException ex) {
            throw new ParseExpressionException(expression, ex);
        }
    }

    @Keyword("Validate condition")
    public void validateCondition(String expression) {
        try {
            Boolean result = operatorParser.parse(expression, blackboard(), Boolean.class);
            publish(new SetStatusEvent(result));
        } catch (org.mazarineblue.parser.exceptions.ParserException ex) {
            throw new ParseExpressionException(expression, ex);
        }
    }

    @Keyword("Substring")
    @Parameters(min = 4)
    public void substring(String input, String variable, String regex,
                          String replacement) {
        Matcher matcher = fetchMatcher(regex, input);
        DataSource source = fetchRegexMappingSource(matcher);
        copyOutputToBlackboard(variable, replacement, source);
    }

    // <editor-fold defaultstate="collapsed" desc="Methods first used in substring()">
    private Matcher fetchMatcher(String regex, String input) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        if (matcher.find())
            return matcher;
        throw new NoMatchException(regex, input);
    }

    private DataSource fetchRegexMappingSource(Matcher matcher) {
        ObjectArraySource source = new ObjectArraySource("Substring-source", true);
        int n = matcher.groupCount();
        for (int i = 0; i <= n; ++i)
            source.setData(Integer.toString(i), matcher.group(i));
        return source;
    }

    private void copyOutputToBlackboard(String variable, String replacement,
                                        DataSource source) {
        try {
            Object output = parser.parse(replacement, source);
            blackboard().setData(variable, output);
        } catch (org.mazarineblue.parser.exceptions.ParserException ex) {
            throw new ParseExpressionException(replacement, ex);
        }
    }
    // </editor-fold>
    // </editor-fold>

    // <editor-fold desc="Flow instructions">
    @Keyword("If")
    @Parameters(min = 1)
    public void flowIf(String expression) {
        LibraryLink link = new IfLibraryLink(this, operatorParser, expression);
        executor().chain().insert(link);
    }

    @Keyword("Switch")
    @Parameters(min = 1)
    public void flowSwitch(Object data) {
        try {
            blackboard().pushSource("switch");
            BreakLibraryLink breakLink = new BreakLibraryLink(this, operatorParser);
            LibraryLink link = new SwitchLibraryLink(this, breakLink, data);
            executor().chain().insert(link);
            executor().chain().insert(breakLink, link);
            blackboard().popSource("switch");
        } catch (BlackboardException ex) {
            throw new ScopeNotFoundException("switch", ex);
        }
    }

    @Keyword("Do")
    public void flowDo() {
        try {
            blackboard().pushSource("do-loop");
            BreakLibraryLink breakLink = new BreakLibraryLink(this, operatorParser);
            LoopTester loopTester = new LoopTester(parser, breakLink, blackboard());
            LibraryLink link = new DoLoopLibraryLink(this, loopTester);
            executor().chain().insertFirst(link);
            executor().chain().insert(breakLink, link);
            blackboard().popSource("do-loop");
        } catch (BlackboardException ex) {
            throw new ScopeNotFoundException("do-loop", ex);
        }
    }

    @Keyword("While")
    @SuppressWarnings("AssignmentToMethodParameter")
    public void flowWhile(String expression) {
        expression = getUnprocessedExpression(0);
        try {
            blackboard().pushSource("while-loop");
            BreakLibraryLink breakLink = new BreakLibraryLink(this, operatorParser);
            LoopTester loopTester = new LoopTester(parser, breakLink, blackboard());
            LibraryLink link = new WhileLoopLibraryLink(this, loopTester, expression);
            executor().chain().insertFirst(link);
            executor().chain().insert(breakLink, link);
            blackboard().popSource("while-loop");
        } catch (BlackboardException ex) {
            throw new ScopeNotFoundException("while-loop", ex);
        }
    }

    private String getUnprocessedExpression(int index) {
        return (String) context().getUnprocessedLine().getParameter(index);
    }

    @Keyword("For")
    @Parameters(min = 4)
    @SuppressWarnings("AssignmentToMethodParameter")
    public void flowFor(String variable, String initialization, String terminination, String increment) {
        terminination = getUnprocessedExpression(2);
        increment = getUnprocessedExpression(3);
        try {
            blackboard().pushSource("for-loop");
            Object data = parser.parse(initialization, blackboard());
            blackboard().setData(variable, data);
            BreakLibraryLink breakLink = new BreakLibraryLink(this, operatorParser);
            LoopTester loopTester = new LoopTester(parser, breakLink, blackboard());
            LibraryLink link = new ForLibraryLink(this, loopTester,
                                                  parser, operatorParser,
                                                  variable, terminination,
                                                  increment);
            executor().chain().insertFirst(link);
            executor().chain().insert(breakLink, link);
            blackboard().popSource("for-loop");
        } catch (org.mazarineblue.parser.exceptions.ParserException ex) {
            throw new ParseExpressionException(increment, ex);
        } catch (BlackboardException ex) {
            throw new ScopeNotFoundException("for-loop", ex);
        }
    }
    // </editor-fold>
}
