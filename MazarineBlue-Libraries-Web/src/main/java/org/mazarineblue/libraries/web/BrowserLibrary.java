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
import org.mazarineblue.keyworddriven.events.FetchLibrariesEvent;
import org.mazarineblue.keyworddriven.events.RemoveLibraryEvent;
import org.mazarineblue.libraries.web.browsers.BrowserRegistry;
import static org.mazarineblue.libraries.web.browsers.BrowserRegistry.createBrowser;
import static org.mazarineblue.libraries.web.util.WebDriverSetupChecker.SetupCheckOption.ALWAYS;
import static org.mazarineblue.libraries.web.util.WebDriverSetupChecker.SetupCheckOption.NEVER;
import static org.mazarineblue.libraries.web.util.WebDriverSetupChecker.SetupCheckOption.ONCE;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.safari.SafariDriver;

public class BrowserLibrary
        extends AbstractBrowserManagerLibrary {

    BrowserLibrary() {
        super(new BrowserRegistry());
    }

    BrowserLibrary(BrowserRegistry manager) {
        super(manager);
    }

    @Keyword("Run setup checker")
    @Parameters(min = 1)
    public void runWebDriverManager(String option) {
        switch (option) {
            default:
            case "once":
                getManager().checkSetup(ONCE);
                break;
            case "never":
                getManager().checkSetup(NEVER);
                break;
            case "always":
                getManager().checkSetup(ALWAYS);
                break;
        }
    }

    @Keyword("Setup chrome")
    @PassInvoker
    public void setupChrome(Invoker invoker) {
        setupBrowser(invoker, new ChromeSetupLibrary(getManager()));
    }

    @Keyword("Start chrome")
    @Parameters(min = 1)
    @PassInvoker
    public void startChrome(Invoker invoker, String name, String initialTabName) {
        getManager().setupChecker().chromeDriverSetup();
        startBrowser(invoker, createBrowser(new ChromeDriver(), name, initialTabName));
    }

    @Keyword("Setup edge")
    @PassInvoker
    public void setupEdge(Invoker invoker) {
        setupBrowser(invoker, new EdgeSetupLibrary(getManager()));
    }

    @Keyword("Start edge")
    @Parameters(min = 1)
    @PassInvoker
    public void startEdge(Invoker invoker, String name, String initialTabName) {
        getManager().setupChecker().edgeDriverSetup();
        startBrowser(invoker, createBrowser(new EdgeDriver(), name, initialTabName));
    }

    @Keyword("Setup firefox")
    @PassInvoker
    public void setupFirefox(Invoker invoker) {
        setupBrowser(invoker, new FirefoxSetupLibrary(getManager()));
    }

    @Keyword("Start firefox")
    @Parameters(min = 1)
    @PassInvoker
    public void startFirefox(Invoker invoker, String name, String initialTabName) {
        getManager().setupChecker().firefoxDriverSetup();
        startBrowser(invoker, createBrowser(new FirefoxDriver(), name, initialTabName));
    }

    @Keyword("Setup htmlunit")
    @PassInvoker
    public void setupHtmlUnit(Invoker invoker) {
        setupBrowser(invoker, new HtmlUnitSetupLibrary(getManager()).setBrowserName(BrowserType.HTMLUNIT));
    }

    @Keyword("Start htmlunit")
    @Parameters(min = 1)
    @PassInvoker
    public void startHtmlUnit(Invoker invoker, String name, String initialTabName) {
        startBrowser(invoker, createBrowser(new HtmlUnitDriver(), name, initialTabName));
    }

    @Keyword("Setup internet explorer")
    @PassInvoker
    public void setupInternetExplorer(Invoker invoker) {
        setupBrowser(invoker, new InternetExplorerSetupLibrary(getManager()));
    }

    @Keyword("Start internet explorer")
    @Parameters(min = 1)
    @PassInvoker
    public void startInternetExplorer(Invoker invoker, String name, String initialTabName) {
        getManager().setupChecker().ieDriverSetup();
        startBrowser(invoker, createBrowser(new InternetExplorerDriver(), name, initialTabName));
    }

    @Keyword("Setup opera")
    @PassInvoker
    public void setupOperaExplorer(Invoker invoker) {
        setupBrowser(invoker, new OperaSetupLibrary(getManager()));
    }

    @Keyword("Start opera")
    @Parameters(min = 1)
    @PassInvoker
    public void startOpera(Invoker invoker, String name, String initialTabName) {
        getManager().setupChecker().operaDriverSetup();
        startBrowser(invoker, createBrowser(new OperaDriver(), name, initialTabName));
    }

    @Keyword("Setup phantomJS")
    @PassInvoker
    public void setupPhantomJS(Invoker invoker) {
        setupBrowser(invoker, new PhantomJsSetupLibrary(getManager()));
    }

    @Keyword("Start phantomJS")
    @Parameters(min = 1)
    @PassInvoker
    public void startPhantomJS(Invoker invoker, String name, String initialTabName) {
        startBrowser(invoker, createBrowser(new PhantomJSDriver(), name, initialTabName));
    }

    @Keyword("Setup safari")
    @PassInvoker
    public void startSafari(Invoker invoker) {
        setupBrowser(invoker, new SafariSetupLibrary(getManager()));
    }

    @Keyword("Start safari")
    @Parameters(min = 1)
    @PassInvoker
    public void startSafari(Invoker invoker, String name, String initialTabName) {
        startBrowser(invoker, createBrowser(new SafariDriver(), name, initialTabName));
    }

    @Keyword("Switch browser")
    @Parameters(min = 1)
    public void switchBrowser(String name) {
        getManager().switchTo(name);
    }

    @Keyword("Stop browser")
    @PassInvoker
    public void stopBrowser(Invoker invoker, String name) {
        String backup = getManager().currentBrowser().name();
        getManager().switchTo(name);
        getManager().currentBrowser().driver().quit();
        getManager().switchTo(backup);
        getManager().unregiser(name);
        if (getManager().browserCount() == 0)
            removeTabLibrary(invoker);
    }

    private void removeTabLibrary(Invoker invoker) {
        FetchLibrariesEvent e = new FetchLibrariesEvent(lib -> lib.getClass().equals(AbstractBrowserLibrary.class));
        invoker.publish(e);
        e.list().forEach(lib -> invoker.publish(new RemoveLibraryEvent(lib)));
    }
}
