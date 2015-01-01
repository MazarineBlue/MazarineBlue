package org.mazarineblue.mail.keywords;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.search.SearchTerm;
import org.mazarineblue.keyworddriven.Keyword;
import org.mazarineblue.keyworddriven.exceptions.ConflictException;
import org.mazarineblue.keyworddriven.exceptions.InstructionException;
import org.mazarineblue.keyworddriven.exceptions.KeywordConflictException;
import org.mazarineblue.keyworddriven.exceptions.SentenceConflictException;
import org.mazarineblue.keyworddriven.librarymanager.Library;

/**
 *
 * @author Mandy Tak {@literal <mandy.tak@MazarineBlue.org>}
 */
public class MailLibrary extends Library {

    private final long timeout = 30000;
    private final Map<String, ServerProfileLibrary> serverProfiles = new HashMap<>(4);
    private final Map<String, MailFilterLibrary> mailFilters = new HashMap<>(4);
    private final Map<String, Store> servers = new HashMap<>(4);
    private final Map<String, Session> sessions = new HashMap<>(4);
    private final Map<String, Folder> mailboxes = new HashMap<>(4);
    private ServerProfileLibrary activeServerProfileLibrary;
    private MailFilterLibrary activeMailFilterLibrary;
    private ComposeMailLibrary activeComposeMailLibrary;
    private OpenMostRecentMailLibrary activeMostRecentMailLibrary;

    public MailLibrary() throws KeywordConflictException {
        super("org.mazarineblue.mail");
    }

    @Override
    protected void setup() {
    }

    @Override
    protected void teardown() {
    }

    public boolean isConnectedToMailServer(String mailServer) {
        Store s = servers.get(mailServer);
        if (s == null)
            return false;

        return s.isConnected();
    }

    public OpenMostRecentMailLibrary getActiveMostRecentMailLibrary() {
        return activeMostRecentMailLibrary;
    }

    public boolean containsMailServerProfile(String mailServerProfile) {
        return serverProfiles.containsKey(mailServerProfile);
    }

    public boolean isActiveServerProfile(String mailServerProfile) {
        if (activeServerProfileLibrary == null)
            return false;

        return (activeServerProfileLibrary.getIdentifier().equals(mailServerProfile));
    }

    public boolean containsSMTPMailSession(String mailServerProfile) {
        return sessions.containsKey(mailServerProfile);
    }

    public boolean containsMailFilterProfile(String mailFilterProfile) {
        return mailFilters.containsKey(mailFilterProfile);
    }

    public boolean isActiveMailFilter(String mailFilter) {
        if (activeMailFilterLibrary == null)
            return false;
        return (activeMailFilterLibrary.getIdentifier().equals(mailFilter));
    }

    public boolean isMailboxOpen(String mailbox) {
        Folder b = mailboxes.get(mailbox);
        return b == null ? false : b.isOpen();
    }

    @Keyword("Mail server profile")
    public void mailServerProfile(String identifier) {
        if (identifier == null || identifier.isEmpty())
            throw new InstructionException("The server profile identifier is invalid.");
        if (serverProfiles.containsKey(identifier))
            throw new InstructionException("The server profile identifier is already used.");
        if (activeServerProfileLibrary != null)
            throw new InstructionException("Instruction cannot be called twice");

        try {
            activeServerProfileLibrary = new ServerProfileLibrary(identifier);
            executor().libraries().register(activeServerProfileLibrary);
        } catch (KeywordConflictException | SentenceConflictException ex) {
            throw new InstructionException("An error occured while creating and registering the server profile", ex);
        }
    }

    @Keyword("End mail server profile")
    public void endMailServerProfile() {
        if (activeServerProfileLibrary == null)
            throw new InstructionException("There is no active mail server profile to end");

        String identifier = activeServerProfileLibrary.getIdentifier();
        serverProfiles.put(identifier, activeServerProfileLibrary);
        executor().libraries().unregister(activeServerProfileLibrary);
        activeServerProfileLibrary = null;
    }

