/*
 * Copyright (c) 2016 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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
package org.mazarineblue.utililities;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mazarineblue.utililities.util.TestThread;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
@RunWith(HierarchicalContextRunner.class)
@SuppressWarnings("UtilityClassWithoutPrivateConstructor")
public class BlockingTwoWayPipeTest {

    private static final int TIMEOUT = 2000;
    private static final int DOZE = 50;

    @SuppressWarnings("PublicInnerClass")
    public class Factory {

        @Test
        public void testFactory() {
            BlockingTwoWayPipeFactory<Object> factory = new BlockingTwoWayPipeFactory<>();
            assertNotEquals(null, factory.createPipe(1));
            assertEquals(BlockingTwoWayPipe.class, factory.createPipe(1).getClass());
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class DoubleQueue {

        private final int capacity = 2;
        private BlockingQueue<Number> input, output;
        private TwoWayPipe<Number> pipe, redirect;

        @Before
        public void setup() {
            input = new ArrayBlockingQueue<>(capacity);
            output = new ArrayBlockingQueue<>(capacity);
            pipe = new BlockingTwoWayPipe<>(input, output);
            redirect = pipe.redirect();
        }

        @After
        public void teardown() {
            while (!input.isEmpty())
                input.poll();
            while (!output.isEmpty())
                output.poll();
            input = output = null;
            pipe = redirect = null;
        }

        @Test
        public void test_BothQueues_Empty() {
            assertEquals(0, pipe.readQueueSize());
            assertEquals(0, pipe.writeQueueSize());
            assertEquals(0, redirect.readQueueSize());
            assertEquals(0, redirect.writeQueueSize());

            assertFalse(pipe.isReadable());
            assertTrue(pipe.isWritable());
            assertTrue(pipe.isReadQueueEmpty());
            assertFalse(pipe.isReadQueueFull());
            assertTrue(pipe.isWriteQueueEmpty());
            assertFalse(pipe.isWriteQueueFull());

            assertFalse(redirect.isReadable());
            assertTrue(redirect.isWritable());
            assertTrue(redirect.isReadQueueEmpty());
            assertFalse(redirect.isReadQueueFull());
            assertTrue(redirect.isWriteQueueEmpty());
            assertFalse(redirect.isWriteQueueFull());
        }

        @Test
        public void test_InputQueue_HalfFull()
                throws InterruptedException {
            input.put(1);
            assertEquals(1, pipe.readQueueSize());
            assertEquals(0, pipe.writeQueueSize());
            assertEquals(0, redirect.readQueueSize());
            assertEquals(1, redirect.writeQueueSize());

            assertTrue(pipe.isReadable());
            assertTrue(pipe.isWritable());
            assertFalse(pipe.isReadQueueEmpty());
            assertFalse(pipe.isReadQueueFull());
            assertTrue(pipe.isWriteQueueEmpty());
            assertFalse(pipe.isWriteQueueFull());

            assertFalse(redirect.isReadable());
            assertTrue(redirect.isWritable());
            assertTrue(redirect.isReadQueueEmpty());
            assertFalse(redirect.isReadQueueFull());
            assertFalse(redirect.isWriteQueueEmpty());
            assertFalse(redirect.isWriteQueueFull());
        }

        @Test
        public void test_InputQueue_Full()
                throws InterruptedException {
            for (int i = 1; i <= capacity; ++i)
                input.put(i);
            assertEquals(capacity, pipe.readQueueSize());
            assertEquals(0, pipe.writeQueueSize());
            assertEquals(0, redirect.readQueueSize());
            assertEquals(capacity, redirect.writeQueueSize());

            assertTrue(pipe.isReadable());
            assertTrue(pipe.isWritable());
            assertFalse(pipe.isReadQueueEmpty());
            assertTrue(pipe.isReadQueueFull());
            assertTrue(pipe.isWriteQueueEmpty());
            assertFalse(pipe.isWriteQueueFull());

            assertFalse(redirect.isReadable());
            assertFalse(redirect.isWritable());
            assertTrue(redirect.isReadQueueEmpty());
            assertFalse(redirect.isReadQueueFull());
            assertFalse(redirect.isWriteQueueEmpty());
            assertTrue(redirect.isWriteQueueFull());
        }

        @Test
        public void test_OutputQueue_HalfFull()
                throws InterruptedException {
            output.put(1);
            assertEquals(0, pipe.readQueueSize());
            assertEquals(1, pipe.writeQueueSize());
            assertEquals(1, redirect.readQueueSize());
            assertEquals(0, redirect.writeQueueSize());

            assertFalse(pipe.isReadable());
            assertTrue(pipe.isWritable());
            assertTrue(pipe.isReadQueueEmpty());
            assertFalse(pipe.isReadQueueFull());
            assertFalse(pipe.isWriteQueueEmpty());
            assertFalse(pipe.isWriteQueueFull());

            assertTrue(redirect.isReadable());
            assertTrue(redirect.isWritable());
            assertFalse(redirect.isReadQueueEmpty());
            assertFalse(redirect.isReadQueueFull());
            assertTrue(redirect.isWriteQueueEmpty());
            assertFalse(redirect.isWriteQueueFull());
        }

        @Test
        public void test_OutputQueue_Full()
                throws InterruptedException {
            for (int i = 1; i <= capacity; ++i)
                output.put(i);
            assertEquals(0, pipe.readQueueSize());
            assertEquals(capacity, pipe.writeQueueSize());
            assertEquals(capacity, redirect.readQueueSize());
            assertEquals(0, redirect.writeQueueSize());

            assertFalse(pipe.isReadable());
            assertFalse(pipe.isWritable());
            assertTrue(pipe.isReadQueueEmpty());
            assertFalse(pipe.isReadQueueFull());
            assertFalse(pipe.isWriteQueueEmpty());
            assertTrue(pipe.isWriteQueueFull());

            assertTrue(redirect.isReadable());
            assertTrue(redirect.isWritable());
            assertFalse(redirect.isReadQueueEmpty());
            assertTrue(redirect.isReadQueueFull());
            assertTrue(redirect.isWriteQueueEmpty());
            assertFalse(redirect.isWriteQueueFull());
        }

        @Test
        public void test_BothQueue_Full()
                throws InterruptedException {
            for (int i = 1; i <= capacity; ++i) {
                input.add(i);
                output.put(i);
            }
            assertEquals(capacity, pipe.readQueueSize());
            assertEquals(capacity, pipe.writeQueueSize());
            assertEquals(capacity, redirect.readQueueSize());
            assertEquals(capacity, redirect.writeQueueSize());

            assertTrue(pipe.isReadable());
            assertFalse(pipe.isWritable());
            assertFalse(pipe.isReadQueueEmpty());
            assertTrue(pipe.isReadQueueFull());
            assertFalse(pipe.isWriteQueueEmpty());
            assertTrue(pipe.isWriteQueueFull());

            assertTrue(redirect.isReadable());
            assertFalse(redirect.isWritable());
            assertFalse(redirect.isReadQueueEmpty());
            assertTrue(redirect.isReadQueueFull());
            assertFalse(redirect.isWriteQueueEmpty());
            assertTrue(redirect.isWriteQueueFull());
        }

        @Test
        public void test_Clear()
                throws InterruptedException {
            for (int i = 1; i <= capacity; ++i) {
                input.add(i);
                output.put(i);
            }
            pipe.clear();

            assertEquals(0, pipe.readQueueSize());
            assertEquals(0, pipe.writeQueueSize());
            assertEquals(0, redirect.readQueueSize());
            assertEquals(0, redirect.writeQueueSize());

            assertFalse(pipe.isReadable());
            assertTrue(pipe.isWritable());
            assertTrue(pipe.isReadQueueEmpty());
            assertFalse(pipe.isReadQueueFull());
            assertTrue(pipe.isWriteQueueEmpty());
            assertFalse(pipe.isWriteQueueFull());

            assertFalse(redirect.isReadable());
            assertTrue(redirect.isWritable());
            assertTrue(redirect.isReadQueueEmpty());
            assertFalse(redirect.isReadQueueFull());
            assertTrue(redirect.isWriteQueueEmpty());
            assertFalse(redirect.isWriteQueueFull());
        }

        @Test(timeout = TIMEOUT)
        public void write_CapacityAmount_Succes()
                throws InterruptedException {
            for (int i = 0; i < capacity; ++i)
                pipe.write(i);
            for (int i = 0; i < capacity; ++i)
                assertEquals(i, redirect.read());
        }

        @Test(expected = InterruptedException.class, timeout = TIMEOUT)
        public void write_CapacityAmountPlusOne_Blocks()
                throws Throwable {
            for (int i = 0; i < capacity; ++i)
                pipe.write(i);
            TestThread thread = new TestThread(DOZE, () -> pipe.write(capacity));
            thread.start();
            thread.waitTillStarted(DOZE);
            thread.interrupt();
            thread.waitForThrowable();
            throw thread.getCause();
        }

        @Test(timeout = TIMEOUT)
        public void write_CapacityAmountPlusOne_Timeout()
                throws InterruptedException {
            for (int i = 0; i < capacity; ++i)
                pipe.write(i);
            TestThread thread = new TestThread(DOZE, () -> pipe.write(capacity, DOZE));
            thread.start();
            thread.waitTillStopped();
        }

        @Test(expected = InterruptedException.class, timeout = TIMEOUT)
        public void read_EmptyPipe_Blocks()
                throws Throwable {
            TestThread thread = new TestThread(DOZE, () -> pipe.read());
            thread.start();
            thread.waitTillStarted(DOZE);
            thread.interrupt();
            thread.waitForThrowable();
            throw thread.getCause();
        }

        @Test(timeout = TIMEOUT)
        public void read_EmptyPipe_Timeout()
                throws InterruptedException {
            TestThread thread = new TestThread(DOZE, () -> pipe.read(DOZE));
            thread.start();
            thread.waitTillStopped();
        }

        @Test(timeout = TIMEOUT)
        public void read_IntegerIn_SameIntegerOut()
                throws InterruptedException {
            Integer value = 12;
            pipe.write(value);
            assertEquals(value, redirect.read(Number.class));
        }

        @Test(timeout = 100, expected = ClassCastException.class)
        public void read_IntegerIn_FloatOut_ThrowsException()
                throws InterruptedException {
            Integer value = 12;
            pipe.write(value);
            assertEquals(value, redirect.read(Float.class));
        }
    }
}
