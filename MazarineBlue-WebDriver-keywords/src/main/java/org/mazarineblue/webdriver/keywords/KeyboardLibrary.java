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

import org.mazarineblue.keyworddriven.Keyword;
import org.mazarineblue.keyworddriven.Parameters;
import org.mazarineblue.keyworddriven.librarymanager.Library;
import org.mazarineblue.webdriver.WebKey;
import org.mazarineblue.webdriver.WebToolkit;
import org.mazarineblue.webdriver.exceptions.ExecutionException;
import org.mazarineblue.webdriver.exceptions.KeyNotFoundException;
import org.mazarineblue.webdriver.exceptions.KeyNotValidException;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class KeyboardLibrary
        extends AbstractWebToolkitLibrary {

    KeyboardLibrary(Library library, WebToolkit toolkit, TestUtility testUtility) {
        super(library, toolkit, testUtility);
    }

    @Keyword("Type")
    @Parameters(min = 1, max = 2)
    public void type(String keyName, String input) {
        WebKey key = convertToWebKey(keyName);
        try {
            if (input == null)
                input = "";
            toolkit.keyboardInstructions().type(key, input);
        } catch (ExecutionException ex) {
            throw new KeyNotFoundException(keyName, ex);
        }
    }

    @Keyword("Continue typing")
    @Parameters(min = 1, max = 2)
    public void continueTyping(String keyName, String input) {
        WebKey key = convertToWebKey(keyName);
        try {
            if (input == null)
                input = "";
            toolkit.keyboardInstructions().type(key, input, false);
        } catch (ExecutionException ex) {
            throw new KeyNotFoundException(keyName, ex);
        }
    }

    @Keyword("Press key")
    @Parameters(min = 1)
    public void pressKey(String keyName, String... input) {
        if (keyName == null || keyName.equals(""))
            throw new KeyNotValidException(keyName);
        WebKey key = convertToWebKey(keyName);
        try {
            toolkit.keyboardInstructions().pressKey(key, input);
        } catch (ExecutionException ex) {
            throw new KeyNotFoundException(keyName, ex);
        }
    }
}
