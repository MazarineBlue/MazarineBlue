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
package org.mazarineblue.subscribers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import org.mazarineblue.eventdriven.Invoker;
import org.mazarineblue.eventnotifier.Event;

/**
 * A {@code RecorderSubscriber} is a {@code Subscriber} that records event
 * until a stop condition is met.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class RecorderSubscriber
        extends AbstractProcessingSubscriber {

    private final List<Event> recording = new ArrayList<>(4);

    /**
     * Constructs a {@code RecorderSubscriber} that removes it self when the
     * specified stopCondition is evaluates to true.
     *
     * @param invoker       used to remove this link from.
     * @param stopCondition stop recording event when this condition evaluates true.
     */
    public RecorderSubscriber(Invoker invoker, Predicate<Event> stopCondition) {
        super(invoker, stopCondition);
    }

    @Override
    public String toString() {
        return "size = " + recording.size();
    }

    @Override
    protected void processEvent(Event event) {
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
        return 17 * 59 * 59
                + 59 * Objects.hashCode(this.recording)
                + super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && getClass() == obj.getClass()
                && Objects.equals(this.recording, ((RecorderSubscriber) obj).recording)
                && super.equals(obj);
    }
}
