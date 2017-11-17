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

import java.util.Iterator;
import java.util.Set;
import org.mazarineblue.mbt.criteria.trace.TracePattern;
import org.mazarineblue.mbt.tests.TestCase;

public interface TestGoal
        extends Comparable<TestGoal> {

    public static TestGoal build(TracePattern... tp) {
        return new AtomicTestGoal(tp);
    }

    public static TestGoal and(TestGoal left, TestGoal right) {
        return new AndTestGoal(left, right);
    }

    public static TestGoal or(TestGoal left, TestGoal right) {
        return new OrTestGoal(left, right);
    }

    public static TestGoal getFalse() {
        return new FalseTestGoal();
    }

    public boolean satisfied(TestCase tc);

    @Override
    default int compareTo(TestGoal other) {
        Set<TracePattern> we = getTracePatterns(), they = other.getTracePatterns();
        int c = we.size() - they.size();
        Iterator<TracePattern> it1 = we.iterator(), it2 = they.iterator();
        while (c == 0 && it1.hasNext())
            c = it1.next().compareTo(it2.next());
        return c;
    }

    public Set<TracePattern> getTracePatterns();
}
