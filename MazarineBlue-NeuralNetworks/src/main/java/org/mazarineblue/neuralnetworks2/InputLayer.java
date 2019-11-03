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
import java.util.List;
import org.mazarineblue.neuralnetworks2.Layer2;

public class InputLayer
        implements Layer2 {

    private final List<InputNeuron> list = new ArrayList<>();

    public void addNeuron(InputNeuron neuron) {
        list.add(neuron);
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void setInputs(double[] inputs) {
        if (list.size() != inputs.length)
            throw new RuntimeException("Argument mismatch");
        int i = -1;
        for (InputNeuron n : list)
            n.setInput(inputs[++i]);
    }
}
