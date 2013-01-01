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
package org.mazarineblue.webdriver.keywords;

import org.mazarineblue.datasources.DataSource;
import org.mazarineblue.keyworddriven.librarymanager.Library;
import org.mazarineblue.webdriver.exceptions.WebKeyTypeUnsupportedException;
import org.mazarineblue.webdriver.keys.WebKeyFactory;
import org.mazarineblue.webdriver.WebKey;
import org.mazarineblue.webdriver.WebSource;
import org.mazarineblue.webdriver.WebKeyValidationException;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class TestUtility {

    private Library library;
    private final WebKeyFactory factory = new WebKeyFactory();

    public TestUtility(Library library) {
        this.library = library;
    }

    public void setLibrary(Library library) {
        this.library = library;
    }

    public WebKey getKey(String name)
            throws WebKeyTypeUnsupportedException, WebKeyValidationException {
        DataSource source = library.getDataSource();
        String type = source.getData(WebSource.type(name), String.class);
        String key = source.getData(WebSource.key(name), String.class);
        return factory.createInstance(type, key);
    }
}
