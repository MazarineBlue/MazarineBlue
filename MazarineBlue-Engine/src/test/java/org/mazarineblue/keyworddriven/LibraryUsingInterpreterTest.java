/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mazarineblue.keyworddriven;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
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
import org.mazarineblue.eventdriven.feeds.MemoryFeed;
import org.mazarineblue.keyworddriven.events.AddLibraryEvent;
import org.mazarineblue.keyworddriven.events.ExecuteInstructionLineEvent;
import org.mazarineblue.keyworddriven.events.FetchLibrariesEvent;
import org.mazarineblue.keyworddriven.events.ValidateInstructionLineEvent;
import org.mazarineblue.keyworddriven.exceptions.ArgumentsAreIncompatibleException;
import org.mazarineblue.keyworddriven.exceptions.InstructionInaccessibleException;
import org.mazarineblue.keyworddriven.exceptions.InstructionNotFoundException;
import org.mazarineblue.keyworddriven.exceptions.KeywordConflictException;
import org.mazarineblue.keyworddriven.exceptions.MultipleInstructionsFoundException;
import org.mazarineblue.keyworddriven.exceptions.PrimativesNotAllowedByondMinimumBorderException;
import org.mazarineblue.keyworddriven.exceptions.ToFewArgumentsException;
import org.mazarineblue.keyworddriven.util.TestConflictingKeywordsLibrary;
import org.mazarineblue.keyworddriven.util.TestEmptyLibrary;
import org.mazarineblue.keyworddriven.util.TestFilledLibrarySpy;
import org.mazarineblue.keyworddriven.util.TestNonPublicInstructionLibrary;
import org.mazarineblue.keyworddriven.util.TestPrimativesUsedBeyongMinimumLibrary;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
@RunWith(HierarchicalContextRunner.class)
public class LibraryUsingInterpreterTest {

    private static final String NO_NAMESPACE = Library.NO_NAMESPACE;
    private static final String REGISTRED_NAMESPACE = "test";
    private static final String REGISTRED_NAMESPACE2 = "tset";
    private static final String UNREGISTERED_NAMESPACE = "foo";

    private static final String NON_EXISTING_INSTRUCTION = TestFilledLibrarySpy.NON_EXISTING_INSTRUCTION;
    private static final String ZERO_ARGUMENTS_INSTRUCTION = TestFilledLibrarySpy.ZERO_ARGUMENTS_INSTRUCTION;
    private static final String ONE_ARGUMENT_INSTRUCTION = TestFilledLibrarySpy.ONE_ARGUMENT_INSTRUCTION;
    private static final String INVOKER_ONE_ARGUMENT_INSTRUCTION = TestFilledLibrarySpy.INVOKER_ONE_ARGUMENT_INSTRUCTION;
    private static final String NON_PUBLIC_LIBRARY_INSTRUCTION = TestNonPublicInstructionLibrary.NON_PUBLIC_LIBRARY_INSTRUCTION;

    private static final String DELIMITER = ".";

    private static final String NO_NAMESPACE_ZERO_ARGUMENTS_INSTRUCTION = NO_NAMESPACE + DELIMITER + ZERO_ARGUMENTS_INSTRUCTION;
    private static final String UNREGISTERED_NAMESPACE_ZERO_ARGUMENTS_INSTRUCTION = UNREGISTERED_NAMESPACE + DELIMITER + ZERO_ARGUMENTS_INSTRUCTION;
    private static final String NO_NAMESPACE_NON_EXISTING_INSTRUCTION = NO_NAMESPACE + DELIMITER + NON_EXISTING_INSTRUCTION;
    private static final String NO_NAMESPACE_ONE_ARGUMENT_INSTRUCTION = NO_NAMESPACE + DELIMITER + ONE_ARGUMENT_INSTRUCTION;
    private static final String REGISTRED_NAMESPACE_ZERO_ARGUMENT_INSTRUCTION = REGISTRED_NAMESPACE + DELIMITER + ZERO_ARGUMENTS_INSTRUCTION;
    private static final String REGISTRED_NAMESPACE_ONE_ARGUMENT_INSTRUCTION = REGISTRED_NAMESPACE + DELIMITER + ONE_ARGUMENT_INSTRUCTION;
    private static final String NO_NAMESPACE_INVOKER_ONE_ARGUMENT_INSTRUCTION = NO_NAMESPACE + DELIMITER + INVOKER_ONE_ARGUMENT_INSTRUCTION;
    private static final String REGISTRED_NAMESPACE_INVOKER_ONE_ARGUMENT_INSTRUCTION = REGISTRED_NAMESPACE + DELIMITER + INVOKER_ONE_ARGUMENT_INSTRUCTION;

