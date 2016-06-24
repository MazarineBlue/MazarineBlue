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
package org.mazarineblue.datasources;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.mazarineblue.datasources.exceptions.EmtpyScopeStack;
import org.mazarineblue.datasources.exceptions.IllegalSourceStateException;
import org.mazarineblue.datasources.exceptions.NegativeIndexException;
import org.mazarineblue.datasources.exceptions.ScopeNotFoundException;
import org.mazarineblue.datasources.exceptions.VariableAlreadyDeclared;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class BlackboardSource
        extends ArraySource {

    static public final String LOCAL = "Local";
    static public final String SHEET = "Sheet";
    static public final String GLOBAL = "Global";

    private final Deque<ObjectArraySource> stack = new ArrayDeque<>();

    public BlackboardSource(String identifier) {
        super(identifier, true);
    }

    @Override
    public String toString() {
        return "BlackboardSource{"
                + getColumns().stream().collect(Collectors.joining(", "))
                + "}";
    }

    private String columnToString(String column) {
        try {
            return column + "='" + getData(column) + "'";
        } catch (IllegalSourceStateException ex) {
            return column + "=exception";
        }
    }

    public void setup() {
        setup(null);
    }

    public void setup(Map<String, Object> presetVariables) {
        if (stack.isEmpty() == false)
            throw new IllegalStateException("This blackboard is already used");
        ObjectArraySource source = new ObjectArraySource(GLOBAL, true);
        if (presetVariables != null)
            for (Map.Entry<String, Object> entry : presetVariables.entrySet())
                source.setData(entry.getKey(), entry.getValue());
        stack.add(source);
    }

    public void teardown() {
        stack.pop();
        if (stack.isEmpty() == false)
            throw new IllegalStateException(
                    "This blackboard stack must be empty");
    }

    public void pushSource(String scope) {
        ObjectArraySource source = new ObjectArraySource(scope, true);
        stack.add(source);
    }

    @Deprecated
    public void pushSource(ObjectArraySource source) {
        stack.add(source);
    }

    public ObjectArraySource popSource() {
        if (stack.size() == 1)
            throw new EmtpyScopeStack();
        return stack.pop();
    }

    @Deprecated
    public ObjectArraySource popSource(ObjectArraySource source) {
        return popSourceHelper(source.getSourceIdentifier());
    }

    public boolean popSource(String scope) {
        return popSourceHelper(scope) != null;
    }
    
    private ObjectArraySource popSourceHelper(String scope) {
        Iterator<ObjectArraySource> it = stack.iterator();
        while (it.hasNext()) {
            ObjectArraySource source = it.next();
            if (scope.equals(source.getSourceIdentifier())) {
                it.remove();
                return source;
            }
        }
        throw new ScopeNotFoundException(scope);
    }
    
    public void declareVariable(String name) {
        ObjectArraySource source = stack.getFirst();
        declareVariable(source, name);
    }

    public void declareVariable(String name, String scope) {
        ObjectArraySource source = findByScope(scope);
        declareVariable(source, name);
    }

    private ObjectArraySource findByScope(String scope) {
        for (ObjectArraySource source : stack)
            if (scope.equals(source.getSourceIdentifier()))
                return source;
        throw new ScopeNotFoundException(scope);
    }

    private void declareVariable(ObjectArraySource source, String name) {
        if (source.containsColumn(name))
            throw new VariableAlreadyDeclared(name);
        source.setData(name, null);
    }

    @Override
    public final Object getData(String column) {
        for (ObjectArraySource source : stack)
            if (source.containsColumn(column))
                return source.getData(column);
        return null;
    }

    @Override
    public int getIndex(String column) {
        return -1;
    }

    @Override
    public Object getData(int index) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public boolean setData(String column, Object value) {
        ObjectArraySource source = findByName(column);
        source.setData(column, value);
        return true;
    }

    private ObjectArraySource findByName(String name) {
        for (ObjectArraySource source : stack)
            if (source.containsColumn(name))
                return source;
        return stack.getFirst();
    }

    @Override
    public boolean setData(int index, Object value) {
        if (index < 0)
            throw new NegativeIndexException(index);
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public final Set<String> getColumns() {
        return stack.stream()
                .flatMap(x -> x.getColumns().stream())
                .collect(Collectors.toSet());
    }
}
