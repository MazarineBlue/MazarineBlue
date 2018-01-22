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
package org.mazarineblue.runners.fitnesse;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.After;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mazarineblue.eventdriven.Processor;
import org.mazarineblue.eventdriven.feeds.MemoryFeed;
import org.mazarineblue.eventnotifier.Event;
import org.mazarineblue.eventnotifier.subscribers.SubscriberSpy;
import org.mazarineblue.executors.Executor;
import org.mazarineblue.executors.ExecutorBuilder;
import org.mazarineblue.executors.ExecutorFactory;
import org.mazarineblue.fs.MemoryFileSystem;
import org.mazarineblue.libraries.fixtures.events.PathEvent;
import org.mazarineblue.runners.fitnesse.engineplugin.FitnesseSubscriber;
import org.mazarineblue.runners.fitnesse.engineplugin.FixtureLoaderSubscriber;
import org.mazarineblue.runners.fitnesse.events.AssignFitnesseEvent;
import org.mazarineblue.runners.fitnesse.events.CallFitnesseEvent;
import org.mazarineblue.runners.fitnesse.events.CreateFitnesseEvent;
import org.mazarineblue.runners.fitnesse.events.NewInstanceEvent;
import org.mazarineblue.utilities.util.TestHashCodeAndEquals;

@RunWith(HierarchicalContextRunner.class)
public class HashCodeAndEqualsTest {

