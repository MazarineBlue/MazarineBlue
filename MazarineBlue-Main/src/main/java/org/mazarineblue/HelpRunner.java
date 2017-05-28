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
package org.mazarineblue;

import org.mazarineblue.plugins.PluginLoader;
import org.mazarineblue.plugins.Runner;
import org.mazarineblue.plugins.RunnerPlugin;

public class HelpRunner
        implements Runner {

    @Override
    public void setArguments(String... args) {
        // Argumments are not required for this class.
    }

    @Override
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public void start() {
        System.out.println("usage: <command> [<args>]");
        System.out.println("Commands:");

        int n = PluginLoader.getInstance().getPlugins(RunnerPlugin.class).stream()
                .map(plugin -> plugin.name().length())
                .max((left, right) -> left - right)
                .orElse(0);
        int m = 80 - n - 3;

        PluginLoader.getInstance().getPlugins(RunnerPlugin.class).stream()
                .sorted((left, right) -> left.name().compareTo(right.name()))
                .forEach(plugin -> System.out.println(plugin.name()
                        + spaces(n - plugin.name().length() + 3)
                        + limit(plugin.description(), m)));
    }

    private static String spaces(int n) {
        StringBuilder builder = new StringBuilder(n);
        for (int i = 0; i < n; ++i)
            builder.append(' ');
        return builder.toString();
    }

    private static String limit(String str, int n) {
        return str.length() < n ? str : str.substring(0, n - 3) + "...";
    }
}
