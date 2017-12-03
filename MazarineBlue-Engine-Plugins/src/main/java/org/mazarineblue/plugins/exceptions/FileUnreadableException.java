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
package org.mazarineblue.plugins.exceptions;

import java.io.File;

/**
 * A {@code FileNotSupportedException} is thrown by {@link FeedService} by any
 * operations where a {@link File} is used to read from, but is unreadable.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class FileUnreadableException
        extends FileException {

    private static final long serialVersionUID = 1L;

    public FileUnreadableException(File file, Throwable ex) {
        this(file.toString(), ex);
    }

    public FileUnreadableException(String file, Throwable ex) {
        super("File unreadable:", file, ex);
    }
}
