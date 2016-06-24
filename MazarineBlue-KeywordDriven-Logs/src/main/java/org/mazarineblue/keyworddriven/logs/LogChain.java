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
package org.mazarineblue.keyworddriven.logs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.mazarineblue.datasources.DataSource;
import org.mazarineblue.keyworddriven.InstructionLine;
import org.mazarineblue.keyworddriven.InterpreterContext;
import org.mazarineblue.keyworddriven.RunningProcessor.ProcessingType;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class LogChain
        implements Log {

    private final List<Log> logs;

    public LogChain(Log... logs) {
        this.logs = new ArrayList<>(Arrays.asList(logs));
    }

    public void add(Log log) {
        logs.add(log);
    }

    @Override
    public void info(String message) {
        for (Log log : logs)
            log.info(message);
    }

    @Override
    public void info(Exception ex) {
        for (Log log : logs)
            log.info(ex);
    }

    @Override
    public void warning(String message) {
        for (Log log : logs)
            log.warning(message);
    }

    @Override
    public void warning(Exception ex) {
        for (Log log : logs)
            log.warning(ex);
    }

    @Override
    public void error(String message) {
        for (Log log : logs)
            log.error(message);
    }

    @Override
    public void error(Throwable ex) {
        for (Log log : logs)
            log.error(ex);
    }

    @Override
    public void next(InstructionLine line, DataSource source, InterpreterContext context, ProcessingType processingType) {
        for (Log log : logs)
            log.next(line, source, context, processingType);
    }

    @Override
    public void setActualParameters(Object[] actualParameters) {
        for (Log log : logs)
            log.setActualParameters(actualParameters);
    }

    @Override
    public void done(Date endDate) {
        for (Log log : logs)
            log.done(endDate);
    }

    @Override
    public void incrementNestedInstruction(Date startDate) {
        for (Log log : logs)
            log.incrementNestedInstruction(startDate);
    }

    @Override
    public void decrementNestedInstruction(Date endDate) {
        for (Log log : logs)
            log.decrementNestedInstruction(endDate);
    }
}
