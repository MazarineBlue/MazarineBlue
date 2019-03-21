/*
 * Copyright (c) Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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
package org.mazarineblue.utilities;

import java.util.Objects;
import java.util.function.Predicate;

public class PredicateIdentifierWrapper<T>
        implements Predicate<T> {

    private Predicate<T> wrapped;
    private String identifier;

    public PredicateIdentifierWrapper(Predicate<T> wrapped) {
        this.wrapped = wrapped;
    }

    public PredicateIdentifierWrapper<T> setIdentifier(String identifier) {
        this.identifier = identifier;
        return this;
    }

    @Override
    public String toString() {
        return "identifier=" + identifier;
    }

    @Override
    public boolean test(T t) {
        return wrapped.test(t);
    }

    @Override
    public int hashCode() {
        return 53 * 5 + Objects.hashCode(this.identifier);
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj != null && getClass() == obj.getClass()
                && Objects.equals(this.identifier, ((PredicateIdentifierWrapper) obj).identifier);
    }

    @Override
    public Predicate<T> or(Predicate<? super T> other) {
        Objects.requireNonNull(other);
        PredicateIdentifierWrapper<T> p = new PredicateIdentifierWrapper<>((t) -> test(t) || other.test(t));
        p.identifier = this.identifier;
        if (other instanceof TestPredicate)
            p.identifier += " (or) " + ((PredicateIdentifierWrapper<T>) other).identifier;
        return p;
    }

    @Override
    public Predicate<T> negate() {
        PredicateIdentifierWrapper<T> p = new PredicateIdentifierWrapper<>((t) -> !test(t));
        p.identifier = this.identifier;
        return p;
    }

    @Override
    public Predicate<T> and(Predicate<? super T> other) {
        Objects.requireNonNull(other);
        PredicateIdentifierWrapper<T> p = new PredicateIdentifierWrapper<>((t) -> test(t) && other.test(t));
        p.identifier = this.identifier;
        if (other instanceof TestPredicate)
            p.identifier += " (and) " + ((PredicateIdentifierWrapper<T>) other).identifier;
        return p;
    }
}
