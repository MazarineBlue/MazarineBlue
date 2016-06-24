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
import org.mazarineblue.webdriver.exceptions.ExecutionException;
import org.openqa.selenium.StaleElementReferenceException;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
abstract class RecoverFromStaleElementAction<Return, Param> {

    private final int retries;

    protected RecoverFromStaleElementAction() {
        this(3);
    }

    protected RecoverFromStaleElementAction(int retries) {
        this.retries = retries;
    }

    public Return execute(WebKey key)
            throws ExecutionException {
        return execute(key, (Param) null);
    }

    public Return execute(WebKey key, Param... param)
            throws ExecutionException {
        int i = retries;
        while (true)
            try {
                return action(key, param);
            } catch (StaleElementReferenceException ex) {
                if (i == 0)
                    throw ex;
                --i;
            } catch (RuntimeException ex) {
                throw ex;
            }
    }

    protected abstract Return action(WebKey key, Param... param)
            throws ExecutionException;
}
