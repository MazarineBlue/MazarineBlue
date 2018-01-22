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
import java.io.InputStream;
import java.lang.reflect.Method;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.mazarineblue.eventnotifier.Event;
import org.mazarineblue.executors.events.CallFileSystemMethodEvent;
import org.mazarineblue.executors.events.CreateFeedExecutorEvent;
import org.mazarineblue.executors.events.EvaluateExpressionEvent;
import org.mazarineblue.executors.events.SetFileSystemEvent;
import org.mazarineblue.fs.FileSystem;
import org.mazarineblue.fs.util.DummyFileSystem;
import static org.mazarineblue.parser.expressions.Expression.leaf;

public class CloneTest {

    @Test
    public void clone_RequestObjectCreationEvent() {
        CreateFeedExecutorEvent<Integer> e = new CreateFeedExecutorEvent<>();
        e.setResult(2);
        assertEquals(e, Event.clone(e));
    }

    @Test
    public void clone_EvaluateExpressionEvent() {
        EvaluateExpressionEvent e = new EvaluateExpressionEvent(leaf("2"));
        e.evaluate(null);
        assertEquals(e, Event.clone(e));
    }

    @Test
    public void clone_SetFileSystemEvent() {
        Event e = new SetFileSystemEvent(new DummyFileSystem());
        assertEquals(e, Event.clone(e));
    }

    @Test
    public void clone_CallFileSystemMethodEvent()
            throws NoSuchMethodException {
        Method method = FileSystem.class.getMethod("mkfile", File.class, InputStream.class);
        Event e = new CallFileSystemMethodEvent(method, new File("file"), null);
        assertEquals(e, Event.clone(e));
    }
}
