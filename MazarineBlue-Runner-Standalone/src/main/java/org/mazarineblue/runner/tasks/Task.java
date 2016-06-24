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
package org.mazarineblue.runner.tasks;

import java.util.Date;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public interface Task
        extends Comparable<Task> {

    public enum State {

        WAITING,
        RUNNING,
        DONE,
        ERROR,
    }

    public void run()
            throws TaskTargetException, InterruptedException;

    public void waitForScheduleDate()
            throws InterruptedException;

    public long getDelay();

    public void setDelay(long delay);

    public void openLog();

    public void openReport();

    public boolean isPaused();

    public void pause();

    public void resume();

    public void cancle();

    public State getState();

    public Date getScheduledDate();

    public String getSourceLocation();

    public String getSheetName();

    public boolean after(Task other);

    public boolean before(Task other);
}
