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
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * {@code AbstractCapabilitiesLibrary} is a {@code AbstractSetupLibrary} that contains keywords for
 * {@code DesiredCapabilities} capable webdrivers.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public abstract class AbstractDesiredCapabilitiesLibrary
        extends AbstractCapabilitiesLibrary<DesiredCapabilities> {

    public AbstractDesiredCapabilitiesLibrary(BrowserRegistry manager) {
        super(manager, new DesiredCapabilities());
    }

    @Keyword("Set accept insecure certs")
    @Parameters(min = 1)
    public void setAcceptInsecureCerts(boolean flag) {
        getOptions().setAcceptInsecureCerts(flag);
    }

    @Keyword("Set browser name")
    @Parameters(min = 1)
    public AbstractDesiredCapabilitiesLibrary setBrowserName(String name) {
        getOptions().setBrowserName(name);
        return this;
    }

    @Keyword("Set javascript enabled")
    @Parameters(min = 1)
    public AbstractDesiredCapabilitiesLibrary setJavascriptEnabled(boolean flag) {
        getOptions().setJavascriptEnabled(flag);
        return this;
    }

    @Keyword("Set platform")
    @Parameters(min = 1)
    public AbstractDesiredCapabilitiesLibrary setPlatform(String platform) {
        getOptions().setPlatform(getPlatform(platform));
        return this;
    }

    @Keyword("Set version")
    @Parameters(min = 1)
    public AbstractDesiredCapabilitiesLibrary setVersion(String version) {
        getOptions().setVersion(version);
        return this;
    }
}
