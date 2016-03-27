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

import java.lang.reflect.Method;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.mazarineblue.keyworddriven.exceptions.NoPublicMethodFoundException;
import org.mazarineblue.keyworddriven.util.MethodsUtil;

public class MethodProfileTest {

    @Test(expected = NullPointerException.class)
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void constructor_Null() {
        new MethodProfile(null);
    }

    @Test(expected = NoPublicMethodFoundException.class)
    public void getMethod_PrivateMethod() {
        Method method = MethodsUtil.getPrivateMethod();
        new MethodProfile(method).getMethod();
    }

    @Test
    public void getMethod_PublicMethod() {
        Method method = MethodsUtil.getPublicMethod();
        MethodProfile profile = new MethodProfile(method);
        assertEquals(method.getName(), profile.getMethod().getName());
    }
}
