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
package org.mazarineblue.fitnesse;

import java.io.File;
import org.mazarineblue.util.DateFactory;
import org.mazarineblue.util.FileTransformer;

class FitnesseFileTransformer
        implements FileTransformer {

    private final DateFactory dateFactory = DateFactory.getDefaultInstance();

    FitnesseFileTransformer() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public File getLogfile(File parent, String child) {
        return new File(parent, "log-" + dateFactory.getCurrentDate() + ".xml");
    }
}
