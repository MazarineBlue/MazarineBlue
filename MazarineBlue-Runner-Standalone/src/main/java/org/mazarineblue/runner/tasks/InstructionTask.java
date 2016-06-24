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
package org.mazarineblue.runner.tasks;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.transform.TransformerException;
import org.mazarineblue.keyworddriven.DefaultProcessor;
import org.mazarineblue.keyworddriven.RunningInterpreter;
import org.mazarineblue.keyworddriven.documentMediators.DocumentMediator;
import org.mazarineblue.keyworddriven.exceptions.InterpreterAlReadyRunningException;
import org.mazarineblue.keyworddriven.exceptions.InterpreterSetupException;
import org.mazarineblue.keyworddriven.feeds.Feed;
import org.mazarineblue.keyworddriven.feeds.FeedBuilder;
import org.mazarineblue.keyworddriven.links.Chain;
import org.mazarineblue.keyworddriven.logs.DomLog;
import org.mazarineblue.keyworddriven.logs.Log;
import org.mazarineblue.keyworddriven.logs.LogBuilder;
import org.mazarineblue.keyworddriven.logs.LogBuilderImpl;
import org.mazarineblue.keyworddriven.logs.visitors.LogVisitor;
import org.mazarineblue.keyworddriven.logs.visitors.XmlLogVisitor;
import org.mazarineblue.keyworddriven.sheetfactories.SheetFactory;
import org.mazarineblue.mazarineblue.keyworddriven.feeds.FeedBuilderImpl;
import org.mazarineblue.runner.swing.DocumentManager;
import org.mazarineblue.util.XmlUtil;

/**
 *
 * @author Alex de Kruijff {@literal <alex.de.kruijff@MazarineBlue.org>}
 */
public class InstructionTask
        implements Task {

    protected boolean setOpenLog, setEmailLog;
    private long delay = 0;
    protected RunningInterpreter executor;

    private Chain chain;
    protected final DocumentManager documentManager;
    private final SheetFactory sheetFactory;
    private final Feed feed;
    private final Log log;
    private DomLog domLog;
    private State state;
    private Date scheduledDate;

    public InstructionTask(DocumentManager documentManager,
                           SheetFactory sheetFactory, Feed feed) {
        this(documentManager, sheetFactory, feed, null);
        state = State.WAITING;
    }

    public InstructionTask(DocumentManager documentManager,
                           SheetFactory sheetFactory, Feed feed, Log log) {
        this.documentManager = documentManager;
        this.sheetFactory = sheetFactory;
        this.feed = feed;
        this.log = log;
    }

    @Override
    public String toString() {
        return "state=" + state
                + ", scheduledDate=" + scheduledDate
                + ", delay=" + delay;
    }

    @Override
    public void run()
            throws InterruptedException, TaskTargetException {
        try {
            executeInstructions();
        } finally {
            finalActions();
        }
    }

    @Override
    public final void waitForScheduleDate()
            throws InterruptedException {
        Date current, scheduled = getScheduledDate();
        while (scheduled.after(current = new Date())) {
            long millis = scheduled.getTime() - current.getTime();
            if (millis > 30000)
                millis -= 30000;
            else if (millis > 1000)
                millis -= 1000;
            Thread.sleep(millis);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Methods first used in run()">
    private void executeInstructions()
            throws InterruptedException, TaskTargetException {
        try {
            waitForScheduleDate();
            state = State.RUNNING;
            FeedBuilder feedBuilder = new FeedBuilderImpl();
            LogBuilder logBuilder = new LogBuilderImpl();
            DefaultProcessor processor = new DefaultProcessor(feedBuilder, logBuilder);
            executor = processor.init();
            domLog = new DomLog(executor.chain(), new Date());
            Log chain = this.log == null ?  domLog : logBuilder.createLog(domLog, this.log);
            Map<String, Object> variables = new HashMap();
            variables.put("org.mazarineblue.delay", delay);
            executor.execute(feed, chain, documentManager, sheetFactory, variables);
            state = State.DONE;
        } catch (InterpreterSetupException | InterpreterAlReadyRunningException ex) {
            state = State.ERROR;
            throw new TaskTargetException(ex);
        }
    }

    private void finalActions()
            throws InterruptedException, TaskTargetException {
        try {
            saveLog(documentManager);
            if (setOpenLog)
                documentManager.openLogOutput();
            if (setEmailLog)
                documentManager.emailLogOutput();
        } catch (IOException ex) {
            throw new TaskTargetException(ex);
        }
    }

    private void saveLog(DocumentMediator mediator)
            throws IOException {
        String folder = mediator.getDefaultFolder(executor.getStartDate());
        String input = getXmlInput();
        mediator.writeLogOutput(folder, "log", input);
    }

    private String getXmlInput()
            throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        buffer.write(XmlUtil.getVersion().getBytes());
        buffer.write(XmlUtil.getStylesheet("log.xsl").getBytes());
        LogVisitor visitor = new XmlLogVisitor(buffer);
        domLog.visitor(visitor);
        String output = buffer.toString();
        buffer.close();
        try {
            return XmlUtil.convertToPrettyFormat(output);
        } catch (TransformerException ex) {
            Logger.getLogger(InstructionTask.class.getName()).log(Level.SEVERE,
                                                                  null, ex);
        }
        return output;
    }
    // </editor-fold>

    @Override
    public long getDelay() {
        return delay;
    }

    @Override
    public void setDelay(long delay) {
        if (delay < 0)
            throw new IllegalArgumentException(
                    "Argument delay must be 0 or larger");
        this.delay = delay;
    }

    @Override
    public void openLog() {
        documentManager.openLogOutput();
    }

    @Override
    public void openReport() {
        documentManager.openReportOutput();
    }

    @Override
    public boolean isPaused() {
        return executor.getState() == RunningInterpreter.State.PAUSED;
    }

    @Override
    public void pause() {
        executor.pause();
    }

    @Override
    public void resume() {
        executor.resume();
    }

    @Override
    public void cancle() {
        executor.cancle();
    }

    @Override
    public State getState() {
        return state;
    }

    public void setScheduledDate(Date scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    @Override
    public Date getScheduledDate() {
        return scheduledDate == null ? new Date() : scheduledDate;
    }

    @Override
    public String getSourceLocation() {
        return sheetFactory.getLocation();
    }

    @Override
    public String getSheetName() {
        return feed.getIdentifier();
    }

    public void setOpenLog() {
        setOpenLog = true;
    }

    public void setEmailLog() {
        setEmailLog = true;
    }

    @Override
    public boolean after(Task other) {
        if (other == null)
            return false;
        Date otherDate = other.getScheduledDate();
        return scheduledDate.after(otherDate);
    }

    @Override
    public boolean before(Task other) {
        if (other == null)
            return false;
        Date otherDate = other.getScheduledDate();
        return scheduledDate.before(otherDate);
    }

    @Override
    public int compareTo(Task other) {
        if (other == null)
            return 0;
        Date otherDate = other.getScheduledDate();
        if (scheduledDate.equals(otherDate))
            return 0;
        return scheduledDate.before(otherDate) ? -1 : 1;
    }
}
