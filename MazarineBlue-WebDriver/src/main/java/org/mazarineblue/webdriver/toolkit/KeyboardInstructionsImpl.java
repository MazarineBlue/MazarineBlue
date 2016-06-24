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

import org.mazarineblue.webdriver.WebKey;
import org.mazarineblue.webdriver.WebToolkit.KeyboardInstructions;
import org.mazarineblue.webdriver.exceptions.ExecutionException;
import org.mazarineblue.webdriver.exceptions.KeyUnsupportedException;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class KeyboardInstructionsImpl
        implements KeyboardInstructions {

    private final WebElementFetcher fetcher;

    KeyboardInstructionsImpl(WebElementFetcher fetcher) {
        this.fetcher = fetcher;
    }

    @Override
    public void type(WebKey key, CharSequence input, boolean clear)
            throws ExecutionException {
        type.execute(key, input, clear);
    }

    private final RecoverFromStaleElementAction<Void, Object> type = new RecoverFromStaleElementAction() {

        @Override
        protected Void action(WebKey key, Object... parameter)
                throws ExecutionException {
            CharSequence input = (CharSequence) parameter[0];
            Boolean clear = (Boolean) parameter[1];

            WebElement e = fetcher.fetchElement(key);
            if (clear)
                e.clear();
            e.sendKeys(input);
            return null;
        }
    };

    @Override
    public void pressKey(WebKey key, String... input)
            throws ExecutionException {
        presskey.execute(key, input);
    }

    private final RecoverFromStaleElementAction<Void, String> presskey = new RecoverFromStaleElementAction() {

        @Override
        protected Void action(WebKey key, Object... parameter)
                throws ExecutionException {
            WebElement e = fetcher.fetchElement(key);
            for (Object str : parameter)
                e.sendKeys(convertToKeys((String) str));
            return null;
        }

        private Keys convertToKeys(String input)
                throws ExecutionException {
            switch (input.toLowerCase()) {
                case "null":
                    return Keys.NULL;
                case "cancle":
                    return Keys.CANCEL;
                case "help":
                    return Keys.HELP;
                case "back space":
                    return Keys.BACK_SPACE;
                case "tab":
                    return Keys.TAB;
                case "clear":
                    return Keys.CLEAR;
                case "enter":
                case "return":
                    return Keys.RETURN;
                case "shift":
                    return Keys.SHIFT;
                case "left shift":
                    return Keys.LEFT_SHIFT;
                case "control":
                    return Keys.CONTROL;
                case "left control":
                    return Keys.LEFT_CONTROL;
                case "alt":
                    return Keys.ALT;
                case "left alt":
                    return Keys.LEFT_ALT;
                case "pause":
                    return Keys.PAUSE;
                case "escape":
                    return Keys.ESCAPE;
                case "space":
                    return Keys.SPACE;
                case "page up":
                    return Keys.PAGE_UP;
                case "page down":
                    return Keys.PAGE_DOWN;
                case "end":
                    return Keys.END;
                case "home":
                    return Keys.HOME;
                case "left":
                    return Keys.LEFT;
                case "arrow left":
                    return Keys.ARROW_LEFT;
                case "up":
                    return Keys.UP;
                case "arrow up":
                    return Keys.ARROW_UP;
                case "right":
                    return Keys.RIGHT;
                case "arrow right":
                    return Keys.ARROW_RIGHT;
                case "down":
                    return Keys.DOWN;
                case "arrow down":
                    return Keys.ARROW_DOWN;
                case "insert":
                    return Keys.INSERT;
                case "delete":
                    return Keys.DELETE;
                case "semicolun":
                    return Keys.SEMICOLON;
                case "equals0":
                    return Keys.EQUALS;
                case "numpad1":
                    return Keys.NUMPAD1;
                case "numpad2":
                    return Keys.NUMPAD2;
                case "numpad3":
                    return Keys.NUMPAD3;
                case "numpad4":
                    return Keys.NUMPAD4;
                case "numpad5":
                    return Keys.NUMPAD5;
                case "numpad6":
                    return Keys.NUMPAD6;
                case "numpad7":
                    return Keys.NUMPAD7;
                case "numpad8":
                    return Keys.NUMPAD8;
                case "numpad9":
                    return Keys.NUMPAD9;
                case "numpad0":
                    return Keys.NUMPAD0;
                case "multiply":
                    return Keys.MULTIPLY;
                case "add":
                    return Keys.ADD;
                case "separator":
                    return Keys.SEPARATOR;
                case "subtract":
                    return Keys.SUBTRACT;
                case "decimal":
                    return Keys.DECIMAL;
                case "divide":
                    return Keys.DIVIDE;
                case "f1":
                    return Keys.F1;
                case "f2":
                    return Keys.F2;
                case "f3":
                    return Keys.F3;
                case "f4":
                    return Keys.F4;
                case "f5":
                    return Keys.F5;
                case "f6":
                    return Keys.F6;
                case "f7":
                    return Keys.F7;
                case "f8":
                    return Keys.F8;
                case "f9":
                    return Keys.F9;
                case "f10":
                    return Keys.F10;
                case "f11":
                    return Keys.F11;
                case "f12":
                    return Keys.F12;
                case "meta":
                    return Keys.META;
                case "command":
                    return Keys.COMMAND;
                case "zenkaku hankaku":
                    return Keys.ZENKAKU_HANKAKU;
                default:
                    throw new KeyUnsupportedException(
                            "Key not supported: " + input);
            }
        }
    };
}
