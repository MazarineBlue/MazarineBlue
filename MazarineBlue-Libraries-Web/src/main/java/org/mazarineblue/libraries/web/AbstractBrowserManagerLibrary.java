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
import org.mazarineblue.keyworddriven.Library;
import org.mazarineblue.keyworddriven.Parameters;
import org.mazarineblue.keyworddriven.events.AddLibraryEvent;
import org.mazarineblue.keyworddriven.events.RemoveLibraryEvent;
import static org.mazarineblue.libraries.web.WebDriverLibraryPlugin.NAMESPACE;
import org.mazarineblue.libraries.web.browsers.Browser;
import org.mazarineblue.libraries.web.browsers.BrowserRegistry;
import org.mazarineblue.libraries.web.exceptions.BrowserNameConflictException;

/**
 * {@code AbstractBrowserManagerLibrary} is a library that can start
 * and stop browsers.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public abstract class AbstractBrowserManagerLibrary
        extends Library {

    private final BrowserRegistry manager;

    public AbstractBrowserManagerLibrary(BrowserRegistry manager) {
        super(NAMESPACE);
        this.manager = manager;
    }

    protected BrowserRegistry getManager() {
        return manager;
    }

    @Keyword("Browser count")
    public int browserCount() {
        return manager.browserCount();
    }

    protected void setupBrowser(Invoker invoker, AbstractSetupLibrary lib) {
        invoker.publish(new RemoveLibraryEvent(this));
        invoker.publish(new AddLibraryEvent(lib));
    }

    protected void startBrowser(Invoker invoker, Browser browser) {
        try {
            if (getManager().browserCount() == 0)
                registerLibraries(invoker);
            getManager().register(browser);
        } catch (BrowserNameConflictException ex) {
            browser.driver().close();
            throw ex;
        }
    }

    private void registerLibraries(Invoker invoker) {
        invoker.publish(new AddLibraryEvent(new SimpleKeywordsLibrary(getManager())));
        invoker.publish(new AddLibraryEvent(new TabLibrary(getManager())));
    }
}
