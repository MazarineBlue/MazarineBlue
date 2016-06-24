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
package org.mazarineblue.test.links;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.mazarineblue.eventbus.EventHandler;
import org.mazarineblue.events.log.LogDecrementNestedLevelEvent;
import org.mazarineblue.events.log.LogIncrementNestedLevelEvent;
import org.mazarineblue.keyworddriven.links.Link;
import org.mazarineblue.keyworddriven.logs.dom.Composite;
import org.mazarineblue.keyworddriven.logs.dom.Status;
import org.mazarineblue.test.events.SetTestcaseEvent;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class MiniLogLink
        implements Link {

    private final Map<Key, Composite> compositeMap = new HashMap();
    private final Status[] filters;

    static public class Key {

        private final String platform, suite, testcase;

        public Key(String platform, String suite, String testcase) {
            this.platform = platform;
            this.suite = suite;
            this.testcase = testcase;
        }

        @Override
        public String toString() {
            return "platform=" + platform + ", suite=" + suite + ", testcase=" + testcase;
        }

        public String getPlatform() {
            return platform;
        }

        public String getSuite() {
            return suite;
        }

        public String getTestcase() {
            return testcase;
        }
    }

    public MiniLogLink(Status... filter) {
        this.filters = filter;
    }

    private Key mostRecentKey;

    @Override
    public String toString() {
        return mostRecentKey == null ? "" : mostRecentKey.toString();
    }

    @EventHandler
    public void eventHandler(SetTestcaseEvent event) {
        if (event.getTestcase().equals(""))
            return;
        mostRecentKey = new Key(event.getPlatform(), event.getSuite(),
                                event.getTestcase());
    }

    @EventHandler
    public void eventHandler(LogIncrementNestedLevelEvent event) {
        ++level;
    }

    private int level = 0;

    @EventHandler
    public void eventHandler(LogDecrementNestedLevelEvent event) {
        if (--level > 0)
            return;
        Composite composite = event.getComposite();
        if (wasStatusRequested(composite.getStatus()))
            compositeMap.put(mostRecentKey, composite);
    }

    private boolean wasStatusRequested(Status status) {
        if (filters == null)
            return true;
        for (Status filter : filters)
            if (filter.equals(status))
                return true;
        return false;
    }

    public Collection<Key> getKeys() {
        return compositeMap.keySet();
    }

    public Composite getComposite(String platform, String suite, String testcase) {
        Key key = new Key(platform, suite, testcase);
        return compositeMap.get(key);
    }

    public Composite getComposite(Key key) {
        return compositeMap.get(key);
    }
}
