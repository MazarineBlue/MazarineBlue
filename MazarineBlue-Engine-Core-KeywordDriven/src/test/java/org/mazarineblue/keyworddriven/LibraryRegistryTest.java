/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mazarineblue.keyworddriven;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import static java.util.Arrays.asList;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mazarineblue.eventbus.link.EventBusLink;
import org.mazarineblue.eventbus.link.SubscribeEvent;
import org.mazarineblue.eventdriven.Interpreter;
import org.mazarineblue.eventdriven.InvokerEvent;
import org.mazarineblue.eventdriven.feeds.MemoryFeed;
import org.mazarineblue.keyworddriven.events.AddLibraryEvent;
import org.mazarineblue.keyworddriven.events.CountLibrariesEvent;
import org.mazarineblue.keyworddriven.events.ExecuteInstructionLineEvent;
import org.mazarineblue.keyworddriven.events.FetchLibrariesEvent;
import org.mazarineblue.keyworddriven.events.KeywordDrivenEvent;
import org.mazarineblue.keyworddriven.events.RemoveLibraryEvent;
import org.mazarineblue.keyworddriven.exceptions.InstructionInaccessibleException;
import org.mazarineblue.keyworddriven.exceptions.LibraryInaccessibleException;
import org.mazarineblue.keyworddriven.exceptions.LibraryNotFoundException;
import org.mazarineblue.keyworddriven.exceptions.ToFewArgumentsException;
import org.mazarineblue.keyworddriven.util.DummyLibrary;
import org.mazarineblue.keyworddriven.util.TestLibraryExternalCaller;
import org.mazarineblue.utililities.util.TestHashCodeAndEquals;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
@RunWith(HierarchicalContextRunner.class)
public class LibraryRegistryTest {

    @SuppressWarnings("PublicInnerClass")
    public class TestAccess {

        @Test(expected = LibraryInaccessibleException.class)
        public void execute_PrivateLibrary_ThrowsExeption() {
            EventBusLink link = new EventBusLink();
            link.subscribe(KeywordDrivenEvent.class, null, new LibraryRegistry(new PrivateLibrary()));
        }

        private class PrivateLibrary
                extends Library {

            PrivateLibrary() {
                super("");
            }

            @Keyword("foo")
            public void foo() {
                // This class is about the accesebility.
            }
        }

