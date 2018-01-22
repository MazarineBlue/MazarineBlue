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

import static java.lang.System.currentTimeMillis;
import java.util.Date;
import static javax.mail.Flags.Flag.ANSWERED;
import static javax.mail.Flags.Flag.DELETED;
import static javax.mail.Flags.Flag.DRAFT;
import static javax.mail.Flags.Flag.FLAGGED;
import static javax.mail.Flags.Flag.RECENT;
import static javax.mail.Flags.Flag.SEEN;
import static javax.mail.Message.RecipientType.BCC;
import static javax.mail.Message.RecipientType.CC;
import static javax.mail.Message.RecipientType.TO;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.mazarineblue.eventdriven.feeds.MemoryFeed;
import org.mazarineblue.executors.util.AbstractExecutorTestHelper;
import org.mazarineblue.keyworddriven.events.ExecuteInstructionLineEvent;
import org.mazarineblue.libraries.util.TestMessage;
import org.mazarineblue.libraries.util.TestStore;
import org.mazarineblue.variablestore.events.GetVariableEvent;

public class FilterLibraryTest
        extends AbstractExecutorTestHelper {

    private static final int MINUTES = 60000;

    private FilterLibrary lib;
    private TestMessage msg;

    @Before
    public void setup() {
        lib = new FilterLibrary();
        msg = new TestMessage();
        TestStore.init();
        TestStore.putFolder("foo", TestStore.createFolderContaining(msg));
    }

    @After
    public void teardown() {
        lib = null;
        TestStore.clear();
    }

    @Test
    public void testResetFilter() {
        GetVariableEvent e1 = new GetVariableEvent("var1");
        GetVariableEvent e2 = new GetVariableEvent("var2");
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "test.mazarineblue.libraries.email"),
                               new ExecuteInstructionLineEvent("Connect to pop3 server"),
                               new ExecuteInstructionLineEvent("Open mail folder", "foo"),
                               new ExecuteInstructionLineEvent("Filter answered messages"),
                               new ExecuteInstructionLineEvent("Set", "var1", "=Count messages"),
                               new ExecuteInstructionLineEvent("Reset filter"),
                               new ExecuteInstructionLineEvent("Set", "var2", "=Count messages"),
                               new ExecuteInstructionLineEvent("Close mail folder"),
                               new ExecuteInstructionLineEvent("Disconnect from pop3 server"),
                               e1,
                               e2));
        assertSuccess();
        assertEquals(0, e1.getValue());
        assertEquals(1, e2.getValue());
    }

    @Test
    public void filterAnswered_AnsweredMessage()
            throws Exception {
        msg.setFlag(ANSWERED, true);
        Object count = filterFolderAndReturnMessageCount("Filter answered messages");
        assertSuccess();
        assertEquals(1, count);
    }

    @Test
    public void filterAnswered_NotAnsweredMessage()
            throws Exception {
        msg.setFlag(ANSWERED, false);
        Object count = filterFolderAndReturnMessageCount("Filter answered messages");
        assertSuccess();
        assertEquals(0, count);
    }

    @Test
    public void filterNotAnswered_AnsweredMessage()
            throws Exception {
        msg.setFlag(ANSWERED, true);
        Object count = filterFolderAndReturnMessageCount("Filter not answered messages");
        assertSuccess();
        assertEquals(0, count);
    }

    @Test
    public void filterNotAnswered_NotAnsweredMessage()
            throws Exception {
        msg.setFlag(ANSWERED, false);
        Object count = filterFolderAndReturnMessageCount("Filter not answered messages");
        assertSuccess();
        assertEquals(1, count);
    }

    @Test
    public void filterDeleted_DeletedMessage()
            throws Exception {
        msg.setFlag(DELETED, true);
        Object count = filterFolderAndReturnMessageCount("Filter deleted messages");
        assertSuccess();
        assertEquals(1, count);
    }

    @Test
    public void filterDeleted_NotDeletedMessage()
            throws Exception {
        msg.setFlag(DELETED, false);
        Object count = filterFolderAndReturnMessageCount("Filter deleted messages");
        assertSuccess();
        assertEquals(0, count);
    }

    @Test
    public void filterNotDeleted_DeletedMessage()
            throws Exception {
        msg.setFlag(DELETED, true);
        Object count = filterFolderAndReturnMessageCount("Filter not deleted messages");
        assertSuccess();
        assertEquals(0, count);
    }

    @Test
    public void filterNotDeleted_NotDeletedMessage()
            throws Exception {
        msg.setFlag(DELETED, false);
        Object count = filterFolderAndReturnMessageCount("Filter not deleted messages");
        assertSuccess();
        assertEquals(1, count);
    }

    @Test
    public void filterDraft_DraftMessage()
            throws Exception {
        msg.setFlag(DRAFT, true);
        Object count = filterFolderAndReturnMessageCount("Filter draft messages");
        assertSuccess();
        assertEquals(1, count);
    }

    @Test
    public void filterDraft_NotDraftMessage()
            throws Exception {
        msg.setFlag(DRAFT, false);
        Object count = filterFolderAndReturnMessageCount("Filter draft messages");
        assertSuccess();
        assertEquals(0, count);
    }

    @Test
    public void filterNotDraft_DraftMessage()
            throws Exception {
        msg.setFlag(DRAFT, true);
        Object count = filterFolderAndReturnMessageCount("Filter non-draft messages");
        assertSuccess();
        assertEquals(0, count);
    }

    @Test
    public void filterNotDraft_NotDraftMessage()
            throws Exception {
        msg.setFlag(DRAFT, false);
        Object count = filterFolderAndReturnMessageCount("Filter non-draft messages");
        assertSuccess();
        assertEquals(1, count);
    }

    @Test
    public void filterFlagged_FlaggedMessage()
            throws Exception {
        msg.setFlag(FLAGGED, true);
        Object count = filterFolderAndReturnMessageCount("Filter flagged messages");
        assertSuccess();
        assertEquals(1, count);
    }

    @Test
    public void filterFlagged_NotFlaggedMessage()
            throws Exception {
        msg.setFlag(FLAGGED, false);
        Object count = filterFolderAndReturnMessageCount("Filter flagged messages");
        assertSuccess();
        assertEquals(0, count);
    }

    @Test
    public void filterNotFlagged_FlaggedMessage()
            throws Exception {
        msg.setFlag(FLAGGED, true);
        Object count = filterFolderAndReturnMessageCount("Filter non-flagged messages");
        assertSuccess();
        assertEquals(0, count);
    }

    @Test
    public void filterNotFlagged_NotFlaggedMessage()
            throws Exception {
        msg.setFlag(FLAGGED, false);
        Object count = filterFolderAndReturnMessageCount("Filter non-flagged messages");
        assertSuccess();
        assertEquals(1, count);
    }

    @Test
    public void filterRecent_RecentMessage()
            throws Exception {
        msg.setFlag(RECENT, true);
        Object count = filterFolderAndReturnMessageCount("Filter recent messages");
        assertSuccess();
        assertEquals(1, count);
    }

    @Test
    public void filterRecent_NotRecentMessage()
            throws Exception {
        msg.setFlag(RECENT, false);
        Object count = filterFolderAndReturnMessageCount("Filter recent messages");
        assertSuccess();
        assertEquals(0, count);
    }

    @Test
    public void filterNotRecent_RecentMessage()
            throws Exception {
        msg.setFlag(RECENT, true);
        Object count = filterFolderAndReturnMessageCount("Filter non-recent messages");
        assertSuccess();
        assertEquals(0, count);
    }

    @Test
    public void filterNotRecent_NotRecentMessage()
            throws Exception {
        msg.setFlag(RECENT, false);
        Object count = filterFolderAndReturnMessageCount("Filter non-recent messages");
        assertSuccess();
        assertEquals(1, count);
    }

    @Test
    public void filterSeen_SeenMessage()
            throws Exception {
        msg.setFlag(SEEN, true);
        Object count = filterFolderAndReturnMessageCount("Filter seen messages");
        assertSuccess();
        assertEquals(1, count);
    }

    @Test
    public void filterSeen_NotSeenMessage()
            throws Exception {
        msg.setFlag(SEEN, false);
        Object count = filterFolderAndReturnMessageCount("Filter seen messages");
        assertSuccess();
        assertEquals(0, count);
    }

    @Test
    public void filterNotSeen_SeenMessage()
            throws Exception {
        msg.setFlag(SEEN, true);
        Object count = filterFolderAndReturnMessageCount("Filter unseen messages");
        assertSuccess();
        assertEquals(0, count);
    }

    @Test
    public void filterNotSeen_NotSeenMessage()
            throws Exception {
        msg.setFlag(SEEN, false);
        Object count = filterFolderAndReturnMessageCount("Filter unseen messages");
        assertSuccess();
        assertEquals(1, count);
    }

    @Test
    public void filterSendDate_BeforeStartDate1()
            throws Exception {
        msg.setSentDate(new Date(currentTimeMillis() - 2 * MINUTES));
        Object count = filterFolderAndReturnMessageCount("Filter messages with send date within range", "-1m", "+1m");
        assertSuccess();
        assertEquals(0, count);
    }

    @Test
    public void filterSendDate_BeforeStartDate2()
            throws Exception {
        msg.setSentDate(new Date(0));
        lib.filterSendDate(new Date(1), new Date(3));
        assertFalse(lib.test(msg));
    }

    @Test
    public void filterSendDate_OnStartDate()
            throws Exception {
        msg.setSentDate(new Date(1));
        lib.filterSendDate(new Date(1), new Date(3));
        assertTrue(lib.test(msg));
    }

    @Test
    public void filterSendDate_WithinRange1()
            throws Exception {
        msg.setSentDate(new Date(currentTimeMillis()));
        Object count = filterFolderAndReturnMessageCount("Filter messages with send date within range", "-1m", "+1m");
        assertSuccess();
        assertEquals(1, count);
    }

    @Test
    public void filterSendDate_WithinRange2()
            throws Exception {
        msg.setSentDate(new Date(2));
        lib.filterSendDate(new Date(1), new Date(3));
        assertTrue(lib.test(msg));
    }

    @Test
    public void filterSendDate_OnEndDate()
            throws Exception {
        msg.setSentDate(new Date(3));
        lib.filterSendDate(new Date(1), new Date(3));
        assertFalse(lib.test(msg));
    }

    @Test
    public void filterSendDate_AfterStartDate1()
            throws Exception {
        msg.setSentDate(new Date(currentTimeMillis() + 2 * MINUTES));
        Object count = filterFolderAndReturnMessageCount("Filter messages with send date within range", "-1m", "+1m");
        assertSuccess();
        assertEquals(0, count);
    }

    @Test
    public void filterReceivedDate_BeforeStartDate1()
            throws Exception {
        msg.setReceivedDate(new Date(currentTimeMillis() - 2 * MINUTES));
        Object count = filterFolderAndReturnMessageCount("Filter messages with received date within range", "-1m", "+1m");
        assertSuccess();
        assertEquals(0, count);
    }

    @Test
    public void filterReceivedDate_BeforeStartDate2()
            throws Exception {
        msg.setReceivedDate(new Date(0));
        lib.filterReceivedDate(new Date(1), new Date(3));
        assertFalse(lib.test(msg));
    }

    @Test
    public void filterReceivedDate_OnStartDate()
            throws Exception {
        msg.setReceivedDate(new Date(1));
        lib.filterReceivedDate(new Date(1), new Date(3));
        assertTrue(lib.test(msg));
    }

    @Test
    public void filterReceivedDate_WithinRange1()
            throws Exception {
        msg.setReceivedDate(new Date(currentTimeMillis()));
        Object count = filterFolderAndReturnMessageCount("Filter messages with received date within range", "-1m", "+1m");
        assertSuccess();
        assertEquals(1, count);
    }

    @Test
    public void filterReceivedDate_WithinRange2()
            throws Exception {
        msg.setReceivedDate(new Date(2));
        lib.filterReceivedDate(new Date(1), new Date(3));
        assertTrue(lib.test(msg));
    }

    @Test
    public void filterReceivedDate_OnEndDate()
            throws Exception {
        msg.setReceivedDate(new Date(3));
        lib.filterReceivedDate(new Date(1), new Date(3));
        assertFalse(lib.test(msg));
    }

    @Test
    public void filterReceivedDate_AfterStartDate()
            throws Exception {
        msg.setReceivedDate(new Date(currentTimeMillis() + 2 * MINUTES));
        Object count = filterFolderAndReturnMessageCount("Filter messages with received date within range", "-1m", "+1m");
        assertSuccess();
        assertEquals(0, count);
    }

    @Test
    public void filterSubject_MatchingSubject_SingleArgument()
            throws Exception {
        msg.setSubject("foo");
        Object count = filterFolderAndReturnMessageCount("Filter messages with subject", "foo");
        assertSuccess();
        assertEquals(1, count);
    }

    @Test
    public void filterSubject_MatchingSubject_DoubleArguments()
            throws Exception {
        msg.setSubject("foo");
        Object count = filterFolderAndReturnMessageCount("Filter messages with subject", "bar", "foo");
        assertSuccess();
        assertEquals(1, count);
    }

    @Test
    public void filterSubject_NonMatchingSubject()
            throws Exception {
        msg.setSubject("foo");
        Object count = filterFolderAndReturnMessageCount("Filter messages with subject", "bar");
        assertSuccess();
        assertEquals(0, count);
    }

    @Test
    public void filterReplyTo_MachtingReplyTo()
            throws Exception {
        msg.setReplyTo("Foo Bar <foo@example.com>");
        Object count = filterFolderAndReturnMessageCount("Filter messages with reply-to address", "foo@example.com");
        assertSuccess();
        assertEquals(1, count);
    }

    @Test
    public void filterReplyTo_NonMachtingReplyTo()
            throws Exception {
        msg.setReplyTo("Foo Bar <foo@example.com>");
        Object count = filterFolderAndReturnMessageCount("Filter messages with reply-to address", "bar@example.com");
        assertSuccess();
        assertEquals(0, count);
    }

    @Test
    public void filterFrom_MachtingFrom()
            throws Exception {
        msg.setFrom("Foo Bar <foo@example.com>");
        Object count = filterFolderAndReturnMessageCount("Filter messages with from address", "bar@example.com", "foo@example.com");
        assertSuccess();
        assertEquals(1, count);
    }

    @Test
    public void filterFrom_NonMachtingFrom()
            throws Exception {
        msg.setFrom("Foo Bar <foo@example.com>");
        Object count = filterFolderAndReturnMessageCount("Filter messages with from address", "bar@example.com");
        assertSuccess();
        assertEquals(0, count);
    }

    @Test
    public void filterRecipicent_MachtingTo()
            throws Exception {
        msg.setRecipient(TO, "Foo Bar <foo@example.com>");
        Object count = filterFolderAndReturnMessageCount("Filter messages with recipient address", "bar@example.com", "foo@example.com");
        assertSuccess();
        assertEquals(1, count);
    }

    @Test
    public void filterRecipicent_NonMachtingTo()
            throws Exception {
        msg.setRecipient(TO, "Foo Bar <foo@example.com>");
        Object count = filterFolderAndReturnMessageCount("Filter messages with recipient address", "bar@example.com");
        assertSuccess();
        assertEquals(0, count);
    }

    @Test
    public void filterRecipicent_MachtingCc()
            throws Exception {
        msg.setRecipient(CC, "Foo Bar <foo@example.com>");
        Object count = filterFolderAndReturnMessageCount("Filter messages with recipient address", "bar@example.com", "foo@example.com");
        assertSuccess();
        assertEquals(1, count);
    }

    @Test
    public void filterRecipicent_NonMachtingCc()
            throws Exception {
        msg.setRecipient(CC, "Foo Bar <foo@example.com>");
        Object count = filterFolderAndReturnMessageCount("Filter messages with recipient address", "bar@example.com");
        assertSuccess();
        assertEquals(0, count);
    }

    @Test
    public void filterRecipicent_MachtingBcc()
            throws Exception {
        msg.setRecipient(BCC, "Foo Bar <foo@example.com>");
        Object count = filterFolderAndReturnMessageCount("Filter messages with recipient address", "bar@example.com", "foo@example.com");
        assertSuccess();
        assertEquals(1, count);
    }

    @Test
    public void filterRecipicent_NonMachtingBcc()
            throws Exception {
        msg.setRecipient(BCC, "Foo Bar <foo@example.com>");
        Object count = filterFolderAndReturnMessageCount("Filter messages with recipient address", "bar@example.com");
        assertSuccess();
        assertEquals(0, count);
    }

    @Test
    public void filterTo_MachtingTo()
            throws Exception {
        msg.setRecipient(TO, "Foo Bar <foo@example.com>");
        Object count = filterFolderAndReturnMessageCount("Filter messages with to address", "bar@example.com", "foo@example.com");
        assertSuccess();
        assertEquals(1, count);
    }

    @Test
    public void filterTo_NonMachtingTo()
            throws Exception {
        msg.setRecipient(TO, "Foo Bar <foo@example.com>");
        Object count = filterFolderAndReturnMessageCount("Filter messages with to address", "bar@example.com");
        assertSuccess();
        assertEquals(0, count);
    }

    @Test
    public void filterCc_MachtingCc()
            throws Exception {
        msg.setRecipient(CC, "Foo Bar <foo@example.com>");
        Object count = filterFolderAndReturnMessageCount("Filter messages with cc address", "bar@example.com", "foo@example.com");
        assertSuccess();
        assertEquals(1, count);
    }

    @Test
    public void filterCc_NonMachtingCc()
            throws Exception {
        msg.setRecipient(CC, "Foo Bar <foo@example.com>");
        Object count = filterFolderAndReturnMessageCount("Filter messages with cc address", "bar@example.com");
        assertSuccess();
        assertEquals(0, count);
    }

    @Test
    public void filterBcc_MachtingBcc()
            throws Exception {
        msg.setRecipient(BCC, "Foo Bar <foo@example.com>");
        Object count = filterFolderAndReturnMessageCount("Filter messages with bcc address", "bar@example.com", "foo@example.com");
        assertSuccess();
        assertEquals(1, count);
    }

    @Test
    public void filterBcc_NonMachtingBcc()
            throws Exception {
        msg.setRecipient(BCC, "Foo Bar <foo@example.com>");
        Object count = filterFolderAndReturnMessageCount("Filter messages with bcc address", "bar@example.com");
        assertSuccess();
        assertEquals(0, count);
    }

    private Object filterFolderAndReturnMessageCount(String keyword, Object... parameters) {
        GetVariableEvent e = new GetVariableEvent("var");
        execute(new MemoryFeed(new ExecuteInstructionLineEvent("Import library", "test.mazarineblue.libraries.email"),
                               new ExecuteInstructionLineEvent("Connect to pop3 server"),
                               new ExecuteInstructionLineEvent("Open mail folder", "foo"),
                               new ExecuteInstructionLineEvent(keyword, parameters),
                               new ExecuteInstructionLineEvent("Set", "var", "=Count messages"),
                               new ExecuteInstructionLineEvent("Close mail folder"),
                               new ExecuteInstructionLineEvent("Disconnect from pop3 server"),
                               e));
        return e.getValue();
    }
}
