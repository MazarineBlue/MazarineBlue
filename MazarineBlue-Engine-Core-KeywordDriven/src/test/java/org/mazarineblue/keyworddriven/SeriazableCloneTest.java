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
import org.mazarineblue.eventbus.util.TestPredicate;
import org.mazarineblue.keyworddriven.events.AddLibraryEvent;
import org.mazarineblue.keyworddriven.events.CommentInstructionLineEvent;
import org.mazarineblue.keyworddriven.events.CountLibrariesEvent;
import org.mazarineblue.keyworddriven.events.ExecuteInstructionLineEvent;
import org.mazarineblue.keyworddriven.events.FetchLibrariesEvent;
import org.mazarineblue.keyworddriven.events.RemoveLibraryEvent;
import org.mazarineblue.keyworddriven.events.ValidateInstructionLineEvent;
import org.mazarineblue.keyworddriven.util.TestLibraryWithInstructions;
import org.mazarineblue.utililities.ObjectsUtil;

public class SeriazableCloneTest {

    @Test
    public void clone_AddLibraryEvent() {
        AddLibraryEvent expected = new AddLibraryEvent(new TestLibraryWithInstructions(""));
        assertEquals(expected, ObjectsUtil.clone(expected));
    }

    @Test
    public void clone_RemoveLibraryEvent() {
        RemoveLibraryEvent expected = new RemoveLibraryEvent(new TestLibraryWithInstructions(""));
        assertEquals(expected, ObjectsUtil.clone(expected));
    }

    @Test
    public void clone_CommentInstructionLineEvent() {
        CommentInstructionLineEvent expected = new CommentInstructionLineEvent("arg1", "arg2");
        assertEquals(expected, ObjectsUtil.clone(expected));
    }

    @Test
    public void clone_ExecuteInstructionLineEvent() {
        ExecuteInstructionLineEvent expected = new ExecuteInstructionLineEvent("arg1", "arg2");
        expected.setResult(3);
        assertEquals(expected, ObjectsUtil.clone(expected));
    }

    @Test
    public void clone_ValidateInstructionLineEvent() {
        ValidateInstructionLineEvent expected = new ValidateInstructionLineEvent("arg1", "arg2");
        assertEquals(expected, ObjectsUtil.clone(expected));
    }

    @Test
    public void clone_CountLibrariesEvent() {
        Predicate<Library> condition = new TestPredicate<>(true);
        CountLibrariesEvent expected = new CountLibrariesEvent(condition);
        expected.addToCount(asList(new TestLibraryWithInstructions("")));
        assertEquals(expected, ObjectsUtil.clone(expected));
    }

    @Test
    public void clone_FetchLibrariesEvent() {
        Predicate<Library> condition = new TestPredicate<>(true);
        FetchLibrariesEvent expected = new FetchLibrariesEvent(condition);
        expected.addLibrary(new TestLibraryWithInstructions(""));
        assertEquals(expected, ObjectsUtil.clone(expected));
    }
}
