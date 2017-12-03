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
package org.mazarineblue.eventdriven;

import org.mazarineblue.eventbus.Event;

class InvokerImpl
        implements Invoker {

    private final Interpreter interpreter;
    private final ChainImpl chain;

    InvokerImpl(Interpreter interpreter, ChainImpl chain) {
        this.interpreter = interpreter;
        this.chain = chain;
    }

    @Override
    public Interpreter interpreter() {
        return interpreter;
    }

    @Override
    public Chain chain() {
        return chain;
    }

    @Override
    public void publish(Event event) {
        if (event instanceof InvokerEvent)
            ((InvokerEvent) event).setInvoker(this);
        chain.publish(event);
    }
}
