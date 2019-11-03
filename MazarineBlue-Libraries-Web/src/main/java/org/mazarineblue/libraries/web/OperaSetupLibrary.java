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
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.opera.OperaOptions;

public class OperaSetupLibrary
        extends AbstractMutableCapabilitiesLibrary<OperaOptions> {

    public OperaSetupLibrary(BrowserRegistry manager) {
        super(manager, new OperaOptions());
    }

    @Keyword("Add argument")
    @Parameters(min = 1)
    public void addArgument(String... arguments) {
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

    @Keyword("Set experimental option")
    @Parameters(min = 1)
    public void setExperimentalOption(String name, Object value) {
        getOptions().setExperimentalOption(name, value);
    }

    @Keyword("Set binary")
    @Parameters(min = 1)
    public void setBinary(File path) {
        getOptions().setBinary(path);
    }

    @Keyword("Start opera")
    @PassInvoker
    @Parameters(min = 1)
    public void start(Invoker invoker, String browserName, String initialTabName) {
        getManager().setupChecker().operaDriverSetup();
        startBrowser(invoker, createBrowser(new OperaDriver(getOptions()), browserName, initialTabName));
    }
}
