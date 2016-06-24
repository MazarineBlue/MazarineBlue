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
package org.mazarineblue.webdriver.toolkit;

import java.util.List;
import java.util.regex.Pattern;
import org.mazarineblue.webdriver.WebKey;
import org.mazarineblue.webdriver.WebToolkit.ElementInstructions;
import org.mazarineblue.webdriver.exceptions.ExecutionException;
import org.openqa.selenium.WebElement;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
class ElementInstructionImpl
        implements ElementInstructions {

    private final WebElementFetcher fetcher;

    ElementInstructionImpl(WebElementFetcher fetcher) {
        this.fetcher = fetcher;
    }

    @Override
    public void submit(WebKey key)
            throws ExecutionException {
        submit.execute(key);
    }

    private final RecoverFromStaleElementAction<Void, Object> submit = new RecoverFromStaleElementAction() {

        @Override
        protected Void action(WebKey key, Object... parameter)
                throws ExecutionException {
            WebElement e = fetcher.fetchElement(key);
            e.submit();
            return null;
        }
    };

    @Override
    public int getElementCount(WebKey key)
            throws ExecutionException {
        List<WebElement> list = fetcher.fetchElements(key);
        return list.size();
    }

    @Override
    public String getElementAttributeValue(WebKey key, String attribute)
            throws ExecutionException {
        return getElementAttributeValue.execute(key, attribute);
    }

    private final RecoverFromStaleElementAction<String, Object> getElementAttributeValue = new RecoverFromStaleElementAction() {

        @Override
        protected String action(WebKey key, Object... param)
                throws ExecutionException {
            String attribute = (String) param[0];

            WebElement e = fetcher.fetchElement(key);
            String value = e.getAttribute(attribute);
            return value;
        }
    };

    @Override
    public String getElementCssValue(WebKey key, String css)
            throws ExecutionException {
        return getElementCssValue.execute(key, css);
    }

    private final RecoverFromStaleElementAction<String, Object> getElementCssValue = new RecoverFromStaleElementAction() {

        @Override
        protected String action(WebKey key, Object... param)
                throws ExecutionException {
            String css = (String) param[0];

            WebElement e = fetcher.fetchElement(key);
            String value = e.getCssValue(css);
            return value;
        }
    };

    @Override
    public String getElementTageName(WebKey key)
            throws ExecutionException {
        return getElementTagName.execute(key);
    }

    private final RecoverFromStaleElementAction<String, Object> getElementTagName = new RecoverFromStaleElementAction() {

        @Override
        protected String action(WebKey key, Object... param)
                throws ExecutionException {
            Pattern pattern = (Pattern) param[0];

            WebElement e = fetcher.fetchElement(key);
            String value = e.getTagName();
            return value;
        }
    };

    @Override
    public boolean isElementSelected(WebKey key)
            throws ExecutionException {
        return isElementSelected.execute(key);
    }

    private final RecoverFromStaleElementAction<Boolean, Object> isElementSelected = new RecoverFromStaleElementAction() {

        @Override
        protected Boolean action(WebKey key, Object... param)
                throws ExecutionException {
            WebElement e = fetcher.fetchElement(key);
            return e.isSelected();
        }
    };

    @Override
    public boolean isElementDisplayed(WebKey key)
            throws ExecutionException {
        return isElementDisplayed.execute(key);
    }

    private final RecoverFromStaleElementAction<Boolean, Object> isElementDisplayed = new RecoverFromStaleElementAction() {

        @Override
        protected Boolean action(WebKey key, Object... param)
                throws ExecutionException {
            WebElement e = fetcher.fetchElement(key);
            return e.isDisplayed();
        }
    };

    @Override
    public boolean isElementEnabled(WebKey key)
            throws ExecutionException {
        return isElementEnabled.execute(key);
    }

    private final RecoverFromStaleElementAction<Boolean, Object> isElementEnabled = new RecoverFromStaleElementAction() {

        @Override
        protected Boolean action(WebKey key, Object... param)
                throws ExecutionException {
            WebElement e = fetcher.fetchElement(key);
            return e.isEnabled();
        }
    };

    @Override
    public String getElementText(WebKey key)
            throws ExecutionException {
        return getElementText.execute(key);
    }

    private final RecoverFromStaleElementAction<String, Object> getElementText = new RecoverFromStaleElementAction() {

        @Override
        protected String action(WebKey key, Object... param)
                throws ExecutionException {
            WebElement e = fetcher.fetchElement(key);
            String value = e.getText();
            return value;
        }
    };
}
