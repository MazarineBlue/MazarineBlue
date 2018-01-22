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
import org.mazarineblue.util.DateFactory;

/**
 * A {@code FileTranformat} transform a input filename to an output filename.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public interface FileTransformer {

    public static FileTransformer newInstance() {
        return newInstance(DateFactory.newInstance());
    }

    public static FileTransformer newInstance(DateFactory dateFactory) {
        return new DefaultFileTransformer(dateFactory);
    }

    /**
     * Transforms the specified filename to a filename for the log.
     *
     * @param parent The parent abstract pathname
     * @param child  The child pathname string
     * @return the filename for the log.
     */
    public File getLogfile(File parent, String child);
}
