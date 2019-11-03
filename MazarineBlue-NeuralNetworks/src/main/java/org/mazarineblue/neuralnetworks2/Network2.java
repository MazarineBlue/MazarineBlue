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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Network2 {

    private final String id;
    private final InputLayer inputLayer = new InputLayer();
    private final List<Layer2> layers = new ArrayList<>();

    public Network2(String id) {
        this.id = id;
        layers.add(inputLayer);
    }

    public List<Layer2> layers() {
        return Collections.unmodifiableList(layers);
    }

    public InputLayer inputLayer() {
        return inputLayer;
    }

    public Layer2 outputLayer() {
        return layers.get(layers.size() - 1);
    }

    public GeneralLayer createLayer(String id, GeneralNeuron bias) {
        GeneralLayer layer = new GeneralLayer(id, bias);
        layers.add(layer);
        return layer;
    }

    public Double[] apply(double[] input) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void setInputs(double[] inputs) {
        inputLayer.setInputs(inputs);
    }
}
