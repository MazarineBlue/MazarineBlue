/*
 * Copyright (c) 2012-2014 Alex de Kruijff
 * Copyright (c) 2014-2015 Specialisterren
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

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.mazarineblue.keyworddriven.Keyword;
import org.mazarineblue.keyworddriven.Parameters;
import org.mazarineblue.keyworddriven.exceptions.MalformedURLException;
import org.mazarineblue.keyworddriven.librarymanager.Library;
import org.mazarineblue.webdriver.WebToolkit;
import org.mazarineblue.webdriver.exceptions.BrowserCloseException;
import org.mazarineblue.webdriver.exceptions.BrowserUnsupported;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class WebDriverLibrary
        extends Library {

    private final Map<String, Capabilities> profiles = new HashMap<>();
    private final Map<String, WebToolkit> browsers = new HashMap<>(1);
    private final WebFactory factory = new WebFactory();
    private String activeName;
    private BrowserLibrary activeLibrary;

    public WebDriverLibrary() {
        super("org.mazarineblue.webdriver");
    }

    @Override
    protected void setup() {
    }

    @Override
    protected void teardown() {
    }

    @Keyword("Browser profile")
    @Parameters(min = 1)
    public void profile(String name, String browser) {
        try {
            DesiredCapabilities capabilities = browser == null
                    ? new DesiredCapabilities()
                    : factory.createCapabilities(browser);
            profiles.put(name, capabilities);
            executor().chain().insert(new BrowserProfileLibrary(getNamespace(),
                                                                this,
                                                                capabilities));
        } catch (UnsupportedProfileException ex) {
            throw new BrowserUnsupported(browser, ex);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Browser Instructions">
    @Keyword("Open browser")
    @Parameters(min = 2)
    public void openBrowser(String name, String profile, String url) {
        WebDriver driver = (url == null)
                ? createLocal(profile)
                : createRemote(url, profile);
        browsers.put(name, new WebToolkit(driver));
    }

    private WebDriver createLocal(String profile) {
        try {
            Capabilities capabilities = profiles.get(profile);
            return factory.createWebDriver(profile, capabilities);
        } catch (UnsupportedProfileException ex) {
            throw new BrowserUnsupported(profile, ex);
        }
    }

    private WebDriver createRemote(String url, String profile) {
        try {
            Capabilities capabilities = profiles.containsKey(profile)
                    ? profiles.get(profile)
                    : factory.createCapabilities(profile);
            return new RemoteWebDriver(new URL(url), capabilities);
        } catch (UnsupportedProfileException ex) {
            throw new BrowserUnsupported(profile, ex);
        } catch (java.net.MalformedURLException ex) {
            throw new MalformedURLException(url, ex);
        }
    }

    @Keyword("Select browser")
    @Parameters(min = 1)
    public void selectBrowser(String name) {
        WebToolkit toolkit = browsers.get(name);
        activeName = name;
        if (activeLibrary == null) {
            activeLibrary = new BrowserLibrary(this, toolkit);
            executor().libraries().register(activeLibrary);
        }
        activeLibrary.switchTo(toolkit);
    }

    @Keyword("Close browser")
    public void closeBrowser(String name) {
        try (WebToolkit kill = browsers.get(name)) {
            browsers.remove(name);
            if (activeLibrary != null && activeLibrary.selected(kill)) {
                activeLibrary.close();
                executor().libraries().unregister(activeLibrary);
                activeName = null;
                activeLibrary = null;
            }
        } catch (Exception ex) {
            throw new BrowserCloseException();
        }
    }
    // </editor-fold>
}
