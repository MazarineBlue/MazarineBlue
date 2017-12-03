/*
 * Copyright (c) 2015-2016 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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
package org.mazarineblue.executors;

import java.io.File;
import java.util.Arrays;
import static java.util.Arrays.stream;
import java.util.Objects;

class SheetOptions {

    private final File file;
    private final String[] sheets;

    SheetOptions(File file, String... sheets) {
        this.file = file;
        this.sheets = sheets;
    }

    File getFile() {
        return file;
    }

    boolean containsSheet(String sheet) {
        return stream(sheets).anyMatch(s -> s.equals(sheet));
    }

    @Override
    public int hashCode() {
        return 7 * 67 * 67
                + 67 * Objects.hashCode(this.file)
                + Arrays.deepHashCode(this.sheets);
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj != null && getClass() == obj.getClass()
                && Objects.equals(this.file, ((SheetOptions) obj).file)
                && Arrays.deepEquals(this.sheets, ((SheetOptions) obj).sheets);
    }
}
