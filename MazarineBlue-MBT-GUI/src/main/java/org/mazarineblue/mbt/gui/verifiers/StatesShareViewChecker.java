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
package org.mazarineblue.mbt.gui.verifiers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Supplier;
import org.mazarineblue.mbt.gui.model.State;

/**
 * A {@code StatesShareViewChecker} is a utility that checks if each pair of
 * states share at least one view to getters.
 *
 * @see #doStatePairsShareAView(State, Supplier)
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class StatesShareViewChecker {

    private final State inputState;
    private final Supplier<Collection<State>> collectionFetcher;
    private Collection<State> otherStates;

    /**
     * Checks if each {@link State state} pair, existing out of the specified
     * {@code state} and one {@code state} from the collection, share at least
     * one view.
     *
     * @param state             the {@code state} to check against each state
     *                          in the collection.
     * @param collectionFetcher the {@code supplier} that fetches a collection
     *                          of states.
     * @return {@code true} if all pair have at least one view.
     */
    public static boolean doStatePairsShareAView(State state, Supplier<Collection<State>> collectionFetcher) {
        return new StatesShareViewChecker(state, collectionFetcher).found();
    }

    private StatesShareViewChecker(State inputState, Supplier<Collection<State>> collectionFetcher) {
        this.inputState = inputState;
        this.collectionFetcher = collectionFetcher;
    }

    private boolean found() {
        return !isInputStateUsed() || !isOtherStateUsed() || isSharedViewFoundForAllStatePairs();
    }

    private boolean isInputStateUsed() {
        return inputState != null;
    }

    private boolean isOtherStateUsed() {
        otherStates = collectionFetcher.get();
        return otherStates != null;
    }

    private boolean isSharedViewFoundForAllStatePairs() {
        Collection<String> inputViews = inputState.getViews();
        return otherStates.stream()
                .map(state -> state.getViews())
                .filter(otherViews -> !isBothCollectionsEmpty(inputViews, otherViews))
                .map(otherViews -> intersection(inputViews, otherViews))
                .noneMatch(sharedViews -> sharedViews.isEmpty());
    }

    private static boolean isBothCollectionsEmpty(Collection<String> inputViews, Collection<String> otherViews) {
        return inputViews.isEmpty() && otherViews.isEmpty();
    }

    private Collection<String> intersection(Collection<String> inputViews, Collection<String> otherViews) {
        Collection<String> sharedViews = new ArrayList<>(inputViews);
        sharedViews.retainAll(otherViews);
        return sharedViews;
    }
}
