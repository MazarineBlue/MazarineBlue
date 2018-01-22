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
package org.mazarineblue.runners.fitnesse;

import fitnesse.plugins.PluginFeatureFactoryBase;
import fitnesse.testrunner.TestSystemFactoryRegistry;
import org.mazarineblue.eventdriven.events.ExceptionThrownEvent;
import org.mazarineblue.eventnotifier.Event;
import static org.mazarineblue.eventnotifier.Event.matchesAny;
import static org.mazarineblue.eventnotifier.Event.matchesNone;
import static org.mazarineblue.eventnotifier.Event.matchesNoneAutoConsumable;
import org.mazarineblue.eventnotifier.subscribers.ConsumeEventsSubscriber;
import org.mazarineblue.eventnotifier.subscribers.UnconsumedExceptionThrowerSubscriber;
import org.mazarineblue.executors.ExecutorBuilder;
import org.mazarineblue.executors.ExecutorFactory;
import org.mazarineblue.fs.FileSystem;
import org.mazarineblue.runners.fitnesse.engineplugin.FitnesseSubscriber;
import org.mazarineblue.runners.fitnesse.engineplugin.FixtureLoaderSubscriber;
import org.mazarineblue.runners.threadrunner.ThreadServiceRunnerFactory;
import org.mazarineblue.runners.threadrunner.events.RunnerEvent;
import org.mazarineblue.utililities.BlockingTwoWayPipeFactory;

/**
 * The {@code Plugin} is the MazarineBlue main class for the FitNesse point of
 * view.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class MazarineBlueFitnessePlugin
        extends PluginFeatureFactoryBase {

    public static final String NAME = "mazarineblue";
    public static final int TIMEOUT = 4000; // Timeout in milliseconds
    private final ExecutorBuilder builder = new ExecutorBuilder();

    MazarineBlueFitnessePlugin() {
    }

    MazarineBlueFitnessePlugin(FileSystem fs) {
        builder.setFileSystem(fs);
    }

    @Override
    public void registerTestSystemFactories(TestSystemFactoryRegistry registry) {
        BlockingTwoWayPipeFactory<Event> pipeFactory = new BlockingTwoWayPipeFactory<>();
        ThreadServiceRunnerFactory runnerFactory = new ThreadServiceRunnerFactory(createProcessorFactory(), TIMEOUT);
        MazarineBlueSlimClient slimClient = new MazarineBlueSlimClient(pipeFactory, runnerFactory);
        MazarineBlueTestSystemFactory factory = new MazarineBlueTestSystemFactory(slimClient);
        registry.registerTestSystemFactory(NAME, factory);
    }

    private ExecutorFactory createProcessorFactory() {
        ExecutorFactory factory = ExecutorFactory.newInstance(builder);
        factory.addLinkAfterVariableParser(FitnesseSubscriber::new);
        factory.addLinkAfterVariableParser(FixtureLoaderSubscriber::new);
        factory.addLinkAfterEventNotifier(() -> new UnconsumedExceptionThrowerSubscriber<>(
                matchesNone(RunnerEvent.class).and(matchesNoneAutoConsumable())));
        factory.addLink(() -> new ConsumeEventsSubscriber<>(matchesAny(ExceptionThrownEvent.class)));
        return factory;
    }
}
