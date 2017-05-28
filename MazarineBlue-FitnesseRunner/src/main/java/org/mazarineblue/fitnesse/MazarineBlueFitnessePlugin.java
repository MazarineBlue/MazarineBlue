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
package org.mazarineblue.fitnesse;

import fitnesse.plugins.PluginFeatureFactoryBase;
import fitnesse.testrunner.TestSystemFactoryRegistry;
import org.mazarineblue.eventbus.Event;
import org.mazarineblue.eventdriven.events.ExceptionThrownEvent;
import org.mazarineblue.executors.FeedExecutorBuilder;
import org.mazarineblue.executors.FeedExecutorFactory;
import org.mazarineblue.executors.listeners.SubscriberFactory;
import org.mazarineblue.fitnesse.engineplugin.FitnesseSubscriber;
import org.mazarineblue.fitnesse.engineplugin.FixtureLoaderLink;
import org.mazarineblue.fitnesse.events.FitnesseEvent;
import org.mazarineblue.links.ConsumeEventsLink;
import org.mazarineblue.links.UnconsumedExceptionThrowerLink;
import org.mazarineblue.threadrunner.ThreadServiceRunnerFactory;
import org.mazarineblue.threadrunner.events.RunnerEvent;
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

    @Override
    public void registerTestSystemFactories(TestSystemFactoryRegistry registry) {
        BlockingTwoWayPipeFactory<Event> pipeFactory = new BlockingTwoWayPipeFactory<>();
        ThreadServiceRunnerFactory runnerFactory = new ThreadServiceRunnerFactory(createInterpreterFactory(), TIMEOUT);
        MazarineBlueSlimClient slimClient = new MazarineBlueSlimClient(pipeFactory, runnerFactory);
        MazarineBlueTestSystemFactory factory = new MazarineBlueTestSystemFactory(slimClient);
        registry.registerTestSystemFactory(NAME, factory);
    }

    private FeedExecutorFactory createInterpreterFactory() {
        FeedExecutorFactory factory = FeedExecutorFactory.getDefaultInstance(new FeedExecutorBuilder());
        factory.addSubscriber(SubscriberFactory.getDefaultInstance(FitnesseEvent.class, null, new FitnesseSubscriber()));
        factory.addLinkAfterEventBus(() -> new UnconsumedExceptionThrowerLink(RunnerEvent.class));
        factory.addLink(FixtureLoaderLink::new);
        factory.addLink(() -> new ConsumeEventsLink(ExceptionThrownEvent.class));
        return factory;
    }
}
