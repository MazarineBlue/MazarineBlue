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
package org.mazarineblue.plugins.util;

import java.io.InputStream;
import java.util.ArrayList;
import static java.util.Arrays.stream;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import org.mazarineblue.eventdriven.Feed;
import org.mazarineblue.eventdriven.util.feeds.DummyFeed;
import org.mazarineblue.plugins.FeedPlugin;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class MemoryFeedPlugin
        implements FeedPlugin {

    private final Predicate<String> canProcess;
    private final Feed dummy = new DummyFeed();
    private final List<Payload> list = new ArrayList<>();

    private class Payload {

        private final String sheet;
        private final Supplier<Feed> feedSupplier;

        private Payload(String sheet, Supplier<Feed> supplier) {
            if (!returnsObjectOrThrowsException(supplier))
                throw new IllegalArgumentException("Supplier not allowed to supply null");
            this.sheet = sheet;
            this.feedSupplier = supplier;
        }

        private boolean returnsObjectOrThrowsException(Supplier<Feed> supplier) {
            try {
                return supplier.get() != null;
            } catch (RuntimeException ex) {
                return true;
            }
        }

        @Override
        public String toString() {
            return "sheet=" + sheet + ", feedSupplier=" + feedSupplier.getClass().getSimpleName();
        }
    }

    public MemoryFeedPlugin() {
        this(t -> true);
    }

    public MemoryFeedPlugin(Predicate<String> canProcess) {
        this.canProcess = canProcess;
    }

    public MemoryFeedPlugin addSheet(String sheet, Feed feed) {
        list.add(new Payload(sheet, () -> feed));
        return this;
    }

    public MemoryFeedPlugin addSheet(String... sheets) {
        stream(sheets).map(s -> new Payload(s, () -> dummy)).forEach(p -> list.add(p));
        return this;
    }

    public MemoryFeedPlugin addSheet(String sheet, Supplier<Feed> feed) {
        list.add(new Payload(sheet, feed));
        return this;
    }

    @Override
    public boolean canProcess(String mimeType) {
        return this.canProcess.test(mimeType);
    }

    @Override
    public String[] readSheetNames(InputStream input) {
        return list.stream()
                .map(payload -> payload.sheet)
                .toArray(size -> new String[size]);
    }

    @Override
    public Feed createFeed(InputStream input, String sheet) {
        return list.stream()
                .filter(payload -> payload.sheet.equals(sheet))
                .map(payload -> payload.feedSupplier)
                .findAny().orElse(() -> new DummyFeed())
                .get();
    }
}
