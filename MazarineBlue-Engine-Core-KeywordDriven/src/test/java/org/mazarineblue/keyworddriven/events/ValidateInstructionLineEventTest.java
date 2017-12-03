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
package org.mazarineblue.keyworddriven.events;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mazarineblue.utililities.util.TestHashCodeAndEquals;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
@RunWith(HierarchicalContextRunner.class)
public class ValidateInstructionLineEventTest
        extends TestHashCodeAndEquals<ValidateInstructionLineEvent> {

    @Test
    public void hashCode_DifferentFlags() {
        ValidateInstructionLineEvent a = new ValidateInstructionLineEvent("foo", "oof");
        ValidateInstructionLineEvent b = new ValidateInstructionLineEvent("foo", "oof");
        a.setArgumentsAreIncompatible();
        b.setInstructionsNotFound();
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void equals_DifferentFlags() {
        ValidateInstructionLineEvent a = new ValidateInstructionLineEvent("foo", "oof");
        ValidateInstructionLineEvent b = new ValidateInstructionLineEvent("foo", "oof");
        a.setArgumentsAreIncompatible();
        b.setInstructionsNotFound();
        assertFalse(a.equals(b));
    }

    @Override
    protected ValidateInstructionLineEvent getObject() {
        ValidateInstructionLineEvent e = new ValidateInstructionLineEvent("foo", "oof");
        e.setArgumentsAreIncompatible();
        e.setUserErrorFlags(7);
        return e;
    }

    @Override
    protected ValidateInstructionLineEvent getDifferentObject() {
        ValidateInstructionLineEvent e = new ValidateInstructionLineEvent("foo", "oof");
        e.setUserErrorFlags(1);
        return e;
    }
}