    @Keyword("Connect to imap mail server")
    public void connectToImapMailServer(String identifier, String profilesIdentifier, String username, String password) throws InstructionException {
        Session emailSession = getEmailSession(profilesIdentifier, username, password, "imap");
        ServerProfileLibrary serverProfile = serverProfiles.get(profilesIdentifier);
        Properties p = serverProfile.getProperties();
        String protocol = p.getProperty("mail.store.protocol");

        try {
            Store store = emailSession.getStore(protocol);
            store.connect(serverProfile.getHost(), username, password);
            servers.put(identifier, store);
        } catch (NoSuchProviderException ex) {
            throw new InstructionException("Cannot retrieve the email store using protocol: " + protocol, ex);
        } catch (MessagingException ex) {
            throw new InstructionException("Cannot connect to the mail server with host: " + serverProfile.getHost(), ex);
        }
    }

    private Session getEmailSession(String profilesIdentifier, String username, String password, String protocol) {
        if (profilesIdentifier == null || profilesIdentifier.isEmpty() || serverProfiles.containsKey(profilesIdentifier) == false)
            throw new InstructionException("The mailServerProfile identifier is invalid");
        if (username == null || username.isEmpty())
            throw new InstructionException("No username is specified.");
        if (password == null || password.isEmpty())
            throw new InstructionException("No password is specified.");

        ServerProfileLibrary serverProfile = serverProfiles.get(profilesIdentifier);
        serverProfile.setProtocol(protocol);
        if (serverProfile == null)
            throw new InstructionException("Developer issue: expected the mailServerProfile: " + profilesIdentifier);

        return createEmailSession(serverProfile, username, password);
    }

