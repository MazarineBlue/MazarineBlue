/*
 * Copyright (c) 2012-2014 Alex de Kruijff
 * Copyright (c) 2014-2015 Specialisterren
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
package org.mazarineblue.test.datadriven3.registry;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mazarineblue.datasources.DataSource;
import org.mazarineblue.test.datadriven3.exceptions.ExecutionException;
import org.mazarineblue.test.datadriven3.listeners.ReportListenerThread;
import org.mazarineblue.test.datadriven3.util.WaitNotifier;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.nl>}
 */
class ProfileWorker extends Thread {

    static private final Thread.UncaughtExceptionHandler handler = new Thread.UncaughtExceptionHandler() {

        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            thread.interrupt();
            try {
                if (thread instanceof ProfileWorker)
                    uncaughtException((ProfileWorker) thread, ex);
                else
                    throw ex;
            } catch (Throwable ex1) {
                Logger.getLogger(ProfileWorker.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }

        private void uncaughtException(ProfileWorker worker, Throwable throwable)
                throws Throwable {
            try {
                worker.exception = new ExecutionException(throwable);
                worker.waitNotifier.doNotify();
            } catch (InterruptedException ex) {
                Logger.getLogger(ProfileWorker.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    };

    private final BlockingQueue<Profile> queue = new LinkedBlockingQueue<>();
    private final DataSource source;
    private final ReportListenerThread reportListener;
    private final WaitNotifier waitNotifier;
    private boolean waithing = true;
    private ExecutionException exception;

    ProfileWorker(DataSource source, ReportListenerThread reportListener, WaitNotifier waitNotifier) {
        this.source = source;
        this.reportListener = reportListener;
        this.waitNotifier = waitNotifier;
        setUncaughtExceptionHandler(handler);
    }

    @Override
    public String toString() {
        return "ProfileWorker{" + "id=" + waitNotifier.getId() + ", waithing=" + waithing + ", exception=" + exception + '}';
    }

    public ExecutionException getException() {
        return exception;
    }

    void queue(Profile profile) throws InterruptedException {
        queue.put(profile);
    }

    @Override
    public synchronized void start() {
        waithing = true;
        super.start();
    }

    @Override
    public void run() {
        do {
            try {
                while (waithing) {
                    Profile profile = queue.take();
                    if (profile instanceof PoisionProfile)
                        return;
                    testProfile(profile, source);
                    if (queue.size() == 0)
                        waitNotifier.doNotify();
                }
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                waithing = false;
            }
        } while (queue.size() > 0);
    }

    private void testProfile(Profile profile, DataSource source) throws InterruptedException {
        String mark = source == null ? "" : source.reset();
        try {
            do {
                if (source != null)
                    source.next();
                Boolean result = profile.invokeTestMethod(source);
                if (result == null)
                    reportListener.setSkipped(profile);
                else if (result.equals(Boolean.TRUE))
                    reportListener.setPassed(profile);
                else
                    reportListener.setFailed(profile);
            } while (source != null && source.hasNext());
        } catch (InvocationTargetException ex) {
            profile.error(ex);
        } finally {
            if (source != null)
                source.reset(mark);
        }
    }
}
