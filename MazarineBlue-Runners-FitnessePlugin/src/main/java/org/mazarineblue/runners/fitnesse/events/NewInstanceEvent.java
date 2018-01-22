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
package org.mazarineblue.runners.fitnesse.events;

import java.util.Objects;
import org.mazarineblue.eventnotifier.Event;
import org.mazarineblue.keyworddriven.Library;
import org.mazarineblue.libraries.fixtures.events.FixtureEvent;
import org.mazarineblue.utilities.ArgumentList;

/**
 * A {@code NewInstanceEvent} is an {@link Event} that request
 * {@link ClassLoaderLink} to load a class and register it as a
 * {@link Library}.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class NewInstanceEvent
        extends FixtureEvent {

    private static final long serialVersionUID = 1L;

    private final String actor;
    private final String fixture;
    private final ArgumentList argumentList;

    /**
     * Constructs a {@code NewInstanceEvent} that causes the loading of the
     * specified fixture, calling the constructor with the specified arguments,
     * to be instanted loaded for the specified actor.
     *
     * @param actor   the actor to give access to the fixture.
     * @param fixture the fixture to load.
     * @param args    the arguments to use when creating the fixture.
     */
    public NewInstanceEvent(String actor, String fixture, Object... args) {
        this.actor = actor;
        this.fixture = fixture;
        this.argumentList = new ArgumentList(args);
    }

    @Override
    public String toString() {
        return actor + ", " + fixture + ", [" + argumentList + ']';
    }

    @Override
    public String message() {
        return "actor=" + actor + ", fixture=" + fixture + ", arguments=[" + argumentList + ']';
    }

    public String getActor() {
        return actor;
    }

    public String getFixture() {
        return fixture;
    }

    public Object[] getArguments() {
        return argumentList.getArguments();
    }

    @Override
    public int hashCode() {
        return 3 * 19 * 19
                + 19 * 19 * Objects.hashCode(this.actor)
                + 19 * Objects.hashCode(this.fixture)
                + Objects.hashCode(this.argumentList);
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj != null && getClass() == obj.getClass()
                && Objects.equals(this.actor, ((NewInstanceEvent) obj).actor)
                && Objects.equals(this.fixture, ((NewInstanceEvent) obj).fixture)
                && Objects.equals(this.argumentList, ((NewInstanceEvent) obj).argumentList);
    }
}
