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
package org.mazarineblue.mbt.tests;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.mazarineblue.mbt.X;
import org.mazarineblue.mbt.expressions.Assignment;
import org.mazarineblue.mbt.expressions.Expression;
import org.mazarineblue.mbt.expressions.Implies;
import org.mazarineblue.mbt.model.Effect;
import org.mazarineblue.mbt.model.State;
import org.mazarineblue.mbt.model.Transition;

class TraceExtensionImpl
        implements TraceExtension {

    private Expression expression = null;
    private final List<Transition> transitions;

    TraceExtensionImpl() {
        transitions = new ArrayList<>();
    }

    TraceExtensionImpl(TraceExtension te) {
        transitions = new ArrayList<>(te.getTransitions());
    }

    @Override
    public void addFirst(Transition t) {
        transitions.add(0, t);
    }

    @Override
    public void add(Transition t) {
        if (!transitions.add(t))
            throw new IllegalStateException();
    }

    @Override
    public void remove(Transition t) {
        if (!transitions.remove(t))
            throw new IllegalStateException();
    }

    @Override
    public State getLastTargetState() {
        int n = transitions.size();
        if (n == 0)
            throw new IllegalStateException();
        return transitions.get(n - 1).getTarget();
    }

    @Override
    public List<Transition> getTransitions() {
        return Collections.unmodifiableList(transitions);
    }

    @Override
    public void transformExpressions(Effect effect) {
        if (expression == null)
            return;
        Collection<Expression> arr = effect.getExpressions();
        for (Expression e : arr)
            if (e instanceof Implies) // Implies(Expression, Set)
                try {
                    Expression counterPart = e.not().swap();
                    expression = arr.contains(counterPart)
                            ? expression.replace((Assignment) e.getRightExpression(), e.getLeftExpression()) // l
                            : expression.append((Assignment) e.getRightExpression(), e.getLeftExpression()); // r || l
                } catch (RuntimeException ex) {
                }
            else if (e instanceof Assignment) // Set()
                expression = expression.replace((Assignment) e);
            else
                System.out.println();
    }

    @Override
    public boolean match(Transition t) {
        return false;
    }

    @Override
    public boolean contains(Transition t) {
        return transitions.contains(t);
    }

    @Override
    public int compareTo(TraceExtension other) {
        return X.compateTo(transitions, other.getTransitions());
    }

    @Override
    public Expression getExpression() {
        return expression;
    }

    @Override
    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    private void eval() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
