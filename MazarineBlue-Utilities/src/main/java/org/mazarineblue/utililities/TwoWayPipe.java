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

/**
 * A {@code TwoWayPipe} allows for communication between different processes.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 * @param <T> the type of object to transmit.
 */
public interface TwoWayPipe<T> {

    /**
     * Returns an different pipe in the opposite direction.
     * <p>
     * This pipe might block on a read, if the redirected pipe is writable. And
     * this pipe might block on a write, if the redirected pipe is readable.
     *
     * @return the pipe in the opposite direction.
     */
    public TwoWayPipe<T> redirect();

    /**
     * Writes the object to the pipe, waiting if necessary for space to become
     * available.
     *
     * @param obj the message to write to the pipe.
     * @throws InterruptedException if interrupted while waiting.
     */
    public void write(T obj)
            throws InterruptedException;

    /**
     * Writes the object to the pipe, waiting up to the specified wait time if
     * necessary for space to become available.
     *
     * @param obj     the message to write to the pipe.
     * @param timeout how long to wait before giving up.
     * @return {@code true} if successful, or {@code false} if the specified
     *         waiting time elapses before space is available.
     *
     * @throws InterruptedException if interrupted while waiting.
     */
    public boolean write(T obj, int timeout)
            throws InterruptedException;

    /**
     * Reads from the pipe, waiting if necessary until an object becomes
     * available.
     *
     * @param <E>  the type to cast to.
     * @param type the type to cast to.
     * @return the first object on the read queue.
     *
     * @throws InterruptedException if interrupted while waiting.
     * @throws ClassCastException   when the object can not be cast to the
     *                              requested type.
     */
    public <E extends T> E read(Class<E> type)
            throws InterruptedException;

    /**
     * Reads from the pipe, waiting if necessary until an object becomes
     * available.
     *
     * @return the first object on the read queue.
     *
     * @throws InterruptedException if interrupted while waiting.
     */
    public T read()
            throws InterruptedException;

    /**
     * Reads from the pipe, waiting up to the specified wait time if necessary
     * until an object can be read from the pipe.
     *
     * @param <E>     the type to cast to.
     * @param type    the type to cast to.
     * @param timeout how long to wait before giving up, in milliseconds.
     * @return the first object on the read queue or {@code null} if the
     *         specified waiting time elapsed before an event became available.
     *
     * @throws InterruptedException if interrupted while waiting.
     * @throws ClassCastException   when the object can not be cast to the
     *                              requested type.
     */
    public <E extends T> E read(Class<E> type, int timeout)
            throws InterruptedException;

    /**
     * Reads from the pipe, waiting up to the specified wait time if necessary
     * until an object can be read from the pipe.
     *
     * @param timeout how long to wait before giving up.
     * @return the first object on the read queue or {@code null} if the
     *         specified waiting time elapsed before an event became available.
     *
     * @throws InterruptedException if interrupted while waiting.
     */
    public T read(int timeout)
            throws InterruptedException;

    /**
     * Indicates if the pipe is readable.
     * <p>
     * This method doesn't give an guarantee that the pipe will be readable if
     * multiple threads read from the same pipe.
     *
     * @return true if the pipe is readable.
     */
    public boolean isReadable();

    /**
     * Indicates if the pipe is writable.
     * <p>
     * This method doesn't give an guarantee that the pipe will be writable if
     * multiple threads read from the same pipe.
     *
     * @return true if the pipe is writable.
     */
    public boolean isWritable();

    /**
     * Returns {@code true} if the read queue doesn't contain any events.
     *
     * @return true if the read queue is empty.
     */
    public boolean isReadQueueEmpty();

    /**
     * Returns {@code true} if the write queue doesn't contain any events.
     *
     * @return true if the write queue is empty.
     */
    public boolean isWriteQueueEmpty();

    /**
     * Returns {@code true} if the read queue is full.
     *
     * @return true if the read queue is full.
     */
    public boolean isReadQueueFull();

    /**
     * Returns {@code true} if the read queue is full.
     *
     * @return true if the write queue is full.
     */
    public boolean isWriteQueueFull();

    /**
     * Returns the amount of events waiting to be read by the other end of the
     * pipe.
     *
     * @return the size of the read queue.
     */
    public int readQueueSize();

    /**
     * Returns the amount of events waiting to be read by this end of the pipe.
     *
     * @return the size of the write queue.
     */
    public int writeQueueSize();

    /**
     * Removes all of the elements from this pipe.
     */
    public void clear();
}
