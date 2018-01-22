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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import static java.lang.reflect.Modifier.isFinal;
import static java.lang.reflect.Modifier.isStatic;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import jdk.nashorn.internal.ir.annotations.Immutable;
import org.mazarineblue.utilities.exceptions.NotEqualException;
import org.mazarineblue.utilities.exceptions.UnclonableObjectException;

/**
 * An {@code ObjectsUtil} is a utility class that performs operations one the
 * specified object.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 * @param <T> the type of serializable object.
 */
public class ObjectsUtil<T> {

    private static final Logger LOG = Logger.getLogger(ObjectsUtil.class.getName());

    private final T obj;

    /**
     * Constructs an {@code ObjectsUtil} using the specified object to perform
     * operations on.
     *
     * @param obj the object to perform operations on.
     */
    public ObjectsUtil(T obj) {
        this.obj = obj;
    }

    /**
     * Returns the object stored within this instance.
     *
     * @return the object stored within this instance.
     */
    public T get() {
        return obj;
    }

    /**
     * Makes a deep copy of the specified object.
     * <p>
     * Cloning is done though the use of serialization. Any immutable objects
     * of fields are not cloned, but instead referenced copied, using the
     * method {@link Object#hashCode()} and {@link Object#equals(Object)}.
     * This only applies to classes that are immutable, because all there
     * fields are final or are declared immutable.
     *
     * @param <T> the type of serializable object.
     * @param src the object to clone.
     * @return a deep copy of the specified object.
     *
     * @see Immutable
     */
    public static <T extends SerializableClonable> T clone(T src) {
        T dst = new ObjectsUtil<>(src).deepcopy().get();
        dst.copyTransient(src);
        return dst;
    }

    /**
     * Makes a deep copy of the specified object.
     * <p>
     * Cloning is done though the use of serialization. Any immutable objects
     * of fields are not cloned, but instead referenced copied, using the
     * method {@link Object#hashCode()} and {@link Object#equals(Object)}.
     * This only applies to classes that are immutable, because all there
     * fields are final or are declared immutable.
     *
     * @return a deep copy of the specified object.
     *
     * @see Immutable
     */
    public ObjectsUtil<T> deepcopy() {
        try {
            CloneRegistry registry = new CloneRegistry();
            T clone = registry.deepCopy(obj);
            return new ObjectsUtil<>(clone);
        } catch (IOException | ClassNotFoundException ex) {
            throw new UnclonableObjectException(ex);
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Helper methods for deepcopy()">
    private static class CloneRegistry {

        private final Map<Class<?>, Map<Object, Object>> map = new HashMap<>(16);

        private <T> T deepCopy(Object obj)
                throws IOException, ClassNotFoundException {
            byte[] data = serialize(obj);
            return deserialize(data);
        }

        private <T> byte[] serialize(T obj)
                throws IOException {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            serialize(obj, output);
            return output.toByteArray();
        }

        private <T> void serialize(T obj, OutputStream output)
                throws IOException {
            try (ObjectOutputStream oos = new CloneObjectOutputStream(output)) {
                oos.writeObject(obj);
                oos.flush();
            }
        }

        private <T extends Serializable> T deserialize(byte[] data)
                throws IOException, ClassNotFoundException {
            return deserialize(new ByteArrayInputStream(data));
        }

        @SuppressWarnings("unchecked")
        private <T extends Serializable> T deserialize(InputStream input)
                throws IOException, ClassNotFoundException {
            return (T) new CloneObjectInputStream(input).readObject();
        }

        class CloneObjectOutputStream
                extends ObjectOutputStream {

            CloneObjectOutputStream(OutputStream output)
                    throws IOException {
                super(output);
                enableReplaceObject(true);
            }

            @Override
            protected Object replaceObject(Object obj)
                    throws IOException {
                if (!isArray(obj) && isImmutable(obj))
                    store(obj);
                return obj;
            }

            @SuppressWarnings({"NestedAssignment", "CollectionWithoutInitialCapacity"})
            private void store(Object obj) {
                Map<Object, Object> m = map.get(obj.getClass());
                if (m == null) {
                    m = new HashMap<>();
                    map.put(obj.getClass(), m);
                }
                m.put(obj, obj);
            }
        }

        class CloneObjectInputStream
                extends ObjectInputStream {

            CloneObjectInputStream(InputStream input)
                    throws IOException {
                super(input);
                enableResolveObject(true);
            }

            @Override
            protected Object resolveObject(Object obj)
                    throws IOException {
                return !isArray(obj) && isImmutable(obj) ? retrieve(obj) : obj;
            }

            private Object retrieve(Object obj) {
                Class<? extends Object> type = obj.getClass();
                Map<Object, Object> m = map.get(type);
                if (m == null)
                    return obj;
                Object fetched = m.get(obj);
                if (fetched == null) {
                    String format = "Methods equals and hashCode missing from %s";
                    LOG.log(Level.WARNING, format, obj.getClass().getCanonicalName());
                }
                return fetched != null ? fetched : obj;
            }
        }

        private static boolean isArray(Object obj) {
            return obj.getClass().isArray();
        }

        private static boolean isImmutable(Object obj) {
            return isImmutable(obj.getClass());
        }

        private static boolean isImmutable(Class<?> type) {
            return isString(type) || isNumber(type) || isParentImmutable(type) && isImmutable(type.getDeclaredFields());
        }

        private static boolean isParentImmutable(Class<?> type) {
            final Class<?> superclass = type.getSuperclass();
            return superclass == null || superclass.equals(Object.class) || isImmutable(superclass);
        }

        private static boolean isImmutable(Field[] fields) {
            for (Field field : fields)
                if (!isImmutable(field))
                    return false;
            return true;
        }

        private static boolean isImmutable(Field field) {
            return isStatic(field.getModifiers()) ? true
                    : isString(field.getType()) ? true
//                    : isDeclaredImmutable(field) ? true
                    : !isFinal(field.getModifiers()) ? false
                    : isPrimative(field.getType()) ? true
                    : isNumber(field.getType()) ? true
                    : isImmutable(field.getType());
        }

        private static boolean isString(Class<?> type) {
            return type.equals(String.class);
        }

        private static boolean isDeclaredImmutable(Field field) {
            return false;
//            return null != field.getAnnotation(Immutable.class);
        }

        private static boolean isPrimative(Class<?> type) {
            return type.isPrimitive();
        }

        private static boolean isNumber(Class<?> type) {
            return Number.class.isAssignableFrom(type);
        }
    }
    //</editor-fold>

    public ObjectsUtil<T> assertEquals(T expected) {
        if (bothAreNull(expected) || bothAreEqual(expected))
            return this;
        throw new NotEqualException(obj, expected);
    }

    private boolean bothAreNull(T expected) {
        return obj == null && expected == null;
    }

    private boolean bothAreEqual(T expected) {
        return obj != null && expected != null && obj.equals(expected);
    }
}
