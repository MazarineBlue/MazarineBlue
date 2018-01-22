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

import static java.util.Arrays.stream;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import static java.util.stream.Collectors.toList;
import javax.mail.Address;
import static javax.mail.Flags.Flag.ANSWERED;
import static javax.mail.Flags.Flag.DELETED;
import static javax.mail.Flags.Flag.DRAFT;
import static javax.mail.Flags.Flag.FLAGGED;
import static javax.mail.Flags.Flag.RECENT;
import static javax.mail.Flags.Flag.SEEN;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import static javax.mail.Message.RecipientType.BCC;
import static javax.mail.Message.RecipientType.CC;
import static javax.mail.Message.RecipientType.TO;
import javax.mail.MessagingException;
import org.mazarineblue.keyworddriven.Keyword;
import org.mazarineblue.keyworddriven.Library;
import org.mazarineblue.keyworddriven.Parameters;
import static org.mazarineblue.libraries.email.EmailLibraryPlugin.NAMESPACE;
import org.mazarineblue.libraries.email.exceptions.EmailBackendException;

public class FilterLibrary
        extends Library
        implements Predicate<Message> {

    private Predicate<Message> filter = m -> true;

    public FilterLibrary() {
        super(NAMESPACE);
    }

    @Override
    public boolean test(Message msg) {
        return filter.test(msg);
    }

    @Keyword("Reset filter")
    public void resetFilter() {
        filter = m -> true;
    }

    @Keyword("Filter answered messages")
    public void filterAnswered() {
        filter = filter.and(this::isAnswered);
    }

    @Keyword("Filter not answered messages")
    public void filterNotAnswered() {
        filter = filter.and(m -> !isAnswered(m));
    }

    private boolean isAnswered(Message message) {
        try {
            return message.isSet(ANSWERED);
        } catch (MessagingException ex) {
            throw new EmailBackendException(ex);
        }
    }

    @Keyword("Filter deleted messages")
    public void filterDeleted() {
        filter = filter.and(this::isDeleted);
    }

    @Keyword("Filter not deleted messages")
    public void filterNotDeleted() {
        filter = filter.and(m -> !isDeleted(m));
    }

    private boolean isDeleted(Message message) {
        try {
            return message.isSet(DELETED);
        } catch (MessagingException ex) {
            throw new EmailBackendException(ex);
        }
    }

    @Keyword("Filter draft messages")
    public void filterDraft() {
        filter = filter.and(this::isDraft);
    }

    @Keyword("Filter non-draft messages")
    public void filterNotDraft() {
        filter = filter.and(m -> !isDraft(m));
    }

    private boolean isDraft(Message message) {
        try {
            return message.isSet(DRAFT);
        } catch (MessagingException ex) {
            throw new EmailBackendException(ex);
        }
    }

    @Keyword("Filter flagged messages")
    public void filterFlagged() {
        filter = filter.and(this::isFlagged);
    }

    @Keyword("Filter non-flagged messages")
    public void filterNotFlagged() {
        filter = filter.and(m -> !isFlagged(m));
    }

    private boolean isFlagged(Message message) {
        try {
            return message.isSet(FLAGGED);
        } catch (MessagingException ex) {
            throw new EmailBackendException(ex);
        }
    }

    @Keyword("Filter recent messages")
    public void filterRecent() {
        filter = filter.and(this::isRecent);
    }

    @Keyword("Filter non-recent messages")
    public void filterNotRecent() {
        filter = filter.and(m -> !isRecent(m));
    }

    private boolean isRecent(Message message) {
        try {
            return message.isSet(RECENT);
        } catch (MessagingException ex) {
            throw new EmailBackendException(ex);
        }
    }

    @Keyword("Filter seen messages")
    public void filterSeen() {
        filter = filter.and(this::isSeen);
    }

    @Keyword("Filter unseen messages")
    public void filterNotSeen() {
        filter = filter.and(m -> !isSeen(m));
    }

    private boolean isSeen(Message message) {
        try {
            return message.isSet(SEEN);
        } catch (MessagingException ex) {
            throw new EmailBackendException(ex);
        }
    }

    @Keyword("Filter messages with send date within range")
    @Parameters(min = 2)
    public void filterSendDate(Date start, Date end) {
        filter = filter.and(m -> withinRange(start, getSendDate(m), end));
    }

    private static Date getSendDate(Message message) {
        try {
            return message.getSentDate();
        } catch (MessagingException ex) {
            throw new EmailBackendException(ex);
        }
    }

    @Keyword("Filter messages with received date within range")
    @Parameters(min = 2)
    public void filterReceivedDate(Date start, Date end) {
        filter = filter.and(m -> withinRange(start, getReceivedDate(m), end));
    }

    private static Date getReceivedDate(Message message) {
        try {
            return message.getReceivedDate();
        } catch (MessagingException ex) {
            throw new EmailBackendException(ex);
        }
    }

    private static boolean withinRange(Date start, Date date, Date end) {
        return !date.before(start) && date.before(end); // [start, end)
    }

    @Keyword("Filter messages with reply-to address")
    @Parameters(min = 1)
    public void filterReplyTo(String... patterns) {
        filter = filter.and(anyMatches(patterns, this::anyReplyToMatchesPattern));
    }

    private Predicate<Message> anyReplyToMatchesPattern(String pattern) {
        return m -> doesAddresMatchPattern(getReplyTo(m), pattern);
    }

    private List<String> getReplyTo(Message message) {
        try {
            return stream(message.getReplyTo())
                    .map(Address::toString)
                    .collect(toList());
        } catch (MessagingException ex) {
            throw new EmailBackendException(ex);
        }
    }

    @Keyword("Filter messages with from address")
    @Parameters(min = 1)
    public void filterFrom(String... patterns) {
        filter = filter.and(anyMatches(patterns, this::anyFromMatchesPattern));
    }

    private Predicate<Message> anyFromMatchesPattern(String pattern) {
        return m -> doesAddresMatchPattern(getFrom(m), pattern);
    }

    private List<String> getFrom(Message message) {
        try {
            return stream(message.getFrom())
                    .map(Address::toString)
                    .collect(toList());
        } catch (MessagingException ex) {
            throw new EmailBackendException(ex);
        }
    }

    @Keyword("Filter messages with recipient address")
    @Parameters(min = 1)
    public void filterRecipient(String... patterns) {
        filter = filter.and(anyMatches(patterns, this::anyRecipientMatchsPattern));
    }

    private Predicate<Message> anyRecipientMatchsPattern(String pattern) {
        return m -> doesAddresMatchPattern(getAllRecipients(m), pattern);
    }

    private List<String> getAllRecipients(Message message) {
        try {
            return stream(message.getAllRecipients())
                    .map(Address::toString)
                    .collect(toList());
        } catch (MessagingException ex) {
            throw new EmailBackendException(ex);
        }
    }

    @Keyword("Filter messages with to address")
    @Parameters(min = 1)
    public void filterTo(String... patterns) {
        filter = filter.and(anyMatches(patterns, this::anyToMatchesPattern));
    }

    private Predicate<Message> anyToMatchesPattern(String pattern) {
        return m -> doesAddresMatchPattern(getRecipients(m, TO), pattern);
    }

    @Keyword("Filter messages with cc address")
    @Parameters(min = 1)
    public void filterCc(String... patterns) {
        filter = filter.and(anyMatches(patterns, this::anyCcMatchesPattern));
    }

    private Predicate<Message> anyCcMatchesPattern(String pattern) {
        return m -> doesAddresMatchPattern(getRecipients(m, CC), pattern);
    }

    @Keyword("Filter messages with bcc address")
    @Parameters(min = 1)
    public void filterBcc(String... patterns) {
        filter = filter.and(anyMatches(patterns, this::anyBccMatchesPattern));
    }

    private Predicate<Message> anyBccMatchesPattern(String pattern) {
        return m -> doesAddresMatchPattern(getRecipients(m, BCC), pattern);
    }

    private List<String> getRecipients(Message message, RecipientType type) {
        try {
            return stream(message.getRecipients(type))
                    .map(Address::toString)
                    .collect(toList());
        } catch (MessagingException ex) {
            throw new EmailBackendException(ex);
        }
    }

    private static boolean doesAddresMatchPattern(List<String> list, String pattern) {
        pattern = pattern.toLowerCase();
        for (String item : list)
            if (item.toLowerCase().contains(pattern))
                return true;
        return false;
    }

    @Keyword("Filter messages with subject")
    @Parameters(min = 1)
    public void filterSubject(String... patterns) {
        filter = filter.and(anyMatches(patterns, this::anySubjectMatchesPattern));
    }

    private Predicate<Message> anySubjectMatchesPattern(String pattern) {
        return m -> doesSubjectMatchPattern(getSubject(m), pattern);
    }

    private String getSubject(Message message) {
        try {
            return message.getSubject();
        } catch (MessagingException ex) {
            throw new EmailBackendException(ex);
        }
    }

    private static boolean doesSubjectMatchPattern(String subject, String pattern) {
        subject = subject.toLowerCase();
        pattern = pattern.toLowerCase();
        return subject.contains(pattern);
    }

    private static Predicate<Message> anyMatches(String[] patterns, Function<String, Predicate<Message>> func) {
        Predicate<Message> condition = func.apply(patterns[0]);
        for (int i = 1; i < patterns.length; ++i)
            condition = condition.or(func.apply(patterns[i].toLowerCase()));
        return condition;
    }
}
