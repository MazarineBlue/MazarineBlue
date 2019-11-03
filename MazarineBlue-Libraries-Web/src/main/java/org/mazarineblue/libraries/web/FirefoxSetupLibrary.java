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

import java.nio.file.Path;
import org.mazarineblue.eventdriven.Invoker;
import org.mazarineblue.keyworddriven.Keyword;
import org.mazarineblue.keyworddriven.Parameters;
import org.mazarineblue.keyworddriven.PassInvoker;
import org.mazarineblue.libraries.web.browsers.BrowserRegistry;
import static org.mazarineblue.libraries.web.browsers.BrowserRegistry.createBrowser;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class FirefoxSetupLibrary
        extends AbstractMutableCapabilitiesLibrary<FirefoxOptions> {

    FirefoxSetupLibrary(BrowserRegistry manager) {
        super(manager, new FirefoxOptions());
    }

    @Keyword("Add argument")
    @Parameters(min = 1)
    public void addArgument(String... arguments) {
        getOptions().addArguments(arguments);
    }

    @Keyword("Add boolean preference")
    @Parameters(min = 2)
    public void addBooleanPreference(String key, boolean value) {
        addPreference(key, value);
    }

    @Keyword("Add integer preference")
    @Parameters(min = 2)
    public void addIntegerPreference(String key, int value) {
        addPreference(key, value);
    }

    @Keyword("Add preference")
    @Parameters(min = 2)
    public void addPreference(String key, Object value) {
        if (value instanceof Boolean)
            getOptions().addPreference(key, (boolean) value);
        else if (value instanceof Integer)
            getOptions().addPreference(key, (int) value);
        else if (value instanceof String)
            getOptions().addPreference(key, (String) value);
        else
            throw new IllegalArgumentException("Value (" + value + ") must be a boolean, int or String.");
    }

    @Keyword("Set accept insecure certs")
    @Parameters(min = 1)
    public void setAcceptInsecureCerts(boolean flag) {
        getOptions().setAcceptInsecureCerts(flag);
    }

    @Keyword("Set binary")
    @Parameters(min = 1)
    public void setBinary(Path path) {
        getOptions().setBinary(path);
    }

    @Keyword("Set Headless")
    @Parameters(min = 1)
    public void setHeadless(boolean flag) {
        getOptions().setHeadless(flag);
    }

    @Keyword("Start firefox")
    @Parameters(min = 1)
    @PassInvoker
    public void start(Invoker invoker, String browserName, String initialTabName) {
        getManager().setupChecker().firefoxDriverSetup();
        startBrowser(invoker, createBrowser(new FirefoxDriver(getOptions()), browserName, initialTabName));
    }
}
