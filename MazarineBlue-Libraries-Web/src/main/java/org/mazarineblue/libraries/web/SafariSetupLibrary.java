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

import org.mazarineblue.eventdriven.Invoker;
import org.mazarineblue.keyworddriven.Keyword;
import org.mazarineblue.keyworddriven.Parameters;
import org.mazarineblue.keyworddriven.PassInvoker;
import org.mazarineblue.libraries.web.browsers.BrowserRegistry;
import static org.mazarineblue.libraries.web.browsers.BrowserRegistry.createBrowser;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

public class SafariSetupLibrary
        extends AbstractMutableCapabilitiesLibrary<SafariOptions> {

    public SafariSetupLibrary(BrowserRegistry manager) {
        super(manager, new SafariOptions());
    }

    @Keyword("Set automatic inspection")
    public SafariSetupLibrary setAutomaticInspection(boolean flag) {
        getOptions().setAutomaticInspection(flag);
        return this;
    }

    @Keyword("Set automatic profiling")
    public SafariSetupLibrary setAutomaticProfiling(boolean flag) {
        getOptions().setAutomaticProfiling(flag);
        return this;
    }

    @Keyword("Set use technology preview")
    public SafariSetupLibrary setUseTechnologyPreview(boolean flag) {
        getOptions().setUseTechnologyPreview(flag);
        return this;
    }

    @Keyword("Start safari")
    @PassInvoker
    @Parameters(min = 1)
    public void start(Invoker invoker, String browserName, String initialTabName) {
        getManager().setupChecker().edgeDriverSetup();
        startBrowser(invoker, createBrowser(new SafariDriver(getOptions()), browserName, initialTabName));
    }
}
