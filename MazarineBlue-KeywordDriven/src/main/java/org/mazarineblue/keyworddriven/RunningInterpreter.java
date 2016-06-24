/*
 * Copyright (c) 2015 Alex de Kruijff
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
package org.mazarineblue.keyworddriven;

import java.util.Date;
import org.mazarineblue.datasources.DataSource;
import org.mazarineblue.keyworddriven.feeds.Feed;
import org.mazarineblue.keyworddriven.logs.Log;

/**
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public interface RunningInterpreter
        extends Interpreter {

    public static enum State {

        WAITING,
        RUNNING,
        PAUSED,
        CANCELED,
    };

    /**
     * Facilitates the nested execution of a specified feed and reporting to
     * the specified log.
     *
     * @param feed holding all the instruction to be executed
     * @param log the reporting instance
     * @param context
     */
    public void executeNested(Feed feed, Log log, InterpreterContext context);

    /**
     * Facilitates the nested validation of a specified feed and reporting to
     * the specified log.
     *
     * @param feed holding all the instruction to be validated
     * @param log the reporting instance
     * @param context
     */
    public void validateNested(Feed feed, Log log, InterpreterContext context);

    public void setSource(DataSource externalSource);

    public State getState();

    public void pause();

    public void resume();

    public void cancle();

    public void setSleep(long delay);

    public Date getStartDate();
}
