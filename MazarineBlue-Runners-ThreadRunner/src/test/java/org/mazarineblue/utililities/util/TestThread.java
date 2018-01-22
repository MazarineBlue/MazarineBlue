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
package org.mazarineblue.utililities.util;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.Assert.assertEquals;
import org.mazarineblue.utililities.BlockingTwoWayPipeFactory;
import org.mazarineblue.utililities.TwoWayPipe;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class TestThread
        extends Thread
        implements AutoCloseable {

    private static final String STARTED = "Started";
    private static final String STOPPED = "Stopped";
    private final int timeout;
    private final TwoWayPipe<String> pipe = new BlockingTwoWayPipeFactory<String>().createLoopedBackedPipe(16);
    private final TestRunnable target;
    private final TestUncaughtExceptionHandler handler = new TestUncaughtExceptionHandler();

    public TestThread(int timeout, TestRunnable target) {
        this.timeout = timeout;
        this.target = target;
        this.setUncaughtExceptionHandler(handler);
    }

    @Override
    public void close() {
        super.interrupt();
    }

    @Override
    @SuppressWarnings(value = "CallToThreadRun")
    public void run() {
        signal(STARTED);
        try {
            target.run();
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            handler.setCause(ex);
        } finally {
            signal(STOPPED);
        }
    }

    private void signal(String signal) {
        try {
            pipe.write(signal);
        } catch (InterruptedException ex) {
            pipe.clear();
            currentThread().interrupt();
        }
    }

    public void waitTillStarted()
            throws InterruptedException {
        waitTillStarted(0);
    }

    public void waitTillStarted(long millis)
            throws InterruptedException {
        assertEquals(STARTED, pipe.read(String.class, timeout));
        await().atMost(millis, MILLISECONDS);
    }

    public void waitTillStopped()
            throws InterruptedException {
        String read = pipe.read(String.class);
        if (read == STARTED)
            read = pipe.read(String.class);
        assertEquals(STOPPED, read);
    }

    public void waitForThrowable()
            throws InterruptedException {
        handler.waitFor(Throwable.class);
    }

    public Throwable getCause()
            throws Throwable {
        return handler.getCause();
    }
}
