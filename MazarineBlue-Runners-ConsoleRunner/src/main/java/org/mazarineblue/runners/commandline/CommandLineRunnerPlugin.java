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

import org.mazarineblue.fs.DiskFileSystem;
import org.mazarineblue.fs.FileSystem;
import org.mazarineblue.plugins.Runner;
import org.mazarineblue.plugins.RunnerPlugin;
import org.openide.util.lookup.ServiceProvider;

/**
 * A {@code ConsoleRunnerPlugin} is a {@code RunnerPlugin}, which creates a
 * {@link CommandLineRunner} dynamically to read feeds from the local disk.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
@ServiceProvider(service = RunnerPlugin.class)
public class CommandLineRunnerPlugin
        implements RunnerPlugin {

    public static final String APP = "app";
    public static final String NAME = "cli";
    private final FileSystem fs;

    /**
     * Creates a runner, which uses the local disk to read feeds from.
     */
    public CommandLineRunnerPlugin() {
        fs = new DiskFileSystem();
    }

    CommandLineRunnerPlugin(FileSystem fs) {
        this.fs = fs;
    }

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public String description() {
        return "execute the feeds provided in the argument list";
    }

    @Override
    public Runner createRunner() {
        return new CommandLineRunner(fs);
    }
}
