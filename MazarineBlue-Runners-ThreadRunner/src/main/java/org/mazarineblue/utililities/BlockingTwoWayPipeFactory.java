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

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * A {@code TwoWayPipeFactory} is a {@code TwoWayPipeFactory} that can setup a
 * {@link BlockingTwoWayPipe}. A {@link BlockingTwoWayPipe} is
 * {@link TwoWayPipe} that allows for communication between different processes
 * though the use of {@link BlockingQueue} allowing communications between
 * treads.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 * @param <T> is the base classes these pipes can read and write.
 */
public class BlockingTwoWayPipeFactory<T>
        implements TwoWayPipeFactory<T> {

    @Override
    public TwoWayPipe<T> createPipe(int capacity) {
        BlockingQueue<T> input = new ArrayBlockingQueue<>(capacity);
        BlockingQueue<T> output = new ArrayBlockingQueue<>(capacity);
        return new BlockingTwoWayPipe<>(input, output);
    }

    @Override
    public TwoWayPipe<T> createLoopedBackedPipe(int capacity) {
        BlockingQueue<T> queue = new ArrayBlockingQueue<>(capacity);
        return new BlockingTwoWayPipe<>(queue, queue);
    }
}
