/*
 * Copyright (c) 2012-2014 Alex de Kruijff
 * Copyright (c) 2014-2015 Specialisterren
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

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import org.mazarineblue.keyworddriven.documentMediators.DocumentMediator;
import org.mazarineblue.keyworddriven.sheetfactories.NullSheetFactory;
import org.mazarineblue.keyworddriven.sheetfactories.SheetFactory;
import org.mazarineblue.keyworddriven.documentMediators.FileDocumentMediator;
import org.mazarineblue.keyworddriven.feeds.FeedBuilder;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class DefaultSheetFactorySelector
        implements SheetFactorySelector {

    private final FeedBuilder feedBuilder;
    private final Map<Pattern, SheetFactoryBuilder> sheetFactories = new HashMap();

    public DefaultSheetFactorySelector(FeedBuilder feedBuilder) {
        this.feedBuilder = feedBuilder;
        SheetFactoryBuilder factory = new ExcelSheetFactoryBuilder();
        sheetFactories.put(Pattern.compile("\\.xlsx$"), factory);
        sheetFactories.put(Pattern.compile("\\.xls$"), factory);
    }

    @Override
    public SheetFactory getSheetFactory(File file) {
        if (file == null)
            return new NullSheetFactory(feedBuilder);

        String path = file.getAbsolutePath();
        for (Pattern pattern : sheetFactories.keySet())
            if (pattern.matcher(path).find())
                try {
                    SheetFactoryBuilder builder = sheetFactories.get(pattern);
                    DocumentMediator mediator = new FileDocumentMediator(file);
                    return builder.buildFactory(feedBuilder, mediator);
                } catch (IOException ex) {
                }
        return null;
    }
}
