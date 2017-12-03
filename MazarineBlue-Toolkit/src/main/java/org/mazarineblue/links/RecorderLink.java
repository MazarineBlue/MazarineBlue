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
package org.mazarineblue.links;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import org.mazarineblue.eventbus.Event;
import org.mazarineblue.eventdriven.Invoker;
import org.mazarineblue.eventdriven.Link;

/**
 * A {@code RecorderLink} is a {@code Link} that records event until a stop
 * condition is met.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class RecorderLink
        extends Link {

    private final Invoker invoker;
    private final Predicate<Event> stopCondition;
    private final List<Event> recording = new ArrayList<>(4);

    /**
     * Constructs a {@code RecorderLink} that removes it self when the specified
     * stopCondition is evaluates to true.
     *
     * @param invoker       used to remove this link from.
     * @param stopCondition stop recording event when this condition evaluates true.
     */
    public RecorderLink(Invoker invoker, Predicate<Event> stopCondition) {
        this.invoker = invoker;
        this.stopCondition = stopCondition;
    }

    @Override
    public String toString() {
        return "size = " + recording.size();
    }

    @Override
    public void eventHandler(Event event) {
        if (matchesStopCondition(event))
            stopRecording();
        else
            recordEvent(event);
    }

    private boolean matchesStopCondition(Event event) {
        return stopCondition.test(event);
    }

    private void stopRecording() {
        invoker.interpreter().removeLink(this);
    }

    private void recordEvent(Event event) {
        recording.add(event);
        event.setConsumed(true);
    }

    @SuppressWarnings("unchecked")
    public Collection<Event> getRecording() {
        return recording.isEmpty()
                ? Collections.emptyList()
                : Event.clone(recording);
    }

    @Override
    public int hashCode() {
        return 7 * 47 * 47 * 47
                + 47 * 47 * Objects.hashCode(this.invoker)
                + 47 * Objects.hashCode(this.stopCondition)
                + Objects.hashCode(this.recording);
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && getClass() == obj.getClass()
                && Objects.equals(this.invoker, ((RecorderLink) obj).invoker)
                && Objects.equals(this.stopCondition, ((RecorderLink) obj).stopCondition)
                && Objects.equals(this.recording, ((RecorderLink) obj).recording);
    }
}
