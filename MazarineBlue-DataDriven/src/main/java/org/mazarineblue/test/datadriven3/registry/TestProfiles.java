/*
 * Copyright (c) 2012-2014 Alex de Kruijff
 * Copyright (c) 2014-2015 Specialisterren
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
package org.mazarineblue.test.datadriven3.registry;

import java.util.Collection;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.nl>}
 */
class TestProfiles {

    private final Collection<TestProfile> profiles;

    TestProfiles(Collection<TestProfile> profiles) {
        this.profiles = profiles;
    }

    boolean isEmpty() {
        return profiles == null || profiles.size() == 0;
    }

    boolean isDependendOn(TestProfile profile) {
        for (TestProfile listed : profiles)
            if (listed == profile)
                return true;
            else if (listed.equals(profile))
                return true;
            else if (listed.isDependendOn(profile))
                return true;
        return false;
    }

    /**
     * Returns the worst state of all profiles. When one profile ha
     *
     * @return FAIL - when one profile has failed, otherwise UNSET when one
     *         profile was not run, otherwize PASS.
     */
    TestProfile.State getState() {
        TestProfile.State state = TestProfile.State.PASS;
        for (TestProfile profile : profiles)
            switch (profile.getState()) {
                case FAIL:
                case SKIP: // Skipped states indicates a failed dependency
                    return TestProfile.State.FAIL;
                case UNSET:
                    state = TestProfile.State.UNSET;
            }
        return state;
    }

    void fetchProfiles(Collection<TestProfile> todo) {
        for (TestProfile profile : profiles)
            profile.fetchProfiles(todo);
    }

    void addSoftDependencies(TestProfiles profiles) {
        for (TestProfile profile : this.profiles)
            profile.addSoftDependencies(profiles);
    }
}
