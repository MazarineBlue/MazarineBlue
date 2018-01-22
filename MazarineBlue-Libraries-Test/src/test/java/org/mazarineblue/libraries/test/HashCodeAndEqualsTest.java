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
package org.mazarineblue.libraries.test;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import org.junit.runner.RunWith;
import org.mazarineblue.libraries.test.events.ExecuteSetupEvent;
import org.mazarineblue.libraries.test.events.ExecuteTestEvent;
import org.mazarineblue.libraries.test.events.SetTestListenerEvent;
import org.mazarineblue.libraries.test.model.TestSystem;
import org.mazarineblue.libraries.test.model.suites.Suite;
import org.mazarineblue.libraries.test.model.tests.Test;
import org.mazarineblue.libraries.test.util.OutputCollectorTestListener;
import org.mazarineblue.libraries.test.util.SuiteStub;
import org.mazarineblue.libraries.test.util.TestStub;
import org.mazarineblue.utilities.util.TestHashCodeAndEquals;

@RunWith(HierarchicalContextRunner.class)
public class HashCodeAndEqualsTest {

    private static Test createTest(String global, String local, String test) {
        Suite g = Suite.newInstance(null, global);
        Suite l = Suite.newInstance(g, local);
        return Test.newInstance(l, test);
    }

    @SuppressWarnings("PublicInnerClass")
    public class ExecuteSetupEventHCAE
            extends TestHashCodeAndEquals<ExecuteSetupEvent> {

        @Override
        protected ExecuteSetupEvent getDifferentObject() {
            return new ExecuteSetupEvent(Suite.newInstance(null, "oof"));
        }

        @Override
        protected ExecuteSetupEvent getObject() {
            return new ExecuteSetupEvent(Suite.newInstance(null, "foo"));
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class MainTestLibraryHCAE
            extends TestHashCodeAndEquals<MainTestLibrary> {

        @Override
        protected MainTestLibrary getDifferentObject() {
            return new MainTestLibrary(TestSystem.newInstance(null));
        }

        @Override
        protected MainTestLibrary getObject() {
            return new MainTestLibrary(null);
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class SetTestListenerEventHCAE
            extends TestHashCodeAndEquals<SetTestListenerEvent> {

        @Override
        protected SetTestListenerEvent getDifferentObject() {
            return new SetTestListenerEvent(new OutputCollectorTestListener());
        }

        @Override
        protected SetTestListenerEvent getObject() {
            return new SetTestListenerEvent(null);
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class SuiteEventHCAE
            extends TestHashCodeAndEquals<ExecuteTestEvent> {

        @Override
        protected ExecuteTestEvent getDifferentObject() {
            return new ExecuteTestEvent(createTest("global", "local", "oof"));
        }

        @Override
        protected ExecuteTestEvent getObject() {
            return new ExecuteTestEvent(createTest("global", "local", "foo"));
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class TestEventHCAE
            extends TestHashCodeAndEquals<ExecuteTestEvent> {

        @org.junit.Test
        public void hashCode_DifferentSuite()
                throws Exception {
            int a = getObject().hashCode();
            int b = getDifferentSuite().hashCode();
            assertNotEquals(a, b);
        }

        @org.junit.Test
        public void equals_DifferentSuite()
                throws Exception {
            Object a = getObject();
            Object b = getDifferentSuite();
            assertFalse(a.equals(b));
        }

        @Override
        protected ExecuteTestEvent getDifferentObject() {
            return new ExecuteTestEvent(createTest("global", "local", "oof"));
        }

        @Override
        protected ExecuteTestEvent getObject() {
            return new ExecuteTestEvent(createTest("global", "local", "foo"));
        }

        private ExecuteTestEvent getDifferentSuite() {
            return new ExecuteTestEvent(createTest("global", "oof", "foo"));
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class RuntimeTestLibraryHCAE
            extends TestHashCodeAndEquals<RuntimeTestLibrary> {

        @Override
        protected RuntimeTestLibrary getObject() {
            return new RuntimeTestLibrary(null, createTest("global", "local", "name"));
        }

        @Override
        protected RuntimeTestLibrary getDifferentObject() {
            return new RuntimeTestLibrary(null, createTest("global", "local", "foo"));
        }

        @org.junit.Test
        public void hashCode_DifferentSuite()
                throws Exception {
            int a = getObject().hashCode();
            int b = getDifferentGlobalSuite().hashCode();
            assertNotEquals(a, b);
        }

        @org.junit.Test
        public void equals_DifferentGlobalSuite()
                throws Exception {
            Object a = getObject();
            Object b = getDifferentGlobalSuite();
            assertFalse(a.equals(b));
        }

        private Object getDifferentGlobalSuite() {
            return new RuntimeTestLibrary(null, createTest("foo", "local", "name"));
        }

        @org.junit.Test
        public void hashCode_DifferentLocalNotEquals()
                throws Exception {
            int a = getObject().hashCode();
            int b = getDifferentLocalSuite().hashCode();
            assertNotEquals(a, b);
        }

        @org.junit.Test
        public void equals_DifferentLocalSuite()
                throws Exception {
            Object a = getObject();
            Object b = getDifferentLocalSuite();
            assertFalse(a.equals(b));
        }

        private Object getDifferentLocalSuite() {
            return new RuntimeTestLibrary(null, createTest("global", "foo", "name"));
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class DefaultTestHCAE
            extends TestHashCodeAndEquals<Test> {

        @Override
        protected Test getObject() {
            return createTest("global", "local", "test");
        }

        @Override
        protected Test getDifferentObject() {
            return createTest("foo", "foo", "foo");
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class TestKeyHCAE
            extends TestHashCodeAndEquals<Object> {

        @Override
        protected Object getObject() {
            Test test = new TestStub("foo", "foo");
            return test.getKey();
        }

        @Override
        protected Object getDifferentObject() {
            Test test = new TestStub("oof", "oof");
            return test.getKey();
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class SuiteKeyHCAE
            extends TestHashCodeAndEquals<Object> {

        @Override
        protected Object getObject() {
            Suite suite = new SuiteStub("foo");
            return suite.getKey();
        }

        @Override
        protected Object getDifferentObject() {
            Suite suite = new SuiteStub("oof");
            return suite.getKey();
        }
    }
}
