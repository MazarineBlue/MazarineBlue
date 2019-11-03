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
import static java.util.Collections.unmodifiableList;
import java.util.List;

public class Layer {

    private final String id;
    private final List<Neuron> list;

    public Layer(String id) {
        this.id = id;
        list = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Layer: " + id;
    }

    void add(Neuron e) {
        list.add(e);
    }

    int size() {
        return list.size();
    }

    List<Neuron> neurons() {
        return unmodifiableList(list);
    }
}
