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
package org.mazarineblue.procedures.events;

import java.util.Objects;
import org.mazarineblue.eventbus.events.AbstractEvent;

/**
 * A {@code ProcedureEvent} is the base {@link Event} of all events in the
 * procedures component.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public abstract class ProcedureEvent
        extends AbstractEvent {

    private static final long serialVersionUID = 1L;

    private final String name;

    ProcedureEvent(String name) {
        this.name = name;
    }

    ProcedureEvent() {
        name = null;
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        return 5 * 59
                + Objects.hashCode(this.name);
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && getClass() == obj.getClass() && Objects.equals(this.name, ((ProcedureEvent) obj).name);
    }
}
