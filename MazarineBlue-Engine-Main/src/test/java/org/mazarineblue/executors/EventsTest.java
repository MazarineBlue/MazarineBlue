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
import org.mazarineblue.executors.events.CreateFeedExecutorEvent;
import org.mazarineblue.executors.events.EvaluateExpressionEvent;
import org.mazarineblue.executors.events.SetFileSystemEvent;
import org.mazarineblue.fs.MemoryFileSystem;
import org.mazarineblue.parser.expressions.Expression;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
@RunWith(HierarchicalContextRunner.class)
public class EventsTest {

    @Test
    public void test_EvaluateExpressionEvent() {
        EvaluateExpressionEvent e = new EvaluateExpressionEvent(Expression.leaf("a"));
        e.evaluate(t -> 2);
        assertEquals("expression=a, result=2", e.toString());
        assertEquals("expression=a", e.message());
        assertEquals("result=2", e.responce());
    }

    @Test
    public void test_RequestObjectCreationEvent() {
        CreateFeedExecutorEvent<Object> e = new CreateFeedExecutorEvent<>();
        e.setResult(3);
        assertEquals("result=3", e.toString());
        assertEquals("", e.message());
        assertEquals("result=3", e.responce());
    }

    @Test
    public void test_SetFileSystemEvent() {
        SetFileSystemEvent e = new SetFileSystemEvent(new MemoryFileSystem());
        assertEquals("fs=MemoryFileSystem{size = 0}", e.toString());
        assertEquals("fs=org.mazarineblue.fs.MemoryFileSystem{size = 0}", e.message());
        assertEquals("", e.responce());
    }
}
