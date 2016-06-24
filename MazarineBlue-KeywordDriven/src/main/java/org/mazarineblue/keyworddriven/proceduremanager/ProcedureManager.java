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

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import org.mazarineblue.datasources.BlackboardSource;
import org.mazarineblue.datasources.DataSource;
import org.mazarineblue.keyworddriven.InstructionLine;
import org.mazarineblue.keyworddriven.InterpreterContext;
import org.mazarineblue.keyworddriven.feeds.Feed;
import org.mazarineblue.keyworddriven.feeds.FeedBuilder;
import org.mazarineblue.keyworddriven.logs.Log;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class ProcedureManager {

    private final FeedBuilder feedBuilder;
    private final Deque<Map<String, Procedure>> stack;

    /**
     * Create an procedure manager that can manage procedures within a scope.
     */
    public ProcedureManager(FeedBuilder feedBuilder) {
        this.feedBuilder = feedBuilder;
        stack = new ArrayDeque<>(4);
        stack.push(new HashMap<>(4));
    }

    /**
     * Copy constructor
     */
    public ProcedureManager(ProcedureManager procedureManager) {
        feedBuilder = procedureManager.feedBuilder;
        stack = new ArrayDeque<>(procedureManager.stack);
    }

    public final Map<String, Procedure> peekScope() {
        return stack.peek();
    }

    /**
     * Creates a new scope an pushed this to the stack.
     */
    public void pushScope() {
        stack.push(new HashMap<>(stack.peek()));
    }

    /**
     * Pushes a scope to the stack.
     *
     * @param scope the map with procedures to be pushed.
     */
    public void pushScope(Map<String, Procedure> scope) {
        stack.push(scope);
    }

    /**
     * Pops a scope from the stack.
     *
     * @return the scope.
     */
    public Map<String, Procedure> popScope() {
        return stack.pop();
    }

    /**
     * Indicates if the current scope contains a procedure that matches the call of the specied line.
     *
     * @param line to be tested agains the current scope.
     * @return true if a match is found.
     */
    public boolean contains(InstructionLine line) {
        if (line.getNamespace().isEmpty() == false)
            return false;
        return peekScope().containsKey(line.getKeyword());
    }

    /**
     * Registers a procedure under the specified calling name. If a procedure is registered under this name, then it is
     * replaced and the old procedure is returned.
     *
     * @param name      the name under which the procedure can be called.
     * @param procedure the procedure to register.
     * @return the old procedure if this was registered un the specified name.
     */
    final public Procedure register(String name, Procedure procedure) {
        return peekScope().put(name, procedure);
    }

    public void unregister(String name) {
        peekScope().remove(name);
    }

    public void execute(InstructionLine line, InterpreterContext context) {
        Procedure procedure = getProcedure(line);
        Collection<InstructionLine> instructions = procedure.getInstructions();
        Feed feed = feedBuilder.createFeed(instructions);
        context.executeNested(line, procedure, feed);
    }

    private Procedure getProcedure(InstructionLine line) {
        String keyword = line.getKeyword();
        Procedure procedure = peekScope().get(keyword);
        return procedure;
    }
}
