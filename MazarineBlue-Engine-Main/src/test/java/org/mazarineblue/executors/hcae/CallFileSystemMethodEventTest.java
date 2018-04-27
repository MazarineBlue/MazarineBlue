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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;
import org.junit.Assert;
import org.junit.Test;
import org.mazarineblue.eventnotifier.Event;
import org.mazarineblue.executors.events.CallFileSystemMethodEvent;
import org.mazarineblue.fs.FileSystem;
import org.mazarineblue.utilities.util.TestHashCodeAndEquals;

@SuppressWarnings(value = "PublicInnerClass")
public class CallFileSystemMethodEventTest
        extends TestHashCodeAndEquals<Event> {

    @Override
    protected Event getObject() {
        try {
            Method method = FileSystem.class.getMethod("mkfile", File.class, InputStream.class);
            return new CallFileSystemMethodEvent(method, new File("file"), null);
        } catch (NoSuchMethodException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    protected Event getDifferentObject() {
        try {
            Method method = FileSystem.class.getMethod("mkfile", File.class, InputStream.class);
            return new CallFileSystemMethodEvent(method, new File("foo"), null);
        } catch (NoSuchMethodException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Test
    public void hashCode_DifferentInputStream() {
        try {
            int a = getObject().hashCode();
            int b = getDifferentInputStream().hashCode();
            Assert.assertNotEquals(a, b);
        } catch (NoSuchMethodException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Test
    public void equals_DifferentInputStream() {
        try {
            Event a = getObject();
            Event b = getDifferentInputStream();
            Assert.assertFalse(a.equals(b));
        } catch (NoSuchMethodException ex) {
            throw new RuntimeException(ex);
        }
    }

    private Event getDifferentInputStream()
            throws NoSuchMethodException {
        Method method = FileSystem.class.getMethod("mkfile", File.class, InputStream.class);
        InputStream input = new ByteArrayInputStream("foo".getBytes());
        return new CallFileSystemMethodEvent(method, new File("foo"), input);
    }
}
