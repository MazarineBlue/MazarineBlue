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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.mazarineblue.datasources.DataSource;
import org.mazarineblue.test.datadriven3.exceptions.ExecutionException;
import org.mazarineblue.test.datadriven3.listeners.ReportListener;
import org.mazarineblue.test.datadriven3.listeners.ReportListenerThread;
import org.mazarineblue.test.datadriven3.util.WaitNotifier;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.nl>}
 */
public class ExecutionManager {

    private final DataSource[] sources;
    private final ParallelType parallelType;
    private final ReportListener reportListener;

    public ExecutionManager(DataSource[] sources, ParallelType type, ReportListener reportListener) {
        this.sources = sources;
        this.parallelType = type;
        this.reportListener = reportListener;
    }

    public void process(List<TestProfile> profiles) throws ExecutionException {
        ReportListenerThread reportListenerThread = new ReportListenerThread(reportListener);
        reportListenerThread.start();
        WaitNotifier waitNotifier = new WaitNotifier();
        ProfileWorker[] workers = startWorkers(sources, reportListenerThread, waitNotifier);
        try {
            queueJobs(workers, profiles, waitNotifier);
            endWorkers(workers);
            waitForWorkers(workers);
            reportListenerThread.end();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            for (ProfileWorker worker : workers)
                worker.interrupt();
        }
    }

    private ProfileWorker[] startWorkers(DataSource[] source, ReportListenerThread reportListener, WaitNotifier waitNotifier) {
        ProfileWorker[] worker = new ProfileWorker[sources.length];
        for (int i = 0; i < worker.length; ++i) {
            worker[i] = new ProfileWorker(source[i], reportListener, waitNotifier);
            worker[i].start();
        }
        return worker;
    }

    private void queueJobs(ProfileWorker[] worker, Collection<TestProfile> list, WaitNotifier waitNotifier)
            throws InterruptedException, ExecutionException {
        Collection<TestProfile> todo = new ArrayList<>(list);
        int workerIndex = 0;
        while (todo.isEmpty() == false) {
            Iterator<TestProfile> it = todo.iterator();
            boolean deadlock = true;

            while (it.hasNext()) {
                TestProfile profile = it.next();
                if (profile.getState().equals(TestProfile.State.UNSET) == false)
                    it.remove();
                else if (profile.mustSkip()) {
                    profile.setState(TestProfile.State.SKIP);
                    it.remove();
                } else if (profile.canRun()) {
                    worker[workerIndex].queue(profile);
                    if (++workerIndex >= worker.length)
                        workerIndex = 0;
                    it.remove();
                    deadlock = false;
                }
            }

            if (deadlock)
                break;
            waitNotifier.doWait();
            checkWorkersForErrors(worker);
        }
    }

    private void checkWorkersForErrors(ProfileWorker[] workers) throws ExecutionException {
        for (ProfileWorker worker : workers) {
            ExecutionException ex = worker.getException();
            if (ex == null)
                continue;
            throw ex;
        }
    }

    private void endWorkers(ProfileWorker[] worker) throws InterruptedException {
        for (int i = 0; i < worker.length; ++i)
            worker[i].queue(new PoisionProfile());
    }

    private void waitForWorkers(ProfileWorker[] worker) throws InterruptedException {
        for (int i = 0; i < worker.length; ++i)
            worker[i].join();
    }
}