    @SuppressWarnings("PublicInnerClass")
    public class AssignFitnesseEventHCAE
            extends TestHashCodeAndEquals<AssignFitnesseEvent> {

        @Override
        protected AssignFitnesseEvent getObject() {
            return new AssignFitnesseEvent("symbol", "foo");
        }

        @Override
        protected AssignFitnesseEvent getDifferentObject() {
            return new AssignFitnesseEvent("symbol", "oof");
        }

        @Test
        public void hashCode_DifferentSymbol() {
            AssignFitnesseEvent a = new AssignFitnesseEvent("symbol", "foo");
            AssignFitnesseEvent b = new AssignFitnesseEvent("lobmys", "foo");
            assertNotEquals(a.hashCode(), b.hashCode());
        }

        @Test
        public void equals_DifferentSymbol() {
            AssignFitnesseEvent a = new AssignFitnesseEvent("symbol", "foo");
            AssignFitnesseEvent b = new AssignFitnesseEvent("lobmys", "foo");
            assertFalse(a.equals(b));
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class CallFitnesseEventHCAE
            extends TestHashCodeAndEquals<CallFitnesseEvent> {

        @Override
        protected CallFitnesseEvent getObject() {
            return new CallFitnesseEvent("instance", "method", "arg1", "arg2");
        }

        @Override
        protected CallFitnesseEvent getDifferentObject() {
            return new CallFitnesseEvent("instance", "method", "arg2", "arg1");
        }

        @Test
        public void hashCode_DifferentInstances() {
            CallFitnesseEvent a = new CallFitnesseEvent("instance", "method", "arg1", "arg2");
            CallFitnesseEvent b = new CallFitnesseEvent("ecnatsni", "method", "arg1", "arg2");
            assertNotEquals(a.hashCode(), b.hashCode());
        }

        @Test
        public void hashCode_DifferentMethod() {
            CallFitnesseEvent a = new CallFitnesseEvent("instance", "method", "arg1", "arg2");
            CallFitnesseEvent b = new CallFitnesseEvent("instance", "dohtem", "arg1", "arg2");
            assertNotEquals(a.hashCode(), b.hashCode());
        }

        @Test
        public void equals_DifferentInstances() {
            CallFitnesseEvent a = new CallFitnesseEvent("instance", "method", "arg1", "arg2");
            CallFitnesseEvent b = new CallFitnesseEvent("ecnatsni", "method", "arg1", "arg2");
            assertFalse(a.equals(b));
        }

        @Test
        public void equals_DifferentMethod() {
            CallFitnesseEvent a = new CallFitnesseEvent("instance", "method", "arg1", "arg2");
            CallFitnesseEvent b = new CallFitnesseEvent("instance", "dohtem", "arg1", "arg2");
            assertFalse(a.equals(b));
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class CreateFitnesseEventHCAE
            extends TestHashCodeAndEquals<CreateFitnesseEvent> {

        @Override
        protected CreateFitnesseEvent getObject() {
            return new CreateFitnesseEvent("instance", "fixture", "arg1", "arg2");
        }

        @Override
        protected CreateFitnesseEvent getDifferentObject() {
            return new CreateFitnesseEvent("instance", "fixture", "arg2", "arg1");
        }

        @Test
        public void hashCode_DifferentInstance() {
            CreateFitnesseEvent a = new CreateFitnesseEvent("instance", "fixture", "arg1", "arg2");
            CreateFitnesseEvent b = new CreateFitnesseEvent("ecnatsni", "fixture", "arg1", "arg2");
            assertNotEquals(a.hashCode(), b.hashCode());
        }

        @Test
        public void hashCode_DifferentFixture() {
            CreateFitnesseEvent a = new CreateFitnesseEvent("instance", "fixture", "arg1", "arg2");
            CreateFitnesseEvent b = new CreateFitnesseEvent("instance", "erutxif", "arg1", "arg2");
            assertNotEquals(a.hashCode(), b.hashCode());
        }

        @Test
        public void equals_DifferentInstance() {
            CreateFitnesseEvent a = new CreateFitnesseEvent("instance", "fixture", "arg1", "arg2");
            CreateFitnesseEvent b = new CreateFitnesseEvent("ecnatsni", "fixture", "arg1", "arg2");
            assertFalse(a.equals(b));
        }

        @Test
        public void equals_DifferentFixture() {
            CreateFitnesseEvent a = new CreateFitnesseEvent("instance", "fixture", "arg1", "arg2");
            CreateFitnesseEvent b = new CreateFitnesseEvent("instance", "erutxif", "arg1", "arg2");
            assertFalse(a.equals(b));
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class PathFitnesseEventHCAE
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

    @SuppressWarnings("PublicInnerClass")
    public class FotmesseSinscriberHCAE
            extends TestHashCodeAndEquals<FitnesseSubscriber> {

        @Override
        protected FitnesseSubscriber getObject() {
            return new FitnesseSubscriber();
        }

        @Override
        protected FitnesseSubscriber getDifferentObject() {
            FitnesseSubscriber subscriber = new FitnesseSubscriber();
            Processor processor = Processor.newInstance();
            processor.addLink(subscriber);
            processor.execute(new MemoryFeed(new CreateFitnesseEvent("instance", "fixture")));
            return subscriber;
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class FixtureLoaderSubscriberHCAE
            extends TestHashCodeAndEquals<FixtureLoaderSubscriber> {

        private Executor executor;
        private FixtureLoaderSubscriber actual;
        private SubscriberSpy<Event> spy;

        @Before
        public void setup() {
            actual = new FixtureLoaderSubscriber();

            ExecutorBuilder builder = new ExecutorBuilder();
            builder.setFileSystem(new MemoryFileSystem());

            ExecutorFactory factory = ExecutorFactory.newInstance(builder);
            factory.addLinkAfterVariableParser(() -> actual);

            spy = new SubscriberSpy<>(e -> true);
            executor = factory.create();
            executor.addLink(spy);
        }

        @After
        public void teardown() {
            executor = null;
            actual = null;
            spy = null;
        }

        @Override
        protected FixtureLoaderSubscriber getObject() {
            return new FixtureLoaderSubscriber();
        }

        @Override
        protected FixtureLoaderSubscriber getDifferentObject() {
            return new FixtureLoaderSubscriber("foo");
        }

        @Test
        public void hashCode_DifferentRegistrations()
                throws ReflectiveOperationException {
            executor.execute(new MemoryFeed(new NewInstanceEvent("actor", "LoginDialogDriver", "bob", "secret")));
            assertNotEquals(new FixtureLoaderSubscriber().hashCode(), actual.hashCode());
        }

        @Test
        public void equals_DifferentRegistrations()
                throws ReflectiveOperationException {
            executor.execute(new MemoryFeed(new NewInstanceEvent("actor", "LoginDialogDriver", "bob", "secret")));
            assertFalse(new FixtureLoaderSubscriber().equals(actual));
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class NewInstanceEventHCAE
            extends TestHashCodeAndEquals<NewInstanceEvent> {

        @Override
        protected NewInstanceEvent getObject() {
            return new NewInstanceEvent("actor", "fixture", "arg1", "arg2");
        }

        @Override
        protected NewInstanceEvent getIdenticalObject() {
            return new NewInstanceEvent("actor", "fixture", "arg1", "arg2");
        }

        @Override
        protected NewInstanceEvent getDifferentObject() {
            return new NewInstanceEvent("actor", "fixture", "arg2", "arg1");
        }

        @Test
        public void hashCode_DifferentInstance() {
            NewInstanceEvent a = new NewInstanceEvent("actor", "fixture", "arg1", "arg2");
            NewInstanceEvent b = new NewInstanceEvent("rotca", "fixture", "arg1", "arg2");
            assertNotEquals(a.hashCode(), b.hashCode());
        }

        @Test
        public void hashCode_DifferentFixture() {
            NewInstanceEvent a = new NewInstanceEvent("actor", "fixture", "arg1", "arg2");
            NewInstanceEvent b = new NewInstanceEvent("actor", "erutxif", "arg1", "arg2");
            assertNotEquals(a.hashCode(), b.hashCode());
        }

        @Test
        public void equals_DifferentInstance() {
            NewInstanceEvent a = new NewInstanceEvent("actor", "fixture", "arg1", "arg2");
            NewInstanceEvent b = new NewInstanceEvent("rotca", "fixture", "arg1", "arg2");
            assertFalse(a.equals(b));
        }

        @Test
        public void equals_DifferentFixture() {
            NewInstanceEvent a = new NewInstanceEvent("actor", "fixture", "arg1", "arg2");
            NewInstanceEvent b = new NewInstanceEvent("actor", "erutxif", "arg1", "arg2");
            assertFalse(a.equals(b));
        }
    }
}
