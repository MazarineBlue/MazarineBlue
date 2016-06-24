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
package org.mazarineblue.runner.sheetFactorySelector;

import java.io.IOException;
import org.mazarineblue.keyworddriven.documentMediators.DocumentMediator;
import org.mazarineblue.keyworddriven.feeds.FeedBuilder;
import org.mazarineblue.keyworddriven.sheetfactories.SheetFactory;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class ExcelSheetFactoryBuilder
        implements SheetFactoryBuilder {

    @Override
    public SheetFactory buildFactory(FeedBuilder feedBuilder, DocumentMediator mediator)
            throws IOException {
        return new ExcelSheetFactory(feedBuilder, mediator);
    }
}
