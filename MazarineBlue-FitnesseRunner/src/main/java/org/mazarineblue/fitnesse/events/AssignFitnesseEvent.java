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
package org.mazarineblue.fitnesse.events;

import java.util.Objects;
import org.mazarineblue.fitnesse.engineplugin.FitnesseSubscriber;

/**
 * An {@code AssignFitnesseEvent} is a {@code FitnesseEvent} that instructs a
 * {@link FitnesseSubscriber} to assign the specified value to the specified
 * symbol.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class AssignFitnesseEvent
        extends FitnesseEvent {

    private final String symbol;
    private final Object value;

    /**
     * Constructs an {@code AssignFitnesseEvent} as an instruction for
     * {@link FitnesseSubscriber} to assign the specified value to the
     * specified symbol.
     *
     * @param symbol the symbol to assign the value to.
     * @param value  the value to assign to the symbol.
     */
    public AssignFitnesseEvent(String symbol, Object value) {
        this.symbol = symbol;
        this.value = value;
    }

    public String getSymbol() {
        return symbol;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        return 7 * 47 * 47
                + 47 * Objects.hashCode(this.symbol)
                + Objects.hashCode(this.value);
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && getClass() == obj.getClass()
                && Objects.equals(this.symbol, ((AssignFitnesseEvent) obj).symbol)
                && Objects.equals(this.value, ((AssignFitnesseEvent) obj).value);
    }

    @Override
    public String toString() {
        return "" + symbol + "=" + value;
    }
}
