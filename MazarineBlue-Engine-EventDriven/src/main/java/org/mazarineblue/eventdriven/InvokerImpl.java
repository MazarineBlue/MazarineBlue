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

import org.mazarineblue.eventnotifier.Event;

class InvokerImpl
        implements Invoker {

    private final Processor processor;
    private final ChainImpl chain;

    InvokerImpl(Processor processor, ChainImpl chain) {
        this.processor = processor;
        this.chain = chain;
    }

    @Override
    public Processor processor() {
        return processor;
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
