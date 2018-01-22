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
package org.mazarineblue.utililities.util;

import org.mazarineblue.utililities.TwoWayPipe;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class TwoWayPipeDummy<T>
        implements TwoWayPipe<T> {

    private final T readObject;

    public TwoWayPipeDummy() {
        this.readObject = null;
    }

    public TwoWayPipeDummy(T readObject) {
        this.readObject = readObject;
    }

    @Override
    public TwoWayPipe<T> redirect() {
        return this;
    }

    @Override
    public void write(T obj)
            throws InterruptedException {
        // For testing purposes, there is no need for an implemantion.
    }

    @Override
    public boolean write(T obj, int timeout)
            throws InterruptedException {
        return true;
    }

    @Override
    public T read()
            throws InterruptedException {
        return readObject;
    }

    @Override
    public T read(int timeout)
            throws InterruptedException {
        return readObject;
    }

    @Override
    public <E extends T> E read(Class<E> type)
            throws InterruptedException {
        return newInstance(type);
    }

    @Override
    public <E extends T> E read(Class<E> type, int timeout)
            throws InterruptedException {
        return newInstance(type);
    }

    private <E extends T> E newInstance(Class<E> type) {
        try {
            return type.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public boolean isReadable() {
        return true;
    }

    @Override
    public boolean isWritable() {
        return true;
    }

    @Override
    public boolean isReadQueueEmpty() {
        return false;
    }

    @Override
    public boolean isWriteQueueEmpty() {
        return false;
    }

    @Override
    public boolean isReadQueueFull() {
        return false;
    }

    @Override
    public boolean isWriteQueueFull() {
        return false;
    }

    @Override
    public int readQueueSize() {
        return 1;
    }

    @Override
    public int writeQueueSize() {
        return 1;
    }

    @Override
    public void clear() {
        // Nothing to do here.
    }
}
