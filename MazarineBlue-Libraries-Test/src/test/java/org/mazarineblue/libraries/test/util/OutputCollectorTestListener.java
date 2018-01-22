/*
 * Copyright (c) 2017 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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
package org.mazarineblue.libraries.test.util;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import java.util.function.Predicate;
import org.mazarineblue.eventnotifier.Event;
import org.mazarineblue.libraries.test.model.listeners.TestListener;
import org.mazarineblue.libraries.test.model.tests.Test;

public class OutputCollectorTestListener
        implements TestListener {

    private static final int EXPECTED_OUTPUT_SIZE_PER_TEST = 4096;

    private final Map<Test, StringBuilder> builders = new HashMap<>(8);
    private final Deque<Boolean> stack = new ArrayDeque<>();
    private final Predicate<Event> spyCondition;
    private int nested = 0;

    public OutputCollectorTestListener() {
        this(t -> true);
    }

    public OutputCollectorTestListener(Predicate<Event> spyCondition) {
        this.spyCondition = spyCondition;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(16384);
        TreeSet<Test> set = new TreeSet<>(Test.naturalOrder());
        set.addAll(builders.keySet());
        set.stream()
                .forEach(t -> builder.append(getBuilder(t)));
        return builder.toString();
    }

    @Override
    public void testStarted(Test test) {
        printMessage(test, "Test start: suite=" + test.suite().name() + ", name=" + test.name());
        ++nested;
    }

    @Override
    public void testEnded(Test test) {
        --nested;
        printMessage(test, "Test end: " + test);
        printMessage(test, "================================================================================");
    }

    @Override
    public void startPublishingEvent(Test test, Event event) {
        stack.push(spyCondition.test(event));
        if (!stack.peek())
            return;

        String name = event.getClass().getName();
        printMessage(test, "Event start: " + name + " message: " + event.message());
        ++nested;
    }

    @Override
    public void endPublishedEvent(Test test, Event event) {
        if (!stack.pop())
            return;

        --nested;
        String name = event.getClass().getName();
        printMessage(test, "Event end: " + name + " responce: " + event.responce());
    }

    @Override
    public void error(Test test, String message) {
        if (!stack.peek())
            return;
        printMessage(test, "Error: " + message);
    }

    @Override
    public void exceptionThrown(Test test, RuntimeException ex) {
        if (!stack.peek())
            return;
        printMessage(test, "Exception: " + ex.getMessage());
    }

    private void printMessage(Test test, String message) {
        StringBuilder builder = getBuilder(test);
        appendHeader(builder);
        builder.append(message);
        builder.append(System.lineSeparator());
    }

    private StringBuilder getBuilder(Test test) {
        if (builders.containsKey(test))
            return builders.get(test);
        StringBuilder builder = new StringBuilder(EXPECTED_OUTPUT_SIZE_PER_TEST);
        builders.put(test, builder);
        return builder;
    }

    private void appendHeader(StringBuilder builder) {
        for (int i = 0; i < nested; ++i)
            builder.append("| ");
    }
}
