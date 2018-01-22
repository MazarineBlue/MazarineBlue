/*
 * Copyright (c) 2016 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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
package org.mazarineblue.keyworddriven.events;

import java.io.Serializable;
import java.util.Objects;
import org.mazarineblue.keyworddriven.Keyword;
import org.mazarineblue.keyworddriven.Library;
import static org.mazarineblue.keyworddriven.util.GracefullConvertor.degraceMethod;
import static org.mazarineblue.keyworddriven.util.GracefullConvertor.degraceNamespace;

/**
 * A {@code Path} is a mapping for a namespace and {@link Keyword}.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 * @see Library#getNamespace()
 * @see Keyword
 */
class Path
        implements Serializable {

    private static final String DELIMITER = ".";
    private static final String NO_NAMESPACE = "";
    private static final long serialVersionUID = 1L;

    private final String namespace;
    private final String keyword;

    Path(String path) {
        namespace = degraceNamespace(determineNamespace(path));
        keyword = degraceMethod(determineKeyword(path));
    }

    private static String determineNamespace(String path) {
        return hasNamespace(path) ? path.substring(0, indexOfDelimiter(path)) : NO_NAMESPACE;
    }

    private static String determineKeyword(String path) {
        return hasNamespace(path) ? path.substring(indexOfKeyword(path)) : path;
    }

    private static boolean hasNamespace(String path) {
        return path == null ? false : path.contains(DELIMITER);
    }

    private static int indexOfDelimiter(String path) {
        return path.lastIndexOf(DELIMITER);
    }

    private static int indexOfKeyword(String path) {
        return path.lastIndexOf(DELIMITER) + DELIMITER.length();
    }

    @Override
    public String toString() {
        return namespace + DELIMITER + keyword;
    }

    String getNamespace() {
        return namespace;
    }

    String getKeyword() {
        return keyword;
    }

    @Override
    public int hashCode() {
        return 7 * 79 * 79
                + 79 * Objects.hashCode(namespace)
                + Objects.hashCode(keyword);
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj != null && getClass() == obj.getClass()
                && Objects.equals(this.namespace, ((Path) obj).namespace)
                && Objects.equals(this.keyword, ((Path) obj).keyword);
    }
}
