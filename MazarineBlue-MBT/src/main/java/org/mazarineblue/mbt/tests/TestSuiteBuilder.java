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

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.mazarineblue.mbt.criteria.step.StepPattern;
import org.mazarineblue.mbt.criteria.trace.TracePattern;
import org.mazarineblue.mbt.expressions.Expression;
import static org.mazarineblue.mbt.expressions.Expression.and;
import org.mazarineblue.mbt.goals.TestGoal;
import org.mazarineblue.mbt.model.State;
import org.mazarineblue.mbt.model.StateMachine;
import org.mazarineblue.mbt.model.Transition;

public class TestSuiteBuilder {

    private final StateMachine sm;

    public TestSuiteBuilder(StateMachine sm) {
        this.sm = sm;
    }

    public Set<TraceExtension> extendsPathForTestGoal(TestGoal tg) {
        return tg.getTracePatterns().stream()
                .map(tp -> buildTraceExtension(tp, 0, TraceExtension.createDefault()))
                .flatMap(Set::stream)
                .collect(HashSet::new, Set::add, Set::addAll);
    }

    //<editor-fold defaultstate="collapsed" desc="Helper methods for extendsPathForTestGoal">
    private Set<TraceExtension> buildTraceExtension(TracePattern tp, int index, TraceExtension te) {
        Set<TraceExtension> traceExtensions = new HashSet<>();
        if (index < tp.size()) {
            StepPattern sp = tp.get(index);
            sm.getTransitions().stream()
                    .filter(sp::match)
                    .forEach(t -> {
                        te.add(t);
                        traceExtensions.addAll(buildTraceExtension(tp, index + 1, te));
                        te.remove(t);
                    });
        } else {
            State s = te.getLastTargetState();
            sm.getOutgoing(s).stream()
                    .filter(t -> !sm.isTargetCompleting(t))
                    .forEach(t -> {
                        te.add(t);
                        traceExtensions.addAll(buildTraceExtension(tp, index + 1, te));
                        te.remove(t);
                    });
            long count = sm.getOutgoing(s).stream()
                    .filter(t -> !sm.isTargetCompleting(t))
                    .count();
            if (count == 0)
                traceExtensions.add(TraceExtension.createDefault(te));
        }
        return traceExtensions;
    }
    //</editor-fold>

    public TestCase createTestCase(TraceExtension te) {
        State s = te.getLastTargetState();
        TestCase tc = searchBackwardsFromNode(s, te);
        return tc.isValidTestCase() ? tc : null;
    }

    //<editor-fold defaultstate="collapsed" desc="Helper methods for createTestCase">
    private TestCase searchBackwardsFromNode(State s, TraceExtension te) {
        if (sm.getInitialConfiguration().contains(s))
            return new TestCaseImpl(te);
        Optional<Transition> found = sm.getIncomming(s).stream()
                .filter(t -> te.contains(t))
                .findAny();
        if (found.isPresent()) {
            TestCase tc = traverseTransition(found.get(), te);
            if (tc != null)
                return tc;
        } else
            for (Transition t : sm.getIncomming(s)) {
                te.addFirst(t);
                TestCase tc = traverseTransition(t, te);
                if (tc != null)
                    return tc;
                te.remove(t);
            }
        return null;
    }
    
    private TestCase traverseTransition(Transition t, TraceExtension te) {
        Expression e1 = te.getExpression();
        
        te.transformExpressions(t.getEffect());
        Expression e2 = te.getExpression();
        Expression e3 = t.getGuard() == null ? null : t.getGuard().getExpression();
        Expression e4 = e1 == null && e3 == null ? null
                : e2 == null ? e3.convertToDNF()
                : e3 == null ? e2.convertToDNF()
                : and(e2, e3).convertToDNF();
        
        te.setExpression(e4);
        TestCase tc = searchBackwardsFromNode(t.getSource(), te);
        te.setExpression(e1);
        return tc.isValidTestCase() ? tc : null;
    }
    //</editor-fold>
}
