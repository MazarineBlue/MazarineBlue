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
public class TwoWayPipeDummy<Type>
        implements TwoWayPipe<Type> {

    private final Type readObject;

    public TwoWayPipeDummy() {
        this.readObject = null;
    }

    public TwoWayPipeDummy(Type readObject) {
        this.readObject = readObject;
    }

    @Override
    public TwoWayPipe<Type> redirect() {
        return this;
    }

    @Override
    public void write(Type obj)
            throws InterruptedException {
    }

    @Override
    public boolean write(Type obj, int timeout)
            throws InterruptedException {
        return true;
    }

    @Override
    public Type read()
            throws InterruptedException {
        return readObject;
    }

    @Override
    public Type read(int timeout)
            throws InterruptedException {
        return readObject;
    }

    @Override
    public <T extends Type> T read(Class<T> type)
            throws InterruptedException {
        return newInstance(type);
    }

    @Override
    public <T extends Type> T read(Class<T> type, int timeout)
            throws InterruptedException {
        return newInstance(type);
    }

    private <T extends Type> T newInstance(Class<T> type) {
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
