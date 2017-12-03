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
package org.mazarineblue.executors;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import java.io.File;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mazarineblue.eventbus.Event;
import org.mazarineblue.executors.events.RequestObjectCreationEvent;
import org.mazarineblue.executors.events.SetFileSystemEvent;
import org.mazarineblue.executors.links.DefaulExecutorFactoryLink;
import org.mazarineblue.fs.util.DummyFileSystem;
import org.mazarineblue.utililities.util.TestHashCodeAndEquals;

@RunWith(HierarchicalContextRunner.class)
public class HashCodeAndEqualsTest {

    @SuppressWarnings("PublicInnerClass")
    public class DefaulExecutorFactoryLinkHCAE
            extends TestHashCodeAndEquals<DefaulExecutorFactoryLink> {

        @Override
        protected DefaulExecutorFactoryLink getObject() {
            return new DefaulExecutorFactoryLink(null);
        }

        @Override
        protected DefaulExecutorFactoryLink getDifferentObject() {
            FeedExecutorBuilder builder = new FeedExecutorBuilder();
            FeedExecutorFactory factory = new DefaultFeedExecutorFactory(builder);
            return new DefaulExecutorFactoryLink(factory);
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class RequestObjectCreationEventHCAE
            extends TestHashCodeAndEquals<Event> {

        @Override
        protected Event getObject() {
            RequestObjectCreationEvent<Integer> e = new RequestObjectCreationEvent<>();
            e.setResult(1);
            return e;
        }

        @Override
        protected Event getDifferentObject() {
            RequestObjectCreationEvent<Integer> e = new RequestObjectCreationEvent<>();
            e.setResult(2);
            return e;
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class SetFileSystemEventHCAE
            extends TestHashCodeAndEquals<Event> {

        @Override
        protected Event getObject() {
            return new SetFileSystemEvent(null);
        }

        @Override
        protected Event getDifferentObject() {
            return new SetFileSystemEvent(new DummyFileSystem());
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class SheetOptionsHCAE
            extends TestHashCodeAndEquals<SheetOptions> {

        @Override
        protected SheetOptions getObject() {
            return new SheetOptions(new File("foo"), "foo");
        }

        @Override
        protected SheetOptions getDifferentObject() {
            return new SheetOptions(new File("oof"), "foo");
        }

        @Test
        public void hashCode_DifferentSheets() {
            int a = getObject().hashCode();
            int b = getDifferentSheets().hashCode();
                assertNotEquals(a, b);
        }

        @Test
        public void equals_DifferentSheets() {
            Object a = getObject();
            Object b = getDifferentSheets();
            assertFalse(a.equals(b));
        }

        private SheetOptions getDifferentSheets() {
            return new SheetOptions(new File("foo"), "foo", "foo");
        }
    }
}
