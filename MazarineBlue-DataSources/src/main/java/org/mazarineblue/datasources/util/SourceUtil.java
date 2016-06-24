/*
 * Copyright (c) 2015 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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
package org.mazarineblue.datasources.util;

import static java.util.Arrays.asList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.mazarineblue.datasources.ArraySource;
import org.mazarineblue.datasources.DataSource;

/**
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class SourceUtil {

    @SafeVarargs
    @SuppressWarnings("varargs")
    public static <T> Set<T> asSet(T... arr) {
        Set<T> set = new TreeSet<>();
        set.addAll(asList(arr));
        return set;
    }

    public static void copyAndTransform(DataSource src, ArraySource dst) {
        List<String> mark = src.getLineIdentifiers();
        src.reset();
        new SourceWalker(src, dst).copy((data, firstData) -> {
            return firstData + '.' + data;
        });

        src.reset();
        new SourceWalker(src, dst).copy((data, firstData) -> {
            return data + '.' + firstData;
        });

        src.reset(mark);
    }

    private static class SourceWalker {

        private final DataSource src, dst;

        private SourceWalker(DataSource src, DataSource dst) {
            this.src = src;
            this.dst = dst;
        }

        protected void copy(Key key) {
            Map<String, String> map = mapColumn(key);
            while (src.hasNext()) {
                src.next();
                copyLine(map);
            }
        }
        
        @SuppressWarnings("CollectionWithoutInitialCapacity")
        private Map<String, String> mapColumn(Key key) {
            Map<String, String> map = new HashMap<>();
            String firstColumn = src.getData(0, String.class);
            src.getColumns().stream().forEach((column) -> {
                map.put(column, key.convert(column, firstColumn));
            });
            return map;
        }

        private void copyLine(Map<String, String> map) {
            String firstColumn = src.getData(0, String.class);
            src.getColumns().stream().forEach((column) -> {
                copyColumn(column, map.get(column));
            });
        }

        private void copyColumn(String from, String to) {
            Object obj = src.getData(from);
            dst.setData(to, obj);
        }
    }

    @FunctionalInterface
    public interface Key {

        String convert(String data, String firstData);
    }

    private SourceUtil() {
    }
}
