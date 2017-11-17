/*
 * Copyright (c) 2016 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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
package org.mazarineblue.logs;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Objects;
import org.mazarineblue.eventbus.Event;
import org.mazarineblue.eventbus.EventHandler;
import org.mazarineblue.eventdriven.Link;
import org.mazarineblue.utililities.Immutable;
import org.mazarineblue.utililities.Timestamp;

/**
 * A {@code PrintLink} is a {@code Link} that convert every {@link Event} to a
 * {@code String} using the {@code toString()} method.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
@Immutable
public class PrintLink
        extends Link {

    private static final long serialVersionUID = 1L;

    private final PrintStream output;
    private final Timestamp timestamp;

    /**
     * Constructs a {@code PrintLink} using the specified {@code OutputStream}
     * and {@code Timestamp}.
     *
     * @param stream    the stream to write to.
     * @param timestamp the source for the timestamps.
     */
    public PrintLink(OutputStream stream, Timestamp timestamp) {
        output = stream instanceof PrintStream ? (PrintStream) stream : new PrintStream(stream);
        this.timestamp = timestamp;
    }

    @Override
    @EventHandler
    public void eventHandler(Event event) {
        String now = timestamp.getTimestamp();
        String name = event.getClass().getSimpleName();
        String msg = event.message();
        output.println(now + " | " + name + ": " + msg);
    }

    @Override
    public int hashCode() {
        return 7 * 53
                + Objects.hashCode(this.timestamp);
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && getClass() == obj.getClass()
                && Objects.equals(this.timestamp, ((PrintLink) obj).timestamp);
    }
}
