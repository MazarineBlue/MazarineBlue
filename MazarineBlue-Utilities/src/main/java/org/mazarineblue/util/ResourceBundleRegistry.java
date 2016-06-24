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
package org.mazarineblue.util;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class ResourceBundleRegistry {

    static private volatile ResourceBundleRegistry singleton = null;

    static ResourceBundleRegistry getInstance() {
        ResourceBundleRegistry avoidAcess = singleton;
        if (avoidAcess == null)
            synchronized (ResourceBundleRegistry.class) {
                avoidAcess = singleton;
                if (avoidAcess == null)
                    avoidAcess = singleton = new ResourceBundleRegistry();
            }
        return avoidAcess;
    }

    public static String getString(String resourceBundle, String key) {
        ResourceBundleRegistry registry = getInstance();
        ResourceBundle bundle = registry.getResourceBundle(resourceBundle);
        return bundle.getString(key);
    }

    private Locale locale;

    private ResourceBundleRegistry() {
        this(null);
    }

    private ResourceBundleRegistry(Locale locale) {
        this.locale = locale;
    }

    public void setLocale(Locale locale) {
        if (this.locale.equals(locale))
            return;
        synchronized (this) {
            this.locale = locale;
        }
    }

    public ResourceBundle getResourceBundle(String name) {
        return locale == null
                ? ResourceBundle.getBundle(name)
                : ResourceBundle.getBundle(name, locale);
    }
}
