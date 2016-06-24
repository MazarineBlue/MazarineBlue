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
package org.mazarineblue.runner.swing;

import java.awt.EventQueue;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingWorker;
import org.mazarineblue.runner.tasks.Task;
import org.mazarineblue.runner.tasks.Task.State;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class TaskWorker
        extends SwingWorker {

    private final MainFrame frame;
    private final TaskPanel panel;
    private final Task task;

    TaskWorker(MainFrame frame, TaskPanel panel, Task task) {
        this.frame = frame;
        this.panel = panel;
        this.task = task;
        setPanelState();
    }

    private void setPanelState() {
        State state = task.getState();
        String text = state.name();
        panel.setStatus(text, state == State.ERROR);
    }

    @Override
    protected Void doInBackground()
            throws Exception {
        task.waitForScheduleDate();
        EventQueue.invokeLater(() -> {
            try {
                while (task.getState() == State.WAITING)
                    Thread.sleep(100);
            } catch (InterruptedException ex) {
            }
            frame.increaseBusy();
            panel.setBusy(true);
            setPanelState();
        });
        task.run();
        return null;
    }

    @Override
    protected void done() {
        frame.decreaseBusy();
        panel.setBusy(false);
        setPanelState();
        try {
            Object obj = get();
        } catch (InterruptedException ex) {
        } catch (ExecutionException ex) {
            panel.setException(ex);
        }
    }
}
