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

import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;
import org.mazarineblue.datasources.exceptions.NegativeIndexException;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class ResourceSource
        extends ArraySource {

    private final ResourceBundle bundle;

    /**
     * Gets a resource bundle as data source.
     *
     * @param sourceIdentifier the name of the source
     * @param baseName the base name of the resource bundle.
     * @exception java.lang.NullPointerException if <code>baseName</code> is <code>null</code>
     * @exception MissingResourceException if no resource bundle for the specified base name can be found
     */
    public ResourceSource(String sourceIdentifier, String baseName) {
        super(sourceIdentifier);
        bundle = ResourceBundle.getBundle(baseName);
    }

    public ResourceSource(String sourceIdentifier, ResourceBundle bundle) {
        super(sourceIdentifier);
        this.bundle = bundle;
    }

    @Override
    public Object getData(String column) {
        try {
            return bundle.getString(column);
        } catch (MissingResourceException ex) {
            return null;
        }
    }

    @Override
    public Set<String> getColumns() {
        return bundle.keySet();
    }

    @Override
    public int getIndex(String column) {
        return -1;
    }

    @Override
    public Object getData(int index) {
        validateState();
        return null;
    }

    @Override
    public boolean setData(String column, Object value) {
        validateState();
        return false;
    }

    @Override
    public boolean setData(int index, Object value) {
        if (index < 0)
            throw new NegativeIndexException(index);
        validateState();
        return false;
    }
}
