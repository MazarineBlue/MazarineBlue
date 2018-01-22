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
package org.mazarineblue.executors.events;

import java.util.Objects;
import org.mazarineblue.eventdriven.InvokerEvent;
import org.mazarineblue.utilities.SerializableClonable;

/**
 * A {@code CreateFeedExecutorEvent} is a event that request the creation
 * of an object.
 *
 * @param <T> The object requested to be created.
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class CreateFeedExecutorEvent<T>
        extends InvokerEvent {

    private static final long serialVersionUID = 1L;

    private transient T obj;

    @Override
    public String toString() {
        return "result=" + obj.toString();
    }

    @Override
    public String responce() {
        return "result=" + obj.toString();
    }

    /**
     * Returns the result of the request.
     *
     * @return the created object.
     */
    public T getResult() {
        return obj;
    }

    /**
     * Sets the result of the processed request.
     *
     * @param obj the result
     */
    public void setResult(T obj) {
        this.obj = obj;
    }

    @Override
    public int hashCode() {
        return 23 * 7 + Objects.hashCode(this.obj);
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj != null && getClass() == obj.getClass()
                && Objects.equals(this.obj, ((CreateFeedExecutorEvent<?>) obj).obj);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <E extends SerializableClonable> void copyTransient(E other) {
        super.copyTransient(other);
        obj = (T) ((CreateFeedExecutorEvent<?>) other).obj;
    }
}
