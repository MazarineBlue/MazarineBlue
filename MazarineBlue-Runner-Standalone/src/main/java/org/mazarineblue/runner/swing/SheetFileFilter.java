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
package org.mazarineblue.runner.swing;

import java.io.File;
import javax.swing.filechooser.FileFilter;
import org.mazarineblue.runner.sheetFactorySelector.SheetFactorySelector;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class SheetFileFilter
        extends FileFilter {

    private final SheetFactorySelector sheetFactorySelector;

    public SheetFileFilter(final SheetFactorySelector sheetFactorySelector) {
        this.sheetFactorySelector = sheetFactorySelector;
    }

    @Override
    public boolean accept(File file) {
        if (file == null)
            return true;
        if (file.isDirectory())
            return true;
        if (file.isFile() == false)
            return false;
        if (file.isHidden() == true)
            return false;
        if (file.canRead() == false)
            return false;
        return sheetFactorySelector.getSheetFactory(file) != null;
    }

    @Override
    public String getDescription() {
        return "Filter on sheet files";
    }
}