    private MemoryFeed feed;
    private Interpreter interpreter;

    @Before
    public void setup() {
        feed = new MemoryFeed(4);
        feed.add(new SubscribeEvent(new LibraryRegistry()));
        interpreter = Interpreter.getDefaultInstance();
        interpreter.addLink(new EventBusLink());
    }

    @After
    public void teardown() {
        feed = null;
        interpreter = null;
    }

    @Test(expected = KeywordConflictException.class)
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void creation_ConflictingKeywords_ThrowsKeywordConflictException() {
        new TestConflictingKeywordsLibrary();
    }

    @Test(expected = InstructionInaccessibleException.class)
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void creation_NonPublicMethod_ThrowsInstructionInAccessibleException() {
        new TestNonPublicInstructionLibrary();
    }

    @Test(expected = PrimativesNotAllowedByondMinimumBorderException.class)
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void creation_InstructionWithPrimativesBeyondMinimumBorder_ThrowsPrimativesBeyongMinimumBorderException() {
        new TestPrimativesUsedBeyongMinimumLibrary();
    }

    @Test
    @SuppressWarnings("NestedAssignment")
    public void fetchLibraries_True_AllFound() {
        FetchLibrariesEvent e;
        feed.add(new AddLibraryEvent(new TestEmptyLibrary(REGISTRED_NAMESPACE)));
        feed.add(new AddLibraryEvent(new TestFilledLibrarySpy(REGISTRED_NAMESPACE2)));
        feed.add(e = new FetchLibrariesEvent());
        interpreter.execute(feed);
        assertEquals(2, e.getLibraries().size());
    }

    @SuppressWarnings("PublicInnerClass")
    public class EmptyLink {

        @Test
        @SuppressWarnings("NestedAssignment")
        public void validate_EmptyLink_UnregistedNamespace_DoesntValidate() {
            ValidateInstructionLineEvent event = new ValidateInstructionLineEvent(
                    UNREGISTERED_NAMESPACE_ZERO_ARGUMENTS_INSTRUCTION);
            feed.add(event);
            interpreter.execute(feed);
            assertFalse(event.isValid());
            assertFalse(event.isArgumentsAreIncompatible());
            assertTrue(event.isInstructionNotFound());
            assertFalse(event.isLineHasToFewArguments());
            assertFalse(event.isMultipleInstructionsFound());
            assertEquals(0, event.getUserErrorFlags());
        }

        @Test(expected = InstructionNotFoundException.class)
        public void execute_EmptyLink_UnregistedNamespace_ThrowsInstructionNotFoundException() {
            feed.add(new ExecuteInstructionLineEvent(UNREGISTERED_NAMESPACE_ZERO_ARGUMENTS_INSTRUCTION));
            interpreter.execute(feed);
        }

        @Test
        public void validate_EmptyLink_NonExistingInstruction_DoesntValidate() {
            ValidateInstructionLineEvent event = new ValidateInstructionLineEvent(
                    NO_NAMESPACE_ZERO_ARGUMENTS_INSTRUCTION);
            feed.add(event);
            interpreter.execute(feed);
            assertFalse(event.isValid());
            assertFalse(event.isArgumentsAreIncompatible());
            assertTrue(event.isInstructionNotFound());
            assertFalse(event.isLineHasToFewArguments());
            assertFalse(event.isMultipleInstructionsFound());
            assertEquals(0, event.getUserErrorFlags());
        }

