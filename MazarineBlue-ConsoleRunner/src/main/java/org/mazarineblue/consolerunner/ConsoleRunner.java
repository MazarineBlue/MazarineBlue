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
package org.mazarineblue.consolerunner;

import java.io.File;
import java.util.Arrays;
import org.mazarineblue.executors.FeedExecutor;
import org.mazarineblue.executors.FeedExecutorBuilder;
import org.mazarineblue.executors.FeedExecutorFactory;
import org.mazarineblue.executors.FeedExecutorOutput;
import org.mazarineblue.fs.DiskFileSystem;
import org.mazarineblue.fs.FileSystem;
import org.mazarineblue.plugins.Runner;

public class ConsoleRunner
        implements Runner {

    private final FeedExecutorFactory feedExecutorFactory;
    private final FeedExecutorOutput output;
    private String[] args;

    public static void main(String... args) {
        new ConsoleRunner().execute(args);
    }

    public ConsoleRunner() {
        this(new DiskFileSystem(), new ConsoleOutput());
    }

    public ConsoleRunner(FileSystem fs, FeedExecutorOutput output) {
        this(output, FeedExecutorFactory.getDefaultInstance(new FeedExecutorBuilder().setFileSystem(fs).setOutput(output)));
    }

    public ConsoleRunner(FeedExecutorOutput output, FeedExecutorFactory feedExecutorFactory) {
        this.output = output;
        this.feedExecutorFactory = feedExecutorFactory;
    }

    @Override
    public void setArguments(String... args) {
        this.args = args == null ? null : Arrays.copyOf(args, args.length);
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
            FeedExecutor feedExecutor = feedExecutorFactory.create();
            feedExecutor.execute(new File(file));
            containsErrors |= feedExecutor.containsErrors();
        }
        if (containsErrors)
            output.printHelp();
    }
}
