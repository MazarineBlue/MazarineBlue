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
package org.mazarineblue.subscribers.recorder;

import java.util.Objects;
import java.util.function.Predicate;
import org.mazarineblue.eventdriven.Invoker;
import org.mazarineblue.eventnotifier.Event;
import org.mazarineblue.subscribers.AbstractProcessingSubscriber;

/**
 * A {@code RecorderSubscriber} is a {@code Subscriber} that records event
 * until a stop condition is met.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class RecorderSubscriber
        extends AbstractProcessingSubscriber {

    private final Recording recording = new Recording(4);

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
        return recording.toString();
    }

    @Override
    protected void processEvent(Event event) {
        recording.add(event);
        event.setConsumed(true);
    }

    public Recording getRecording() {
        return recording;
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
