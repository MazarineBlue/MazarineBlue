/*
 * Copyright (c) 2018 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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
package org.mazarineblue.libraries.util;

import static java.lang.System.getProperties;
import java.util.HashMap;
import java.util.Map;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import static javax.mail.Session.getDefaultInstance;
import javax.mail.Store;
import javax.mail.URLName;

public class TestStore
        extends Store {

    static private TestStore globalStore;
    static private Map<String, Folder> folders;

    public static void init() {
        globalStore = new TestStore();
        folders = new HashMap<>();
        folders.put("inbox", newFolder(20));
    }

    public static void clear() {
        globalStore = null;
        folders = null;
    }

    public static Folder newFolder(int n) {
        return new TestFolder(globalStore).init(n);
    }

    public static Folder createFolderContaining(Message msg) {
        return new TestFolder(globalStore).init(msg);
    }

    public static Folder putFolder(String name, Folder folder) {
        return folders.put(name, folder);
    }

    public TestStore() {
        this(getDefaultInstance(getProperties()), new URLName("test", null, -1, null, null, null));
    }

    public TestStore(Session session, URLName urlname) {
        super(session, urlname);
    }

    @Override
    public Folder getDefaultFolder()
            throws MessagingException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Folder getFolder(String name)
            throws MessagingException {
        if (folders.containsKey(name))
            return folders.get(name);
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Folder getFolder(URLName url)
            throws MessagingException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected boolean protocolConnect(String host, int port, String user, String password)
            throws MessagingException {
        return true;
    }
}
