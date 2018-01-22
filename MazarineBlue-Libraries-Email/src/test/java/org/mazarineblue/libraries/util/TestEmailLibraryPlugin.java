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

import static java.util.Arrays.asList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.NoSuchProviderException;
import javax.mail.Provider;
import static javax.mail.Provider.Type.STORE;
import javax.mail.Session;
import org.mazarineblue.keyworddriven.Library;
import org.mazarineblue.libraries.email.EmailLibrary;
import org.mazarineblue.plugins.AbstractLibraryPlugin;
import org.mazarineblue.plugins.LibraryPlugin;
import org.openide.util.lookup.ServiceProvider;

/**
 * This class overwrites the functionality of the production version of
 * {@code EmailLibraryPlugin}.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
@ServiceProvider(service = LibraryPlugin.class)
public class TestEmailLibraryPlugin
        extends AbstractLibraryPlugin {

    static final String NAMESPACE = "test.mazarineblue.libraries.email";

    public TestEmailLibraryPlugin() {
        super(NAMESPACE);
    }

    @Override
    protected List<Library> doLibraries() {
        return asList(createLibrary());
    }

    private EmailLibrary createLibrary() {
        try {
            Session session = Session.getDefaultInstance(System.getProperties());
            session.setProvider(new Provider(STORE, "pop3", TestStore.class.getName(), null, null));
            session.setProvider(new Provider(STORE, "imap", TestStore.class.getName(), null, null));
            return new EmailLibrary(session);
        } catch (NoSuchProviderException ex) {
            Logger.getLogger(TestEmailLibraryPlugin.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
