/*
 * Copyright (c) Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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
package org.mazarineblue.libraries.web;

import org.mazarineblue.keyworddriven.Keyword;
import org.mazarineblue.keyworddriven.Parameters;
import org.mazarineblue.libraries.web.browsers.BrowserRegistry;
import org.openqa.selenium.MutableCapabilities;

/**
 * {@code AbstractMutableCapabilitiesLibrary} is a {@code AbstractCapabilitiesLibrary} that contains keywords for
 * {@code MutableCapabilities} capable webdrivers.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 * @param <T> 
 */
public abstract class AbstractMutableCapabilitiesLibrary<T extends MutableCapabilities>
        extends AbstractCapabilitiesLibrary<T> {

    public AbstractMutableCapabilitiesLibrary(BrowserRegistry manager, T options) {
        super(manager, options);
    }

    @Keyword("Set capability")
    @Parameters(min = 2)
    public AbstractMutableCapabilitiesLibrary<T> setCapability(String key, Object value) {
        getOptions().setCapability(key, value);
        return this;
    }

    @Keyword("Set platform")
    @Parameters(min = 2)
    public AbstractMutableCapabilitiesLibrary<T> setPlatform(String key, String platform) {
        getOptions().setCapability(key, getPlatform(platform));
        return this;
    }
}
