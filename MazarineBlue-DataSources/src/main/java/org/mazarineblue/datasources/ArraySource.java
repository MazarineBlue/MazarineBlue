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

import org.mazarineblue.datasources.exceptions.IllegalSourceStateException;
import org.mazarineblue.datasources.exceptions.RowIndexOutOfBoundsException;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public abstract class ArraySource
        extends AbstractSource {

    private boolean initialized = false;

    protected ArraySource(String sourceIdentifier) {
        super(sourceIdentifier);
    }

    protected ArraySource(String sourceIdentifier, boolean initialize) {
        super(sourceIdentifier);
        initialized = initialize;
    }

    @Override
    public boolean hasNext() {
        return initialized == false;
    }

    @Override
    public void next() {
        if (initialized == true)
            throw new RowIndexOutOfBoundsException(0);
        initialized = true;
    }

    @Override
    public void reset() {
        initialized = false;
    }

    @Override
    public String getLineIdentifier() {
        return getSourceIdentifier() + ":" + (initialized ? "0" : "-1");
    }

    protected void validateState() {
        if (initialized == false)
            throw new IllegalSourceStateException("Next was never called");
    }

    public boolean isInitilized() {
        return initialized;
    }

    public abstract int getIndex(String column);
}
