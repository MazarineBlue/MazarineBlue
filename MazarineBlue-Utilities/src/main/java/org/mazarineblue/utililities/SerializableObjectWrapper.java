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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Objects;

/**
 * A {@code SerializableObjectWrapper} is an object wrapper that serialize the
 * object.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class SerializableObjectWrapper
        implements Serializable {

    private static final long serialVersionUID = 1L;

    @Immutable
    private transient Object object;

    /**
     * Constructs {@code SerializableObjectWrapper} using the specified object
     * and adds the ability to serialize it.
     *
     * @param object
     */
    public SerializableObjectWrapper(Object object) {
        this.object = object;
    }

    @Override
    public String toString() {
        return object.toString();
    }

    public Object getObject() {
        return object;
    }

    public Class<?> getWrappedClass() {
        return object.getClass();
    }

    private void writeObject(ObjectOutputStream output)
            throws IOException {
        output.defaultWriteObject();
        output.writeObject(object);
    }

    private void readObject(ObjectInputStream input)
            throws IOException, ClassNotFoundException {
        input.defaultReadObject();
        object = input.readObject();
    }

    @Override
    public int hashCode() {
        return 7 * 79
                + Objects.hashCode(this.object);
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && getClass() == obj.getClass()
                && Objects.equals(this.object, ((SerializableObjectWrapper) obj).object);
    }
}
