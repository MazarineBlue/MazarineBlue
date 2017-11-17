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
package org.mazarineblue.mbt.tests;

import java.util.List;
import org.mazarineblue.mbt.expressions.Expression;
import org.mazarineblue.mbt.model.Effect;
import org.mazarineblue.mbt.model.State;
import org.mazarineblue.mbt.model.Transition;

public interface TraceExtension
        extends Comparable<TraceExtension> {

    public static TraceExtension createDefault() {
        return new TraceExtensionImpl();
    }

    public static TraceExtension createDefault(TraceExtension te) {
        return new TraceExtensionImpl(te);
    }

    public void addFirst(Transition t);

    public void add(Transition t);

    public void remove(Transition t);

    public State getLastTargetState();

    public List<Transition> getTransitions();

    public void transformExpressions(Effect effect);

    public Expression getExpression();

    public void setExpression(Expression expression);

    public boolean match(Transition t);

    public boolean contains(Transition t);
}
