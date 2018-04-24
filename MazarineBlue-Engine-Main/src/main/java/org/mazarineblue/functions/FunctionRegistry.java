/*
 * Copyright (c) 2018 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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
package org.mazarineblue.functions;

import java.util.HashMap;
import java.util.Map;

public class FunctionRegistry {

    private final Map<String, Function> map = new HashMap<>();
    private final FunctionRegistry adaptee;

    public FunctionRegistry() {
        this.adaptee = null;
    }

    public FunctionRegistry(FunctionRegistry adaptee) {
        this.adaptee = adaptee;
    }

    void add(Function function) {
        map.put(function.getName(), function);
    }

    boolean containsFunction(String keyword) {
        return getFunction(keyword) != null;
    }

    Function getFunction(String keyword) {
        Function func = map.get(keyword);
        if (func != null)
            return func;
        if (adaptee != null)
            return adaptee.getFunction(keyword);
        return null;
    }
}