    private Session createEmailSession(ServerProfileLibrary serverProfile, String username, String password) {
        System.setProperty("mail.mime.decodetext.strict", "false");
        Session emailSession = null;
        emailSession = Session.getInstance(serverProfile.getProperties(), new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        emailSession.setDebug(false);
        return emailSession;
    }

    @Keyword("Connect to smtp mail server")
    public void connectToSMTPMailServer(String identifier, String profilesIdentifier, String username, String password) throws InstructionException {
        Session emailSession = getEmailSession(profilesIdentifier, username, password, "smtp");
        sessions.put(identifier, emailSession);
    }

    @Keyword("Close connection to imap mail server")
    public void closeConnectionToImapMailServer(String identifier) {
        if (identifier == null || identifier.isEmpty())
            throw new InstructionException("The identifier is invalid");
        if (servers.containsKey(identifier) == false)
            throw new InstructionException("No account opened with identifier: " + identifier);
        if (servers.containsKey(identifier)) {
            closeConnection(identifier);
        }
    }

    private void closeConnection(String identifier) {
        Store mailServerStore = servers.get(identifier);
        if (mailServerStore == null)
            throw new InstructionException("Developer issue: expected the mailStore object with identifier: " + identifier);

        try {
            mailServerStore.close();
        } catch (MessagingException ex) {
            throw new InstructionException("Cannot close connection to mailserver", ex);
        }
    }

    @Keyword("Open mailbox")
    public void openMailbox(String identifier, String serverIdentifier, String mailbox) {
        if (identifier == null || identifier.isEmpty())
            throw new InstructionException("The identifier for the mailbox is invalid.");
        if (serverIdentifier == null || serverIdentifier.isEmpty())
            throw new InstructionException("The server identifier is invalid.");
        if (mailbox == null || mailbox.isEmpty())
            throw new InstructionException("The mailbox name is invalid.");
        if (servers.containsKey(serverIdentifier) == false)
            throw new InstructionException("Please call connect to mail server first using the identifier: " + serverIdentifier);

        openMailFolder(identifier, serverIdentifier, mailbox);
    }

    private void openMailFolder(String identifier, String serverIdentifier, String mailbox) {
        Store mailStore = servers.get(serverIdentifier);
        if (mailStore == null)
            throw new InstructionException("Developer issue: expected the mailStore with identifier: " + serverIdentifier);

        Folder folder = null;
        try {
            folder = mailStore.getFolder(mailbox);
            folder.open(Folder.READ_ONLY);
        } catch (MessagingException ex) {
            throw new InstructionException("Could not find or open mailbox: " + mailbox, ex);
        }
        mailboxes.put(identifier, folder);
    }

    @Keyword("Close mailbox")
    public void closeMailBox(String mailboxIdentifier) {
        if (mailboxIdentifier == null || mailboxIdentifier.isEmpty())
            throw new InstructionException("The mailboxIdentifier is invalid.");
        if (mailboxes.containsKey(mailboxIdentifier) == false)
            throw new InstructionException("No mailbox found with identifier: " + mailboxIdentifier);
        closeMailFolder(mailboxIdentifier);
    }

    private void closeMailFolder(String mailboxIdentifier) {
        Folder folder = mailboxes.get(mailboxIdentifier);
        if (folder == null)
            throw new InstructionException("Developer issue: expected the mailbox with identifier: " + mailboxIdentifier);
        try {
            folder.close(false);
        } catch (MessagingException ex) {
            throw new InstructionException("Cannot close the mailbox with identifier: " + " " + mailboxIdentifier, ex);
        }
    }

    @Keyword("Mail filter")
    public void mailFilter(String identifier) {
        if (identifier == null || identifier.isEmpty())
            throw new InstructionException("The mail filter identifier is invalid.");
        if (mailFilters.containsKey(identifier))
            throw new InstructionException("The mail filter identifier is already used.");
        if (activeMailFilterLibrary != null)
            throw new InstructionException("Instruction can not be called twice");

        try {
            activeMailFilterLibrary = new MailFilterLibrary(identifier);
            executor().libraries().register(activeMailFilterLibrary);
        } catch (KeywordConflictException | SentenceConflictException ex) {
            throw new InstructionException("An error occured while creating and registering the mail filter", ex);
        }

    }

    @Keyword("End mail filter")
    public void endMailFilter() {
        if (activeMailFilterLibrary == null)
            throw new InstructionException("There is no active mail filter to end");

        String identifier = activeMailFilterLibrary.getIdentifier();
        mailFilters.put(identifier, activeMailFilterLibrary);
        executor().libraries().unregister(activeMailFilterLibrary);
        activeMailFilterLibrary = null;
    }

    @Keyword("Open most recent mail")
    public void openMostRecentMail(String mailboxIdentifier, String mailFilterIdentifier) {
        try {
            if (mailboxIdentifier == null || mailboxIdentifier.isEmpty())
                throw new InstructionException("The mailboxIdentifier is invalid.");
            if (mailboxes.containsKey(mailboxIdentifier) == false)
                throw new InstructionException("No mailbox found with identifier: " + mailboxIdentifier);
            if (activeMostRecentMailLibrary != null)
                throw new InstructionException("Instruction cannot be called twice");

            Folder folder = getFolder(mailboxIdentifier);
            MailFilterLibrary filter = getFilterLibrary(mailFilterIdentifier);
            SearchTerm searchTerm = filter == null ? null : filter.getSearchCondition();
            Message message = getMail(folder, searchTerm);

            activeMostRecentMailLibrary = new OpenMostRecentMailLibrary(message);
            executor().libraries().register(activeMostRecentMailLibrary);
        } catch (MessageNotFoundException ex) {
            throw new InstructionException(ex);
        } catch (InterruptedException ex) {
            throw new InstructionException("Function interuppted", ex);
        } catch (ConflictException ex) {
            throw new InstructionException(ex);
        }
    }

    private Folder getFolder(String mailboxIdentifier) {
        Folder folder = mailboxes.get(mailboxIdentifier);
        if (folder != null)
            return folder;
        throw new InstructionException("Developer issue: expected the mailbox with identifier: " + mailboxIdentifier);
    }

    private MailFilterLibrary getFilterLibrary(String mailFilterIdentifier) {
        if (mailFilterIdentifier == null || mailFilterIdentifier.isEmpty())
            return null;
        else if (mailFilters.containsKey(mailFilterIdentifier) == false)
            throw new InstructionException("No mail filter found with identifier: " + mailFilterIdentifier);
        return mailFilters.get(mailFilterIdentifier);
    }

    private Message getMail(Folder folder, SearchTerm searchTerm) throws MessageNotFoundException, InterruptedException {
        return getMail(folder, System.currentTimeMillis() + timeout, searchTerm);
    }

    private Message getMail(Folder folder, long endTime, SearchTerm searchTerm) throws MessageNotFoundException, InterruptedException {
        if (searchTerm == null)
            return getMail(folder, endTime);

        FetchMail fetchMail = new FetchMail() {
            @Override
            protected Message fetchMailInterval(Folder folder) throws MessagingException {
                Message[] messages = folder.search(searchTerm);
                if (messages.length > 0)
                    return messages[messages.length - 1];
                return null;
            }
        };
        return fetchMail.fetchMail(folder, endTime);
    }

    private abstract class FetchMail {

        private Message fetchMail(Folder folder, long endTime) throws MessageNotFoundException, InterruptedException {
            try {
                long endIntervalTime = System.currentTimeMillis() + timeout / 10;
                while (System.currentTimeMillis() < endTime) {
                    Message msg = fetchMailInterval(folder);
                    if (msg != null)
                        return msg;
                    if (System.currentTimeMillis() < endIntervalTime)
                        Thread.sleep(endIntervalTime - System.currentTimeMillis());
                }
            } catch (MessagingException ex) {
                throw new InstructionException("Developer issue: IO error while reading mail folder");
            }
            throw new MessageNotFoundException("Could not find the message in " + timeout + " ms");
        }

        protected abstract Message fetchMailInterval(Folder folder) throws MessagingException;
    }

    private Message getMail(Folder folder) throws MessageNotFoundException, InterruptedException {
        return getMail(folder, System.currentTimeMillis() + timeout);
    }

    private Message getMail(Folder folder, long endTime) throws MessageNotFoundException, InterruptedException {
        FetchMail fetchMail = new FetchMail() {
            @Override
            protected Message fetchMailInterval(Folder folder) throws MessagingException {
                if (folder.getMessageCount() > 0)
                    return folder.getMessage(folder.getMessageCount());
                return null;
            }
        };
        return fetchMail.fetchMail(folder, endTime);
    }

    @Keyword("Close most recent mail")
    public void closeMostRecentMail() {
        if (activeMostRecentMailLibrary == null)
            throw new InstructionException("There is no active recent mail to close");

        executor().libraries().unregister(activeMostRecentMailLibrary);
        activeMostRecentMailLibrary = null;
    }

    @Keyword("Compose mail")
    public void composeMail() {
        try {
            activeComposeMailLibrary = new ComposeMailLibrary();
            executor().libraries().register(activeComposeMailLibrary);
        } catch (KeywordConflictException | SentenceConflictException ex) {
            throw new InstructionException("An error occured while creating and registering the compose mail functionality", ex);
        }
    }

    @Keyword("Send mail")
    public void sendMail(String serverIdentifier) {
        if (serverIdentifier == null || serverIdentifier.isEmpty())
            throw new InstructionException("The server identifier is invalid.");
        Session session = sessions.get(serverIdentifier);
        if (session == null)
            throw new InstructionException("The server identifier does not correspond to a connection");

        composeMailAndSend(session);

        executor().libraries().unregister(activeComposeMailLibrary);
        activeComposeMailLibrary = null;
    }

    private void composeMailAndSend(Session session) {
        Message m = new MimeMessage(session);
        try {
            m.setFrom(new InternetAddress(activeComposeMailLibrary.getFrom()));
            for (String recipient : activeComposeMailLibrary.getTo())
                m.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            m.setSubject(activeComposeMailLibrary.getSubject());
            m.setText(activeComposeMailLibrary.getBody());
            Transport.send(m);
        } catch (MessagingException ex) {
            throw new InstructionException("Something went wrong when trying to send the email message", ex);
        }
    }
}
