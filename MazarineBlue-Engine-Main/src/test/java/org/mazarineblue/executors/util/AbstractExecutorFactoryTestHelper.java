/*
 * Copyright (c) 2017 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
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
package org.mazarineblue.executors.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;
import org.junit.After;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.mazarineblue.eventdriven.events.ExceptionThrownEvent;
import org.mazarineblue.eventdriven.listeners.ChainModifierListener;
import org.mazarineblue.eventdriven.listeners.FeedExecutorListener;
import org.mazarineblue.eventdriven.listeners.PublisherListener;
import org.mazarineblue.eventnotifier.Event;
import static org.mazarineblue.eventnotifier.Event.matchesNone;
import org.mazarineblue.eventnotifier.Subscriber;
import org.mazarineblue.eventnotifier.subscribers.UnconsumedExceptionThrowerSubscriber;
import org.mazarineblue.executors.Executor;
import org.mazarineblue.executors.ExecutorFactory;
import org.mazarineblue.fs.FileSystem;
import org.mazarineblue.fs.MemoryFileSystem;

/**
 * A {@code AbstractExecutorTestHelper} is a test utility for executing feeds.
 *
 * This utility reads and write to a in memory FileSystem, collects output
 * written to stdin and throws exceptions for any events that are not consumed,
 * with the exception of {@link ExceptionThrownEvent}, which is thrown when a
 * event is not consumed.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public abstract class AbstractExecutorFactoryTestHelper
        implements FileSystem, ExecutorFactory {

    private FileSystem fs;
    private FeedExecutorOutputSpy output;
    private ExecutorFactory factory;
    private ConsoleLogger logger;

    @Before
    public void setup_ExecutorTestHelper() {
        fs = new MemoryFileSystem();
        output = new FeedExecutorOutputSpy();
        factory = TestFeedExecutorFactory.newInstance(fs, output);
        factory.addLinkAfterEventNotifier(() -> new UnconsumedExceptionThrowerSubscriber<>(matchesNone(ExceptionThrownEvent.class)));
        logger = new ConsoleLogger();
    }

    @After
    public void teardown_ExecutorTestHelper() {
        fs = null;
        output = null;
        factory = null;
        logger.restore();
        logger = null;
    }

    protected FeedExecutorOutputSpy getOutput() {
        return output;
    }

    protected ExecutorFactory getFactory() {
        return factory;
    }

    //<editor-fold defaultstate="collapsed" desc="FileSystem instructions">
    @Override
    public void mkdir(File dir)
            throws IOException {
        fs.mkdir(dir);
    }

    @Override
    public void mkfile(File file, String content)
            throws IOException {
        fs.mkfile(file, content);
    }

    @Override
    public void mkfile(File file, InputStream input)
            throws IOException {
        fs.mkfile(file, input);
    }

    @Override
    public void mkfile(File file, Object... arr)
            throws IOException {
        fs.mkfile(file, arr);
    }

    @Override
    public void mkfile(File file, Collection<?> col)
            throws IOException {
        fs.mkfile(file, col);
    }

    @Override
    public void delete(File file)
            throws IOException {
        fs.delete(file);
    }

    @Override
    public void deleteAll()
            throws IOException {
        fs.deleteAll();
    }

    @Override
    public InputStream getInputStream(File file)
            throws IOException {
        return fs.getInputStream(file);
    }

    @Override
    public String getContent(File file)
            throws IOException {
        return fs.getContent(file);
    }

    @Override
    public Object[] getArray(File file)
            throws IOException {
        return fs.getArray(file);
    }

    @Override
    public <T> T[] getArray(File file, Class<T[]> type)
            throws IOException {
        return fs.getArray(file, type);
    }

    @Override
    public <T> List<T> getList(File file)
            throws IOException {
        return fs.getList(file);
    }

    @Override
    public File[] listChilderen(File dir) {
        return fs.listChilderen(dir);
    }

    @Override
    public boolean exists(File file) {
        return fs.exists(file);
    }

    @Override
    public boolean isDirectory(File file) {
        return fs.isDirectory(file);
    }

    @Override
    public boolean isFile(File file) {
        return fs.isFile(file);
    }

    @Override
    public boolean isHidden(File file) {
        return fs.isHidden(file);
    }

    @Override
    public boolean isReadable(File file) {
        return fs.isReadable(file);
    }

    @Override
    public File getParent(File file) {
        return fs.getParent(file);
    }
    //</editor-fold>

    @Override
    public Executor create() {
        return factory.create();
    }

    //<editor-fold defaultstate="collapsed" desc="ExecutorFactory instructions">
    @Override
    public void addLinkAfterVariableParser(Supplier<Subscriber<Event>> supplier) {
        factory.addLinkAfterVariableParser(supplier);
    }

    @Override
    public void addLinkAfterEventNotifier(Supplier<Subscriber<Event>> supplier) {
        factory.addLinkAfterEventNotifier(supplier);
    }

    @Override
    public void addLink(Supplier<Subscriber<Event>> supplier) {
        factory.addLink(supplier);
    }

    @Override
    public void setChainModifierListener(Supplier<ChainModifierListener> listener) {
        factory.setChainModifierListener(listener);
    }

    @Override
    public void setPublisherListener(Supplier<PublisherListener> supplier) {
        factory.setPublisherListener(supplier);
    }

    @Override
    public void setFeedExecutorListener(Supplier<FeedExecutorListener> supplier) {
        factory.setFeedExecutorListener(supplier);
    }
    //</editor-fold>

    public void throwFirstException() {
        output.throwFirstException();
    }

    public void assertOutput(String expected) {
        assertEquals(expected, logger.toString());
    }

    public void assertHelpPrinted(boolean flag) {
        assertEquals(flag, output.helpPrinted());
    }

    public void assertProcessingFiles(String... files) {
        assertArrayEquals(files, output.getProcessingFiles());
    }

    public void assertMissingFiles(String... files) {
        assertArrayEquals(files, output.getMissingFiles());
    }

    public void assertUnreadableFiles(String... files) {
        assertArrayEquals(files, output.getUnreadableFiles());
    }

    public void assertFilesNotSupported(String... files) {
        assertArrayEquals(files, output.getFilesNotSupported());
    }
}
