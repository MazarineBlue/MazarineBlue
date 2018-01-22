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

import org.junit.Test;
import org.mazarineblue.keyworddriven.exceptions.DeclaringMethodClassNotEqualToCalleeException;
import org.mazarineblue.keyworddriven.exceptions.InstructionInaccessibleException;
import org.mazarineblue.keyworddriven.exceptions.KeywordConflictException;
import org.mazarineblue.keyworddriven.exceptions.LibraryInaccessibleException;
import org.mazarineblue.keyworddriven.exceptions.PrimativesNotAllowedByondMinimumBorderException;
import org.mazarineblue.keyworddriven.util.libraries.AbstractTestLibrary;
import org.mazarineblue.keyworddriven.util.libraries.ConflictingKeywordsLibrary;
import org.mazarineblue.keyworddriven.util.libraries.PrimativesBeyondBoundryLibrary;
import org.mazarineblue.keyworddriven.util.libraries.PrivateInstructionLibrary;
import org.mazarineblue.keyworddriven.util.libraries.WrongCallerDuringInstallationLibrary;

public class LibraryInitialisationTest {

    @Test(expected = LibraryInaccessibleException.class)
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void init_PriveLibrary() {
        new PrivateTestLibrary();
    }

    @Test(expected = InstructionInaccessibleException.class)
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void init_PriveInstruction() {
        new PrivateInstructionLibrary();
    }

    @Test(expected = PrimativesNotAllowedByondMinimumBorderException.class)
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void init_PrimativesBoyondBoundry() {
        new PrimativesBeyondBoundryLibrary();
    }

    @Test(expected = KeywordConflictException.class)
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void init_ConflictingKeywords() {
        new ConflictingKeywordsLibrary();
    }

    @Test(expected = DeclaringMethodClassNotEqualToCalleeException.class)
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void init_WrongCaller() {
        new WrongCallerDuringInstallationLibrary();
    }

    private static class PrivateTestLibrary
            extends AbstractTestLibrary {

        @Keyword("Test")
        public void test() {
            throw new UnsupportedOperationException("Never called.");
        }
    }
}
