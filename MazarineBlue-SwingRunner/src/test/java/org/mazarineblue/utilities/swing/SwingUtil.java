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
package org.mazarineblue.utilities.swing;

import java.awt.Component;
import java.awt.Window;
import java.io.File;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;
import javax.swing.AbstractButton;
import javax.swing.JComboBox;
import static javax.swing.SwingUtilities.invokeLater;
import static org.awaitility.Awaitility.await;
import org.hamcrest.core.IsNot;
import org.hamcrest.core.IsNull;
import org.mazarineblue.utilities.swing.exceptions.ChildNotFoundException;

/**
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class SwingUtil {

    @SuppressWarnings("SleepWhileInLoop")
    public static <T> T waitFor(Supplier<T> s, long timeout) {
        return await().atMost(timeout, MILLISECONDS)
                .until(new ReturnNullOnExceptionCallable<T>(s), new IsNot<>(new IsNull<>()));
    }

    public static void waitUntilFalse(Supplier<Boolean> s, int timeout)
            throws TimeoutException {
        waitUntilTrue(() -> !s.get(), timeout);
    }

    public static void waitUntilTrue(Supplier<Boolean> s, int timeout) {
        await().atMost(timeout, MILLISECONDS)
                .until(s::get);
    }

    public static <T extends Window> T fetchWindowTitled(Window parent, String title, Class<T> type) {
        Searcher<T> searcher = new Searcher<>(new WindowFetcher<>(), new TitleMatcher<>(title, type));
        T found = searcher.searchChilderen(parent);
        if (found == null)
            throw new ChildNotFoundException(title);
        return found;
    }

    public static <T extends Component> T fetchWindowIndexed(Window parent, int index, Class<T> type) {
        Searcher<T> searcher = new Searcher<>(new WindowFetcher<>(), new IndexMatcher<>(index, type));
        T found = searcher.searchChilderen(parent);
        if (found == null)
            throw new ChildNotFoundException(index);
        return found;
    }

    public static void clickButton(Component parent, String name) {
        fetchChildNamed(parent, name, AbstractButton.class).doClick();
    }

    public static Object getComboBoxOptionAtIndex(Component parent, String name, int index) {
        return fetchChildNamed(parent, name, JComboBox.class).getItemAt(index);
    }

    public static Object[] getComboBoxOptions(Component parent, String name) {
        JComboBox<?> comboBox = fetchChildNamed(parent, name, JComboBox.class);
        Object[] arr = new File[comboBox.getItemCount()];
        for (int i = 0; i < arr.length; ++i)
            arr[i] = comboBox.getItemAt(i);
        return arr;
    }

    public static Object getComboBoxSelectedItem(Component parent, String name) {
        return fetchChildNamed(parent, name, JComboBox.class).getSelectedItem();
    }

    public static void setComboBoxOption(Component parent, String name, Object option) {
        JComboBox<?> comboBox = fetchChildNamed(parent, name, JComboBox.class);
        invokeLater(() -> comboBox.setSelectedItem(option));
        await().until(() -> comboBox.getSelectedItem() != null && comboBox.getSelectedItem().equals(option));
    }

    public static <T extends Component> T fetchChildNamed(Component parent, String name, Class<T> type) {
        Searcher<T> searcher = new Searcher<>(new DefaultChilderenFetcher<>(), new NameMatcher<>(name, type));
        T found = searcher.searchChilderen(parent);
        if (found == null)
            throw new ChildNotFoundException(name);
        return found;
    }

    public static <T extends Component> T fetchChildIndexed(Component parent, int index, Class<T> type) {
        Searcher<T> searcher = new Searcher<>(new DefaultChilderenFetcher<>(), new IndexMatcher<>(index, type));
        T found = searcher.searchChilderen(parent);
        if (found == null)
            throw new ChildNotFoundException(index);
        return found;
    }

    private static class Searcher<T extends Component> {

        private final Fetcher<T> fetcher;
        private final Matcher<T> matcher;

        private Searcher(Fetcher<T> fetcher, Matcher<T> matcher) {
            this.fetcher = fetcher;
            this.matcher = matcher;
        }

        @SuppressWarnings("unchecked")
        private T searchChilderen(Component parent) {
            for (Component child : fetcher.getChilderen(parent)) {
                T found = searchChild(child);
                if (found != null)
                    return found;
            }
            return null;
        }

        @SuppressWarnings("unchecked")
        private T searchChild(Component child) {
            return child instanceof Window && !child.isDisplayable() ? null
                    : matcher.childMatches(child) ? (T) child
                    : searchChilderen(child);
        }
    }

    @SuppressWarnings("PublicInnerClass")
    public static interface Fetcher<T extends Component> {

        Component[] getChilderen(Component parent);
    }

    @SuppressWarnings("PublicInnerClass")
    public static interface Matcher<T extends Component> {

        public boolean childMatches(Component c);
    }

    private SwingUtil() {
    }
}
