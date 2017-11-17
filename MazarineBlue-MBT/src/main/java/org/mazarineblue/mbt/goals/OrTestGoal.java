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
package org.mazarineblue.mbt.goals;

import java.util.Set;
import org.mazarineblue.mbt.criteria.trace.TracePattern;
import org.mazarineblue.mbt.tests.TestCase;

class OrTestGoal
        implements TestGoal {

    private final TestGoal left;
    private final TestGoal right;

    OrTestGoal(TestGoal left, TestGoal right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean satisfied(TestCase tc) {
        return left.satisfied(tc) || right.satisfied(tc);
    }

    @Override
    public Set<TracePattern> getTracePatterns() {
        Set<TracePattern> tracePatterns = left.getTracePatterns();
        tracePatterns.addAll(right.getTracePatterns());
        return tracePatterns;
    }
}
