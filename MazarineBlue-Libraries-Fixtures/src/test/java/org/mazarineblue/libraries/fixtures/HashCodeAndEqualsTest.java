/*
 * Copyright (c) 2017 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mazarineblue.eventdriven.util.TestInvoker;
import org.mazarineblue.libraries.fixtures.events.PathEvent;
import org.mazarineblue.utilities.util.TestHashCodeAndEquals;

@RunWith(HierarchicalContextRunner.class)
public class HashCodeAndEqualsTest {

    public class FixtureLoaderLibraryHCAE
            extends TestHashCodeAndEquals<FixtureLoaderLibrary> {

        @Test
        public void hashCode_DifferentContent_NotEquals()
                throws Exception {
            int a = getObject().hashCode();
            int b = getDifferentContent().hashCode();
            assertNotEquals(a, b);
        }

        @Test
        public void equals_DifferentContent_ReturnFalse()
                throws Exception {
            Object a = getObject();
            Object b = getDifferentContent();
            assertFalse(a.equals(b));
        }

        @Override
        protected FixtureLoaderLibrary getObject() {
            return new FixtureLoaderLibrary();
        }

        @Override
        protected FixtureLoaderLibrary getDifferentObject() {
            FixtureLoaderLibrary lib = new FixtureLoaderLibrary();
            lib.eventHandler(new PathEvent("a"));
            return lib;
        }

        private FixtureLoaderLibrary getDifferentContent()
                throws Exception {
            FixtureLoaderLibrary lib = new FixtureLoaderLibrary();
            TestInvoker invoker = new TestInvoker();
            lib.importFixture(invoker, "LoginDialogDriver", "User", "Password");
            return lib;
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class PathHCAE
            extends TestHashCodeAndEquals<PathEvent> {

        @Override
        protected PathEvent getObject() {
            return new PathEvent("foo");
        }

        @Override
        protected PathEvent getDifferentObject() {
            return new PathEvent("oof");
        }
    }
}
