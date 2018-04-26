/*
 * Copyright (c) 2017 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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
package org.mazarineblue.runners.commandline;

import java.io.File;
import static java.util.Arrays.copyOf;
import org.mazarineblue.executors.Executor;
import org.mazarineblue.executors.ExecutorBuilder;
import org.mazarineblue.executors.ExecutorFactory;
import org.mazarineblue.executors.ExecutorListener;
import org.mazarineblue.fs.FileSystem;
import org.mazarineblue.plugins.Runner;

/**
 * A {@code ConsoleRunner} is {@code Runner} that can read feeds from a
 * {@code FileSystem}.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class CommandLineRunner
        implements Runner {

    private final ExecutorFactory feedExecutorFactory;
    private final ExecutorListener output;
    private String[] args;

    /**
     * Creates a {@code ConsoleRunner}, which runs from the specified
     * {@code FileSystem}.
     *
     * @param fs the file system to load the feeds from
     */
    public CommandLineRunner(FileSystem fs) {
        this(fs, new ConsoleOutput());
    }

    private CommandLineRunner(FileSystem fs, ExecutorListener output) {
        this(output,
             ExecutorFactory.newInstance(new ExecutorBuilder().setFileSystem(fs).setOutput(output)));
    }

    CommandLineRunner(ExecutorListener output, ExecutorFactory feedExecutorFactory) {
        this.output = output;
        this.feedExecutorFactory = feedExecutorFactory;
    }

    @Override
    public void setArguments(String... args) {
        this.args = args == null ? null : copyOf(args, args.length);
    }

    @Override
    public void start() {
        if (args == null || args.length == 0)
            output.printHelp();
        else
            run(args);
    }

    private void run(String[] args) {
        boolean containsErrors = false;
        for (String file : args) {
            Executor feedExecutor = feedExecutorFactory.create();
            feedExecutor.execute(new File(file));
            containsErrors |= feedExecutor.containsErrors();
        }
        if (containsErrors)
            output.printHelp();
    }
}
