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
import static java.util.Collections.unmodifiableCollection;
import java.util.Iterator;
import org.mazarineblue.mbt.expressions.Expression;

class EffectImpl
        implements Effect {

    private final String title;
    private final Collection<Expression> expressions;

    EffectImpl(String title, Collection<Expression> expressions) {
        this.title = title;
        this.expressions = expressions;
    }

    @Override
    public String toString() {
        return " / " + title;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public Collection<Expression> getExpressions() {
        return unmodifiableCollection(expressions);
    }

    @Override
    public int compareTo(Effect other) {
        int c = title.compareTo(other.getTitle());
        if (c != 0)
            c = expressions.size() - other.getExpressions().size();
        if (c != 0) {
            Iterator<Expression> it1 = expressions.iterator(), it2 = other.getExpressions().iterator();
            while (it1.hasNext() && c != 0)
                c = it1.next().compareTo(it2.next());
        }
        return c;
    }
}
