/*
 * Copyright (c) 2017 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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
package org.mazarineblue.libraries.test;

import java.util.Objects;
import org.mazarineblue.eventdriven.Invoker;
import org.mazarineblue.eventnotifier.EventHandler;
import org.mazarineblue.keyworddriven.Keyword;
import org.mazarineblue.keyworddriven.PassInvoker;
import org.mazarineblue.keyworddriven.events.AddLibraryEvent;
import org.mazarineblue.libraries.test.events.SetTestListenerEvent;
import org.mazarineblue.libraries.test.model.TestSystem;
import org.mazarineblue.libraries.test.model.listeners.TestListener;

public class MainTestLibrary
        extends AbstractTestLibrary {

    private TestSystem system;
    private TestListener listener = null;

    public MainTestLibrary(TestSystem system) {
        this.system = system;
    }

    @EventHandler
    public void eventHandler(SetTestListenerEvent event) {
        listener = event.getTestListener();
        event.setConsumed(true);
    }

    @PassInvoker
    @Keyword("Test set")
    public void testSystem(Invoker invoker) {
        system = TestSystem.newInstance(invoker);
        invoker.publish(new AddLibraryEvent(new RecorderTestLibrary(system)));
    }

    @PassInvoker
    @Keyword("Run tests")
    public void runTest(Invoker invoker) {
        if (listener != null)
            system.setTestListener(listener);
        system.run();
    }

    @Override
    public int hashCode() {
        return 97 * 5 + Objects.hashCode(this.system);
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj != null && getClass() == obj.getClass()
                && Objects.equals(this.system, ((MainTestLibrary) obj).system);
    }
}
