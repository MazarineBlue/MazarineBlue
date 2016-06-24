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
import org.mazarineblue.webdriver.WebKey;
import org.mazarineblue.webdriver.exceptions.ExecutionException;
import org.mazarineblue.webdriver.exceptions.WebKeyNotFoundException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
abstract class WebElementFetcher {

    private WebKey key;
    private WebDriverWait waiter;
    private final ExpectedCondition<List<WebElement>> condition;
    private long timeoutInSeconds, sleepInMillis;

    protected WebElementFetcher(WebDriver driver) {
        this(driver, 120);
    }

    protected WebElementFetcher(WebDriver driver, long timeoutInSeconds) {
        this(driver, timeoutInSeconds, 100);
    }

    public WebElementFetcher(WebDriver driver, long timeoutInSeconds,
                             long sleepInMillis) {
        setWebDriver(driver, timeoutInSeconds, sleepInMillis);
        condition = (WebDriver d) -> d.findElements(key.getBy());
    }

    public final void setWebDriver(WebDriver driver) {
        setWebDriver(driver, timeoutInSeconds, sleepInMillis);
    }

    public final void setWebDriver(WebDriver driver, long timeoutInSeconds) {
        setWebDriver(driver, timeoutInSeconds, sleepInMillis);
    }

    public final void setWebDriver(WebDriver driver, long timeoutInSeconds,
                                   long sleepInMillis) {
        waiter = new WebDriverWait(driver, timeoutInSeconds, sleepInMillis);
        this.timeoutInSeconds = timeoutInSeconds;
        this.sleepInMillis = sleepInMillis;
    }

    public WebElement fetchElement(WebKey key)
            throws ExecutionException, WebKeyNotFoundException {
        List<WebElement> list = fetchElements(key);
        if (list.isEmpty())
            throw new WebKeyNotFoundException(
                    "Coudn't find any elements with key: " + key);
        WebElement e = list.get(0);
        action(e);
        return e;
    }

    public List<WebElement> fetchElements(WebKey key)
            throws ExecutionException {
        if (key == null)
            throw new ExecutionException("WebKey must be specified");
        this.key = key;
        return waiter.until(condition);
    }

    protected abstract void action(WebElement e);
}
