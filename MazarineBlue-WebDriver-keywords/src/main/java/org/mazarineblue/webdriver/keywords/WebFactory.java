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

import java.util.HashMap;
import java.util.Map;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariDriver;

/**
 * This factory created default webdriver and capabilities objects.
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
class WebFactory {

    private final Map<String, Factory> factories = new HashMap();

    private interface Factory {

        public WebDriver createWebDriver(Capabilities capabilities)
                throws UnsupportedProfileException;

        public DesiredCapabilities createCapabilities()
                throws UnsupportedProfileException;
    }

    WebFactory() {
        factories.put("android", new Factory() {
            @Override
            public WebDriver createWebDriver(Capabilities capabilities)
                    throws UnsupportedProfileException {
                throw new UnsupportedProfileException("URL required for android");
            }

            @Override
            public DesiredCapabilities createCapabilities() {
                return DesiredCapabilities.android();
            }
        });
        factories.put("chrome", new Factory() {
            @Override
            public WebDriver createWebDriver(Capabilities capabilities) {
                if (capabilities == null)
                    return new ChromeDriver();
                return new ChromeDriver(capabilities);
            }

            @Override
            public DesiredCapabilities createCapabilities() {
                return DesiredCapabilities.chrome();
            }
        });
        factories.put("ff", new Factory() {
            @Override
            public WebDriver createWebDriver(Capabilities capabilities) {
                if (capabilities == null)
                    return new FirefoxDriver();
                return new FirefoxDriver(capabilities);
            }

            @Override
            public DesiredCapabilities createCapabilities() {
                return DesiredCapabilities.firefox();
            }
        });
        factories.put("firefox", new Factory() {
            @Override
            public WebDriver createWebDriver(Capabilities capabilities) {
                if (capabilities == null)
                    return new FirefoxDriver();
                return new FirefoxDriver(capabilities);
            }

            @Override
            public DesiredCapabilities createCapabilities() {
                return DesiredCapabilities.firefox();
            }
        });
        factories.put("htmlunit", new Factory() {
            @Override
            public WebDriver createWebDriver(Capabilities capabilities) {
                if (capabilities == null)
                    return new HtmlUnitDriver();
                return new HtmlUnitDriver(capabilities);
            }

            @Override
            public DesiredCapabilities createCapabilities() {
                return DesiredCapabilities.htmlUnit();
            }
        });
        factories.put("htmlunit+js", new Factory() {
            @Override
            public WebDriver createWebDriver(Capabilities capabilities) {
                if (capabilities == null)
                    return new HtmlUnitDriver(true);
                return new HtmlUnitDriver(capabilities);
            }

            @Override
            public DesiredCapabilities createCapabilities() {
                return DesiredCapabilities.htmlUnitWithJs();
            }
        });
        factories.put("ie", new Factory() {
            @Override
            public WebDriver createWebDriver(Capabilities capabilities) {
                if (capabilities == null)
                    return new InternetExplorerDriver();
                return new InternetExplorerDriver(capabilities);
            }

            @Override
            public DesiredCapabilities createCapabilities() {
                return DesiredCapabilities.internetExplorer();
            }
        });
        factories.put("internet explorer", new Factory() {
            @Override
            public WebDriver createWebDriver(Capabilities capabilities) {
                if (capabilities == null)
                    return new InternetExplorerDriver(capabilities);
                return new InternetExplorerDriver();
            }

            @Override
            public DesiredCapabilities createCapabilities() {
                return DesiredCapabilities.internetExplorer();
            }
        });
        factories.put("ipad", new Factory() {
            @Override
            public WebDriver createWebDriver(Capabilities capabilities)
                    throws UnsupportedProfileException {
                throw new UnsupportedProfileException("URL required for ipad");
            }

            @Override
            public DesiredCapabilities createCapabilities() {
                return DesiredCapabilities.ipad();
            }
        });
        factories.put("iphone", new Factory() {
            @Override
            public WebDriver createWebDriver(Capabilities capabilities)
                    throws UnsupportedProfileException {
                throw new UnsupportedProfileException("URL required for iphone");
            }

            @Override
            public DesiredCapabilities createCapabilities() {
                return DesiredCapabilities.iphone();
            }
        });
        factories.put("opera", new Factory() {
            @Override
            public WebDriver createWebDriver(Capabilities capabilities)
                    throws UnsupportedProfileException {
                throw new UnsupportedProfileException("URL required for opera");
            }

            @Override
            public DesiredCapabilities createCapabilities() {
                return DesiredCapabilities.opera();
            }
        });
        factories.put("phantomjs", new Factory() {
            @Override
            public WebDriver createWebDriver(Capabilities capabilities)
                    throws UnsupportedProfileException {
                throw new UnsupportedProfileException(
                        "URL required for phantomjs");
            }

            @Override
            public DesiredCapabilities createCapabilities() {
                return DesiredCapabilities.phantomjs();
            }
        });
        factories.put("safari", new Factory() {
            @Override
            public WebDriver createWebDriver(Capabilities capabilities) {
                if (capabilities == null)
                    return new SafariDriver(capabilities);
                return new SafariDriver();
            }

            @Override
            public DesiredCapabilities createCapabilities() {
                return DesiredCapabilities.safari();
            }
        });
    }

    /**
     * Check if a profile with the specified name exists.
     *
     * @param profile the specified name to check
     * @return true if the profile exists.
     */
    public boolean containsProfile(String profile) {
        return factories.containsKey(profile);
    }

    /**
     * Creates a webdriver object for the specified (default) profile.
     *
     * @param profile the profile to create a webdriver object for.
     * @return the created webdriver object.
     * @throws UnsupportedProfileException when the profile is unsupported.
     */
    public WebDriver createWebDriver(String profile)
            throws UnsupportedProfileException {
        return createWebDriver(profile, null);
    }

    /**
     * Creates a webdriver object for the specified (default) profile.
     *
     * @param profile the profile to create a webdriver object for.
     * @param capabilities the desired capabilities of the webdriver object.
     * @return the created webdriver object.
     * @throws UnsupportedProfileException when the profile is unsupported.
     */
    public final WebDriver createWebDriver(String profile,
                                           Capabilities capabilities)
            throws UnsupportedProfileException {
        profile = profile.toLowerCase();
        Factory factory = factories.get(profile);
        if (factory == null)
            throw new UnsupportedProfileException(
                    "Profile unsupported: " + profile);
        return factory.createWebDriver(capabilities);
    }

    /**
     * Creates the standard capabilities for one of the (default) profiles.
     *
     * @param profile the profile to create a capability for.
     * @return the created capabilty.
     * @throws UnsupportedProfileException when the profile is unsupported.
     */
    public DesiredCapabilities createCapabilities(String profile)
            throws UnsupportedProfileException {
        profile = profile.toLowerCase();
        Factory factory = factories.get(profile);
        if (factory == null)
            throw new UnsupportedProfileException(
                    "Profile unsupported: " + profile);
        return factory.createCapabilities();
    }
}
