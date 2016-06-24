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
package org.mazarineblue.webdriver.keys;

import org.mazarineblue.webdriver.WebKey;
import org.mazarineblue.webdriver.WebKeyValidationException;
import org.mazarineblue.webdriver.exceptions.WebKeyTypeUnsupportedException;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class WebKeyFactory {

    static public enum Type {

        CLASS,
        CSS,
        ID,
        LINK,
        NAME,
        XPATH,
    };

    public WebKey createInstance(String type, String key)
            throws WebKeyTypeUnsupportedException, WebKeyValidationException {
        if (type == null || key == null)
            return null;
        return createInstance(convert(type, key), key);
    }

    private Type convert(String type, String key)
            throws WebKeyTypeUnsupportedException {
        switch (type) {
            case "class":
                return Type.CLASS;
            case "css":
                return Type.CSS;
            case "id":
                return Type.ID;
            case "link":
                return Type.LINK;
            case "name":
                return Type.NAME;
            case "xpath":
                return Type.XPATH;
            default:
                throw new WebKeyTypeUnsupportedException(type, key);
        }
    }

    public final WebKey createInstance(Type type, String key)
            throws WebKeyTypeUnsupportedException, WebKeyValidationException {
        if (key == null)
            return null;
        WebKey obj = helper(type, key);
        if (obj.isValid() == false)
            throw new WebKeyValidationException(obj);
        return obj;
    }

    private WebKey helper(Type type, String key)
            throws WebKeyTypeUnsupportedException {
        switch (type) {
            case CLASS:
                return new WebClassKey(key);
            case CSS:
                return new WebCssKey(key);
            case ID:
                return new WebIdKey(key);
            case LINK:
                return new WebLinkKey(key);
            case NAME:
                return new WebNameKey(key);
            case XPATH:
                return new WebXpathKey(key);
            default:
                throw new WebKeyTypeUnsupportedException(type.name(), key);
        }
    }
}
