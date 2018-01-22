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
package org.mazarineblue.libraries.test.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import static java.util.stream.Collectors.toList;
import org.mazarineblue.eventdriven.Invoker;
import org.mazarineblue.eventdriven.feeds.MemoryFeed;
import org.mazarineblue.executors.Executor;
import org.mazarineblue.executors.events.CreateFeedExecutorEvent;
import org.mazarineblue.libraries.test.exceptions.RuntimeInterruptedException;
import org.mazarineblue.libraries.test.model.listeners.TestListener;
import org.mazarineblue.libraries.test.model.suites.Suite;
import org.mazarineblue.libraries.test.model.tests.Test;
import org.mazarineblue.variablestore.VariableStore;
import org.mazarineblue.variablestore.events.GetGlobalVariableStoreEvent;
import org.mazarineblue.variablestore.events.SetGlobalVariableStoreEvent;

class DefaultTestSytem
        implements TestSystem {

    private final Invoker invoker;
    private final Suite globalSuite = Suite.newInstance(null, "global");
    private final Map<Key, Suite> suites = new HashMap<>();
    private final Map<Key, Test> tests = new HashMap<>();
    private Semaphore pool = new Semaphore(1);
    private TestListener listener = null;

    DefaultTestSytem(Invoker invoker) {
        this.invoker = invoker;
    }

    @Override
    public String toString() {
        return "Test count=" + tests.size();
    }

    @Override
    public void setMaximumJobs(int maximumJobs) {
        pool = new Semaphore(maximumJobs);
    }

    @Override
    public void setTestListener(TestListener listener) {
        this.listener = listener;
    }

    @Override
    public Suite getGlobalSuite() {
        return globalSuite;
    }

    @Override
    public boolean contains(Suite suite) {
        return suites.containsKey(suite.getKey());
    }

    @Override
    public Suite getSuite(Key suite) {
        return suites.get(suite);
    }

    @Override
    public void add(Suite suite) {
        suites.put(suite.getKey(), suite);
    }

    @Override
    public boolean contains(Test test) {
        return tests.containsKey(test.getKey());
    }

    @Override
    public void add(Test test) {
        tests.put(test.getKey(), test);
    }

    @Override
    public void run() {
        try {
            List<TestThread> list = tests.values().stream()
                    .map(test -> new TestThread(createFeedExecutor(invoker), test, pool))
                    .peek(thread -> thread.setTestListener(listener))
                    .collect(toList());
            list.stream().peek(TestThread::waitForLock).forEach(Thread::start);
            list.stream().forEach(TestThread::waitForJobCompletion);
        } catch (RuntimeInterruptedException ex) {
            Thread.interrupted();
        }
    }

    private Executor createFeedExecutor(Invoker invoker) {
        Executor executor = newFeedExecutor(invoker);
        init(executor, invoker);
        return executor;
    }

    private Executor newFeedExecutor(Invoker invoker) {
        CreateFeedExecutorEvent<Executor> e = new CreateFeedExecutorEvent<>();
        invoker.publish(e);
        return e.getResult();
    }

    private void init(Executor executor, Invoker invoker) {
        VariableStore store = getGlobalVariableStore(invoker);
        executor.execute(new MemoryFeed(new SetGlobalVariableStoreEvent(store)));
    }

    private VariableStore getGlobalVariableStore(Invoker invoker) {
        GetGlobalVariableStoreEvent e = new GetGlobalVariableStoreEvent();
        invoker.publish(e);
        return e.getStore();
    }
}
