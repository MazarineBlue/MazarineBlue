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
import org.mazarineblue.fs.DiskFileSystem;
import org.mazarineblue.fs.FileSystem;
import org.mazarineblue.util.FileTransformer;

public final class FeedExecutorBuilder {

    private File baseFile;
    private FileSystem fs;
    private FeedExecutorOutput output;
    private FileTransformer fileTransformer;

    public FeedExecutorBuilder() {
    }

    FeedExecutorBuilder(FeedExecutorBuilder builder) {
        baseFile = builder.getBaseFile();
        fs = builder.getFs();
        output = builder.getOutput();
        fileTransformer = builder.getFileTransformer();
    }

    public File getBaseFile() {
        return baseFile != null ? baseFile : new File("");
    }

    public FileSystem getFs() {
        return fs != null ? fs : new DiskFileSystem();
    }

    public FeedExecutorOutput getOutput() {
        return output != null ? output : FeedExecutorOutput.getDummy();
    }

    public FileTransformer getFileTransformer() {
        return fileTransformer != null ? fileTransformer : FileTransformer.getDefaultInstance();
    }

    public FeedExecutorBuilder setBaseFile(String baseFile) {
        return setBaseFile(new File(baseFile));
    }

    public FeedExecutorBuilder setBaseFile(File baseFile) {
        this.baseFile = baseFile;
        return this;
    }

    public FeedExecutorBuilder setFileSystem(FileSystem fs) {
        this.fs = fs;
        return this;
    }

    public FeedExecutorBuilder setOutput(FeedExecutorOutput output) {
        this.output = output;
        return this;
    }

    public FeedExecutorBuilder setFileTransformer(FileTransformer fileTransformer) {
        this.fileTransformer = fileTransformer;
        return this;
    }
}