        @Test(expected = InstructionInaccessibleException.class)
        public void executeInstruction_CallerInstructionMismatch_InstructionInaccessibleExceptionThrown() {
            Library library = new TestLibraryExternalCaller("namespace", new Object()) {
                @Keyword("instruction")
                public void test() {
                    // This test is about using the wrong keyword.
                }
            };
            LibraryRegistry registry = new LibraryRegistry(library);
            registry.executeInstruction(new ExecuteInstructionLineEvent("namespace.instruction"));
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class TestUsingEvents {

        private MemoryFeed feed;
        private Interpreter interpreter;

        @Before
        public void setup() {
            feed = new MemoryFeed(4);
            feed.add(new SubscribeEvent(new LibraryRegistry()));
            interpreter = Interpreter.newInstance();
            interpreter.addLink(new EventBusLink());
        }

        @After
        public void teardown() {
            feed = null;
            interpreter = null;
        }

        @Test
        public void addLibrary_Accepted() {
            InvokerEvent addEvent = new AddLibraryEvent(new TestLibraryExternalCaller(Library.NO_NAMESPACE));
            CountLibrariesEvent countEvent = new CountLibrariesEvent();
            feed.add(addEvent);
            feed.add(countEvent);
            interpreter.execute(feed);
            assertTrue(addEvent.isConsumed());
            assertEquals(1, countEvent.getCount());
        }

        @Test(expected = LibraryNotFoundException.class)
        public void removeLibrary_LibraryNotAdded_ExceptionThrown() {
            feed.add(new RemoveLibraryEvent(new TestLibraryExternalCaller(Library.NO_NAMESPACE)));
            feed.add(new CountLibrariesEvent());
            interpreter.execute(feed);
        }

        @Test
        public void removeLibrary_Accepted() {
            Library library = new TestLibraryExternalCaller(Library.NO_NAMESPACE);
            RemoveLibraryEvent removeEvent = new RemoveLibraryEvent(library);
            CountLibrariesEvent countEvent = new CountLibrariesEvent();
            feed.add(new AddLibraryEvent(library));
            feed.add(removeEvent);
            feed.add(countEvent);
            interpreter.execute(feed);
            assertEquals(0, countEvent.getCount());
            assertTrue(removeEvent.isConsumed());
            assertFalse(countEvent.isConsumed());
        }

        @Test
        public void testFetchLibrariesEvent_WithoutFilter() {
            Library library1 = new Library("namespace") {
                @Keyword("Instruction")
                public void instruction() {
                    // This test is about fetchting the libraries.
                }
            };
            Library library2 = new Library("foo") {
                @Keyword("Instruction")
                public void instruction() {
                    // This test is about fetchting the libraries.
                }
            };
            LibraryRegistry registry = new LibraryRegistry(library1, library2);
            FetchLibrariesEvent e = new FetchLibrariesEvent();
            registry.eventHandler(e);
            assertEquals(asList(library1, library2), e.getLibraries());
        }

        @Test
        public void testFetchLibrariesEvent_WithFilter() {
            Library library1 = new Library("namespace") {
                @Keyword("Instruction")
                public void instruction() {
                }
            };
            Library library2 = new Library("foo") {
                @Keyword("Instruction")
                public void instruction() {
                }
            };
            LibraryRegistry registry = new LibraryRegistry(library1, library2);
            FetchLibrariesEvent e = new FetchLibrariesEvent(l -> l.getNamespace().equals("namespace"));
            registry.eventHandler(e);
            assertEquals(asList(library1), e.getLibraries());
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class TestCallingInstructionsTest {

        @Test
        public void instructionWithNoArguments_CallerWithoutArguments() {
            Library library = new Library("namespace") {
                @Keyword("Instruction")
                public int instruction() {
                    return 123;
                }
            };
            ExecuteInstructionLineEvent e = new ExecuteInstructionLineEvent("Instruction");
            LibraryRegistry registry = new LibraryRegistry(library);
            registry.executeInstruction(e);
            assertEquals(123, e.getResult());
        }

        @Test
        public void instructionWithNoArguments_CallerWithOneArgument() {
            Library library = new Library("namespace") {
                @Keyword("Instruction")
                public int instruction() {
                    return 123;
                }
            };
            ExecuteInstructionLineEvent e = new ExecuteInstructionLineEvent("Instruction", 1);
            LibraryRegistry registry = new LibraryRegistry(library);
            registry.executeInstruction(e);
            assertEquals(123, e.getResult());
        }

        @Test(expected = ToFewArgumentsException.class)
        public void instructionWithSingleRequiredArgument_CalledWithoutArguments() {
            Library library = new Library("namespace") {
                @Keyword("Instruction")
                @Parameters(min = 1)
                public int instruction(Object arg) {
                    return 123;
                }
            };
            ExecuteInstructionLineEvent e = new ExecuteInstructionLineEvent("Instruction");
            LibraryRegistry registry = new LibraryRegistry(library);
            registry.executeInstruction(e);
            assertEquals(124, e.getResult());
        }

        @Test
        public void instructionWithSingleArgument_CalledWithoutArguments() {
            Library library = new Library("namespace") {
                @Keyword("Instruction")
                public int instruction(Object arg) {
                    return 123;
                }
            };
            ExecuteInstructionLineEvent e = new ExecuteInstructionLineEvent("Instruction");
            LibraryRegistry registry = new LibraryRegistry(library);
            registry.executeInstruction(e);
            assertEquals(123, e.getResult());
        }

        @Test
        public void instructionWithSingleArgument_CalledWithOneArgument() {
            Library library = new Library("namespace") {
                @Keyword("Instruction")
                public int instruction(Object arg) {
                    return 123 + (Integer) arg;
                }
            };
            ExecuteInstructionLineEvent e = new ExecuteInstructionLineEvent("Instruction", 1);
            LibraryRegistry registry = new LibraryRegistry(library);
            registry.executeInstruction(e);
            assertEquals(124, e.getResult());
        }

        @Test
        public void instructionWithSingleArgument_CalledWithTwoArguments() {
            Library library = new Library("namespace") {
                @Keyword("Instruction")
                public int instruction(Object arg) {
                    return 123 + (Integer) arg;
                }
            };
            ExecuteInstructionLineEvent e = new ExecuteInstructionLineEvent("Instruction", 1, 2);
            LibraryRegistry registry = new LibraryRegistry(library);
            registry.executeInstruction(e);
            assertEquals(124, e.getResult());
        }

        @Test
        public void instructionWithVariableArgument_CalledWithoutArguments() {
            Library library = new Library("namespace") {
                @Keyword("Instruction")
                public int instruction(Object arg, Object... arr) {
                    int i = 123;
                    for (Object obj : arr)
                        i += (Integer) obj;
                    return i;
                }
            };
            ExecuteInstructionLineEvent e = new ExecuteInstructionLineEvent("Instruction");
            LibraryRegistry registry = new LibraryRegistry(library);
            registry.executeInstruction(e);
            assertEquals(123, e.getResult());
        }

        @Test
        public void instructionWithVariableArgument_CalledWithOneArguments() {
            Library library = new Library("namespace") {
                @Keyword("Instruction")
                public int instruction(Object... arr) {
                    int i = 123;
                    for (Object obj : arr)
                        i += (Integer) obj;
                    return i;
                }
            };
            ExecuteInstructionLineEvent e = new ExecuteInstructionLineEvent("Instruction", 1);
            LibraryRegistry registry = new LibraryRegistry(library);
            registry.executeInstruction(e);
            assertEquals(124, e.getResult());
        }

        @Test
        public void instructionWithVariableArgument_CalledWithThreeArguments() {
            Library library = new Library("namespace") {
                @Keyword("Instruction")
                public int instruction(Object arg, Object... arr) {
                    int i = 123;
                    for (Object obj : arr)
                        i += (Integer) obj;
                    return i;
                }
            };
            ExecuteInstructionLineEvent e = new ExecuteInstructionLineEvent("Instruction", 1, 2, 3);
            LibraryRegistry registry = new LibraryRegistry(library);
            registry.executeInstruction(e);
            assertEquals(128, e.getResult());
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class EqualsAndHashCode
            extends TestHashCodeAndEquals<LibraryRegistry> {

        @Override
        protected LibraryRegistry getObject() {
            return new LibraryRegistry();
        }

        @Override
        protected LibraryRegistry getDifferentObject() {
            LibraryRegistry registry = new LibraryRegistry();
            registry.addLibrary(new DummyLibrary("foo"));
            return registry;
        }
    }
}
