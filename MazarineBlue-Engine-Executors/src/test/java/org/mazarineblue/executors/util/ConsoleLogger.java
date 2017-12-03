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
package org.mazarineblue.executors.util;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static java.lang.System.clearProperty;
import static java.lang.System.setProperty;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public final class ConsoleLogger {

    private final PrintStream backup;
    private final ByteArrayOutputStream stream;
    private final Handler[] backupHandlers;

    public static void setHandlers(Handler... handlers) {
        clearHandlers();
        addHandlers(handlers);
    }

    public static void clearHandlers()
            throws SecurityException {
        Logger logger = Logger.getLogger("");
        for (Handler h : logger.getHandlers())
            logger.removeHandler(h);
    }

    public static void addHandlers(Handler... handlers)
            throws SecurityException {
        Logger logger = Logger.getLogger("");
        for (Handler h : handlers)
            logger.addHandler(h);
    }

    public ConsoleLogger() {
        this.backup = System.err;
        stream = new ByteArrayOutputStream();
        System.setErr(new PrintStream(stream));

        setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s%n");
        backupHandlers = Logger.getLogger("").getHandlers();
        setHandlers(new ConsoleHandler());
    }

    public void restore() {
        System.setErr(backup);
        clearProperty("java.util.logging.SimpleFormatter.format");
        setHandlers(backupHandlers);
    }

    @Override
    public String toString() {
        return stream.toString();
    }
}
