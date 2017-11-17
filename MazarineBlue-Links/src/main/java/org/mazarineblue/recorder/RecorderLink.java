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
package org.mazarineblue.recorder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.mazarineblue.eventbus.Event;
import org.mazarineblue.eventdriven.Interpreter;
import org.mazarineblue.eventdriven.Link;
import org.mazarineblue.function.Condition;
import org.mazarineblue.utililities.ObjectsUtil;

/**
 * A {@code RecorderLink} is a {@code Link} that records {@code Events} until a
 * stop condition is met.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class RecorderLink
        extends Link {

    private static final long serialVersionUID = 1L;

    private final Interpreter interpreter;
    private final Condition<Event> stopCondition;
    private final List<Event> recording = new ArrayList<>(4);

    /**
     * Constructs a {@code RecorderLink} that stops when the specified
     * stopCondition is evaluated true.
     *
     * @param interpreter   the interpreter used to remove this link from.
     * @param stopCondition the condition to evaluate with.
     */
    public RecorderLink(Interpreter interpreter, Condition<Event> stopCondition) {
        this.interpreter = interpreter;
        this.stopCondition = stopCondition;
    }

    @Override
    public String toString() {
        return "recording size = " + recording.size();
    }

    @Override
    public void eventHandler(Event event) {
        if (matchesStopCondition(event))
            stopRecording(event);
        else
            recordEvent(event);
    }

    private boolean matchesStopCondition(Event event) {
        return stopCondition.apply(event);
    }

    private void stopRecording(Event event) {
        interpreter.removeLink(this);
        event.setConsumed(true);
    }

    private void recordEvent(Event event) {
        recording.add(event);
    }

    @SuppressWarnings("unchecked")
    public List<Event> getRecording() {
        return recording.isEmpty()
                ? Collections.emptyList()
                : cloneEventList();
    }

    private List<Event> cloneEventList() {
        List<Event> list = ObjectsUtil.clone(recording);
        list.stream().forEach(e -> e.setConsumed(false));
        return list;
    }

    @Override
    public int hashCode() {
        return 7 * 47 * 47 * 47
                + 47 * 47 * Objects.hashCode(this.interpreter)
                + 47 * Objects.hashCode(this.stopCondition)
                + Objects.hashCode(this.recording);
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && getClass() == obj.getClass()
                && Objects.equals(this.interpreter, ((RecorderLink) obj).interpreter)
                && Objects.equals(this.stopCondition, ((RecorderLink) obj).stopCondition)
                && Objects.equals(this.recording, ((RecorderLink) obj).recording);
    }
}
