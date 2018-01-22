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

import java.lang.Thread.State;
import static java.util.Arrays.stream;
import java.util.Collection;
import java.util.function.Predicate;
import static java.util.stream.Collectors.toList;
import org.mazarineblue.utilities.ObjectsUtil;
import org.mazarineblue.utilities.SerializableClonable;

/**
 * An event is an message that something has happened or a request for
 * something to happen.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public interface Event
        extends SerializableClonable {

    /**
     * Constructs an condition that evaluates to {@code true}, when an event
     * is auto consumable.
     *
     * @return {@code true} when the event is not auto consumable.
     */
    public static Predicate<Event> matchesAnyAutoConsumable() {
        return Event::isAutoConsumable;
    }

    /**
     * Constructs an condition that evaluates to {@code true}, when an event
     * is not auto consumable.
     *
     * @return {@code true} when the event is not auto consumable.
     */
    public static Predicate<Event> matchesNoneAutoConsumable() {
        return e -> !e.isAutoConsumable();
    }

    /**
     * Constructs an condition that evaluates to {@code true}, when an event
     * is auto consumable.
     *
     * @return {@code true} when the event is not auto consumable.
     */
    public static Predicate<Event> matchesAnyConsumed() {
        return Event::isConsumed;
    }

    /**
     * Constructs an condition that evaluates to {@code true}, when an event
     * is not auto consumable.
     *
     * @return {@code true} when the event is not auto consumable.
     */
    public static Predicate<Event> matchesNoneConsumed() {
        return e -> !e.isConsumed();
    }

    /**
     * Constructs a condition that is {@code true} when the {@code Event} is
     * one of the specified types.
     *
     * @param types the specified types to check for.
     * @return {@code true}} when the {@code Event} is one of the specified
     *         types.
     */
    public static Predicate<Event> matchesAny(Class<?>... types) {
        return e -> stream(types)
                .anyMatch(t -> t.isAssignableFrom(e.getClass()));
    }

    /**
     * Constructs a condition that is {@code true} when the {@code Event} is
     * none of the specified types.
     *
     * @param types the specified types to check for.
     * @return {@code true}} when the {@code Event} is one of the specified
     *         types.
     */
    public static Predicate<Event> matchesNone(Class<?>... types) {
        return e -> stream(types)
                .noneMatch(t -> t.isAssignableFrom(e.getClass()));
    }

    /**
     * Makes a deep copy of the specified collection.
     * <p>
     * Cloning is done though the use of serialization.
     *
     * @param <T> the type of serializable object.
     * @param src the collection to clone.
     * @return a deep copy of the specified object.
     *
     * @see ObjectsUtil.clone
     */
    public static <T extends Event> Collection<T> clone(Collection<T> src) {
        return src.stream()
                .map(e -> clone(e))
                .collect(toList());
    }

    /**
     * Makes a deep copy of the specified event.
     * <p>
     * Cloning is done though the use of serialization.
     *
     * @param <T> the type of serializable object.
     * @param src the collection to clone.
     * @return a deep copy of the specified object.
     *
     * @see ObjectsUtil.clone
     */
    public static <T extends Event> T clone(T src) {
        T dst = ObjectsUtil.clone(src);
        dst.setConsumed(false);
        return dst;
    }

    /**
     * Returns the {@link State status} of the {@code Event}.
     * <p>
     * The state {@code OK} indicates that the process has not been processes
     * or that there where no problem during handling. The state {@code WARNING}
     * indicates there a potentially harmful situation occurred, but the
     * {@link EventHandler} was able to process the {@code Event}. And the state
     * {@code ERROR} indicates that something prevented the {@code EventHandler}
     * to process the {@code Event} fully.
     *
     * @return the state the {@code Event} is in.
     */
    public Status status();

    /**
     * Returns a string representation of the event message.
     *
     * @return an empty string when there was no message.
     */
    public String message();

    /**
     * Returns a string representation of the response on the event message.
     *
     * @return an empty string when there was no response.
     */
    public String responce();

    /**
     * Indicates if this event was consumed.
     *
     * @return true if this message was consumed.
     */
    public boolean isConsumed();

    /**
     * Changes the state of the event to consumed.
     *
     * @param consumed {@code true} if the events was consumed.
     */
    public void setConsumed(boolean consumed);

    /**
     * Indicates if this event should be automatically consumed by {@link ConsumeEventsLink}.
     *
     * @return {@code true} is this event should be automatically consumed
     */
    public boolean isAutoConsumable();

    /**
     * Status indicates the result of an {@link EventHandler}.
     * <p>
     * The state {@code OK} indicates that the process has not been processes
     * or that there where no problem during handling. The state {@code WARNING}
     * indicates there a potentially harmful situation occurred, but the
     * {@link EventHandler} was able to process the {@link Event}. And the state
     * {@code ERROR} indicates that something prevented the {@code EventHandler}
     * to process the {@code Event} fully.
     */
    @SuppressWarnings("PublicInnerClass")
    public enum Status {

        OK,
        WARNING,
        ERROR,
    }
}
