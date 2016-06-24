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
package org.mazarineblue.keyworddriven.util.old;

import java.util.HashMap;
import java.util.Map;
import org.mazarineblue.datasources.DataSource;
import org.mazarineblue.keyworddriven.exceptions.SheetNotFoundException;
import org.mazarineblue.keyworddriven.feeds.Feed;
import org.mazarineblue.keyworddriven.sheetfactories.SheetFactory;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class TestSheetFactory
        implements SheetFactory {

    private Map<String, DataSource> map = new HashMap();

    public TestSheetFactory() {
    }

    public DataSource put(String key, DataSource value) {
        return map.put(key, value);
    }

    @Override
    public String getLocation() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String[] getSheetNames() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Feed getSheetFeed(String sheetName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DataSource getArraySource(String sourceIdentifier, String sheetName) {
        if (map.containsKey(sheetName) == false)
            throw new SheetNotFoundException(sheetName);
        return map.get(sheetName);
    }

    @Override
    public DataSource getMatrixSource(String sourceIdentifier, String sheetName) {
        if (map.containsKey(sheetName) == false)
            throw new SheetNotFoundException(sheetName);
        return map.get(sheetName);
    }
}
