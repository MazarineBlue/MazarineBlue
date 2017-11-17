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
package org.mazarineblue.mbt;

import java.util.ArrayList;
import static java.util.Arrays.asList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mazarineblue.mbt.criteria.CoverageCriteria;
import org.mazarineblue.mbt.criteria.Limits;
import static org.mazarineblue.mbt.criteria.Limits.unlimited;
import org.mazarineblue.mbt.criteria.step.StepPattern;
import org.mazarineblue.mbt.criteria.trace.TracePattern;
import org.mazarineblue.mbt.expressions.ComparisonExpression;
import org.mazarineblue.mbt.expressions.Expression;
import static org.mazarineblue.mbt.expressions.Expression.buildExpression;
import org.mazarineblue.mbt.expressions.False;
import org.mazarineblue.mbt.expressions.True;
import org.mazarineblue.mbt.goals.TestGoal;
import org.mazarineblue.mbt.model.Event;
import org.mazarineblue.mbt.model.State;
import org.mazarineblue.mbt.model.StateConfiguration;
import org.mazarineblue.mbt.model.StateMachine;
import org.mazarineblue.mbt.model.Transition;
import org.mazarineblue.mbt.tests.TestCase;
import org.mazarineblue.mbt.tests.TestSuiteBuilder;
import org.mazarineblue.mbt.tests.TraceExtension;

public class XTest {

    private StateMachine sm;
    private State start;
    private State idle;
    private State inserted;
    private State evaluated;
    private State notRecognized;
    private State recognized;
    private State fitsInPackage;
    private State tooSmall;
    private State tooBig;

    private Transition startIdle;
    private Transition idleInserted;
    private Transition insertedEvaluated;
    private Transition evaluatedRecognized;
    private Transition evaluatedNotRecognized;
    private Transition t06;
    private Transition recognizedFitsInpackage;
    private Transition recognizedTooSmall;
    private Transition recognizedTooBig;
    private Transition notRecognizedIdle;
    private Transition fitsInpackageIdle;
    private Transition tooSmallIdle;
    private Transition tooBigIdle;
    private Transition t14;
    private Transition t15;
    private Transition t16;
    private Transition t17;
    private Transition t18;
    private Transition t19;

    @Before
    public void setup() {
        sm = createModelOfSortingMachine();
    }

    private StateMachine createModelOfSortingMachine() {
        StateMachine sm = StateMachine.createDefault();
        start = sm.createState("Start");
        idle = sm.createState("Idle");
        inserted = sm.createState("Object inserted");
        evaluated = sm.createState("Object evaluated");
        notRecognized = sm.createState("Object not recognized");
        recognized = sm.createState("Object recognized");
        fitsInPackage = sm.createState("Object fits in package");
        tooSmall = sm.createState("Object is too small");
        tooBig = sm.createState("Object is too big");

        sm.setIntialStates(idle);
        startIdle = sm.createTransition(start, idle);
        idleInserted = sm.createTransition(idle, inserted)
                .setEvents(Event.create("detectItemEvent(object)", DetectItem.class))
                .setEffect("detectItem(object)", new ArrayList<Expression>() {{
                           add(buildExpression("$height = ($object.height * 2) + 2"));
                           add(buildExpression("$width = ($object.width + 2) * 2"));
                       }});
        insertedEvaluated = sm.createTransition(inserted, evaluated).setEffect("recognized()", new ArrayList<Expression>() {{
                                                           add(buildExpression("$height < 20 -> $recognized = true"));
                                                           add(buildExpression("$height >= 20 -> $recognized = false"));
                                                       }});
        evaluatedRecognized = sm.createTransition(evaluated, recognized).setGuard("$recognized == true");
        evaluatedNotRecognized = sm.createTransition(evaluated, notRecognized).setGuard("$recognized == false");
        recognizedFitsInpackage = sm.createTransition(recognized, fitsInPackage).setGuard("$width >= 20 && $width <= 30");
        recognizedTooSmall = sm.createTransition(recognized, tooSmall).setGuard("$width < 20");
        recognizedTooBig = sm.createTransition(recognized, tooBig).setGuard("$width > 30");
        notRecognizedIdle = sm.createTransition(notRecognized, idle).setEvents(Event.create("storePackageEvent", StorePackage.class));
        fitsInpackageIdle = sm.createTransition(fitsInPackage, idle).setEvents(Event.create("storePackageEvent", StorePackage.class));
        tooSmallIdle = sm.createTransition(tooSmall, idle).setEvents(Event.create("storePackageEvent", StorePackage.class));
        tooBigIdle = sm.createTransition(tooBig, idle).setEvents(Event.create("storePackageEvent", StorePackage.class));
        return sm;
    }

