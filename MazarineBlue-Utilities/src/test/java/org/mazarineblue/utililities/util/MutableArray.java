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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class MutableArray
        implements Serializable {

    private static final long serialVersionUID = 1L;
    private Object[] arr = new ImmutableObject[0];

    private void writeObject(ObjectOutputStream output)
            throws IOException {
        output.writeObject(arr);
    }

    private void readObject(ObjectInputStream input)
            throws IOException, ClassNotFoundException {
        arr = (Object[]) input.readObject();
    }

    @Override
    public int hashCode() {
        return 7 * 67
                + Arrays.deepHashCode(this.arr);
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && getClass() == obj.getClass() && Arrays.deepEquals(this.arr, ((MutableArray) obj).arr);
    }
}
