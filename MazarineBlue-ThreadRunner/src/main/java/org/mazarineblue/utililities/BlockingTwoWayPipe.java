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

import java.util.concurrent.BlockingQueue;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * A {@code BlockingTwoWayPipe} is {@code TwoWayPipe} that allows for
 * communication between different processes though the use of
 * {@link BlockingQueue} allowing communications between treads.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 * @param <T> the type of object to transmit.
 */
public class BlockingTwoWayPipe<T>
        implements TwoWayPipe<T> {

    protected final BlockingQueue<T> input;
    protected final BlockingQueue<T> output;

    /**
     * Constructs a {@code BlockingTwoWayPipe} though the use of the specified
     * input and output {@code BlockingQueue}.
     *
     * @param input  the queue to use for receiving messages.
     * @param output the queue to use for transmitting messages.
     */
    public BlockingTwoWayPipe(BlockingQueue<T> input, BlockingQueue<T> output) {
        this.input = input;
        this.output = output;
    }

    @Override
    public String toString() {
        return "input = " + input.size() + ", output = " + output.size();
    }

    @Override
    public TwoWayPipe<T> redirect() {
        return new BlockingTwoWayPipe<>(output, input);
    }

    @Override
    public boolean isWritable() {
        return output.remainingCapacity() != 0;
    }

    @Override
    public boolean isReadQueueEmpty() {
        return input.isEmpty();
    }

    @Override
    public boolean isWriteQueueEmpty() {
        return output.isEmpty();
    }

    @Override
    public boolean isReadQueueFull() {
        return input.remainingCapacity() == 0;
    }

    @Override
    public boolean isWriteQueueFull() {
        return output.remainingCapacity() == 0;
    }

    @Override
    public int readQueueSize() {
        return input.size();
    }

    @Override
    public int writeQueueSize() {
        return output.size();
    }

    @Override
    public void write(T e)
            throws InterruptedException {
        output.put(e);
    }

    @Override
    public boolean write(T obj, int timeout)
            throws InterruptedException {
        return output.offer(obj, timeout, MILLISECONDS);
    }

    @Override
    public boolean isReadable() {
        return !input.isEmpty();
    }

    @Override
    public <E extends T> E read(Class<E> type)
            throws InterruptedException {
        return castObject(read(), type);
    }

    @Override
    public final T read()
            throws InterruptedException {
        return input.take();
    }

    @Override
    public <E extends T> E read(Class<E> type, int timeout)
            throws InterruptedException {
        T obj = read(timeout);
        return castObject(obj, type);
    }

    @Override
    public T read(int timeout)
            throws InterruptedException {
        return input.poll(timeout, MILLISECONDS);
    }

    @SuppressWarnings("unchecked")
    private <E extends T> E castObject(T obj, Class<E> type) {
        if (type.isAssignableFrom(obj.getClass()))
            return (E) obj;
        throw new ClassCastException("Expected: <" + type + "> but got: <" + obj.getClass() + '>');
    }

    @Override
    public void clear() {
        input.clear();
        output.clear();
    }
}