        @Test(expected = InstructionNotFoundException.class)
        public void execute_EmptyLink_NonExistingInstruction_ThrowsInstructionNotFoundException() {
            feed.add(new ExecuteInstructionLineEvent(NO_NAMESPACE_ZERO_ARGUMENTS_INSTRUCTION));
            interpreter.execute(feed);
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class EmptyLibrary {

        @Before
        public void setup() {
            feed.add(new AddLibraryEvent(new TestEmptyLibrary(REGISTRED_NAMESPACE)));
        }

        @Test
        public void validate_EmptyLibrary_UnregistedNamespace_DoesntValidate() {
            ValidateInstructionLineEvent event = new ValidateInstructionLineEvent(
                    UNREGISTERED_NAMESPACE_ZERO_ARGUMENTS_INSTRUCTION);
            feed.add(event);
            interpreter.execute(feed);
            assertFalse(event.isValid());
            assertFalse(event.isArgumentsAreIncompatible());
            assertTrue(event.isInstructionNotFound());
            assertFalse(event.isLineHasToFewArguments());
            assertFalse(event.isMultipleInstructionsFound());
            assertEquals(0, event.getUserErrorFlags());
        }

        @Test(expected = InstructionNotFoundException.class)
        public void execute_EmptyLibrary_UnregistedNamespace_ThrowsInstructionNotFoundException() {
            feed.add(new ExecuteInstructionLineEvent(UNREGISTERED_NAMESPACE_ZERO_ARGUMENTS_INSTRUCTION));
            interpreter.execute(feed);
        }

        @Test
        public void validate_EmptyLibrary_NonExistingInstruction_DoesntValidate() {
            ValidateInstructionLineEvent event = new ValidateInstructionLineEvent(NO_NAMESPACE_NON_EXISTING_INSTRUCTION);
            feed.add(event);
            interpreter.execute(feed);
            assertFalse(event.isValid());
            assertFalse(event.isArgumentsAreIncompatible());
            assertTrue(event.isInstructionNotFound());
            assertFalse(event.isLineHasToFewArguments());
            assertFalse(event.isMultipleInstructionsFound());
            assertEquals(0, event.getUserErrorFlags());
        }

        @Test(expected = InstructionNotFoundException.class)
        public void execute_EmptyLibrary_NonExistingInstruction_ThrowsInstructionNotFoundException() {
            feed.add(new ExecuteInstructionLineEvent(NO_NAMESPACE_NON_EXISTING_INSTRUCTION));
            interpreter.execute(feed);
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class FilledLibrary {

        private TestFilledLibrarySpy library;

        @Before
        public void setup() {
            library = new TestFilledLibrarySpy(REGISTRED_NAMESPACE);
            feed.add(new AddLibraryEvent(library));
        }

        @Test
        public void namespace_Equals_REGISTRED_NAMESPACE() {
            assertEquals(REGISTRED_NAMESPACE, library.getNamespace());
        }

        @Test
        public void validate_FilledLibrary_UnregistedNamespace_DoesntValidate() {
            ValidateInstructionLineEvent event = new ValidateInstructionLineEvent(
                    UNREGISTERED_NAMESPACE_ZERO_ARGUMENTS_INSTRUCTION);
            feed.add(event);
            interpreter.execute(feed);
            assertFalse(event.isValid());
            assertFalse(event.isArgumentsAreIncompatible());
            assertTrue(event.isInstructionNotFound());
            assertFalse(event.isLineHasToFewArguments());
            assertFalse(event.isMultipleInstructionsFound());
            assertEquals(0, event.getUserErrorFlags());
        }

        @Test(expected = InstructionNotFoundException.class)
        public void execute_FilledLibrary_UnregistedNamespace_ThrowsInstructionNotFoundException() {
            feed.add(new ExecuteInstructionLineEvent(UNREGISTERED_NAMESPACE_ZERO_ARGUMENTS_INSTRUCTION));
            interpreter.execute(feed);
        }

        @Test
        public void validate_FilledLibrary_NonExistingInstruction_DoesntValidate() {
            ValidateInstructionLineEvent event = new ValidateInstructionLineEvent(NO_NAMESPACE_NON_EXISTING_INSTRUCTION);
            feed.add(event);
            interpreter.execute(feed);
            assertFalse(event.isValid());
            assertFalse(event.isArgumentsAreIncompatible());
            assertTrue(event.isInstructionNotFound());
            assertFalse(event.isLineHasToFewArguments());
            assertFalse(event.isMultipleInstructionsFound());
            assertEquals(0, event.getUserErrorFlags());
        }

        @Test(expected = InstructionNotFoundException.class)
        public void execute_FilledLibrary_NonExistingInstruction_ThrowsInstructionNotFoundException() {
            feed.add(new ExecuteInstructionLineEvent(NO_NAMESPACE_NON_EXISTING_INSTRUCTION));
            interpreter.execute(feed);
        }

        @Test
        public void validate_FilledLibrary_ZeroArgumentsInstruction_Integer_DoesValidate() {
            ValidateInstructionLineEvent event = new ValidateInstructionLineEvent(
                    NO_NAMESPACE_ZERO_ARGUMENTS_INSTRUCTION, 1);
            feed.add(event);
            interpreter.execute(feed);
            assertTrue(event.isValid());
            assertFalse(event.isArgumentsAreIncompatible());
            assertFalse(event.isInstructionNotFound());
            assertFalse(event.isLineHasToFewArguments());
            assertFalse(event.isMultipleInstructionsFound());
            assertEquals(0, event.getUserErrorFlags());
        }

        @Test
        public void execute_FilledLibrary_ZeroArgumentsInstruction_Integer_DoesExecute() {
            feed.add(new ExecuteInstructionLineEvent(NO_NAMESPACE_ZERO_ARGUMENTS_INSTRUCTION, 1));
            interpreter.execute(feed);
            assertEquals(1, library.calledCount());
        }

        @Test
        public void validate_FilledLibrary_OneArgumentInstruction_Void_DoesntValidate() {
            ValidateInstructionLineEvent event = new ValidateInstructionLineEvent(NO_NAMESPACE_ONE_ARGUMENT_INSTRUCTION);
            feed.add(event);
            interpreter.execute(feed);
            assertFalse(event.isValid());
            assertFalse(event.isArgumentsAreIncompatible());
            assertFalse(event.isInstructionNotFound());
            assertTrue(event.isLineHasToFewArguments());
            assertFalse(event.isMultipleInstructionsFound());
            assertEquals(0, event.getUserErrorFlags());
        }

        @Test(expected = ToFewArgumentsException.class)
        public void execute_FilledLibrary_OneArgumentInstruction_Void_ThrowsToFewArgumentsException() {
            feed.add(new ExecuteInstructionLineEvent(NO_NAMESPACE_ONE_ARGUMENT_INSTRUCTION));
            interpreter.execute(feed);
        }

        @Test
        public void validate_FilledLibrary_OneArgumentInstruction_String_DoesntValidate() {
            ValidateInstructionLineEvent event = new ValidateInstructionLineEvent(NO_NAMESPACE_ONE_ARGUMENT_INSTRUCTION,
                                                                                  "bla");
            feed.add(event);
            interpreter.execute(feed);
            assertFalse(event.isValid());
            assertTrue(event.isArgumentsAreIncompatible());
            assertFalse(event.isInstructionNotFound());
            assertFalse(event.isLineHasToFewArguments());
            assertFalse(event.isMultipleInstructionsFound());
            assertEquals(0, event.getUserErrorFlags());
        }

        @Test(expected = ArgumentsAreIncompatibleException.class)
        public void execute_FilledLibrary_OneArgumentInstruction_String_ThrowsArgumentsAreIncompatibleException() {
            feed.add(new ExecuteInstructionLineEvent(NO_NAMESPACE_ONE_ARGUMENT_INSTRUCTION, "bla"));
            interpreter.execute(feed);
        }

        @Test
        public void validate_FilledLibrary_OneArgumentInstruction_Integer_DoesValidate() {
            ValidateInstructionLineEvent event = new ValidateInstructionLineEvent(NO_NAMESPACE_ONE_ARGUMENT_INSTRUCTION,
                                                                                  1);
            feed.add(event);
            interpreter.execute(feed);
            assertTrue(event.isValid());
            assertFalse(event.isArgumentsAreIncompatible());
            assertFalse(event.isInstructionNotFound());
            assertFalse(event.isLineHasToFewArguments());
            assertFalse(event.isMultipleInstructionsFound());
            assertEquals(0, event.getUserErrorFlags());
        }

        @Test
        public void execute_FilledLibrary_OneArgumentInstruction_Integer_DoesExecute() {
            feed.add(new ExecuteInstructionLineEvent(NO_NAMESPACE_ONE_ARGUMENT_INSTRUCTION, 1));
            interpreter.execute(feed);
            assertEquals(1, library.calledCount());
        }

        @Test
        public void validate_FilledLibrary_OneArgumentInstruction_Int_DoesValidate() {
            ValidateInstructionLineEvent event = new ValidateInstructionLineEvent(NO_NAMESPACE_ONE_ARGUMENT_INSTRUCTION,
                                                                                  1);
            feed.add(event);
            interpreter.execute(feed);
            assertTrue(event.isValid());
            assertFalse(event.isArgumentsAreIncompatible());
            assertFalse(event.isInstructionNotFound());
            assertFalse(event.isLineHasToFewArguments());
            assertFalse(event.isMultipleInstructionsFound());
            assertEquals(0, event.getUserErrorFlags());
        }

        @Test
        public void execute_FilledLibrary_OneArgumentInstruction_Int_DoesExecute() {
            feed.add(new ExecuteInstructionLineEvent(NO_NAMESPACE_ONE_ARGUMENT_INSTRUCTION, 1));
            interpreter.execute(feed);
            assertEquals(1, library.calledCount());
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public class MultipleFilledLibrary {

        private TestFilledLibrarySpy library;

        @Before
        public void setup() {
            library = new TestFilledLibrarySpy(REGISTRED_NAMESPACE);
            feed.add(new AddLibraryEvent(library));
            feed.add(new AddLibraryEvent(new TestFilledLibrarySpy(REGISTRED_NAMESPACE2)));
        }

        @SuppressWarnings("PublicInnerClass")
        public class WithoutEventParameter {

            @Test
            public void validate_FilledLibrary_OneArgumentInstruction_NoNamespace_DoesntValidate() {
                ValidateInstructionLineEvent event = new ValidateInstructionLineEvent(
                        NO_NAMESPACE_ONE_ARGUMENT_INSTRUCTION, 1);
                feed.add(event);
                interpreter.execute(feed);
                assertFalse(event.isValid());
                assertFalse(event.isArgumentsAreIncompatible());
                assertFalse(event.isInstructionNotFound());
                assertFalse(event.isLineHasToFewArguments());
                assertTrue(event.isMultipleInstructionsFound());
                assertEquals(0, event.getUserErrorFlags());
            }

            @Test(expected = MultipleInstructionsFoundException.class)
            public void execute_FilledLibrary_OneArgumentInstruction_NoNamespace_ThrowsMultipleInstructionsFoundException() {
                feed.add(new ExecuteInstructionLineEvent(NO_NAMESPACE_ONE_ARGUMENT_INSTRUCTION, 1));
                interpreter.execute(feed);
            }

            @Test
            public void validate_FilledLibrary_OneArgumentInstruction_NoNamespace_DoesValidate() {
                ValidateInstructionLineEvent event = new ValidateInstructionLineEvent(
                        REGISTRED_NAMESPACE_ONE_ARGUMENT_INSTRUCTION, 1);
                feed.add(event);
                interpreter.execute(feed);
                assertTrue(event.isValid());
                assertFalse(event.isArgumentsAreIncompatible());
                assertFalse(event.isInstructionNotFound());
                assertFalse(event.isLineHasToFewArguments());
                assertFalse(event.isMultipleInstructionsFound());
                assertEquals(0, event.getUserErrorFlags());
            }

            @Test
            public void execute_FilledLibrary_OneArgumentInstruction_NoNamespace_DoesExecute() {
                feed.add(new ExecuteInstructionLineEvent(REGISTRED_NAMESPACE_ONE_ARGUMENT_INSTRUCTION, 1));
                interpreter.execute(feed);
                assertEquals(1, library.calledCount());
            }
        }

        @SuppressWarnings("PublicInnerClass")
        public class WithEventParameter {

            @Test
            public void validate_FilledLibrary_EventOneArgumentInstruction_NoNamespace_DoesntValidate() {
                ValidateInstructionLineEvent event = new ValidateInstructionLineEvent(
                        NO_NAMESPACE_INVOKER_ONE_ARGUMENT_INSTRUCTION, 1);
                feed.add(event);
                interpreter.execute(feed);
                assertFalse(event.isValid());
                assertFalse(event.isArgumentsAreIncompatible());
                assertFalse(event.isInstructionNotFound());
                assertFalse(event.isLineHasToFewArguments());
                assertTrue(event.isMultipleInstructionsFound());
                assertEquals(0, event.getUserErrorFlags());
            }

            @Test(expected = MultipleInstructionsFoundException.class)
            public void execute_FilledLibrary_EventOneArgumentInstruction_NoNamespace_ThrowsMultipleInstructionsFoundException() {
                feed.add(new ExecuteInstructionLineEvent(NO_NAMESPACE_INVOKER_ONE_ARGUMENT_INSTRUCTION, 1));
                interpreter.execute(feed);
            }

            @Test
            public void validate_FilledLibrary_EventOneArgumentInstruction_NoNamespace_DoesValidate() {
                ValidateInstructionLineEvent event = new ValidateInstructionLineEvent(
                        REGISTRED_NAMESPACE_INVOKER_ONE_ARGUMENT_INSTRUCTION, 1);
                feed.add(event);
                interpreter.execute(feed);
                assertTrue(event.isValid());
                assertFalse(event.isArgumentsAreIncompatible());
                assertFalse(event.isInstructionNotFound());
                assertFalse(event.isLineHasToFewArguments());
                assertFalse(event.isMultipleInstructionsFound());
                assertEquals(0, event.getUserErrorFlags());
            }

            @Test
            public void execute_FilledLibrary_EventOneArgumentInstruction_NoNamespace_DoesExecute() {
                feed.add(new ExecuteInstructionLineEvent(REGISTRED_NAMESPACE_INVOKER_ONE_ARGUMENT_INSTRUCTION, 1));
                interpreter.execute(feed);
                assertEquals(1, library.calledCount());
            }
        }
    }
}
