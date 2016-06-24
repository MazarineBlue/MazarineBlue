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
package org.mazarineblue.test.report;

import org.mazarineblue.test.report.visitors.TestObjectVisitor;
import org.mazarineblue.test.report.visitors.TestObjectVisitorException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * The object is the basic building block for the report classes.
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 * @param <Child> The child type
 */
public class TestObject<Child extends TestObject> {

    static PlatformFactory factory = PlatformFactory.getDefaultFactory();
    private final WeakReference<TestObject> parent;
    private Collection<Child> childerenCollection;
    private Map<String, Child> childerenMap;
    private Tickets tickets;
    final String name;
    Map<String, Platform> platforms;

    TestObject(String name, TestObject parent, Map<String, Platform> platforms) {
        this.name = name;
        this.parent = new WeakReference<>(parent);
        this.platforms = new TreeMap<>();
        Iterator<Platform> it = platforms.values().iterator();
        while (it.hasNext()) {
            Platform p = it.next();
            this.platforms.put(p.name, p);
        }
    }

    public Collection<Child> getChilderen() {
        return Collections.unmodifiableCollection(childerenCollection);
    }

    public int countChilderen() {
        if (childerenCollection == null)
            return 0;
        return childerenCollection.size();
    }

    protected Child[] childerenToArray(Child[] arr) {
        return childerenCollection.toArray(arr);
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name + ": " + getStatus().name().toLowerCase();
    }

    public Status getStatus() {
        Status global = Status.passed;
        for (String platform : platforms.keySet()) {
            Status s = getPlatformStatus(platform);
            if (s == Status.failed)
                return Status.failed;
            if (s == Status.unset)
                global = Status.unset;
        }
        return global;
    }

    public Status getStatus(String platform) {
        return getPlatformStatus(platform);
    }

    Status getPlatformStatus(String platform) {
        Platform p = platforms.get(platform);
        if (p.status != Status.unset)
            return p.status;
        if (childerenMap == null || childerenMap.isEmpty())
            return p.status;
        return getChildPlatformStatus(platform);
    }

    private Status getChildPlatformStatus(String platform) {
        Status status = Status.passed;
        for (Child child : childerenMap.values()) {
            Status childStatus = child.getPlatformStatus(platform);
            if (childStatus == Status.failed)
                return Status.failed;
            if (childStatus == Status.unset)
                status = Status.unset;
        }
        return status;
    }

    public String[] getPlatforms() {
        Set<String> set = platforms.keySet();
        String[] arr = new String[set.size()];
        return set.toArray(arr);
    }

    public Status[] getPlatformStatusses() {
        Set<String> set = platforms.keySet();
        Status[] arr = new Status[set.size()];
        int i = -1;
        for (String key : set) {
            Platform platform = platforms.get(key);
            arr[++i] = platform == null ? Status.unset : platform.status;
        }
        return arr;
    }

    public void accept(TestObjectVisitor visitor)
            throws TestObjectVisitorException {
        acceptLocal(visitor);
        acceptChilderen(visitor);
    }

    private void acceptLocal(TestObjectVisitor visitor)
            throws TestObjectVisitorException {
        if (tickets != null)
            tickets.output(visitor);
    }

    private void acceptChilderen(TestObjectVisitor visitor)
            throws TestObjectVisitorException {
        if (childerenCollection != null)
            for (Child obj : childerenCollection)
                obj.accept(visitor);
    }

    protected void addTicket(final Ticket ticket) {
        if (tickets == null)
            tickets = new Tickets();
        tickets.add(ticket);
    }

    protected Collection<Ticket> fetchTickets() {
        return fetchTickets(true);
    }

    @SuppressWarnings("unchecked")
    protected Collection<Ticket> fetchTickets(boolean recurisive) {
        if (tickets == null)
            return Collections.EMPTY_LIST;
        Collection<Ticket> collection = tickets.getTickets();
        if (childerenMap != null)
            for (Child child : childerenMap.values())
                collection.addAll(child.fetchTickets(recurisive));
        return collection;
    }

    protected Child getTestObject(String name) {
        createMap();
        return childerenMap.get(name);
    }

    private void createMap() {
        if (childerenMap == null)
            childerenMap = new HashMap<>(4);
    }

    protected void addTestObject(String name, Child obj) {
        createMap();
        createCollection();
        if (childerenMap.containsKey(name))
            childerenCollection.remove(obj);
        childerenCollection.add(obj);
        childerenMap.put(name, obj);
    }

    private void createCollection() {
        if (childerenCollection == null)
            childerenCollection = new ArrayList<>(0);
    }

    void setPassed(String platform) {
        Platform p = platforms.get(platform);
        if (p.status != Status.unset)
            return;
        p = Report.factory.get(platform, Status.passed);
        platforms.put(platform, p);
    }

    void setFailed(String platform) {
        Platform p = Report.factory.get(platform, Status.failed);
        platforms.put(platform, p);
    }

    public boolean containsPlatform(String platform) {
        return platforms.containsKey(platform);
    }

    void checkPlatform(Platform platform) {
        if (platforms.containsKey(platform.name))
            return;
        String list = null;
        for (String str : platforms.keySet())
            list = (list == null ? "" : list + ", ") + str;
        throw new IllegalPlatformException(
                "The platform must be one of: " + list);
    }

    public int count(Class<TestObject> type) {
        if (getClass().isAssignableFrom(type))
            return platforms.size();
        return countChilderen(type);
    }

    private int countChilderen(Class<TestObject> type) {
        int count = 0;
        if (childerenCollection != null)
            for (Child child : childerenCollection)
                count += child.count(type);
        return count;
    }

    public int count(Class<TestObject> type, String platform) {
        if (getClass().isAssignableFrom(type))
            return 1;
        return countChilderen(type, platform);
    }

    private int countChilderen(Class<TestObject> type, String platform) {
        int count = 0;
        if (childerenCollection != null)
            for (Child child : childerenCollection)
                count += child.count(type, platform);
        return count;
    }

    public int count(Class<TestObject> type, Status status) {
        if (getClass().isAssignableFrom(type))
            return countPlatforms(type, status);
        return countChilderen(type, status);
    }

    private int countPlatforms(Class<TestObject> type, Status status) {
        int count = 0;
        for (Platform platform : platforms.values())
            if (platform.status.equals(status))
                ++count;
        return count;
    }

    private int countChilderen(Class<TestObject> type, Status status) {
        int count = 0;
        if (childerenCollection != null)
            for (Child child : childerenCollection)
                count += child.count(type, status);
        return count;
    }

    public int count(Class<TestObject> type, String platform, Status status) {
        if (getClass().isAssignableFrom(type))
            return countPlatformStatus(type, platform, status);
        return countChilderen(type, platform, status);
    }

    private int countPlatformStatus(Class<TestObject> type, String platform,
                                    Status status) {
        boolean flag = platforms.get(platform).status.equals(status);
        return flag ? 1 : 0;
    }

    private int countChilderen(Class<TestObject> type, String platform,
                               Status status) {
        int count = 0;
        if (childerenCollection != null)
            for (Child child : childerenCollection)
                count += child.count(type, platform, status);
        return count;
    }
}
