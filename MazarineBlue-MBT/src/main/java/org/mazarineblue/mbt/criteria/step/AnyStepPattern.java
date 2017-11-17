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
package org.mazarineblue.mbt.criteria.step;

import java.util.List;
import java.util.Set;
import org.mazarineblue.mbt.expressions.Expression;
import org.mazarineblue.mbt.model.Event;
import org.mazarineblue.mbt.model.StateConfiguration;
import org.mazarineblue.mbt.model.Transition;

class AnyStepPattern
        extends AbstractStepPattern {

    AnyStepPattern(StateConfiguration sc, Set<Event> e, Expression cva, List<Transition> t) {
        super(sc, e, cva, t);
    }

    @Override
    public String toString() {
        return super.toString() + "*";
    }
}
