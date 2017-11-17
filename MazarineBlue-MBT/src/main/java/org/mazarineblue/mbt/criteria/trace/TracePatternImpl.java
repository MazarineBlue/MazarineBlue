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
package org.mazarineblue.mbt.criteria.trace;

import java.util.ArrayList;
import static java.util.Arrays.asList;
import java.util.List;
import java.util.Objects;
import org.mazarineblue.mbt.X;
import org.mazarineblue.mbt.criteria.step.StepPattern;

class TracePatternImpl
        implements TracePattern {

    private final List<StepPattern> list;

    TracePatternImpl(StepPattern... sp) {
        this.list = new ArrayList<>(asList(sp));
    }

    @Override
    public String toString() {
        if (list.isEmpty())
            return "()";
        StringBuilder builder = null;
        for (StepPattern sp : list)
            builder = builder == null
                    ? new StringBuilder(32 * list.size()).append('(').append(sp)
                    : builder.append(", ").append(sp);
        return builder.append(')').toString();
    }

    @Override
    public void add(StepPattern sp) {
        list.add(sp);
    }

    @Override
    public void addAll(TracePattern tp) {
        list.addAll(tp.getAll());
    }

    @Override
    public StepPattern get(int index) {
        return list.get(index);
    }

    @Override
    public List<StepPattern> getAll() {
        return list;
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public int compareTo(TracePattern other) {
        return X.compateTo(list, other.getAll());
    }

    @Override
    public int hashCode() {
        return 5 * 11
                + Objects.hashCode(this.list);
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && getClass() == obj.getClass()
                && Objects.equals(this.list, ((TracePattern) obj).getAll());
    }
}
