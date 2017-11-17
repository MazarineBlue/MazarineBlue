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

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import org.mazarineblue.mbt.criteria.trace.TracePattern;
import org.mazarineblue.mbt.tests.TestCase;

class AtomicTestGoal
        implements TestGoal {

    private final Set<TracePattern> atc;

    AtomicTestGoal(TracePattern... tp) {
        atc = new HashSet<>(asList(tp));
    }

    @Override
    public String toString() {
        Iterator<TracePattern> it = atc.iterator();
        if (!it.hasNext())
            return "";
        StringBuilder builder = new StringBuilder(it.next().toString());
        while (it.hasNext())
            builder.append(", ").append(it.next());
        return builder.toString();
    }

    @Override
    public boolean satisfied(TestCase tc) {
        return atc.stream().anyMatch(tp -> tc.covers(tp));
    }

    @Override
    public Set<TracePattern> getTracePatterns() {
        return unmodifiableSet(atc);
    }

    @Override
    public int hashCode() {
        return 7 * 79
                + Objects.hashCode(this.atc);
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && getClass() == obj.getClass()
                && Objects.equals(this.atc, ((AtomicTestGoal) obj).atc);
    }
}
