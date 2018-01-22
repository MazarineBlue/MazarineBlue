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
package org.mazarineblue.executors.events;

import java.util.Objects;
import org.mazarineblue.eventnotifier.events.AbstractEvent;
import org.mazarineblue.fs.FileSystem;
import org.mazarineblue.utilities.SerializableClonable;

/**
 * A {@code SetFileSystemEvent} is a request to change the {@code FileSystem}.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class SetFileSystemEvent
        extends AbstractEvent {

    private static final long serialVersionUID = 1L;

    private transient FileSystem fs;

    public SetFileSystemEvent(FileSystem fs) {
        this.fs = fs;
    }

    @Override
    public boolean isAutoConsumable() {
        return true;
    }

    @Override
    public String toString() {
        return "fs=" + fs.getClass().getSimpleName() + '{' + fs + '}';
    }

    @Override
    public String message() {
        return "fs=" + fs.getClass().getName() + '{' + fs + '}';
    }

    public FileSystem getFileSystem() {
        return fs;
    }

    @Override
    public int hashCode() {
        return 3 * 53
                + Objects.hashCode(this.fs);

    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj != null && getClass() == obj.getClass()
                && Objects.equals(this.fs, ((SetFileSystemEvent) obj).fs);
    }

    @Override
    public <E extends SerializableClonable> void copyTransient(E other) {
        super.copyTransient(other);
        fs = ((SetFileSystemEvent) other).fs;
    }
}
