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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.mazarineblue.eventnotifier.DummyEvent;
import org.mazarineblue.eventnotifier.Event;
import org.mazarineblue.keyworddriven.events.InstructionLineEvent;
import static org.mazarineblue.keyworddriven.events.InstructionLineEvent.matchesAnyKeywords;
import static org.mazarineblue.keyworddriven.events.InstructionLineEvent.matchesNoneKeywords;

public class StaticInstructionLineTest {

    @Test
    public void matchesAnyKeywords_DifferentTypeEvent() {
        Event e = new DummyEvent();
        assertFalse(matchesAnyKeywords("foo", "keyword").test(e));
    }

    @Test
    public void matchesNoneKeywords_DifferentTypeEvent() {
        Event e = new DummyEvent();
        assertTrue(matchesNoneKeywords("foo", "keyword").test(e));
    }

    @Test
    public void matchesAnyKeywords_KeywordNotInEvent() {
        Event e = new MyInstruction("my keyword");
        assertFalse(matchesAnyKeywords("foo", "keyword").test(e));
    }

    @Test
    public void matchesNoneKeywords_KeywordNotInEvent() {
        Event e = new MyInstruction("my keyword");
        assertTrue(matchesNoneKeywords("foo", "keyword").test(e));
    }

    @Test
    public void matchesAnyKeywords_KeywordInEvent() {
        Event e = new MyInstruction("namespace.keyword");
        assertTrue(matchesAnyKeywords("foo", "keyword").test(e));
    }

    @Test
    public void matchesNoneKeywords_KeywordInEvent() {
        Event e = new MyInstruction("namespace.keyword");
        assertFalse(matchesNoneKeywords("foo", "keyword").test(e));
    }

    @SuppressWarnings("serial")
    private static class MyInstruction
            extends InstructionLineEvent {

        private MyInstruction(String path, Object... arguments) {
            super(path, arguments);
        }
    }
}
