/*
 * Copyright (c) 2017 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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
package org.mazarineblue.executors.util;

import java.io.File;
import java.util.Objects;
import org.mazarineblue.util.DateFactory;

class DefaultFileTransformer
        implements FileTransformer {

    private final DateFactory dateFactory;

    DefaultFileTransformer(DateFactory dateFactory) {
        this.dateFactory = dateFactory;
    }

    @Override
    public File getLogfile(File parent, String child) {
        int pos = child.lastIndexOf('.');
        String base = pos < 0 ? "" : child.substring(0, pos);
        String date = dateFactory.getCurrentDate();
        return new File(parent, base + "-log-" + date + ".xml");
    }

    @Override
    public int hashCode() {
        return 7 * 59
                + Objects.hashCode(this.dateFactory);
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj != null && getClass() == obj.getClass()
                && Objects.equals(this.dateFactory, ((DefaultFileTransformer) obj).dateFactory);
    }
}
