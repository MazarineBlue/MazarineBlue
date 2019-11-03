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

// https://dzone.com/articles/designing-a-neural-network-in-java
// https://dzone.com/articles/designing-a-neural-network-in-java-activation-func
// https://dzone.com/articles/learning-neural-networks-using-java-libraries
public class Network {

    private final String id;
    private final Input[] inputs;
    private final Layer inputLayer;
    private final List<Layer> hiddenLayers;
    private final Layer outputLayer;

    public Network(String id, Input[] inputs, Layer inputLayer, Layer outputLayer) {
        this.id = id;
        this.inputs = inputs;
        this.inputLayer = inputLayer;
        this.hiddenLayers = new ArrayList<>();
        this.outputLayer = outputLayer;
    }

    Network(String id, Input[] inputs, Layer inputLayer, Layer firstHiddenLayer, Layer... layers) {
        this.id = id;
        this.inputs = inputs;
        this.inputLayer = inputLayer;
        this.hiddenLayers = getHiddenLayers(firstHiddenLayer, layers);
        this.outputLayer = layers[layers.length - 1];
    }

    private List<Layer> getHiddenLayers(Layer firstHiddenLayer, Layer[] layers) {
        List<Layer> hiddenLayers = new ArrayList<>();
        hiddenLayers.add(firstHiddenLayer);
        for (int i = 0; i < layers.length - 1; ++i)
            hiddenLayers.add(layers[i]);
        return hiddenLayers;
    }

    Double[] apply(double[] input) {
        setInputs(input);
        return calculate();
    }

    private void setInputs(double[] inputs) {
        if (this.inputs.length != inputs.length)
            throw new RuntimeException("Argument mismatch");
        for (int i = 0; i < inputs.length; ++i)
            this.inputs[i].set(inputs[i]);
    }

    private Double[] calculate() {
        List<Neuron> neurons = outputLayer.neurons();
        Double[] outputs = new Double[neurons.size()];
        for (int i = 0; i < outputs.length; ++i)
            outputs[i] = neurons.get(i).output();
        return outputs;
    }

    int hiddenLayersCount() {
        return hiddenLayers.size();
    }
    
    Layer getHiddenLayer(int index) {
        return hiddenLayers.get(index);
    }

    Layer getOutputLayer() {
        return outputLayer;
    }
}
