/*
 * Copyright (c) 2012-2014 Alex de Kruijff
 * Copyright (c) 2014-2015 Specialisterren
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
package org.mazarineblue.datasources;

import static java.util.Arrays.asList;
import static java.util.Collections.EMPTY_LIST;
import static java.util.Collections.unmodifiableList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public abstract class AbstractSource
        implements DataSource {

    private final List<String> sourceIdentifiers;

    public AbstractSource(String sourceIdentifier) {
        if (sourceIdentifier == null)
            sourceIdentifiers = EMPTY_LIST;
        else {
            List<String> col = asList(sourceIdentifier);
            this.sourceIdentifiers = unmodifiableList(col);
        }
    }

    @Override
    public List<String> getSourceIdentifiers() {
        return sourceIdentifiers;
    }

    public String getSourceIdentifier() {
        return sourceIdentifiers.get(0);
    }

    @Override
    public List<String> getLineIdentifiers() {
        return asList(getLineIdentifier());
    }

    public abstract String getLineIdentifier();

    @Override
    public int hashCode() {
        return 39 + Objects.hashCode(this.sourceIdentifiers);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (AbstractSource.class.isAssignableFrom(obj.getClass()) == false)
            return false;
        final AbstractSource other = (AbstractSource) obj;
        return Objects.equals(this.sourceIdentifiers, other.sourceIdentifiers);
    }
}
