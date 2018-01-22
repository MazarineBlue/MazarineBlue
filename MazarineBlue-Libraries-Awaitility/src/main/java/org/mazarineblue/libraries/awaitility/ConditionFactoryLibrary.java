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
package org.mazarineblue.libraries.awaitility;

import static java.lang.Boolean.TRUE;
import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import java.util.function.BiFunction;
import org.awaitility.Awaitility;
import org.awaitility.core.ConditionFactory;
import org.mazarineblue.eventdriven.Invoker;
import org.mazarineblue.keyworddriven.Beta;
import org.mazarineblue.keyworddriven.Keyword;
import org.mazarineblue.keyworddriven.Parameters;
import org.mazarineblue.keyworddriven.PassInvoker;
import org.mazarineblue.keyworddriven.events.ExecuteInstructionLineEvent;
import org.mazarineblue.keyworddriven.events.RemoveLibraryEvent;

public class ConditionFactoryLibrary
        extends AbstractAwaitilityLibrary {

    private ConditionFactory factory;

    ConditionFactoryLibrary() {
        factory = Awaitility.await();
    }

    public static ConditionFactory apply(Duration duration, BiFunction<Long, TimeUnit, ConditionFactory> func) {
        long seconds = duration.getSeconds();
        return seconds != 0
                ? func.apply(seconds, SECONDS)
                : func.apply((long) duration.getNano(), NANOSECONDS);
    }
    
    @Keyword("At least")
    @Parameters(min = 1)
    @PassInvoker
    public void atLeast(Invoker invoker, Duration timeout) {
        factory = apply(timeout, factory::atLeast);
    }

    @Keyword("At most")
    @Keyword("Timeout")
    @Parameters(min = 1)
    @PassInvoker
    public void atMost(Invoker invoker, Duration timeout) {
        factory = apply(timeout, factory::atMost);
    }

    @Keyword("Between")
    @Parameters(min = 2)
    @PassInvoker
    public void between(Invoker invoker, Duration atLeast, Duration atMost) {
        factory = apply(atLeast, factory::atLeast);
        factory = apply(atMost, factory::atMost);
    }

    @Keyword("Poll delay")
    @Parameters(min = 1)
    @PassInvoker
    public void pollDelay(Invoker invoker, Duration delay) {
        factory = apply(delay, factory::pollDelay);
    }

    @Keyword("Poll interval")
    @Parameters(min = 1)
    @PassInvoker
    public void pollInterval(Invoker invoker, Duration pollInterval) {
        factory = apply(pollInterval, factory::pollInterval);
    }
    
    @Beta
    @Keyword("Message")
    @PassInvoker
    public void await(Invoker invoker, String alias) {
        factory = alias == null ? factory.await() : factory.await(alias);
    }

    @Keyword("Forever")
    @PassInvoker
    public void forever(Invoker invoker) {
        factory = factory.forever();
    }

    @PassInvoker
    @Keyword("Until")
    @Parameters(min = 1)
    public void until(Invoker invoker, String path, Object... arguments) {
        factory.until(() -> instructionReturnsTrue(invoker, path, arguments));
        removeLibrary(invoker);
    }

    private Boolean instructionReturnsTrue(Invoker invoker, String path, Object... arguments) {
        ExecuteInstructionLineEvent e = new ExecuteInstructionLineEvent(path, arguments);
        e.setExpectResultType(Boolean.class);
        invoker.publish(e);
        return Objects.equals(TRUE, e.getResult());
    }

    private void removeLibrary(Invoker invoker) {
        invoker.publish(new RemoveLibraryEvent(this));
    }
}
