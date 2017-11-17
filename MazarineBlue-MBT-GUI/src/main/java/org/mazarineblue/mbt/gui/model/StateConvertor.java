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
import java.util.List;
import java.util.function.Function;

/**
 * A {@code StateConvertor} is an object that is able to convert a given state
 * into another equal state contained within the convertor.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
class StateConvertor {

    private final List<State> states;
    private final List<State> cached = new ArrayList<>();
    private final Function<State, State> mapper;

    /**
     * Sets up a {@code StateConvertor} with a set of states to use as
     * replacements.
     *
     * @param states the states to use as replacements.
     */
    StateConvertor(List<State> states) {
        this.states = states;
        mapper = s -> s;
    }

    /**
     * Sets up a {@code StateConvertor} with a set of states to use as
     * replacements and a mapper function to apply to each convertion.
     *
     * @param states the states to use as replacements.
     * @param mapper a function to apply to each convertion.
     */
    StateConvertor(List<State> states, Function<State, State> mapper) {
        this.states = states;
        this.mapper = mapper;
    }

    /**
     * Converts the specified {@code states} to another {@code state}, using
     * first the mapper function and then looking for an equal {@code state}.
     *
     * @param list a list of {@code states} to convert.
     * @return the converted list of {@code states}.
     */
    List<State> convert(List<State> list) {
        return list.stream()
                .map(mapper)
                .map(s -> convertHelper(s))
                .collect(ArrayList::new, List::add, List::addAll);
    }

    /**
     * Converts the specified {@code state} to another {@code state}, using
     * first the mapper function and then looking for an equal {@code state}.
     *
     * @param s a {@code state} to convert.
     * @return the converted {@code state}.
     */
    State convert(State s) {
        return convertHelper(mapper.apply(s));
    }

    private State convertHelper(State state) {
        int index = states.indexOf(state);
        return index >= 0 ? states.get(index) : createOrFetchCopy(state);
    }

    private State createOrFetchCopy(State state) {
        int index = cached.indexOf(state);
        return index >= 0 ? cached.get(index) : createCopy(state);
    }

    private State createCopy(State state) {
        State s = State.createDefault(state);
        cached.add(s);
        return s;
    }
}
