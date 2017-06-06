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
package org.mazarineblue.threadrunner.util;

import org.mazarineblue.eventbus.Event;
import org.mazarineblue.threadrunner.ServiceRunner;
import org.mazarineblue.threadrunner.ServiceRunnerFactory;
import org.mazarineblue.utililities.TwoWayPipe;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class ServiceRunnerFactoryDummy
        implements ServiceRunnerFactory {

    @Override
    public ServiceRunner createRunner(TwoWayPipe<Event> pipe) {
        return new ServiceRunner() {

            @Override
            public void start() {
            }

            @Override
            public void bye() {
            }

            @Override
            public void kill() {
            }
        };
    }
}