    @After
    public void teardown() {
        sm = null;
    }

    private static Set<TestGoal> createStateGoals(StateMachine sm) {
        return sm.getStates().stream()
                .map(s -> new StateConfiguration(s))
                .map(sc -> StepPattern.required(sc, null, null, null))
                .map(sp -> TracePattern.create(sp))
                .map(tp -> TestGoal.build(tp))
                .collect(HashSet::new, Set::add, Set::addAll);
    }

    private static Set<TestGoal> createTransitionGoals(StateMachine sm) {
        return sm.getTransitions().stream()
                .map(t -> StepPattern.required(null, null, null, asList(t)))
                .map(sp -> TracePattern.create(sp))
                .map(tp -> TestGoal.build(tp))
                .collect(HashSet::new, Set::add, Set::addAll);
    }

    private static TestGoal createTestGoal(Transition... transitions) {
        StepPattern[] arr = new StepPattern[transitions.length];
        for (int i = 0; i < transitions.length; ++i)
            arr[i] = StepPattern.required(null, null, null, asList(transitions[i]));
        return TestGoal.build(TracePattern.create(arr));
    }

    @Test
    public void allStates() {
        Set<TestGoal> expected = createStateGoals(sm);
        assertGoals(expected, CoverageCriteria.TransitionBased.ALL_STATES.getGoals(sm, unlimited()));
    }

    @Test
    public void allConfigurations() {
        Set<TestGoal> expected = createStateGoals(sm);
        assertGoals(expected, CoverageCriteria.TransitionBased.ALL_CONFIGURATIONS.getGoals(sm, unlimited()));
    }

    @Test
    public void allTransitions() {
        Set<TestGoal> expected = createStateGoals(sm);
        expected.addAll(createTransitionGoals(sm));
        Set<TestGoal> actual = CoverageCriteria.TransitionBased.ALL_TRANSITIONS.getGoals(sm, unlimited());
        assertGoals(expected, actual);
    }

    @Test
    public void allTransitionPairs() {
        Set<TestGoal> expected = createStateGoals(sm);
        expected.addAll(createTransitionGoals(sm));
        expected.addAll(createTransitionPairGoals(sm));
        Set<TestGoal> actual = CoverageCriteria.TransitionBased.ALL_TRANSITION_PAIRS.getGoals(sm, unlimited());
        assertGoals(expected, actual);
    }

    //<editor-fold defaultstate="collapsed" desc="Helper methods for allTransitionpairs()">
    private static Set<TestGoal> createTransitionPairGoals(StateMachine sm) {
        Set<TestGoal> goals = new HashSet<>();
        for (Transition t1 : sm.getTransitions())
            for (Transition t2 : sm.getOutgoing(t1.getTarget()))
                goals.add(TestGoal.build(
                        TracePattern.create(StepPattern.required(null, null, null, asList(t1))),
                        TracePattern.create(StepPattern.required(null, null, null, asList(t2)))
                ));
        return goals;
    }
    //</editor-fold>

    @Test(timeout = 1000)
    public void allPaths() {
        Set<TestGoal> expected = createStateGoals(sm);
        expected.addAll(getAllExpectedPaths());
        Set<TestGoal> actual = CoverageCriteria.TransitionBased.ALL_PATHS.getGoals(sm, Limits.maxDepth(5));
        assertGoals(expected, actual);
    }

