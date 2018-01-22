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
package org.mazarineblue.runners.threadrunner;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import static java.lang.Thread.sleep;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mazarineblue.eventnotifier.Event;
import org.mazarineblue.eventnotifier.events.TestEvent;
import org.mazarineblue.executors.ExecutorBuilder;
import org.mazarineblue.executors.ExecutorFactory;
import org.mazarineblue.executors.util.TestFeedExecutorFactory;
import org.mazarineblue.fs.MemoryFileSystem;
import org.mazarineblue.runners.threadrunner.events.ByeEvent;
import org.mazarineblue.runners.threadrunner.events.KillEvent;
import org.mazarineblue.runners.threadrunner.exceptions.TimeoutException;
import org.mazarineblue.runners.threadrunner.util.WaitingFeedExecutor;
import org.mazarineblue.utililities.BlockingTwoWayPipeFactory;
import org.mazarineblue.utililities.TwoWayPipe;
import org.mazarineblue.utililities.exceptions.InterruptedRuntimeException;
import org.mazarineblue.utililities.util.TestRunnable;
import org.mazarineblue.utililities.util.TestThread;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
@RunWith(HierarchicalContextRunner.class)
public class ThreadRunnerTest {

    private static final int TIMEOUT = 1000;
    private static final int DOZE = 100;

    private final int capacity = 4;
    private TwoWayPipe<Event> pipe;
    private ThreadServiceRunner runner;

    @Before
    public void setup() {
        pipe = new BlockingTwoWayPipeFactory<Event>().createPipe(capacity);
    }

    @After
    public void teardown() {
        if (runner != null)
            runner.interrupt();
        pipe = null;
        runner = null;
    }

    private void fillPipe()
            throws InterruptedException {
        for (int i = 0; i < capacity; ++i)
            pipe.write(new TestEvent());
    }

    @SuppressWarnings("PublicInnerClass")
    public class FullPipe {

        @Before
        public void setup()
                throws InterruptedException {
            runner = new ThreadServiceRunner(pipe.redirect(), null, 2 * DOZE);
            fillPipe();
        }

        @Test(timeout = TIMEOUT, expected = InterruptedRuntimeException.class)
        public void bye_InterrupedDuringWrite_RunnerWasInterruped()
                throws Throwable {
            TestThread thread = startThreadAndCall(runner::bye);
            thread.interrupt();
            thread.waitForThrowable();
            throw thread.getCause();
        }

        @Test(timeout = TIMEOUT, expected = InterruptedRuntimeException.class)
        public void kill_InterrupedDuringWrite_RunnerWasInterruped()
                throws Throwable {
            TestThread thread = startThreadAndCall(runner::kill);
            thread.interrupt();
            thread.waitForThrowable();
            throw thread.getCause();
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class RunnerInWaitForEverMode {

        @Before
        public void setup()
                throws InterruptedException {
            startRunnerInWaitForEverMode();
        }

        private void startRunnerInWaitForEverMode()
                throws InterruptedException {
            ExecutorFactory factory = new TestFeedExecutorFactory(new WaitingFeedExecutor());
            runner = new ThreadServiceRunner(pipe.redirect(), factory, DOZE);
            runner.start();
        }

        @Test(timeout = TIMEOUT, expected = InterruptedRuntimeException.class)
        public void bye_InterrupedDuringWait_RunnerWasInterruped()
                throws Throwable {
            TestThread thread = startThreadAndCall(runner::bye);
            thread.interrupt();
            thread.waitForThrowable();
            throw thread.getCause();
        }

        @Test(timeout = TIMEOUT, expected = InterruptedRuntimeException.class)
        public void kill_InterrupedDuringWait_RunnerWasInterruped()
                throws Throwable {
            TestThread thread = startThreadAndCall(runner::kill);
            thread.interrupt();
            thread.waitForThrowable();
            throw thread.getCause();
        }

        @Test(timeout = TIMEOUT, expected = TimeoutException.class)
        public void kill_InterrupedDuringWait_TimeoutReached()
                throws Throwable {
            fillPipe();
            TestThread thread = startThreadAndCall(runner::kill);
            thread.waitForThrowable();
            throw thread.getCause();
        }
    }

    private TestThread startThreadAndCall(TestRunnable target)
            throws InterruptedException {
        TestThread thread = new TestThread(DOZE, target);
        thread.start();
        thread.waitTillStarted();
        return thread;
    }

    @SuppressWarnings("PublicInnerClass")
    public class HappyFlow {

        @Before
        public void setup()
                throws InterruptedException {
            ExecutorFactory factory = ExecutorFactory.newInstance(new ExecutorBuilder().setFileSystem(new MemoryFileSystem()));
            runner = new ThreadServiceRunner(pipe.redirect(), factory, DOZE);
            pipe.write(new TestEvent());
            runner.start();
            waitUntilReadQueueIsSizeOfAtLeast(1);
        }

        @Test(timeout = TIMEOUT)
        @SuppressWarnings("unchecked")
        public void bye_RunnerStops()
                throws InterruptedException {
            runner.bye();
            waitUntilReadQueueIsSizeOfAtLeast(2);
            assertFalse(runner.isAlive());
            assertEventsRecieved(TestEvent.class, ByeEvent.class);
        }

        @Test(timeout = TIMEOUT)
        @SuppressWarnings("unchecked")
        public void kill_RunnerStops()
                throws InterruptedException {
            runner.kill();
            waitUntilReadQueueIsSizeOfAtLeast(2);
            assertFalse(runner.isAlive());
            assertEventsRecieved(TestEvent.class, KillEvent.class);
        }

        @SuppressWarnings("SleepWhileInLoop")
        private void waitUntilReadQueueIsSizeOfAtLeast(int amount)
                throws InterruptedException {
            while (pipe.readQueueSize() < amount)
                sleep(DOZE);
        }

        @SuppressWarnings("unchecked")
        private void assertEventsRecieved(Class<? extends Event>... types)
                throws InterruptedException {
            for (Class<? extends Event> type : types)
                assertEquals(type, pipe.read().getClass());
        }
    }
}
