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
 * A {@code TwoWayPipeFactory} is a factory that can setup a
 * {@link TwoWayPipe}. A {@link TwoWayPipe} allows for communication between
 * different processes.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 * @param <T> is the base classes these pipes can read and write.
 */
public interface TwoWayPipeFactory<T> {

    /**
     * Creates an double lane pipe.
     * <p>
     * Use this pipe when you want to share a pipe between to threads or
     * processes where both thread read and write.
     *
     * @param capacity that required capacity of the pipe.
     * @return a double lane pipe.
     */
    public TwoWayPipe<T> createPipe(int capacity);

    /**
     * CreateS a looped backed pipe.
     * <p>
     * Use this pipe when you want to share this pipe between two threads or
     * processes, where one only reads an the other only writes.
     *
     * @param capacity that required capacity of the pipe.
     * @return a looped backed pipe.
     */
    public TwoWayPipe<T> createLoopedBackedPipe(int capacity);
}
