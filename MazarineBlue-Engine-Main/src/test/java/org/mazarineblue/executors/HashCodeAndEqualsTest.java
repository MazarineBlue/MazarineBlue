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
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mazarineblue.eventnotifier.Event;
import org.mazarineblue.executors.events.CallFileSystemMethodEvent;
import org.mazarineblue.executors.events.CreateFeedExecutorEvent;
import org.mazarineblue.executors.events.EvaluateExpressionEvent;
import org.mazarineblue.executors.events.SetFileSystemEvent;
import org.mazarineblue.executors.subscribers.DefaulExecutorFactorySubscriber;
import org.mazarineblue.executors.util.FileTransformer;
import org.mazarineblue.fs.FileSystem;
import org.mazarineblue.fs.util.DummyFileSystem;
import static org.mazarineblue.parser.expressions.Expression.leaf;
import org.mazarineblue.util.TestDateFactory;
import org.mazarineblue.utilities.util.TestHashCodeAndEquals;

@RunWith(HierarchicalContextRunner.class)
public class HashCodeAndEqualsTest {

    @SuppressWarnings("PublicInnerClass")
    public class DefaulExecutorFactorySubscriberHCAE
            extends TestHashCodeAndEquals<DefaulExecutorFactorySubscriber> {

        @Override
        protected DefaulExecutorFactorySubscriber getObject() {
            return new DefaulExecutorFactorySubscriber(null);
        }

        @Override
        protected DefaulExecutorFactorySubscriber getDifferentObject() {
            ExecutorBuilder builder = new ExecutorBuilder();
            ExecutorFactory factory = new DefaultExecutorFactory(builder);
            return new DefaulExecutorFactorySubscriber(factory);
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class RequestObjectCreationEventHCAE
            extends TestHashCodeAndEquals<Event> {

        @Override
        protected Event getObject() {
            CreateFeedExecutorEvent<Integer> e = new CreateFeedExecutorEvent<>();
            e.setResult(1);
            return e;
        }

        @Override
        protected Event getDifferentObject() {
            CreateFeedExecutorEvent<Integer> e = new CreateFeedExecutorEvent<>();
            e.setResult(2);
            return e;
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class SheetOptionsHCAE
            extends TestHashCodeAndEquals<SheetOptions> {

        @Override
        protected SheetOptions getObject() {
            return new SheetOptions(new File("foo"), "foo");
        }

        @Override
        protected SheetOptions getDifferentObject() {
            return new SheetOptions(new File("oof"), "foo");
        }

        @Test
        public void hashCode_DifferentSheets() {
            int a = getObject().hashCode();
            int b = getDifferentSheets().hashCode();
                assertNotEquals(a, b);
        }

        @Test
        public void equals_DifferentSheets() {
            Object a = getObject();
            Object b = getDifferentSheets();
            assertFalse(a.equals(b));
        }

        private SheetOptions getDifferentSheets() {
            return new SheetOptions(new File("foo"), "foo", "foo");
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class DefaultFileTransformerHCAE
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
            assertNotEquals(a, b);
        }

        @Test
        public void equals_DifferentFileTranformers() {
            FileTransformer a = FileTransformer.newInstance();
            FileTransformer b = getDifferentFileTransformers();
            assertFalse(a.equals(b));
        }

        private FileTransformer getDifferentFileTransformers() {
            return (parent, child) -> {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            };
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class DummyFeedExecutorOutputTest
            extends TestHashCodeAndEquals<Object> {

        @Override
        protected Object getObject() {
            return new DummyExecutorListener();
        }

        @Override
        protected Object getDifferentObject() {
            return "foo";
        }
    }

    /* ********************************************************************** *
     *                               Libraries                                *
     * ********************************************************* ,,^..^,, *** */

    /* ********************************************************************** *
     *                                 Events                                 *
     * ********************************************************* ,,^..^,, *** */
    @SuppressWarnings("PublicInnerClass")
    public class EvaluateExpressionEventHCAE
            extends TestHashCodeAndEquals<Event> {

        @Override
        protected Event getObject() {
            EvaluateExpressionEvent e = new EvaluateExpressionEvent(leaf("1"));
            e.evaluate(null);
            return e;
        }

        @Override
        protected Event getDifferentObject() {
            EvaluateExpressionEvent e = new EvaluateExpressionEvent(leaf("2"));
            e.evaluate(null);
            return e;
        }

        public void hashCode_DifferentExpressions() {
            int a = new EvaluateExpressionEvent(leaf("1")).hashCode();
            int b = new EvaluateExpressionEvent(leaf("2")).hashCode();
            assertNotEquals(a, b);
        }

        @Test
        public void equals_DifferentExpressions() {
            Event a = new EvaluateExpressionEvent(leaf("1"));
            Event b = new EvaluateExpressionEvent(leaf("2"));
            assertFalse(a.equals(b));
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class SetFileSystemEventHCAE
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

    @SuppressWarnings("PublicInnerClass")
    public class CallFileSystemMethodEventHCAE
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
                assertNotEquals(a, b);
            } catch (NoSuchMethodException ex) {
                throw new RuntimeException(ex);
            }
        }

        @Test
        public void equals_DifferentInputStream() {
            try {
                Event a = getObject();
                Event b = getDifferentInputStream();
                assertFalse(a.equals(b));
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
}
