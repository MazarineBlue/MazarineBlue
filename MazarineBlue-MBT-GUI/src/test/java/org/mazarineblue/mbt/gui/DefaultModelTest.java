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
package org.mazarineblue.mbt.gui;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.mbt.gui.exceptions.DestinationStateRequiredException;
import org.mazarineblue.mbt.gui.exceptions.IncompatibleViewsException;
import org.mazarineblue.mbt.gui.exceptions.SourceStateRequiredException;
import org.mazarineblue.mbt.gui.model.GraphModel;
import org.mazarineblue.mbt.gui.model.State;
import org.mazarineblue.mbt.gui.model.Transition;

public class DefaultModelTest {

    private GraphModel model;
    private State stateA;
    private State stateB;
    private State stateC;
    private State stateD;
    private State stateE;

    @Before
    public void setup() {
        model = GraphModel.createDefault();
        stateA = State.createDefault("State A").addViews("view 1", "view 2");
        stateB = State.createDefault("State B").addViews("view 1");
        stateC = State.createDefault("State C").addViews("view 2");
        stateD = State.createDefault("State D").addViews("view 3");
        stateE = State.createDefault("State E");
    }

    @After
    public void teardown() {
        model = null;
        stateA = stateB = stateC = stateD = stateE = null;
    }

    @Test(expected = SourceStateRequiredException.class)
    public void addTransition_WithoutSourceState() {
        model.addTransition(Transition.createDefault("transition").setDestination(stateA));
    }

    @Test(expected = DestinationStateRequiredException.class)
    public void addTransition_WithoutDestinationState() {
        model.addTransition(Transition.createDefault("transition").setSources(stateA));
    }

    @Test
    public void addTransition_StateA_StateB() {
        model.addTransition(Transition.createDefault("transition").setSources(stateA).setDestination(stateB));
    }

    @Test
    public void addTransition_StateBC_StateA() {
        model.addTransition(Transition.createDefault("transition").setSources(stateB, stateC).setDestination(stateA));
    }

    @Test(expected = IncompatibleViewsException.class)
    public void addTransition_StateBC_StateB() {
        model.addTransition(Transition.createDefault("transition").setSources(stateB, stateC).setDestination(stateB));
    }

    @Test
    public void addTransition_StateB_StateB() {
        model.addTransition(Transition.createDefault("transition").setSources(stateB).setDestination(stateB));
    }

    @Test(expected = IncompatibleViewsException.class)
    public void addTransition_StateB_StateC() {
        model.addTransition(Transition.createDefault("transition").setSources(stateB).setDestination(stateC));
    }

    @Test(expected = IncompatibleViewsException.class)
    public void addTransition_StateB_StateE() {
        model.addTransition(Transition.createDefault("transition").setSources(stateB).setDestination(stateE));
    }

    @Test
    public void addTransition_StateE_StateE() {
        model.addTransition(Transition.createDefault("transition").setSources(stateE).setDestination(stateE));
    }
}
