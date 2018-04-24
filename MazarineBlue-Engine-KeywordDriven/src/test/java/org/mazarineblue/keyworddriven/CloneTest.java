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

import static java.util.Arrays.asList;
import java.util.function.Predicate;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.mazarineblue.eventnotifier.Event;
import org.mazarineblue.keyworddriven.events.AddLibraryEvent;
import org.mazarineblue.keyworddriven.events.CommentInstructionLineEvent;
import org.mazarineblue.keyworddriven.events.ContainsConversionRuleEvent;
import org.mazarineblue.keyworddriven.events.ContainsLibrariesEvent;
import org.mazarineblue.keyworddriven.events.CountLibrariesEvent;
import org.mazarineblue.keyworddriven.events.ExecuteInstructionLineEvent;
import org.mazarineblue.keyworddriven.events.FetchLibrariesEvent;
import org.mazarineblue.keyworddriven.events.IsAbleToProcessInstructionLineEvent;
import org.mazarineblue.keyworddriven.events.RegisterConversionRuleEvent;
import org.mazarineblue.keyworddriven.events.RemoveLibraryEvent;
import org.mazarineblue.keyworddriven.events.ValidateInstructionLineEvent;
import org.mazarineblue.keyworddriven.util.Converted;
import org.mazarineblue.keyworddriven.util.Input;
import org.mazarineblue.keyworddriven.util.libraries.TestLibrary1;
import org.mazarineblue.utilities.ObjectsUtil;
import org.mazarineblue.utilities.TestPredicate;

public class CloneTest {

    /* ********************************************************************** *
     *                            Library events                              *
     * ********************************************************* ,,^..^,, *** */
    @Test
    public void clone_AddLibraryEvent() {
        Event e = new AddLibraryEvent(new TestLibrary1());
        assertEquals(e, ObjectsUtil.clone(e));
    }

    @Test
    public void clone_RemoveLibraryEvent() {
        Event e = new RemoveLibraryEvent(new TestLibrary1());
        assertEquals(e, ObjectsUtil.clone(e));
    }

    @Test
    public void clone_ContainsLibrariesEvent() {
        Predicate<Library> condition = new TestPredicate<>(true);
        ContainsLibrariesEvent e = new ContainsLibrariesEvent(condition);
        e.contains(asList(new TestLibrary1()));
        assertEquals(e, ObjectsUtil.clone(e));
    }

    @Test
    public void clone_CountLibrariesEvent() {
        Predicate<Library> condition = new TestPredicate<>(true);
        CountLibrariesEvent e = new CountLibrariesEvent(condition);
        e.addToCount(asList(new TestLibrary1()));
        assertEquals(e, ObjectsUtil.clone(e));
    }

    @Test
    public void clone_FetchLibrariesEvent() {
        Predicate<Library> condition = new TestPredicate<>(true);
        FetchLibrariesEvent e = new FetchLibrariesEvent(condition);
        e.addLibrary(new TestLibrary1());
        assertEquals(e, ObjectsUtil.clone(e));
    }

    /* ********************************************************************** *
     *                          Instruction events                            *
     * ********************************************************* ,,^..^,, *** */
    @Test
    public void clone_CommentInstructionLineEvent() {
        Event e = new CommentInstructionLineEvent("arg1", "arg2");
        assertEquals(e, ObjectsUtil.clone(e));
    }

    @Test
    public void clone_ExecuteInstructionLineEvent() {
        ExecuteInstructionLineEvent e = new ExecuteInstructionLineEvent("arg1", "arg2");
        e.setResult(3);
        assertEquals(e, ObjectsUtil.clone(e));
    }

    @Test
    public void clone_ValidateInstructionLineEvent() {
        Event e = new ValidateInstructionLineEvent("arg1", "arg2");
        assertEquals(e, ObjectsUtil.clone(e));
    }

    @Test
    public void clone_IsAbleToProcessInstructionLineEvent() {
        IsAbleToProcessInstructionLineEvent e = new IsAbleToProcessInstructionLineEvent("arg1", "arg2");
        e.setResult(true);
        assertEquals(e, ObjectsUtil.clone(e));
    }

    /* ********************************************************************** *
     *                             Other events                               *
     * ********************************************************* ,,^..^,, *** */

    @Test
    public void clone_ContainsConversionRuleEvent() {
        Event e = new ContainsConversionRuleEvent<Input, Converted>(Input.class, Converted.class);
        assertEquals(e, ObjectsUtil.clone(e));
    }

    @Test
    public void clone_RegisterConversionRuleEvent() {
        Event e = new RegisterConversionRuleEvent<Input, Converted>(Input.class, Converted.class,
                                                                    input -> new Converted());
        assertEquals(e, ObjectsUtil.clone(e));
    }
}
