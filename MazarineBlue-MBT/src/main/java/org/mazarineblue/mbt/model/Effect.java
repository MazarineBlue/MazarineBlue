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
package org.mazarineblue.mbt.model;

import java.util.Collection;
import org.mazarineblue.mbt.Variable;
import org.mazarineblue.mbt.expressions.Expression;

public interface Effect
        extends Comparable<Effect> {

    default boolean isVariableDefined(Variable var) {
        return getExpressions().stream().anyMatch(e -> e.isVariableDefined(var));
    }

    default boolean isVariableUsed(Variable var) {
        return getExpressions().stream().anyMatch(e -> e.isVariableUsed(var));
    }

    public String getTitle();

    public Collection<Expression> getExpressions();
}
