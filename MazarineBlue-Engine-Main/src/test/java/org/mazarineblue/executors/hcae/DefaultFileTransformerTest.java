/*
 * Copyright (c) 2018 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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
package org.mazarineblue.executors.hcae;

import org.junit.Assert;
import org.junit.Test;
import org.mazarineblue.executors.util.FileTransformer;
import org.mazarineblue.util.TestDateFactory;
import org.mazarineblue.utilities.util.TestHashCodeAndEquals;

@SuppressWarnings(value = "PublicInnerClass")
public class DefaultFileTransformerTest
        extends TestHashCodeAndEquals<FileTransformer> {

    @Override
    protected FileTransformer getObject() {
        return FileTransformer.newInstance();
    }

    @Override
    protected FileTransformer getDifferentObject() {
        return FileTransformer.newInstance(new TestDateFactory());
    }

    @Test
    public void hashCode_DifferentFileTranformers() {
        int a = FileTransformer.newInstance().hashCode();
        int b = getDifferentFileTransformers().hashCode();
        Assert.assertNotEquals(a, b);
    }

    @Test
    public void equals_DifferentFileTranformers() {
        FileTransformer a = FileTransformer.newInstance();
        FileTransformer b = getDifferentFileTransformers();
        Assert.assertFalse(a.equals(b));
    }

    private FileTransformer getDifferentFileTransformers() {
        return (java.io.File parent, java.lang.String child) -> {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        };
    }
}
