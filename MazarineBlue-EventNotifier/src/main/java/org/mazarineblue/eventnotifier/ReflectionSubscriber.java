/*
 * Copyright (c) 2012 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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
package org.mazarineblue.eventnotifier;

import org.mazarineblue.eventnotifier.exceptions.SubscriberClassRequiresPublicDeclarationException;

/**
 * An {@code ReflectionSubsciber} allows to use event handlers with concrete
 * classes as parameters instead of only the shared abstract interfaces. Any
 * method that is preceeded with the {@link @EventHandler} is an EventHandler
 * and must only and only one parameter.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 * @param <E> the type of base events this subscriber listens to.
 */
public abstract class ReflectionSubscriber<E extends Event>
        implements Subscriber<E> {

    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    protected ReflectionSubscriber() {
        new EventHandlerCallerImpl(this);
    }

    /**
     * Calls eventHandlers (methods) of the concrete class.
     * <p>
     * All applicable eventHandlers are called, until the event is consumed or
     * there are no more applicable eventHandlers. The order in which the
     * eventHandlers are called is undefined.
     * <p>
     * A method is an eventHandler when it is marked as such with the
     * annotation EventHandler and it has only a event as parameters.
     *
     * @param event the event to process.
     */
    @Override
    public void eventHandler(E event) {
        new EventHandlerCallerImpl(this).publish(event, E::isConsumed);
    }

    /**
     * This method is called when there was no eventHandler to process the
     * specified {@code Event}.
     *
     * @param event the {@code Event}, for which there was no eventHandler.
     */
    @SuppressWarnings("NoopMethodInAbstractClass")
    protected void uncatchedEventHandler(E event) {
        // We only process events we have @EventHandlers for.
    }

    private class EventHandlerCallerImpl
            extends EventHandlerCaller<E> {

        private EventHandlerCallerImpl(ReflectionSubscriber<E> owner) {
            super(owner);
        }

        @Override
        @SuppressWarnings("unchecked")
        protected void unpublished(E event) {
            ReflectionSubscriber.class.cast(getOwner()).uncatchedEventHandler(event);
        }

        @Override
        @SuppressWarnings("unchecked")
        public RuntimeException createPublicClassDeclarationRequiredException() {
            throw new SubscriberClassRequiresPublicDeclarationException(ReflectionSubscriber.class.cast(getOwner()));
        }

        @Override
        public RuntimeException createTargetException(Throwable cause) {
            return cause instanceof RuntimeException
                    ? (RuntimeException) cause
                    : new RuntimeException(cause.getMessage(), cause);
        }
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj.getClass().equals(getClass());
    }
}
