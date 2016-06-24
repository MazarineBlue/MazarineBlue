/*
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

import org.mazarineblue.events.instructions.InstructionLineEvent;
import org.mazarineblue.keyworddriven.Keyword;
import org.mazarineblue.keyworddriven.Parameters;
import org.mazarineblue.keyworddriven.librarymanager.Library;
import org.mazarineblue.keyworddriven.links.LibraryLink;
import org.mazarineblue.webdriver.exceptions.PlatformUnsupported;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
class BrowserProfileLibrary
        extends LibraryLink {

    private final DesiredCapabilities capabilities;

    public BrowserProfileLibrary(String namespace, Library library,
                                 DesiredCapabilities capabilities) {
        super(namespace, library);
        this.capabilities = capabilities;
    }

    @Override
    protected void processNonkeyEvents(InstructionLineEvent event) {
    }

    @Keyword("Browser name")
    @Parameters(min = 1)
    public void setBrowserName(String name) {
        capabilities.setBrowserName(name);
    }

    @Keyword("Version")
    @Parameters(min = 1)
    public void setVersion(String version) {
        capabilities.setVersion(version);
    }

    @Keyword("Platform")
    @Parameters(min = 1)
    public void setPlatform(String platform) {
        Platform result = Platform.fromString(platform);
        if (result != null)
            capabilities.setPlatform(result);
        else
            throw new PlatformUnsupported(platform);
    }

    @Keyword("Enable javascript")
    public void setJavascriptEnable() {
        capabilities.setJavascriptEnabled(true);
    }

    @Keyword("Disable javascript")
    public void setJavascriptDisable() {
        capabilities.setJavascriptEnabled(false);
    }

    @Keyword("End browser profile")
    public void endProfile() {
        executor().chain().remove(this);
    }
}
