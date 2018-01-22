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
package org.mazarineblue.keyworddriven;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import java.lang.reflect.Method;
import static java.util.Arrays.stream;
import org.junit.After;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mazarineblue.eventdriven.Processor;
import org.mazarineblue.keyworddriven.events.AddLibraryEvent;
import org.mazarineblue.keyworddriven.events.CommentInstructionLineEvent;
import org.mazarineblue.keyworddriven.events.ContainsConversionRuleEvent;
import org.mazarineblue.keyworddriven.events.ContainsLibrariesEvent;
import org.mazarineblue.keyworddriven.events.CountLibrariesEvent;
import org.mazarineblue.keyworddriven.events.ExecuteInstructionLineEvent;
import org.mazarineblue.keyworddriven.events.FetchLibrariesEvent;
import org.mazarineblue.keyworddriven.events.RegisterConversionRuleEvent;
import org.mazarineblue.keyworddriven.events.RemoveLibraryEvent;
import org.mazarineblue.keyworddriven.events.ValidateInstructionLineEvent;
import org.mazarineblue.keyworddriven.util.Converted;
import org.mazarineblue.keyworddriven.util.Input;
import org.mazarineblue.keyworddriven.util.libraries.DummyLibrary;
import org.mazarineblue.keyworddriven.util.libraries.TestLibrary1;
import org.mazarineblue.keyworddriven.util.libraries.TestLibrary2;
import org.mazarineblue.utilities.util.TestHashCodeAndEquals;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
@RunWith(HierarchicalContextRunner.class)
public class HashCodeAndEqualsTest {

    private Processor processor;

    @Before
    public void setup() {
        processor = Processor.newInstance();
        processor.addLink(new LibraryRegistry());
    }

    @After
    public void teardown() {
        processor = null;
    }

