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
package org.mazarineblue.webdriver;

import org.mazarineblue.datasources.DataSource;
import org.mazarineblue.webdriver.exceptions.WebKeyTypeUnsupportedException;
import org.mazarineblue.webdriver.keys.WebKeyFactory;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class WebSource {

    static public String type(String name) {
        return name + ".type";
    }

    static public String key(String name) {
        return name + ".key";
    }

    static public String attribute(String name) {
        return name + ".attribute";
    }

    static public String value(String name) {
        return name + ".value";
    }

    @Deprecated
    static public String postfix(String name, String postfix) {
        return name + postfix;
    }

    private DataSource source;
    private final WebKeyFactory factory;

    public WebSource(DataSource source, WebKeyFactory factory) {
        this.source = source;
        this.factory = factory;
    }

    public void setSource(DataSource source) {
        this.source = source;
    }

    public WebKey getKey(String name)
            throws WebKeyTypeUnsupportedException, WebKeyValidationException {
        String type = (String) source.getData(type(name), String.class);
        String key = (String) source.getData(key(name), String.class);
        return factory.createInstance(type, key);
    }

    public String getAttribute(String name) {
        return (String) source.getData(attribute(name), String.class);
    }

    public Object getValue(String name) {
        return source.getData(value(name));
    }

    @Deprecated
    public Object getData(String name, String postfix) {
        return source.getData(postfix(name, postfix));
    }
}
