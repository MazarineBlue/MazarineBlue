/*
 * Copyright (c) 2016 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.mazarineblue.eventnotifier;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import static java.lang.reflect.Modifier.isPublic;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import org.mazarineblue.eventnotifier.exceptions.EventHandlerRequiresOneParameterException;
import org.mazarineblue.eventnotifier.exceptions.EventHandlerRequiresValidParameterTypeException;
import org.mazarineblue.eventnotifier.exceptions.EventHandlerRequiresVoidReturnTypeException;
import org.mazarineblue.utilities.exceptions.NeverThrownException;

/**
 * An {code iCaller} determines which
 * {@link EventHandler EventHandlers} can process an {@code Event} and calls
 * it. The abstract method unpublished is called when there are no applicable
 * {@code EventHandlers} available.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 * @param <E> the type of event to work on.
 */
public abstract class EventHandlerCaller<E extends Event> {

    private final Object owner;
    private final List<Method> eventHandlers;

    /**
     * Looks up all {@link EventHandler EventHandlers} belonging to the owner
     * and registers them.
     *
     * @param owner the owner of the {@code EventHandlers}.
     */
    public EventHandlerCaller(Object owner) {
        this.owner = owner;
        eventHandlers = createEventHandlerList();
    }

    // <editor-fold defaultstate="collapsed" desc="Helper methods for the constructor">
    private List<Method> createEventHandlerList() {
        List<Method> list = new ArrayList<>(countEventHandlers());
        fillListWithEventHandlers(list);
        return list;
    }

    private int countEventHandlers() {
        int count = 0;
        for (Method method : owner.getClass().getMethods())
            if (isMethodEventHandler(method))
                ++count;
        return count;
    }

    private void fillListWithEventHandlers(List<Method> list) {
        for (Method method : owner.getClass().getMethods())
            if (isMethodEventHandler(method))
                list.add(method);
    }

    private boolean isMethodEventHandler(Method method) {
        if (!isEventHandler(method))
            return false;
        if (!isReturnTypeVoid(method))
            throw new EventHandlerRequiresVoidReturnTypeException(method);
        if (!hasOnlyOneParameter(method))
            throw new EventHandlerRequiresOneParameterException(method);
        if (!isParameterTypeValid(method))
            throw new EventHandlerRequiresValidParameterTypeException(method);
        return true;
    }

    private boolean isEventHandler(Method method) {
        return method.getAnnotation(EventHandler.class) != null;
    }

    private static boolean isReturnTypeVoid(Method method) {
        return Void.TYPE.isAssignableFrom(method.getReturnType());
    }

    private boolean hasOnlyOneParameter(Method method) {
        return method.getParameterTypes().length == 1;
    }

    private static boolean isParameterTypeValid(Method method) {
        Class<?>[] type = method.getParameterTypes();
        return Event.class.isAssignableFrom(type[0]);
    }
    // </editor-fold>

    /**
     * Publishes the event to the all {@link EventHandler EventHandlers}, until
     * the given condition evaluates to true.
     *
     * @param event         the event to pass to the {@code EventHandlers}.
     * @param stopCondition the condition to evaluate after each pass.
     */
    public void publish(E event, Function<E, Boolean> stopCondition) {
        if (isPassable(event))
            passToEventHandlers(event, stopCondition);
        else
            unpublished(event);
    }

    // <editor-fold defaultstate="collapsed" desc="Helper methods for the constructor">
    private boolean isPassable(E event) {
        return eventHandlers.stream().anyMatch(method -> methodsParameterIsOfType(method, event.getClass()));
    }

    private boolean methodsParameterIsOfType(Method method, Class<?> type) {
        Class<?> parameterType = method.getParameterTypes()[0];
        return parameterType.isAssignableFrom(type);
    }

    private void passToEventHandlers(E event, Function<E, Boolean> stopCondition) {
        for (Method method : eventHandlers) {
            if (!methodsParameterIsOfType(method, event.getClass()))
                continue;
            callEventHandler(event, method);
            if (stopCondition.apply(event))
                return;
        }
    }

    private void callEventHandler(E event, Method method) {
        try {
            method.invoke(owner, event);
        } catch (IllegalAccessException ex) {
            if (!classIsDeclaredPublic(method.getDeclaringClass()))
                throw createPublicClassDeclarationRequiredException();
            throw new NeverThrownException(ex);
        } catch (IllegalArgumentException ex) {
            throw new NeverThrownException(ex);
        } catch (InvocationTargetException ex) {
            throw createTargetException(ex.getCause());
        }
    }

    private boolean classIsDeclaredPublic(Class<?> type) {
        return isPublic(type.getModifiers());
    }
    // </editor-fold>

    protected abstract void unpublished(E event);

    protected abstract RuntimeException createPublicClassDeclarationRequiredException();

    protected abstract RuntimeException createTargetException(Throwable cause);

    public Object getOwner() {
        return owner;
    }
}
