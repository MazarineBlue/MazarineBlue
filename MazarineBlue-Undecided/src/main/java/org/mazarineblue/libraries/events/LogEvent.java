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
package org.mazarineblue.libraries.events;

import java.util.Objects;
import java.util.logging.Level;
import org.mazarineblue.keyworddriven.events.KeywordDrivenEvent;
import org.mazarineblue.utililities.SerializableObjectWrapper;

public class LogEvent
        extends KeywordDrivenEvent {

    private static final long serialVersionUID = 1L;

    private final SerializableObjectWrapper msg;
    private final Level level;

    public LogEvent(Object msg) {
        this(msg, Level.FINEST);
    }

    public LogEvent(Object msg, Level level) {
        this.msg = new SerializableObjectWrapper(msg);
        this.level = level;
    }

    @Override
    public int hashCode() {
        return 413 + 59 * Objects.hashCode(this.msg) + Objects.hashCode(this.level);
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && getClass() == obj.getClass()
                && Objects.equals(this.msg, ((LogEvent) obj).msg)
                && Objects.equals(this.level, ((LogEvent) obj).level);
    }
}
