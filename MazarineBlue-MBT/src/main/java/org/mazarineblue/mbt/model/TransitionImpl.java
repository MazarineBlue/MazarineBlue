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
package org.mazarineblue.mbt.model;

import static java.util.Arrays.asList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.mazarineblue.mbt.expressions.Expression;
import static org.mazarineblue.mbt.expressions.Expression.buildExpression;

class TransitionImpl
        implements Transition {

    private final State source;
    private final State target;
    private Set<Event> events;
    private Guard guard = new DummyGuard();
    private Effect effect = new DummyEffect();

    TransitionImpl(State source, State target) {
        this.source = source;
        this.target = target;
    }

    @Override
    public String toString() {
        String s = "(" + source + " -> " + target + ")";
        String t = toString(events) + guard + effect;
        return t.trim().isEmpty() ? s : s + ": " + t;
    }

    private String toString(Collection<Event> events) {
        if (events == null || events.isEmpty())
            return "";
        StringBuilder builder = new StringBuilder();
        Iterator<Event> it = events.iterator();
        builder.append(it.next());
        while (it.hasNext())
            builder.append(", ").append(it.next());
        return builder.toString();
    }

    @Override
    public State getSource() {
        return source;
    }

    @Override
    public boolean isSource(State s) {
        return source.equals(s);
    }

    @Override
    public State getTarget() {
        return target;
    }

    @Override
    public boolean isTarget(State s) {
        return target.equals(s);
    }

    @Override
    public Transition setEvents(Event... events) {
        this.events = new HashSet(asList(events));
        return this;
    }

    @Override
    public Set<Event> getEvents() {
        return events;
    }

    @Override
    public Transition setGuard(String expression) {
        guard = new GuardImpl(buildExpression(expression));
        return this;
    }

    @Override
    public Guard getGuard() {
        return guard;
    }

    @Override
    public Transition setEffect(String title, Collection<Expression> expressions) {
        this.effect = new EffectImpl(title, expressions);
        return this;
    }

    @Override
    public Effect getEffect() {
        return effect;
    }

    @Override
    public boolean isSatified(ValueAssignment va) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int compareTo(Transition other) {
        int c = source.compareTo(other.getSource());
        if (c == 0)
            c = target.compareTo(other.getTarget());
        if (c == 0 && guard != null)
            c = guard.compareTo(other.getGuard());
        if (c == 0)
            c = effect.compareTo(other.getEffect());
        return c;
    }
}
