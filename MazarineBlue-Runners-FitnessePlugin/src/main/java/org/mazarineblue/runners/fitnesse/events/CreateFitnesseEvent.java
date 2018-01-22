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
package org.mazarineblue.runners.fitnesse.events;

import java.util.Objects;
import org.mazarineblue.runners.fitnesse.engineplugin.FitnesseSubscriber;
import org.mazarineblue.utilities.ArgumentList;

/**
 * A {@code CreateFitnesseEvent} is a {@code FitnesseEvent} that instructs a
 * {@link FitnesseSubscriber} to create the specified fixture.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class CreateFitnesseEvent
        extends FitnesseEvent {

    private static final long serialVersionUID = 1L;

    private final String instance;
    private final String fixture;
    private final ArgumentList arguments;

    /**
     * Constructs a {@code CreateFitnesseEvent} as an instruction for
     * {@code FitnesseSubscriber} to create the specified fixture.
     *
     * @param instance  the instance owning the fixture.
     * @param fixture   the fixture to create.
     * @param arguments the argument to pass to the fixture constructor.
     */
    public CreateFitnesseEvent(String instance, String fixture, Object... arguments) {
        this.instance = instance;
        this.fixture = fixture;
        this.arguments = new ArgumentList(arguments);
    }

    @Override
    public String toString() {
        return instance + ", " + fixture + ", [" + arguments + "]";
    }

    @Override
    public String message() {
        return "instance=" + instance + ", fixture=" + fixture + ", arguments=[" + arguments + "]";
    }

    public String getInstance() {
        return instance;
    }

    public String getFixture() {
        return fixture;
    }

    public Object[] getArguments() {
        return arguments.getArguments();
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj != null && getClass() == obj.getClass()
                && Objects.equals(this.instance, ((CreateFitnesseEvent) obj).instance)
                && Objects.equals(this.fixture, ((CreateFitnesseEvent) obj).fixture)
                && Objects.equals(this.arguments, ((CreateFitnesseEvent) obj).arguments);
    }

    @Override
    public int hashCode() {
        return 7 * 37 * 37 * 37
                + 37 * 37 * Objects.hashCode(this.instance)
                + 37 * Objects.hashCode(this.fixture)
                + Objects.hashCode(this.arguments);
    }
}
