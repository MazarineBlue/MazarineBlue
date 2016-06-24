/*
 * Copyright (c) 2015 Alex de Kruijff
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

import org.mazarineblue.keyworddriven.librarymanager.Library;
import org.mazarineblue.webdriver.WebKey;
import org.mazarineblue.webdriver.WebToolkit;
import org.mazarineblue.webdriver.WebKeyValidationException;
import org.mazarineblue.webdriver.exceptions.ExecutionException;
import org.mazarineblue.webdriver.exceptions.KeyNotFoundException;
import org.mazarineblue.webdriver.exceptions.KeyNotValidException;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
class AbstractWebToolkitLibrary
        extends Library {

    protected WebToolkit toolkit;
    protected final TestUtility testUtility;

    AbstractWebToolkitLibrary(Library library, WebToolkit toolkit,
                              TestUtility testUtility) {
        super("org.mazarineblue.webdriver", library);
        this.toolkit = toolkit;
        this.testUtility = testUtility;
    }

    @Override
    protected void setup() {
    }

    @Override
    protected void teardown() {
    }

    void switchTo(WebToolkit toolkit) {
        this.toolkit = toolkit;
    }

    boolean selected(WebToolkit toolkit) {
        return this.toolkit == toolkit;
    }

    protected WebKey convertToWebKey(String keyName) {
        try {
            return testUtility.getKey(keyName);
        } catch (ExecutionException ex) {
            throw new KeyNotFoundException(keyName, ex);
        } catch (WebKeyValidationException ex) {
            throw new KeyNotValidException(keyName);
        }
    }
}
