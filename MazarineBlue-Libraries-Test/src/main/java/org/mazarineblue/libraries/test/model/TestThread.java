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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;
import org.mazarineblue.eventdriven.feeds.MemoryFeed;
import org.mazarineblue.executors.Executor;
import org.mazarineblue.keyworddriven.events.AddLibraryEvent;
import org.mazarineblue.libraries.test.RuntimeTestLibrary;
import org.mazarineblue.libraries.test.events.ExecuteSetupEvent;
import org.mazarineblue.libraries.test.events.ExecuteTeardownEvent;
import org.mazarineblue.libraries.test.events.ExecuteTestEvent;
import org.mazarineblue.libraries.test.exceptions.RuntimeInterruptedException;
import org.mazarineblue.libraries.test.model.listeners.TestListener;
import org.mazarineblue.libraries.test.model.tests.Test;

class TestThread
        extends Thread {

    private final Executor executor;
    private final Test test;
    private final Semaphore lock;
    private TestListener listener = TestListener.getDummy();

    TestThread(Executor executor, Test test, Semaphore lock) {
        this.executor = executor;
        this.test = test;
        this.lock = lock;
    }

    public void setTestListener(TestListener listener) {
        if (listener != null)
            this.listener = listener;
    }

    void waitForLock() {
        try {
            lock.acquire();
        } catch (InterruptedException ex) {
            throw new RuntimeInterruptedException(ex);
        }
    }

    @Override
    public void run() {
        try {
            listener.testStarted(test);
            test.setTestListener(listener);
            executor.setPublisherListener(new TestPublisherListener(listener, test));
            executor.execute(createFeed(test));
        } catch(RuntimeException ex) {
            listener.exceptionThrown(test, ex);
        } finally {
            listener.testEnded(test);
            lock.release();
        }
    }

    private MemoryFeed createFeed(Test test) {
        MemoryFeed feed = new MemoryFeed(new AddLibraryEvent(new RuntimeTestLibrary(listener, test)));
        test.getSuites().stream().forEach(s -> feed.add(new ExecuteSetupEvent(s)));
        feed.add(new ExecuteTestEvent(test));
        reverse(test.getSuites()).stream().forEach(s -> feed.add(new ExecuteTeardownEvent(s)));
        return feed;
    }

    private <T> List<T> reverse(Collection<T> collection) {
        List<T> list = new ArrayList<>(collection);
        Collections.reverse(list);
        return list;
    }

    void waitForJobCompletion() {
        try {
            join();
        } catch (InterruptedException ex) {
            throw new RuntimeInterruptedException(ex);
        }
    }
}
