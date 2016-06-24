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

import org.mazarineblue.events.SetStatusEvent;
import org.mazarineblue.keyworddriven.Keyword;
import org.mazarineblue.keyworddriven.Parameters;
import org.mazarineblue.keyworddriven.librarymanager.Library;
import org.mazarineblue.webdriver.WebKey;
import org.mazarineblue.webdriver.WebToolkit;
import org.mazarineblue.webdriver.exceptions.ExecutionException;
import org.mazarineblue.webdriver.exceptions.KeyNotFoundException;
import org.mazarineblue.webdriver.exceptions.WebKeyTypeUnsupportedException;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class ElementLibrary
        extends AbstractWebToolkitLibrary {

    ElementLibrary(Library library, WebToolkit toolkit, TestUtility testUtility) {
        super(library, toolkit, testUtility);
    }

    @Keyword("Submit")
    @Parameters(min = 1)
    public void submit(String keyName) {
        try {
            WebKey key = convertToWebKey(keyName);
            toolkit.elementInstructions().submit(key);
        } catch (ExecutionException ex) {
            throw new KeyNotFoundException(keyName, ex);
        }
    }

    @Keyword("Validate element exists")
    @Parameters(min = 1)
    public void validateElementExist(String keyName) {
        try {
            WebKey key = convertToWebKey(keyName);
            int count = toolkit.elementInstructions().getElementCount(key);
            boolean flag = count > 0;
            publish(new SetStatusEvent(flag));
            if (flag == false) {
                String format = "Element not found: %s";
                String error = String.format(format, keyName);
                log().error(error);
            }
        } catch (ExecutionException ex) {
            throw new KeyNotFoundException(keyName, ex);
        }
    }

    @Keyword("Validate element count")
    @Parameters(min = 2)
    public void validateElementCount(String keyName, int min, int max) {
        try {
            WebKey key = convertToWebKey(keyName);
            int count = toolkit.elementInstructions().getElementCount(key);
            boolean flag = min <= count && count <= max;
            publish(new SetStatusEvent(flag));
            if (flag == false) {
                String format = "Element count was %d but should be between %d and %d";
                String error = String.format(format, count, min, max);
                log().error(error);
            }
        } catch (ExecutionException ex) {
            throw new KeyNotFoundException(keyName, ex);
        }
    }

    @Keyword("Copy element attribute value")
    @Parameters(min = 3)
    public void copyElementAttributeValue(String variableName, String keyName, String attribute) {
        try {
            WebKey key = convertToWebKey(keyName);
            String attributeValue = toolkit.elementInstructions().getElementAttributeValue(
                    key, attribute);
            blackboard().setData(variableName, attributeValue);
        } catch (ExecutionException ex) {
            throw new KeyNotFoundException(keyName, ex);
        }
    }

    @Keyword("Validate element contains attribute")
    @Parameters(min = 2)
    public void validateElementContainsAttribute(String keyName, String attribute) {
        try {
            WebKey key = convertToWebKey(keyName);
            String value = toolkit.elementInstructions().getElementAttributeValue(
                    key, attribute);
            boolean flag = BrowserLibrary.check(value, ".+");
            publish(new SetStatusEvent(flag));
            if (flag == false) {
                String format = "Element %s with attribute %s had value %s";
                String error = String.format(format, keyName, attribute, value);
                log().error(error);
            }
        } catch (ExecutionException ex) {
            throw new KeyNotFoundException(keyName, ex);
        }
    }

    @Deprecated
    @Keyword("Validate element attribute")
    @Parameters(min = 3)
    public void validateElementAttribute(String keyName, String attribute, String regex) {
        validateElementContainsAttributeValue(keyName, attribute, regex);
    }

    @Keyword("Validate element contains attribute value")
    @Parameters(min = 3)
    public void validateElementContainsAttributeValue(String keyName, String attribute, String regex) {
        try {
            WebKey key = convertToWebKey(keyName);
            String value = toolkit.elementInstructions().getElementAttributeValue(
                    key, attribute);
            boolean flag = BrowserLibrary.check(value, regex);
            publish(new SetStatusEvent(flag));
            if (flag == false) {
                String format = "Element %s with attribute %s had value %s and dit not match regex: %s";
                String error = String.format(format, keyName, attribute, value,
                                             regex);
                log().error(error);
            }
        } catch (ExecutionException ex) {
            throw new KeyNotFoundException(keyName, ex);
        }
    }

    @Keyword("Copy element css value")
    @Parameters(min = 3)
    public void copyElementCssValue(String variableName, String keyName,  String css) {
        try {
            WebKey key = convertToWebKey(keyName);
            String cssValue = toolkit.elementInstructions().getElementCssValue(
                    key, css);
            blackboard().setData(variableName, cssValue);
        } catch (ExecutionException ex) {
            throw new KeyNotFoundException(keyName, ex);
        }
    }

    @Keyword("Validate element contains css")
    @Parameters(min = 2)
    public void validateElementContainsCss(String keyName, String css) {
        try {
            WebKey key = convertToWebKey(keyName);
            String value = toolkit.elementInstructions().getElementCssValue(key,
                                                                            css);
            boolean flag = BrowserLibrary.check(value, ".+");
            publish(new SetStatusEvent(flag));
            if (flag == false) {
                String format = "Element %s with css %s had value %s";
                String error = String.format(format, keyName, css, value);
                log().error(error);
            }
        } catch (ExecutionException ex) {
            throw new KeyNotFoundException(keyName, ex);
        }
    }

    @Deprecated
    @Keyword("Validate element css")
    @Parameters(min = 3)
    public void validateElementCss(String keyName, String css, String regex) {
        validateElementContainsCssValue(keyName, css, regex);
    }

    @Keyword("Validate element contains css value")
    @Parameters(min = 3)
    public void validateElementContainsCssValue(String keyName, String css, String regex) {
        try {
            WebKey key = convertToWebKey(keyName);
            String value = toolkit.elementInstructions().getElementCssValue(key,
                                                                            css);
            boolean flag = BrowserLibrary.check(value, regex);
            publish(new SetStatusEvent(flag));
            if (flag == false) {
                String format = "Element %s with css %s had value %s and dit not match regex: %s";
                String error = String.format(format, keyName, css, value, regex);
                log().error(error);
            }
        } catch (ExecutionException ex) {
            throw new KeyNotFoundException(keyName, ex);
        }
    }

    @Keyword("Copy element tag name")
    @Parameters(min = 2)
    public void copyElementTagName(String variableName, String keyName)
            throws WebKeyTypeUnsupportedException {
        WebKey key = convertToWebKey(keyName);
        try {
            String tagName = toolkit.elementInstructions().getElementTageName(
                    key);
            blackboard().setData(variableName, tagName);
        } catch (ExecutionException ex) {
            throw new KeyNotFoundException(keyName, ex);
        }
    }

    @Keyword("Validate element tag name")
    @Parameters(min = 2)
    public void validateElementTagName(String keyName, String regex)
            throws WebKeyTypeUnsupportedException {
        WebKey key = convertToWebKey(keyName);
        try {
            String tag = toolkit.elementInstructions().getElementTageName(key);
            boolean flag = BrowserLibrary.check(tag, regex);
            publish(new SetStatusEvent(flag));
            if (flag == false) {
                String format = "Element %s had the tag %s that not match regex: %s";
                String error = String.format(format, keyName, regex);
                log().error(error);
            }
        } catch (ExecutionException ex) {
            throw new KeyNotFoundException(keyName, ex);
        }
    }

    @Keyword("Copy element is selected")
    @Parameters(min = 2)
    public void copyElementIsSelected(String variabeName, String keyName) {
        try {
            WebKey key = convertToWebKey(keyName);
            boolean flag = toolkit.elementInstructions().isElementSelected(key);
            blackboard().setData(variabeName, flag);
        } catch (ExecutionException ex) {
            throw new KeyNotFoundException(keyName, ex);
        }
    }

    @Keyword("Validate element is selected")
    @Parameters(min = 1)
    public void validateElementIsSelected(String keyName, Boolean selected) {
        if (selected == null)
            selected = true;
        try {
            WebKey key = convertToWebKey(keyName);
            boolean flag = toolkit.elementInstructions().isElementSelected(key);
            publish(new SetStatusEvent(flag));
            if (flag != selected) {
                String format = "Element %s was %s selected";
                String error = String.format(format, keyName,
                                             selected ? "not" : "");
                log().error(error);
            }
        } catch (ExecutionException ex) {
            throw new KeyNotFoundException(keyName, ex);
        }
    }

    @Keyword("Copy element is displayed")
    @Parameters(min = 2)
    public void copyElementIsVisible(String variabeName, String keyName) {
        try {
            WebKey key = convertToWebKey(keyName);
            boolean flag = toolkit.elementInstructions().isElementDisplayed(key);
            blackboard().setData(variabeName, flag);
        } catch (ExecutionException ex) {
            throw new KeyNotFoundException(keyName, ex);
        }
    }

    @Keyword("Validate element is displayed")
    @Parameters(min = 1)
    public void validateElementIsVisible(String keyName, Boolean selected) {
        if (selected == null)
            selected = true;
        try {
            WebKey key = convertToWebKey(keyName);
            boolean flag = toolkit.elementInstructions().isElementDisplayed(key);
            publish(new SetStatusEvent(flag));
            if (flag != selected) {
                String format = "Element %s was %s visible";
                String error = String.format(format, keyName,
                                             selected ? "not" : "");
                log().error(error);
            }
        } catch (ExecutionException ex) {
            throw new KeyNotFoundException(keyName, ex);
        }
    }

    @Keyword("Copy element is enabled")
    @Parameters(min = 2)
    public void copyElementIsEnabled(String variabeName, String keyName) {
        try {
            WebKey key = convertToWebKey(keyName);
            boolean flag = toolkit.elementInstructions().isElementDisplayed(key);
            blackboard().setData(variabeName, flag);
        } catch (ExecutionException ex) {
            throw new KeyNotFoundException(keyName, ex);
        }
    }

    @Keyword("Validate element is enabled")
    @Parameters(min = 1)
    public void validateElementIsEnabled(String keyName, Boolean selected) {
        if (selected == null)
            selected = true;
        try {
            WebKey key = convertToWebKey(keyName);
            boolean flag = toolkit.elementInstructions().isElementEnabled(key);
            publish(new SetStatusEvent(flag));
            if (flag != selected) {
                String format = "Element %s was %s visible";
                String error = String.format(format, keyName,
                                             selected ? "not" : "");
                log().error(error);
            }
        } catch (ExecutionException ex) {
            throw new KeyNotFoundException(keyName, ex);
        }
    }

    @Keyword("Copy element value")
    @Parameters(min = 2)
    public void copyElementValue(String variableName, String keyName) {
        try {
            WebKey key = convertToWebKey(keyName);
            String text = toolkit.elementInstructions().getElementText(key);
            blackboard().setData(variableName, text);
        } catch (ExecutionException ex) {
            throw new KeyNotFoundException(keyName, ex);
        }
    }

    @Keyword("Validate element value")
    @Parameters(min = 2)
    public void validateElementValue(String keyName, String regex) {
        try {
            WebKey key = convertToWebKey(keyName);
            String text = toolkit.elementInstructions().getElementText(key);
            boolean flag = BrowserLibrary.check(text, regex);
            publish(new SetStatusEvent(flag));
            if (flag == false) {
                String format = "Element %s had the text '%s' that not match regex: %s";
                String error = String.format(format, keyName, text, regex);
                log().error(error);
            }
        } catch (ExecutionException ex) {
            throw new KeyNotFoundException(keyName, ex);
        }
    }
}
