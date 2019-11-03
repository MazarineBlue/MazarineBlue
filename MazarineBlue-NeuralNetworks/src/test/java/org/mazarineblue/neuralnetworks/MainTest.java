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

import org.mazarineblue.neuralnetworks2.InputLayer;
import org.mazarineblue.neuralnetworks2.InputNeuron;
import org.mazarineblue.neuralnetworks2.GeneralNeuron;
import org.mazarineblue.neuralnetworks2.Network2;
import org.junit.Test;
import org.mazarineblue.neuralnetworks.activations.FixedActivationFunction;
import org.mazarineblue.neuralnetworks.sums.WeigthedSumFunction;
import org.mazarineblue.neuralnetworks2.GeneralLayer;

/**
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class MainTest {

    private double[][] inputs = {{1, 1}, {1, 0}, {0, 1}, {0, 0}};
    private double expectedOutputs[][] = {{0}, {1}, {1}, {0}};

    private final double learningRate = 2.9f;
    private final double momentum = 0.7f;

    @Test
    public void test() {
        Network2 network = setupNetwork();
        train(network, 50000, 0.001);
    }

    private Network2 setupNetwork() {
        Network2 network = new Network2("Network");

        InputLayer inputLayer = network.inputLayer();
        inputLayer.addNeuron(new InputNeuron("A1", new WeigthedSumFunction()));
        inputLayer.addNeuron(new InputNeuron("A2", new WeigthedSumFunction()));

        GeneralLayer layer = network.createLayer("B", new GeneralNeuron("B*", new WeigthedSumFunction(), (Double) -> 1d));
        layer.addNeuron(new GeneralNeuron("B1", new WeigthedSumFunction(), new FixedActivationFunction(1d)));
        layer.addNeuron(new GeneralNeuron("B2", new WeigthedSumFunction(), new FixedActivationFunction(1d)));

        GeneralLayer outputLayer = network.createLayer("OUTPUT", new GeneralNeuron("Z*", new WeigthedSumFunction(), (Double) -> 1d));
        outputLayer.addNeuron(new GeneralNeuron("Z1", new WeigthedSumFunction(), new FixedActivationFunction(1d)));

        return network;
    }

    private void train(Network2 network, int maxSteps, double minError) {
        int i;
        double error = 1;
        for (i = 0; i < maxSteps && error > minError; ++i) {
            error = 0;
            for (int j = 0; j < inputs.length; ++j) {
                double[] input = inputs[j];
                Double[] actual = network.apply(input);
                double[] expected = expectedOutputs[j];
                error += calculateError(expected, actual);
//                applyBackpropagation(expectedOutputs[j]);
            }
        }
    }

    private double calculateError(double[] expected, Double[] actual) {
        double error = 0;
        for (int i = 0; i < expectedOutputs[i].length; i++)
            error += Math.pow(actual[i] - expected[i], 2);
        return error;
    }
}
