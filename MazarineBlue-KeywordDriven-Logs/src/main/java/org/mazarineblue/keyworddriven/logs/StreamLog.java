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

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Date;
import org.mazarineblue.datasources.DataSource;
import org.mazarineblue.keyworddriven.InstructionLine;
import org.mazarineblue.keyworddriven.InterpreterContext;
import org.mazarineblue.keyworddriven.RunningProcessor.ProcessingType;
import org.mazarineblue.keyworddriven.exceptions.ConsumableException;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class StreamLog
        implements Log {

    private final PrintStream output;
    private String prefix = "";

    public StreamLog(OutputStream output) {
        this.output = new PrintStream(output);
    }

    public StreamLog(PrintStream output) {
        this.output = output;
    }

    @Override
    public void next(InstructionLine line, DataSource source, InterpreterContext context, ProcessingType processingType) {
        String namespace = line.getNamespace();
        String keyword = line.getKeyword();
        Object[] parameters = line.getParameters();

        output.print(prefix);
        output.print("Instruction: ");
        output.print(namespace);
        output.print('.');
        output.print(keyword);
        for (Object param : parameters) {
            output.print(" | ");
            output.print(param);
        }
        output.println();
    }

    @Override
    public void setActualParameters(Object[] actualParameters) {
        output.println("Actual paramters: ");
        boolean first = true;
        for (Object param : actualParameters) {
            if (first)
                first = false;
            else
                output.print(" | ");
            output.print(param);
        }
    }

    @Override
    public void done(Date endDate) {
        output.println();
    }

    @Override
    public void incrementNestedInstruction(Date startDate) {
        prefix += "  ";
    }

    @Override
    public void decrementNestedInstruction(Date endDate) {
        prefix = prefix.length() <= 2 ? "" : prefix.substring(2);
    }

    @Override
    public void info(Exception ex) {
        if (ex instanceof ConsumableException) {
            ConsumableException iex = (ConsumableException) ex;
            if (iex.isConsumed()) {
                info(ex.getMessage());
                return;
            }
        }
        output.print(prefix);
        output.print("INFO: ");
        ex.printStackTrace(output);
    }

    @Override
    public void info(String message) {
        output.print(prefix);
        output.print("INFO: ");
        output.println(message);
    }

    @Override
    public void warning(Exception ex) {
        if (ex instanceof ConsumableException) {
            ConsumableException iex = (ConsumableException) ex;
            if (iex.isConsumed()) {
                warning(ex.getMessage());
                return;
            }
        }
        output.print(prefix);
        output.print("WARNING: ");
        ex.printStackTrace(output);
    }

    @Override
    public void warning(String message) {
        output.print(prefix);
        output.print("WARNING: ");
        output.println(message);
    }

    @Override
    public void error(Throwable ex) {
        if (ex instanceof ConsumableException) {
            ConsumableException iex = (ConsumableException) ex;
            if (iex.isConsumed()) {
                error(ex.getMessage());
                return;
            }
        }
        output.print(prefix);
        output.print("ERROR: ");
        ex.printStackTrace(output);
    }

    @Override
    public void error(String message) {
        output.print(prefix);
        output.print("ERROR: ");
        output.println(message);
    }
}
