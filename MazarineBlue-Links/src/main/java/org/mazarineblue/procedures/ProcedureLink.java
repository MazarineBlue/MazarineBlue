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
package org.mazarineblue.procedures;

import java.util.HashMap;
import java.util.Objects;
import org.mazarineblue.eventbus.Event;
import org.mazarineblue.eventbus.EventHandler;
import org.mazarineblue.eventdriven.Interpreter;
import org.mazarineblue.eventdriven.Link;
import org.mazarineblue.procedures.events.CallProcedureEvent;
import org.mazarineblue.procedures.events.StartRecordingProcedureEvent;
import org.mazarineblue.procedures.events.StopRecordingProcedureEvent;
import org.mazarineblue.procedures.exceptions.ProcedureNameTakenException;
import org.mazarineblue.procedures.exceptions.ProcedureNotFoundException;
import org.mazarineblue.procedures.exceptions.StartRecordingProcedureFirstException;
import org.mazarineblue.procedures.exceptions.StopRecordingPreviousProcedureFirstException;

/**
 * A {@code ProcedureLink} is a {@code Link} that can record a
 * {@link Procedure} and execute it.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 * @see CallProcedureEvent
 * @see StartRecordingProcedureEvent
 * @see StopRecordingProcedureEvent
 */
public class ProcedureLink
        extends Link {

    private static final long serialVersionUID = 1L;

    private Procedure procedureRecording;
    private final ProcedureRegistry registry;
    private final HashMap<Class<?>, Class<?>> ignoreList = new HashMap<>(4);

    /**
     * Constructs a {@code ProcedureLink} that uses the specified
     * {@code Interpreter} to publish procedures to.
     *
     * @param interpreter the publish destination when procedures are called.
     * @param ignoreTypes the types to ignore when recording a procedure.
     */
    public ProcedureLink(Interpreter interpreter, Class<?>... ignoreTypes) {
        registry = new ProcedureRegistry(interpreter);
        for (Class<?> type : ignoreTypes)
            ignoreList.put(type, type);
    }

    @Override
    public String toString() {
        return "ProcedureLink{" + "procedureRecording=" + procedureRecording + ", registry=" + registry + ", ignoreList=" + ignoreList + '}';
    }

    /**
     * Event handlers are not meant to be called directly, instead publish an
     * event to an {@link Interpreter}; please see the specified event for more
     * information about this event handler.
     *
     * @param event the event this {@code EventHandler} processes.
     * @see StartRecordingProcedureEvent
     */
    @EventHandler
    public void eventHandler(StartRecordingProcedureEvent event) {
        if (procedureRecording != null)
            throw new StopRecordingPreviousProcedureFirstException(procedureRecording.getName());
        if (registry.contains(event.getName()))
            throw new ProcedureNameTakenException(event.getName());
        procedureRecording = new Procedure(event.getName());
        registry.put(event.getName(), procedureRecording);
        event.setConsumed(true);
    }

    /**
     * Event handlers are not meant to be called directly, instead publish an
     * event to an {@link Interpreter}; please see the specified event for more
     * information about this event handler.
     *
     * @param event the event this {@code EventHandler} processes.
     * @see StopRecordingProcedureEvent
     */
    @EventHandler
    public void eventHandler(StopRecordingProcedureEvent event) {
        if (procedureRecording == null)
            throw new StartRecordingProcedureFirstException();
        procedureRecording = null;
        event.setConsumed(true);
    }

    @Override
    protected void uncatchedEventHandler(Event e) {
        if (procedureRecording == null || isUnrecorableEvent(e.getClass()))
            return;
        procedureRecording.add(e);
        e.setConsumed(true);
    }

    @SuppressWarnings("unchecked")
    private boolean isUnrecorableEvent(Class<?> type) {
        Class<?> superclass = type.getSuperclass();
        return Event.class.isAssignableFrom(superclass) && isUnrecorableEvent(superclass) ? true
                : ignoreList.containsKey(type);
    }

    /**
     * Event handlers are not meant to be called directly, instead publish an
     * event to an {@link Interpreter}; please see the specified event for more
     * information about this event handler.
     *
     * @param event the event this {@code EventHandler} processes.
     * @see CallProcedureEvent
     */
    @EventHandler
    public void eventHandler(CallProcedureEvent event) {
        if (!registry.contains(event.getName()))
            throw new ProcedureNotFoundException(event.getName());
        registry.call(event.getName());
        event.setConsumed(true);
    }

    @Override
    public int hashCode() {
        return 3 * 53 * 53 * 53
                + 53 * 53 * Objects.hashCode(this.procedureRecording)
                + 53 * Objects.hashCode(this.registry)
                + Objects.hashCode(this.ignoreList.keySet());
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && getClass() == obj.getClass()
                && Objects.equals(this.procedureRecording, ((ProcedureLink) obj).procedureRecording)
                && Objects.equals(this.registry, ((ProcedureLink) obj).registry)
                && Objects.equals(this.ignoreList.keySet(), ((ProcedureLink) obj).ignoreList.keySet());
    }
}
