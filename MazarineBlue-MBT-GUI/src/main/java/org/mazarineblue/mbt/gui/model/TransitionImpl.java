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
package org.mazarineblue.mbt.gui.model;

import java.util.ArrayList;
import static java.util.Arrays.asList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import static org.mazarineblue.mbt.gui.StringConstants.BUSINESS_VALUE_MAX;
import static org.mazarineblue.mbt.gui.StringConstants.BUSINESS_VALUE_MIN;
import org.mazarineblue.mbt.gui.exceptions.BusinessValueOutOfRangeException;
import org.mazarineblue.mbt.gui.exceptions.DestinationStateRequiredException;
import org.mazarineblue.mbt.gui.exceptions.IncompatibleViewsException;
import org.mazarineblue.mbt.gui.exceptions.SourceStateRequiredException;
import org.mazarineblue.mbt.gui.verifiers.StatesShareViewChecker;

class TransitionImpl
        extends ModelElementImpl<Transition>
        implements Transition {

    private static final long serialVersionUID = 1L;

    private String guard;
    private int businessValue;
    private List<State> sources;
    private State destination;

    TransitionImpl(String name) {
        super(name);
    }

    @Override
    public void verify() {
        if (sources == null)
            throw new SourceStateRequiredException();
        if (destination == null)
            throw new DestinationStateRequiredException();
        if (!StatesShareViewChecker.doStatePairsShareAView(destination, () -> sources))
            throw new IncompatibleViewsException(sources, destination);
        // @TODO add verification implementation here
    }

    @Override
    public Transition copy(Transition other, StateConvertor convertor) {
        super.copy(other);
        guard = other.getGuard();
        businessValue = other.getBusinessValue();
        sources = convertor.convert(other.getSources());
        destination = convertor.convert(other.getDestination());
        return this;
    }

    @Override
    public boolean containsSourceWithView(String view) {
        return sources.stream().noneMatch(s -> !s.containsView(view));
    }

    @Override
    public boolean containsDestinationWithView(String view) {
        return destination.containsView(view);
    }

    @Override
    public String getGuard() {
        return guard;
    }

    @Override
    public Transition setGuard(String guard) {
        this.guard = guard;
        return this;
    }

    @Override
    public int getBusinessValue() {
        return businessValue;
    }

    @Override
    public Transition setBusinessValue(int value) {
        if (value < BUSINESS_VALUE_MIN || value > BUSINESS_VALUE_MAX)
            throw new BusinessValueOutOfRangeException(value);
        this.businessValue = value;
        return this;
    }

    @Override
    public List<State> getSources() {
        return Collections.unmodifiableList(sources.stream()
                .map(s -> new UnmodifiableState(s))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll));
    }

    @Override
    public Transition setSources(State... states) {
        sources = new ArrayList<>(asList(states));
        return this;
    }

    @Override
    public Transition setSources(Collection<State> states) {
        this.sources = new ArrayList<>(states);
        return this;
    }

    @Override
    public State getDestination() {
        return new UnmodifiableState(destination);
    }

    @Override
    public Transition setDestination(State state) {
        destination = state;
        return this;
    }

    @Override
    public boolean contains(State state) {
        return getDestination().equals(state) || getSources().stream().anyMatch(s -> s.equals(state));
    }

    @Override
    public boolean isSource(State state) {
        return getSources().stream().anyMatch(s -> s.equals(state));
    }

    @Override
    public boolean isDestination(State state) {
        return getDestination().equals(state);
    }

    @Override
    public int hashCode() {
        return 5 * 17 * 17 * 17 * 17 * 17 + super.hashCode()
                + 17 * 17 * 17 * 17 * Objects.hashCode(guard)
                + 17 * 17 * 17 * businessValue
                + 17 * 17 * Objects.hashCode(sources)
                + 17 * Objects.hashCode(destination);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Transition
                && businessValue == ((Transition) obj).getBusinessValue()
                && Objects.equals(guard, ((Transition) obj).getGuard())
                && Objects.equals(sources, ((Transition) obj).getSources())
                && Objects.equals(destination, ((Transition) obj).getDestination());
    }
}
