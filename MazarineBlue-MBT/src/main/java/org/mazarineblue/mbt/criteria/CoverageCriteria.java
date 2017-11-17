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
package org.mazarineblue.mbt.criteria;

import java.util.ArrayList;
import static java.util.Arrays.asList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.mazarineblue.mbt.Variable;
import org.mazarineblue.mbt.criteria.step.StepPattern;
import org.mazarineblue.mbt.criteria.trace.TracePattern;
import org.mazarineblue.mbt.expressions.ComparisonExpression;
import org.mazarineblue.mbt.expressions.Expression;
import static org.mazarineblue.mbt.expressions.Expression.not;
import org.mazarineblue.mbt.expressions.False;
import org.mazarineblue.mbt.expressions.Or;
import org.mazarineblue.mbt.expressions.True;
import org.mazarineblue.mbt.goals.TestGoal;
import org.mazarineblue.mbt.model.Event;
import org.mazarineblue.mbt.model.State;
import org.mazarineblue.mbt.model.StateConfiguration;
import org.mazarineblue.mbt.model.StateMachine;
import org.mazarineblue.mbt.model.Transition;
import org.mazarineblue.mbt.model.ValueAssignment;

public interface CoverageCriteria {
    // allDefs <- allUses <- allDefUsePaths <- allPaths
    // allStates <- allConfiugrations <- allPaths
    // allStates <- allTransitions <- allTransitionPairs <- allPaths
    // allStates <- allTransitions <- decisionCoverage <- decisionConditionCoverage <- (...)
    // conditionCoverage <- decisionConditionCoverage <- modifiedConditionDecisionCoverage <- MultipleConditionCoverage

    public static Set<CoverageCriteria> getDefault() {
        Set<CoverageCriteria> all = new HashSet<>(TransitionBased.getDefault());
        all.addAll(ControlFlowBased.getDefault());
        all.addAll(DataFlowBased.getDefault());
        return all;
    }

    @SuppressWarnings("PublicInnerClass")
    public static interface TransitionBased {

        public static final AllStates ALL_STATES = new AllStates();
        public static final AllConfigurations ALL_CONFIGURATIONS = new AllConfigurations();
        public static final AllTransitions ALL_TRANSITIONS = new AllTransitions();
        public static final AllTransitionPairs ALL_TRANSITION_PAIRS = new AllTransitionPairs();
        public static final AllPaths ALL_PATHS = new AllPaths();

        public static Set<CoverageCriteria> getDefault() {
            return new TreeSet<>(asList(ALL_STATES, ALL_CONFIGURATIONS, ALL_TRANSITIONS,
                                        ALL_TRANSITION_PAIRS, ALL_PATHS));
        }

