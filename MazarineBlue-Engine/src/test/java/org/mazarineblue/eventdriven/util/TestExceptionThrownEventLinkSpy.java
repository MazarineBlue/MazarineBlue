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
package org.mazarineblue.eventdriven.util;

import org.mazarineblue.eventbus.Event;
import org.mazarineblue.eventbus.EventHandler;
import org.mazarineblue.eventdriven.Link;
import org.mazarineblue.eventdriven.events.ExceptionThrownEvent;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class TestExceptionThrownEventLinkSpy
        extends Link {

    private RuntimeException exception;
    private Event owner;
    private String message;

    @EventHandler
    public void eventHandler(ExceptionThrownEvent event) {
        exception = event.getException();
        owner = event.getCause();
        message = event.message();
    }

    public RuntimeException getException() {
        return exception;
    }

    public Event getOwner() {
        return owner;
    }

    public String message() {
        return message;
    }
}
