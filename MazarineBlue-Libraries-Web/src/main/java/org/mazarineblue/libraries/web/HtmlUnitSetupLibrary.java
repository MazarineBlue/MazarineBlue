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
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

public class HtmlUnitSetupLibrary
        extends AbstractDesiredCapabilitiesLibrary {

    public HtmlUnitSetupLibrary(BrowserRegistry manager) {
        super(manager);
    }

    @Keyword("Start htmlunit")
    @PassInvoker
    @Parameters(min = 1)
    public void start(Invoker invoker, String browserName, String initialTabName) {
        startBrowser(invoker, createBrowser(new HtmlUnitDriver(getOptions()), browserName, initialTabName));
    }
}
