/*
 * Copyright (c) Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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
package org.mazarineblue.libraries.web.util;

import java.io.IOException;
import java.net.InetSocketAddress;
import org.simpleframework.http.core.Container;
import org.simpleframework.http.core.ContainerSocketProcessor;
import org.simpleframework.transport.connect.SocketConnection;

public class HttpServer
        implements AutoCloseable {

    private SocketConnection connection;
    private final InetSocketAddress socket;
    private Container container;
    private int threads = 1;

    public HttpServer() {
        this(Ports.freePort());
    }

    public HttpServer(int port) {
        this(new InetSocketAddress("localhost", port));
    }

    public HttpServer(InetSocketAddress address) {
        this.socket = address;
    }

    public String getBase() {
        return getHostnameHelper() + ":" + socket.getPort();
    }

    private String getHostnameHelper() {
        String hostname = socket.getHostName();
        return hostname == null ? "" : hostname;
    }

    public HttpServer setContainer(Container container) {
        this.container = container;
        return this;
    }

    public HttpServer setThreads(int threads) {
        this.threads = threads;
        return this;
    }

    public HttpServer start()
            throws IOException {
        connection = new SocketConnection(new ContainerSocketProcessor(container, threads));
        connection.connect(socket);
        return this;
    }

    @Override
    public void close()
            throws IOException {
        connection.close();
    }
}
