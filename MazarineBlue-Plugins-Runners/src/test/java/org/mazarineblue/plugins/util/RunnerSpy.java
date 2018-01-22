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
package org.mazarineblue.plugins.util;

import static java.util.Arrays.copyOf;
import org.mazarineblue.plugins.Runner;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class RunnerSpy
        implements Runner {

    private String[] arguments;
    private boolean started = false;

    @Override
    public void setArguments(String... args) {
        this.arguments = args;
    }

    public String[] getArguments() {
        return copyOf(arguments, arguments.length);
    }

    @Override
    public void start() {
        started = true;
    }

    public boolean isStarted() {
        return started;
    }
}
