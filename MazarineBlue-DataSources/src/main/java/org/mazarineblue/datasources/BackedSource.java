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
package org.mazarineblue.datasources;

import java.util.ArrayList;
import static java.util.Arrays.asList;
import java.util.Collection;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.mazarineblue.datasources.exceptions.ReachedEndOfDataSourceException;

/**
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public abstract class BackedSource
        implements DataSource {

    @SuppressWarnings("CollectionWithoutInitialCapacity")
    private final List<DataSource> list = new ArrayList<>();

    protected BackedSource(DataSource... sources) {
        list.addAll(asList(sources));
    }

    public Stream<DataSource> stream() {
        return list.stream();
    }

    public Stream<DataSource> parallelStream() {
        return list.parallelStream();
    }

    protected int size() {
        return list.size();
    }

    protected boolean isEmpty() {
        return list.isEmpty();
    }

    protected void add(DataSource source) {
        list.add(source);
    }

    protected boolean remove(DataSource source) {
        if (list.isEmpty())
            throw new EmptyStackException();
        return list.remove(source);
    }

    protected void addAll(Collection<? extends DataSource> c) {
        list.addAll(c);
    }

    protected void clear() {
        list.clear();
    }

    protected DataSource pop() {
        if (list.isEmpty())
            throw new EmptyStackException();
        int index = list.size() - 1;
        return list.remove(index);
    }

    protected void push(DataSource source) {
        list.add(source);
    }

    @Override
    public boolean hasNext() {
        return list.isEmpty() ? false : stream().allMatch(source -> source.hasNext());
    }

    @Override
    public void next() {
        if (hasNext() == false)
            throw new ReachedEndOfDataSourceException();
        stream().forEach(source -> source.next());
    }

    @Override
    public void reset() {
        stream().forEach(source -> source.reset());
    }

    @Override
    public List<String> getSourceIdentifiers() {
        return mapAndCollect(source -> source.getSourceIdentifiers());
    }

    @Override
    public List<String> getLineIdentifiers() {
        return mapAndCollect(source -> source.getLineIdentifiers());
    }

    @SuppressWarnings("LocalVariableHidesMemberVariable")
    private <T> List<T> mapAndCollect(Function<DataSource, List<T>> mapper) {
        return list.stream()
                .map(mapper)
                .flatMap((list) -> list.stream())
                .collect(Collectors.toList());
    }

    @Override
    public Set<String> getColumns() {
        return list.stream()
                .map(source -> source.getColumns())
                .flatMap(set -> set.stream())
                .collect(Collectors.toSet());
    }

    @Override
    public Object getData(String column) {
        return findFirstDataSource(source -> source.getData(column) != null).getData(column);
    }

    @Override
    public Object getData(int index) {
        return findFirstDataSource(source -> source.getData(index) != null).getData(index);
    }

    @Override
    public boolean setData(String column, Object value) {
        return findFirstDataSource(source -> true).setData(column, value);
    }

    @Override
    public boolean setData(int index, Object value) {
        return findFirstDataSource(source -> true).setData(index, value);
    }

    private DataSource findFirstDataSource(Predicate<DataSource> predicate) {
        return list.stream()
                .filter(predicate)
                .findFirst()
                .orElse(new NullDataSource(false));
    }

    @Override
    public int hashCode() {
        return 155 + Objects.hashCode(this.list);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (BackedSource.class.isAssignableFrom(obj.getClass()) == false)
            return false;
        final BackedSource other = (BackedSource) obj;
        return Objects.equals(this.list, other.list);
    }
}