    /* ********************************************************************** *
     *                            Library classes                             *
     * ********************************************************* ,,^..^,, *** */
    @SuppressWarnings("PublicInnerClass")
    public class LibraryRegistryHCAE
            extends TestHashCodeAndEquals<LibraryRegistry> {

        @Override
        protected LibraryRegistry getObject() {
            return new LibraryRegistry();
        }

        @Override
        protected LibraryRegistry getDifferentObject() {
            return new LibraryRegistry(new DummyLibrary("namespace"));
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class LibraryHCAE
            extends TestHashCodeAndEquals<Library> {

        @Override
        protected Library getObject() {
            return new DummyLibrary("namespace");
        }

        @Override
        protected Library getDifferentObject() {
            return new DummyLibrary("different");
        }

        @Test
        public void hashcode_DifferentLibrary() {
            Library a = new TestLibrary1();
            Library b = new TestLibrary2();
            assertNotEquals(a.hashCode(), b.hashCode());
        }

        @Test
        public void equals_DifferentLibrary() {
            Library a = new TestLibrary1();
            Library b = new TestLibrary2();
            assertFalse(a.equals(b));
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class InstructionHCAE
            extends TestHashCodeAndEquals<Instruction> {

        @Override
        protected Instruction getObject() {
            return new Instruction(getMethod("first", 0));
        }

        @Override
        protected Instruction getDifferentObject() {
            return new Instruction(getMethod("first", 1));
        }

        @Test
        public void hashcode_DifferentMethodParameters() {
            Instruction a = new Instruction(getMethod("first", 0));
            Instruction b = new Instruction(getMethod("first", 1));
            assertNotEquals(a.hashCode(), b.hashCode());
        }

        @Test
        public void equals_DifferentMethodParameters() {
            Instruction a = new Instruction(getMethod("first", 0));
            Instruction b = new Instruction(getMethod("first", 1));
            assertFalse(a.equals(b));
        }
    }

    /* ********************************************************************** *
     *                             Helper methods                             *
     * ********************************************************* ,,^..^,, *** */
    public Method getMethod(String name, int parameterCount) {
        return stream(getClass().getMethods())
                .filter(m -> m.getDeclaringClass().equals(getClass()))
                .filter(m -> m.getName().equals(name))
                .filter(m -> m.getParameterCount() == parameterCount)
                .findAny()
                .orElseThrow(() -> new UnsupportedOperationException("Method unavailable."));
    }

    // These methods are called using reflection by getMethod
    public void first() {
        // For testing purposes, there is no need for an implemantion.
    }
    public void second() {
        // For testing purposes, there is no need for an implemantion.
    }
    public void thirth(String a) {
        // For testing purposes, there is no need for an implemantion.
    }
    public void first(String a) {
        // For testing purposes, there is no need for an implemantion.
    }

    /* ********************************************************************** *
     *                            Library events                              *
     * ********************************************************* ,,^..^,, *** */
    @SuppressWarnings("PublicInnerClass")
    public class AddLibraryEventHCAE
            extends TestHashCodeAndEquals<AddLibraryEvent> {

        @Override
        protected AddLibraryEvent getObject() {
            return new AddLibraryEvent(new TestLibrary1());
        }

        @Override
        protected AddLibraryEvent getDifferentObject() {
            return new AddLibraryEvent(new TestLibrary2());
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class ContainsLibrariesEventHCAE
            extends TestHashCodeAndEquals<ContainsLibrariesEvent> {

        @Override
        protected ContainsLibrariesEvent getObject() {
            return new ContainsLibrariesEvent();
        }

        @Override
        protected ContainsLibrariesEvent getDifferentObject() {
            ContainsLibrariesEvent e = new ContainsLibrariesEvent();
            new LibraryRegistry(new TestLibrary1()).eventHandler(e);
            return e;
        }

        @Test
        public void equals_getDifferentFilter() {
            ContainsLibrariesEvent a = new ContainsLibrariesEvent();
            ContainsLibrariesEvent b = new ContainsLibrariesEvent(t -> false);
            assertNotEquals(a, b);
        }

        @Test
        public void hashCode_getDifferentFilter() {
            ContainsLibrariesEvent a = new ContainsLibrariesEvent();
            ContainsLibrariesEvent b = new ContainsLibrariesEvent(t -> false);
            assertNotEquals(a.hashCode(), b.hashCode());
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class CountLibrariesEventHCAE
            extends TestHashCodeAndEquals<CountLibrariesEvent> {

        @Override
        protected CountLibrariesEvent getObject() {
            return new CountLibrariesEvent();
        }

        @Override
        protected CountLibrariesEvent getDifferentObject() {
            CountLibrariesEvent e = new CountLibrariesEvent();
            new LibraryRegistry(new TestLibrary1()).eventHandler(e);
            return e;
        }

        @Test
        public void equals_getDifferentFilter() {
            CountLibrariesEvent a = new CountLibrariesEvent();
            CountLibrariesEvent b = new CountLibrariesEvent(t -> false);
            assertNotEquals(a, b);
        }

        @Test
        public void hashCode_getDifferentFilter() {
            CountLibrariesEvent a = new CountLibrariesEvent();
            CountLibrariesEvent b = new CountLibrariesEvent(t -> false);
            assertNotEquals(a.hashCode(), b.hashCode());
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class FetchLibrariesEventHCAE
            extends TestHashCodeAndEquals<FetchLibrariesEvent> {

        @Override
        protected FetchLibrariesEvent getObject() {
            return new FetchLibrariesEvent();
        }

        @Override
        protected FetchLibrariesEvent getDifferentObject() {
            FetchLibrariesEvent e = new FetchLibrariesEvent();
            new LibraryRegistry(new TestLibrary1()).eventHandler(e);
            return e;
        }

        @Test
        public void equals_getDifferentFilter() {
            FetchLibrariesEvent a = new FetchLibrariesEvent();
            FetchLibrariesEvent b = new FetchLibrariesEvent(t -> false);
            assertNotEquals(a, b);
        }

        @Test
        public void hashCode_getDifferentFilter() {
            FetchLibrariesEvent a = new FetchLibrariesEvent();
            FetchLibrariesEvent b = new FetchLibrariesEvent(t -> false);
            assertNotEquals(a.hashCode(), b.hashCode());
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class RemoveLibraryEventHCAE
            extends TestHashCodeAndEquals<RemoveLibraryEvent> {

        @Override
        protected RemoveLibraryEvent getObject() {
            return new RemoveLibraryEvent(new TestLibrary1());
        }

        @Override
        protected RemoveLibraryEvent getDifferentObject() {
            return new RemoveLibraryEvent(new TestLibrary2());
        }
    }

    /* ********************************************************************** *
     *                          Instruction events                            *
     * ********************************************************* ,,^..^,, *** */
    @SuppressWarnings("PublicInnerClass")
    public class CommentInstructionLineEventHCAE
            extends TestHashCodeAndEquals<CommentInstructionLineEvent> {

        @Override
        protected CommentInstructionLineEvent getObject() {
            return new CommentInstructionLineEvent();
        }

        @Override
        protected CommentInstructionLineEvent getDifferentObject() {
            return new CommentInstructionLineEvent("argument");
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class ExecuteInstructionLineEventHCAE
            extends TestHashCodeAndEquals<ExecuteInstructionLineEvent> {

        @Override
        protected ExecuteInstructionLineEvent getObject() {
            return new ExecuteInstructionLineEvent("keyword");
        }

        @Override
        protected ExecuteInstructionLineEvent getDifferentObject() {
            return new ExecuteInstructionLineEvent("different keyword");
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class ValidateInstructionLineEventHCAE
            extends TestHashCodeAndEquals<ValidateInstructionLineEvent> {

        @Override
        protected ValidateInstructionLineEvent getObject() {
            return new ValidateInstructionLineEvent("Keyword");
        }

        @Override
        protected ValidateInstructionLineEvent getDifferentObject() {
            return new ValidateInstructionLineEvent("different keyword");
        }
    }

    /* ********************************************************************** *
     *                         ConversionRule events                          *
     * ********************************************************* ,,^..^,, *** */
    @SuppressWarnings("PublicInnerClass")
    public class ContainsConversionRuleEventHCAE
            extends TestHashCodeAndEquals<ContainsConversionRuleEvent<?, ?>> {

        @Override
        protected ContainsConversionRuleEvent<?, ?> getObject() {
            return new ContainsConversionRuleEvent<>(Input.class, Converted.class);
        }

        @Override
        protected ContainsConversionRuleEvent<?, ?> getDifferentObject() {
            return new ContainsConversionRuleEvent<>(Input.class, Input.class);
        }

        @Test
        public void hashCode_DifferentOutputTypes() {
            int a = getObject().hashCode();
            int b = new ContainsConversionRuleEvent<>(Converted.class, Converted.class).hashCode();
            assertNotEquals(getMessage(), a, b);
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class RegisterConversionRuleEventHCAE
            extends TestHashCodeAndEquals<RegisterConversionRuleEvent<Input, Converted>> {

        @Override
        protected RegisterConversionRuleEvent<Input, Converted> getObject() {
            return new RegisterConversionRuleEvent<>(Input.class, Converted.class, t -> new Converted());
        }

        @Override
        protected RegisterConversionRuleEvent<Input, Converted> getDifferentObject() {
            return new RegisterConversionRuleEvent<>(Input.class, Converted.class, null);
        }
    }
}
