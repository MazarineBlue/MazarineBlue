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
package org.mazarineblue.executors;

import java.io.File;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import org.junit.Test;
import org.mazarineblue.util.FileTransformer;
import org.mazarineblue.util.TestDateFactory;
import org.mazarineblue.utililities.util.TestHashCodeAndEquals;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class DefaultFileTransformerTest
        extends TestHashCodeAndEquals<FileTransformer> {

    @Test
    public void hashCode_DifferentFileTranformers() {
        int a = FileTransformer.newInstance().hashCode();
        int b = new FileTransformer() {
            @Override
            public File getLogfile(File parent, String child) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        }.hashCode();
        assertNotEquals(a, b);
    }

    @Test
    public void equals_DifferentFileTranformers() {
        FileTransformer a = FileTransformer.newInstance();
        FileTransformer b = (parent, child) -> {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        };
        assertFalse(a.equals(b));
    }

    @Override
    protected FileTransformer getObject() {
        return FileTransformer.newInstance();
    }

    @Override
    protected FileTransformer getDifferentObject() {
        return FileTransformer.newInstance(new TestDateFactory());
    }
}
