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
package org.mazarineblue.neuralnetworks;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Neuron {

    private final String id;
    private final List<Connection> connections = new ArrayList<>();
    private final Function<List<Connection>, Double> collectingFunction;
    private final Function<Double, Double> activationFunction;

    public Neuron(String id) {
        this.id = id;
        this.collectingFunction = null;
        this.activationFunction = null;
    }

    public Neuron(String id, Function<List<Connection>, Double> collectingFunction, Function<Double, Double> activationFunction) {
        this.id = id;
        this.collectingFunction = collectingFunction;
        this.activationFunction = activationFunction;
    }

    @Override
    public String toString() {
        return "Neuron: " + id;
    }
    
    public void addInput(Connection e) {
        connections.add(e);
    }

    public Double output() {
        return activationFunction.apply(collectingFunction.apply(connections));
    }

    List<Connection> getInputConnections() {
        return connections;
    }
}
