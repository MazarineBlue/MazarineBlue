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
package org.mazarineblue.fitnesse;

import fitnesse.testsystems.Descriptor;
import fitnesse.testsystems.TestExecutionException;
import fitnesse.testsystems.TestPage;
import fitnesse.testsystems.TestSystem;
import fitnesse.testsystems.TestSystemFactory;
import fitnesse.testsystems.TestSystemListener;
import fitnesse.testsystems.UnableToStartException;
import fitnesse.testsystems.UnableToStopException;

public class TestFactory
        implements TestSystemFactory {

    public TestFactory() {
    }

    @Override
    public TestSystem create(Descriptor d) {
        return new TestSystem() {
            @Override
            public String getName() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void start()
                    throws UnableToStartException {
            }

            @Override
            public void bye()
                    throws UnableToStopException {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void kill() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void runTests(TestPage tp)
                    throws TestExecutionException {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public boolean isSuccessfullyStarted() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void addTestSystemListener(TestSystemListener tl) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
    }
}
