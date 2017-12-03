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
package org.mazarineblue.plugins;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class RunnerTest {

    @Test
    public void test_Null() {
        RunnerSpy spy = new RunnerSpy();
        spy.execute((String[]) null);
        assertArrayEquals(null, spy.getArguments());
        assertTrue(spy.isStarted());
    }

    @Test
    public void test_WithoutArgumens() {
        RunnerSpy spy = new RunnerSpy();
        String[] expected = new String[0];
        spy.execute(expected);
        assertArrayEquals(null, spy.getArguments());
        assertTrue(spy.isStarted());
    }

    @Test
    public void test_WithArgumens() {
        RunnerSpy spy = new RunnerSpy();
        String[] expected = new String[]{"Argument 1", "Argument 2"};
        spy.execute(expected);
        assertArrayEquals(expected, spy.getArguments());
        assertTrue(spy.isStarted());
    }

    private class RunnerSpy
            implements Runner {

        private String[] args;
        private boolean started = false;

        @Override
        public void setArguments(String... args) {
            this.args = args;
        }

        public String[] getArguments() {
            return args;
        }

        @Override
        public void start() {
            started = true;
        }

        public boolean isStarted() {
            return started;
        }
    }
}
