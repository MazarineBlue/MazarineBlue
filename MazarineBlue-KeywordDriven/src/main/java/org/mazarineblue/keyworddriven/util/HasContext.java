/*
 * Copyright (c) 2015 Specialisterren
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
package org.mazarineblue.keyworddriven.util;

import org.mazarineblue.datasources.BlackboardSource;
import org.mazarineblue.keyworddriven.InterpreterContext;
import org.mazarineblue.keyworddriven.RunningInterpreter;
import org.mazarineblue.keyworddriven.documentMediators.DocumentMediator;
import org.mazarineblue.keyworddriven.feeds.Feed;
import org.mazarineblue.keyworddriven.logs.Log;
import org.mazarineblue.keyworddriven.sheetfactories.SheetFactory;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public interface HasContext {

    public InterpreterContext context();

    public RunningInterpreter executor();

    public Feed feed();

    public Log log();

    public BlackboardSource blackboard();

    public SheetFactory sheetFactory();

    public DocumentMediator documentMediator();
}
