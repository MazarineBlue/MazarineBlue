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
import org.mazarineblue.utililities.ArgumentList;

/**
 * A {@code CallFitnesseEvent} is a {@code FitnesseEvent} that instructs a
 * {@link FitnesseSubscriber} to call the specified method belonging to the
 * specified fixture using the specified arguments.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class CallFitnesseEvent
        extends FitnesseEvent {

    private final String instance;
    private final String method;
    private final ArgumentList arguments;
    private Object result;

    /**
     * Constructs a {@code CallFitnesseEvent} as an instruction for
     * {@link FitnesseSubscriber} to call the specified method belonging to the
     * specified fixture using the specified arguments.
     *
     * @param instance  the instance that hold a reference to the method to call.
     * @param method    the method to call.
     * @param arguments the argument to pass to the method.
     * @see CreateFitnesseEvent
     */
    public CallFitnesseEvent(String instance, String method, Object... arguments) {
        this.instance = instance;
        this.method = method;
        this.arguments = new ArgumentList(arguments);
    }

    @Override
    public String toString() {
        return "" + instance + ", " + method + ", [" + arguments + "]";
    }

    public String getInstance() {
        return instance;
    }

    public String getMethod() {
        return method;
    }

    public Object[] getArguments() {
        return arguments.getArguments();
    }

    public CallFitnesseEvent setResult(Object result) {
        this.result = result;
        return this;
    }

    public Object getResult() {
        return result == null ? null : result;
    }

    @Override
    public int hashCode() {
        return 7 * 89 * 89 * 89
                + 89 * 89 * Objects.hashCode(this.instance)
                + 89 * Objects.hashCode(this.method)
                + Objects.hashCode(this.arguments);
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && getClass() == obj.getClass()
                && Objects.equals(this.instance, ((CallFitnesseEvent) obj).instance)
                && Objects.equals(this.method, ((CallFitnesseEvent) obj).method)
                && Objects.equals(this.arguments, ((CallFitnesseEvent) obj).arguments);
    }
}
