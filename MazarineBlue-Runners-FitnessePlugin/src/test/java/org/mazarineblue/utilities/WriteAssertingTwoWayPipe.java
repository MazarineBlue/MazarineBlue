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
package org.mazarineblue.utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import static org.junit.Assert.fail;
import org.mazarineblue.utililities.BlockingTwoWayPipe;
import org.mazarineblue.utililities.TwoWayPipe;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class WriteAssertingTwoWayPipe<T>
        implements TwoWayPipe<T> {

    private static final int REQUEST = 0;
    private static final int RESPONCE = 1;

    private final BlockingQueue<T> output;
    private final BlockingQueue<T> input;

    private final List<Tupel<T>> list = new ArrayList<>(16);
    private int index = -1;

    public WriteAssertingTwoWayPipe() {
        this.output = new ArrayBlockingQueue<>(16);
        this.input = new ArrayBlockingQueue<>(16);
    }

    @Override
    public TwoWayPipe<T> redirect() {
        return new BlockingTwoWayPipe<>(output, input);
    }

    @SuppressWarnings("unchecked")
    public void add(T... arr) {
        list.add(new Tupel<>(arr));
    }

    @Override
    public boolean write(T obj, int timeout)
            throws InterruptedException {
        write(obj);
        return true;
    }

    @Override
    public final void write(T obj) {
        try {
            T expected = list.get(++index).get(REQUEST);
            if (!expected.equals(obj)) {
                expected.equals(obj);
                fail("At index " + index + " expected: <" + expected + "> bus was: <" + obj + '>');
            }
        } catch (IndexOutOfBoundsException ex) {
            fail("Expected " + index + " objects, found one more: " + obj);
        }
    }

    @Override
    public <E extends T> E read(Class<E> type, int timeout)
            throws InterruptedException {
        return read(type);
    }

    @Override
    @SuppressWarnings("unchecked")
    public final <E extends T> E read(Class<E> type)
            throws InterruptedException {
        T obj = read();
        if (type.isAssignableFrom(obj.getClass()))
            return (E) obj;
        String err = "Coudn't cast <" + obj.getClass().getName() + "> to <" + type.getName() + '>';
        fail(err);
        throw new ClassCastException(err);
    }

    @Override
    public T read(int timeout)
            throws InterruptedException {
        return read();
    }

    @Override
    public final T read()
            throws InterruptedException {
        Tupel<T> tupel = list.get(index);
        int tupelIndex = hasArgument(tupel, RESPONCE) ? RESPONCE : REQUEST;
        return tupel.get(tupelIndex);
    }

    private boolean hasArgument(Tupel<T> tupel, int index) {
        return tupel.size() > index;
    }

    @Override
    public boolean isReadable() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isWritable() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isReadQueueEmpty() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isWriteQueueEmpty() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isReadQueueFull() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isWriteQueueFull() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int readQueueSize() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int writeQueueSize() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void clear() {
        input.clear();
        output.clear();
    }
}
