/*
 * Copyright (c) 2015 Alex de Kruijff
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
package org.mazarineblue.datasources;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.datasources.exceptions.EmtpyScopeStack;
import org.mazarineblue.datasources.exceptions.ScopeNotFoundException;
import org.mazarineblue.datasources.exceptions.VariableAlreadyDeclared;
import org.mazarineblue.datasources.util.AbstractArraySourceTest;
import static org.mazarineblue.datasources.util.SourceUtil.asSet;

/**
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class BlackboardSourceTest
        extends AbstractArraySourceTest {

    private static final String IDENTIFIER = "";
    private BlackboardSource source;

    @Override
    protected void next() {
    }

    @Override
    protected void reset() {
        source.reset();
    }

    @Before
    public void setup() {
        source = new BlackboardSource(IDENTIFIER);
        source.setup();
        setup(source);
    }

    @Test
    @Override
    public void nextCalledTwice() {
        super.nextCalledTwice();
    }

    @Test(expected = UnsupportedOperationException.class)
    @Override
    public void getDataByIndex_Uninitialized() {
        super.getDataByIndex_Uninitialized();
    }

    @Test
    @Override
    public void getDataByColumn_Uninitialized() {
        super.getDataByColumn_Uninitialized();
    }

    @Test(expected = UnsupportedOperationException.class)
    @Override
    public void setDataUsingIndex_Uninitialized() {
        super.setDataUsingIndex_Uninitialized();
    }

    @Test
    @Override
    public void setDataUsingColumn_Uninitialized() {
        super.setDataUsingColumn_Uninitialized();
    }

    @Test(expected = UnsupportedOperationException.class)
    @Override
    public void getDataByIndex_UnavailableValue_ReturnsNull() {
        super.getDataByIndex_UnavailableValue_ReturnsNull();
    }

    @Test(expected = UnsupportedOperationException.class)
    @Override
    public void getDataByIndex_AvailableValue_ReturnsObject() {
        super.getDataByIndex_AvailableValue_ReturnsObject();
    }

    @Test(expected = UnsupportedOperationException.class)
    @Override
    public void setDataUsingIndexThenColumn() {
        super.setDataUsingIndexThenColumn();
    }

    @Test(expected = UnsupportedOperationException.class)
    @Override
    public void setDataUsingColumnThenIndex() {
        super.setDataUsingColumnThenIndex();
    }

    @Test
    public void declareVariable() {
        source.declareVariable("variable");
        assertEquals(asSet("variable"), source.getColumns());
        assertEquals(null, source.getData("variable"));
    }

    @Test(expected = VariableAlreadyDeclared.class)
    public void declareVariable_Twice() {
        source.declareVariable("variable");
        source.declareVariable("variable");
    }

    @Test(expected = ScopeNotFoundException.class)
    public void declareVariable_OnNonexistingScope() {
        source.declareVariable("variable", "scope");
    }

    @Test
    public void declareVariable_OnExistingScope() {
        source.pushSource("scope");
        source.declareVariable("variable", "scope");
        assertEquals(asSet("variable"), source.getColumns());
        assertEquals(null, source.getData("variable"));
    }

    @Test(expected = ScopeNotFoundException.class)
    public void declareVariable_OnRemovedScope() {
        source.pushSource("scope");
        source.declareVariable("variable", "scope");
        source.popSource("scope");
        source.declareVariable("variable", "scope");
    }
    
    @Test(expected = EmtpyScopeStack.class)
    public void popScope_EmptyStack() {
        source.popSource();
    }
    
    @Test
    public void popScope_FilledStack() {
        source.pushSource("scope");
        source.popSource();
    }
    
    @Test(expected = ScopeNotFoundException.class)
    public void popScope_NonExistingScope() {
        source.popSource("scope");
    }
    
    @Test
    public void popScope_ExistingScope() {
        source.pushSource("scope");
        source.popSource("scope");
    }
}
