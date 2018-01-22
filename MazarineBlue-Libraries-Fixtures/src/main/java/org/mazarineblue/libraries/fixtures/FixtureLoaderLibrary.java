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
package org.mazarineblue.libraries.fixtures;

import static java.lang.Class.forName;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import static java.lang.reflect.Modifier.isPublic;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.mazarineblue.eventdriven.Invoker;
import org.mazarineblue.eventdriven.Processor;
import org.mazarineblue.eventnotifier.EventHandler;
import org.mazarineblue.keyworddriven.Keyword;
import org.mazarineblue.keyworddriven.Library;
import org.mazarineblue.keyworddriven.Parameters;
import org.mazarineblue.keyworddriven.PassInvoker;
import org.mazarineblue.keyworddriven.events.AddLibraryEvent;
import org.mazarineblue.keyworddriven.events.RemoveLibraryEvent;
import static org.mazarineblue.libraries.fixtures.FixtureLoaderLibraryPlugin.NAMESPACE;
import org.mazarineblue.libraries.fixtures.events.PathEvent;
import org.mazarineblue.libraries.fixtures.exceptions.FixtureNotFoundException;
import org.mazarineblue.libraries.fixtures.exceptions.FixtureNotPublicException;
import org.mazarineblue.libraries.fixtures.exceptions.NoSuchConstructorException;

public class FixtureLoaderLibrary
        extends Library {

    private final Map<String, FixtureLibrary> map = new HashMap<>();
    private final List<String> paths = new ArrayList<>();

    public FixtureLoaderLibrary() {
        super(NAMESPACE);
    }

    @Override
    public String toString() {
        return "paths = " + Arrays.toString(paths.toArray());
    }

    /**
     * Event handlers are not meant to be called directly, instead publish an
     * event to an {@link Processor}; please see the specified event for more
     * information about this event handler.
     *
     * @param event the event this {@code EventHandler} processes.
     * @see PathEvent
     */
    @EventHandler
    public void eventHandler(PathEvent event) {
        paths.add(event.getPath());
        event.setConsumed(true);
    }

    @PassInvoker
    @Keyword("Import fixture")
    @Parameters(min = 1)
    public void importFixture(Invoker invoker, String path, Object... args)
            throws InstantiationException, IllegalAccessException, InvocationTargetException {
        FixtureLibrary lib = createLibrary(path, args);
        deregisterLibrary(invoker, path);
        registerLibrary(invoker, lib, path);
    }

    public FixtureLibrary createLibrary(String path, Object... args)
            throws InstantiationException, InvocationTargetException, IllegalAccessException {
        Class<?> type = searchPathsForClass(paths, path);
        String namespace = type.getPackage() != null ? type.getPackage().getName() : "";
        Object obj = newInstance(type, args);
        return new FixtureLibrary(namespace, obj);
    }

    // <editor-fold defaultstate="collapsed" desc="Helper methods for createLibrary()">
    private static Class<?> searchPathsForClass(List<String> paths, String className) {
        List<String> classNames = convertToFullName(paths, className);
        Class<?> type = searchClassNamesForClass(classNames);
        if (type != null)
            return type;
        throw new FixtureNotFoundException(className, paths);
    }

    private static List<String> convertToFullName(List<String> paths, String className) {
        List<String> list = new ArrayList<>(paths.size() + 1);
        paths.stream().forEach(path -> list.add(path + "." + className));
        list.add(className);
        return list;
    }

    private static Class<?> searchClassNamesForClass(List<String> classNames) {
        for (String className : classNames) {
            Class<?> type = tryForClassName(className);
            if (type != null)
                return type;
        }
        return null;
    }

    private static Class<?> tryForClassName(String className) {
        try {
            Class<?> type = forName(className);
            if (!isPublic(type.getModifiers()))
                throw new FixtureNotPublicException(className);
            return type;
        } catch (ClassNotFoundException ex) {
            return null;
        }
    }

    private static Object newInstance(Class<?> fixtureType, Object[] arguments)
            throws InstantiationException, IllegalAccessException, InvocationTargetException {
        Constructor<?> constructor = getConstructor(fixtureType, arguments);
        return constructor.newInstance(arguments);
    }

    private static Constructor<?> getConstructor(Class<?> fixtureType, Object[] arguments) {
        Class<?>[] parameterTypes = convertToParameterType(arguments);
        for (Constructor<?> constructor : fixtureType.getConstructors())
            if (Arrays.equals(parameterTypes, constructor.getParameterTypes()))
                return constructor;
        throw new NoSuchConstructorException(fixtureType, parameterTypes);
    }

    private static Class<?>[] convertToParameterType(Object[] arguments) {
        Class<?>[] parameterTypes = new Class<?>[arguments.length];
        for (int i = 0; i < arguments.length; ++i)
            parameterTypes[i] = arguments[i].getClass();
        return parameterTypes;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Helper methods for importFixture()">
    private void deregisterLibrary(Invoker invoker, String path) {
        if (!map.containsKey(path))
            return;
        FixtureLibrary library = map.get(path);
        invoker.publish(new RemoveLibraryEvent(library));
        map.remove(path);
    }

    private void registerLibrary(Invoker invoker, FixtureLibrary library, String path) {
        invoker.publish(new AddLibraryEvent(library));
        map.put(path, library);
    }
    // </editor-fold>

    @Override
    public int hashCode() {
        return 83 * 83 * 7
                + 83 * Objects.hashCode(this.map)
                + Objects.hashCode(this.paths);
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj != null && getClass() == obj.getClass()
                && Objects.equals(this.map, ((FixtureLoaderLibrary) obj).map)
                && Objects.equals(this.paths, ((FixtureLoaderLibrary) obj).paths);
    }
}
