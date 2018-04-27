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

import org.mazarineblue.eventnotifier.Event;
import org.mazarineblue.executors.events.SetFileSystemEvent;
import org.mazarineblue.fs.util.DummyFileSystem;
import org.mazarineblue.utilities.util.TestHashCodeAndEquals;

@SuppressWarnings(value = "PublicInnerClass")
public class SetFileSystemEventTest
        extends TestHashCodeAndEquals<Event> {

    @Override
    protected Event getObject() {
        return new SetFileSystemEvent(null);
    }

    @Override
    protected Event getDifferentObject() {
        return new SetFileSystemEvent(new DummyFileSystem());
    }
}
