/*
 * Copyright (c) Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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
package org.mazarineblue.subscribers;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;
import org.mazarineblue.eventdriven.ClosingProcessorEvent;
import org.mazarineblue.eventdriven.Invoker;
import org.mazarineblue.eventnotifier.Event;
import static org.mazarineblue.eventnotifier.Event.matchesAny;
import org.mazarineblue.eventnotifier.Subscriber;
import org.mazarineblue.utilities.exceptions.UnknownIssueException;

/**
 * A {@code AbstractProcessingSubscriber} is a {@code Subscriber} that
 * processes event until a stop condition is met.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public abstract class AbstractProcessingSubscriber
        implements Subscriber<Event> {

    private final Invoker invoker;
    private final Predicate<Event> stopCondition;
    private final ThrowExecptionRules throwExecptionRules = new ThrowExecptionRules();

    /**
     * Constructs a {@code AbstractProcessingSubscriber} that removes it self
     * when the specified stopCondition is evaluates to true.
     *
     * @param invoker       used to remove this link from.
     * @param stopCondition stop recording event when this condition evaluates true.
     */
    protected AbstractProcessingSubscriber(Invoker invoker, Predicate<Event> stopCondition) {
        this.invoker = invoker;
        this.stopCondition = stopCondition.or(matchesAny(ClosingProcessorEvent.class));
    }

    public void addThrowExceptionRule(Predicate<Event> condition, Supplier<RuntimeException> supplier) {
        throwExecptionRules.add(new ThrowExecptionRule(condition, supplier));
    }

    @Override
    public void eventHandler(Event event) {
        if (throwExecptionRules.test(event)) {
            stopProcessingEvents();
            throw throwExecptionRules.get(event);
        } else if (matchesStopCondition(event))
            stopProcessingEvents();
        else
            processEvent(event);
    }

    private static class ThrowExecptionRules
            implements Predicate<Event>  {

        private final List<ThrowExecptionRule> rules = new ArrayList<>(4);

        @Override
        public boolean test(Event event) {
            return rules.stream().anyMatch(rule -> rule.test(event));
        }

        private void add(ThrowExecptionRule throwExecptionRule) {
            rules.add(throwExecptionRule);
        }

        private RuntimeException get(Event event) {
            return rules.stream()
                    .filter(rule -> rule.test(event))
                    .findAny()
                    .orElseThrow(() -> new UnknownIssueException())
                    .get();
        }
    }

    private static class ThrowExecptionRule
            implements Predicate<Event>, Supplier<RuntimeException> {

        private final Predicate<Event> condition;
        private final Supplier<RuntimeException> supplier;

        private ThrowExecptionRule(Predicate<Event> condition, Supplier<RuntimeException> supplier) {
            this.condition = condition;
            this.supplier = supplier;
        }

        @Override
        public boolean test(Event e) {
            return condition.test(e);
        }
        
        @Override
        public RuntimeException get() {
            throw supplier.get();
        }
    }

    private boolean matchesStopCondition(Event event) {
        return stopCondition.test(event);
    }

    private void stopProcessingEvents() {
        invoker.processor().removeLink(this);
    }

    protected abstract void processEvent(Event event);

    @Override
    public int hashCode() {
        return 13 * 47 * 47 * 47
                + 47 * 47 * Objects.hashCode(this.invoker)
                + 47 * Objects.hashCode(this.stopCondition);
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && getClass() == obj.getClass()
                && Objects.equals(this.invoker, ((AbstractProcessingSubscriber) obj).invoker)
                && Objects.equals(this.stopCondition, ((AbstractProcessingSubscriber) obj).stopCondition);
    }
}
