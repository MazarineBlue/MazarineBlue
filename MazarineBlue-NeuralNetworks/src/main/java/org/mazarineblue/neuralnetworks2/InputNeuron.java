/*
 * Copyright (c) Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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
package org.mazarineblue.neuralnetworks2;

import org.mazarineblue.neuralnetworks2.Neuron2;
import java.util.List;
import java.util.function.Function;
import org.mazarineblue.neuralnetworks.Connection;

public class InputNeuron
        implements Neuron2 {

    private final String id;
    private double input;
    private final Function<List<Connection>, Double> summingFunction;

    public InputNeuron(String id, Function<List<Connection>, Double> summingFunction) {
        this.id = id;
        this.summingFunction = summingFunction;
    }

    void setInput(double input) {
        this.input = input;
    }
}
