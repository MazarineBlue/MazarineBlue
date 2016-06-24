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
package org.mazarineblue.keyworddriven.util.old;

import java.util.Date;
import org.mazarineblue.datasources.DataSource;
import org.mazarineblue.keyworddriven.InstructionLine;
import org.mazarineblue.keyworddriven.InterpreterContext;
import org.mazarineblue.keyworddriven.RunningProcessor.ProcessingType;
import static org.mazarineblue.keyworddriven.RunningProcessor.ProcessingType.VALIDATED;
import org.mazarineblue.keyworddriven.logs.Log;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class TestLog
        implements Log {

    private int lines = 0, validated = 0;
    private int level = 0, depth = 0;
    private int info = 0, warning = 0, error = 0, exception = 0;

    public int getLines() {
        return lines;
    }

    public int getExecuted() {
        return lines - validated;
    }

    public int getValidated() {
        return validated;
    }

    public int getLevel() {
        return level;
    }

    public int getDepth() {
        return depth;
    }

    public int getInfo() {
        return info;
    }

    public int getWarning() {
        return warning;
    }

    public int getError() {
        return error;
    }

    @Override
    public void info(String message) {
        ++info;
    }

    @Override
    public void info(Exception ex) {
        ++info;
    }

    @Override
    public void warning(String message) {
        ++warning;
    }

    @Override
    public void warning(Exception ex) {
        ++warning;
    }

    @Override
    public void error(String message) {
        ++error;
    }

    @Override
    public void error(Throwable ex) {
        ++error;
    }

    @Override
    public void next(InstructionLine line, DataSource source, InterpreterContext context, ProcessingType processingType) {
        ++lines;
        switch (processingType) {
            case VALIDATED:
                ++validated;
        }
    }

    @Override
    public void setActualParameters(Object[] parameters) {
    }

    @Override
    public void done(Date endDate) {
    }

    @Override
    public void incrementNestedInstruction(Date startDate) {
        ++level;
        if (level > depth)
            depth = level;
    }

    @Override
    public void decrementNestedInstruction(Date endDate) {
        --level;
    }
}
