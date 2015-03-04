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
package org.mazarineblue.test.datadriven3.listeners;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.mazarineblue.test.datadriven3.registry.Profile;
import org.mazarineblue.test.datadriven3.util.WaitNotifier;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.nl>}
 */
public class ReportListenerThread extends Thread {

    private final BlockingQueue<ReportElement> queue = new LinkedBlockingQueue<>();
    private final ReportListener reportListener;
    private final WaitNotifier waitNotifier = new WaitNotifier();
    private boolean end;

    public ReportListenerThread(ReportListener reportListener) {
        this.reportListener = reportListener;
    }

    @Override
    public void run() {
        try {
            end = false;
            while (end == false)
                consume(queue.take());
            waitNotifier.doNotify();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    private void consume(ReportElement e) {
        switch (e.getCommand()) {
            case END:
                end = true;
                return;
            case PASS:
                reportListener.setPassed(e.getProfile());
                return;
            case FAIL:
                reportListener.setFailed(e.getProfile());
                return;
            case SKIP:
                reportListener.setSkipped(e.getProfile());
            case UNSET:
            default:
        }
    }

    public void setPassed(Profile profile) throws InterruptedException {
        queue.put(new ReportElement(ReportElement.Command.PASS, profile));
    }

    public void setFailed(Profile profile) throws InterruptedException {
        queue.put(new ReportElement(ReportElement.Command.FAIL, profile));
    }

    public void setSkipped(Profile profile) throws InterruptedException {
        queue.put(new ReportElement(ReportElement.Command.SKIP, profile));
    }

    public void end() throws InterruptedException {
        queue.put(new ReportElement(ReportElement.Command.END, null));
        waitNotifier.doWait();
    }
}
