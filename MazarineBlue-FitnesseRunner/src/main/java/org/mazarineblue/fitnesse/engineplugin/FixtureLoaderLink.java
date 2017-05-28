/*
 * Copyright (c) 2015-2016 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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
package org.mazarineblue.fitnesse.engineplugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import static java.util.Arrays.asList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.mazarineblue.eventbus.EventHandler;
import org.mazarineblue.eventdriven.Interpreter;
import org.mazarineblue.eventdriven.Invoker;
import org.mazarineblue.eventdriven.Link;
import org.mazarineblue.fitnesse.engineplugin.exceptions.FixtureNotFoundException;
import org.mazarineblue.fitnesse.engineplugin.exceptions.FixtureNotPublicException;
import org.mazarineblue.fitnesse.engineplugin.exceptions.NoSuchConstructorException;
import org.mazarineblue.fitnesse.events.NewInstanceEvent;
import org.mazarineblue.fitnesse.events.PathEvent;
import org.mazarineblue.keyworddriven.Library;
import org.mazarineblue.keyworddriven.events.AddLibraryEvent;
import org.mazarineblue.keyworddriven.events.RemoveLibraryEvent;

/**
 * A {@code ClasssLoaderLink} is a {@code Link} that loads a class dynamically
 * as a Library with the namespace 'org.mazarineblue.fixtures', looking for it
 * at the specified paths.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 * @see PathEvent
 * @see NewInstanceEvent
 */
public class FixtureLoaderLink
        extends Link {

    private static final String NAMESPACE = "org.mazarineblue.fixtures";

    private final List<String> paths;
    private final Map<String, Library> map = new HashMap<>(4);

    /**
     * Constructs a {@code FixtureLoaderLink} with paths to look in when
     * creating new instances of a fixture, using the specified paths.
     *
     * @param paths the paths to look in for classes.
     */
    public FixtureLoaderLink(String... paths) {
        this.paths = new ArrayList<>(asList(paths));
    }

    @Override
    public String toString() {
        return "paths = " + Arrays.toString(paths.toArray());
    }

    /**
     * Event handlers are not meant to be called directly, instead publish an
     * event to an {@link Interpreter}; please see the specified event for more
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

    /**
     * Event handlers are not meant to be called directly, instead publish an
     * event to an {@link Interpreter}; please see the specified event for more
     * information about this event handler.
     *
     * @param event the event this {@code EventHandler} processes.
     * @throws ReflectiveOperationException when reflection doesn't work.
     * @see NewInstanceEvent
     */
    @EventHandler
    public void eventHandler(NewInstanceEvent event)
            throws ReflectiveOperationException {
        if (isLibraryRegisteredBy(event.getActor()))
            deregisterLibrary(event.getInvoker(), event.getActor());
        registerLibrary(event.getInvoker(), event.getActor(), createLibrary(event));
        event.setConsumed(true);
    }

    private boolean isLibraryRegisteredBy(String actor) {
        return map.containsKey(actor);
    }

    private void deregisterLibrary(Invoker invoker, String actor) {
        Library library = map.get(actor);
        map.remove(actor);
        invoker.publish(new RemoveLibraryEvent(library));
    }

    private Library createLibrary(NewInstanceEvent event)
            throws ReflectiveOperationException {
        Class<?> fixtureType = searchPathsForClass(paths, event.getFixture());
        Object obj = newInstance(fixtureType, event.getArguments());
        return new FixtureLibrary(NAMESPACE, obj);
    }

    // <editor-fold defaultstate="collapsed" desc="Helper methods for createLibrary()">
    private Class<?> searchPathsForClass(List<String> paths, String className) {
        List<String> classNames = convertToFullName(paths, className);
        Class<?> type = searchClassNamesForClass(classNames);
        if (type != null)
            return type;
        throw new FixtureNotFoundException(className, paths);
    }

    private List<String> convertToFullName(List<String> paths, String className) {
        List<String> list = new ArrayList<>(paths.size() + 1);
        paths.stream().forEach(path -> list.add(path + "." + className));
        list.add(className);
        return list;
    }

    private Class<?> searchClassNamesForClass(List<String> classNames) {
        for (String className : classNames) {
            Class<?> type = tryForClassName(className);
            if (type != null)
                return type;
        }
        return null;
    }

    private Class<?> tryForClassName(String className) {
        try {
            Class<?> type = Class.forName(className);
            if (!Modifier.isPublic(type.getModifiers()))
                throw new FixtureNotPublicException(className);
            return type;
        } catch (ClassNotFoundException ex) {
            return null;
        }
    }

    private Object newInstance(Class<?> fixtureType, Object[] arguments)
            throws InstantiationException, IllegalAccessException, InvocationTargetException {
        Constructor<?> constructor = getConstructor(fixtureType, arguments);
        return constructor.newInstance(arguments);
    }

    private Constructor<?> getConstructor(Class<?> fixtureType, Object[] arguments) {
        Class<?>[] parameterTypes = convertToParameterType(arguments);
        for (Constructor<?> constructor : fixtureType.getConstructors())
            if (Arrays.equals(parameterTypes, constructor.getParameterTypes()))
                return constructor;
        throw new NoSuchConstructorException(fixtureType, parameterTypes);
    }

    private Class<?>[] convertToParameterType(Object[] arguments) {
        Class<?>[] parameterTypes = new Class<?>[arguments.length];
        for (int i = 0; i < arguments.length; ++i)
            parameterTypes[i] = arguments[i].getClass();
        return parameterTypes;
    }
    // </editor-fold>

    private void registerLibrary(Invoker invoker, String actor, Library library) {
        map.put(actor, library);
        invoker.publish(new AddLibraryEvent(library));
    }

    @Override
    public int hashCode() {
        return 3 * 11 * 11
                + 11 * Objects.hashCode(this.paths)
                + Objects.hashCode(this.map);
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && getClass() == obj.getClass()
                && Objects.equals(this.paths, ((FixtureLoaderLink) obj).paths)
                && Objects.equals(this.map, ((FixtureLoaderLink) obj).map);
    }
}
