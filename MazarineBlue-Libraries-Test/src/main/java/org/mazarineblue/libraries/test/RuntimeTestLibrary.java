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

import static java.lang.String.format;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import org.mazarineblue.eventdriven.Feed;
import org.mazarineblue.eventdriven.feeds.MemoryFeed;
import org.mazarineblue.eventnotifier.EventHandler;
import org.mazarineblue.keyworddriven.Keyword;
import org.mazarineblue.keyworddriven.Parameters;
import org.mazarineblue.libraries.test.events.ExecuteSetupEvent;
import org.mazarineblue.libraries.test.events.ExecuteTeardownEvent;
import org.mazarineblue.libraries.test.events.ExecuteTestEvent;
import org.mazarineblue.libraries.test.model.Key;
import static org.mazarineblue.libraries.test.model.Status.PASS;
import org.mazarineblue.libraries.test.model.listeners.TestListener;
import org.mazarineblue.libraries.test.model.tests.Test;

public class RuntimeTestLibrary
        extends AbstractTestLibrary {

    private final Set<Key> executedSuiteSetups = new TreeSet<>(Key.naturalOrder());
    private final TestListener listener;
    private final Test test;

    public RuntimeTestLibrary(TestListener listener, Test test) {
        this.listener = listener;
        this.test = test;
    }

    @EventHandler
    public void eventHandler(ExecuteSetupEvent event) {
        try {
            Feed feed = new MemoryFeed(event.getEvents());
            event.invoker().processor().execute(feed);
            executedSuiteSetups.add(event.getSuiteKey());
        } catch (RuntimeException ex) {
            // This exception is already registred with the TestListener and should not be thrown.
        } finally {
            event.setConsumed(true);
        }
    }

    @EventHandler
    public void eventHandler(ExecuteTestEvent event) {
        try {
            if (!test.result().status().equals(PASS))
                return;
            Feed feed = new MemoryFeed(event.getEvents());
            event.invoker().processor().execute(feed);
        } catch (RuntimeException ex) {
            // This exception is already registred with the TestListener and should not be thrown.
        } finally {
            event.setConsumed(true);
        }
    }

    @EventHandler
    public void eventHandler(ExecuteTeardownEvent event) {
        try {
            if (!executedSuiteSetups.contains(event.getSuiteKey()))
                return;
            Feed feed = new MemoryFeed(event.getEvents());
            event.invoker().processor().execute(feed);
        } catch (RuntimeException ex) {
            // This exception is already registred with the TestListener and should not be thrown.
        } finally {
            event.setConsumed(true);
        }
    }

    @Keyword("Check equals")
    @Parameters(min = 2)
    public void checkEqual(Object expected, Object actual) {
        if (!expected.equals(actual))
            addError(format("%s is not equal to %s", expected, actual));
    }

    @Keyword("Check not equals")
    @Parameters(min = 2)
    public void checkNotEqual(Object expected, Object actual) {
        if (expected.equals(actual))
            addError(format("%s is equal to %s", expected, actual));
    }

    @Keyword("Fail")
    public void fail(String errorMessage) {
        addError(errorMessage);
    }

    private void addError(String errorMessage) {
        test.result().setFailed();
        listener.error(test, errorMessage);
    }

    @Override
    public int hashCode() {
        return 59 * 7
                + Objects.hashCode(this.test);
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj != null && getClass() == obj.getClass()
                && Objects.equals(this.test, ((RuntimeTestLibrary) obj).test);
    }
}
