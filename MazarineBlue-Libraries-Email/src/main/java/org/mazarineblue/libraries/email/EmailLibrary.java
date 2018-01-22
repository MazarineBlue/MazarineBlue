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
package org.mazarineblue.libraries.email;

import static java.lang.Integer.parseInt;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import org.mazarineblue.eventdriven.Invoker;
import org.mazarineblue.keyworddriven.Keyword;
import org.mazarineblue.keyworddriven.Library;
import static org.mazarineblue.keyworddriven.Library.matchesAny;
import org.mazarineblue.keyworddriven.PassInvoker;
import org.mazarineblue.keyworddriven.events.AddLibraryEvent;
import org.mazarineblue.keyworddriven.events.ContainsLibrariesEvent;
import org.mazarineblue.keyworddriven.events.RemoveLibraryEvent;
import static org.mazarineblue.libraries.email.EmailLibraryPlugin.NAMESPACE;
import org.mazarineblue.libraries.email.exceptions.EmailBackendException;

public class EmailLibrary
        extends Library {

    private static final int DEFAULT_POP3_PORT = 110;
    private static final int DEFAULT_IMAP_PORT = 143;

    private final Session session;
    private StoreLibrary storeLibrary;

    public EmailLibrary(Session session) {
        super(NAMESPACE);
        this.session = session;
    }

    @Keyword("Connect to pop3 server")
    @PassInvoker
    public void connectPop(Invoker invoker, String username, String password, String host, String port) {
        int p = port != null ? parseInt(port) : DEFAULT_POP3_PORT;
        connect(invoker, "pop3", host, p, username, password);
    }

    @Keyword("Connect to imap server")
    @PassInvoker
    public void connectImap(Invoker invoker, String username, String password, String host, String port) {
        int p = port != null ? parseInt(port) : DEFAULT_IMAP_PORT;
        connect(invoker, "imap", host, p, username, password);
    }

    private void connect(Invoker invoker, String protocol, String host, int port, String username, String password) {
        try {
            if (storeLibrary != null && doesInvokerContainsLibraries(invoker, StoreLibrary.class))
                invoker.publish(new RemoveLibraryEvent(storeLibrary));
            Store store = session.getStore(protocol);
            store.connect(host, port, username, password);
            storeLibrary = new StoreLibrary(store);
            invoker.publish(new AddLibraryEvent(storeLibrary));
        } catch (MessagingException ex) {
            throw new EmailBackendException(ex);
        }
    }

    static boolean doesInvokerContainsLibraries(Invoker invoker, Class<?>... types) {
        ContainsLibrariesEvent e = new ContainsLibrariesEvent(matchesAny(types));
        invoker.publish(e);
        return e.found();
    }
}