    //<editor-fold defaultstate="collapsed" desc="Helper methods for allPaths">
    private Set<TestGoal> getAllExpectedPaths() {
        Set<TestGoal> set = new TreeSet<>();
        set.add(createTestGoal(idleInserted));
        set.add(createTestGoal(idleInserted, insertedEvaluated));
        set.add(createTestGoal(idleInserted, insertedEvaluated, evaluatedNotRecognized));
        set.add(createTestGoal(idleInserted, insertedEvaluated, evaluatedRecognized));
        set.add(createTestGoal(idleInserted, insertedEvaluated, evaluatedNotRecognized, notRecognizedIdle));
        set.add(createTestGoal(idleInserted, insertedEvaluated, evaluatedRecognized, recognizedFitsInpackage));
        set.add(createTestGoal(idleInserted, insertedEvaluated, evaluatedRecognized, recognizedTooSmall));
        set.add(createTestGoal(idleInserted, insertedEvaluated, evaluatedRecognized, recognizedTooBig));
        set.add(createTestGoal(idleInserted, insertedEvaluated, evaluatedNotRecognized, notRecognizedIdle, idleInserted));
        set.add(createTestGoal(idleInserted, insertedEvaluated, evaluatedRecognized, recognizedFitsInpackage, fitsInpackageIdle));
        set.add(createTestGoal(idleInserted, insertedEvaluated, evaluatedRecognized, recognizedTooSmall, tooSmallIdle));
        set.add(createTestGoal(idleInserted, insertedEvaluated, evaluatedRecognized, recognizedTooBig, tooBigIdle));
        return set;
    }
    //</editor-fold>

    @Test
    public void decisionCoverage() {
        Set<TestGoal> expected = createStateGoals(sm);
        expected.addAll(createTransitionGoals(sm));
        expected.addAll(createDecisionCoverage(sm));
        Set<TestGoal> actual = CoverageCriteria.ControlFlowBased.DECISION_COVERAGE.getGoals(sm, unlimited());
        assertGoals(expected, actual);
    }

    //<editor-fold defaultstate="collapsed" desc="Helper methods for decisionCoverage()">
    private static Set<TestGoal> createDecisionCoverage(StateMachine sm) {
        return sm.getTransitions().stream()
                .map(t -> createDecisionCoverage(new StateConfiguration(t.getSource()),
                                                 t.getEvents(),
                                                 createDecisionCoverageExpressions(t)))
                .flatMap(Collection::stream)
                .collect(HashSet::new, Set::add, Set::addAll);
    }

    private static Set<TestGoal> createDecisionCoverage(StateConfiguration sc, Set<Event> e, List<Expression> list) {
        return list.stream()
                .map(expression -> StepPattern.required(sc, e, expression, null))
                .map(sp -> TracePattern.create(sp))
                .map(tp -> TestGoal.build(tp))
                .collect(HashSet::new, Set::add, Set::addAll);
    }

    private static List<Expression> createDecisionCoverageExpressions(Transition t) {
        Expression ex = t.getGuard().getExpression();
        return ex instanceof True || ex instanceof False
                ? asList((Expression) null)
                : asList(ex.simplify(), ex.not().simplify());
    }
    //</editor-fold>

    /*
    Assume we want to test the following code extract:

if ( (A || B) && C )
where A, B and C represent atomic boolean expressions (i.e. not divisible in other boolean sub-expressions).

In order to ensure Condition coverage criteria for this example, A, B and C should be evaluated at least one time "true" and one time "false" during tests, which would be the case with the 2 following tests:
    A = true  / B = true  / C = true
    A = false / B = false / C = false

In order to ensure Decision coverage criteria, the condition ( (A ou B) et C ) should also be evaluated at least one time to "true" and one time to "false". Indeed, in our previous test cases:
    A = true  / B = true  / C = true   --->  la décision est évaluée à "true"
    A = false / B = false / C = false  --->  la décision est évaluée à "false"
and Decision coverage is also realized.
     */
    @Test
    public void conditionCoverage() {
        Set<TestGoal> expected = new HashSet<>();
        for (Transition t : sm.getTransitions()) {
            Expression expression = t.getGuard().getExpression();
        }
        Set<TestGoal> actual = CoverageCriteria.ControlFlowBased.CONDITION_COVERAGE.getGoals(sm, unlimited());
        assertGoals(expected, actual);
    }

