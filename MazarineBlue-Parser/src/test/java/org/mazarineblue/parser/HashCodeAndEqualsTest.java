/*
 * Copyright (c) 2017 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.mazarineblue.parser;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import static org.junit.Assert.assertNotEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mazarineblue.parser.expressions.Expression;
import static org.mazarineblue.parser.expressions.Expression.addition;
import static org.mazarineblue.parser.expressions.Expression.leaf;
import static org.mazarineblue.parser.expressions.Expression.minus;
import org.mazarineblue.utilities.util.TestHashCodeAndEquals;

@RunWith(HierarchicalContextRunner.class)
public class HashCodeAndEqualsTest {

    @SuppressWarnings("PublicInnerClass")
    public class LeafHCAE
            extends TestHashCodeAndEquals<Expression> {

        @Override
        protected Expression getObject() {
            return leaf("a");
        }

        @Override
        protected Expression getDifferentObject() {
            return leaf("b");
        }
    };

    @SuppressWarnings("PublicInnerClass")
    public class UnaryNodeHCAE
            extends TestHashCodeAndEquals<Expression> {

        @Override
        protected Expression getObject() {
            return minus(leaf("1"));
        }

        @Override
        protected Expression getDifferentObject() {
            return minus(leaf("2"));
        }
    };

    @SuppressWarnings("PublicInnerClass")
    public class BinaryNodeHCAE
            extends TestHashCodeAndEquals<Expression> {

        @Override
        protected Expression getObject() {
            return addition(leaf("1"), leaf("2"));
        }

        @Override
        protected Expression getDifferentObject() {
            return addition(leaf("2"), leaf("2"));
        }

        @Test
        public void hashCode_DifferentRightNodes() {
            int a = addition(leaf("1"), leaf("2")).hashCode();
            int b = addition(leaf("1"), leaf("3")).hashCode();
            assertNotEquals(a, b);
        }

        @Test
        public void equals_DifferentRightNodes() {
            Expression a = addition(leaf("1"), leaf("2"));
            Expression b = addition(leaf("1"), leaf("3"));
            assertNotEquals(a, b);
        }
    };
}
