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

import java.util.logging.Level;
import java.util.logging.Logger;
import org.mazarineblue.webdriver.Offset;
import org.mazarineblue.webdriver.WebKey;
import org.mazarineblue.webdriver.WebToolkit.MouseInstructions;
import org.mazarineblue.webdriver.exceptions.ExecutionException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
class MouseInstructionImpl
        implements MouseInstructions {

    private final WebDriver driver;
    private final WebElementFetcher fetcher;

    MouseInstructionImpl(WebDriver driver, WebElementFetcher fetcher) {
        this.driver = driver;
        this.fetcher = fetcher;
    }

    @Override
    public void click(WebKey key)
            throws ExecutionException {
        click.execute(key);
    }

    private final RecoverFromStaleElementAction<Void, Object> click = new RecoverFromStaleElementAction<Void, Object>() {

        @Override
        protected Void action(WebKey key, Object... param)
                throws ExecutionException {
            WebElement e = key == null ? null : fetcher.fetchElement(key);
            
            Actions actions = new Actions(driver);
            actions.click(e);
            actions.perform();
            return null;
        }
    };

    @Override
    public void clickAndHold(WebKey key)
            throws ExecutionException {
        clickAndHold.execute(key);
    }

    private final RecoverFromStaleElementAction<Void, Object> clickAndHold = new RecoverFromStaleElementAction<Void, Object>() {

        @Override
        protected Void action(WebKey key, Object... param)
                throws ExecutionException {
            WebElement e = key == null ? null : fetcher.fetchElement(key);
            
            Actions actions = new Actions(driver);
            actions.clickAndHold(e);
            actions.perform();
            return null;
        }
    };

    @Override
    public void contextClick(WebKey key)
            throws ExecutionException {
        contextClick.execute(key);
    }

    private final RecoverFromStaleElementAction<Void, Object> contextClick = new RecoverFromStaleElementAction<Void, Object>() {
        @Override
        protected Void action(WebKey key, Object... param)
                throws ExecutionException {
            WebElement e = key == null ? null : fetcher.fetchElement(key);
            
            Actions actions = new Actions(driver);
            actions.contextClick(e);
            actions.perform();
            return null;
        }
    };

    @Override
    public void dragAndDrop(WebKey from, WebKey to)
            throws ExecutionException {
        dragAndDrop.execute(from, to);
    }

    private final RecoverFromStaleElementAction<Void, Object> dragAndDrop = new RecoverFromStaleElementAction<Void, Object>() {
        @Override
        protected Void action(WebKey key, Object... param)
                throws ExecutionException {
            WebElement from = fetcher.fetchElement(key);
            WebElement to = fetcher.fetchElement((WebKey) param[0]);

            Actions actions = new Actions(driver);
            actions.dragAndDrop(from, to);
            actions.perform();
            return null;
        }
    };

    @Override
    public void dragAndDropByOffset(WebKey from, Offset offset)
            throws ExecutionException {
        dragAndDropByOffset.execute(from, offset);
    }

    private final RecoverFromStaleElementAction<Void, Object> dragAndDropByOffset = new RecoverFromStaleElementAction<Void, Object>() {
        @Override
        protected Void action(WebKey key, Object... param)
                throws ExecutionException {
            WebElement from = fetcher.fetchElement(key);
            Offset offset = (Offset) param[0];

            Actions actions = new Actions(driver);
            actions.dragAndDropBy(from, offset.xOffset(), offset.yOffset());
            actions.perform();
            return null;
        }
    };

    @Override
    public void doubleClick(WebKey key)
            throws ExecutionException {
        doubleClick.execute(key);
    }

    private final RecoverFromStaleElementAction<Void, Object> doubleClick = new RecoverFromStaleElementAction<Void, Object>() {
        @Override
        protected Void action(WebKey key, Object... param)
                throws ExecutionException {
            WebElement e = key == null ? null : fetcher.fetchElement(key);

            Actions actions = new Actions(driver);
            actions.doubleClick(e);
            actions.perform();
            return null;
        }
    };

    @Override
    public void moveByOffset(Offset offset) {
        try {
            moveByOffset.execute(null, offset);
        } catch (ExecutionException ex) {
            Logger.getLogger(MouseInstructionImpl.class.getName()).log(
                    Level.SEVERE, null, ex);
        }
    }

    private final RecoverFromStaleElementAction<Void, Object> moveByOffset = new RecoverFromStaleElementAction<Void, Object>() {
        @Override
        protected Void action(WebKey key, Object... param)
                throws ExecutionException {
            Offset offset = (Offset) param[0];

            Actions actions = new Actions(driver);
            actions.moveByOffset(offset.xOffset(), offset.yOffset());
            actions.perform();
            return null;
        }
    };

    @Override
    public void moveToElement(WebKey key)
            throws ExecutionException {
        moveToElement.execute(key);
    }

    private final RecoverFromStaleElementAction<Void, Object> moveToElement = new RecoverFromStaleElementAction<Void, Object>() {
        @Override
        protected Void action(WebKey key, Object... param)
                throws ExecutionException {
            WebElement e = key == null ? null : fetcher.fetchElement(key);

            Actions actions = new Actions(driver);
            actions.moveToElement(e);
            actions.perform();
            return null;
        }
    };

    @Override
    public void moveToElementByOffset(WebKey key, Offset offset)
            throws ExecutionException {
        moveToElementByOffset.execute(key, offset);
    }

    private final RecoverFromStaleElementAction<Void, Object> moveToElementByOffset = new RecoverFromStaleElementAction<Void, Object>() {
        @Override
        protected Void action(WebKey key, Object... param)
                throws ExecutionException {
            WebElement e = key == null ? null : fetcher.fetchElement(key);
            Offset offset = (Offset) param[0];

            Actions actions = new Actions(driver);
            actions.moveToElement(e);
            actions.moveByOffset(offset.xOffset(), offset.yOffset());
            actions.perform();
            return null;
        }
    };

    @Override
    public void releaseMouse(WebKey key)
            throws ExecutionException {
        releaseMouse.execute(key);
    }

    private final RecoverFromStaleElementAction<Void, Object> releaseMouse = new RecoverFromStaleElementAction<Void, Object>() {
        @Override
        protected Void action(WebKey key, Object... param)
                throws ExecutionException {
            WebElement e = key == null ? null : fetcher.fetchElement(key);

            Actions actions = new Actions(driver);
            actions.release(e);
            actions.perform();
            return null;
        }
    };
}