    //<editor-fold defaultstate="collapsed" desc="Helper methods for conditionCoverage()">
    private static Set<TestGoal> createConditionCoverage(StateMachine sm) {
        return sm.getTransitions().stream()
                .map(t -> createConditionCoverage(new StateConfiguration(t.getSource()), t.getEvents(), t))
                .flatMap(Collection::stream)
                .collect(HashSet::new, Set::add, Set::addAll);
    }

    private static Set<TestGoal> createConditionCoverage(StateConfiguration sc, Set<Event> events, Transition t) {
        Set<ComparisonExpression> breakdown = t.getGuard().getExpression().breakdown(ComparisonExpression.class);
        if (breakdown.isEmpty()) {
            TestGoal goal = TestGoal.build(TracePattern.create(StepPattern.required(sc, events, null, null)));
            return new TreeSet<>(asList(goal));
        }
        Set<TestGoal> goals = new HashSet<>(2 * breakdown.size());
        for (Expression cva : breakdown) {
            goals.add(TestGoal.build(TracePattern.create(StepPattern.required(sc, events, cva, null))));
            goals.add(TestGoal.build(TracePattern.create(StepPattern.required(sc, events, cva.not(), null))));
        }
        return goals;
    }
    //</editor-fold>

    @Test
    public void desisionConditionCoverage() {
        Set<TestGoal> expected = createStateGoals(sm);
        expected.addAll(createTransitionGoals(sm));
        expected.addAll(createDecisionCoverage(sm));
        Set<TestGoal> actual = CoverageCriteria.ControlFlowBased.DESISION_CONDITION_COVERAGE.getGoals(sm, unlimited());
        assertGoals(expected, actual);
    }

    @Test
    @Ignore
    public void mcdc() {
        Set<TestGoal> goals = CoverageCriteria.ControlFlowBased.UNIQUE_CAUSE_MCDC.getGoals(sm, unlimited());
    }

    @Test
    @Ignore
    public void multipleConditionCoverage() {
        Set<TestGoal> goals = CoverageCriteria.ControlFlowBased.MULTIPLE_CONDITION_COVERAGE.getGoals(sm, unlimited());
    }

    @Test
    @Ignore
    public void allDefs() {
        Set<TestGoal> goals = CoverageCriteria.DataFlowBased.ALL_DEFS.getGoals(sm, unlimited());
    }

    @Test
    @Ignore
    public void allUses() {
        Set<TestGoal> goals = CoverageCriteria.DataFlowBased.ALL_USES.getGoals(sm, unlimited());
    }

    @Test
    @Ignore
    public void allDefUsePaths() {
        Set<TestGoal> goals = CoverageCriteria.DataFlowBased.ALL_DEF_USE_PATHS.getGoals(sm, unlimited());
    }

    @Test
    @Ignore
    public void test() {
        List<TestGoal> goals = new ArrayList<>(CoverageCriteria.TransitionBased.ALL_STATES.getGoals(sm, unlimited()));

        TestSuiteBuilder builder = new TestSuiteBuilder(sm);
        TestGoal goal = goals.get(7);
        Set<TraceExtension> set = builder.extendsPathForTestGoal(goal);
        TraceExtension te = getTraceExtention(set, 2);
        TestCase createTestCase = builder.createTestCase(te);
    }

    private TraceExtension getTraceExtention(Set<TraceExtension> tes, int n) {
        Iterator<TraceExtension> it = tes.iterator();
        for (int i = 1; i < n; ++i)
            it.next();
        return it.next();
    }

    private void assertGoals(Set<TestGoal> expected, Set<TestGoal> actual) {
        for (TestGoal goal : expected)
            if (!actual.contains(goal))
                fail("TestGoal <" + goal + "> not found in test goals.");
        assertEquals(expected.size(), actual.size());
    }
}
