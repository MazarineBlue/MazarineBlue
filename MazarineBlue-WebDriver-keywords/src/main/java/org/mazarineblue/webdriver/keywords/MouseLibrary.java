/*
 * Copyright (c) 2012-2014 Alex de Kruijff
 * Copyright (c) 2015 Specialisterren
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
import org.mazarineblue.webdriver.Offset;
import org.mazarineblue.webdriver.WebKey;
import org.mazarineblue.webdriver.WebToolkit;
import org.mazarineblue.webdriver.exceptions.ElementNotFoundException;
import org.mazarineblue.webdriver.exceptions.ExecutionException;
import org.mazarineblue.webdriver.exceptions.WebKeyTypeUnsupportedException;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class MouseLibrary
        extends AbstractWebToolkitLibrary {

    public MouseLibrary(Library library, WebToolkit toolkit,
                        TestUtility testUtility) {
        super(library, toolkit, testUtility);
    }

    @Keyword("Click")
    public void click(String keyName) {
        try {
            WebKey key = keyName == null ? null : convertToWebKey(keyName);
            toolkit.mouseInstructions().click(key);
        } catch (ExecutionException ex) {
            throw new ElementNotFoundException(keyName, ex);
        }
    }

    @Keyword("Click and hold")
    public void clickAndHold(String keyName) {
        try {
            WebKey key = convertToWebKey(keyName);
            toolkit.mouseInstructions().click(key);
        } catch (ExecutionException ex) {
            throw new ElementNotFoundException(keyName, ex);
        }
    }

    @Keyword("Context click")
    public void contextClick(String keyName) {
        try {
            WebKey key = keyName == null ? null : convertToWebKey(keyName);
            toolkit.mouseInstructions().contextClick(key);
        } catch (ExecutionException ex) {
            throw new ElementNotFoundException(keyName, ex);
        }
    }

    @Keyword("Drag and drop")
    @Parameters(min = 2)
    public void dragAndDrop(String keyFromName, String keyToName) {
        try {
            WebKey from = convertToWebKey(keyFromName);
            WebKey to = convertToWebKey(keyToName);
            toolkit.mouseInstructions().dragAndDrop(from, to);
        } catch (ExecutionException ex) {
            throw new ElementNotFoundException(keyFromName, keyToName, ex);
        }
    }

    @Keyword("Drag and drop by offset")
    @Parameters(min = 3)
    public void dragAndDropByOffset(String keyFromName, int xOffset, int yOffset) {
        Offset to = new Offset(xOffset, yOffset);
        try {
            WebKey from = convertToWebKey(keyFromName);
            toolkit.mouseInstructions().dragAndDropByOffset(from, to);
        } catch (ExecutionException ex) {
            throw new ElementNotFoundException(keyFromName, to, ex);
        }
    }

    @Keyword("Double click")
    public void doubleClick(String keyName) {
        try {
            WebKey key = keyName == null ? null : convertToWebKey(keyName);
            toolkit.mouseInstructions().doubleClick(key);
        } catch (ExecutionException ex) {
            throw new ElementNotFoundException(keyName, ex);
        }
    }

    @Deprecated
    @Keyword("Move by offset")
    @Parameters(min = 2)
    public void moveByOffset(int xOffset, int yOffset)
            throws WebKeyTypeUnsupportedException {
        moveMouseByOffset(xOffset, yOffset);
    }

    @Keyword("Move mouse by offset")
    @Parameters(min = 2)
    public void moveMouseByOffset(int xOffset, int yOffset)
            throws WebKeyTypeUnsupportedException {
        Offset offset = new Offset(xOffset, yOffset);
        toolkit.mouseInstructions().moveByOffset(offset);
    }

    @Deprecated
    @Keyword("Move to element")
    @Parameters(min = 1)
    public void moveToElement(String keyName) {
        moveMouseToElement(keyName);
    }

    @Keyword("Move mouse to element")
    @Parameters(min = 1)
    public void moveMouseToElement(String keyName) {
        try {
            WebKey key = keyName == null ? null : convertToWebKey(keyName);
            toolkit.mouseInstructions().moveToElement(key);
        } catch (ExecutionException ex) {
            throw new ElementNotFoundException(keyName, ex);
        }
    }

    @Deprecated
    @Keyword("Move to element by offset")
    @Parameters(min = 3)
    public void moveToElementByOffset(String keyName, int xOffset, int yOffset) {
        moveMouseToElementByOffset(keyName, xOffset, yOffset);
    }

    @Keyword("Move mouse to element by offset")
    @Parameters(min = 3)
    public void moveMouseToElementByOffset(String keyName, int xOffset, int yOffset) {
        Offset offset = new Offset(xOffset, yOffset);
        try {
            WebKey key = keyName == null ? null : convertToWebKey(keyName);
            toolkit.mouseInstructions().moveToElementByOffset(key, offset);
        } catch (ExecutionException ex) {
            throw new ElementNotFoundException(keyName, offset, ex);
        }
    }

    @Keyword("Release mouse")
    public void releaseMouse(String keyName) {
        try {
            WebKey key = keyName == null ? null : convertToWebKey(keyName);
            toolkit.mouseInstructions().releaseMouse(key);
        } catch (ExecutionException ex) {
            throw new ElementNotFoundException(keyName, ex);
        }
    }
}
