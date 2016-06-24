/*
 * Copyright (c) 2012 Alex de Kruijff
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
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mazarineblue.eventbus;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.mazarineblue.eventbus.exceptions.EventHandlerRequiresOneParameterException;
import org.mazarineblue.eventbus.exceptions.SubscriberTargetException;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 * @param <E>
 */
public interface ReflectionSubscriber<E extends Event>
        extends Subscriber<E> {

    /**
     * Calls eventHandlers (methods) of the concrete class.
     *
     * All applicable eventHandlers are called, until the event is consumed or
     * there are no more applicable eventHandlers. The order in which the
     * eventHandlers are called is undefined.
     *
     * A method is an eventHandler when it is marked as such with the annotation
     * EventHandler and it has only a event as parameters.
     *
     * @param event the event to process. exception.
     */
    @Override
    default void eventHandler(E event) {
        Class type = getClass();
        for (Method method : type.getMethods()) {
            EventHandler annotation = method.getAnnotation(EventHandler.class);
            if (annotation == null)
                continue;

            Class[] parameterType = method.getParameterTypes();
            if (parameterType.length != 1)
                throw new EventHandlerRequiresOneParameterException(method, event);
            if (parameterType[0].isAssignableFrom(event.getClass()) == false)
                continue;

            try {
                method.invoke(this, event);
                if (event.isConsumed())
                    return;
            } catch (IllegalAccessException | IllegalArgumentException ex) {
                /* These exception are never thrown because at this point all
                 * methods are public and have one parameter that can take the
                 * event passed. But lets throw an exception for saftey.
                 */
                throw new RuntimeException(ex);
            } catch (InvocationTargetException ex) {
                Throwable cause = ex.getCause();
                throw new SubscriberTargetException(cause.getMessage(), cause);
            }
        }
    }
}
