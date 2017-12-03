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

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mazarineblue.executors.events.SetFileSystemEvent;
import org.mazarineblue.fs.DiskFileSystem;
import org.mazarineblue.fs.MemoryFileSystem;
import org.mazarineblue.utililities.util.TestHashCodeAndEquals;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
@RunWith(HierarchicalContextRunner.class)
public class EventsTest {

    @SuppressWarnings("PublicInnerClass")
    public class TestSetFileSystemEvent
            extends TestHashCodeAndEquals<SetFileSystemEvent> {

        @Test
        public void toString_() {
            SetFileSystemEvent e = new SetFileSystemEvent(new MemoryFileSystem());
            assertEquals("fs=MemoryFileSystem{size = 0}", e.toString());
        }

        @Override
        protected SetFileSystemEvent getObject() {
            return new SetFileSystemEvent(new MemoryFileSystem());
        }

        @Override
        protected SetFileSystemEvent getDifferentObject() {
            return new SetFileSystemEvent(new DiskFileSystem());
        }
    }
}
