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

import org.mazarineblue.libraries.web.browsers.BrowserRegistry;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Platform;

/**
 * {@code AbstractCapabilitiesLibrary} is a {@code AbstractSetupLibrary} that contains keywords for {@code Capabilities}
 * capable webdrivers.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 * @param <T> 
 */
public abstract class AbstractCapabilitiesLibrary<T extends Capabilities>
        extends AbstractSetupLibrary {

    public static Platform getPlatform(String platform) {
        switch(platform.trim().toLowerCase()) {
            case "android": return Platform.ANDROID;
            default:
            case "any": return Platform.ANY;
            case "el capitan": return Platform.EL_CAPITAN;
            case "high sierra": return Platform.HIGH_SIERRA;
            case "ios": return Platform.IOS;
            case "linux": return Platform.LINUX;
            case "mac": return Platform.MAC;
            case "mavericks": return Platform.MAVERICKS;
            case "mojave": return Platform.MOJAVE;
            case "mountain lion": return Platform.MOUNTAIN_LION;
            case "sierra": return Platform.SIERRA;
            case "snow leopard": return Platform.SNOW_LEOPARD;
            case "unix": return Platform.UNIX;
            case "vista": return Platform.VISTA;
            case "windows": return Platform.WINDOWS;
            case "windows 10": return Platform.WIN10;
            case "windows 8": return Platform.WIN8;
            case "windows 8.1": return Platform.WIN8_1;
            case "windows xp": return Platform.XP;
            case "yosemite": return Platform.YOSEMITE;
        }
    }

    private final T options;

    public AbstractCapabilitiesLibrary(BrowserRegistry manager, T options) {
        super(manager);
        this.options = options;
    }

    protected T getOptions() {
        return options;
    }
}