        //<editor-fold defaultstate="collapsed" desc="class AllStates">
        class AllStates
                extends AbstractCoverageCriteria {

            public AllStates() {
                super("Transition based - All states",
                      "Alle goals are satisfied when iff all state is visited.");
            }

            @Override
            public Set<TestGoal> getGoals(StateMachine sm, Limits limits) {
                return sm.getStates().stream()
                        .map(s -> new StateConfiguration(s))
                        .map(sc -> StepPattern.required(sc, null, null, null))
                        .map(sp -> TracePattern.create(sp))
                        .map(tp -> TestGoal.build(tp))
                        .collect(HashSet::new, Set::add, Set::addAll);
            }
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="class AllConfigurations">
        static class AllConfigurations
                extends AbstractCoverageCriteria {

            public AllConfigurations() {
                super("Transition based - All configurations",
                      "All goals are satisified iff all configuration (sets of concurrently active states) are visited.");
            }

            @Override
            public Set<TestGoal> getGoals(StateMachine sm, Limits limits) {
                return sm.getConfigurations().stream()
                        .map(sc -> StepPattern.required(sc, null, null, null))
                        .map(sp -> TracePattern.create(sp))
                        .map(tp -> TestGoal.build(tp))
                        .collect(HashSet::new, Set::add, Set::addAll);
            }
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="class AllTransitions">
        static class AllTransitions
                extends AbstractCoverageCriteria {

            public AllTransitions() {
                super("Transition based - All transitions",
                      "All goals are satisified iff all transitions are traversed.");
            }

            @Override
            public Set<TestGoal> getGoals(StateMachine sm, Limits limits) {
                return sm.getTransitions().stream()
                        .map(t -> StepPattern.required(null, null, null, asList(t)))
                        .map(sp -> TracePattern.create(sp))
                        .map(tp -> TestGoal.build(tp))
                        .collect(() -> ALL_STATES.getGoals(sm, limits), Set::add, Set::addAll);
            }
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="class AllTransitionPairs">
        static class AllTransitionPairs
                extends AbstractCoverageCriteria {

            public AllTransitionPairs() {
                super("Transition based - All transition pairs",
                      "All goals are satisified iff all pairs of adjacent transitions are traversed.");
            }

            @Override
            public Set<TestGoal> getGoals(StateMachine sm, Limits limits) {
                Set<TestGoal> testgoals = ALL_TRANSITIONS.getGoals(sm, limits);
                for (Transition t1 : sm.getTransitions())
                    for (Transition t2 : sm.getOutgoing(t1.getTarget()))
                        testgoals.add(createTestGoal(t1, t2));
                return testgoals;
            }

            private TestGoal createTestGoal(Transition t1, Transition t2) {
                return TestGoal.build(TracePattern.create(StepPattern.required(null, null, null, asList(t1))),
                                      TracePattern.create(StepPattern.required(null, null, null, asList(t2))));
            }
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="class AllPaths">
        static class AllPaths
                extends AbstractCoverageCriteria {

            public AllPaths() {
                super("Transition based - All paths",
                      "All goals are satisified iff all posible paths are traversed.");
            }

            @Override
            public Set<TestGoal> getGoals(StateMachine sm, Limits limits) {
                Set<TestGoal> testgoals = ALL_CONFIGURATIONS.getGoals(sm, limits);
                TransitionsRegistry registry = new TransitionsRegistry(sm, limits);
                testgoals.addAll(getPath(sm, registry));
                return testgoals;
            }

            private static Set<TestGoal> getPath(StateMachine sm, TransitionsRegistry registry) {
                Set<TestGoal> testgoals = new HashSet<>();
                if (!registry.getTransitions().isEmpty())
                    testgoals.add(createGoal(registry.getTransitions()));
                if (registry.isLimitReached())
                    return testgoals;
                for (State s : registry.getStates())
                    for (Transition t : sm.getOutgoing(s)) {
                        registry.push(t);
                        testgoals.addAll(getPath(sm, registry));
                        if (!t.equals(registry.pop()))
                            throw new IllegalStateException();
                    }
                return testgoals;
            }

            private static class TransitionsRegistry {

                private final List<Transition> transitions = new ArrayList<>();
                private final StateConfiguration sc;
                private final Limits limits;
                private int depth;

                private TransitionsRegistry(StateMachine sm, Limits limits) {
                    this.sc = sm.getInitialConfiguration();
                    this.limits = limits;
                }

                @Override
                public String toString() {
                    return transitions.isEmpty() ? "" : toString(transitions.iterator());
                }

                private String toString(Iterator<Transition> it) {
                    StringBuilder builder = new StringBuilder();
                    Transition t = it.next();
                    builder.append(t.getSource().toString()).append(" -> ").append(t.getTarget().toString());
                    while (it.hasNext())
                        builder.append(" -> ").append(it.next().getTarget().toString());
                    return builder.toString();
                }

                private boolean isLimitReached() {
                    return isLimitDepthReached();
                }

                private boolean isLimitDepthReached() {
                    int max = limits.getDepth();
                    return max >= 0 && depth >= max;
                }

                private List<Transition> getTransitions() {
                    return transitions;
                }

                private Collection<State> getStates() {
                    return sc.getStates();
                }

                private void push(Transition t) {
                    transitions.add(t);
                    sc.remove(t.getSource());
                    sc.add(t.getTarget());
                    ++depth;
                }

                private Transition pop() {
                    int n = transitions.size();
                    Transition last = transitions.remove(n - 1);
                    sc.remove(last.getTarget());
                    sc.add(last.getSource());
                    --depth;
                    return last;
                }
            }

            private static TestGoal createGoal(List<Transition> transitions) {
                TracePattern tp = transitions.stream()
                        .map(t -> StepPattern.required(null, null, null, asList(t)))
                        .collect(TracePattern::create, TracePattern::add, TracePattern::addAll);
                return TestGoal.build(tp);
            }
        }
        //</editor-fold>
    }

    @SuppressWarnings("PublicInnerClass")
    public static interface ControlFlowBased {

        public static final DesisionCoverage DECISION_COVERAGE = new DesisionCoverage();
        public static final ConditionCoverage CONDITION_COVERAGE = new ConditionCoverage();
        public static final DecisionConditionCoverage DESISION_CONDITION_COVERAGE = new DecisionConditionCoverage();
        public static final UniqueCauseMCDC UNIQUE_CAUSE_MCDC = new UniqueCauseMCDC();
        public static final MultipleConditionCoverage MULTIPLE_CONDITION_COVERAGE = new MultipleConditionCoverage();

        public static Set<CoverageCriteria> getDefault() {
            return new TreeSet<>(asList(DECISION_COVERAGE, CONDITION_COVERAGE, DESISION_CONDITION_COVERAGE,
                                        UNIQUE_CAUSE_MCDC, MULTIPLE_CONDITION_COVERAGE));
        }

        //<editor-fold defaultstate="collapsed" desc="class DesisionCoverage">
        class DesisionCoverage
                extends AbstractCoverageCriteria {

            public DesisionCoverage() {
                super("Control flow based - Desision coverage",
                      "All goals are satisified iff all guard condition is evaluated to true and false, respectivly.");
            }

            @Override
            public Set<TestGoal> getGoals(StateMachine sm, Limits limits) {
                return sm.getTransitions().stream()
                        .map(t -> createGoals(new StateConfiguration(t.getSource()),
                                              t.getEvents(),
                                              createExpressions(t)))
                        .collect(() -> TransitionBased.ALL_TRANSITIONS.getGoals(sm, limits), Set::addAll, Set::addAll);
            }

            private Set<TestGoal> createGoals(StateConfiguration sc, Set<Event> e, List<Expression> expressions) {
                return expressions.stream()
                        .map(expression -> StepPattern.required(sc, e, expression, null))
                        .map(sp -> TracePattern.create(sp))
                        .map(tp -> TestGoal.build(tp))
                        .collect(HashSet::new, Set::add, Set::addAll);
            }

            private List<Expression> createExpressions(Transition t) {
                Expression e = t.getGuard().getExpression();
                return e instanceof True || e instanceof False
                        ? asList((Expression) null)
                        : asList(e.simplify(), e.not().simplify());
            }

            private Set<Expression> collect(Expression e) {
                Set<Expression> set = new HashSet<>();
                e.visit(expression -> {
                    if (Or.class.isAssignableFrom(expression.getClass()))
                        return false;
                    if (Expression.class.isAssignableFrom(expression.getClass()))
                        set.add(expression.simplify());
                    return true;
                });
                return set;
            }
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="class ConditionCoverage">
        class ConditionCoverage
                extends AbstractCoverageCriteria {

            public ConditionCoverage() {
                super("Transition based - Condition coverage",
                      "All goals are satisified iff for each guard condition, alle included atomic boolean expressions "
                      + "are evaluated to true and false, respectivly.");
            }

            @Override
            public Set<TestGoal> getGoals(StateMachine sm, Limits limits) {
                return sm.getTransitions().stream()
                        .map(t -> createGoals(new StateConfiguration(t.getSource()),
                                              t.getEvents(),
                                              t.getGuard().getExpression()))
                        .flatMap(Collection::stream)
                        .collect(HashSet::new, Set::add, Set::addAll);
            }

            private Set<TestGoal> createGoals(StateConfiguration sc, Set<Event> e, Expression expression) {
                return expression.breakdown(ComparisonExpression.class).stream()
                        .map(ac -> createGoalsHelper(sc, e, ac))
                        .flatMap(Collection::stream)
                        .collect(HashSet::new, Set::add, Set::addAll);
            }

            private Set<TestGoal> createGoalsHelper(StateConfiguration sc, Set<Event> e, Expression ac) {
                Set<TestGoal> goals = new TreeSet<>();
                goals.add(TestGoal.build(TracePattern.create(StepPattern.required(sc, e, ac, null))));
                goals.add(TestGoal.build(TracePattern.create(StepPattern.required(sc, e, not(ac), null))));
                return goals;
            }
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="class DesisionConditionCoverage">
        class DecisionConditionCoverage
                extends AbstractCoverageCriteria {

            public DecisionConditionCoverage() {
                super("Transition based - Desision condition coverage", "A union of desision and condition coverage.");
            }

            @Override
            public Set<TestGoal> getGoals(StateMachine sm, Limits limits) {
                Set<TestGoal> testgoals = DECISION_COVERAGE.getGoals(sm, limits);
                testgoals.addAll(CONDITION_COVERAGE.getGoals(sm, limits));
                return testgoals;
            }
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="class UniqueCauseMCDC">
        class UniqueCauseMCDC
                extends AbstractCoverageCriteria {

            public UniqueCauseMCDC() {
                super("Transition based - Unique cause Modified converage desision coverage");
            }

            @Override
            public Set<TestGoal> getGoals(StateMachine sm, Limits limits) {
                Set<TestGoal> testgoals = TransitionBased.ALL_TRANSITION_PAIRS.getGoals(sm, limits);
                for (Transition t : sm.getTransitions())
                    for (Expression ac : t.getGuard().getExpression().breakdown(ComparisonExpression.class)) {
                        StateConfiguration sc = new StateConfiguration(t.getSource());
                        Set<Event> e = t.getEvents();
                        testgoals.add(createTestGoal(t, ac, sc, e));
                    }
                return testgoals;
            }

            private TestGoal createTestGoal(Transition t, Expression ac, StateConfiguration sc, Set<Event> e) {
                TestGoal ctg = TestGoal.getFalse();
                for (ValueAssignment va : y(t)) {
                    ConditionValueAssignment cva = new ConditionValueAssignment(va);
                    cva.remove(ac);
                    ConditionValueAssignment cva1 = new ConditionValueAssignment(cva).makeTrue(ac);
                    ConditionValueAssignment cva2 = new ConditionValueAssignment(cva).makeFalse(ac);
                    if (cva1.evaluate() == cva2.evaluate())
                        continue;
                    TestGoal atg1 = TestGoal.build(TracePattern.create(StepPattern.required(sc, e, cva1, null)));
                    TestGoal atg2 = TestGoal.build(TracePattern.create(StepPattern.required(sc, e, cva2, null)));
                    ctg = TestGoal.or(ctg, TestGoal.and(atg1, atg2));
                }
                return ctg;
            }
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="class MultipleConditionCoverage">
        class MultipleConditionCoverage
                extends AbstractCoverageCriteria {

            public MultipleConditionCoverage() {
                super("Transition based - multiple condition coverage",
                      "All goals are satisified if each value assignement of the truth tables of all guard is tested.");
            }

            @Override
            public Set<TestGoal> getGoals(StateMachine sm, Limits limits) {
                Set<TestGoal> testgoals = TransitionBased.ALL_TRANSITIONS.getGoals(sm, limits);
                sm.getTransitions().stream().forEach(t -> {
                    y(t).stream().forEach(va -> {
                        StateConfiguration sc = new StateConfiguration(t.getSource());
                        Set<Event> e = t.getEvents();
                        ConditionValueAssignment cva = new ConditionValueAssignment(va);
                        testgoals.add(TestGoal.build(TracePattern.create(StepPattern.required(sc, e, cva, null))));
                    });
                });
                return testgoals;
            }
        }
        //</editor-fold>

        static Set<ValueAssignment> y(Transition t) {
            return t.getGuard().getExpression().breakdown(ComparisonExpression.class).stream()
                    .map(ValueAssignment::create)
                    .collect(HashSet::new, Set::add, Set::addAll);
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public static interface DataFlowBased {

        public static final AllDefs ALL_DEFS = new AllDefs();
        public static final AllUses ALL_USES = new AllUses();
        public static final AllDefUsePaths ALL_DEF_USE_PATHS = new AllDefUsePaths();

        public static Set<CoverageCriteria> getDefault() {
            return new HashSet<>(asList(ALL_DEFS));
        }

        //<editor-fold defaultstate="collapsed" desc="class AllDefs">
        class AllDefs
                extends AbstractCoverageCriteria {

            public AllDefs() {
                super("Data flow based - All defs");
            }

            @Override
            public Set<TestGoal> getGoals(StateMachine sm, Limits limits) {
                Set<TestGoal> testgoals = new HashSet<>();
                for (Variable var : sm.getVariables()) {
                    Set<Transition> t_rd = sm.getTransitionsDefining(var);
                    Set<Transition> t_ru = sm.getTransitionsUsing(var);
                    for (Transition t_d : sm.getTransitionsUsing(var)) {
                        t_rd.remove(t_d);
                        testgoals.add(createTestGoal(t_ru, sm, t_rd, t_d));
                        t_rd.add(t_d);
                    }
                }
                return testgoals;
            }

            private TestGoal createTestGoal(Set<Transition> t_ud, StateMachine sm, Set<Transition> t_rd, Transition t_d) {
                TestGoal ctg = TestGoal.getFalse();
                for (Transition t_u : t_ud)
                    ctg = TestGoal.or(ctg, TestGoal.build(
                                      TracePattern.create(StepPattern.required(null, null, null, asList(t_d))),
                                      TracePattern.create(StepPattern.any(null, null, null, sm.not(t_rd))),
                                      TracePattern.create(StepPattern.required(null, null, null, asList(t_u)))));
                return ctg;
            }
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="class AllUses">
        class AllUses
                extends AbstractCoverageCriteria {

            public AllUses() {
                super("Data flow based - All uses");
            }

            @Override
            public Set<TestGoal> getGoals(StateMachine sm, Limits limits) {
                Set<TestGoal> testgoals = new HashSet<>();
                for (Variable var : sm.getVariables())
                    testgoals.addAll(createGoals(sm, var));
                return testgoals;
            }

            private Set<TestGoal> createGoals(StateMachine sm, Variable var) {
                Set<TestGoal> testgoals = new HashSet<>();
                Set<Transition> t_rd = sm.getTransitionsDefining(var);
                Set<Transition> transitions = sm.getTransitionsUsing(var);
                for (Transition t_d : transitions) {
                    t_rd.remove(t_d);
                    testgoals.addAll(createTestGoals(sm, transitions, t_rd, t_d));
                    t_rd.add(t_d);
                }
                return testgoals;
            }

            private Set<TestGoal> createTestGoals(StateMachine sm, Set<Transition> transitions,
                                                  Set<Transition> t_rd, Transition t_d) {
                Set<TestGoal> testgoals = new HashSet<>();
                for (Transition t_u : transitions)
                    testgoals.add(TestGoal.build(
                            TracePattern.create(StepPattern.required(null, null, null, asList(t_d))),
                            TracePattern.create(StepPattern.any(null, null, null, sm.not(t_rd))),
                            TracePattern.create(StepPattern.required(null, null, null, asList(t_u)))));
                return testgoals;
            }
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="class AllDefUsePaths">
        class AllDefUsePaths
                extends AbstractCoverageCriteria {

            public AllDefUsePaths() {
                super("Data flow based - All def use paths");
            }

            @Override
            public Set<TestGoal> getGoals(StateMachine sm, Limits limits) {
                Set<TestGoal> testgoals = new HashSet<>();
                for (Variable var : sm.getVariables())
                    for (Transition t_d : sm.getTransitionsDefining(var))
                        for (Transition t_u : sm.getTransitionsUsing(var))
                            testgoals.addAll(new PathsBetween(sm, var, t_d, t_u).getAllTestGoals());
                return testgoals;
            }

            private class PathsBetween {

                private final StateMachine sm;
                private final Variable var;
                private final Transition t_d;
                private final Transition t_u;

                private PathsBetween(StateMachine sm, Variable var, Transition t_d, Transition t_u) {
                    this.sm = sm;
                    this.var = var;
                    this.t_d = t_d;
                    this.t_u = t_u;
                }

                private Set<TestGoal> getAllTestGoals() {
                    State s = t_d.getTarget();
                    return sm.getConfigurations(sc -> sc.contains(s)).stream()
                            .map(sc -> findPaths(sc))
                            .flatMap(Collection::stream)
                            .collect(HashSet::new, Set::add, Set::addAll);
                }

                private Set<TestGoal> findPaths(StateConfiguration sc) {
                    List<Transition> transitions = new ArrayList<>();
                    transitions.add(t_u);
                    return findPaths(sc, transitions);
                }

                private Set<TestGoal> findPaths(StateConfiguration sc, List<Transition> transitions) {
                    Set<TestGoal> testgoals = new HashSet<>();
                    if (t_u.equals(getLast(transitions)))
                        testgoals.add(createGoal(transitions));
                    testgoals.addAll(findPathsHelper(sc, transitions));
                    return testgoals;
                }

                private TestGoal createGoal(List<Transition> transitions) {
                    TracePattern tp = TracePattern.create();
                    for (int i = 0; i < transitions.size() - 1; ++i)
                        tp.add(StepPattern.required(null, null, null, asList(transitions.get(i))));
                    return TestGoal.build(tp);
                }

                private Set<TestGoal> findPathsHelper(StateConfiguration sc, List<Transition> transitions) {
                    Set<TestGoal> testgoals = new HashSet<>();
                    for (State s : sc.getStates()) {
                        sc.remove(s);
                        for (Transition t : sm.getOutgoing(s)) {
                            if (t.isVariableDefined(var))
                                continue;
                            transitions.add(t);
                            sc.add(t.getTarget());
                            testgoals.addAll(findPaths(sc, transitions));
                            sc.remove(t.getTarget());
                            transitions.remove(t);
                        }
                        sc.add(s);
                    }
                    return testgoals;
                }
            }

            private static <T> T getLast(List<T> list) {
                return list.isEmpty() ? null : list.get(list.size() - 1);
            }
        }
        //</editor-fold>
    }

    public String getTitle();

    public String getDescription();

    public Set<TestGoal> getGoals(StateMachine sm, Limits limits);
}
