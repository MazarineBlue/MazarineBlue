/*
 * Copyright (c) 2016 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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

import org.junit.After;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import org.junit.Before;
import org.junit.Test;
import static org.mazarineblue.keyworddriven.util.MethodsUtil.getPublicMethod;
import static org.mazarineblue.keyworddriven.util.MethodsUtil.getPublicMethodWithOtherName;
import static org.mazarineblue.keyworddriven.util.MethodsUtil.getPublicMethodWithOtherParameterTypes;
import org.mazarineblue.utililities.util.TestHashCodeAndEquals;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class InstructionTest
        extends TestHashCodeAndEquals<Object> {

    private Instruction instruction;

    @Before
    public void setup() {
        instruction = new Instruction(getPublicMethod());
    }

    @After
    public void teardown() {
        instruction = null;
    }

    @Test
    @SuppressWarnings("IncompatibleEquals")
    public void hashCode_DifferentMethodName() {
        Instruction other = new Instruction(getPublicMethodWithOtherName());
        assertNotEquals(instruction.hashCode(), other.hashCode());
    }

    @Test
    @SuppressWarnings("IncompatibleEquals")
    public void equals_DifferentMethodName() {
        Instruction other = new Instruction(getPublicMethodWithOtherName());
        assertFalse(instruction.equals(other));
    }

    @Override
    protected Object getIdenticalObject() {
        return new Instruction(getPublicMethod());
    }

    @Override
    protected Object getObject() {
        return instruction;
    }

    @Override
    protected Object getDifferentObject() {
        return new Instruction(getPublicMethodWithOtherParameterTypes());
    }
}
