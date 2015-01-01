package org.mazarineblue.mail.keywords;

import java.util.Properties;
import org.mazarineblue.keyworddriven.Keyword;
import org.mazarineblue.keyworddriven.exceptions.InstructionException;
import org.mazarineblue.keyworddriven.exceptions.KeywordConflictException;
import org.mazarineblue.keyworddriven.librarymanager.Library;
import org.mazarineblue.mail.ServerProfile;

/**
 *
 * @author Mandy Tak {@literal <mandy.tak@MazarineBlue.org>}
 */
public class ServerProfileLibrary extends Library {

    private ServerProfile profile;

    public ServerProfileLibrary(String identifier) throws KeywordConflictException {
        super("org.mazarineblue.mail");
        profile = new ServerProfile(identifier);
    }

    @Override
    protected void setup() {

    }

    @Override
    protected void teardown() {
    }

    @Keyword("Server")
    public void setServer(String string) {
        if (string == null || string.isEmpty())
            throw new InstructionException("The server string is empty.");
        determineHostAndPort(string);
    }

    private void determineHostAndPort(String string) {
        String host;
        int port;
        try {
            String[] arr = string.split(":");
            host = arr[0];
            if (arr.length > 2)
                throw new InstructionException("The specified server is in the wrong format");
            port = arr.length > 1 ? Integer.parseInt(arr[1]) : ServerProfile.defaultPort;
            if (host.isEmpty())
                throw new InstructionException("No host is specified");
        } catch (NumberFormatException ex) {
            throw new InstructionException("Port number excepetion", ex);
        }
        profile.setHost(host);
        profile.setPort(port);
    }

    @Keyword("SSL")
    public void setSSL(String bool) {
        profile.setSsl(booleanConvertor(bool));
    }

    @Keyword("StartTLS")
    public void setStartTLS(String bool) {
        profile.setStartTLS(booleanConvertor(bool));
    }

    public void setProtocol(String protocol) {
        profile.setProtocol(protocol);
    }

    public boolean booleanConvertor(String bool) {
        switch (bool) {
            case "yes":
            case "true":
                return true;
            case "no":
            case "false":
                return false;
            default:
                throw new InstructionException("The specified input should be yes/true or no/false.");
        }
    }

    public String getHost() {
        return profile.getHost();
    }

    public String getIdentifier() {
        return profile.getIdentifier();
    }

    public Properties getProperties() {
        return profile.getProperties();
    }

    public String getStartTLSKey(String protocol) {
        return "mail." + protocol + ".starttls.enable";
    }

    public String getProtocolKey() {
        return "mail.store.protocol";
    }

    public String getPortKey(String protocol) {
        return "mail." + protocol + ".port";
    }

    public String getHostKey(String protocol) {
        return "mail." + protocol + ".host";

    }

    public String getSSLKey(String protocol) {
        return "mail." + protocol + ".ssl";
    }

    public String getAuthKey(String protocol) {
        return "mail." + protocol + ".auth";
    }
}
