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

import java.io.File;
import org.mazarineblue.eventdriven.Invoker;
import org.mazarineblue.keyworddriven.Keyword;
import org.mazarineblue.keyworddriven.Parameters;
import org.mazarineblue.keyworddriven.PassInvoker;
import org.mazarineblue.libraries.web.browsers.BrowserRegistry;
import static org.mazarineblue.libraries.web.browsers.BrowserRegistry.createBrowser;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class ChromeSetupLibrary
        extends AbstractMutableCapabilitiesLibrary<ChromeOptions> {

    ChromeSetupLibrary(BrowserRegistry manager) {
        super(manager, new ChromeOptions());
    }

    @Keyword("Add arguments")
    @Parameters(min = 1)
    public void addArguments(String... arguments) {
        getOptions().addArguments(arguments);
    }

    @Keyword("Add encoded extensions")
    @Parameters(min = 1)
    public void addEncodedExtensions(String... encoded) {
        getOptions().addEncodedExtensions(encoded);
    }

    @Keyword("Add extensions")
    @Parameters(min = 1)
    public void addExtensions(File... paths) {
        getOptions().addExtensions(paths);
    }

    @Keyword("Set accept insecure certs")
    @Parameters(min = 1)
    public void setAcceptInsecureCerts(boolean flag) {
        getOptions().setAcceptInsecureCerts(flag);
    }

    @Keyword("Set binary")
    @Parameters(min = 1)
    public void setBinary(File path) {
        getOptions().setBinary(path);
    }

    @Keyword("Set experimental option")
    @Parameters(min = 2)
    public void setExperimentalOption(String key, Object value) {
        getOptions().setExperimentalOption(key, value);
    }

    @Keyword("Set headless")
    @Parameters(min = 1)
    public void setHeadless(boolean flag) {
        getOptions().setHeadless(flag);
    }

    @Keyword("Start chrome")
    @Parameters(min = 1)
    @PassInvoker
    public void start(Invoker invoker, String browserName, String initialTabName) {
        getManager().setupChecker().chromeDriverSetup();
        startBrowser(invoker, createBrowser(new ChromeDriver(getOptions()), browserName, initialTabName));
    }
}
